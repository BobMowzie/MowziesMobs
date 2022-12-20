package com.bobmowzie.mowziesmobs.server.entity.sculptor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderPlatform;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Optional;

public class EntitySculptor extends MowzieGeckoEntity {
    public static int TEST_HEIGHT = 40;
    public static int TEST_RADIUS = 16;

    public static final AbilityType<EntitySculptor, StartTestAbility> START_TEST = new AbilityType<>("testStart", StartTestAbility::new);
    public static final AbilityType<EntitySculptor, EndTestAbility> END_TEST = new AbilityType<>("testEnd", EndTestAbility::new);

    private static final EntityDataAccessor<ItemStack> DESIRES = SynchedEntityData.defineId(EntitySculptor.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> IS_TRADING = SynchedEntityData.defineId(EntitySculptor.class, EntityDataSerializers.BOOLEAN);

    public boolean handLOpen = true;
    public boolean handROpen = true;
    private Player customer;
    private Player testingPlayer;
    private Optional<Double> prevPlayerVelY;
    private Optional<Vec3> prevPlayerPosition;
    private int ticksAcceleratingUpward;
    private boolean testing;

    private EntityPillar pillar;

    public EntitySculptor(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(2, new UseAbilityAI<>(this, START_TEST));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        Item tradeItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ConfigHandler.COMMON.MOBS.BARAKO.whichItem.get()));
        getEntityData().define(DESIRES, new ItemStack(tradeItem, ConfigHandler.COMMON.MOBS.BARAKO.howMany.get()));
        getEntityData().define(IS_TRADING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    public void setDesires(ItemStack stack) {
        getEntityData().set(DESIRES, stack);
    }

    public ItemStack getDesires() {
        return getEntityData().get(DESIRES);
    }

    public boolean fulfillDesire(Slot input) {
        ItemStack desires = getDesires();
        if (canPayFor(input.getItem(), desires)) {
            input.remove(desires.getCount());
            return true;
        }
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (testing && !level.isClientSide()) {
            if (testingPlayer == null) {
                sendAbilityMessage(END_TEST);
                prevPlayerPosition = Optional.empty();
                prevPlayerVelY = Optional.empty();
            }
            else {
                checkIfPlayerCheats();
            }
        }
    }

    private void checkIfPlayerCheats() {
        // Check if testing player is flying
        if (testingPlayer.getAbilities().flying) playerCheated(testingPlayer);
        if (!testingPlayer.isOnGround()) {
            double playerVelY = testingPlayer.getDeltaMovement().y();
            if (prevPlayerVelY != null && prevPlayerVelY.isPresent()) {
                double acceleration = playerVelY - prevPlayerVelY.get();
                if (acceleration >= 0.0) {
                    ticksAcceleratingUpward++;
                }
                else if (ticksAcceleratingUpward > 0) {
                    ticksAcceleratingUpward--;
                }
                if (ticksAcceleratingUpward > 5) playerCheated(testingPlayer);
            }
            prevPlayerVelY = Optional.of(playerVelY);
        }
        else {
            ticksAcceleratingUpward = 0;
            prevPlayerVelY = Optional.empty();
        }

        // Check if testing player teleported
        Vec3 currPosition = testingPlayer.position();
        if (prevPlayerPosition != null && prevPlayerPosition.isPresent()) {
            if (currPosition.distanceTo(prevPlayerPosition.get()) > 3.0) {
                playerCheated(testingPlayer);
            }
        }
        prevPlayerPosition = Optional.of(currPosition);
    }

    public void playerCheated(Player player) {
        if (player == testingPlayer) {
            sendAbilityMessage(END_TEST);
        }
    }

    public void setTrading(boolean trading) {
        entityData.set(IS_TRADING, trading);
    }

    public boolean isTrading() {
        return entityData.get(IS_TRADING);
    }

    public Player getCustomer() {
        return customer;
    }

    public void setCustomer(Player customer) {
        setTrading(customer != null);
        this.customer = customer;
    }

    public void openGUI(Player playerEntity) {
        setCustomer(playerEntity);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.level.isClientSide && getTarget() == null && isAlive()) {
            playerEntity.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                    return new ContainerSculptorTrade(id, EntitySculptor.this, playerInventory);
                }

                @Override
                public Component getDisplayName() {
                    return EntitySculptor.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (canTradeWith(player) && getTarget() == null && isAlive()) {
            openGUI(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public boolean canTradeWith(Player player) {
        return !isTrading() && !(getHealth() <= 0) && !testing;
    }

    public boolean doesItemSatisfyDesire(ItemStack stack) {
        return canPayFor(stack, getDesires());
    }

    private static boolean canPayFor(ItemStack stack, ItemStack worth) {
        return stack.getItem() == worth.getItem() && stack.getCount() >= worth.getCount();
    }

    public EntityPillar getPillar() {
        return pillar;
    }

    private <ENTITY extends IAnimatable> void instructionListener(CustomInstructionKeyframeEvent<ENTITY> event) {
        if (event.instructions.contains("closeHandR")) {
            handROpen = false;
        }
        if (event.instructions.contains("closeHandL")) {
            handLOpen = false;
        }
        if (event.instructions.contains("openHandR")) {
            handROpen = true;
        }
        if (event.instructions.contains("openHandL")) {
            handLOpen = true;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        controller.registerCustomInstructionListener(this::instructionListener);
    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[] {START_TEST, END_TEST};
    }

    public static class StartTestAbility extends Ability<EntitySculptor> {
        private static int MAX_RANGE_TO_GROUND = 12;

        private BlockPos spawnPillarPos;
        private BlockState spawnPillarBlock;
        private EntityPillar pillar;

        public StartTestAbility(AbilityType<EntitySculptor, StartTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 18),
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
            });
        }

        @Override
        public boolean tryAbility() {
            Vec3 from = getUser().position();
            Vec3 to = from.subtract(0, MAX_RANGE_TO_GROUND, 0);
            BlockHitResult result = getUser().level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, getUser()));
            if (result.getType() != HitResult.Type.MISS) {
                this.spawnPillarPos = result.getBlockPos();
                this.spawnPillarBlock = getUser().level.getBlockState(spawnPillarPos);
                if (result.getDirection() != Direction.UP) {
                    BlockState blockAbove = getUser().level.getBlockState(spawnPillarPos.above());
                    if (blockAbove.isSuffocating(getUser().level, spawnPillarPos.above()) || blockAbove.isAir())
                        return false;
                }
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            playAnimation("testStart", false);
            getUser().testing = true;
            getUser().testingPlayer = getUser().getCustomer();
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE && spawnPillarPos != null) {
                if (spawnPillarBlock == null || !EffectGeomancy.isBlockDiggable(spawnPillarBlock)) spawnPillarBlock = Blocks.STONE.defaultBlockState();
                pillar = new EntityPillar(EntityHandler.PILLAR.get(), getUser().level, getUser(), Blocks.STONE.defaultBlockState(), spawnPillarPos);
                pillar.setTier(EntityGeomancyBase.GeomancyTier.SMALL);
                pillar.setPos(spawnPillarPos.getX() + 0.5F, spawnPillarPos.getY() + 1, spawnPillarPos.getZ() + 0.5F);
                pillar.setDoRemoveTimer(false);
                if (pillar.checkCanSpawn()) {
                    getUser().level.addFreshEntity(pillar);
                }
                getUser().pillar = pillar;

                int numStartBoulders = rand.nextInt(2, 4);
                for (int i = 0; i < numStartBoulders; i++) {
                    Vec3 spawnBoulderPos = pillar.position().add(new Vec3(rand.nextFloat(5, 10), 0, 0).yRot(rand.nextFloat((float) (2f * Math.PI))));
                    EntityBoulderPlatform boulderPlatform = new EntityBoulderPlatform(EntityHandler.BOULDER_PLATFORM.get(), getUser().getLevel(), getUser(), Blocks.STONE.defaultBlockState(), BlockPos.ZERO, EntityGeomancyBase.GeomancyTier.MEDIUM);
                    boulderPlatform.setPos(spawnBoulderPos.add(0, 1, 0));
                    boulderPlatform.setMainPath();
                    getUser().getLevel().addFreshEntity(boulderPlatform);
                }
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                /*if (!getUser().getLevel().isClientSide()) {
                    int k = getTicksInSection();
                    if (k < TEST_HEIGHT + 3) {
                        for (int i = -TEST_RADIUS; i < TEST_RADIUS; i++) {
                            for (int j = -TEST_RADIUS; j < TEST_RADIUS; j++) {
                                if (new Vec2(i, j).length() < TEST_RADIUS) {
                                    BlockPos pos = getUser().blockPosition().east(i).north(j).above(k);
                                    EntityBlockSwapper swapper = new EntityBlockSwapper.EntityBlockSwapperSculptor(EntityHandler.BLOCK_SWAPPER.get(), getUser().getLevel(), pos, Blocks.AIR.defaultBlockState(), 200, false, false);
                                    getUser().getLevel().addFreshEntity(swapper);
                                }
                            }
                        }
                    }
                }*/
                if (getTicksInSection() == 0 && !getUser().getLevel().isClientSide()) {
                    EntityBlockSwapper.EntityBlockSwapperSculptor swapper = new EntityBlockSwapper.EntityBlockSwapperSculptor(EntityHandler.BLOCK_SWAPPER_SCULPTOR.get(), getUser().getLevel(), getUser().blockPosition(), Blocks.AIR.defaultBlockState(), 60, false, false);
                    getUser().getLevel().addFreshEntity(swapper);
                }
                if (pillar != null && pillar.getHeight() > TEST_HEIGHT) {
                    nextSection();
                }
            }
        }

        @Override
        public void end() {
            super.end();
            if (pillar != null) pillar.stopRising();
        }
    }

    public static class EndTestAbility extends Ability<EntitySculptor> {

        public EndTestAbility(AbilityType<EntitySculptor, EndTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, new AbilitySection[] {
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 20)
            });
        }

        @Override
        public boolean tryAbility() {
            return getUser().pillar != null;
        }

        @Override
        public void start() {
            super.start();
            playAnimation("testStart", false);
            if (getUser().pillar != null) getUser().pillar.startFalling();
        }

        @Override
        public void tick() {
            super.tick();
            if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (getUser().pillar == null || getUser().pillar.isRemoved()) nextSection();
            }
        }

        @Override
        public void end() {
            super.end();
            if (getUser() != null) {
                getUser().pillar = null;
                getUser().testingPlayer = null;
                getUser().testing = false;
            }
        }
    }
}
