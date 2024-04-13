package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection.AbilitySectionDuration;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection.AbilitySectionInstant;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Random;

public class Ability<T extends LivingEntity> {
    private final AbilitySection[] sectionTrack;
    private final int cooldownMax;
    private final AbilityType<T, ? extends Ability> abilityType;
    private final T user;
    private final AbilityCapability.IAbilityCapability abilityCapability;

    private int ticksInUse;
    private int ticksInSection;
    private int currentSectionIndex;
    private boolean isUsing;
    private int cooldownTimer;

    protected Random rand;

    protected RawAnimation activeAnimation;

    public Ability(AbilityType<T, ? extends Ability> abilityType, T user, AbilitySection[] sectionTrack, int cooldownMax) {
        this.abilityType = abilityType;
        this.user = user;
        this.abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(user);
        this.sectionTrack = sectionTrack;
        this.cooldownMax = cooldownMax;
        this.rand = new Random();
    }

    public Ability(AbilityType<T, ? extends Ability> abilityType, T user, AbilitySection[] sectionTrack) {
        this(abilityType, user, sectionTrack, 0);
    }

    public void start() {
        if (!runsInBackground()) abilityCapability.setActiveAbility(this);
        ticksInUse = 0;
        ticksInSection = 0;
        currentSectionIndex = 0;
        isUsing = true;
        beginSection(getSectionTrack()[0]);
    }

    public void playAnimation(String animationName) {
        playAnimation(animationName, Animation.LoopType.DEFAULT);
    }

    public void playAnimation(String animationName, boolean shouldLoop) {
        playAnimation(animationName, shouldLoop ? Animation.LoopType.LOOP : Animation.LoopType.PLAY_ONCE);
    }

    public void playAnimation(String animationName, Animation.LoopType loopType) {
        if (getUser() instanceof MowzieGeckoEntity && getUser().level().isClientSide()) {
            MowzieGeckoEntity entity = (MowzieGeckoEntity) getUser();
            RawAnimation newActiveAnimation = RawAnimation.begin().then(animationName, loopType);
            activeAnimation = newActiveAnimation;
            MowzieAnimationController<MowzieGeckoEntity> controller = entity.getController();
            if (controller != null) {
                controller.playAnimation(entity, newActiveAnimation);
            }
        }
    }

    public void tick() {
        if (isUsing()) {
            if (getUser().isEffectiveAi() && !canContinueUsing()) AbilityHandler.INSTANCE.sendInterruptAbilityMessage(getUser(), this.abilityType);

            tickUsing();

            ticksInUse++;
            ticksInSection++;
            AbilitySection section = getCurrentSection();
            if (section instanceof AbilitySectionInstant) {
                nextSection();
            } else if (section instanceof AbilitySectionDuration) {
                AbilitySectionDuration sectionDuration = (AbilitySectionDuration) section;
                if (ticksInSection > sectionDuration.duration) nextSection();
            }
        }
        else {
            tickNotUsing();
            if (getCooldownTimer() > 0) cooldownTimer--;
        }
    }

    public void tickUsing() {

    }

    public void tickNotUsing() {

    }

    public void end() {
        ticksInUse = 0;
        ticksInSection = 0;
        isUsing = false;
        cooldownTimer = getMaxCooldown();
        currentSectionIndex = 0;
        if (!runsInBackground()) abilityCapability.setActiveAbility(null);
    }

    public void interrupt() {
        end();
    }

    public void complete() {
        end();
    }

    /**
     * Server-only check to see if the user can use this ability. Checked before packet is sent.
     * @return Whether or not the ability can be used
     */
    public boolean canUse() {
        if (getUser().hasEffect(EffectHandler.FROZEN.get())) return false;
        boolean toReturn = (!isUsing() || canCancelSelf()) && cooldownTimer == 0;
        if (!runsInBackground()) toReturn = toReturn && (abilityCapability.getActiveAbility() == null || canCancelActiveAbility() || abilityCapability.getActiveAbility().canBeCanceledByAbility(this));
        return toReturn;
    }

    /**
     * Both sides check and behavior when user tries to use this ability. Ability only starts if this returns true.
     * Called after packet is received.
     * @return Whether or not the ability try succeeded
     */
    public boolean tryAbility() {
        return true;
    }

    public boolean canCancelActiveAbility() {
        return false;
    }

    public Ability getActiveAbility() {
        AbilityCapability.IAbilityCapability capability = getAbilityCapability();
        if (capability == null) return null;
        return getAbilityCapability().getActiveAbility();
    }

    public boolean canCancelSelf() {
        return false;
    }

    public boolean canBeCanceledByAbility(Ability ability) {
        return false;
    }

    protected boolean canContinueUsing() {
        return !getUser().hasEffect(EffectHandler.FROZEN.get());
    }

    public boolean isUsing() {
        return isUsing;
    }

    public T getUser() {
        return user;
    }

    public Level getLevel() {
        return user.level();
    }

    public int getTicksInUse() {
        return ticksInUse;
    }

    public int getTicksInSection() {
        return ticksInSection;
    }

    public int getCooldownTimer() {
        return cooldownTimer;
    }

    public void nextSection() {
        jumpToSection(currentSectionIndex + 1);
    }

    public void jumpToSection(int sectionIndex) {
        endSection(getCurrentSection());
        currentSectionIndex = sectionIndex;
        ticksInSection = 0;
        if (currentSectionIndex >= getSectionTrack().length) {
            complete();
        }
        else {
            beginSection(getCurrentSection());
        }
    }

    protected void endSection(AbilitySection section) {

    }

    protected void beginSection(AbilitySection section) {

    }

    public AbilitySection getCurrentSection() {
        if (currentSectionIndex >= getSectionTrack().length) return null;
        return getSectionTrack()[currentSectionIndex];
    }

    public boolean damageInterrupts() {
        return false;
    }

    public void onTakeDamage(LivingHurtEvent event) {
        if (isUsing() && event.getResult() != Event.Result.DENY && event.getAmount() > 0.0 && damageInterrupts()) AbilityHandler.INSTANCE.sendInterruptAbilityMessage(getUser(), getAbilityType());
    }

    /**
     * Non-background abilities require no other non-background abilities running to run.
     * Only one non-background ability can run at once.
     * Background abilities can all run simultaneously
     * @return
     */
    public boolean runsInBackground() {
        return false;
    }

    /**
     * Unused for background abilities
     * @return
     */
    public boolean preventsAttacking() {
        return true;
    }

    /**
     * Unused for background abilities
     * @return
     */
    public boolean preventsBlockBreakingBuilding() {
        return true;
    }

    /**
     * Unused for background abilities
     * @return
     */
    public boolean preventsInteracting() {
        return true;
    }

    /**
     * Unused for background abilities
     * @return
     */
    public boolean preventsItemUse(ItemStack stack) {
        return true;
    }

    public AbilitySection[] getSectionTrack() {
        return sectionTrack;
    }

    public int getMaxCooldown() {
        return cooldownMax;
    }

    public AbilityCapability.IAbilityCapability getAbilityCapability() {
        return abilityCapability;
    }

    public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
        if (activeAnimation == null || activeAnimation.getAnimationStages().isEmpty())
            return PlayState.STOP;
        e.getController().setAnimation(activeAnimation);
        return PlayState.CONTINUE;
    }

    public void codeAnimations(MowzieGeoModel<? extends GeoEntity> model, float partialTick) {

    }

    public boolean isAnimating() {
        return isUsing();
    }

    public AbilityType<T, ? extends Ability> getAbilityType() {
        return abilityType;
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity player, double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(player, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double r) {
        return player.level().getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(r, r, r), e -> e != player && player.distanceTo(e) <= r);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return player.level().getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(dX, dY, dZ), e -> e != player && player.distanceTo(e) <= r);
    }

    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        if (isUsing()) {
            compound.putInt("ticks_in_use", ticksInUse);
            compound.putInt("ticks_in_section", ticksInSection);
            compound.putInt("current_section", currentSectionIndex);
        }
        else if (cooldownTimer > 0) {
            compound.putInt("cooldown_timer", cooldownTimer);
        }
        return compound;
    }

    public void readNBT(Tag nbt) {
        CompoundTag compound = (CompoundTag) nbt;
        isUsing = compound.contains("ticks_in_use");
        if (isUsing) {
            ticksInUse = compound.getInt("ticks_in_use");
            ticksInSection = compound.getInt("ticks_in_section");
            currentSectionIndex = compound.getInt("current_section");
        }
        else {
            cooldownTimer = compound.getInt("cooldown_timer");
        }
    }

    // Client events
    public void onRenderTick(TickEvent.RenderTickEvent event) {

    }
}
