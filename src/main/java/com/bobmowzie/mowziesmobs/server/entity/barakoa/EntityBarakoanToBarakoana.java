package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class EntityBarakoanToBarakoana extends EntityBarakoan<EntityBarakoana> implements LeaderSunstrikeImmune {
    public EntityBarakoanToBarakoana(World world) {
        this(world, null);
    }

    public EntityBarakoanToBarakoana(World world, EntityBarakoana leader) {
        super(world, EntityBarakoana.class, leader);
        this.targetTasks.addTask(3, new BarakoaHurtByTargetAI(this, true));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (leader != null) {
            setAttackTarget(leader.getAttackTarget());
        }

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.setDead();
        }
    }

    @Override
    protected int getTribeCircleTick() {
        if (leader == null) return 0;
        return leader.circleTick;
    }

    @Override
    protected int getPackSize() {
        if (leader == null) return 0;
        return leader.getPackSize();
    }

    @Override
    protected void addAsPackMember() {
        if (leader == null) return;
        leader.addPackMember(this);
    }

    @Override
    protected void removeAsPackMember() {
        if (leader == null) return;
        leader.removePackMember(this);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        /*Entity entity = source.getTrueSource();
        if (entity != null && entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isCreativeMode)) {
                if (leader != null) {
                    leader.setAttackTarget((EntityLivingBase) entity);
                } else {
                    this.setAttackTarget((EntityLivingBase) entity);
                }
            }
        }*/
        return super.attackEntityFrom(source, damage);
    }

    public void removeLeader() {
        this.setLeaderUUID(ABSENT_LEADER);
        this.leader = null;
        this.setAttackTarget(null);
        this.targetTasks.addTask(4, new BarakoaAttackTargetAI(this, PlayerEntity.class, 0, true, false));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, CowEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, PigEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, SheepEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, 0, true, false, null));
        this.targetTasks.addTask(5, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 0, true, false, null));
    }
}
