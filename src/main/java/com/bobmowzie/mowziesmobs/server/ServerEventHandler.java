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
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.message.MessageFreezeEffect;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerAttackMob;
import com.bobmowzie.mowziesmobs.server.message.MessageSunblockEffect;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ServerEventHandler {

    @SubscribeEvent
    public void onJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player || event.getEntity() instanceof MowzieGeckoEntity) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability((LivingEntity) event.getEntity());
            if (abilityCapability != null) abilityCapability.instanceAbilities((LivingEntity) event.getEntity());
        }

        if (event.getEntity() instanceof Player) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability((Player) event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) playerCapability.addedToWorld(event);
        }

        if (event.getLevel().isClientSide) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof Zombie && !(entity instanceof ZombifiedPiglin)) {
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityFoliaath.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 0, true, false, null));
        }
        if (entity instanceof AbstractSkeleton) {
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 0, true, false, null));
        }

        if (entity instanceof Parrot) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof Animal) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AbstractVillager) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }


        if (entity instanceof Pillager) {
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntitySculptor.class, 0, true, false, null));
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();

            if (entity.getEffect(EffectHandler.POISON_RESIST.get()) != null && entity.getEffect(MobEffects.POISON) != null) {
                entity.removeEffectNoUpdate(MobEffects.POISON);
            }

            if (!entity.level().isClientSide) {
                Item headItemStack = entity.getItemBySlot(EquipmentSlot.HEAD).getItem();
                if (headItemStack instanceof ItemUmvuthanaMask) {
                    ItemUmvuthanaMask mask = (ItemUmvuthanaMask) headItemStack;
                    EffectHandler.addOrCombineEffect(entity, mask.getPotion(), 50, 0, true, false);
                }
            }

            if (entity instanceof Mob && !(entity instanceof EntityUmvuthanaCrane)) {
                Mob mob = (Mob) entity;
                if (mob.getTarget() instanceof EntityUmvuthi && mob.getTarget().hasEffect(EffectHandler.SUNBLOCK.get())) {
                    EntityUmvuthanaCrane sunblocker = mob.level().getNearestEntity(EntityUmvuthanaCrane.class, TargetingConditions.DEFAULT, mob, mob.getX(), mob.getY() + mob.getEyeHeight(), mob.getZ(), mob.getBoundingBox().inflate(40.0D, 15.0D, 40.0D));
                    mob.setTarget(sunblocker);
                }
            }

            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.tick(entity);
            }
            LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.LIVING_CAPABILITY);
            if (livingCapability != null) {
                livingCapability.tick(entity);
            }
            AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
            if (abilityCapability != null) {
                abilityCapability.tick(entity);
            }
        }
    }

    @SubscribeEvent
    public void onAddPotionEffect(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == EffectHandler.SUNBLOCK.get()) {
            if (!event.getEntity().level().isClientSide()) {
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntity(), true));
            }
            MowziesMobs.PROXY.playSunblockSound(event.getEntity());
        }
        if (event.getEffectInstance().getEffect() == EffectHandler.FROZEN.get()) {
            if (!event.getEntity().level().isClientSide()) {
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntity(), true));
                FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
                if (frozenCapability != null) {
                    frozenCapability.onFreeze(event.getEntity());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRemovePotionEffect(MobEffectEvent.Remove event) {
    	if(event.getEffectInstance() == null)
    		return;
        if (!event.getEntity().level().isClientSide() && event.getEffectInstance().getEffect() == EffectHandler.SUNBLOCK.get()) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntity(), false));
        }
        if (!event.getEntity().level().isClientSide() && event.getEffectInstance().getEffect() == EffectHandler.FROZEN.get()) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntity(), false));
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void onPotionEffectExpire(MobEffectEvent.Expired event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (!event.getEntity().level().isClientSide() && effectInstance != null && effectInstance.getEffect() == EffectHandler.SUNBLOCK.get()) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageSunblockEffect(event.getEntity(), false));
        }
        if (!event.getEntity().level().isClientSide() && effectInstance != null && effectInstance.getEffect() == EffectHandler.FROZEN.get()) {
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntity(), false));
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // Copied from LivingEntity's applyPotionDamageCalculations
        DamageSource source = event.getSource();
        LivingEntity livingEntity = event.getEntity();
        if (source == null || livingEntity == null) return;
        float damage = event.getAmount();
        if (!source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            if (livingEntity.hasEffect(EffectHandler.SUNBLOCK.get()) && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                int i = (livingEntity.getEffect(EffectHandler.SUNBLOCK.get()).getAmplifier() + 2) * 5;
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

        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.getEntity().removeEffectNoUpdate(EffectHandler.FROZEN.get());
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntity(), false));
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntity());
            }
        }
        if (event.getEntity() instanceof Player) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onTakeDamage(event);
                }
            }
        }

        if (event.getEntity() != null) {
            LivingEntity living = event.getEntity();
            LivingCapability.ILivingCapability capability = CapabilityHandler.getCapability(living, CapabilityHandler.LIVING_CAPABILITY);
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
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
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
        LivingEntity living = event.getEntity();
        if (event.isCancelable() && living.hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(living);
        if (abilityCapability != null && event.isCancelable() && abilityCapability.itemUsePrevented(event.getItem())) {
            event.setCanceled(true);
            return;
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if (event.isCancelable() && living.hasEffect(EffectHandler.FROZEN.get())) {
                event.setCanceled(true);
                return;
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(living);
            if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
                event.setCanceled(true);
                return;
            }

            if (entity instanceof Player) {
                cheatSculptor((Player) entity);

                BlockState block = event.getPlacedBlock();
                if (
                        block.getBlock() == Blocks.FIRE ||
                        block.getBlock() == Blocks.TNT ||
                        block.getBlock() == Blocks.RESPAWN_ANCHOR ||
                        block.getBlock() == Blocks.DISPENSER ||
                        block.getBlock() == Blocks.CACTUS
                ) {
                    aggroUmvuthana((Player) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntity();
        if (living != null) {
            if (event.isCancelable() && living.hasEffect(EffectHandler.FROZEN.get())) {
                event.setCanceled(true);
                return;
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
            if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
                event.setCanceled(true);
                return;
            }

            if (event.getEmptyBucket().getItem() == Items.LAVA_BUCKET) {
                aggroUmvuthana(event.getEntity());
            }

            if (event.getEmptyBucket().getItem() == Items.WATER_BUCKET) {
                cheatSculptor(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        cheatSculptor(event.getPlayer());

        BlockState block = event.getState();
        if (block.getBlock() == Blocks.GOLD_BLOCK ||
            block.is(BlockTags.PLANKS) ||
            block.is(BlockTags.LOGS) ||
            block.is(BlockTags.LEAVES) ||
            block.getBlock() == Blocks.LIGHT_GRAY_TERRACOTTA ||
            block.getBlock() == Blocks.RED_TERRACOTTA ||
            block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE_SLAB ||
            block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE ||
            block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE_STAIRS ||
            block.getBlock() == Blocks.CAMPFIRE ||
            block.getBlock() == Blocks.IRON_BARS ||
            block.getBlock() == Blocks.SKELETON_SKULL ||
            block.getBlock() == Blocks.TORCH
        ) {
            aggroUmvuthana(event.getPlayer());
        }
    }

    public <T extends Entity> List<T> getEntitiesNearby(Entity startEntity, Class<T> entityClass, double r) {
        return startEntity.level().getEntitiesOfClass(entityClass, startEntity.getBoundingBox().inflate(r, r, r), e -> e != startEntity && startEntity.distanceTo(e) <= r);
    }

    private List<LivingEntity> getEntityBaseNearby(LivingEntity user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.level().getEntities(user, user.getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        ArrayList<LivingEntity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && user.distanceTo(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        Player player = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {

            if (event.getLevel().isClientSide && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING.get())) {
                if (player.isShiftKeyDown()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SUNSTRIKE_ABILITY);
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
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        Player player = event.getEntity();
        if (player.level().getBlockState(event.getPos()).getBlock() instanceof ChestBlock) {
            aggroUmvuthana(player);
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {

            ItemStack item = event.getItemStack();
            if (
                    item.getItem() == Items.FLINT_AND_STEEL ||
                    item.getItem() == Items.TNT_MINECART
            ) {
                aggroUmvuthana(player);
            }

            if (event.getSide() == LogicalSide.CLIENT && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING.get()) && player.level().getBlockState(event.getPos()).getMenuProvider(player.level(), event.getPos()) == null) {
                if (player.isShiftKeyDown()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SUNSTRIKE_ABILITY);
                }
            }
            if (player.getMainHandItem().is(ItemHandler.WROUGHT_AXE.get()) && player.level().getBlockState(event.getPos()).getMenuProvider(player.level(), event.getPos()) != null) {
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
        Player player = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == ItemHandler.SPEAR.get()) {
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
        LivingEntity entity = event.getEntity();
        if (entity.getHealth() <= event.getAmount() && entity.hasEffect(EffectHandler.FROZEN.get())) {
            entity.removeEffectNoUpdate(EffectHandler.FROZEN.get());
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageFreezeEffect(event.getEntity(), false));
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(entity);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.itemUsePrevented(event.getItemStack())) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickWithItem(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (event.isCancelable() && player.hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
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
            if (entity.hasEffect(EffectHandler.FROZEN.get()) && entity.onGround()) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
            }
        }

        if (event.getEntity() instanceof Player) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
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
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        if (event.getEntity() != null) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
            if (abilityCapability != null && event.isCancelable() && abilityCapability.attackingPrevented()) {
                event.setCanceled(true);
                return;
            }

            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                playerCapability.setPrevCooledAttackStrength(event.getEntity().getAttackStrengthScale(0.5f));

                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onLeftClickEntity(event);
                }

                if (event.getTarget() instanceof ItemFrame) {
                    ItemFrame itemFrame = (ItemFrame) event.getTarget();
                    if (itemFrame.getItem().getItem() instanceof ItemUmvuthanaMask) {
                        aggroUmvuthana(event.getEntity());
                    }
                }
                if (event.getTarget() instanceof LeaderSunstrikeImmune) {
                    aggroUmvuthana(event.getEntity());
                }

                if (!(event.getTarget() instanceof LivingEntity)) return;
                if (event.getTarget() instanceof EntityUmvuthanaFollowerToPlayer) return;
                if (!event.getEntity().level().isClientSide()) {
                    for (int i = 0; i < playerCapability.getPackSize(); i++) {
                        EntityUmvuthanaFollowerToPlayer umvuthana = playerCapability.getUmvuthanaPack().get(i);
                        LivingEntity living = (LivingEntity) event.getTarget();
                        if (umvuthana.getMaskType() != MaskType.FAITH) {
                            if (!living.isInvulnerable()) umvuthana.setTarget(living);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkCritEvent(CriticalHitEvent event) {
        ItemStack weapon = event.getEntity().getMainHandItem();
        Player attacker = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getPrevCooledAttackStrength() == 1.0f && !weapon.isEmpty() && event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity)event.getTarget();
            if (weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3 lookDir = new Vec3(target.getLookAngle().x, 0, target.getLookAngle().z).normalize();
                Vec3 vecBetween = new Vec3(target.getX() - event.getEntity().getX(), 0, target.getZ() - event.getEntity().getZ()).normalize();
                double dot = lookDir.dot(vecBetween);
                if (dot > 0.7) {
                    event.setResult(Event.Result.ALLOW);
                    event.setDamageModifier(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.backstabDamageMultiplier.get().floatValue());
                    target.playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1f, 1.2f);
                    AbilityHandler.INSTANCE.sendAbilityMessage(attacker, AbilityHandler.BACKSTAB_ABILITY);

                    if (target.level().isClientSide() && target != null && attacker != null) {
                        Vec3 ringOffset = attacker.getLookAngle().scale(-target.getBbWidth() / 2.f);
                        ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                        Vec3 pos = target.position().add(0, target.getBbHeight() / 2f, 0).add(ringOffset);
                        AdvancedParticleBase.spawnParticle(target.level(), ParticleHandler.RING_SPARKS.get(), pos.x(), pos.y(), pos.z(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, true, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{1f, 1f, 0f}, new float[]{0f, 0.5f, 1f}), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 15f), false)
                        });
                        RandomSource rand = attacker.level().getRandom();
                        float explodeSpeed = 2.5f;
                        for (int i = 0; i < 10; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 8f + 15f;
                            ParticleVanillaCloudExtended.spawnVanillaCloud(target.level(), pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                        }
                        for (int i = 0; i < 10; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 2.5f + 5f;
                            AdvancedParticleBase.spawnParticle(target.level(), ParticleHandler.PIXEL.get(), pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                        for (int i = 0; i < 6; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 5f + 10f;
                            AdvancedParticleBase.spawnParticle(target.level(), ParticleHandler.BUBBLE.get(), pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                    }
                }
            }
            else if (weapon.getItem() instanceof ItemSpear) {
                if (target instanceof Animal && target.getMaxHealth() <= 30 && attacker.level().getRandom().nextFloat() <= 0.334) {
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
        if (
                event.getEntityMounting() instanceof EntityUmvuthi ||
                event.getEntityMounting() instanceof EntityFrostmaw ||
                event.getEntityMounting() instanceof EntityWroughtnaut ||
                event.getEntityMounting() instanceof EntitySculptor
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        List<MowzieEntity> mobs = getEntitiesNearby(event.getEntity(), MowzieEntity.class, 40);
        for (MowzieEntity mob : mobs) {
            if (mob.resetHealthOnPlayerRespawn()) {
                mob.setHealth(mob.getMaxHealth());
            }
        }
    }

    @SubscribeEvent
    public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(BlockHandler.CLAWED_LOG.get().asItem()) || event.getItemStack().is(BlockHandler.PAINTED_ACACIA.get().asItem())) {
            event.setBurnTime(300);
        }
        else if (event.getItemStack().is(BlockHandler.PAINTED_ACACIA_SLAB.get().asItem())) {
            event.setBurnTime(150);
        }
        else if (event.getItemStack().is(BlockHandler.THATCH.get().asItem())) {
            event.setBurnTime(100);
        }
    }

    private void aggroUmvuthana(Player player) {
        List<EntityUmvuthi> barakos = getEntitiesNearby(player, EntityUmvuthi.class, 50);
        for (EntityUmvuthi barako : barakos) {
            if (barako.getTarget() == null || !(barako.getTarget() instanceof Player)) {
                if (!player.isCreative() && !player.isSpectator() && player.blockPosition().distSqr(barako.getRestrictCenter()) < 900) {
                    if (barako.canAttack(player)) barako.setMisbehavedPlayerId(player.getUUID());
                }
            }
        }
        List<EntityUmvuthanaMinion> barakoas = getEntitiesNearby(player, EntityUmvuthanaMinion.class, 50);
        for (EntityUmvuthanaMinion barakoa : barakoas) {
            if (barakoa.getTarget() == null || !(barakoa.getTarget() instanceof Player)) {
                if (player.blockPosition().distSqr(barakoa.getRestrictCenter()) < 900) {
                    if (barakoa.canAttack(player)) barakoa.setMisbehavedPlayerId(player.getUUID());
                }
            }
        }
    }

    private void cheatSculptor(Player player) {
        List<EntitySculptor> sculptors = player.level().getEntitiesOfClass(EntitySculptor.class, player.getBoundingBox().inflate(EntitySculptor.TEST_RADIUS + 3, EntitySculptor.TEST_HEIGHT, EntitySculptor.TEST_RADIUS + 3), EntitySculptor::isTesting);
        for (EntitySculptor sculptor : sculptors) {
            sculptor.playerCheated();
        }
    }
}
