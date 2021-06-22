package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ai.AvoidEntityIfNotTamedGoal;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class ServerEventHandler {
    private final static int SUNSTRIKE_COOLDOWN = 45;
    private final static int SOLARBEAM_COOLDOWN = 110;

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ZombieEntity && !(entity instanceof ZombifiedPiglinEntity)) {
            ((CreatureEntity) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((CreatureEntity) entity, EntityFoliaath.class, 0, true, false, null));
            ((CreatureEntity) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((CreatureEntity) entity, EntityBarakoa.class, 0, true, false, null));
            ((CreatureEntity) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((CreatureEntity) entity, EntityBarako.class, 0, true, false, null));
        }
        if (entity instanceof SkeletonEntity) {
            ((CreatureEntity) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((CreatureEntity) entity, EntityBarakoa.class, 0, true, false, null));
            ((CreatureEntity) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((CreatureEntity) entity, EntityBarako.class, 0, true, false, null));
        }

        if (entity instanceof ParrotEntity) {
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AnimalEntity) {
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((CreatureEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((CreatureEntity) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((CreatureEntity) entity, EntityBarako.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((CreatureEntity) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((CreatureEntity) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AbstractVillagerEntity) {
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityBarako.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();

            if (entity.getActivePotionEffect(EffectHandler.POISON_RESIST) != null && entity.getActivePotionEffect(Effects.POISON) != null) {
                entity.removeActivePotionEffect(Effects.POISON);
            }

            if (!entity.world.isRemote) {
                Item headItemStack = entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
                if (headItemStack instanceof ItemBarakoaMask) {
                    ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
                    EffectHandler.addOrCombineEffect(entity, mask.getPotion(), 50, 0, true, false);
                }
            }

            if (entity instanceof MobEntity) {
                MobEntity mob = (MobEntity) entity;
                if (mob.getAttackTarget() instanceof EntityBarako && mob.getAttackTarget().isPotionActive(EffectHandler.SUNBLOCK)) {
                    EntityBarakoaya sunblocker = mob.world.getClosestEntity(EntityBarakoaya.class, EntityPredicate.DEFAULT, mob, mob.getPosX(), mob.getPosY() + mob.getEyeHeight(), mob.getPosZ(), mob.getBoundingBox().grow(40.0D, 15.0D, 40.0D));
                    mob.setAttackTarget(sunblocker);
                }
            }

//            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.tick(entity);
            }
            LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entity, LivingCapability.LivingProvider.LIVING_CAPABILITY);
            if (livingCapability != null) {
                livingCapability.tick(entity);
            }
        }
    }

    @SubscribeEvent
    public void onAddPotionEffect(PotionEvent.PotionAddedEvent event) {
        if (event.getPotionEffect().getPotion() == EffectHandler.SUNBLOCK) {
            if (!event.getEntity().world.isRemote()) {
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntityLiving(), true));
            }
            MowziesMobs.PROXY.playSunblockSound(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onRemovePotionEffect(PotionEvent.PotionRemoveEvent event) {
        if (!event.getEntity().world.isRemote() && event.getPotion() == EffectHandler.SUNBLOCK) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntityLiving(), false));
        }
    }

    @SubscribeEvent
    public void onPotionEffectExpire(PotionEvent.PotionExpiryEvent event) {
        EffectInstance effectInstance = event.getPotionEffect();
        if (!event.getEntity().world.isRemote() && effectInstance != null && effectInstance.getPotion() == EffectHandler.SUNBLOCK) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntityLiving(), false));
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // Copied from LivingEntity's applyPotionDamageCalculations
        DamageSource source = event.getSource();
        LivingEntity livingEntity = event.getEntityLiving();
        if (source == null || livingEntity == null) return;
        float damage = event.getAmount();
        if (!source.isDamageAbsolute()) {
            if (livingEntity.isPotionActive(EffectHandler.SUNBLOCK) && source != DamageSource.OUT_OF_WORLD) {
                int i = (livingEntity.getActivePotionEffect(EffectHandler.SUNBLOCK).getAmplifier() + 2) * 5;
                int j = 25 - i;
                float f = damage * (float)j;
                float f1 = damage;
                damage = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - damage;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (livingEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)livingEntity).addStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
                    } else if (source.getTrueSource() instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)source.getTrueSource()).addStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        PlayerEntity player = event.player;
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            playerCapability.tick(event);
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.tick(event);
            }
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (event.isCancelable() && living.isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null && event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling ||
                        playerCapability.getUntilAxeSwing() > 0
                ) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if (event.isCancelable() && living.isPotionActive(EffectHandler.FROZEN)) {
                event.setCanceled(true);
                return;
            }

            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(entity, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null && event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling ||
                        playerCapability.getUntilAxeSwing() > 0
                ) {
                    event.setCanceled(true);
                    return;
                }
            }

            if (entity instanceof PlayerEntity) {
                BlockState block = event.getPlacedBlock();
                if (
                        block.getBlock() == Blocks.FIRE ||
                        block.getBlock() == Blocks.TNT ||
                        block.getBlock() == Blocks.RESPAWN_ANCHOR ||
                        block.getBlock() == Blocks.DISPENSER ||
                        block.getBlock() == Blocks.CACTUS
                ) {
                    aggroBarakoa((PlayerEntity) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living != null) {
            if (event.isCancelable() && living.isPotionActive(EffectHandler.FROZEN)) {
                event.setCanceled(true);
                return;
            }
            if (event.getEmptyBucket().getItem() == Items.LAVA_BUCKET) {
                aggroBarakoa(event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        PlayerEntity player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null && event.isCancelable()) {
            if (
                    playerCapability.getUsingSolarBeam() ||
                            playerCapability.getGeomancy().isSpawningBoulder() ||
                            playerCapability.getGeomancy().tunneling ||
                            playerCapability.getUntilAxeSwing() > 0
            ) {
                event.setCanceled(true);
                return;
            }
        }

        BlockState block = event.getState();
        if (block.getBlock() == Blocks.GOLD_BLOCK ||
            block.getMaterial() == Material.WOOD ||
            block.getBlock() == BlockHandler.THATCH.get() ||
            block.getBlock() == Blocks.RED_TERRACOTTA ||
            block.getBlock() == Blocks.SKELETON_SKULL ||
            block.getBlock() == Blocks.TORCH
        ) {
            aggroBarakoa(player);
        }
    }

    public <T extends Entity> List<T> getEntitiesNearby(Entity startEntity, Class<T> entityClass, double r) {
        return startEntity.world.getEntitiesWithinAABB(entityClass, startEntity.getBoundingBox().grow(r, r, r), e -> e != startEntity && startEntity.getDistance(e) <= r);
    }

    private List<LivingEntity> getEntityLivingBaseNearby(LivingEntity user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.world.getEntitiesWithinAABBExcludingEntity(user, user.getBoundingBox().grow(distanceX, distanceY, distanceZ));
        ArrayList<LivingEntity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && user.getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        PlayerEntity player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            if (event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling ||
                        playerCapability.getUntilAxeSwing() > 0
                ) {
                    event.setCanceled(true);
                    return;
                }
            }

            if (event.getWorld().isRemote && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(EffectHandler.SUNS_BLESSING) && playerCapability.getUntilSunstrike() <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSolarBeam());
                    playerCapability.setUntilSunstrike(SOLARBEAM_COOLDOWN);
                    playerCapability.setUsingSolarBeam(true);
                } else {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSummonSunstrike());
                    playerCapability.setUntilSunstrike(SUNSTRIKE_COOLDOWN);
                }
            }

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            if (event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling ||
                        playerCapability.getUntilAxeSwing() > 0
                ) {
                    event.setCanceled(true);
                    return;
                }
            }

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.getBlockState(event.getPos()).getBlock() instanceof ChestBlock) {
            aggroBarakoa(player);
        }
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            if (event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling ||
                        playerCapability.getUntilAxeSwing() > 0 ||
                        player.isPotionActive(EffectHandler.FROZEN)
                ) {
                    event.setCanceled(true);
                    return;
                }
            }

            ItemStack item = event.getItemStack();
            if (
                    item.getItem() == Items.FLINT_AND_STEEL ||
                    item.getItem() == Items.TNT_MINECART
            ) {
                aggroBarakoa(player);
            }

            if (event.getSide() == LogicalSide.CLIENT && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(EffectHandler.SUNS_BLESSING) && playerCapability.getUntilSunstrike() <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSolarBeam());
                    playerCapability.setUntilSunstrike(SOLARBEAM_COOLDOWN);
                } else {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSummonSunstrike());
                    playerCapability.setUntilSunstrike(SUNSTRIKE_COOLDOWN);
                }
            }
            if (player.world.getBlockState(event.getPos()).getContainer(player.world, event.getPos()) != null) {
                player.resetCooldown();
                return;
            }
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        double range = 6.5;
        PlayerEntity player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == ItemHandler.SPEAR) {
            LivingEntity entityHit = ItemSpear.raytraceEntities(player.getEntityWorld(), player, range);
            if (entityHit != null) {
                MowziesMobs.NETWORK.sendToServer(new MessagePlayerAttackMob(entityHit));
            }
        }
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onLeftClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingHurtEvent event) {
        if (event.getSource().isFireDamage()) {
            event.getEntityLiving().removeActivePotionEffect(EffectHandler.FROZEN);
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> event.getEntity()), new MessageRemoveFreezeProgress(event.getEntityLiving()));
        }
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onTakeDamage(event);
                }
            }
        }

        if (event.getEntityLiving() != null) {
            LivingEntity living = event.getEntityLiving();
            LivingCapability.ILivingCapability capability = CapabilityHandler.getCapability(living, LivingCapability.LivingProvider.LIVING_CAPABILITY);
            if (capability != null) {
                capability.setLastDamage(event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            if (event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling ||
                        playerCapability.getUntilAxeSwing() > 0
                ) {
                    event.setCanceled(true);
                    return;
                }
            }

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickWithItem(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (event.isCancelable() && player.isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            if (event.isCancelable()) {
                if (
                        playerCapability.getUsingSolarBeam() ||
                                playerCapability.getGeomancy().isSpawningBoulder() ||
                                playerCapability.getGeomancy().tunneling ||
                                playerCapability.getUntilAxeSwing() > 0
                ) {
                    event.setCanceled(true);
                    return;
                }
            }

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onLeftClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
         if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.isPotionActive(EffectHandler.FROZEN) && entity.isOnGround()) {
                entity.setMotion(entity.getMotion().mul(1, 0, 1));
            }
        }

        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onJump(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                playerCapability.setPrevCooledAttackStrength(event.getPlayer().getCooledAttackStrength(0.5f));

                if (
                        event.isCancelable() && (
                        playerCapability.getUsingSolarBeam() ||
                        playerCapability.getGeomancy().isSpawningBoulder() ||
                        playerCapability.getGeomancy().tunneling
                )) {
                    event.setCanceled(true);
                    return;
                }

                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onLeftClickEntity(event);
                }

                if (event.getTarget() instanceof ItemFrameEntity) {
                    ItemFrameEntity itemFrame = (ItemFrameEntity) event.getTarget();
                    if (itemFrame.getDisplayedItem().getItem() instanceof ItemBarakoaMask) {
                        aggroBarakoa(event.getPlayer());
                    }
                }
                if (event.getTarget() instanceof LeaderSunstrikeImmune) {
                    aggroBarakoa(event.getPlayer());
                }

                if (!(event.getTarget() instanceof LivingEntity)) return;
                if (event.getTarget() instanceof EntityBarakoanToPlayer) return;
                if (!event.getPlayer().world.isRemote()) {
                    for (int i = 0; i < playerCapability.getPackSize(); i++) {
                        EntityBarakoanToPlayer barakoa = playerCapability.getTribePack().get(i);
                        LivingEntity living = (LivingEntity) event.getTarget();
                        if (barakoa.getMask() != MaskType.FAITH) {
                            if (!living.isInvulnerable()) barakoa.setAttackTarget(living);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkCritEvent(CriticalHitEvent event) {
        ItemStack weapon = event.getPlayer().getHeldItemMainhand();
        PlayerEntity attacker = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getPrevCooledAttackStrength() == 1.0f && !weapon.isEmpty() && weapon.getItem() instanceof ItemNagaFangDagger && event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity)event.getTarget();
            Vector3d lookDir = new Vector3d(target.getLookVec().x, 0, target.getLookVec().z).normalize();
            Vector3d vecBetween = new Vector3d(target.getPosX() - event.getPlayer().getPosX(), 0, target.getPosZ() - event.getPlayer().getPosZ()).normalize();
            double dot = lookDir.dotProduct(vecBetween);
            if (dot > 0.7) {
                event.setDamageModifier(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.backstabDamageMultiplier.get().floatValue());
                target.playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1f, 1.2f);

                if (target.world.isRemote() && target != null && attacker != null) {
                    Vector3d ringOffset = attacker.getLookVec().scale(-target.getWidth() / 2.f);
                    ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                    Vector3d pos = target.getPositionVec().add(0, target.getHeight() / 2f, 0).add(ringOffset);
                    AdvancedParticleBase.spawnParticle(target.world, ParticleHandler.RING_SPARKS.get(), pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{1f, 1f, 0f}, new float[]{0f, 0.5f, 1f}), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 15f), false)
                    });
                    Random rand = attacker.world.getRandom();
                    float explodeSpeed = 2.5f;
                    for (int i = 0; i < 10; i++) {
                        Vector3d particlePos = new Vector3d(rand.nextFloat() * 0.25, 0, 0);
                        particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                        particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 8f + 15f;
                        ParticleVanillaCloudExtended.spawnVanillaCloud(target.world, pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                    }
                    for (int i = 0; i < 10; i++) {
                        Vector3d particlePos = new Vector3d(rand.nextFloat() * 0.25, 0, 0);
                        particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                        particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 2.5f + 5f;
                        AdvancedParticleBase.spawnParticle(target.world, ParticleHandler.PIXEL.get(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
                    }
                    for (int i = 0; i < 6; i++) {
                        Vector3d particlePos = new Vector3d(rand.nextFloat() * 0.25, 0, 0);
                        particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                        particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 5f + 10f;
                        AdvancedParticleBase.spawnParticle(target.world, ParticleHandler.BUBBLE.get(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "frozen"), new FrozenCapability.FrozenProvider());
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "last_damage"), new LivingCapability.LivingProvider());
        }
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "player"), new PlayerCapability.PlayerProvider());
        }
    }

    private void aggroBarakoa(PlayerEntity player) {
        List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 50);
        for (EntityBarako barako : barakos) {
            if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                if (player.getPosition().distanceSq(barako.getHomePosition()) < 900) {
                    if (barako.canAttack(player)) barako.setMisbehavedPlayerId(player.getUniqueID());
                }
            }
        }
        List<EntityBarakoaVillager> barakoas = getEntitiesNearby(player, EntityBarakoaVillager.class, 50);
        for (EntityBarakoaVillager barakoa : barakoas) {
            if (barakoa.getAttackTarget() == null || !(barakoa.getAttackTarget() instanceof PlayerEntity)) {
                if (player.getPosition().distanceSq(barakoa.getHomePosition()) < 900) {
                    if (barakoa.canAttack(player)) barakoa.setMisbehavedPlayerId(player.getUniqueID());
                }
            }
        }
    }
}
