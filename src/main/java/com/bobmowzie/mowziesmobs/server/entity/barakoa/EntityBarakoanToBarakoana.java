package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.world.entity.monster.Enemy;

public class EntityBarakoanToBarakoana extends EntityBarakoan<EntityBarakoana> implements LeaderSunstrikeImmune, Enemy {
    public EntityBarakoanToBarakoana(EntityType<? extends EntityBarakoanToBarakoana> type, Level world) {
        this(type, world, null);
    }

    public EntityBarakoanToBarakoana(EntityType<? extends EntityBarakoanToBarakoana> type, Level world, EntityBarakoana leader) {
        super(type, world, EntityBarakoana.class, leader);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new BarakoaHurtByTargetAI(this, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (leader != null) {
            setTarget(leader.getTarget());
        }

        if (!this.level.isClientSide && this.level.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.discard() ;
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

    public void removeLeader() {
        this.setLeaderUUID(ABSENT_LEADER);
        this.leader = null;
        this.setTarget(null);
    }

    @Override
    public void setLeaderUUID(Optional<UUID> uuid) {
        super.setLeaderUUID(uuid);
        if (uuid == ABSENT_LEADER) registerHuntingTargetGoals();
    }
}
