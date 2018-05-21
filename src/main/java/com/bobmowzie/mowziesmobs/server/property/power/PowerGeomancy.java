package com.bobmowzie.mowziesmobs.server.property.power;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
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
    private Vec3d lookPos = new Vec3d(0, 0, 0);
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
                if (spawnBoulderCharge > 2) player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 0, 2, false, false));
                if (spawnBoulderCharge == 1 && player.world.isRemote) MowziesMobs.PROXY.playBoulderChargeSound(player);
                if ((spawnBoulderCharge + 10) % 15 == 0 && spawnBoulderCharge < 40) {
                    EntityRing ring = new EntityRing(player.world, (float)player.posX, (float)player.posY + player.height/2f, (float)player.posZ, new Vec3d(0, 1, 0), 10, 0.83f, 1, 0.39f, 0.7f, 0.8f + 2.7f * spawnBoulderCharge/60f, false) {
                        public float interpolate(float delta) {
                            return 1 - (ticksExisted + delta)/duration;
                        }
                    };
                    if (player.world.isRemote) player.world.spawnEntity(ring);
                }
                if (spawnBoulderCharge == 50) {
                    EntityRing ring = new EntityRing(player.world, (float)player.posX, (float)player.posY + player.height/2f, (float)player.posZ, new Vec3d(0, 1, 0), 20, 0.83f, 1, 0.39f, 0.7f, 4, true);
                    if (player.world.isRemote) player.world.spawnEntity(ring);
                    player.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1, 1f);
                }
                if (player.world.isRemote && spawnBoulderCharge > 5 && spawnBoulderCharge < 30) {
                    int particleCount = 4;
                    while (--particleCount != 0) {
                        double radius = 0.5f + 1.5f * spawnBoulderCharge/30f;
                        double yaw = Math.random() * 2 * Math.PI;
                        double pitch = Math.random() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        MMParticle.ORB.spawn(player.world, player.posX + ox, player.posY + oy + player.height/2, player.posZ + oz, ParticleFactory.ParticleArgs.get().withData(player.posX, player.posY + player.height/2, player.posZ, 14));
                    }
                }
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
            int x = MathHelper.floor(event.getHitVec().x);
            int y = MathHelper.floor(event.getHitVec().y);
            int z = MathHelper.floor(event.getHitVec().z);
            lookPos = new Vec3d(event.getHitVec().x, event.getHitVec().y, event.getHitVec().z);
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

    public Vec3d getLookPos() {
        return lookPos;
    }

    private void spawnBoulder(EntityPlayer player) {
        int size = (int)Math.max(0, Math.floor(spawnBoulderCharge/15.f) - 1);
        EntityBoulder boulder = new EntityBoulder(player.world, player, size, spawnBoulderBlock);
        boulder.setPosition(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F);
        if (!player.world.isRemote && boulder.checkCanSpawn()) {
            player.world.spawnEntity(boulder);
        }
        spawnBoulderCooldown = 10;
        spawnBoulderCharge = 0;
        spawningBoulder = false;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return player.inventory.getCurrentItem() == ItemStack.EMPTY && player.isPotionActive(PotionHandler.GEOMANCY);
    }

    public int getSpawnBoulderCharge() {
        return spawnBoulderCharge;
    }
}
