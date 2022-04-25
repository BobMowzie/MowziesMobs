package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDart extends Arrow {
    public EntityDart(EntityType<? extends EntityDart> type, Level world) {
        super(type, world);
    }

    public EntityDart(EntityType<? extends EntityDart> type, Level worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        setBaseDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BLOW_GUN.attackDamage.get());
    }

    public EntityDart(EntityType<? extends EntityDart> type, Level world, LivingEntity shooter) {
        this(type, world, shooter.getX(),shooter.getY() + (double)shooter.getEyeHeight() - (double)0.1F, shooter.getZ());
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ItemHandler.DART);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        if (getOwner() instanceof Player) living.addEffect(new MobEffectInstance(MobEffects.POISON, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BLOW_GUN.poisonDuration.get(), 3, false, true));
        else living.addEffect(new MobEffectInstance(MobEffects.POISON, 30, 1, false, true));
        living.setArrowCount(living.getArrowCount() - 1);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult raytraceResultIn) {
        if (raytraceResultIn.getType() == HitResult.Type.ENTITY) {
            Entity hit = raytraceResultIn.getEntity();
            Entity shooter = getOwner();
            if (hit instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) hit;
                if (
                        level.isClientSide ||
                        (shooter == hit) ||
                        (shooter instanceof EntityBarakoa && living instanceof EntityBarakoa && ((EntityBarakoa) shooter).isBarakoDevoted() == ((EntityBarakoa) living).isBarakoDevoted()) ||
                        (shooter instanceof EntityBarakoanToPlayer && living == ((EntityBarakoanToPlayer) shooter).getLeader())
                )
                    return;
            }
        }
        super.onHitEntity(raytraceResultIn);
    }
}