package com.bobmowzie.mowziesmobs.server.ai.animation;

import java.util.List;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

public class AnimationFWNStompAttackAI extends AnimationAI<EntityWroughtnaut> {
    public AnimationFWNStompAttackAI(EntityWroughtnaut entity, Animation animation) {
        super(entity, animation);
        setMutexBits(8);
    }

    @Override
    public void updateTask() {
        if (animatingEntity.getAnimation() == getAnimation()) {
            animatingEntity.motionX = 0;
            animatingEntity.motionZ = 0;
            double perpFacing = animatingEntity.renderYawOffset * (Math.PI / 180);
            double facingAngle = perpFacing + Math.PI / 2;
            int hitY = MathHelper.floor(animatingEntity.getEntityBoundingBox().minY - 0.5);
            int tick = animatingEntity.getAnimationTick();
            final int maxDistance = 6;
            WorldServer world = (WorldServer) animatingEntity.world;
            if (tick == 6) {
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_SHOUT_2, 1, 1);
            } else if (tick > 9 && tick < 17) {
                if (tick == 10) {
                    animatingEntity.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.2F, 0.5F + animatingEntity.getRNG().nextFloat() * 0.1F);
                    final double infront = 1.47, side = -0.21;
                    double vx = Math.cos(facingAngle) * infront;
                    double vz = Math.sin(facingAngle) * infront;
                    double perpX = Math.cos(perpFacing);
                    double perpZ = Math.sin(perpFacing);
                    double fx = animatingEntity.posX + vx + perpX * side;
                    double fy = animatingEntity.getEntityBoundingBox().minY + 0.1;
                    double fz = animatingEntity.posZ + vz + perpZ * side;
                    int bx = MathHelper.floor(fx);
                    int bz = MathHelper.floor(fz);
                    int amount = 16 + world.rand.nextInt(8);
                    while (amount-- > 0) {
                        double theta = world.rand.nextDouble() * Math.PI * 2;
                        double dist = world.rand.nextDouble() * 0.1 + 0.25;
                        double sx = Math.cos(theta);
                        double sz = Math.sin(theta);
                        double px = fx + sx * dist;
                        double py = fy + world.rand.nextDouble() * 0.1;
                        double pz = fz + sz * dist;
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, px, py, pz, 0, sx * 0.065, 0, sz * 0.065, 1);
                    }
                } else if (tick == 12) {
                    animatingEntity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 1F + animatingEntity.getRNG().nextFloat() * 0.1F);
                }
                if (tick % 2 == 0) {
                    int distance = tick / 2 - 2;
                    double spread = Math.PI * 2;
                    int arcLen = MathHelper.ceil(distance * spread);
                    double minY = animatingEntity.getEntityBoundingBox().minY;
                    double maxY = animatingEntity.getEntityBoundingBox().maxY;
                    for (int i = 0; i < arcLen; i++) {
                        double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                        double vx = Math.cos(theta);
                        double vz = Math.sin(theta);
                        double px = animatingEntity.posX + vx * distance;
                        double pz = animatingEntity.posZ + vz * distance;
                        float factor = 1 - distance / (float) maxDistance;
                        AxisAlignedBB selection = new AxisAlignedBB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                        List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, selection);
                        for (Entity entity : hit) {
                            if (entity == animatingEntity || entity instanceof EntityFallingBlock) {
                                continue;
                            }
                            if (entity instanceof EntityLivingBase) {
                                entity.attackEntityFrom(DamageSource.FALLING_BLOCK, factor * 5 + 1);
                            }
                            double magnitude = world.rand.nextDouble() * 0.15 + 0.1;
                            entity.motionX += vx * factor * magnitude;
                            if (entity.onGround) {
                                entity.motionY += 0.1 + factor * 0.15;
                            }
                            entity.motionZ += vz * factor * magnitude;
                            if (entity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
                            }
                        }
                        if (world.rand.nextBoolean()) {
                            int hitX = MathHelper.floor(px);
                            int hitZ = MathHelper.floor(pz);
                            BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                            if (world.isAirBlock(pos.up())) {
                                IBlockState block = world.getBlockState(pos);
                                if (block.getMaterial() != Material.AIR && block.isBlockNormalCube()) {
                                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, hitX + 0.5, hitY + 0.5, hitZ + 0.5, block);
                                    fallingBlock.motionX = 0;
                                    fallingBlock.motionY = 0.4 + factor * 0.2;
                                    fallingBlock.motionZ = 0;
                                    fallingBlock.fallTime = 2;
                                    world.spawnEntity(fallingBlock);
                                    world.setBlockToAir(pos);
                                    int amount = 6 + world.rand.nextInt(10);
                                    int stateId = Block.getStateId(block);
                                    while (amount --> 0) {
                                        double cx = px + world.rand.nextFloat() * 2 - 1;
                                        double cy = animatingEntity.getEntityBoundingBox().minY + 0.1 + world.rand.nextFloat() * 0.3;
                                        double cz = pz + world.rand.nextFloat() * 2 - 1;
                                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, cx, cy, cz, 0, vx, 0.4 + world.rand.nextFloat() * 0.2F, vz, 1, stateId);
                                    }
                                }
                            }
                        }
                        if (world.rand.nextBoolean()) {
                            int amount = world.rand.nextInt(5);
                            while (amount-- > 0) {
                                double velX = vx * 0.075;
                                double velY = factor * 0.3 + 0.025;
                                double velZ = vz * 0.075;
                                world.spawnParticle(EnumParticleTypes.CLOUD, px + world.rand.nextFloat() * 2 - 1, animatingEntity.getEntityBoundingBox().minY + 0.1 + world.rand.nextFloat() * 1.5, pz + world.rand.nextFloat() * 2 - 1, 0, velX, velY, velZ, 1);
                            }
                        }
                    }
                }
            }
        }
    }
}
