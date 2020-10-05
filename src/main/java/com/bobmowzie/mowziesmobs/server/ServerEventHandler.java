package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthTalisman;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.world.MowzieWorldGenerator;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

        if (entity instanceof OcelotEntity) {
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof ParrotEntity) {
            ((CreatureEntity) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AnimalEntity) {
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
            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);

            if (property != null) {
                // Freeze logic
                if (property.freezeProgress >= 1) {
                    entity.addPotionEffect(new EffectInstance(PotionHandler.FROZEN, 50, 0, false, false));
                    property.freezeProgress = 1f;
                } else if (property.freezeProgress > 0) {
                    entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 9, MathHelper.floor(property.freezeProgress * 5 + 1), false, false));
                }

                if (entity.isPotionActive(PotionHandler.FROZEN) && !property.prevFrozen) {
                    property.onFreeze(entity);
                }

                if (!entity.world.isRemote) {
                    Item headItemStack = entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
                    if (headItemStack instanceof ItemBarakoaMask) {
                        ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
                        entity.addPotionEffect(new EffectInstance(mask.getPotion(), 45, 0, true, false));
                    }
                }
            }

            if (entity.getActivePotionEffect(PotionHandler.POISON_RESIST) != null && entity.getActivePotionEffect(Effects.POISON) != null) {
                entity.removeActivePotionEffect(Effects.POISON);
            }

            if (entity.isPotionActive(PotionHandler.FROZEN)) {
                if (entity.getActivePotionEffect(PotionHandler.FROZEN).getDuration() <= 0) entity.removeActivePotionEffect(PotionHandler.FROZEN);
                entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 50, false, false));
                entity.setSneaking(false);

                if (entity.world.isRemote && entity.ticksExisted % 2 == 0) {
                    double cloudX = entity.posX + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double cloudZ = entity.posZ + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double cloudY = entity.posY + entity.getHeight() * entity.getRNG().nextFloat();
                    MMParticle.CLOUD.spawn(entity.world, cloudX, cloudY, cloudZ, ParticleFactory.ParticleArgs.get().withData(0d, -0.01d, 0d, 0.75d, 0.75d, 1d, 1, 15d, 25, ParticleCloud.EnumCloudBehavior.CONSTANT));

                    double snowX = entity.posX + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double snowZ = entity.posZ + entity.getWidth() * entity.getRNG().nextFloat() - entity.getWidth() / 2;
                    double snowY = entity.posY + entity.getHeight() * entity.getRNG().nextFloat();
                    MMParticle.SNOWFLAKE.spawn(entity.world, snowX, snowY, snowZ, ParticleFactory.ParticleArgs.get().withData(0d, -0.01d, 0d));
                }
            }
            else {
                if (property != null && property.frozenController != null && property.frozenController.isAlive()) {
                    property.onUnfreeze(entity);
                }
            }

            if (property != null) {
                if (property.freezeDecayDelay <= 0) {
                    property.freezeProgress -= 0.1;
                    if (property.freezeProgress < 0) property.freezeProgress = 0;
                }
                else {
                    property.freezeDecayDelay--;
                }
                property.prevFrozen = entity.isPotionActive(PotionHandler.FROZEN);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        PlayerEntity player = event.player;
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            property.update();

            if (property.untilSunstrike > 0) {
                property.untilSunstrike--;
            }
            if (property.untilAxeSwing > 0) {
                property.untilAxeSwing--;
            }

            if (event.side == LogicalSide.SERVER) {
                for (ItemStack itemStack : event.player.inventory.mainInventory) {
                    if (itemStack.getItem() instanceof ItemEarthTalisman)
                        player.addPotionEffect(new EffectInstance(PotionHandler.GEOMANCY, 0, 0, false, false));
                }
                if (player.getHeldItemOffhand().getItem() instanceof ItemEarthTalisman)
                    player.addPotionEffect(new EffectInstance(PotionHandler.GEOMANCY, 0, 0, false, false));

                List<EntityBarakoanToPlayer> pack = property.tribePack;
                float theta = (2 * (float) Math.PI / pack.size());
                for (int i = 0; i < pack.size(); i++) {
                    EntityBarakoanToPlayer barakoan = pack.get(i);
                    barakoan.index = i;
                    if (barakoan.getAttackTarget() == null && barakoan.getAnimation() != barakoan.DEACTIVATE_ANIMATION) {
                        barakoan.getNavigator().tryMoveToXYZ(player.posX + property.tribePackRadius * MathHelper.cos(theta * i), player.posY, player.posZ + property.tribePackRadius * MathHelper.sin(theta * i), 0.45);
                        if (player.getDistance(barakoan) > 20 && player.onGround) {
                            tryTeleportBarakoan(player, barakoan);
                        }
                    }
                }
            }

            if (!(player.getHeldItemMainhand().getItem() == ItemHandler.ICE_CRYSTAL || player.getHeldItemOffhand().getItem() == ItemHandler.ICE_CRYSTAL) && property.usingIceBreath && property.icebreath != null) {
                property.usingIceBreath = false;
                property.icebreath.remove();
            }

            if (!ConfigHandler.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (!property.usingIceBreath && stack.getItem() == ItemHandler.ICE_CRYSTAL)
                        stack.setDamage(Math.max(stack.getDamage() - 1, 0));
                }
                for (ItemStack stack : player.inventory.offHandInventory) {
                    if (!property.usingIceBreath && stack.getItem() == ItemHandler.ICE_CRYSTAL)
                        stack.setDamage(Math.max(stack.getDamage() - 1, 0));
                }
            }

            if (event.side == LogicalSide.CLIENT) {
                if (Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && !property.mouseLeftDown) {
                    property.mouseLeftDown = true;
                    MowziesMobs.NETWORK.sendToServer(new MessageLeftMouseDown());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onLeftMouseDown(player);
                    }
                }
                if (Minecraft.getInstance().gameSettings.keyBindUseItem.isKeyDown() && !property.mouseRightDown) {
                    property.mouseRightDown = true;
                    MowziesMobs.NETWORK.sendToServer(new MessageRightMouseDown());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onRightMouseDown(player);
                    }
                }
                if (!Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && property.mouseLeftDown) {
                    property.mouseLeftDown = false;
                    MowziesMobs.NETWORK.sendToServer(new MessageLeftMouseUp());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onLeftMouseUp(player);
                    }
                }
                if (!Minecraft.getInstance().gameSettings.keyBindUseItem.isKeyDown() && property.mouseRightDown) {
                    property.mouseRightDown = false;
                    MowziesMobs.NETWORK.sendToServer(new MessageRightMouseUp());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onRightMouseUp(player);
                    }
                }
            }

            if (player.isSneaking() && !property.prevSneaking) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onSneakDown(player);
                }
            }
            else if (!player.isSneaking() && property.prevSneaking) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onSneakUp(player);
                }
            }
            property.prevSneaking = player.isSneaking();

            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onUpdate(event);
            }
        }
    }

    private void tryTeleportBarakoan(PlayerEntity player, EntityBarakoanToPlayer barakoan) {
        int x = MathHelper.floor(player.posX) - 2;
        int z = MathHelper.floor(player.posZ) - 2;
        int y = MathHelper.floor(player.getBoundingBox().minY);

        for (int l = 0; l <= 4; ++l) {
            for (int i1 = 0; i1 <= 4; ++i1) {
                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && barakoan.isTeleportFriendlyBlock(x, z, y, l, i1)) {
                    barakoan.setLocationAndAngles((double) ((float) (x + l) + 0.5F), (double) y, (double) ((float) (z + i1) + 0.5F), barakoan.rotationYaw, barakoan.rotationPitch);
                    barakoan.getNavigator().clearPath();
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntityLiving();
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
        Entity entity  = event.getEntity();
        BlockState block = event.getPlacedBlock();
        if (entity instanceof LivingEntity && block == Blocks.FIRE.getDefaultState()) {
            LivingEntity living = (LivingEntity)entity;
            List<EntityBarako> barakos = getEntitiesNearby(entity, EntityBarako.class, 20);
            for (EntityBarako barako : barakos) {
                if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof PlayerEntity)) {
                    if (barako.canAttack(living)) barako.setAttackTarget(living);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living  = event.getEntityLiving();
        if (living != null) {
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
        PlayerEntity player = event.getEntityPlayer();
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            if (event.getWorld().isRemote && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING) && EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class).untilSunstrike <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSolarBeam());
                    property.untilSunstrike = SOLARBEAM_COOLDOWN;
                } else {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSummonSunstrike());
                    property.untilSunstrike = SUNSTRIKE_COOLDOWN;
                }
            }

            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onRightClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
        if (property != null) {
            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onRightClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getEntityPlayer();
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            if (event.getWorld().isRemote && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING) && EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class).untilSunstrike <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSolarBeam());
                    property.untilSunstrike = SOLARBEAM_COOLDOWN;
                } else {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerSummonSunstrike());
                    property.untilSunstrike = SUNSTRIKE_COOLDOWN;
                }
            }

            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onRightClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        double range = 6.5;
        PlayerEntity player = event.getEntityPlayer();
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == ItemHandler.SPEAR) {
                LivingEntity entityHit = ItemSpear.raytraceEntities(player.getEntityWorld(), player, range);
                if (entityHit != null) {
                    MowziesMobs.NETWORK.sendToServer(new MessagePlayerAttackMob(entityHit));
                }
            }

            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onLeftClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingHurtEvent event) {
        if (event.getSource().isFireDamage() && event.getEntityLiving().isPotionActive(PotionHandler.FROZEN)) {
                event.getEntityLiving().removeActivePotionEffect(PotionHandler.FROZEN);
//                MowziesMobs.NETWORK.sendToDimension(new MessageUnfreezeEntity(event.getEntityLiving()), event.getEntityLiving().dimension); TODO
        }
        if (event.getEntity() instanceof PlayerEntity) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
            if (property != null) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onTakeDamage(event);
                }
            }
        }

        if (event.getSource() instanceof EntityDamageSource && event.getSource().getTrueSource() instanceof LivingEntity)
        {
            LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            ItemStack weapon = attacker.getHeldItemMainhand();
            if (weapon != null && weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3d lookDir = new Vec3d(target.getLookVec().x, 0, target.getLookVec().z).normalize();
                Vec3d vecBetween = new Vec3d(target.posX - attacker.posX, 0, target.posZ - attacker.posZ).normalize();
                double dot = lookDir.dotProduct(vecBetween);
                if (dot > 0.7) event.setAmount(event.getAmount() + ConfigHandler.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.bonusDamage);
            }
        }

        if (event.getEntityLiving() != null) {
            LivingEntity living = event.getEntityLiving();
            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(living, MowzieLivingProperties.class);
            if (property != null) {
                property.lastDamage = event.getAmount();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
        if (property != null) {
            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onRightClickWithItem(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && event.getEntityPlayer().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
        if (property != null) {
            for (int i = 0; i < property.powers.length; i++) {
                property.powers[i].onLeftClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
            if (property != null) {
                if (entity.isPotionActive(PotionHandler.FROZEN) && entity.onGround) {
                    entity.setMotion(entity.getMotion().mul(1, 0, 1));
                }
            }
        }

        if (event.getEntity() instanceof PlayerEntity) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
            if (property != null) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onJump(event);
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
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
            if (property != null) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onLeftClickEntity(event);
                }

                if (!(event.getTarget() instanceof LivingEntity)) return;
                if (event.getTarget() instanceof EntityBarakoanToPlayer) return;
                for (int i = 0; i < property.getPackSize(); i++)
                    property.tribePack.get(i).setAttackTarget((LivingEntity) event.getTarget());
            }
        }
    }

    /*@SubscribeEvent
    public void prePopulateWorld(.Pre event) {
        MowzieWorldGenerator.generatePrePopulate(event.getWorld(), event.getRand(), event.getChunkX(), event.getChunkZ());
    }*/ // TODO

    /*@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MowziesMobs.MODID)) {
            ConfigManager.sync(MowziesMobs.MODID, Config.Type.INSTANCE);
        }
    }*/ // TODO
}
