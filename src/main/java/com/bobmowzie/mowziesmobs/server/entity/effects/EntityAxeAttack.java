package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.Block;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Josh on 7/15/2017.
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

    @Override
    public void tick() {
        super.tick();
        if (caster != null && !caster.isAlive()) remove();
        if (caster != null) {
            setPositionAndRotation(caster.posX, caster.posY, caster.posZ, caster.rotationYaw, caster.rotationPitch);
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
        }
        if (!world.isRemote && ticksExisted == 7) playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 0.7F, 1.1f);
            if (!world.isRemote && caster != null) {
                if (!getVertical() && ticksExisted == SWING_DURATION_HOR /2 - 1) dealDamage(7 * ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolData.attackDamage, 4.5f, 160, 1.2f);
                else if (getVertical() && ticksExisted == SWING_DURATION_VER /2 - 1) {
                    dealDamage(ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolData.attackDamage, 4.5f, 40, 0.8f);
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
                    double px = posX + vx * distance;
                    double pz = posZ + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AxisAlignedBB selection = new AxisAlignedBB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getEntitiesWithinAABB(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity == this || entity instanceof FallingBlockEntity || entity == caster) {
                            continue;
                        }
                        float knockbackResistance = 0;
                        if (entity instanceof LivingEntity) {
                            if (caster instanceof PlayerEntity) entity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) caster), (factor * 5 + 1) * (ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolData.attackDamage / 9.0f));
                            else entity.attackEntityFrom(DamageSource.causeMobDamage(caster), (factor * 5 + 1) * (ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolData.attackDamage / 9.0f));
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
                        BlockPos belowPos = new BlockPos(pos).down();
                        if (world.isAirBlock(abovePos) && !world.isAirBlock(belowPos)) {
                            BlockState block = world.getBlockState(pos);
                            if (block.getMaterial() != Material.AIR && block.isNormalCube(world, pos) && block.getBlock() != Blocks.BEDROCK && !block.getBlock().hasTileEntity(block)) {
                                FallingBlockEntity fallingBlock = new FallingBlockEntity(world, hitX + 0.5, hitY + 0.5, hitZ + 0.5, block);
                                fallingBlock.setMotion(0, 0.1, 0);
                                fallingBlock.fallTime = 2;
                                world.addEntity(fallingBlock);
                                world.removeBlock(pos, false);
                                int amount = 6 + world.rand.nextInt(10);
                                int stateId = Block.getStateId(block);
                                while (amount-- > 0) {
                                    double cx = px + world.rand.nextFloat() * 2 - 1;
                                    double cy = getBoundingBox().minY + 0.1 + world.rand.nextFloat() * 0.3;
                                    double cz = pz + world.rand.nextFloat() * 2 - 1;
//                                    world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, cx, cy, cz, 0, vx, 0.4 + world.rand.nextFloat() * 0.2F, vz, 1, stateId);
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
//                            world.spawnParticle(EnumParticleTypes.CLOUD, px + world.rand.nextFloat() * 2 - 1, entity.getBoundingBox().minY + 0.1 + world.rand.nextFloat() * 1.5, pz + world.rand.nextFloat() * 2 - 1, 0, velX, velY, velZ, 1);
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
            float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - posZ, entityHit.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = rotationYaw % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.posZ - posZ) * (entityHit.posZ - posZ) + (entityHit.posX - posX) * (entityHit.posX - posX));
            if (entityHit != caster && entityHitDistance <= range && entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2 || entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2) {
                if (caster instanceof PlayerEntity) ((PlayerEntity)caster).attackTargetEntityWithCurrentItem(entityHit);
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
