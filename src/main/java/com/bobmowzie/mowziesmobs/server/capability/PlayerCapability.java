package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthTalisman;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.power.PowerGeomancy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;

public class PlayerCapability {
    public static final int SWING_COOLDOWN = 30;

    public interface IPlayerCapability {
        INBT writeNBT();

        void readNBT(INBT nbt);

        Power[] getPowers();

        public void tick(TickEvent.PlayerTickEvent event);

        public boolean isVerticalSwing();

        public void setVerticalSwing(boolean verticalSwing);

        public int getUntilSunstrike();

        public void setUntilSunstrike(int untilSunstrike);

        public int getUntilAxeSwing();

        public void setUntilAxeSwing(int untilAxeSwing);

        public boolean isMouseRightDown();

        public void setMouseRightDown(boolean mouseRightDown);

        public boolean isMouseLeftDown();

        public void setMouseLeftDown(boolean mouseLeftDown);

        public boolean isPrevSneaking();

        public void setPrevSneaking(boolean prevSneaking);

        public int getTribeCircleTick();

        public void setTribeCircleTick(int tribeCircleTick);

        public List<EntityBarakoanToPlayer> getTribePack();

        public void setTribePack(List<EntityBarakoanToPlayer> tribePack);

        public int getTribePackRadius();

        public void setTribePackRadius(int tribePackRadius);

        public EntityIceBreath getIcebreath();

        public void setIcebreath(EntityIceBreath icebreath);

        public boolean isUsingIceBreath();

        public void setUsingIceBreath(boolean usingIceBreath);

        public int getPackSize();
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

        public int tribeCircleTick;
        public List<EntityBarakoanToPlayer> tribePack = new ArrayList<>();
        public int tribePackRadius = 3;

        public EntityIceBreath icebreath;

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

        public EntityIceBreath getIcebreath() {
            return icebreath;
        }

        public void setIcebreath(EntityIceBreath icebreath) {
            this.icebreath = icebreath;
        }

        public boolean isUsingIceBreath() {
            return usingIceBreath;
        }

        public void setUsingIceBreath(boolean usingIceBreath) {
            this.usingIceBreath = usingIceBreath;
        }

        public boolean usingIceBreath;

        public PowerGeomancy geomancy = new PowerGeomancy(this);
        public Power[] powers = new Power[]{geomancy};

        public void tick(TickEvent.PlayerTickEvent event) {
            PlayerEntity player = event.player;

            prevTime = time;
            if (untilSunstrike > 0) {
                untilSunstrike--;
            }
            if (untilAxeSwing > 0) {
                untilAxeSwing--;
            }

            if (event.side == LogicalSide.SERVER) {
                for (ItemStack itemStack : event.player.inventory.mainInventory) {
                    if (itemStack.getItem() instanceof ItemEarthTalisman)
                        player.addPotionEffect(new EffectInstance(PotionHandler.GEOMANCY, 0, 0, false, false));
                }
                if (player.getHeldItemOffhand().getItem() instanceof ItemEarthTalisman)
                    player.addPotionEffect(new EffectInstance(PotionHandler.GEOMANCY, 0, 0, false, false));

                List<EntityBarakoanToPlayer> pack = tribePack;
                float theta = (2 * (float) Math.PI / pack.size());
                for (int i = 0; i < pack.size(); i++) {
                    EntityBarakoanToPlayer barakoan = pack.get(i);
                    barakoan.index = i;
                    if (barakoan.getAttackTarget() == null && barakoan.getAnimation() != EntityBarakoanToPlayer.DEACTIVATE_ANIMATION) {
                        barakoan.getNavigator().tryMoveToXYZ(player.posX + tribePackRadius * MathHelper.cos(theta * i), player.posY, player.posZ + tribePackRadius * MathHelper.sin(theta * i), 0.45);
                        if (player.getDistance(barakoan) > 20 && player.onGround) {
                            tryTeleportBarakoan(player, barakoan);
                        }
                    }
                }
            }

            if (!(player.getHeldItemMainhand().getItem() == ItemHandler.ICE_CRYSTAL || player.getHeldItemOffhand().getItem() == ItemHandler.ICE_CRYSTAL) && usingIceBreath && icebreath != null) {
                usingIceBreath = false;
                icebreath.remove();
            }

            if (!ConfigHandler.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (!usingIceBreath && stack.getItem() == ItemHandler.ICE_CRYSTAL)
                        stack.setDamage(Math.max(stack.getDamage() - 1, 0));
                }
                for (ItemStack stack : player.inventory.offHandInventory) {
                    if (!usingIceBreath && stack.getItem() == ItemHandler.ICE_CRYSTAL)
                        stack.setDamage(Math.max(stack.getDamage() - 1, 0));
                }
            }

            if (event.side == LogicalSide.CLIENT) {
                if (Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && !mouseLeftDown) {
                    mouseLeftDown = true;
                    MowziesMobs.NETWORK.sendToServer(new MessageLeftMouseDown());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onLeftMouseDown(player);
                    }
                }
                if (Minecraft.getInstance().gameSettings.keyBindUseItem.isKeyDown() && !mouseRightDown) {
                    mouseRightDown = true;
                    MowziesMobs.NETWORK.sendToServer(new MessageRightMouseDown());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onRightMouseDown(player);
                    }
                }
                if (!Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && mouseLeftDown) {
                    mouseLeftDown = false;
                    MowziesMobs.NETWORK.sendToServer(new MessageLeftMouseUp());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onLeftMouseUp(player);
                    }
                }
                if (!Minecraft.getInstance().gameSettings.keyBindUseItem.isKeyDown() && mouseRightDown) {
                    mouseRightDown = false;
                    MowziesMobs.NETWORK.sendToServer(new MessageRightMouseUp());
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onRightMouseUp(player);
                    }
                }
            }

            if (player.isSneaking() && !prevSneaking) {
                for (int i = 0; i < powers.length; i++) {
                    powers[i].onSneakDown(player);
                }
            }
            else if (!player.isSneaking() && prevSneaking) {
                for (int i = 0; i < powers.length; i++) {
                    powers[i].onSneakUp(player);
                }
            }
            prevSneaking = player.isSneaking();
        }

        private void tryTeleportBarakoan(PlayerEntity player, EntityBarakoanToPlayer barakoan) {
            int x = MathHelper.floor(player.posX) - 2;
            int z = MathHelper.floor(player.posZ) - 2;
            int y = MathHelper.floor(player.getBoundingBox().minY);

            for (int l = 0; l <= 4; ++l) {
                for (int i1 = 0; i1 <= 4; ++i1) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && barakoan.isTeleportFriendlyBlock(x, z, y, l, i1)) {
                        barakoan.setLocationAndAngles((double) ((float) (x + l) + 0.5F), (double) y, (double) ((float) (z + i1) + 0.5F), barakoan.rotationYaw, barakoan.rotationPitch);
                        barakoan.getNavigator().clearPath();
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
        public INBT writeNBT() {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("untilSunstrike", untilSunstrike);
            compound.putInt("untilAxeSwing", untilAxeSwing);
            compound.putInt("prevTime", prevTime);
            compound.putInt("time", time);
            return compound;
        }

        @Override
        public void readNBT(INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
            untilSunstrike = compound.getInt("untilSunstrike");
            untilAxeSwing = compound.getInt("untilAxeSwing");
            prevTime = compound.getInt("prevTime");
            time = compound.getInt("time");
        }
    }

    public static class PlayerStorage implements Capability.IStorage<IPlayerCapability> {
        @Override
        public INBT writeNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side, INBT nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class PlayerProvider implements ICapabilitySerializable<INBT>
    {
        @CapabilityInject(IPlayerCapability.class)
        public static final Capability<IPlayerCapability> PLAYER_CAPABILITY = null;

        private LazyOptional<IPlayerCapability> instance = LazyOptional.of(PLAYER_CAPABILITY::getDefaultInstance);

        @Override
        public INBT serializeNBT() {
            return PLAYER_CAPABILITY.getStorage().writeNBT(PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            PLAYER_CAPABILITY.getStorage().readNBT(PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null, nbt);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == PLAYER_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
