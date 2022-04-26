package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ai.AvoidEntityIfNotTamedGoal;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.capability.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public final class ServerEventHandler {

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability((LivingEntity) event.getEntity());
            if (abilityCapability != null) abilityCapability.instanceAbilities((LivingEntity) event.getEntity());
        }

        if (event.getEntity() instanceof Player) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability((Player) event.getEntity(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) playerCapability.addedToWorld(event);
        }

        if (event.getWorld().isClientSide) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof Zombie && !(entity instanceof ZombifiedPiglin)) {
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityFoliaath.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityBarakoa.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityBarako.class, 0, true, false, null));
        }
        if (entity instanceof AbstractSkeleton) {
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityBarakoa.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityBarako.class, 0, true, false, null));
        }

        if (entity instanceof Parrot) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof Animal) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityBarako.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AbstractVillager) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityBarako.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();

            if (entity.getEffect(EffectHandler.POISON_RESIST) != null && entity.getEffect(MobEffects.POISON) != null) {
                entity.removeEffectNoUpdate(MobEffects.POISON);
            }

            if (!entity.level.isClientSide) {
                Item headItemStack = entity.getItemBySlot(EquipmentSlot.HEAD).getItem();
                if (headItemStack instanceof ItemBarakoaMask) {
                    ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
                    EffectHandler.addOrCombineEffect(entity, mask.getPotion(), 50, 0, true, false);
                }
            }

            if (entity instanceof Mob) {
                Mob mob = (Mob) entity;
                if (mob.getTarget() instanceof EntityBarako && mob.getTarget().hasEffect(EffectHandler.SUNBLOCK)) {
                    EntityBarakoaya sunblocker = mob.level.getNearestEntity(EntityBarakoaya.class, TargetingConditions.DEFAULT, mob, mob.getX(), mob.getY() + mob.getEyeHeight(), mob.getZ(), mob.getBoundingBox().inflate(40.0D, 15.0D, 40.0D));
                    mob.setTarget(sunblocker);
                }
            }

            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.tick(entity);
            }
            LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entity, LivingCapability.LivingProvider.LIVING_CAPABILITY);
            if (livingCapability != null) {
                livingCapability.tick(entity);
            }
            AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
            if (abilityCapability != null) {
                abilityCapability.tick(entity);
            }
        }
    }

    @SubscribeEvent
    public void onAddPotionEffect(PotionEvent.PotionAddedEvent event) {
        if (event.getPotionEffect().getEffect() == EffectHandler.SUNBLOCK) {
            if (!event.getEntity().level.isClientSide()) {
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntityLiving(), true));
            }
            MowziesMobs.PROXY.playSunblockSound(event.getEntityLiving());
        }
        if (event.getPotionEffect().getEffect() == EffectHandler.FROZEN) {
            if (!event.getEntity().level.isClientSide()) {
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntityLiving(), true));
                FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
                if (frozenCapability != null) {
                    frozenCapability.onFreeze(event.getEntityLiving());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRemovePotionEffect(PotionEvent.PotionRemoveEvent event) {
        if (!event.getEntity().level.isClientSide() && event.getPotion() == EffectHandler.SUNBLOCK) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntityLiving(), false));
        }
        if (!event.getEntity().level.isClientSide() && event.getPotion() == EffectHandler.FROZEN) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntityLiving(), false));
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntityLiving());
            }
        }
    }

    @SubscribeEvent
    public void onPotionEffectExpire(PotionEvent.PotionExpiryEvent event) {
        MobEffectInstance effectInstance = event.getPotionEffect();
        if (!event.getEntity().level.isClientSide() && effectInstance != null && effectInstance.getEffect() == EffectHandler.SUNBLOCK) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntityLiving(), false));
        }
        if (!event.getEntity().level.isClientSide() && effectInstance != null && effectInstance.getEffect() == EffectHandler.FROZEN) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntityLiving(), false));
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntityLiving());
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // Copied from LivingEntity's applyPotionDamageCalculations
        DamageSource source = event.getSource();
        LivingEntity livingEntity = event.getEntityLiving();
        if (source == null || livingEntity == null) return;
        float damage = event.getAmount();
        if (!source.isBypassMagic()) {
            if (livingEntity.hasEffect(EffectHandler.SUNBLOCK) && source != DamageSource.OUT_OF_WORLD) {
                int i = (livingEntity.getEffect(EffectHandler.SUNBLOCK).getAmplifier() + 2) * 5;
                int j = 25 - i;
                float f = damage * (float)j;
                float f1 = damage;
                damage = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - damage;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (livingEntity instanceof ServerPlayer) {
                        ((ServerPlayer)livingEntity).awardStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
                    } else if (source.getEntity() instanceof ServerPlayer) {
                        ((ServerPlayer)source.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
                    }
                }
            }
        }

        if (event.getSource().isFire()) {
            event.getEntityLiving().removeEffectNoUpdate(EffectHandler.FROZEN);
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntityLiving(), false));
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntityLiving(), FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntityLiving());
            }
        }
        if (event.getEntity() instanceof Player) {
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
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        Player player = event.player;
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
        if (event.isCancelable() && living.hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(living);
        if (abilityCapability != null && event.isCancelable() && abilityCapability.itemUsePrevented(event.getItem())) {
            event.setCanceled(true);
            return;
        }

        if (living instanceof Player) {
            Player player = (Player) living;
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
            if (event.isCancelable() && living.hasEffect(EffectHandler.FROZEN)) {
                event.setCanceled(true);
                return;
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(living);
            if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
                event.setCanceled(true);
                return;
            }

            if (entity instanceof Player) {
                BlockState block = event.getPlacedBlock();
                if (
                        block.getBlock() == Blocks.FIRE ||
                        block.getBlock() == Blocks.TNT ||
                        block.getBlock() == Blocks.RESPAWN_ANCHOR ||
                        block.getBlock() == Blocks.DISPENSER ||
                        block.getBlock() == Blocks.CACTUS
                ) {
                    aggroBarakoa((Player) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living != null) {
            if (event.isCancelable() && living.hasEffect(EffectHandler.FROZEN)) {
                event.setCanceled(true);
                return;
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntityLiving());
            if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
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
        if (event.isCancelable() && event.getPlayer().hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        BlockState block = event.getState();
        if (block.getBlock() == Blocks.GOLD_BLOCK ||
            block.getMaterial() == Material.WOOD ||
            block.getBlock() == BlockHandler.THATCH.get() ||
            block.getBlock() == Blocks.RED_TERRACOTTA ||
            block.getBlock() == Blocks.SKELETON_SKULL ||
            block.getBlock() == Blocks.TORCH
        ) {
            aggroBarakoa(event.getPlayer());
        }
    }

    public <T extends Entity> List<T> getEntitiesNearby(Entity startEntity, Class<T> entityClass, double r) {
        return startEntity.level.getEntitiesOfClass(entityClass, startEntity.getBoundingBox().inflate(r, r, r), e -> e != startEntity && startEntity.distanceTo(e) <= r);
    }

    private List<LivingEntity> getEntityLivingBaseNearby(LivingEntity user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.level.getEntities(user, user.getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        ArrayList<LivingEntity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && user.distanceTo(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        Player player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {

            if (event.getWorld().isClientSide && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING)) {
                if (player.isShiftKeyDown()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SUNSTRIKE_ABILITY);
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
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        Player player = event.getPlayer();
        if (player.level.getBlockState(event.getPos()).getBlock() instanceof ChestBlock) {
            aggroBarakoa(player);
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {

            ItemStack item = event.getItemStack();
            if (
                    item.getItem() == Items.FLINT_AND_STEEL ||
                    item.getItem() == Items.TNT_MINECART
            ) {
                aggroBarakoa(player);
            }

            if (event.getSide() == LogicalSide.CLIENT && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING)) {
                if (player.isShiftKeyDown()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.SUNSTRIKE_ABILITY);
                }
            }
            if (player.level.getBlockState(event.getPos()).getMenuProvider(player.level, event.getPos()) != null) {
                player.resetAttackStrengthTicker();
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
        Player player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == ItemHandler.SPEAR) {
            LivingEntity entityHit = ItemSpear.raytraceEntities(player.getCommandSenderWorld(), player, range);
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
    public void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getHealth() <= event.getAmount() && entity.hasEffect(EffectHandler.FROZEN)) {
            entity.removeEffectNoUpdate(EffectHandler.FROZEN);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntityLiving(), false));
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(entity);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.itemUsePrevented(event.getItemStack())) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickWithItem(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getPlayer();
        if (event.isCancelable() && player.hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
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
            if (entity.hasEffect(EffectHandler.FROZEN) && entity.isOnGround()) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
            }
        }

        if (event.getEntity() instanceof Player) {
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
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        if (event.getEntity() instanceof Player) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
            if (abilityCapability != null && event.isCancelable() && abilityCapability.attackingPrevented()) {
                event.setCanceled(true);
                return;
            }

            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                playerCapability.setPrevCooledAttackStrength(event.getPlayer().getAttackStrengthScale(0.5f));

                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onLeftClickEntity(event);
                }

                if (event.getTarget() instanceof ItemFrame) {
                    ItemFrame itemFrame = (ItemFrame) event.getTarget();
                    if (itemFrame.getItem().getItem() instanceof ItemBarakoaMask) {
                        aggroBarakoa(event.getPlayer());
                    }
                }
                if (event.getTarget() instanceof LeaderSunstrikeImmune) {
                    aggroBarakoa(event.getPlayer());
                }

                if (!(event.getTarget() instanceof LivingEntity)) return;
                if (event.getTarget() instanceof EntityBarakoanToPlayer) return;
                if (!event.getPlayer().level.isClientSide()) {
                    for (int i = 0; i < playerCapability.getPackSize(); i++) {
                        EntityBarakoanToPlayer barakoa = playerCapability.getTribePack().get(i);
                        LivingEntity living = (LivingEntity) event.getTarget();
                        if (barakoa.getMask() != MaskType.FAITH) {
                            if (!living.isInvulnerable()) barakoa.setTarget(living);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkCritEvent(CriticalHitEvent event) {
        ItemStack weapon = event.getPlayer().getMainHandItem();
        Player attacker = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getPrevCooledAttackStrength() == 1.0f && !weapon.isEmpty() && event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity)event.getTarget();
            if (weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3 lookDir = new Vec3(target.getLookAngle().x, 0, target.getLookAngle().z).normalize();
                Vec3 vecBetween = new Vec3(target.getX() - event.getPlayer().getX(), 0, target.getZ() - event.getPlayer().getZ()).normalize();
                double dot = lookDir.dot(vecBetween);
                if (dot > 0.7) {
                    event.setResult(Event.Result.ALLOW);
                    event.setDamageModifier(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.backstabDamageMultiplier.get().floatValue());
                    target.playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1f, 1.2f);
                    AbilityHandler.INSTANCE.sendAbilityMessage(attacker, AbilityHandler.BACKSTAB_ABILITY);

                    if (target.level.isClientSide() && target != null && attacker != null) {
                        Vec3 ringOffset = attacker.getLookAngle().scale(-target.getBbWidth() / 2.f);
                        ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                        Vec3 pos = target.position().add(0, target.getBbHeight() / 2f, 0).add(ringOffset);
                        AdvancedParticleBase.spawnParticle(target.level, ParticleHandler.RING_SPARKS.get(), pos.x(), pos.y(), pos.z(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, true, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{1f, 1f, 0f}, new float[]{0f, 0.5f, 1f}), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 15f), false)
                        });
                        Random rand = attacker.level.getRandom();
                        float explodeSpeed = 2.5f;
                        for (int i = 0; i < 10; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 8f + 15f;
                            ParticleVanillaCloudExtended.spawnVanillaCloud(target.level, pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                        }
                        for (int i = 0; i < 10; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 2.5f + 5f;
                            AdvancedParticleBase.spawnParticle(target.level, ParticleHandler.PIXEL.get(), pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                        for (int i = 0; i < 6; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 5f + 10f;
                            AdvancedParticleBase.spawnParticle(target.level, ParticleHandler.BUBBLE.get(), pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                    }
                }
            }
            else if (weapon.getItem() instanceof ItemSpear) {
                if (target instanceof Animal && target.getMaxHealth() <= 30 && attacker.level.getRandom().nextFloat() <= 0.334) {
                    event.setResult(Event.Result.ALLOW);
                    event.setDamageModifier(400);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "frozen"), new FrozenCapability.FrozenProvider());
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "last_damage"), new LivingCapability.LivingProvider());
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "ability"), new AbilityCapability.AbilityProvider());
        }
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "player"), new PlayerCapability.PlayerProvider());
        }
    }

    @SubscribeEvent
    public void onRideEntity(EntityMountEvent event) {
        if (event.getEntityMounting() instanceof EntityBarako || event.getEntityMounting() instanceof EntityFrostmaw || event.getEntityMounting() instanceof EntityWroughtnaut)
            event.setCanceled(true);
    }

    private void aggroBarakoa(Player player) {
        List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 50);
        for (EntityBarako barako : barakos) {
            if (barako.getTarget() == null || !(barako.getTarget() instanceof Player)) {
                if (!player.isCreative() && !player.isSpectator() && player.blockPosition().distSqr(barako.getRestrictCenter()) < 900) {
                    if (barako.canAttack(player)) barako.setMisbehavedPlayerId(player.getUUID());
                }
            }
        }
        List<EntityBarakoaVillager> barakoas = getEntitiesNearby(player, EntityBarakoaVillager.class, 50);
        for (EntityBarakoaVillager barakoa : barakoas) {
            if (barakoa.getTarget() == null || !(barakoa.getTarget() instanceof Player)) {
                if (player.blockPosition().distSqr(barakoa.getRestrictCenter()) < 900) {
                    if (barakoa.canAttack(player)) barakoa.setMisbehavedPlayerId(player.getUUID());
                }
            }
        }
    }
}
