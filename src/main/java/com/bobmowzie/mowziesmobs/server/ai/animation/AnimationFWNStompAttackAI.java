package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class AnimationFWNStompAttackAI extends SimpleAnimationAI<EntityWroughtnaut> {
    public AnimationFWNStompAttackAI(EntityWroughtnaut entity, Animation animation) {
        super(entity, animation, true);
    }

    @Override
    public void tick() {
        entity.setMotion(0, entity.getMotion().y, 0);
        double perpFacing = entity.renderYawOffset * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = MathHelper.floor(entity.getBoundingBox().minY - 0.5);
        int tick = entity.getAnimationTick();
        final int maxDistance = 6;
        ServerWorld world = (ServerWorld) entity.world;
        if (tick == 6) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_SHOUT_2.get(), 1, 1);
        } else if (tick > 9 && tick < 17) {
            if (tick == 10) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_STEP.get(), 1.2F, 0.5F + entity.getRNG().nextFloat() * 0.1F);
                final double infront = 1.47, side = -0.21;
                double vx = Math.cos(facingAngle) * infront;
                double vz = Math.sin(facingAngle) * infront;
                double perpX = Math.cos(perpFacing);
                double perpZ = Math.sin(perpFacing);
                double fx = entity.getPosX() + vx + perpX * side;
                double fy = entity.getBoundingBox().minY + 0.1;
                double fz = entity.getPosZ() + vz + perpZ * side;
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
                    world.addParticle(ParticleTypes.SMOKE, px, py, pz, sx * 0.065, 0, sz * 0.065);
                }
            } else if (tick == 12) {
                entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 1F + entity.getRNG().nextFloat() * 0.1F);
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(distance * spread);
                double minY = entity.getBoundingBox().minY;
                double maxY = entity.getBoundingBox().maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = entity.getPosX() + vx * distance;
                    double pz = entity.getPosZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AxisAlignedBB selection = new AxisAlignedBB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity == this.entity || entity instanceof FallingBlockEntity) {
                            continue;
                        }
                        float knockbackResistance = 0;
                        if (entity instanceof LivingEntity) {
                            entity.attackEntityFrom(DamageSource.causeMobDamage(this.entity), (factor * 5 + 1) * ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.combatConfig.attackMultiplier.get());
                            knockbackResistance = (float) ((LivingEntity)entity).getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue();
                        }
                        double magnitude = world.rand.nextDouble() * 0.15 + 0.1;
                        float x = 0, y = 0, z = 0;
                        x += vx * factor * magnitude * (1 - knockbackResistance);
                        if (entity.onGround) {
                            y += 0.1 * (1 - knockbackResistance) + factor * 0.15 * (1 - knockbackResistance);
                        }
                        z += vz * factor * magnitude * (1 - knockbackResistance);
                        entity.setMotion(entity.getMotion().add(x, y, z));
                        if (entity instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) entity).connection.sendPacket(new SEntityVelocityPacket(entity));
                        }
                    }
                    if (world.rand.nextBoolean()) {
                        int hitX = MathHelper.floor(px);
                        int hitZ = MathHelper.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).up();
                        BlockPos belowPos = new BlockPos(pos).down();
                        if (world.isAirBlock(abovePos) && !world.isAirBlock(belowPos)) {
                            BlockState block = world.getBlockState(pos);
                            if (block.getMaterial() != Material.AIR && block.isNormalCube(world, pos) && block.getBlock() != Blocks.BEDROCK && !block.getBlock().hasTileEntity(block)) {
                                FallingBlockEntity fallingBlock = new FallingBlockEntity(world, hitX + 0.5, hitY + 0.5, hitZ + 0.5, block);
                                fallingBlock.setMotion(0, 0.4 + factor * 0.2, 0);
                                fallingBlock.fallTime = 2;
                                world.addEntity(fallingBlock);
                                world.removeBlock(pos, false);
                                int amount = 6 + world.rand.nextInt(10);
                                int stateId = Block.getStateId(block);
                                while (amount --> 0) {
                                    double cx = px + world.rand.nextFloat() * 2 - 1;
                                    double cy = entity.getBoundingBox().minY + 0.1 + world.rand.nextFloat() * 0.3;
                                    double cz = pz + world.rand.nextFloat() * 2 - 1;
                                    world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), cx, cy, cz, vx, 0.4 + world.rand.nextFloat() * 0.2F, vz);
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
                            world.addParticle(ParticleTypes.CLOUD, px + world.rand.nextFloat() * 2 - 1, entity.getBoundingBox().minY + 0.1 + world.rand.nextFloat() * 1.5, pz + world.rand.nextFloat() * 2 - 1, velX, velY, velZ);
                        }
                    }
                }
            }
        }
    }
}
