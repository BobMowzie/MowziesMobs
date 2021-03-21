package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.LastDamageCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ServerEventHandler {
    private static final int ICE = Block.getStateId(Blocks.ICE.getDefaultState());

    private final static int SUNSTRIKE_COOLDOWN = 55;
    private final static int SOLARBEAM_COOLDOWN = 110;

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ZombieEntity) {
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
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof VillagerEntity) {
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();

            if (entity.getActivePotionEffect(PotionHandler.POISON_RESIST) != null && entity.getActivePotionEffect(Effects.POISON) != null) {
                entity.removeActivePotionEffect(Effects.POISON);
            }

//            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.tick(entity);
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
        if (event.isCancelable() && living.isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            ItemStack item = event.getItem();
            if (item.getItem() == Items.FLINT_AND_STEEL) {
                List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 20);
                for (EntityBarako barako : barakos) {
                    if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                        if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                            if (barako.canAttack(living)) barako.setAttackTarget(living);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if (event.isCancelable() && living.isPotionActive(PotionHandler.FROZEN)) {
                event.setCanceled(true);
            }
            BlockState block = event.getPlacedBlock();
            if (block == Blocks.FIRE.getDefaultState()) {
                List<EntityBarako> barakos = getEntitiesNearby(entity, EntityBarako.class, 20);
                for (EntityBarako barako : barakos) {
                    if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                        if (barako.canAttack(living)) barako.setAttackTarget(living);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living != null) {
            if (event.isCancelable() && living.isPotionActive(PotionHandler.FROZEN)) {
                event.setCanceled(true);
            }
            if (event.getEmptyBucket().getItem() == Items.LAVA_BUCKET) {
                List<EntityBarako> barakos = getEntitiesNearby(living, EntityBarako.class, 20);
                for (EntityBarako barako : barakos) {
                    if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                        if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                            if (barako.canAttack(living)) barako.setAttackTarget(living);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        PlayerEntity player = event.getPlayer();
        BlockState block = event.getState();
        if (block == Blocks.GOLD_BLOCK.getDefaultState()) {
            List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 10);
            for (EntityBarako barako : barakos) {
                if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                    if (barako.canAttack(player)) barako.setAttackTarget(player);
                }
            }
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
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        PlayerEntity player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            if (event.getWorld().isRemote && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING) && playerCapability.getUntilSunstrike() <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSolarBeam());
                    playerCapability.setUntilSunstrike(SOLARBEAM_COOLDOWN);
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
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
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
        PlayerEntity player = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (event.getSide() == LogicalSide.CLIENT && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING) && playerCapability.getUntilSunstrike() <= 0) {
            if (player.isSneaking()) {
                MowziesMobs.NETWORK.sendToServer(new MessagePlayerSolarBeam());
                playerCapability.setUntilSunstrike(SOLARBEAM_COOLDOWN);
            } else {
                MowziesMobs.NETWORK.sendToServer(new MessagePlayerSummonSunstrike());
                playerCapability.setUntilSunstrike(SUNSTRIKE_COOLDOWN);
            }
        }
        if (playerCapability != null) {
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
            event.getEntityLiving().removeActivePotionEffect(PotionHandler.FROZEN);
            MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> event.getEntity()), new MessageUnfreezeEntity(event.getEntityLiving()));
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

        if (event.getSource() instanceof EntityDamageSource && event.getSource().getTrueSource() instanceof LivingEntity)
        {
            LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            ItemStack weapon = attacker.getHeldItemMainhand();
            if (weapon != null && weapon.getItem() instanceof ItemNagaFangDagger) {
                Vector3d lookDir = new Vector3d(target.getLookVec().x, 0, target.getLookVec().z).normalize();
                Vector3d vecBetween = new Vector3d(target.getPosX() - attacker.getPosX(), 0, target.getPosZ() - attacker.getPosZ()).normalize();
                double dot = lookDir.dotProduct(vecBetween);
                if (dot > 0.7) {
                    event.setAmount(event.getAmount() + ConfigHandler.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.bonusDamage.get());
                    target.playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1f, 1.2f);
                    target.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                    MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), new MessageDaggerCrit(target, attacker));
                }
            }
        }

        if (event.getEntityLiving() != null) {
            LivingEntity living = event.getEntityLiving();
            LastDamageCapability.ILastDamageCapability capability = CapabilityHandler.getCapability(living, LastDamageCapability.LastDamageProvider.LAST_DAMAGE_CAPABILITY);
            if (capability != null) {
                capability.setLastDamage(event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
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
        PlayerEntity player = event.getPlayer();
        if (event.isCancelable() && player.isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
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
            if (entity.isPotionActive(PotionHandler.FROZEN) && entity.isOnGround()) {
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
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onLeftClickEntity(event);
                }

                if (!(event.getTarget() instanceof LivingEntity)) return;
                if (event.getTarget() instanceof EntityBarakoanToPlayer) return;
                for (int i = 0; i < playerCapability.getPackSize(); i++)
                    playerCapability.getTribePack().get(i).setAttackTarget((LivingEntity) event.getTarget());
            }
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "frozen"), new FrozenCapability.FrozenProvider());
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "last_damage"), new LastDamageCapability.LastDamageProvider());
        }
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(MowziesMobs.MODID, "player"), new PlayerCapability.PlayerProvider());
        }
    }
}
