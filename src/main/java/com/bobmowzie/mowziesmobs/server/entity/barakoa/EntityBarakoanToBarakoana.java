package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;

public class EntityBarakoanToBarakoana extends EntityBarakoan<EntityBarakoana> implements LeaderSunstrikeImmune {
    public EntityBarakoanToBarakoana(World world) {
        this(world, null);
    }

    public EntityBarakoanToBarakoana(World world, EntityBarakoana leader) {
        super(world, EntityBarakoana.class, leader);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (leader != null) {
            setAttackTarget(leader.getAttackTarget());
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
        Entity entity = source.getTrueSource();
        if (entity != null && entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isCreativeMode)) {
                if (leader != null) {
                    leader.setAttackTarget((EntityLivingBase) entity);
                } else {
                    this.setAttackTarget((EntityLivingBase) entity);
                }
            }
        }
        return super.attackEntityFrom(source, damage);
    }

    public void removeLeader() {
        this.setLeaderUUID(ABSENT_LEADER);
        this.leader = null;
        this.setAttackTarget(null);
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true, false));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityCow.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityPig.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntitySheep.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityChicken.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, 0, true, false, null));
    }
}
