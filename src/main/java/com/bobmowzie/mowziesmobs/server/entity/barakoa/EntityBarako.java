package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.trade.Trade;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoayaTrade;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.google.common.collect.ImmutableSet;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationRadiusAttack;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationSolarBeam;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationSpawnBarakoa;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationSunStrike;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemTestStructure;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.base.Optional;

import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class EntityBarako extends MowzieEntity implements LeaderSunstrikeImmune, GuiHandler.ContainerHolder {
    public static final Animation DIE_ANIMATION = Animation.create(130);
    public static final Animation HURT_ANIMATION = Animation.create(13);
    public static final Animation BELLY_ANIMATION = Animation.create(40);
    public static final Animation TALK_ANIMATION = Animation.create(80);
    public static final Animation SUNSTRIKE_ANIMATION = Animation.create(15);
    public static final Animation ATTACK_ANIMATION = Animation.create(30);
    public static final Animation SPAWN_ANIMATION = Animation.create(20);
    public static final Animation SOLAR_BEAM_ANIMATION = Animation.create(100);
    public static final Animation BLESS_ANIMATION = Animation.create(60);
    private static final int MAX_HEALTH = 80;
    private static final int SUNSTRIKE_PAUSE_MAX = 40;
    private static final int SUNSTRIKE_PAUSE_MIN = 15;
    private static final int LASER_PAUSE = 230;
    private static final int BARAKOA_PAUSE = 150;
    private static final DataParameter<Integer> DIRECTION = EntityDataManager.createKey(EntityBarako.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DIALOGUE = EntityDataManager.createKey(EntityBarako.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ANGRY = EntityDataManager.createKey(EntityBarako.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<ItemStack>> DESIRES = EntityDataManager.createKey(EntityBarako.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private final Set<UUID> tradedPlayers = new HashSet<>();
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    public ControlledAnimation angryEyebrow = new ControlledAnimation(5);
    private EntityPlayer customer;
    // TODO: Enum!
    public int whichDialogue = 0;
    public int barakoaSpawnCount = 0;
    // TODO: use EnumFacing!
    private int direction = 0;
    private boolean blocksByFeet = true;
    private int timeUntilSunstrike = 0;
    private int timeUntilLaser = 0;
    private int timeUntilBarakoa = 0;

    public EntityBarako(World world) {
        super(world);
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.tasks.addTask(4, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, false));
        this.tasks.addTask(4, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, 0, false, false, null));
        this.tasks.addTask(4, new EntityAINearestAttackableTarget<>(this, EntitySkeleton.class, 0, false, false, null));
        this.tasks.addTask(2, new AnimationAI<>(this, BELLY_ANIMATION, false));
        this.tasks.addTask(2, new AnimationAI<>(this, TALK_ANIMATION, false));
        this.tasks.addTask(2, new AnimationAI<>(this, BLESS_ANIMATION, false));
        this.tasks.addTask(2, new AnimationSunStrike<>(this, SUNSTRIKE_ANIMATION));
        this.tasks.addTask(2, new AnimationRadiusAttack<>(this, ATTACK_ANIMATION, 5, 5, 4.5f, 12, true));
        this.tasks.addTask(2, new AnimationSpawnBarakoa(this, SPAWN_ANIMATION));
        this.tasks.addTask(2, new AnimationSolarBeam<>(this, SOLAR_BEAM_ANIMATION));
        this.tasks.addTask(3, new AnimationTakeDamage<>(this));
        this.tasks.addTask(1, new AnimationDieAI<>(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityBarakoa.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.setSize(1.5f, 2.4f);
        if (getDirection() == 0) {
            this.setDirection(rand.nextInt(4) + 1);
        }
    }

    public EntityBarako(World world, int direction) {
        this(world);
        this.setDirection(direction);
    }

    @Override
    public float getEyeHeight() {
        return 1.4f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getAnimation() == NO_ANIMATION) {
            if (getAttackTarget() == null) {
                int soundType = MathHelper.getInt(this.rand, 0, 9);
                if (soundType < MMSounds.ENTITY_BARAKO_TALK.length) {
                    this.playSound(MMSounds.ENTITY_BARAKO_TALK[soundType], 2F, 1.0F);
                    this.setWhichDialogue(soundType + 1);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, TALK_ANIMATION);
                }
            } else {
                int soundType = MathHelper.getInt(rand, 1, 10);
                if (soundType < 7) {
                    this.playSound(MMSounds.ENTITY_BARAKO_ANGRY[soundType - 1], 2F, 1.0F);
//                    setWhichDialogue(soundType);
//                    AnimationHandler.INSTANCE.sendAnimationMessage(this, 3);
                }
            }
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return MMSounds.ENTITY_BARAKO_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_BARAKO_DIE, 2f, 1);
        return null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1) {
            direction = getDirection();
        }
        this.repelEntities(2.2f, 2.5f, 2.2f, 2.2f);
        this.rotationYaw = (direction - 1) * 90;
        this.renderYawOffset = rotationYaw;
        this.posX = prevPosX;
        this.posZ = prevPosZ;

        if (getAttackTarget() != null) {
            EntityLivingBase target = getAttackTarget();
            this.setAngry(true);
            float entityHitAngle = (float) ((Math.atan2(target.posZ - posZ, target.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = rotationYaw % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = Math.abs(entityHitAngle - entityAttackingAngle);
            if (getAnimation() == NO_ANIMATION && getHealth() <= 60 && timeUntilLaser <= 0 && (entityRelativeAngle < 60 || entityRelativeAngle > 300)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SOLAR_BEAM_ANIMATION);
                timeUntilLaser = LASER_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && targetDistance <= 5) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
            } else if (getAnimation() == NO_ANIMATION && rand.nextInt(80) == 0 && getEntitiesNearby(EntityBarakoa.class, 16).size() < 5 && targetDistance > 5 && timeUntilBarakoa <= 0) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SPAWN_ANIMATION);
                timeUntilBarakoa = BARAKOA_PAUSE;
            } else if (getAnimation() == NO_ANIMATION && timeUntilSunstrike <= 0 && targetDistance > 5) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, SUNSTRIKE_ANIMATION);
                timeUntilSunstrike = getTimeUntilSunstrike();
            }
        } else {
            if (!world.isRemote) {
                this.setAngry(false);
            }
        }

        if (ticksExisted % 20 == 0) {
            blocksByFeet = checkBlocksByFeet();
        }

        if (blocksByFeet) {
            legsUp.increaseTimer();
        } else {
            legsUp.decreaseTimer();
        }

        if (getAngry()) {
            angryEyebrow.increaseTimer();
        } else {
            angryEyebrow.decreaseTimer();
        }

        if (getAnimation() == NO_ANIMATION && getAttackTarget() == null && rand.nextInt(200) == 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BELLY_ANIMATION);
        }

        if (getAnimation() == BELLY_ANIMATION && (getAnimationTick() == 9 || getAnimationTick() == 29)) {
            this.playSound(MMSounds.ENTITY_BARAKO_BELLY, 3f, 1f);
        }

        if (getAnimation() == TALK_ANIMATION && getAnimationTick() == 1) {
            whichDialogue = getWhichDialogue();
        }

        if (getAnimation() == ATTACK_ANIMATION) {
            rotationYawHead = rotationYaw;
            if (getAnimationTick() == 1) {
                this.playSound(MMSounds.ENTITY_BARAKO_BURST, 1.7f, 1.5f);
            }
            if (getAnimationTick() == 10) {
                if (world.isRemote) {
                    spawnExplosionParticles(30);
                }
                this.playSound(MMSounds.ENTITY_BARAKO_ATTACK, 1.7f, 0.9f);
            }
            if (getAnimationTick() <= 6 && world.isRemote) {
                int particleCount = 8;
                while (--particleCount != 0) {
                    double radius = 2f;
                    double yaw = rand.nextFloat() * 2 * Math.PI;
                    double pitch = rand.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    double offsetX = -0.3 * Math.sin(rotationYaw * Math.PI / 180);
                    double offsetZ = -0.3 * Math.cos(rotationYaw * Math.PI / 180);
                    double offsetY = 1;
                    MMParticle.ORB.spawn(world, posX + ox + offsetX, posY + offsetY + oy, posZ + oz + offsetZ, ParticleArgs.get().withData(posX + offsetX, posY + offsetY, posZ + offsetZ, 6));
                }
            }
        }
        if (!world.isRemote && getAttackTarget() == null && getAnimation() != SOLAR_BEAM_ANIMATION) {
            heal(0.2f);
        }
        if (timeUntilSunstrike > 0) {
            timeUntilSunstrike--;
        }
        if (timeUntilLaser > 0) {
            timeUntilLaser--;
        }
        if (timeUntilBarakoa > 0) {
            timeUntilBarakoa--;
        }
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    private boolean checkBlocksByFeet() {
        IBlockState blockLeft;
        IBlockState blockRight;
        if (direction == 1) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
        } else if (direction == 2) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
        } else if (direction == 3) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) - 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
        } else if (direction == 4) {
            blockLeft = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) - 1));
            blockRight = world.getBlockState(new BlockPos(MathHelper.floor(posX) + 1, Math.round((float) (posY - 1)), MathHelper.floor(posZ) + 1));
        } else {
            return false;
        }
//        System.out.println(direction + ", " + (MathHelper.floor(posX) - 1) + ", " + Math.round((float) (posY - 1)) + ", " + MathHelper.floor(posZ) + 1);
        return !(blockLeft.getBlock() instanceof BlockAir && blockRight.getBlock() instanceof BlockAir);
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.25F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = rand.nextFloat() * 0.1F - 0.05f;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
            world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 1, posZ, vx, vy, vz);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(DIRECTION, 0);
        getDataManager().register(DIALOGUE, 0);
        getDataManager().register(ANGRY, false);
        getDataManager().register(DESIRES, Optional.of(new ItemStack(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 10)));
    }

    public int getDirection() {
        return getDataManager().get(DIRECTION);
    }

    public void setDirection(int direction) {
        getDataManager().set(DIRECTION, direction);
    }

    public int getWhichDialogue() {
        return getDataManager().get(DIALOGUE);
    }

    public void setWhichDialogue(int dialogue) {
        getDataManager().set(DIALOGUE, dialogue);
    }

    public boolean getAngry() {
        return getDataManager().get(ANGRY);
    }

    public void setAngry(boolean angry) {
        getDataManager().set(ANGRY, angry);
    }

    public void setDesires(ItemStack stack) {
    	getDataManager().set(DESIRES, Optional.fromNullable(stack));
    }

    public ItemStack getDesires() {
    	return getDataManager().get(DESIRES).get();
    }

    public boolean doesItemSatisfyDesire(ItemStack stack) {
        return canPayFor(stack, getDesires());
    }

    public boolean fulfillDesire(Slot input) {
        ItemStack desires = getDesires();
        if (canPayFor(input.getStack(), desires)) {
            input.decrStackSize(desires.stackSize);
            return true;
        }
        return false;
    }

    public boolean hasTradedWith(EntityPlayer player) {
        return tradedPlayers.contains(EntityPlayer.getUUID(player.getGameProfile()));
    }

    public void rememberTrade(EntityPlayer player) {
        tradedPlayers.add(EntityPlayer.getUUID(player.getGameProfile()));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("direction", getDirection());
        NBTTagList players = new NBTTagList();
        for (UUID uuid : tradedPlayers) {
            players.appendTag(NBTUtil.createUUIDTag(uuid));
        }
        compound.setTag("players", players);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setDirection(compound.getInteger("direction"));
        tradedPlayers.clear();
        NBTTagList players = compound.getTagList("players", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < players.tagCount(); i++) {
            tradedPlayers.add(NBTUtil.getUUIDFromTag(players.getCompoundTagAt(i)));
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {}

    private int getTimeUntilSunstrike() {
        int damageTaken = (int) (MAX_HEALTH - getHealth());
        if (damageTaken > 60) {
            damageTaken = 60;
        }
        return (int) (SUNSTRIKE_PAUSE_MAX - (damageTaken / 60f) * (SUNSTRIKE_PAUSE_MAX - SUNSTRIKE_PAUSE_MIN));
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, BELLY_ANIMATION, TALK_ANIMATION, SUNSTRIKE_ANIMATION, ATTACK_ANIMATION, SPAWN_ANIMATION, SOLAR_BEAM_ANIMATION, BLESS_ANIMATION};
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        List<EntityBarakoa> barakoa = getEntitiesNearby(EntityBarakoa.class, 20, 10, 20, 20);
        for (EntityBarakoa entityBarakoa : barakoa) {
            if (entityBarakoa.isBarakoDevoted()) entityBarakoa.timeUntilDeath = rand.nextInt(20);
        }
        if (!world.isRemote && world.getGameRules().getBoolean("doMobLoot")) {
            dropItem(ItemHandler.INSTANCE.barakoMask, 1);
        }
        super.onDeath(cause);
    }

    public boolean isTrading() {
        return customer != null;
    }

    public EntityPlayer getCustomer() {
        return customer;
    }

    public void setCustomer(EntityPlayer customer) {
        this.customer = customer;
    }

    @Override
    public Container createContainer(World world, EntityPlayer player, int x, int y, int z) {
        return new ContainerBarakoTrade(this, player.inventory, world);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer createGui(World world, EntityPlayer player, int x, int y, int z) {
        return new GuiBarakoTrade(this, player.inventory, world, y != 0);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (canTradeWith(player)) {
            setCustomer(player);
            if (!world.isRemote) {
                GuiHandler.open(GuiHandler.BARAKO_TRADE, player, this, hasTradedWith(player) ? 1 : 0);
            }
            return true;
        }
        return false;
    }

    public boolean canTradeWith(EntityPlayer player) {
        if (isTrading()) {
            return false;
        }
        ItemStack headStack = player.inventory.armorInventory[3];
        return headStack != null && headStack.getItem() instanceof BarakoaMask;
    }

    private static boolean canPayFor(ItemStack stack, ItemStack worth) {
        return worth == null && stack == null || stack != null && stack.getItem() == worth.getItem() && stack.stackSize >= worth.stackSize;
    }
}
