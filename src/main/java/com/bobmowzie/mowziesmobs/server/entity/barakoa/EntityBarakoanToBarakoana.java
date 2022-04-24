package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.ChickenEntity;
import net.minecraft.world.entity.animal.SheepEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

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

        if (!this.level.isClientSide && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
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
