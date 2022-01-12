package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.entity.projectile.ArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.math.EntityRayTraceResult;
import net.minecraft.resources.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class EntityDart extends ArrowEntity {
    public EntityDart(EntityType<? extends EntityDart> type, World world) {
        super(type, world);
    }

    public EntityDart(EntityType<? extends EntityDart> type, World worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        setDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BLOW_GUN.attackDamage.get());
    }

    public EntityDart(EntityType<? extends EntityDart> type, World world, LivingEntity shooter) {
        this(type, world, shooter.getPosX(),shooter.getPosY() + (double)shooter.getEyeHeight() - (double)0.1F, shooter.getPosZ());
        this.setShooter(shooter);
        if (shooter instanceof Player) {
            this.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemHandler.DART);
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        if (getShooter() instanceof Player) living.addPotionEffect(new MobEffectInstance(MobEffects.POISON, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BLOW_GUN.poisonDuration.get(), 3, false, true));
        else living.addPotionEffect(new MobEffectInstance(MobEffects.POISON, 30, 1, false, true));
        living.setArrowCountInEntity(living.getArrowCountInEntity() - 1);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult raytraceResultIn) {
        if (raytraceResultIn.getType() == RayTraceResult.Type.ENTITY) {
            Entity hit = raytraceResultIn.getEntity();
            Entity shooter = getShooter();
            if (hit instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) hit;
                if (world.isClientSide || (shooter == hit) || (shooter instanceof EntityBarakoa && living instanceof EntityBarakoa && ((EntityBarakoa) shooter).isBarakoDevoted() == ((EntityBarakoa) living).isBarakoDevoted()))
                    return;
            }
        }
        super.onEntityHit(raytraceResultIn);
    }
}