package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BobMowzie on 7/15/2017.
 */
public class EntityAxeAttack extends EntityMagicEffect {
    private static final EntityDataAccessor<Boolean> VERTICAL = SynchedEntityData.defineId(EntityAxeAttack.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> AXE_STACK = SynchedEntityData.defineId(EntityAxeAttack.class, EntityDataSerializers.ITEM_STACK);

    public static int SWING_DURATION_HOR = 24;
    public static int SWING_DURATION_VER = 30;
    private float quakeAngle = 0;
    private AABB quakeBB = new AABB(0, 0, 0, 1, 1, 1);

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, Level world) {
        super(type, world);
    }

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, Level world, LivingEntity caster, boolean vertical) {
        super(type, world, caster);
        setVertical(vertical);
        setAxeStack(caster.getMainHandItem());
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(VERTICAL, false);
        getEntityData().define(AXE_STACK, ItemHandler.WROUGHT_AXE.get().getDefaultInstance());
    }

    @Override
    public void tick() {
        super.tick();
        if (caster != null) {
            if (!caster.isAlive()) discard();
            absMoveTo(caster.getX(), caster.getY() + caster.getEyeHeight(), caster.getZ(), caster.getYRot(), caster.getXRot());
        }
        if (!level().isClientSide && tickCount == 7) playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 0.7F, 1.1f);
        if (!level().isClientSide && caster != null) {
            if (!getVertical() && tickCount == SWING_DURATION_HOR /2 - 1) dealDamage(7.0f * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f, 4f, 160, 1.2f);
            else if (getVertical() && tickCount == SWING_DURATION_VER /2 - 1) {
                dealDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(), 4.5f, 40, 0.8f);
                quakeAngle = getYRot();
                quakeBB = getBoundingBox().move(0, -caster.getEyeHeight(), 0);
                playSound(MMSounds.ENTITY_WROUGHT_AXE_LAND.get(), 0.3F, 0.5F);
                playSound(SoundEvents.GENERIC_EXPLODE, 2, 0.9F + random.nextFloat() * 0.1F);
            }
            else if (getVertical() && tickCount == SWING_DURATION_VER /2 + 1) {
                EntityCameraShake.cameraShake(level(), position(), 10, 0.05f, 0, 10);
            }
        }

        if (getVertical() && caster != null) {
            if (tickCount >= SWING_DURATION_VER /2) {
                int maxDistance = 16;
                double perpFacing = quakeAngle * (Math.PI / 180);
                double facingAngle = perpFacing + Math.PI / 2;
                int hitY = Mth.floor(quakeBB.minY - 0.5);
                int distance = tickCount - 15;
                double spread = Math.PI * 0.35F;
                int arcLen = Mth.ceil(distance * spread);
                double minY = quakeBB.minY;
                double maxY = quakeBB.maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = getX() + vx * distance;
                    double pz = getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AABB selection = new AABB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = level().getEntitiesOfClass(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity.onGround()) {
                            if (entity == this || entity instanceof FallingBlockEntity || entity == caster) {
                                continue;
                            }
                            float applyKnockbackResistance = 0;
                            boolean hitEntity = false;
                            if (!raytraceCheckEntity(entity)) continue;

                            if (caster instanceof Player)
                                hitEntity = entity.hurt(damageSources().playerAttack((Player) caster), (factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f));
                            else
                                hitEntity = entity.hurt(damageSources().mobAttack(caster), (factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f));
                            if (entity instanceof LivingEntity) {
                                applyKnockbackResistance = (float) ((LivingEntity) entity).getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                            }
                            if (hitEntity) {
                                double magnitude = -4;
                                double x = vx * (1 - factor) * magnitude * (1 - applyKnockbackResistance);
                                double y = 0;
                                if (entity.onGround()) {
                                    y += 0.15 * (1 - applyKnockbackResistance);
                                }
                                double z = vz * (1 - factor) * magnitude * (1 - applyKnockbackResistance);
                                entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                                if (entity instanceof ServerPlayer) {
                                    ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
                                }
                            }
                        }
                    }
                    if (level().random.nextBoolean()) {
                        int hitX = Mth.floor(px);
                        int hitZ = Mth.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).above();
                        BlockState block = level().getBlockState(pos);
                        BlockState blockAbove = level().getBlockState(abovePos);
                        if (!block.isAir() && block.isRedstoneConductor(level(), pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
                            EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), level(), block, 0.3f);
                            fallingBlock.setPos(hitX + 0.5, hitY + 1, hitZ + 0.5);
                            level().addFreshEntity(fallingBlock);
                        }
                    }
                }
            }
        }
        if (tickCount > SWING_DURATION_HOR) discard();
    }

    private void dealDamage(float damage, float range, float arc, float applyKnockback) {
        boolean hit = false;
        List<Entity> entitiesHit = getEntitiesNearby(range, 2, range, range);
        for (Entity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - getZ(), entityHit.getX() - getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = getYRot() % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - getZ()) * (entityHit.getZ() - getZ()) + (entityHit.getX() - getX()) * (entityHit.getX() - getX())) - entityHit.getBbWidth() / 2f;
            if (entityHit != caster && (!(entityHit instanceof Parrot) || entityHit.getVehicle() != caster) && entityHitDistance <= range && entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2 || entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2) {
                // Do raycast check to prevent damaging through walls
                if (!raytraceCheckEntity(entityHit)) continue;

                PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(caster, CapabilityHandler.PLAYER_CAPABILITY);
                if (playerCapability != null) {
                    playerCapability.setAxeCanAttack(true);
                    if (caster instanceof Player) attackTargetEntityWithCurrentItem(entityHit, (Player)caster, damage / ItemHandler.WROUGHT_AXE.get().getAttackDamage(), applyKnockback);
                    playerCapability.setAxeCanAttack(false);
                }
                else {
                    entityHit.hurt(damageSources().mobAttack(caster), damage);
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().x * applyKnockback, entityHit.getDeltaMovement().y, entityHit.getDeltaMovement().z * applyKnockback);
                }
                hit = true;
            }
        }
        if (hit) {
            playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT.get(), 0.3F, 0.5F);
        }
    }

    public void setVertical(boolean vertical) {
        getEntityData().set(VERTICAL, vertical);
    }

    public boolean getVertical() {
        return getEntityData().get(VERTICAL);
    }

    private List<Entity> getEntitiesNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = level().getEntities(this, getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        ArrayList<Entity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor != null && distanceTo(entityNeighbor) <= radius + entityNeighbor.getBbWidth() / 2f).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public void setAxeStack(ItemStack axeStack) {
        getEntityData().set(AXE_STACK, axeStack);
    }

    public ItemStack getAxeStack() {
        return getEntityData().get(AXE_STACK);
    }

    /**
     * Copied from player entity, with modification
     */
    public void attackTargetEntityWithCurrentItem(Entity targetEntity, Player player, float damageMult, float knockbackMult) {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(player, targetEntity)) return;

        ItemStack oldStack = player.getMainHandItem();
        ItemStack newStack = getAxeStack();
        player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
        player.getAttributes().addTransientAttributeModifiers(newStack.getAttributeModifiers(EquipmentSlot.MAINHAND));

        if (targetEntity.isAttackable()) {
            if (!targetEntity.skipAttackInteraction(player)) {
                float f = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMult;
                float f1;
                if (targetEntity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), ((LivingEntity)targetEntity).getMobType());
                } else {
                    f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), MobType.UNDEFINED);
                }

                float f2 = 1.0f;
                f = f * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackBonus(player);
                    if (player.isSprinting() && flag) {
                        player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    f = f + f1;
                    boolean flag3 = false;

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspect(player);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity)targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isOnFire()) {
                            flag4 = true;
                            targetEntity.setSecondsOnFire(1);
                        }
                    }

                    Vec3 vector3d = targetEntity.getDeltaMovement();
                    boolean flag5 = targetEntity.hurt(damageSources().playerAttack(player), f);
                    if (flag5) {
                        if (i > 0) {
                            if (targetEntity instanceof LivingEntity) {
                                ((LivingEntity)targetEntity).knockback((float)i * 0.5F * knockbackMult, (double)Mth.sin(player.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(player.getYRot() * ((float)Math.PI / 180F))));
                            } else {
                                targetEntity.push((double)(-Mth.sin(player.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F * knockbackMult), 0.1D, (double)(Mth.cos(player.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F * knockbackMult));
                            }

                            player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                            player.setSprinting(false);
                        }

                        if (targetEntity instanceof ServerPlayer && targetEntity.hurtMarked) {
                            ((ServerPlayer)targetEntity).connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
                            targetEntity.hurtMarked = false;
                            targetEntity.setDeltaMovement(vector3d);
                        }

                        if (flag) {
                            player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F);
                        } else {
                            player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
                        }

                        if (f1 > 0.0F) {
                            player.magicCrit(targetEntity);
                        }

                        player.setLastHurtMob(targetEntity);
                        if (targetEntity instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity)targetEntity, player);
                        }

                        EnchantmentHelper.doPostDamageEffects(player, targetEntity);
                        ItemStack itemstack1 = player.getMainHandItem();
                        Entity entity = targetEntity;
                        if (targetEntity instanceof net.minecraftforge.entity.PartEntity) {
                            entity = ((net.minecraftforge.entity.PartEntity<?>) targetEntity).getParent();
                        }

                        if (!player.level().isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hurtEnemy((LivingEntity)entity, player);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copy, InteractionHand.MAIN_HAND);
                                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity)targetEntity).getHealth();
                            player.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                targetEntity.setSecondsOnFire(j * 4);
                            }

                            if (player.level() instanceof ServerLevel && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerLevel)player.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5D), targetEntity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        player.causeFoodExhaustion(0.1F);
                    } else {
                        player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);
                        if (flag4) {
                            targetEntity.clearFire();
                        }
                    }
                }

            }
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, oldStack);
        player.getAttributes().addTransientAttributeModifiers(oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setAxeStack(ItemStack.of(compound.getCompound("axe_stack")));
        setVertical(compound.getBoolean("vertical"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("axe_stack", getAxeStack().save(new CompoundTag()));
        compound.putBoolean("vertical", getVertical());
    }
}
