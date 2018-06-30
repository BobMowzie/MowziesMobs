package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
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
public class EntityAxeAttack extends Entity {
    private static final DataParameter<Boolean> VERTICAL = EntityDataManager.createKey(EntityAxeAttack.class, DataSerializers.BOOLEAN);

    public int swingDurationHoriz = 24;
    public int swingDurationVert = 30;
    private float quakeAngle = 0;
    private BlockPos quakePos = new BlockPos(0, 0, 0);
    private AxisAlignedBB quakeBB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    private EntityLivingBase caster;

    public EntityAxeAttack(World world) {
        super(world);
    }

    public EntityAxeAttack(World world, EntityLivingBase caster, boolean vertical) {
        this(world);
        this.caster = caster;
        setPositionAndRotation(caster.posX, caster.posY, caster.posZ, caster.rotationYaw, caster.rotationPitch);
        setVertical(vertical);
    }

    @Override
    protected void entityInit() {
        getDataManager().register(VERTICAL, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (caster != null) setPositionAndRotation(caster.posX, caster.posY, caster.posZ, caster.rotationYaw, caster.rotationPitch);
        if (!world.isRemote && ticksExisted == 7) playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 1, 0.8f);
            if (!world.isRemote && caster != null) {
                if (!getVertical() && ticksExisted == swingDurationHoriz/2 - 1) dealDamage(7, 4.5f, 160, 1.2f);
                else if (getVertical() && ticksExisted == swingDurationVert/2 - 1) {
                    dealDamage(9, 4.5f, 40, 0.8f);
                    quakeAngle = rotationYaw;
                    quakePos = new BlockPos(posX, posY, posZ);
                    quakeBB = getEntityBoundingBox();
                    playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
                    playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 0.9F + rand.nextFloat() * 0.1F);                }
            }

        if (getVertical() && caster != null) {
            if (ticksExisted >= swingDurationVert/2) {
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
                        if (entity == this || entity instanceof EntityFallingBlock || entity == caster) {
                            continue;
                        }
                        if (entity instanceof EntityLivingBase) {
                            entity.attackEntityFrom(DamageSource.FALLING_BLOCK, factor * 5 + 1);
                        }
                        double magnitude = -0.2;
                        entity.motionX += vx * (1 - factor) * magnitude;
                        if (entity.onGround) {
                            entity.motionY += 0.15;
                        }
                        entity.motionZ += vz * (1 - factor) * magnitude;
                        if (entity instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
                        }
                    }
                    if (world.rand.nextBoolean()) {
                        int hitX = MathHelper.floor(px);
                        int hitZ = MathHelper.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        Block groundBlock = world.getBlockState(pos).getBlock();
                        if (world.isAirBlock(pos.up())) {
                            IBlockState block = world.getBlockState(pos);
                            if (block.getMaterial() != Material.AIR && block.isBlockNormalCube() && groundBlock != Blocks.BEDROCK) {
                                EntityFallingBlock fallingBlock = new EntityFallingBlock(world, hitX + 0.5, hitY + 0.5, hitZ + 0.5, block);
                                fallingBlock.motionX = 0;
                                fallingBlock.motionY = 0.1;
                                fallingBlock.motionZ = 0;
                                fallingBlock.fallTime = 2;
                                world.spawnEntity(fallingBlock);
                                world.setBlockToAir(pos);
                                int amount = 6 + world.rand.nextInt(10);
                                int stateId = Block.getStateId(block);
                                while (amount-- > 0) {
                                    double cx = px + world.rand.nextFloat() * 2 - 1;
                                    double cy = getEntityBoundingBox().minY + 0.1 + world.rand.nextFloat() * 0.3;
                                    double cz = pz + world.rand.nextFloat() * 2 - 1;
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
                        }
                    }
                }
            }
        }
        if (ticksExisted > swingDurationHoriz) setDead();
    }

    private void dealDamage(float damage, float range, float arc, float knockback) {
        boolean hit = false;
        List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(range, 2, range, range);
        for (EntityLivingBase entityHit : entitiesHit) {
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
                entityHit.attackEntityFrom(DamageSource.causeMobDamage(caster), damage);
                entityHit.motionX *= knockback;
                entityHit.motionZ *= knockback;
                hit = true;
            }
        }
        if (hit) {
            playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    public void setVertical(boolean vertical) {
        getDataManager().set(VERTICAL, vertical);
    }

    public boolean getVertical() {
        return getDataManager().get(VERTICAL);
    }

    private List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityLivingBase && getDistanceToEntity(entityNeighbor) <= radius).map(entityNeighbor -> (EntityLivingBase) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }
}
