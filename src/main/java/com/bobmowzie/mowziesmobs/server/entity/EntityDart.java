package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDart extends ArrowEntity {
    public EntityDart(EntityType<? extends EntityDart> type, World world) {
        super(type, world);
    }

    public EntityDart(World world, LivingEntity shooter) {
        super(world, shooter);
        setDamage(ConfigHandler.TOOLS_AND_ABILITIES.BLOW_GUN.attackDamage);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ItemHandler.DART);
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        if (getShooter() instanceof PlayerEntity) living.addPotionEffect(new EffectInstance(Effects.POISON, ConfigHandler.TOOLS_AND_ABILITIES.BLOW_GUN.poisonDuration, 3, false, true));
        else living.addPotionEffect(new EffectInstance(Effects.POISON, 20, 1, false, true));
        living.setArrowCountInEntity(living.getArrowCountInEntity() - 1);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.getType() == RayTraceResult.Type.ENTITY) {
            Entity hit = ((EntityRayTraceResult) raytraceResultIn).getEntity();
            Entity shooter = getShooter();
            if (hit instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) hit;
                if (world.isRemote || (shooter == hit) || (shooter instanceof EntityBarakoa && living instanceof EntityBarakoa && ((EntityBarakoa) shooter).isBarakoDevoted() == ((EntityBarakoa) living).isBarakoDevoted()))
                    return;
            }
            super.onHit(raytraceResultIn);
        }
    }
}