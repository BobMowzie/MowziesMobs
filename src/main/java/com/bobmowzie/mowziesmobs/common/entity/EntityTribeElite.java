package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.ai.AINearestAttackableTargetBarakoa;
import com.bobmowzie.mowziesmobs.common.animation.AnimationBlockAI;
import cpw.mods.fml.common.eventhandler.Event;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

public class EntityTribeElite extends EntityTribesman {
    private List<EntityTribeHunter> pack = new ArrayList<>();

    private int packRadius = 3;

    private boolean persistenceRequired;

    public static final Animation BLOCK_ANIMATION = Animation.create(3, 10);

    public EntityTribeElite(World world) {
        super(world);
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityCow.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityPig.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityChicken.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, true));
        targetTasks.addTask(3, new AINearestAttackableTargetBarakoa(this, EntityPlayer.class, 0, true));
        tasks.addTask(2, new AnimationBlockAI<>(this, BLOCK_ANIMATION));
        setMask(1);
        setSize(0.7f, 2f);
        experienceValue = 12;
    }

    @Override
    public int getAttack() {
        return 6;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        for (int i = 0; i < pack.size(); i++) {
            pack.get(i).index = i;
        }

        if (!worldObj.isRemote && pack != null) {
            float theta = (2 * (float) Math.PI / pack.size());
            for (int i = 0; i < pack.size(); i++) {
                if (pack.get(i).getAttackTarget() == null) {
                    pack.get(i).getNavigator().tryMoveToXYZ(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i), 0.45);
                    if (getDistanceToEntity(pack.get(i)) > 20) {
                        pack.get(i).setPosition(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i));
                    }
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (entity != null && entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isCreativeMode)) {
                setAttackTarget((EntityLivingBase) entity);
            }
        }
        if (entity != null && entity instanceof EntityLivingBase && (getAnimation() == NO_ANIMATION || getAnimation() == DAMAGE_ANIMATION || getAnimation() == BLOCK_ANIMATION)) {
            blockingEntity = (EntityLivingBase) entity;
            playSound("mob.zombie.wood", 0.3f, 1.5f);
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BLOCK_ANIMATION);
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    @Override
    public Animation[] getEntityAnimations() {
        return new Animation[]{BLOCK_ANIMATION};
    }

    public int getpackSize() {
        return pack.size();
    }

    public void removePackMember(EntityTribeHunter tribeHunter) {
        pack.remove(tribeHunter);
        sortPackMembers();
    }

    public void addPackMember(EntityTribeHunter tribeHunter) {
        pack.add(tribeHunter);
        sortPackMembers();
    }

    private void sortPackMembers() {
        double theta = 2 * Math.PI / pack.size();
        for (int i = 0; i < pack.size(); i++) {
            int nearestIndex = -1;
            double smallestDiffSq = Double.MAX_VALUE;
            double targetTheta = theta * i;
            double x = posX + packRadius * Math.cos(targetTheta);
            double z = posZ + packRadius * Math.sin(targetTheta);
            for (int n = 0; n < pack.size(); n++) {
                EntityTribeHunter tribeHunter = pack.get(n);
                double diffSq = (x - tribeHunter.posX) * (x - tribeHunter.posX) + (z - tribeHunter.posZ) * (z - tribeHunter.posZ);
                if (diffSq < smallestDiffSq) {
                    smallestDiffSq = diffSq;
                    nearestIndex = n;
                }
            }
            if (nearestIndex == -1) {
                throw new ArithmeticException("All pack members have NaN x and z?");
            }
            pack.add(i, pack.remove(nearestIndex));
        }
    }

    public int getPackSize() {
        return pack.size();
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        int size = rand.nextInt(2) + 3;
        for (int i = 0; i <= size; i++) {
            EntityTribeHunter tribeHunter = new EntityTribeHunter(worldObj, this);
            pack.add(tribeHunter);
            tribeHunter.setLeaderUUID(getUniqueID().toString());
            tribeHunter.setPosition(posX + 0.1 * i, posY, posZ);
            int weapon = 0;
            if (rand.nextInt(3) == 0) {
                weapon = 1;
            }
            tribeHunter.setWeapon(weapon);
            worldObj.spawnEntityInWorld(tribeHunter);
        }
        return super.onSpawnWithEgg(data);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        for (EntityTribeHunter aPack : pack) {
            aPack.removeLeader();
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        List<EntityLivingBase> nearby = getEntityLivingBaseNearby(10, 4, 10, 10);
        for (EntityLivingBase aNearby : nearby) {
            if (aNearby instanceof EntityTribeElite) {
                return false;
            }
        }
        if (worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox)) {
            int x = MathHelper.floor_double(posX);
            int y = MathHelper.floor_double(boundingBox.minY);
            int z = MathHelper.floor_double(posZ);

            if (y < 63) {
                return false;
            }

            Block block = worldObj.getBlock(x, y - 1, z);

            if (block == Blocks.grass) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void despawnEntity() {

        Event.Result result = null;
//        if (this.persistenceRequired)
//        {
//            this.entityAge = 0;
//        }
        if ((this.entityAge & 0x1F) == 0x1F && (result = ForgeEventFactory.canEntityDespawn(this)) != Event.Result.DEFAULT) {
            if (result == Event.Result.DENY) {
                this.entityAge = 0;
            } else {
                for (EntityTribeHunter aPack : pack) {
                    aPack.setDead();
                }
                this.setDead();
            }
        } else {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

            if (entityplayer != null) {
                double d0 = entityplayer.posX - this.posX;
                double d1 = entityplayer.posY - this.posY;
                double d2 = entityplayer.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.canDespawn() && d3 > 16384.0D) {
                    for (EntityTribeHunter aPack : pack) {
                        aPack.setDead();
                    }
                    this.setDead();
                }

                if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
                    for (EntityTribeHunter aPack : pack) {
                        aPack.setDead();
                    }
                    this.setDead();
                } else if (d3 < 1024.0D) {
                    this.entityAge = 0;
                }
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.persistenceRequired = compound.getBoolean("PersistenceRequired");
    }
}