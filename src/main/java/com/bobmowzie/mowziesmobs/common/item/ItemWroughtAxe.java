package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemWroughtAxe extends ItemSword
{
    public int timer = 0;

    public ItemWroughtAxe()
    {
        super(Item.ToolMaterial.IRON);
        setCreativeTab(MMTabs.generic);
        setUnlocalizedName("wroughtAxe");
    }

    @Override
    public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity entityLiving, int p_77663_4_, boolean p_77663_5_)
    {
        if (entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).getHeldItem() != null && ((EntityPlayer)entityLiving).getHeldItem().getItem() == this) {
            if (timer > 0) timer--;
            if (timer == 15) {
                float damage = 7;
                boolean hit = false;
                float range = 4;
                float knockback = 1.2F;
                float arc = 100;
                List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby((EntityLivingBase) entityLiving, range, 2, range, range);
                for (EntityLivingBase entityHit : entitiesHit) {
                    float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - entityLiving.posZ, entityHit.posX - entityLiving.posX) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingAngle = entityLiving.rotationYaw % 360;
                    if (entityHitAngle < 0) entityHitAngle += 360;
                    if (entityAttackingAngle < 0) entityAttackingAngle += 360;
                    float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                    float entityHitDistance = (float) Math.sqrt((entityHit.posZ - entityLiving.posZ) * (entityHit.posZ - entityLiving.posZ) + (entityHit.posX - entityLiving.posX) * (entityHit.posX - entityLiving.posX));
                    if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) entityLiving), damage);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                        hit = true;
                    }
                }
                if (hit) entityLiving.playSound("minecraft:random.anvil_land", 0.3F, 0.5F);
            }
        }
        super.onUpdate(p_77663_1_, p_77663_2_, entityLiving, p_77663_4_, p_77663_5_);
    }

    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
    {
        return false;
    }

    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
    {
        p_77644_2_.playSound("minecraft:random.anvil_land", 0.3F, 0.5F);
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer entityLiving)
    {
        if (timer <= 0)
        {
            entityLiving.playSound("mowziesmobs:wroughtnautWhoosh", 0.5F, 1F);
            if (!p_77659_2_.isRemote) timer = 30;
        }
        return p_77659_1_;
    }

    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_)
    {
        return true;
    }

    public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
    {
        return 1.0F;
    }

    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.bow;
    }

    public List<EntityLivingBase> getEntityLivingBaseNearby(EntityLivingBase user, double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = user.worldObj.getEntitiesWithinAABBExcludingEntity(user, user.boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> listEntityLivingBase = new ArrayList<EntityLivingBase>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityLivingBase && user.getDistanceToEntity(entityNeighbor) <= radius)
                listEntityLivingBase.add((EntityLivingBase) entityNeighbor);
        }
        return listEntityLivingBase;
    }
}
