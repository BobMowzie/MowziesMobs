package com.bobmowzie.mowziesmobs.server.property.power;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleFallingBlock;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Random;

public class PowerGeomancy extends Power {

    private int doubleTapTimer = 0;

    protected Random rand;

    private int spawnBoulderCooldown = 10;
    private boolean spawningBoulder = false;
    private boolean liftedMouse = true;
    private int spawnBoulderCharge = 0;
    private BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    private Vec3d lookPos = new Vec3d(0, 0, 0);
    private IBlockState spawnBoulderBlock = Blocks.DIRT.getDefaultState();

    public boolean tunneling;
    public boolean prevUnderground;
    public IBlockState justDug = Blocks.DIRT.getDefaultState();

    public PowerGeomancy(MowziePlayerProperties properties) {
        super(properties);
        rand = new Random();
    }

    @Override
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        super.onUpdate(event);
        EntityPlayer player = event.player;
        spawnBoulderCooldown -= 1;
        if (doubleTapTimer > 0) doubleTapTimer--;

        //Tunneling
        if (tunneling) {
            player.fallDistance = 0;
            player.capabilities.isFlying = false;
            boolean underground = !player.world.getEntitiesWithinAABB(EntityBlockSwapper.class, player.getEntityBoundingBox()).isEmpty();
            if (player.onGround && !underground) tunneling = false;
            Vec3d lookVec = player.getLookVec();
            float tunnelSpeed = 0.9f;
            if (underground) {
                if (player.isSneaking()) {
                    player.motionX = tunnelSpeed * lookVec.x;
                    player.motionY = tunnelSpeed * lookVec.y;
                    player.motionZ = tunnelSpeed * lookVec.z;
                }
                else {
                    player.motionX = tunnelSpeed * 0.5 * lookVec.x;
                    player.motionY = 1;
                    player.motionZ = tunnelSpeed * 0.5 * lookVec.z;
                }

                List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(4, 4, 4, 4);
                for (EntityLivingBase entityHit : entitiesHit) {
                    entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), 6 * ConfigHandler.TOOLS_AND_ABILITIES.geomancyAttackMultiplier);
                }
            }
            else player.motionY -= 0.07;


            if ((player.isSneaking() && lookVec.y < 0) || underground) {
                if (player.ticksExisted % 16 == 0) player.playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE.get(rand.nextInt(3)).get(), 0.6f, 0.5f + rand.nextFloat() * 0.2f);
                for (double x = -1.5; x <= 1.5; x++) {
                    for (double y = -1.5; y <= 2; y++) {
                        for (double z = -1.5; z <= 2; z++) {
                            if (Math.sqrt(x * x + y * y + z * z) > 2) continue;
                            BlockPos pos = new BlockPos(player.posX + x + player.motionX, player.posY + y + player.motionY + 0.5, player.posZ + z + player.motionZ);
                            IBlockState blockState = player.world.getBlockState(pos);
                            if (isBlockDiggable(blockState) && blockState.getBlock() != Blocks.BEDROCK) {
                                justDug = blockState;
                                EntityBlockSwapper.swapBlock(player.world, pos, Blocks.AIR.getDefaultState(), 10, false, false);
                            }
                        }
                    }
                }
            }
            if (!prevUnderground && underground) {
                player.playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM.get(rand.nextInt(3)).get(), 1f, 0.9f + rand.nextFloat() * 0.1f);
                EntityRing ring = new EntityRing(player.world, (float) player.posX, (float) player.posY + 0.02f, (float) player.posZ, new Vec3d(0, 1, 0), 10, 0.83f, 1, 0.39f, 1f, 3f, false);
                player.world.spawnEntity(ring);
            }
            if (prevUnderground && !underground) {
                player.playSound(MMSounds.EFFECT_GEOMANCY_BREAK, 1f, 0.9f + rand.nextFloat() * 0.1f);
                EntityRing ring = new EntityRing(player.world, (float) player.posX, (float) player.posY + 0.02f, (float) player.posZ, new Vec3d(0, 1, 0), 10, 0.83f, 1, 0.39f, 1f, 3f, false);
                player.world.spawnEntity(ring);
                player.motionX *= 2;
                player.motionY *= 2;
                player.motionZ *= 2;

                if (player.world.isRemote) {
                    for (int i = 0; i < 6; i++) {
                        if (justDug == null) justDug = Blocks.DIRT.getDefaultState();
                        ParticleFallingBlock.spawnFallingBlock(player.world, player.posX, player.posY + 1, player.posZ, 30f, 80, 1, (float) Math.random() * 0.8f - 0.4f, 0.4f + (float) Math.random() * 0.8f, (float) Math.random() * 0.8f - 0.4f, ParticleFallingBlock.EnumScaleBehavior.CONSTANT, justDug);
                    }
                }
            }
            prevUnderground = underground;
        }

        //Spawning boulder
        if (spawningBoulder) {
            if (player.getDistance(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) > 6 || !canUse(player)) {
                spawningBoulder = false;
                spawnBoulderCharge = 0;
            }
            else {
                spawnBoulderCharge++;
                if (spawnBoulderCharge > 2) player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 0, 2, false, false));
                if (spawnBoulderCharge == 1 && player.world.isRemote) MowziesMobs.PROXY.playBoulderChargeSound(player);
                if ((spawnBoulderCharge + 10) % 10 == 0 && spawnBoulderCharge < 40) {
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
        if (event.getHand() == EnumHand.MAIN_HAND && canUse(player)) {
            if (event.getHitVec() != null && !tunneling && !spawningBoulder && liftedMouse && spawnBoulderCooldown <= 0) {
                lookPos = new Vec3d(event.getHitVec().x, event.getHitVec().y, event.getHitVec().z);
                spawnBoulderPos = event.getPos();
                spawnBoulderBlock = player.world.getBlockState(spawnBoulderPos);
                if (event.getFace() != EnumFacing.UP) {
                    IBlockState blockAbove = player.world.getBlockState(spawnBoulderPos.up());
                    //System.out.println(blockAbove.getBlock().getLocalizedName());
                    if (blockAbove.causesSuffocation() || blockAbove == Blocks.AIR.getDefaultState()) return;
                }
                if (!isBlockDiggable(spawnBoulderBlock)) return;
                spawningBoulder = true;
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        super.onRightClickEmpty(event);
        EntityPlayer player = event.getEntityPlayer();
//        for (int i = 0; i < 6; i++) {
//            ParticleFallingBlock.spawnFallingBlock(player.world, player.posX, player.posY + 1, player.posZ, 30f, 80, 1, (float)Math.random() * 0.8f - 0.4f, 0.4f + (float)Math.random() * 0.8f, (float)Math.random() * 0.8f - 0.4f, ParticleFallingBlock.EnumScaleBehavior.CONSTANT, Blocks.DIRT.getDefaultState());
//        }
    }

    @Override
    public void onSneakDown(EntityPlayer player) {
        super.onSneakDown(player);
        if (doubleTapTimer > 0 && canUse(player) && !player.onGround) {
            tunneling = true;
        }
        doubleTapTimer = 8;
    }

    @Override
    public void onSneakUp(EntityPlayer player) {
        super.onSneakUp(player);
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
        int size = (int)Math.min(Math.max(0, Math.floor(spawnBoulderCharge/10.f) - 1), 2);
        if (spawnBoulderCharge >= 60) size = 3;
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
        return player.getHeldItemMainhand().isEmpty() && player.isPotionActive(PotionHandler.GEOMANCY);
    }

    public int getSpawnBoulderCharge() {
        return spawnBoulderCharge;
    }

    public boolean isBlockDiggable(IBlockState blockState) {
        Material mat = blockState.getMaterial();
        if (mat != Material.GRASS
                && mat != Material.GROUND
                && mat != Material.ROCK
                && mat != Material.CLAY
                && mat != Material.SAND
                ) {
            return false;
        }
        if (blockState == Blocks.HAY_BLOCK
                || blockState.getBlock() == Blocks.NETHER_WART_BLOCK
                || blockState.getBlock() instanceof BlockFence
                || blockState.getBlock() == Blocks.MOB_SPAWNER
                || blockState.getBlock() == Blocks.BONE_BLOCK
                || blockState.getBlock() == Blocks.ENCHANTING_TABLE
                || blockState.getBlock() == Blocks.END_PORTAL_FRAME
                || blockState.getBlock() == Blocks.ENDER_CHEST
                || blockState.getBlock() == Blocks.SLIME_BLOCK
                ) {
            return false;
        }
        return true;
    }
}
