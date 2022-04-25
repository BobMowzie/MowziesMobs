package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class AnimationFWNStompAttackAI extends SimpleAnimationAI<EntityWroughtnaut> {
    public AnimationFWNStompAttackAI(EntityWroughtnaut entity, Animation animation) {
        super(entity, animation, true);
    }

    @Override
    public void tick() {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        double perpFacing = entity.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = Mth.floor(entity.getBoundingBox().minY - 0.5);
        int tick = entity.getAnimationTick();
        final int maxDistance = 6;
        ServerLevel world = (ServerLevel) entity.level;
        if (tick == 6) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_SHOUT_2.get(), 1, 1);
        } else if (tick > 9 && tick < 17) {
            if (tick == 10) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_STEP.get(), 1.2F, 0.5F + entity.getRandom().nextFloat() * 0.1F);
            } else if (tick == 12) {
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 2, 1F + entity.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(entity.level, entity.position(), 25, 0.1f, 0, 20);
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = Mth.ceil(distance * spread);
                double minY = entity.getBoundingBox().minY;
                double maxY = entity.getBoundingBox().maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = entity.getX() + vx * distance;
                    double pz = entity.getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AABB selection = new AABB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getEntitiesOfClass(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity.isOnGround()) {
                            if (entity == this.entity || entity instanceof EntityFallingBlock) {
                                continue;
                            }
                            float applyKnockbackResistance = 0;
                            if (entity instanceof LivingEntity) {
                                entity.hurt(DamageSource.mobAttack(this.entity), (factor * 5 + 1) * ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.combatConfig.attackMultiplier.get().floatValue());
                                applyKnockbackResistance = (float) ((LivingEntity) entity).getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                            }
                            double magnitude = world.random.nextDouble() * 0.15 + 0.1;
                            float x = 0, y = 0, z = 0;
                            x += vx * factor * magnitude * (1 - applyKnockbackResistance);
                            y += 0.1 * (1 - applyKnockbackResistance) + factor * 0.15 * (1 - applyKnockbackResistance);
                            z += vz * factor * magnitude * (1 - applyKnockbackResistance);
                            entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                            if (entity instanceof ServerPlayer) {
                                ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
                            }
                        }
                    }
                    if (world.random.nextBoolean()) {
                        int hitX = Mth.floor(px);
                        int hitZ = Mth.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).above();
                        BlockState block = world.getBlockState(pos);
                        BlockState blockAbove = world.getBlockState(abovePos);
                        if (block.getMaterial() != Material.AIR && block.isRedstoneConductor(world, pos) && !block.getBlock().hasTileEntity(block) && !blockAbove.getMaterial().blocksMotion()) {
                            EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), world, block, (float) (0.4 + factor * 0.2));
                            fallingBlock.setPos(hitX + 0.5, hitY + 1, hitZ + 0.5);
                            world.addFreshEntity(fallingBlock);
                        }
                    }
                }
            }
        }
    }
}
