package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.item.ItemDart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class EntityDart extends EntityArrow implements IProjectile {
    private int landedX = -1;
    private int landedY = -1;
    private int landedZ = -1;
    private Block blockHit;
    private int inData;
    private boolean inGround;
    public int damageIndex = -1;
    private Item arrows;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0D;
    private int knockbackStrength;
    public boolean isOctineArrow;
    public boolean isPoisonedAnglerToothArrow;
    public boolean isBasiliskArrow;

    public EntityDart(World world) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityDart(World world, double x, double y, double z) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        this.yOffset = 0.0F;
    }

    public EntityDart(World world, EntityLivingBase shooter, EntityLivingBase target, float direction, float speed) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D;
        double relativeX = target.posX - shooter.posX;
        double relativeY = target.boundingBox.minY + (double) (target.height / 3.0F) - posY;
        double relativeZ = target.posZ - shooter.posZ;
        double distance = (double) MathHelper.sqrt_double(relativeX * relativeX + relativeZ * relativeZ);

        if (distance >= 1.0E-7D) {
            float f2 = (float) (Math.atan2(relativeZ, relativeX) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float) (-(Math.atan2(relativeY, distance) * 180.0D / Math.PI));
            double d4 = relativeX / distance;
            double d5 = relativeZ / distance;
            this.setLocationAndAngles(shooter.posX + d4, posY, shooter.posZ + d5, f2, f3);
            this.yOffset = 0.0F;
            float f4 = (float) distance * 0.2F;
            this.setThrowableHeading(relativeX, relativeY + (double) f4, relativeZ, direction, speed);
        }
    }

    public EntityDart(World world, EntityLivingBase shooter, float speed) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(posX, posY, posZ);
        this.yOffset = 0.0F;
        this.motionX = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(motionX, motionY, motionZ, speed * 1.5F, 1.0F);
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(16, (byte) 0);
        dataWatcher.addObject(17, 0);
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float yaw, float pitch) {
        float distance = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double) distance;
        y /= (double) distance;
        z /= (double) distance;
        x += rand.nextGaussian() * (double) (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) pitch;
        y += rand.nextGaussian() * (double) (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) pitch;
        z += rand.nextGaussian() * (double) (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) pitch;
        x *= (double) yaw;
        y *= (double) yaw;
        z *= (double) yaw;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float horizontalDist = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = rotationPitch = (float) (Math.atan2(y, (double) horizontalDist) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation2(double x, double y, double z, float rotationYaw, float rotationPitch, int par9) {
        this.setPosition(x, y, z);
        this.setRotation(rotationYaw, rotationPitch);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float horizontalDist = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = rotationPitch = (float) (Math.atan2(y, (double) horizontalDist) * 180.0D / Math.PI);
            this.prevRotationPitch = rotationPitch;
            this.prevRotationYaw = rotationYaw;
            this.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onEntityUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float horizontalMotion = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(motionY, (double) horizontalMotion) * 180.0D / Math.PI);
        }

        Block block = worldObj.getBlock(landedX, landedY, landedZ);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(worldObj, landedX, landedY, landedZ);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(worldObj, landedX, landedY, landedZ);

            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ))) {
                inGround = true;
            }
        }

        if (arrowShake > 0) {
            --arrowShake;
        }

        if (inGround) {
            int metadata = worldObj.getBlockMetadata(landedX, landedY, landedZ);

            if (block == blockHit && metadata == inData) {
                ticksInGround++;

                if (ticksInGround >= 1200) {
                    this.setDead();
                } else if (isOctineArrow && ticksInGround == 1 && worldObj.getBlock(this.landedX, this.landedY + 1, this.landedZ).isReplaceable(worldObj, this.landedX, this.landedY + 1, this.landedZ)) {
                    worldObj.setBlock(this.landedX, this.landedY + 1, this.landedZ, Blocks.fire);
                }
            } else {
                this.inGround = false;
                this.motionX *= (double) (rand.nextFloat() * 0.2F);
                this.motionY *= (double) (rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++ticksInAir;
            Vec3 pos = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 nextTickPos = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition hit = worldObj.func_147447_a(pos, nextTickPos, false, true, false);
            pos = Vec3.createVectorHelper(posX, posY, posZ);
            nextTickPos = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

            if (hit != null) {
                nextTickPos = Vec3.createVectorHelper(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord);
            }

            Entity entity = null;
            List colliding = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (Object collidingObject : colliding)
            {
                Entity collidingEntity = (Entity) collidingObject;

                if (collidingEntity.canBeCollidedWith() && (collidingEntity != shootingEntity || ticksInAir >= 5))
                {
                    float expand = 0.3F;
                    AxisAlignedBB axisalignedbb1 = collidingEntity.boundingBox.expand((double) expand, (double) expand, (double) expand);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(pos, nextTickPos);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = pos.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = collidingEntity;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                hit = new MovingObjectPosition(entity);
            }

            if (hit != null && hit.entityHit != null && hit.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) hit.entityHit;

                if (entityplayer.capabilities.disableDamage || shootingEntity instanceof EntityPlayer && !((EntityPlayer) shootingEntity).canAttackPlayer(entityplayer)) {
                    hit = null;
                }
            }

            float motion;
            float f4;

            if (hit != null) {
                if (hit.entityHit != null) {
                    motion = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    int damage = MathHelper.ceiling_double_int((double) motion * this.damage);

                    if (getIsCritical()) {
                        damage += rand.nextInt(damage / 2 + 2);
                    }

                    DamageSource damageSource = ItemDart.causeArrowDamage(this, shootingEntity == null ? this : shootingEntity);

                    if (isBurning() && !(hit.entityHit instanceof EntityEnderman)) {
                        hit.entityHit.setFire(5);
                    }

                    if (!(hit.entityHit instanceof EntityTribesman && shootingEntity instanceof EntityTribesman) && hit.entityHit.attackEntityFrom(damageSource, (float) damage)) {
                        if (hit.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase) hit.entityHit;

                            if (!worldObj.isRemote) {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (knockbackStrength > 0) {
                                f4 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

                                if (f4 > 0.0F) {
                                    hit.entityHit.addVelocity(motionX * (double) knockbackStrength * 0.6000000238418579D / (double) f4, 0.1D, motionZ * (double) knockbackStrength * 0.6000000238418579D / (double) f4);
                                }
                            }

                            if (shootingEntity != null && shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(entitylivingbase, shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) shootingEntity, entitylivingbase);
                            }

                            if (shootingEntity != null && hit.entityHit != shootingEntity && hit.entityHit instanceof EntityPlayer && shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                            entitylivingbase.addPotionEffect(new PotionEffect(Potion.poison.getId(), 40, 2, false));
                        }

                        playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));

                        if (!(hit.entityHit instanceof EntityEnderman)) {
                            setDead();
                        }
                    } else {
                        motionX *= -0.10000000149011612D;
                        motionY *= -0.10000000149011612D;
                        motionZ *= -0.10000000149011612D;
                        rotationYaw += 180.0F;
                        prevRotationYaw += 180.0F;
                        ticksInAir = 0;
                    }
                } else {
                    this.landedX = hit.blockX;
                    this.landedY = hit.blockY;
                    this.landedZ = hit.blockZ;
                    this.blockHit = block;
                    this.inData = worldObj.getBlockMetadata(landedX, landedY, landedZ);

                    this.motionX = (double) ((float) (hit.hitVec.xCoord - posX));
                    this.motionY = (double) ((float) (hit.hitVec.yCoord - posY));
                    this.motionZ = (double) ((float) (hit.hitVec.zCoord - posZ));
                    motion = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);

                    this.posX -= motionX / (double) motion * 0.05000000074505806D;
                    this.posY -= motionY / (double) motion * 0.05000000074505806D;
                    this.posZ -= motionZ / (double) motion * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);

                    if (blockHit.getMaterial() != Material.air) {
                        blockHit.onEntityCollidedWithBlock(worldObj, landedX, landedY, landedZ, this);
                    }
                }
            }

            if (this.getIsCritical()) {
                for (int i = 0; i < 4; i++) {
                    worldObj.spawnParticle("crit", posX + motionX * (double) i / 4.0D, posY + motionY * (double) i / 4.0D, posZ + motionZ * (double) i / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
                }
            }

            this.posX += motionX;
            this.posY += motionY;
            this.posZ += motionZ;
            this.rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

            while (rotationPitch - prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (rotationYaw - prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (rotationYaw - prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
            this.rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
            float friction = 0.99F;

            if (this.isInWater()) {
                for (int l = 0; l < 4; ++l) {
                    f4 = 0.25F;
                    worldObj.spawnParticle("bubble", posX - motionX * (double) f4, posY - motionY * (double) f4, posZ - motionZ * (double) f4, motionX, motionY, motionZ);
                }

                friction = 0.8F;
            }

            if (isWet()) {
                this.extinguish();
            }

            this.motionX *= (double) friction;
            this.motionY *= (double) friction;
            this.motionZ *= (double) friction;
            this.motionY -= 0.05;
            this.setPosition(posX, posY, posZ);
            this.func_145775_I();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("xTile", (short) landedX);
        compound.setShort("yTile", (short) landedY);
        compound.setShort("zTile", (short) landedZ);
        compound.setShort("life", (short) ticksInGround);
        compound.setByte("inTile", (byte) Block.getIdFromBlock(blockHit));
        compound.setByte("inData", (byte) inData);
        compound.setByte("shake", (byte) arrowShake);
        compound.setByte("inGround", (byte) (inGround ? 1 : 0));
        compound.setByte("pickup", (byte) canBePickedUp);
        compound.setDouble("damage", damage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        landedX = compound.getShort("xTile");
        landedY = compound.getShort("yTile");
        landedZ = compound.getShort("zTile");
        ticksInGround = compound.getShort("life");
        blockHit = Block.getBlockById(compound.getByte("inTile") & 255);
        inData = compound.getByte("inData") & 255;
        arrowShake = compound.getByte("shake") & 255;

        if (compound.hasKey("damage", 99)) {
            damage = compound.getDouble("damage");
        }

        if (compound.hasKey("pickup", 99)) {
            canBePickedUp = compound.getByte("pickup");
        } else if (compound.hasKey("player", 99)) {
            canBePickedUp = compound.getBoolean("player") ? 1 : 0;
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (!worldObj.isRemote && inGround && arrowShake <= 0) {
            boolean canPickUp = canBePickedUp == 1 || canBePickedUp == 2 && player.capabilities.isCreativeMode;

            if (canBePickedUp == 1 && !player.inventory.addItemStackToInventory(new ItemStack(arrows, 1))) {
                canPickUp = false;
            }

            if (canPickUp) {
                this.playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public void setDamage(double damageAmount) {
        damage = damageAmount;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public void setKnockbackStrength(int knockBack) {
        knockbackStrength = knockBack;
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    @Override
    public void setIsCritical(boolean isCritical) {
        byte flags = dataWatcher.getWatchableObjectByte(16);

        if (isCritical) {
            dataWatcher.updateObject(16, (byte) (flags | 1));
        } else {
            dataWatcher.updateObject(16, (byte) (flags & -2));
        }
    }

    @Override
    public boolean getIsCritical() {
        byte flags = dataWatcher.getWatchableObjectByte(16);
        return (flags & 1) != 0;
    }
}