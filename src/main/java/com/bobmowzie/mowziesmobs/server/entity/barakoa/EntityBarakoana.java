package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityBarakoana extends EntityBarakoa implements LeaderSunstrikeImmune {
    private final List<EntityBarakoanToBarakoana> pack = new ArrayList<>();

    private final int packRadius = 3;

    public EntityBarakoana(EntityType<? extends EntityBarakoana> type, World world) {
        super(type, world);
        this.setMask(MaskType.FURY);
        this.experienceValue = 12;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(3, new BarakoaHurtByTargetAI(this, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, 200, true, false, target -> {
            float volume = target.getWidth() * target.getWidth() * target.getHeight();
            return (target.getAttribute(Attributes.ATTACK_DAMAGE) == null || target.getAttributeValue(Attributes.ATTACK_DAMAGE) < 3.0D) && volume > 0.1 && volume < 6;
        }));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 0, true, false, null));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, 0, true, false, null));
        this.targetSelector.addGoal(6, new AvoidEntityGoal<>(this, CreeperEntity.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, true, target -> {
            if (target instanceof PlayerEntity) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask);
            }
            return true;
        }));
    }

    @Override
    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MowzieEntity.createAttributes().createMutableAttribute(Attributes.ATTACK_DAMAGE, 6 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.attackMultiplier.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, 10 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.healthMultiplier.get());
    }

    @Override
    public void tick() {
        super.tick();

        for (int i = 0; i < pack.size(); i++) {
            pack.get(i).index = i;
        }

        if (!world.isRemote && pack != null) {
            float theta = (2 * (float) Math.PI / pack.size());
            for (int i = 0; i < pack.size(); i++) {
                EntityBarakoanToBarakoana hunter = pack.get(i);
                if (hunter.getAttackTarget() == null) {
                    hunter.getNavigator().tryMoveToXYZ(getPosX() + packRadius * MathHelper.cos(theta * i), getPosY(), getPosZ() + packRadius * MathHelper.sin(theta * i), 0.45);
                    if (getDistance(hunter) > 20 && onGround) {
                        hunter.setPosition(getPosX() + packRadius * MathHelper.cos(theta * i), getPosY(), getPosZ() + packRadius * MathHelper.sin(theta * i));
                    }
                }
            }
        }

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
        }
    }

    @Override
    public void remove() {
        if (ticksExisted == 0) {
            pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
        }
        super.remove();
    }

    @Override
    public boolean isNotColliding(IWorldReader worldReader) {
        if (ticksExisted == 0) {
            return !worldReader.containsAnyLiquid(this.getBoundingBox()) && worldReader.hasNoCollisions(this);
        }
        else {
            return !worldReader.containsAnyLiquid(this.getBoundingBox()) && worldReader.hasNoCollisions(this) && this.world.checkNoEntityCollision(this);
        }
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
            double x = getPosX() + packRadius * Math.cos(targetTheta);
            double z = getPosZ() + packRadius * Math.sin(targetTheta);
            for (int n = 0; n < pack.size(); n++) {
                EntityBarakoanToBarakoana tribeHunter = pack.get(n);
                double diffSq = (x - tribeHunter.getPosX()) * (x - tribeHunter.getPosX()) + (z - tribeHunter.getPosZ()) * (z - tribeHunter.getPosZ());
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

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
        int size = rand.nextInt(2) + 3;
        float theta = (2 * (float) Math.PI / size);
        for (int i = 0; i <= size; i++) {
            EntityBarakoanToBarakoana tribeHunter = new EntityBarakoanToBarakoana(EntityHandler.BARAKOAN_TO_BARAKOANA, this.world, this);
            tribeHunter.setPosition(getPosX() + 0.1 * MathHelper.cos(theta * i), getPosY(), getPosZ() + 0.1 * MathHelper.sin(theta * i));
            int weapon = rand.nextInt(3) == 0 ? 1 : 0;
            tribeHunter.setWeapon(weapon);
            world.addEntity(tribeHunter);
        }
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        pack.forEach(EntityBarakoanToBarakoana::removeLeader);
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.BARAKOA.spawnConfig;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        List<LivingEntity> nearby = getEntityLivingBaseNearby(20, 4, 20, 20);
        for (LivingEntity nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityBarakoana || nearbyEntity instanceof VillagerEntity || nearbyEntity instanceof EntityBarako || nearbyEntity instanceof AnimalEntity) {
                return false;
            }
        }
        return super.canSpawn(world, reason) && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDespawnPeaceful()) {
            this.remove();
        } else if (!this.isNoDespawnRequired() && !this.preventDespawn()) {
            Entity entity = this.world.getClosestPlayer(this, -1.0D);
            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
                idleTime = 0;
                entity = null;
            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
                this.remove();
                entity = null;
            }
            if (entity != null) {
                double d0 = entity.getDistanceSq(this);
                if (d0 > 16384.0D && this.canDespawn(d0)) {
                    pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                    this.remove();
                }

                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d0 > 1024.0D && this.canDespawn(d0)) {
                    pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                    this.remove();
                } else if (d0 < 1024.0D) {
                    this.idleTime = 0;
                }
            }

        } else {
            this.idleTime = 0;
        }
    }
}