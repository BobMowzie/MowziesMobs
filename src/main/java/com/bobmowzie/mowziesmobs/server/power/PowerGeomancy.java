package com.bobmowzie.mowziesmobs.server.power;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerStartSummonBoulder;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.Random;

public class PowerGeomancy extends Power {
    public static final double SPAWN_BOULDER_REACH = 5;

    private int doubleTapTimer = 0;

    protected Random rand;

    private int spawnBoulderCooldown = 8;
    private boolean spawningBoulder = false;
    private boolean liftedMouse = true;
    public int spawnBoulderCharge = 0;
    public BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    public Vector3d lookPos = new Vector3d(0, 0, 0);
    private BlockState spawnBoulderBlock = Blocks.DIRT.getDefaultState();

    public boolean tunneling;
    public boolean prevUnderground;
    public BlockState justDug = Blocks.DIRT.getDefaultState();

    public PowerGeomancy(PlayerCapability.PlayerCapabilityImp capability) {
        super(capability);
        rand = new Random();
    }

    @Override
    public void tick(TickEvent.PlayerTickEvent event) {
        super.tick(event);
        PlayerEntity player = event.player;
        spawnBoulderCooldown -= 1;
        if (doubleTapTimer > 0) doubleTapTimer--;

        //Tunneling
        if (tunneling) {
            player.fallDistance = 0;
            player.abilities.isFlying = false;
            boolean underground = !player.world.getEntitiesWithinAABB(EntityBlockSwapper.class, player.getBoundingBox()).isEmpty();
            if (player.isOnGround() && !underground) tunneling = false;
            Vector3d lookVec = player.getLookVec();
            float tunnelSpeed = 0.9f;
            if (underground) {
                if (player.isSneaking()) {
                    player.setMotion(tunnelSpeed * 0.5 * lookVec.x, tunnelSpeed * lookVec.y, tunnelSpeed * 0.5 * lookVec.z);
                }
                else {
                    player.setMotion(tunnelSpeed * 0.5 * lookVec.x, 1, tunnelSpeed * 0.5 * lookVec.z);
                }

                List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(player,4, 4, 4, 4);
                for (LivingEntity entityHit : entitiesHit) {
                    entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), 6 * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.geomancyAttackMultiplier.get());
                }
            }
            else player.setMotion(player.getMotion().subtract(0, 0.07, 0));


            if ((player.isSneaking() && lookVec.y < 0) || underground) {
                if (player.ticksExisted % 16 == 0) player.playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE.get(rand.nextInt(3)).get(), 0.6f, 0.5f + rand.nextFloat() * 0.2f);
                for (double x = -1.5; x <= 1.5; x++) {
                    for (double y = -1.5; y <= 2; y++) {
                        for (double z = -1.5; z <= 2; z++) {
                            if (Math.sqrt(x * x + y * y + z * z) > 2) continue;
                            BlockPos pos = new BlockPos(player.getPosX() + x + player.getMotion().getX(), player.getPosY() + y + player.getMotion().getY() + 0.5, player.getPosZ() + z + player.getMotion().getZ());
                            BlockState blockState = player.world.getBlockState(pos);
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
                if (player.world.isRemote)
                    AdvancedParticleBase.spawnParticle(player.world, ParticleHandler.RING2.get(), (float) player.getPosX(), (float) player.getPosY() + 0.02f, (float) player.getPosZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                });
            }
            if (prevUnderground && !underground) {
                player.playSound(MMSounds.EFFECT_GEOMANCY_BREAK.get(), 1f, 0.9f + rand.nextFloat() * 0.1f);
                if (player.world.isRemote)
                    AdvancedParticleBase.spawnParticle(player.world, ParticleHandler.RING2.get(), (float) player.getPosX(), (float) player.getPosY() + 0.02f, (float) player.getPosZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                });
                player.setMotion(player.getMotion().scale(2f));

                for (int i = 0; i < 6; i++) {
                    if (justDug == null) justDug = Blocks.DIRT.getDefaultState();
//                        ParticleFallingBlock.spawnFallingBlock(player.world, player.getPosX(), player.getPosY() + 1, player.getPosZ(), 30f, 80, 1, player.getRNG().nextFloat() * 0.8f - 0.4f, 0.4f + player.getRNG().nextFloat() * 0.8f, player.getRNG().nextFloat() * 0.8f - 0.4f, ParticleFallingBlock.EnumScaleBehavior.CONSTANT, justDug);
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, player.world, 80, justDug);
                    fallingBlock.setPosition(player.getPosX(), player.getPosY() + 1, player.getPosZ());
                    fallingBlock.setMotion(player.getRNG().nextFloat() * 0.8f - 0.4f, 0.4f + player.getRNG().nextFloat() * 0.8f, player.getRNG().nextFloat() * 0.8f - 0.4f);
                    player.world.addEntity(fallingBlock);
                }
            }
            prevUnderground = underground;
        }

        //Spawning boulder
        if (spawningBoulder) {
            if (player.getDistanceSq(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) > 36 || !canUse(player)) {
                spawningBoulder = false;
                spawnBoulderCharge = 0;
            }
            else {
                spawnBoulderCharge++;
                if (spawnBoulderCharge > 2) player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 0, 2, false, false));
                if (spawnBoulderCharge == 1 && player.world.isRemote) MowziesMobs.PROXY.playBoulderChargeSound(player);
                if ((spawnBoulderCharge + 10) % 10 == 0 && spawnBoulderCharge < 40) {
                    if (player.world.isRemote) {
                        AdvancedParticleBase.spawnParticle(player.world, ParticleHandler.RING2.get(), (float) player.getPosX(), (float) player.getPosY() + player.getHeight() / 2f, (float) player.getPosZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd((0.8f + 2.7f * spawnBoulderCharge / 60f) * 10f, 0), false)
                        });
                    }
                }
                if (spawnBoulderCharge == 50) {
                    if (player.world.isRemote) {
                        AdvancedParticleBase.spawnParticle(player.world, ParticleHandler.RING2.get(), (float) player.getPosX(), (float) player.getPosY() + player.getHeight() / 2f, (float) player.getPosZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 20, true, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, 40f), false)
                        });
                    }
                    player.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1, 1f);
                }
                if (player.world.isRemote && spawnBoulderCharge > 5 && spawnBoulderCharge < 30) {
                    int particleCount = 4;
                    while (--particleCount != 0) {
                        double radius = 0.5f + 1.5f * spawnBoulderCharge/30f;
                        double yaw = player.getRNG().nextFloat() * 2 * Math.PI;
                        double pitch = player.getRNG().nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        player.world.addParticle(new ParticleOrb.OrbData((float) player.getPosX(), (float) player.getPosY() + player.getHeight() /2f, (float) player.getPosZ(), 14), player.getPosX() + ox, player.getPosY() + oy + player.getHeight()/2, player.getPosZ() + oz, 0, 0, 0);
                    }
                }
            }
            if (spawnBoulderCharge > 60) {
                spawnBoulder(player);
                liftedMouse = false;
            }
            else {
                int size = (int)Math.min(Math.max(0, Math.floor(spawnBoulderCharge/10.f) - 1), 2) + 1;
                EntityType<EntityBoulder> type = EntityHandler.BOULDERS[size];
                if (!player.world.hasNoCollisions(type.getBoundingBoxWithSizeApplied(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F))) {
                    spawnBoulder(player);
                }
            }
        }
//        System.out.println(event.player.ticksExisted);
    }

    @Override
    public void onRightMouseUp(PlayerEntity player) {
        super.onRightMouseUp(player);
        liftedMouse = true;
        if (spawningBoulder && player.getDistanceSq(spawnBoulderPos.getX(), spawnBoulderPos.getY(), spawnBoulderPos.getZ()) < 36) {
            spawnBoulder(player);
        }
        else {
            spawningBoulder = false;
            spawnBoulderCharge = 0;
        }
    }

    @Override
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        super.onRightClickEmpty(event);
        PlayerEntity player = event.getPlayer();
        if (event.getHand() == Hand.MAIN_HAND && canUse(player)) {
            if (!tunneling && !spawningBoulder && liftedMouse && spawnBoulderCooldown <= 0) {
                startSpawningBoulder(player);
                MowziesMobs.NETWORK.sendToServer(new MessagePlayerStartSummonBoulder());
            }
        }
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        super.onRightClickBlock(event);
        PlayerEntity player = event.getPlayer();
        if (event.getHand() == Hand.MAIN_HAND && canUse(player)) {
            if (!tunneling && !spawningBoulder && liftedMouse && spawnBoulderCooldown <= 0) {
                startSpawningBoulder(player);
                MowziesMobs.NETWORK.sendToServer(new MessagePlayerStartSummonBoulder());
            }
        }
    }

    @Override
    public void onSneakDown(PlayerEntity player) {
        super.onSneakDown(player);
        if (doubleTapTimer > 0 && canUse(player) && !player.isOnGround()) {
            tunneling = true;
        }
        doubleTapTimer = 8;
    }

    @Override
    public void onSneakUp(PlayerEntity player) {
        super.onSneakUp(player);
    }

    public boolean isSpawningBoulder() {
        return spawningBoulder;
    }

    public BlockPos getSpawnBoulderPos() {
        return spawnBoulderPos;
    }

    public Vector3d getLookPos() {
        return lookPos;
    }

    private void spawnBoulder(PlayerEntity player) {
        int size = (int)Math.min(Math.max(0, Math.floor(spawnBoulderCharge/10.f) - 1), 2);
        if (spawnBoulderCharge >= 60) size = 3;
        EntityBoulder boulder = new EntityBoulder(EntityHandler.BOULDERS[size], player.world, player, spawnBoulderBlock, spawnBoulderPos);
        boulder.setPosition(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F);
        if (!player.world.isRemote && boulder.checkCanSpawn()) {
            player.world.addEntity(boulder);
        }

        if (spawnBoulderCharge > 2) {
            Vector3d playerEyes = player.getEyePosition(1);
            Vector3d vec = playerEyes.subtract(getLookPos()).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            player.rotationYaw = (float) (yaw * 180f / Math.PI + 90);
            player.rotationPitch = (float) (pitch * 180f / Math.PI);
        }

        spawnBoulderCooldown = 10;
        spawnBoulderCharge = 0;
        spawningBoulder = false;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getHeldItemMainhand().isEmpty() && player.isPotionActive(PotionHandler.GEOMANCY);
    }

    public int getSpawnBoulderCharge() {
        return spawnBoulderCharge;
    }

    public boolean isBlockDiggable(BlockState blockState) {
        Material mat = blockState.getMaterial();
        if (mat != Material.ORGANIC
                && mat != Material.EARTH
                && mat != Material.ROCK
                && mat != Material.CLAY
                && mat != Material.SAND
                ) {
            return false;
        }
        return blockState.getBlock() != Blocks.HAY_BLOCK
                && blockState.getBlock() != Blocks.NETHER_WART_BLOCK
                && !(blockState.getBlock() instanceof FenceBlock)
                && blockState.getBlock() != Blocks.SPAWNER
                && blockState.getBlock() != Blocks.BONE_BLOCK
                && blockState.getBlock() != Blocks.ENCHANTING_TABLE
                && blockState.getBlock() != Blocks.END_PORTAL_FRAME
                && blockState.getBlock() != Blocks.ENDER_CHEST
                && blockState.getBlock() != Blocks.SLIME_BLOCK
                && blockState.getBlock() != Blocks.HOPPER
                && !blockState.hasTileEntity();
    }

    public void startSpawningBoulder(PlayerEntity player) {
        Vector3d from = player.getEyePosition(1.0f);
        Vector3d to = from.add(player.getLookVec().scale(SPAWN_BOULDER_REACH));
        BlockRayTraceResult result = player.world.rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            this.lookPos = result.getHitVec();
        }

        this.spawnBoulderPos = result.getPos();
        this.spawnBoulderBlock = player.world.getBlockState(spawnBoulderPos);
        if (result.getFace() != Direction.UP) {
            BlockState blockAbove = player.world.getBlockState(spawnBoulderPos.up());
            //System.out.println(blockAbove.getBlock().getLocalizedName());
            if (blockAbove.isSuffocating(player.world, spawnBoulderPos.up()) || blockAbove.isAir(player.world, spawnBoulderPos.up()))
                return;
        }
        if (!isBlockDiggable(spawnBoulderBlock)) return;
        spawningBoulder = true;
    }
}
