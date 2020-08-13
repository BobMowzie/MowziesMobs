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
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum ServerEventHandler {
    INSTANCE;

    private static final int ICE = Block.getStateId(Blocks.ICE.getDefaultState());

    private final static int SUNSTRIKE_COOLDOWN = 55;
    private final static int SOLARBEAM_COOLDOWN = 110;

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof EntityZombie) {
            ((EntityCreature) entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget<>((EntityCreature) entity, EntityFoliaath.class, 0, true, false, null));
            ((EntityCreature) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget<>((EntityCreature) entity, EntityBarakoa.class, 0, true, false, null));
            ((EntityCreature) entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget<>((EntityCreature) entity, EntityBarako.class, 0, true, false, null));
        }
        if (entity instanceof EntitySkeleton) {
            ((EntityCreature) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget<>((EntityCreature) entity, EntityBarakoa.class, 0, true, false, null));
            ((EntityCreature) entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget<>((EntityCreature) entity, EntityBarako.class, 0, true, false, null));
        }

        if (entity instanceof EntityOcelot) {
            ((EntityCreature) entity).tasks.addTask(3, new EntityAIAvoidEntity<>((EntityCreature) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof EntityParrot) {
            ((EntityCreature) entity).tasks.addTask(3, new EntityAIAvoidEntity<>((EntityCreature) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof EntityAnimal) {
            ((EntityCreature) entity).tasks.addTask(3, new EntityAIAvoidEntity<>((EntityCreature) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((EntityCreature) entity).tasks.addTask(3, new EntityAIAvoidEntity<>((EntityCreature) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof EntityVillager) {
            ((EntityCreature) entity).tasks.addTask(3, new EntityAIAvoidEntity<>((EntityCreature) entity, EntityBarakoa.class, 6.0F, 1.0D, 1.2D));
            ((EntityCreature) entity).tasks.addTask(3, new EntityAIAvoidEntity<>((EntityCreature) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);

            if (property != null) {
                if (property.freezeProgress >= 1) {
                    entity.addPotionEffect(new PotionEffect(PotionHandler.FROZEN, 50, 0, false, false));
                    property.freezeProgress = 1f;
                } else if (property.freezeProgress > 0) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9, MathHelper.floor(property.freezeProgress * 5 + 1), false, false));
                }

                if (entity.isPotionActive(PotionHandler.FROZEN) && !property.prevFrozen) {
                    property.onFreeze(entity);
                }

                if (!entity.world.isRemote) {
                    Item headItemStack = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem();
                    if (headItemStack instanceof ItemBarakoaMask) {
                        ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
                        entity.addPotionEffect(new PotionEffect(mask.getPotion(), 45, 0, true, false));
                    }
                }
            }

            if (entity.getActivePotionEffect(PotionHandler.POISON_RESIST) != null && entity.getActivePotionEffect(MobEffects.POISON) != null) {
                entity.removeActivePotionEffect(MobEffects.POISON);
            }

            if (entity.isPotionActive(PotionHandler.FROZEN)) {
                if (entity.getActivePotionEffect(PotionHandler.FROZEN).getDuration() <= 0) entity.removeActivePotionEffect(PotionHandler.FROZEN);
                entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 2, 50, false, false));
                entity.setSneaking(false);

                if (entity.world.isRemote && entity.ticksExisted % 2 == 0) {
                    double cloudX = entity.posX + entity.width * entity.getRNG().nextFloat() - entity.width / 2;
                    double cloudZ = entity.posZ + entity.width * entity.getRNG().nextFloat() - entity.width / 2;
                    double cloudY = entity.posY + entity.height * entity.getRNG().nextFloat();
                    MMParticle.CLOUD.spawn(entity.world, cloudX, cloudY, cloudZ, ParticleFactory.ParticleArgs.get().withData(0d, -0.01d, 0d, 0.75d, 0.75d, 1d, 1, 15d, 25, ParticleCloud.EnumCloudBehavior.CONSTANT));

                    double snowX = entity.posX + entity.width * entity.getRNG().nextFloat() - entity.width / 2;
                    double snowZ = entity.posZ + entity.width * entity.getRNG().nextFloat() - entity.width / 2;
                    double snowY = entity.posY + entity.height * entity.getRNG().nextFloat();
                    MMParticle.SNOWFLAKE.spawn(entity.world, snowX, snowY, snowZ, ParticleFactory.ParticleArgs.get().withData(0d, -0.01d, 0d));
                }
            }
            else {
                if (property != null && property.frozenController != null && !property.frozenController.isDead) {
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
        EntityPlayer player = event.player;
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            property.update();

            if (property.untilSunstrike > 0) {
                property.untilSunstrike--;
            }
            if (property.untilAxeSwing > 0) {
                property.untilAxeSwing--;
            }

            if (event.side == Side.SERVER) {
                for (ItemStack itemStack : event.player.inventory.mainInventory) {
                    if (itemStack.getItem() instanceof ItemEarthTalisman)
                        player.addPotionEffect(new PotionEffect(PotionHandler.GEOMANCY, 0, 0, false, false));
                }
                if (player.getHeldItemOffhand().getItem() instanceof ItemEarthTalisman)
                    player.addPotionEffect(new PotionEffect(PotionHandler.GEOMANCY, 0, 0, false, false));

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
                property.icebreath.setDead();
            }

            for (ItemStack stack : player.inventory.mainInventory) {
                if (!property.usingIceBreath && stack.getItem() == ItemHandler.ICE_CRYSTAL)
                    stack.setItemDamage(Math.max(stack.getItemDamage() - 1, 0));
            }
            for (ItemStack stack : player.inventory.offHandInventory) {
                if (!property.usingIceBreath && stack.getItem() == ItemHandler.ICE_CRYSTAL)
                    stack.setItemDamage(Math.max(stack.getItemDamage() - 1, 0));
            }

            if (event.side == Side.CLIENT) {
                if (Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown() && !property.mouseLeftDown) {
                    property.mouseLeftDown = true;
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessageLeftMouseDown());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onLeftMouseDown(player);
                    }
                }
                if (Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown() && !property.mouseRightDown) {
                    property.mouseRightDown = true;
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessageRightMouseDown());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onRightMouseDown(player);
                    }
                }
                if (!Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown() && property.mouseLeftDown) {
                    property.mouseLeftDown = false;
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessageLeftMouseUp());
                    for (int i = 0; i < property.powers.length; i++) {
                        property.powers[i].onLeftMouseUp(player);
                    }
                }
                if (!Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown() && property.mouseRightDown) {
                    property.mouseRightDown = false;
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessageRightMouseUp());
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

    private void tryTeleportBarakoan(EntityPlayer player, EntityBarakoanToPlayer barakoan) {
        int x = MathHelper.floor(player.posX) - 2;
        int z = MathHelper.floor(player.posZ) - 2;
        int y = MathHelper.floor(player.getEntityBoundingBox().minY);

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
        EntityLivingBase living = event.getEntityLiving();
        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            ItemStack item = event.getItem();
            if (item.getItem() == Items.FLINT_AND_STEEL) {
                List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 20);
                for (EntityBarako barako : barakos) {
                    if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof EntityPlayer)) {
                        if (EntityAITarget.isSuitableTarget(barako, player, false, false))
                            barako.setAttackTarget(player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.PlaceEvent event) {
        EntityPlayer player  = event.getPlayer();
        IBlockState block = event.getPlacedBlock();
        if (block == Blocks.FIRE.getDefaultState()) {
            List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 20);
            for (EntityBarako barako : barakos) {
                if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof EntityPlayer)) {
                    if (EntityAITarget.isSuitableTarget(barako, player, false, false)) barako.setAttackTarget(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        EntityPlayer player  = event.getEntityPlayer();
        if (player != null) {
            if (event.getEmptyBucket() != null) {
                if (event.getEmptyBucket().getItem() == Items.LAVA_BUCKET) {
                    List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 20);
                    for (EntityBarako barako : barakos) {
                        if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof EntityPlayer)) {
                            if (EntityAITarget.isSuitableTarget(barako, player, false, false))
                                barako.setAttackTarget(player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        IBlockState block = event.getState();
        if (block == Blocks.GOLD_BLOCK.getDefaultState()) {
            List<EntityBarako> barakos = getEntitiesNearby(player, EntityBarako.class, 10);
            for (EntityBarako barako : barakos) {
                if (barako.getAttackTarget() == null || !(barako.getAttackTarget() instanceof EntityPlayer)) {
                    if (EntityAITarget.isSuitableTarget(barako, player, false, false)) barako.setAttackTarget(player);
                }
            }
        }
    }

    public <T extends Entity> List<T> getEntitiesNearby(EntityLivingBase startEntity, Class<T> entityClass, double r) {
        return startEntity.world.getEntitiesWithinAABB(entityClass, startEntity.getEntityBoundingBox().grow(r, r, r), e -> e != startEntity && startEntity.getDistance(e) <= r);
    }

    private List<EntityLivingBase> getEntityLivingBaseNearby(EntityLivingBase user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.world.getEntitiesWithinAABBExcludingEntity(user, user.getEntityBoundingBox().grow(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityLivingBase && user.getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (EntityLivingBase) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(PotionHandler.FROZEN)) {
            event.setCanceled(true);
        }
        EntityPlayer player = event.getEntityPlayer();
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            if (event.getWorld().isRemote && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING) && EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class).untilSunstrike <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerSolarBeam());
                    property.untilSunstrike = SOLARBEAM_COOLDOWN;
                } else {
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerSummonSunstrike());
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
        EntityPlayer player = event.getEntityPlayer();
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            if (event.getWorld().isRemote && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING) && EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class).untilSunstrike <= 0) {
                if (player.isSneaking()) {
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerSolarBeam());
                    property.untilSunstrike = SOLARBEAM_COOLDOWN;
                } else {
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerSummonSunstrike());
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
        EntityPlayer player = event.getEntityPlayer();
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (property != null) {
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == ItemHandler.SPEAR) {
                EntityLivingBase entityHit = ItemSpear.raytraceEntities(player.getEntityWorld(), player, range);
                if (entityHit != null) {
                    MowziesMobs.NETWORK_WRAPPER.sendToServer(new MessagePlayerAttackMob(entityHit));
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
                MowziesMobs.NETWORK_WRAPPER.sendToDimension(new MessageUnfreezeEntity(event.getEntityLiving()), event.getEntityLiving().dimension);
        }
        if (event.getEntity() instanceof EntityPlayer) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
            if (property != null) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onTakeDamage(event);
                }
            }
        }

        if (event.getSource() instanceof EntityDamageSource && event.getSource().getTrueSource() instanceof EntityLivingBase)
        {
            EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
            EntityLivingBase target = event.getEntityLiving();
            ItemStack weapon = attacker.getHeldItemMainhand();
            if (weapon != null && weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3d lookDir = new Vec3d(target.getLookVec().x, 0, target.getLookVec().z).normalize();
                Vec3d vecBetween = new Vec3d(target.posX - attacker.posX, 0, target.posZ - attacker.posZ).normalize();
                double dot = lookDir.dotProduct(vecBetween);
                if (dot > 0.7) event.setAmount(event.getAmount() + 3 * ConfigHandler.TOOLS_AND_ABILITIES.nagaDaggerAttackMultiplier);
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
        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
            if (property != null) {
                if (entity.isPotionActive(PotionHandler.FROZEN) && entity.onGround) {
                    entity.motionY = 0;
                }
            }
        }

        if (event.getEntity() instanceof EntityPlayer) {
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
        if (event.getEntity() instanceof EntityPlayer) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), MowziePlayerProperties.class);
            if (property != null) {
                for (int i = 0; i < property.powers.length; i++) {
                    property.powers[i].onLeftClickEntity(event);
                }

                if (!(event.getTarget() instanceof EntityLivingBase)) return;
                if (event.getTarget() instanceof EntityBarakoanToPlayer) return;
                for (int i = 0; i < property.getPackSize(); i++)
                    property.tribePack.get(i).setAttackTarget((EntityLivingBase) event.getTarget());
            }
        }
    }

    @SubscribeEvent
    public void prePopulateWorld(PopulateChunkEvent.Pre event) {
        MowzieWorldGenerator.generatePrePopulate(event.getWorld(), event.getRand(), event.getChunkX(), event.getChunkZ());
    }
}
