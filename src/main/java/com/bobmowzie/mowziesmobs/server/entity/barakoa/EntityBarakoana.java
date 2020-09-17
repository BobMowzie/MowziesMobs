package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

public class EntityBarakoana extends EntityBarakoa implements LeaderSunstrikeImmune {
    private List<EntityBarakoanToBarakoana> pack = new ArrayList<>();

    private int packRadius = 3;

    public EntityBarakoana(EntityType<? extends EntityBarakoana> type, World world) {
        super(type, world);
        this.targetTasks.addTask(3, new BarakoaHurtByTargetAI(this, true));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, CowEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, PigEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, SheepEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, 0, true, false, null));
        this.targetTasks.addTask(6, new AvoidEntityGoal(this, CreeperEntity.class, 6.0F, 1.0D, 1.2D));
        this.targetTasks.addTask(3, new BarakoaAttackTargetAI(this, PlayerEntity.class, 0, true, false));
        this.setMask(MaskType.FURY);
        this.experienceValue = 12;
    }

    @Override
    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        for (int i = 0; i < pack.size(); i++) {
            pack.get(i).index = i;
        }

        if (!world.isRemote && pack != null) {
            float theta = (2 * (float) Math.PI / pack.size());
            for (int i = 0; i < pack.size(); i++) {
                EntityBarakoanToBarakoana hunter = pack.get(i);
                if (hunter.getAttackTarget() == null) {
                    hunter.getNavigator().tryMoveToXYZ(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i), 0.45);
                    if (getDistance(hunter) > 20 && onGround) {
                        hunter.setPosition(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i));
                    }
                }
            }
        }

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.setDead();
        }
    }

    @Override
    public void setDead() {
        if (ticksExisted == 0) {
            pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
        }
        super.setDead();
    }

    @Override
    public boolean isNotColliding()
    {
        if (ticksExisted == 0) {
            return !this.world.containsAnyLiquid(this.getBoundingBox()) && this.world.getCollisionBoxes(this, this.getBoundingBox()).isEmpty();
        }
        else {
            return !this.world.containsAnyLiquid(this.getBoundingBox()) && this.world.getCollisionBoxes(this, this.getBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getBoundingBox(), this);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        /*Entity entity = source.getTrueSource();
        if (entity != null && entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isCreativeMode)) {
                setAttackTarget((EntityLivingBase) entity);
            }
        }*/
        return super.attackEntityFrom(source, damage);
    }

    public void removePackMember(EntityBarakoanToBarakoana tribeHunter) {
        pack.remove(tribeHunter);
        sortPackMembers();
    }

    public void addPackMember(EntityBarakoanToBarakoana tribeHunter) {
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
                EntityBarakoanToBarakoana tribeHunter = pack.get(n);
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
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, ILivingEntityData data) {
        int size = rand.nextInt(2) + 3;
        float theta = (2 * (float) Math.PI / size);
        for (int i = 0; i <= size; i++) {
            EntityBarakoanToBarakoana tribeHunter = new EntityBarakoanToBarakoana(world, this);
            tribeHunter.setPosition(posX + 0.1 * MathHelper.cos(theta * i), posY, posZ + 0.1 * MathHelper.sin(theta * i));
            int weapon = rand.nextInt(3) == 0 ? 1 : 0;
            tribeHunter.setWeapon(weapon);
            world.spawnEntity(tribeHunter);
        }
        return super.onInitialSpawn(difficulty, data);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        pack.forEach(EntityBarakoanToBarakoana::removeLeader);
    }

    @Override
    protected ConfigHandler.SpawnData getSpawnConfig() {
        return ConfigHandler.MOBS.BARAKOA.spawnData;
    }

    @Override
    public boolean getCanSpawnHere() {
        List<LivingEntity> nearby = getEntityLivingBaseNearby(20, 4, 20, 20);
        for (LivingEntity nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityBarakoana || nearbyEntity instanceof VillagerEntity || nearbyEntity instanceof EntityBarako || nearbyEntity instanceof AnimalEntity) {
                return false;
            }
        }
        return super.getCanSpawnHere() && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Override
    protected void despawnEntity() {
        Event.Result result;
        if (isNoDespawnRequired()) {
        	idleTime = 0;
        } else if ((idleTime & 0x1F) == 0x1F && (result = ForgeEventFactory.canEntityDespawn(this)) != Event.Result.DEFAULT) {
            if (result == Event.Result.DENY) {
            	idleTime = 0;
            } else {
                pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                setDead();
            }
        } else {
            PlayerEntity closestPlayer = world.getClosestPlayerToEntity(this, -1);
            if (closestPlayer != null) {
                double dX = closestPlayer.posX - posX;
                double dY = closestPlayer.posY - posY;
                double dZ = closestPlayer.posZ - posZ;
                double distance = dX * dX + dY * dY + dZ * dZ;
                if (canDespawn() && distance > 16384) {
                    pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                    setDead();
                }
                if (idleTime > 600 && rand.nextInt(800) == 0 && distance > 1024 && canDespawn()) {
                    pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                    setDead();
                } else if (distance < 1024) {
                	idleTime = 0;
                }
            }
        }
    }
}