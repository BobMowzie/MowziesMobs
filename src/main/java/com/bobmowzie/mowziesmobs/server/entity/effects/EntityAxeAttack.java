package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.CreatureAttribute;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ArmorStandEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stats;
import net.minecraft.resources.Hand;
import net.minecraft.resources.SoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.math.AxisAlignedBB;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.math.MathHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BobMowzie on 7/15/2017.
 */
public class EntityAxeAttack extends EntityMagicEffect {
    private static final EntityDataAccessor<Boolean> VERTICAL = EntityDataManager.createKey(EntityAxeAttack.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> AXE_STACK = EntityDataManager.createKey(EntityAxeAttack.class, EntityDataSerializers.ITEMSTACK);

    public static int SWING_DURATION_HOR = 24;
    public static int SWING_DURATION_VER = 30;
    private float quakeAngle = 0;
    private AxisAlignedBB quakeBB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, World world) {
        super(type, world);
    }

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, World world, LivingEntity caster, boolean vertical) {
        this(type, world);
        if (!world.isClientSide) {
            this.setCasterID(caster.getEntityId());
        }
        setVertical(vertical);
        setAxeStack(caster.getHeldItemMainhand());
    }
    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(VERTICAL, false);
        getDataManager().register(AXE_STACK, ItemHandler.WROUGHT_AXE.getDefaultInstance());
    }

    @Override
    public void tick() {
        super.tick();
        if (caster != null) {
            if (!caster.isAlive()) remove();
            setPositionAndRotation(caster.getPosX(), caster.getPosY(), caster.getPosZ(), caster.getYRot(), caster.getXRot());
        }
        if (!world.isClientSide && ticksExisted == 7) playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 0.7F, 1.1f);
        if (!world.isClientSide && caster != null) {
            if (!getVertical() && ticksExisted == SWING_DURATION_HOR /2 - 1) dealDamage(7.0f * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f, 4f, 160, 1.2f);
            else if (getVertical() && ticksExisted == SWING_DURATION_VER /2 - 1) {
                dealDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(), 4.5f, 40, 0.8f);
                quakeAngle = rotationYaw;
                quakeBB = getBoundingBox();
                playSound(MMSounds.ENTITY_WROUGHT_AXE_LAND.get(), 0.3F, 0.5F);
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 0.9F + rand.nextFloat() * 0.1F);
            }
            else if (getVertical() && ticksExisted == SWING_DURATION_VER /2 + 1) {
                EntityCameraShake.cameraShake(world, getPositionVec(), 10, 0.05f, 0, 10);
            }
        }

        if (getVertical() && caster != null) {
            if (ticksExisted >= SWING_DURATION_VER /2) {
                int maxDistance = 16;
                double perpFacing = quakeAngle * (Math.PI / 180);
                double facingAngle = perpFacing + Math.PI / 2;
                int hitY = MathHelper.floor(quakeBB.minY - 0.5);
                int distance = ticksExisted - 15;
                double spread = Math.PI * 0.35F;
                int arcLen = MathHelper.ceil(distance * spread);
                double minY = quakeBB.minY;
                double maxY = quakeBB.maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = getPosX() + vx * distance;
                    double pz = getPosZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AxisAlignedBB selection = new AxisAlignedBB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity.isOnGround()) {
                            if (entity == this || entity instanceof FallingBlockEntity || entity == caster) {
                                continue;
                            }
                            float applyKnockbackResistance = 0;
                            if (entity instanceof LivingEntity) {
                                if (caster instanceof Player)
                                    entity.attackEntityFrom(DamageSource.causePlayerDamage((Player) caster), (factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f));
                                else
                                    entity.attackEntityFrom(DamageSource.causeMobDamage(caster), (factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f));
                                applyKnockbackResistance = (float) ((LivingEntity) entity).getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                            }
                            double magnitude = -0.2;
                            double x = vx * (1 - factor) * magnitude * (1 - applyKnockbackResistance);
                            double y = 0;
                            if (entity.isOnGround()) {
                                y += 0.15 * (1 - applyKnockbackResistance);
                            }
                            double z = vz * (1 - factor) * magnitude * (1 - applyKnockbackResistance);
                            entity.setMotion(entity.getMotion().add(x, y, z));
                            if (entity instanceof ServerPlayer) {
                                ((ServerPlayer) entity).connection.sendPacket(new SEntityVelocityPacket(entity));
                            }
                        }
                    }
                    if (world.rand.nextBoolean()) {
                        int hitX = MathHelper.floor(px);
                        int hitZ = MathHelper.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).up();
                        BlockState block = world.getBlockState(pos);
                        BlockState blockAbove = world.getBlockState(abovePos);
                        if (block.getMaterial() != Material.AIR && block.isNormalCube(world, pos) && !block.getBlock().hasTileEntity(block) && !blockAbove.getMaterial().blocksMovement()) {
                            EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, world, block, 0.3f);
                            fallingBlock.setPosition(hitX + 0.5, hitY + 1, hitZ + 0.5);
                            world.addEntity(fallingBlock);
                        }
                    }
                }
            }
        }
        if (ticksExisted > SWING_DURATION_HOR) remove();
    }

    private void dealDamage(float damage, float range, float arc, float applyKnockback) {
        boolean hit = false;
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(range, 2, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getPosZ() - getPosZ(), entityHit.getPosX() - getPosX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = rotationYaw % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getPosZ() - getPosZ()) * (entityHit.getPosZ() - getPosZ()) + (entityHit.getPosX() - getPosX()) * (entityHit.getPosX() - getPosX()));
            if (entityHit != caster && entityHitDistance <= range && entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2 || entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2) {
                PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(caster, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
                if (playerCapability != null) {
                    playerCapability.setAxeCanAttack(true);
                    if (caster instanceof Player) attackTargetEntityWithCurrentItem(entityHit, (Player)caster, damage / ItemHandler.WROUGHT_AXE.getAttackDamage(), applyKnockback);
                    playerCapability.setAxeCanAttack(false);
                }
                else {
                    entityHit.attackEntityFrom(DamageSource.causeMobDamage(caster), damage);
                    entityHit.setMotion(entityHit.getMotion().x * applyKnockback, entityHit.getMotion().y, entityHit.getMotion().z * applyKnockback);
                }
                hit = true;
            }
        }
        if (hit) {
            playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT.get(), 0.3F, 0.5F);
        }
    }

    public void setVertical(boolean vertical) {
        getDataManager().set(VERTICAL, vertical);
    }

    public boolean getVertical() {
        return getDataManager().get(VERTICAL);
    }

    private List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(distanceX, distanceY, distanceZ));
        ArrayList<LivingEntity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @Override
    public void remove() {
        super.remove();
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public void setAxeStack(ItemStack axeStack) {
        getDataManager().set(AXE_STACK, axeStack);
    }

    public ItemStack getAxeStack() {
        return getDataManager().get(AXE_STACK);
    }

    /**
     * Copied from player entity, with modification
     */
    public void attackTargetEntityWithCurrentItem(Entity targetEntity, Player player, float damageMult, float knockbackMult) {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(player, targetEntity)) return;

        ItemStack oldStack = player.getHeldItemMainhand();
        ItemStack newStack = getAxeStack();
        player.setHeldItem(Hand.MAIN_HAND, newStack);
        player.getAttributeManager().reapplyModifiers(newStack.getAttributeModifiers(EquipmentSlot.MAINHAND));

        if (targetEntity.canBeAttackedWithItem()) {
            if (!targetEntity.hitByEntity(player)) {
                float f = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMult;
                float f1;
                if (targetEntity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((LivingEntity)targetEntity).getCreatureAttribute());
                } else {
                    f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), CreatureAttribute.UNDEFINED);
                }

                float f2 = 1.0f;
                f = f * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackModifier(player);
                    if (player.isSprinting() && flag) {
                        player.world.playSound((Player)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    f = f + f1;
                    boolean flag3 = false;

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(player);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity)targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isBurning()) {
                            flag4 = true;
                            targetEntity.setFire(1);
                        }
                    }

                    Vec3 vector3d = targetEntity.getMotion();
                    boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), f);
                    if (flag5) {
                        if (i > 0) {
                            if (targetEntity instanceof LivingEntity) {
                                ((LivingEntity)targetEntity).applyKnockback((float)i * 0.5F * knockbackMult, (double)MathHelper.sin(player.getYRot() * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(player.getYRot() * ((float)Math.PI / 180F))));
                            } else {
                                targetEntity.addVelocity((double)(-MathHelper.sin(player.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F * knockbackMult), 0.1D, (double)(MathHelper.cos(player.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F * knockbackMult));
                            }

                            player.setMotion(player.getMotion().mul(0.6D, 1.0D, 0.6D));
                            player.setSprinting(false);
                        }

                        if (targetEntity instanceof ServerPlayer && targetEntity.velocityChanged) {
                            ((ServerPlayer)targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.setMotion(vector3d);
                        }

                        if (flag) {
                            player.world.playSound((Player)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
                        } else {
                            player.world.playSound((Player)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
                        }

                        if (f1 > 0.0F) {
                            player.onEnchantmentCritical(targetEntity);
                        }

                        player.setLastAttackedEntity(targetEntity);
                        if (targetEntity instanceof LivingEntity) {
                            EnchantmentHelper.applyThornEnchantments((LivingEntity)targetEntity, player);
                        }

                        EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
                        ItemStack itemstack1 = player.getHeldItemMainhand();
                        Entity entity = targetEntity;
                        if (targetEntity instanceof net.minecraftforge.entity.PartEntity) {
                            entity = ((net.minecraftforge.entity.PartEntity<?>) targetEntity).getParent();
                        }

                        if (!player.world.isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hitEntity((LivingEntity)entity, player);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copy, Hand.MAIN_HAND);
                                player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity)targetEntity).getHealth();
                            player.addStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                targetEntity.setFire(j * 4);
                            }

                            if (player.world instanceof ServerWorld && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerWorld)player.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getPosX(), targetEntity.getPosYHeight(0.5D), targetEntity.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        player.addExhaustion(0.1F);
                    } else {
                        player.world.playSound((Player)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
                        if (flag4) {
                            targetEntity.extinguish();
                        }
                    }
                }

            }
        }
        player.setHeldItem(Hand.MAIN_HAND, oldStack);
        player.getAttributeManager().reapplyModifiers(oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND));
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setAxeStack(ItemStack.read(compound.getCompound("axe_stack")));
        setVertical(compound.getBoolean("vertical"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("axe_stack", getAxeStack().write(new CompoundNBT()));
        compound.putBoolean("vertical", getVertical());
    }
}
