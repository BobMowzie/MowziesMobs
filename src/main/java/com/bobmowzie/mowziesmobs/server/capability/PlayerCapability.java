package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoFirstPersonRenderer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthTalisman;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.power.PowerGeomancy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;

public class PlayerCapability {

    public interface IPlayerCapability {
        Tag writeNBT();

        void readNBT(Tag nbt);

        Power[] getPowers();

        void tick(TickEvent.PlayerTickEvent event);

        void addedToWorld(EntityJoinWorldEvent event);

        boolean isVerticalSwing();

        void setVerticalSwing(boolean verticalSwing);

        int getUntilSunstrike();

        void setUntilSunstrike(int untilSunstrike);

        int getUntilAxeSwing();

        void setUntilAxeSwing(int untilAxeSwing);

        void setAxeCanAttack(boolean axeCanAttack);

        boolean getAxeCanAttack();

        boolean isMouseRightDown();

        void setMouseRightDown(boolean mouseRightDown);

        boolean isMouseLeftDown();

        void setMouseLeftDown(boolean mouseLeftDown);

        boolean isPrevSneaking();

        void setPrevSneaking(boolean prevSneaking);

        int getTribeCircleTick();

        void setTribeCircleTick(int tribeCircleTick);

        List<EntityBarakoanToPlayer> getTribePack();

        void setTribePack(List<EntityBarakoanToPlayer> tribePack);

        int getTribePackRadius();

        void setTribePackRadius(int tribePackRadius);

        int getPackSize();

        Vec3 getPrevMotion();

        void removePackMember(EntityBarakoanToPlayer tribePlayer);

        void addPackMember(EntityBarakoanToPlayer tribePlayer);

        PowerGeomancy getGeomancy();

        void setUsingSolarBeam(boolean b);

        boolean getUsingSolarBeam();

        float getPrevCooledAttackStrength();

        void setPrevCooledAttackStrength(float cooledAttackStrength);

        @OnlyIn(Dist.CLIENT)
        GeckoPlayer.GeckoPlayerThirdPerson getGeckoPlayer();
    }

    public static class PlayerCapabilityImp implements IPlayerCapability {
        public boolean verticalSwing = false;
        public int untilSunstrike = 0;
        public int untilAxeSwing = 0;
        private int prevTime;
        private int time;
        public boolean mouseRightDown = false;
        public boolean mouseLeftDown = false;
        public boolean prevSneaking;
        private float prevCooledAttackStrength;

        public int tribeCircleTick;
        public List<EntityBarakoanToPlayer> tribePack = new ArrayList<>();
        public int tribePackRadius = 3;

        @OnlyIn(Dist.CLIENT)
        private GeckoPlayer.GeckoPlayerThirdPerson geckoPlayer;

        public boolean isVerticalSwing() {
            return verticalSwing;
        }

        public void setVerticalSwing(boolean verticalSwing) {
            this.verticalSwing = verticalSwing;
        }

        public int getUntilSunstrike() {
            return untilSunstrike;
        }

        public void setUntilSunstrike(int untilSunstrike) {
            this.untilSunstrike = untilSunstrike;
        }

        public int getUntilAxeSwing() {
            return untilAxeSwing;
        }

        public void setUntilAxeSwing(int untilAxeSwing) {
            this.untilAxeSwing = untilAxeSwing;
        }

        public void setAxeCanAttack(boolean axeCanAttack) {
            this.axeCanAttack = axeCanAttack;
        }

        public boolean getAxeCanAttack() {
            return axeCanAttack;
        }

        public boolean isMouseRightDown() {
            return mouseRightDown;
        }

        public void setMouseRightDown(boolean mouseRightDown) {
            this.mouseRightDown = mouseRightDown;
        }

        public boolean isMouseLeftDown() {
            return mouseLeftDown;
        }

        public void setMouseLeftDown(boolean mouseLeftDown) {
            this.mouseLeftDown = mouseLeftDown;
        }

        public boolean isPrevSneaking() {
            return prevSneaking;
        }

        public void setPrevSneaking(boolean prevSneaking) {
            this.prevSneaking = prevSneaking;
        }

        public int getTribeCircleTick() {
            return tribeCircleTick;
        }

        public void setTribeCircleTick(int tribeCircleTick) {
            this.tribeCircleTick = tribeCircleTick;
        }

        public List<EntityBarakoanToPlayer> getTribePack() {
            return tribePack;
        }

        public void setTribePack(List<EntityBarakoanToPlayer> tribePack) {
            this.tribePack = tribePack;
        }

        public int getTribePackRadius() {
            return tribePackRadius;
        }

        public void setTribePackRadius(int tribePackRadius) {
            this.tribePackRadius = tribePackRadius;
        }

        public Vec3 getPrevMotion() {
            return prevMotion;
        }

        public PowerGeomancy getGeomancy() {
            return geomancy;
        }

        public void setUsingSolarBeam(boolean b) { this.usingSolarBeam = b; }

        public boolean getUsingSolarBeam() { return this.usingSolarBeam; }

        @Override
        public float getPrevCooledAttackStrength() {
            return prevCooledAttackStrength;
        }

        @Override
        public void setPrevCooledAttackStrength(float cooledAttackStrength) {
            prevCooledAttackStrength = cooledAttackStrength;
        }

        @OnlyIn(Dist.CLIENT)
        public GeckoPlayer.GeckoPlayerThirdPerson getGeckoPlayer() {
            return geckoPlayer;
        }

        private boolean usingSolarBeam;

        public boolean axeCanAttack;

        public Vec3 prevMotion;

        public PowerGeomancy geomancy = new PowerGeomancy(this);
        public Power[] powers = new Power[]{geomancy};

        @Override
        public void addedToWorld(EntityJoinWorldEvent event) {
            if (event.getWorld().isClientSide()) {
                Player player = (Player) event.getEntity();
                geckoPlayer = new GeckoPlayer.GeckoPlayerThirdPerson(player);
                if (event.getEntity() == Minecraft.getInstance().player) GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON = new GeckoPlayer.GeckoPlayerFirstPerson(player);
            }
        }

        public void tick(TickEvent.PlayerTickEvent event) {
            Player player = event.player;

            prevMotion = player.position().subtract(new Vec3(player.xo, player.yo, player.zo));
            prevTime = time;
            if (untilSunstrike > 0) {
                untilSunstrike--;
            }
            if (untilAxeSwing > 0) {
                untilAxeSwing--;
            }

            if (event.side == LogicalSide.SERVER) {
                for (ItemStack itemStack : event.player.inventory.items) {
                    if (itemStack.getItem() instanceof ItemEarthTalisman)
                        player.addEffect(new MobEffectInstance(EffectHandler.GEOMANCY, 20, 0, false, false));
                }
                if (player.getOffhandItem().getItem() instanceof ItemEarthTalisman)
                    player.addEffect(new MobEffectInstance(EffectHandler.GEOMANCY, 20, 0, false, false));

                List<EntityBarakoanToPlayer> pack = tribePack;
                float theta = (2 * (float) Math.PI / pack.size());
                for (int i = 0; i < pack.size(); i++) {
                    EntityBarakoanToPlayer barakoan = pack.get(i);
                    barakoan.index = i;
                    float distanceToPlayer = player.distanceTo(barakoan);
                    if (barakoan.getTarget() == null && barakoan.getAnimation() != EntityBarakoanToPlayer.DEACTIVATE_ANIMATION) {
                        if (distanceToPlayer > 4)
                            barakoan.getNavigation().moveTo(player.getX() + tribePackRadius * Mth.cos(theta * i), player.getY(), player.getZ() + tribePackRadius * Mth.sin(theta * i), 0.45);
                        else
                            barakoan.getNavigation().stop();
                        if (distanceToPlayer > 20 && player.isOnGround()) {
                            tryTeleportBarakoan(player, barakoan);
                        }
                    }
                }
            }

            Ability iceBreathAbility = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.ICE_BREATH_ABILITY);
            if (iceBreathAbility != null && !iceBreathAbility.isUsing()) {
                for (ItemStack stack : player.inventory.items) {
                    restoreIceCrystalStack(player, stack);
                }
                for (ItemStack stack : player.inventory.offhand) {
                    restoreIceCrystalStack(player, stack);
                }
            }

            useIceCrystalStack(player);

            if (event.side == LogicalSide.CLIENT) {
                if (Minecraft.getInstance().options.keyAttack.isDown() && !mouseLeftDown) {
                    mouseLeftDown = true;
                    MowziesMobs.NETWORK.sendToServer(new MessageLeftMouseDown());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onLeftMouseDown(player);
                    }
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null) {
                        for (Ability ability : abilityCapability.getAbilities()) {
                            ability.onLeftMouseDown(player);
                        }
                    }
                }
                if (Minecraft.getInstance().options.keyUse.isDown() && !mouseRightDown) {
                    mouseRightDown = true;
                    MowziesMobs.NETWORK.sendToServer(new MessageRightMouseDown());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onRightMouseDown(player);
                    }
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null) {
                        for (Ability ability : abilityCapability.getAbilities()) {
                            ability.onRightMouseDown(player);
                        }
                    }
                }
                if (!Minecraft.getInstance().options.keyAttack.isDown() && mouseLeftDown) {
                    mouseLeftDown = false;
                    MowziesMobs.NETWORK.sendToServer(new MessageLeftMouseUp());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onLeftMouseUp(player);
                    }
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null) {
                        for (Ability ability : abilityCapability.getAbilities()) {
                            ability.onLeftMouseUp(player);
                        }
                    }
                }
                if (!Minecraft.getInstance().options.keyUse.isDown() && mouseRightDown) {
                    mouseRightDown = false;
                    MowziesMobs.NETWORK.sendToServer(new MessageRightMouseUp());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onRightMouseUp(player);
                    }
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null) {
                        for (Ability ability : abilityCapability.getAbilities()) {
                            ability.onRightMouseUp(player);
                        }
                    }
                }
            }

            if (player.isShiftKeyDown() && !prevSneaking) {
                for (int i = 0; i < powers.length; i++) {
                    powers[i].onSneakDown(player);
                }
                AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                if (abilityCapability != null) {
                    for (Ability ability : abilityCapability.getAbilities()) {
                        ability.onSneakDown(player);
                    }
                }
            }
            else if (!player.isShiftKeyDown() && prevSneaking) {
                for (int i = 0; i < powers.length; i++) {
                    powers[i].onSneakUp(player);
                }
                AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                if (abilityCapability != null) {
                    for (Ability ability : abilityCapability.getAbilities()) {
                        ability.onSneakUp(player);
                    }
                }
            }
            prevSneaking = player.isShiftKeyDown();
        }

        private void restoreIceCrystalStack(Player entity, ItemStack stack) {
            if (stack.getItem() == ItemHandler.ICE_CRYSTAL) {
                if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get()) {
                    stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));
                }
            }
        }

        private void useIceCrystalStack(Player player) {
            ItemStack stack = player.getUseItem();
            if (stack.getItem() == ItemHandler.ICE_CRYSTAL) {
                Ability iceBreathAbility = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.ICE_BREATH_ABILITY);
                if (iceBreathAbility != null && iceBreathAbility.isUsing()) {
                    InteractionHand handIn = player.getUsedItemHand();
                    if (stack.getDamageValue() + 5 < stack.getMaxDamage()) {
                        stack.hurtAndBreak(5, player, p -> p.broadcastBreakEvent(handIn));
                    }
                    else {
                        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get()) {
                            stack.hurtAndBreak(5, player, p -> p.broadcastBreakEvent(handIn));
                        }
                        iceBreathAbility.end();
                    }
                }
            }
        }

        private void tryTeleportBarakoan(Player player, EntityBarakoanToPlayer barakoan) {
            int x = Mth.floor(player.getX()) - 2;
            int z = Mth.floor(player.getZ()) - 2;
            int y = Mth.floor(player.getBoundingBox().minY);

            for (int l = 0; l <= 4; ++l) {
                for (int i1 = 0; i1 <= 4; ++i1) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && barakoan.isTeleportFriendlyBlock(x, z, y, l, i1)) {
                        barakoan.moveTo((float) (x + l) + 0.5F, y, (float) (z + i1) + 0.5F, barakoan.yRot, barakoan.xRot);
                        barakoan.getNavigation().stop();
                        return;
                    }
                }
            }
        }

        public int getTick() {
            return time;
        }

        public void decrementTime() {
            time--;
        }

        public int getPackSize() {
            return tribePack.size();
        }

        public void removePackMember(EntityBarakoanToPlayer tribePlayer) {
            tribePack.remove(tribePlayer);
        }

        public void addPackMember(EntityBarakoanToPlayer tribePlayer) {
            tribePack.add(tribePlayer);
        }

        public Power[] getPowers() {
            return powers;
        }

        @Override
        public Tag writeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putInt("untilSunstrike", untilSunstrike);
            compound.putInt("untilAxeSwing", untilAxeSwing);
            compound.putInt("prevTime", prevTime);
            compound.putInt("time", time);
            return compound;
        }

        @Override
        public void readNBT(Tag nbt) {
            CompoundTag compound = (CompoundTag) nbt;
            untilSunstrike = compound.getInt("untilSunstrike");
            untilAxeSwing = compound.getInt("untilAxeSwing");
            prevTime = compound.getInt("prevTime");
            time = compound.getInt("time");
        }
    }

    public static class PlayerStorage implements Capability.IStorage<IPlayerCapability> {
        @Override
        public Tag writeNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side, Tag nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class PlayerProvider implements ICapabilitySerializable<Tag>
    {
        @CapabilityInject(IPlayerCapability.class)
        public static final Capability<IPlayerCapability> PLAYER_CAPABILITY = null;

        private final LazyOptional<IPlayerCapability> instance = LazyOptional.of(PLAYER_CAPABILITY::getDefaultInstance);

        @Override
        public Tag serializeNBT() {
            return PLAYER_CAPABILITY.getStorage().writeNBT(PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null);
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            PLAYER_CAPABILITY.getStorage().readNBT(PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null, nbt);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == PLAYER_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
