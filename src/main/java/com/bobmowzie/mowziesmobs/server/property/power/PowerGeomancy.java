package com.bobmowzie.mowziesmobs.server.property.power;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.core.patcher.LLibraryHooks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import scala.tools.nsc.interpreter.IMain;

import java.util.Random;

public class PowerGeomancy extends Power {

    private int spawnBoulderCooldown = 10;
    private boolean spawningBoulder = false;
    private boolean liftedMouse = true;
    private int spawnBoulderCharge = 0;
    private BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    private BlockPos lookBlockPos = new BlockPos(0, 0, 0);
    private IBlockState spawnBoulderBlock = Blocks.DIRT.getDefaultState();

    public PowerGeomancy(MowziePlayerProperties properties) {
        super(properties);
    }

    @Override
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        super.onUpdate(event);
        EntityPlayer player = event.player;
        spawnBoulderCooldown -= 1;
        if (spawningBoulder) {
            if (player.getDistance(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) > 6 || !canUse(player)) {
                spawningBoulder = false;
                spawnBoulderCharge = 0;
            }
            else {
                spawnBoulderCharge++;
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 0, 2, false, false));
            }
            if (spawnBoulderCharge > 60) {
                spawnBoulder(player);
                liftedMouse = false;
            }
        }
//        System.out.println(event.player.ticksExisted);
    }

    @Override
    public void onRightMouseUp(EntityPlayer player) {
        super.onRightMouseUp(player);
        liftedMouse = true;
        if (spawningBoulder && player.getDistance(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) < 6) {
            spawnBoulder(player);
        }
        else {
            spawningBoulder = false;
            spawnBoulderCharge = 0;
        }
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        super.onRightClickBlock(event);
        EntityPlayer player = event.getEntityPlayer();
        if (canUse(player) && !spawningBoulder && liftedMouse && event.getFace() == EnumFacing.UP && spawnBoulderCooldown <= 0) {
            int x = MathHelper.floor(event.getHitVec().xCoord);
            int y = MathHelper.floor(event.getHitVec().yCoord);
            int z = MathHelper.floor(event.getHitVec().zCoord);
            lookBlockPos = new BlockPos(event.getHitVec().xCoord, event.getHitVec().yCoord, event.getHitVec().zCoord);
            spawnBoulderPos = new BlockPos(x, y - 1, z);
            spawnBoulderBlock = player.world.getBlockState(spawnBoulderPos);
            Material mat = spawnBoulderBlock.getMaterial();
            if (mat != Material.GRASS
                    && mat != Material.GROUND
                    && mat != Material.ROCK
                    && mat != Material.CLAY
                    && mat != Material.SAND
                    ) {
                return;
            }
            if (spawnBoulderBlock == Blocks.HAY_BLOCK
                    || spawnBoulderBlock.getBlock() == Blocks.NETHER_WART_BLOCK
                    || spawnBoulderBlock.getBlock() instanceof BlockFence
                    || spawnBoulderBlock.getBlock() == Blocks.MOB_SPAWNER
                    || spawnBoulderBlock.getBlock() == Blocks.BONE_BLOCK
                    || spawnBoulderBlock.getBlock() == Blocks.ENCHANTING_TABLE
                    || spawnBoulderBlock.getBlock() == Blocks.END_PORTAL_FRAME
                    || spawnBoulderBlock.getBlock() == Blocks.ENDER_CHEST
                    || spawnBoulderBlock.getBlock() == Blocks.SLIME_BLOCK
                    ) {
                return;
            }
            spawningBoulder = true;
        }
    }

    public boolean isSpawningBoulder() {
        return spawningBoulder;
    }

    public BlockPos getSpawnBoulderPos() {
        return spawnBoulderPos;
    }

    public BlockPos getLookBlockPos() {
        return lookBlockPos;
    }

    private void spawnBoulder(EntityPlayer player) {
        int size = (int)Math.floor(spawnBoulderCharge/15.f);
        EntityBoulder boulder = new EntityBoulder(player.world, player, spawnBoulderPos.getX(), spawnBoulderPos.getY() + 1, spawnBoulderPos.getZ(), size, spawnBoulderBlock);
        if (!player.world.isRemote) {
            if (!boulder.isDead) player.world.spawnEntity(boulder);
        }
        spawnBoulderCooldown = 10;
        spawnBoulderCharge = 0;
        spawningBoulder = false;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return player.inventory.getCurrentItem() == ItemStack.EMPTY && player.isPotionActive(PotionHandler.INSTANCE.geomancy);
    }
}
