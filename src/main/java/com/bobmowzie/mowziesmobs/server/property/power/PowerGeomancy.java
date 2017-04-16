package com.bobmowzie.mowziesmobs.server.property.power;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.tools.nsc.interpreter.IMain;

public class PowerGeomancy extends Power {

    int spawnBoulderCooldown = 10;

    public PowerGeomancy(MowziePlayerProperties properties) {
        super(properties);
    }

    @Override
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        super.onUpdate(event);
        spawnBoulderCooldown -= 1;
//        System.out.println(event.player.ticksExisted);
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        super.onRightClickBlock(event);
        EntityPlayer player = event.getEntityPlayer();
        int x = MathHelper.floor(event.getHitVec().xCoord);
        int y = MathHelper.floor(event.getHitVec().yCoord);
        int z = MathHelper.floor(event.getHitVec().zCoord);
        IBlockState block = player.world.getBlockState(new BlockPos(x, y - 1, z));
        if (canUse(player) && event.getFace() == EnumFacing.UP && spawnBoulderCooldown <= 0 && !player.world.isRemote) {
            EntityBoulder boulder = new EntityBoulder(player.world, player, x, y, z, 1, block);
            player.world.spawnEntity(boulder);
////            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 7, 1, false, false));
            spawnBoulderCooldown = 10;
        }
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return player.inventory.getCurrentItem() == ItemStack.EMPTY && player.isPotionActive(PotionHandler.INSTANCE.geomancy);
    }
}
