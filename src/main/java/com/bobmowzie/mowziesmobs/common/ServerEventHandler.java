package com.bobmowzie.mowziesmobs.common;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribesman;
import com.bobmowzie.mowziesmobs.common.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.common.item.ItemHandler;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSolarBeam;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSummonSunstrike;
import com.bobmowzie.mowziesmobs.common.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.common.property.MMPlayerExtension;
import com.bobmowzie.mowziesmobs.common.property.WroughtAxeSwingProperty;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum ServerEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (event.entity instanceof EntityZombie) {
            ((EntityCreature) event.entity).tasks.addTask(2, new EntityAIAttackOnCollide((EntityCreature) event.entity, EntityFoliaath.class, 1.0D, false));
            ((EntityCreature) event.entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature) event.entity, EntityFoliaath.class, 0, true));

            ((EntityCreature) event.entity).tasks.addTask(2, new EntityAIAttackOnCollide((EntityCreature) event.entity, EntityTribesman.class, 1.0D, false));
            ((EntityCreature) event.entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature) event.entity, EntityTribesman.class, 0, true));
        }
        if (event.entity instanceof EntityOcelot) {
            ((EntityCreature) event.entity).tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (event.entity instanceof EntityCow) {
            ((EntityCreature) event.entity).tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.entity, EntityTribesman.class, 6.0F, 1.0D, 1.2D));
        }
        if (event.entity instanceof EntityPig) {
            ((EntityCreature) event.entity).tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.entity, EntityTribesman.class, 6.0F, 1.0D, 1.2D));
        }
        if (event.entity instanceof EntityChicken) {
            ((EntityCreature) event.entity).tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.entity, EntityTribesman.class, 6.0F, 1.0D, 1.2D));
        }
        if (event.entity instanceof EntitySheep) {
            ((EntityCreature) event.entity).tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.entity, EntityTribesman.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        EntityPlayer player = event.player;
        WroughtAxeSwingProperty property = WroughtAxeSwingProperty.getProperty(player);
        property.update();
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == ItemHandler.INSTANCE.itemWroughtAxe) {
            if (property.getTick() > 0) {
                property.decrementTime();
            }
            if (property.getTick() == WroughtAxeSwingProperty.SWING_HIT_TICK && !player.worldObj.isRemote) {
                float damage = 7;
                boolean hit = false;
                float range = 4;
                float knockback = 1.2F;
                float arc = 100;
                List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(player, range, 2, range, range);
                for (EntityLivingBase entityHit : entitiesHit) {
                    float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - player.posZ, entityHit.posX - player.posX) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingAngle = player.rotationYaw % 360;
                    if (entityHitAngle < 0) {
                        entityHitAngle += 360;
                    }
                    if (entityAttackingAngle < 0) {
                        entityAttackingAngle += 360;
                    }
                    float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                    float entityHitDistance = (float) Math.sqrt((entityHit.posZ - player.posZ) * (entityHit.posZ - player.posZ) + (entityHit.posX - player.posX) * (entityHit.posX - player.posX));
                    if (entityHitDistance <= range && entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2 || entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2) {
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                        hit = true;
                    }
                }
                if (hit) {
                    player.playSound("minecraft:random.anvil_land", 0.3F, 0.5F);
                }
            }
        }
        if (((MMPlayerExtension) event.player.getExtendedProperties("mm:player")).untilSunstrike > 0) {
            ((MMPlayerExtension) event.player.getExtendedProperties("mm:player")).untilSunstrike--;
        }
        if (event.side == Side.CLIENT) {
            return;
        }
        ItemStack headArmorStack = event.player.getEquipmentInSlot(4);
        if (headArmorStack == null) {
            return;
        }
        Item headItemStack = headArmorStack.getItem();
        if (headItemStack instanceof ItemBarakoaMask) {
            ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
            event.player.addPotionEffect(new PotionEffect(mask.getPotionEffectId(), 0, 0));
        }
    }

    @SubscribeEvent
    public void onEntityConstruction(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            event.entity.registerExtendedProperties("mm:player", new MMPlayerExtension());
        }
    }

    private List<EntityLivingBase> getEntityLivingBaseNearby(EntityLivingBase user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.worldObj.getEntitiesWithinAABBExcludingEntity(user, user.boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityLivingBase && user.getDistanceToEntity(entityNeighbor) <= radius).map(entityNeighbor -> (EntityLivingBase) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.world.isRemote && event.entityPlayer.inventory.getCurrentItem() == null && event.entityPlayer.isPotionActive(PotionHandler.INSTANCE.sunsBlessing) && ((MMPlayerExtension) event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike <= 0) {
            if (event.entityPlayer.isSneaking()) {
                MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerSolarBeam());
                ((MMPlayerExtension) event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike = 150;
            } else {
                MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerSummonSunstrike());
                ((MMPlayerExtension) event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike = 90;
            }
        }
    }
}
