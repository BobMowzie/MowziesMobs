package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class EntityBarakoanToBarakoana extends EntityBarakoan<EntityBarakoana> implements LeaderSunstrikeImmune, IMob {
    public EntityBarakoanToBarakoana(EntityType<? extends EntityBarakoanToBarakoana> type, World world) {
        this(type, world, null);
    }

    public EntityBarakoanToBarakoana(EntityType<? extends EntityBarakoanToBarakoana> type, World world, EntityBarakoana leader) {
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
            setAttackTarget(leader.getAttackTarget());
        }

        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
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
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, true, target -> {
            if (target instanceof PlayerEntity) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask);
            }
            return true;
        }));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, 200, true, false, target -> {
            float volume = target.getWidth() * target.getWidth() * target.getHeight();
            return (target.getAttribute(Attributes.ATTACK_DAMAGE) == null || target.getAttributeValue(Attributes.ATTACK_DAMAGE) < 3.0D) && volume > 0.1 && volume < 6;
        }));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 0, true, false, (e) -> !(e instanceof ZombifiedPiglinEntity)));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, 0, true, false, null));
    }
}
