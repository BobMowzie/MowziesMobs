package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class EntityUmvuthanaFollowerToRaptor extends EntityUmvuthanaFollower<EntityUmvuthanaRaptor> implements LeaderSunstrikeImmune, Enemy {
    public EntityUmvuthanaFollowerToRaptor(EntityType<? extends EntityUmvuthanaFollowerToRaptor> type, Level world) {
        this(type, world, null);
    }

    public EntityUmvuthanaFollowerToRaptor(EntityType<? extends EntityUmvuthanaFollowerToRaptor> type, Level world, EntityUmvuthanaRaptor leader) {
        super(type, world, EntityUmvuthanaRaptor.class, leader);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new UmvuthanaHurtByTargetAI(this, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (leader != null) {
            setTarget(leader.getTarget());
        }

        if (!this.level().isClientSide && this.level().getDifficulty() == Difficulty.PEACEFUL)
        {
            this.discard() ;
        }
    }

    @Override
    protected int getGroupCircleTick() {
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
