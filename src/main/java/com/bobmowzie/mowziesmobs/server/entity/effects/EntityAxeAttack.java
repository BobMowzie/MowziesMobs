package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BobMowzie on 7/15/2017.
 */
public class EntityAxeAttack extends EntityMagicEffect {
    private static final DataParameter<Boolean> VERTICAL = EntityDataManager.createKey(EntityAxeAttack.class, DataSerializers.BOOLEAN);

    public static int SWING_DURATION_HOR = 24;
    public static int SWING_DURATION_VER = 30;
    private float quakeAngle = 0;
    private AxisAlignedBB quakeBB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, World world) {
        super(type, world);
    }

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, World world, LivingEntity caster, boolean vertical) {
        this(type, world);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
        setVertical(vertical);
    }
    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(VERTICAL, false);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;//this.getBoundingBox();
    }

    @Override
    public void tick() {
        super.tick();
        if (caster != null && !caster.isAlive()) remove();
        if (caster != null) {
            setPositionAndRotation(caster.getPosX(), caster.getPosY(), caster.getPosZ(), caster.rotationYaw, caster.rotationPitch);
        }
        if (!world.isRemote && ticksExisted == 7) playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 0.7F, 1.1f);
            if (!world.isRemote && caster != null) {
                if (!getVertical() && ticksExisted == SWING_DURATION_HOR /2 - 1) dealDamage(7 * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(), 4.5f, 160, 1.2f);
                else if (getVertical() && ticksExisted == SWING_DURATION_VER /2 - 1) {
                    dealDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(), 4.5f, 40, 0.8f);
                    quakeAngle = rotationYaw;
                    quakeBB = getBoundingBox();
                    playSound(MMSounds.ENTITY_WROUGHT_AXE_LAND.get(), 0.3F, 0.5F);
                    playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 0.9F + rand.nextFloat() * 0.1F);                }
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
                        if (entity == this || entity instanceof FallingBlockEntity || entity == caster) {
                            continue;
                        }
                        float knockbackResistance = 0;
                        if (entity instanceof LivingEntity) {
                            if (caster instanceof PlayerEntity) entity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) caster), (factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f));
                            else entity.attackEntityFrom(DamageSource.causeMobDamage(caster), (factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue() / 9.0f));
                            knockbackResistance = (float) ((LivingEntity)entity).getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue();
                        }
                        double magnitude = -0.2;
                        double x = vx * (1 - factor) * magnitude * (1 - knockbackResistance);
                        double y = 0;
                        if (entity.onGround) {
                            y += 0.15 * (1 - knockbackResistance);
                        }
                        double z = vz * (1 - factor) * magnitude * (1 - knockbackResistance);
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

    private void dealDamage(float damage, float range, float arc, float knockback) {
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
                    if (caster instanceof PlayerEntity) ((PlayerEntity)caster).attackTargetEntityWithCurrentItem(entityHit);
                    playerCapability.setAxeCanAttack(false);
                }
                else entityHit.attackEntityFrom(DamageSource.causeMobDamage(caster), damage);
                entityHit.setMotion(entityHit.getMotion().x * knockback, entityHit.getMotion().y, entityHit.getMotion().z * knockback);
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

    public LivingEntity getCaster() {
        return caster;
    }
}
