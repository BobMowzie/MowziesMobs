package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

import java.util.List;

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
            int hitY = MathHelper.floor_double(animatingEntity.boundingBox.minY - 0.5);
            int tick = animatingEntity.getAnimationTick();
            final int maxDistance = 6;
            WorldServer world = (WorldServer) animatingEntity.worldObj;
            if (tick == 6) {
                animatingEntity.playSound("mowziesmobs:wroughtnautShout2", 1, 1);
            } else if (tick > 7 && tick < 15) {
                if (tick == 10) {
                    animatingEntity.playSound("minecraft:mob.zombie.metal", 1.2F, 0.5F + animatingEntity.getRNG().nextFloat() * 0.1F);
                    final double infront = 1.47, side = -0.21;
                    double vx = Math.cos(facingAngle) * infront;
                    double vz = Math.sin(facingAngle) * infront;
                    double perpX = Math.cos(perpFacing);
                    double perpZ = Math.sin(perpFacing);
                    double fx = animatingEntity.posX + vx + perpX * side;
                    double fy = animatingEntity.boundingBox.minY + 0.1;
                    double fz = animatingEntity.posZ + vz + perpZ * side;
                    int bx = MathHelper.floor_double(fx);
                    int bz = MathHelper.floor_double(fz);
                    String particle = "blockcrack_" + Block.getIdFromBlock(world.getBlock(bx, hitY, bz)) + "_" + world.getBlockMetadata(bx, hitY, bz);
                    int amount = 16 + world.rand.nextInt(8);
                    while (amount --> 0) {
                        double theta = world.rand.nextDouble() * Math.PI * 2;
                        double dist = world.rand.nextDouble() * 0.1 + 0.25;
                        double sx = Math.cos(theta);
                        double sz = Math.sin(theta);
                        double px = fx + sx * dist;
                        double py = fy + world.rand.nextDouble() * 0.1;
                        double pz = fz + sz * dist;
                        world.func_147487_a("smoke", px, py, pz, 0, sx * 0.065, 0, sz * 0.065, 1);   
                    }
                } else if (tick == 12) {
                    animatingEntity.playSound("minecraft:random.explode", 2, 1F + animatingEntity.getRNG().nextFloat() * 0.1F);
                }
                if (tick % 2 == 0) {
                    int distance = tick / 2 - 1;
                    double spread = Math.PI * 2;
                    int arcLen = MathHelper.ceiling_double_int(distance * spread);
                    AxisAlignedBB selection = AxisAlignedBB.getBoundingBox(0, animatingEntity.boundingBox.minY, 0, 0, animatingEntity.boundingBox.maxY , 0);
                    for (int i = 0; i < arcLen; i++) {
                        double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                        double vx = Math.cos(theta);
                        double vz = Math.sin(theta);
                        double px = animatingEntity.posX + vx * distance;
                        double pz = animatingEntity.posZ + vz * distance;
                        float factor = 1 - distance / (float) maxDistance;
                        selection.minX = px - 1.5;
                        selection.minZ = pz - 1.5;
                        selection.maxX = px + 1.5;
                        selection.maxZ = pz + 1.5;
                        List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, selection);
                        for (Entity entity : hit) {
                            if (entity == animatingEntity || entity instanceof EntityFallingBlock) {
                                continue;
                            }
                            if (entity instanceof EntityLivingBase) {
                                entity.attackEntityFrom(DamageSource.fallingBlock, factor * 5 + 1);
                            }
                            double magnitude = world.rand.nextDouble() * 0.15 + 0.1;
                            entity.motionX += vx * factor * magnitude;
                            if (entity.onGround) {
                                entity.motionY += 0.1 + factor * 0.15;
                            }
                            entity.motionZ += vz * factor * magnitude;
                            if (entity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
                            }
                        }
                        if (world.rand.nextBoolean()) {
                            int hitX = MathHelper.floor_double(px);
                            int hitZ = MathHelper.floor_double(pz);
                            if (world.isAirBlock(hitX, hitY + 1, hitZ)) {
                                Block block = world.getBlock(hitX, hitY, hitZ);
                                if (block.getMaterial() != Material.air && block.renderAsNormalBlock()) {
                                    int meta = world.getBlockMetadata(hitX, hitY, hitZ);
                                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, hitX + 0.5, hitY + 0.5, hitZ + 0.5, block, meta);
                                    fallingBlock.motionX = 0;
                                    fallingBlock.motionY = 0.4 + factor * 0.2;
                                    fallingBlock.motionZ = 0;
                                    fallingBlock.field_145812_b = 2;
                                    world.spawnEntityInWorld(fallingBlock);
                                    world.setBlockToAir(hitX, hitY, hitZ);
                                    int amount = 6 + world.rand.nextInt(10);
                                    String particle = "blockcrack_" + Block.getIdFromBlock(block) + "_" + meta;
                                    while (amount --> 0) {
                                        double cx = px + world.rand.nextFloat() * 2 - 1;
                                        double cy = animatingEntity.boundingBox.minY + 0.1 + world.rand.nextFloat() * 0.3;
                                        double cz = pz + world.rand.nextFloat() * 2 - 1;
                                        world.func_147487_a(particle, cx, cy, cz, 0, vx, 0.4 + world.rand.nextFloat() * 0.2F, vz, 1);   
                                    }
                                }
                            }
                        }
                        if (world.rand.nextBoolean()) {
                            int amount = world.rand.nextInt(5);
                            while (amount --> 0) {
                                double velX = vx * 0.075;
                                double velY = factor * 0.3 + 0.025;
                                double velZ = vz * 0.075;
                                world.func_147487_a("cloud", px + world.rand.nextFloat() * 2 - 1, animatingEntity.boundingBox.minY + 0.1 + world.rand.nextFloat() * 1.5, pz + world.rand.nextFloat() * 2 - 1, 0, velX, velY, velZ, 1);   
                            }
                        }
                    }
                }
            }
        }
    }
}
