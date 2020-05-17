package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIGrottolFindMinecart;
import com.bobmowzie.mowziesmobs.server.ai.MMAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.MMEntityMoveHelper;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.grottol.ai.EntityAIGrottolIdle;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.sun.deploy.config.Config;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

/**
 * Created by Josh on 7/3/2018.
 */
public class EntityGrottol extends MowzieEntity implements IMob {
    public static final Animation DIE_ANIMATION = Animation.create(73);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation IDLE_ANIMATION = EntityAIGrottolIdle.animation();
    public static final Animation BURROW_ANIMATION = Animation.create(20);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            IDLE_ANIMATION,
            BURROW_ANIMATION
    };
    public int fleeTime = 0;
    private int timeSinceFlee = 50;
    private int timeSinceMinecart = 0;

    private final BlackPinkRailLine reader = BlackPinkRailLine.create();

    public enum EnumDeathType {
        NORMAL,
        PICKAXE,
        FORTUNE_PICKAXE
    }

    private EnumDeathType death = EnumDeathType.NORMAL;

    public EntityGrottol(World world) {
        super(world);
        setPathPriority(PathNodeType.DANGER_OTHER, 1);
        setPathPriority(PathNodeType.WATER, 3);
        setPathPriority(PathNodeType.LAVA, 1);
        setPathPriority(PathNodeType.DANGER_FIRE, 1);
        setPathPriority(PathNodeType.DANGER_CACTUS, 1);
        experienceValue = 20;
        stepHeight = 1.15F;
        setSize(0.9F, 1.2F);

        moveHelper = new MMEntityMoveHelper(this, 45);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(3, new EntityAISwimming(this));
        tasks.addTask(4, new EntityAIWander(this, 0.3));
        tasks.addTask(1, new EntityAIGrottolFindMinecart(this));
        tasks.addTask(2, new MMAIAvoidEntity<EntityGrottol, EntityPlayer>(this, EntityPlayer.class, 16f, 0.5, 0.7) {
            private int fleeCheckCounter = 0;

            @Override
            protected void onSafe() {
                fleeCheckCounter = 0;
            }

            @Override
            protected void onPathNotFound() {
                if (fleeCheckCounter < 4) {
                    fleeCheckCounter++;
                } else if (getAnimation() == NO_ANIMATION) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityGrottol.BURROW_ANIMATION);
                }
            }

            @Override
            public void updateTask() {
                super.updateTask();
                entity.fleeTime++;
            }

            @Override
            public void resetTask() {
                super.updateTask();
                entity.timeSinceFlee = 0;
                fleeCheckCounter = 0;
            }
        });
        tasks.addTask(8, new EntityAILookIdle(this));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(1, new AnimationTakeDamage<>(this));
        tasks.addTask(1, new AnimationDieAI<>(this));
        tasks.addTask(5, new EntityAIGrottolIdle(this));
        tasks.addTask(2, new AnimationAI<>(this, BURROW_ANIMATION, false));
    }

    @Override
    public int getMaxFallHeight() {
        return 256;
    }

    @Override
    protected PathNavigate createNavigator(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return (float) getDistanceSqToCenter(pos);
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && !isInMinecart();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 * ConfigHandler.GROTTOL.healthMultiplier);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
    }

    @Override
    public boolean getCanSpawnHere() {
        return posY <= 50 && !world.canSeeSky(getPosition()) && getEntitiesNearby(EntityGrottol.class, 20, 20, 20, 20).isEmpty() && super.getCanSpawnHere();
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0) {
                if (!world.isRemote && !isDead) {
                    entityDropItem(ItemHandler.CAPTURED_GROTTOL.create(this), 0.0F);
                    IBlockState state = BlockHandler.GROTTOL.getDefaultState();
                    SoundType sound = state.getBlock().getSoundType(state, world, new BlockPos(this), entity);
                    world.playSound(
                        null,
                        posX, posY, posZ,
                        sound.getBreakSound(),
                        getSoundCategory(),
                        (sound.getVolume() + 1.0F) / 2.0F,
                        sound.getPitch() * 0.8F
                    );
                    if (world instanceof WorldServer) {
                        ((WorldServer) world).spawnParticle(
                            EnumParticleTypes.BLOCK_DUST,
                            posX, posY + height / 2.0D, posZ,
                            32,
                            width / 4.0F, height / 4.0F, width / 4.0F,
                            0.05D,
                            Block.getStateId(state)
                        );
                    }
                    setDead();
                }
                return true;
            }
        }
        return super.hitByEntity(entity);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.canHarvestBlock(Blocks.DIAMOND_ORE.getDefaultState())) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()) > 0) {
                    death = EnumDeathType.FORTUNE_PICKAXE;
                } else {
                    death = EnumDeathType.PICKAXE;
                }
                return super.attackEntityFrom(source, getHealth());
            } else {
                playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.4F, 2.9F);
                return false;
            }
        }
        else if (entity instanceof EntityLiving) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            Entity e = getRidingEntity();
            if (isMinecart(e)) {
                reader.accept((EntityMinecart) e);
                setMoveForward(1);
                timeSinceMinecart++;
                boolean onRail = isBlockRail(world.getBlockState(e.getPosition()).getBlock());
                if ((timeSinceMinecart > 3 && Math.abs(e.motionX) <= 0.001 && Math.abs(e.motionY) <= 0.001 && Math.abs(e.motionZ) <= 0.001) || !onRail) {
                    dismountRidingEntity();
                    timeSinceMinecart = 0;
                }
                else if (onRail) {
                    e.motionX *= 2.7;
                    e.motionZ *= 2.7;
                }
            }
        }
//        if (ticksExisted == 1) System.out.println("Grottle at " + getPosition());

        //Sparkle particles
        if (world.isRemote && isEntityAlive() && rand.nextInt(15) == 0) {
            double x = posX + 0.5f * (2 * rand.nextFloat() - 1f);
            double y = posY + 0.8f + 0.3f * (2 * rand.nextFloat() - 1f);
            double z = posZ + 0.5f * (2 * rand.nextFloat() - 1f);
            if (isBlackPinkInYourArea()) {
                world.spawnParticle(EnumParticleTypes.NOTE, x, y, z, rand.nextDouble() / 2, 0, 0);
            } else {
                MMParticle.SPARKLE.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(0d, 0d, 0d, 1d, 1d, 1d, 4d, 22));   
            }
        }

        //Footstep Sounds
        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (frame % 6 == 0 && speed > 0.05) {
            playSound(MMSounds.ENTITY_GROTTOL_STEP, 1F, 1.8f);
        }

        if (timeSinceFlee < 50) {
            timeSinceFlee++;
        } else {
            fleeTime = 0;
        }

        // AI Task
        if (!world.isRemote && fleeTime >= 55 && getAnimation() == NO_ANIMATION && !isAIDisabled() && !isPotionActive(PotionHandler.FROZEN)) {
            IBlockState blockBeneath = world.getBlockState(getPosition().down());
            Material mat = blockBeneath.getMaterial();
            if (mat == Material.GRASS || mat == Material.GROUND || mat == Material.SAND || mat == Material.CLAY || mat == Material.ROCK) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, BURROW_ANIMATION);
            }
        }
        if (!world.isRemote && getAnimation() == BURROW_ANIMATION) {
            if (getAnimationTick() % 4 == 3) {
                playSound(SoundEvents.BLOCK_SAND_PLACE, 1, 0.8f + rand.nextFloat() * 0.4f);
                IBlockState blockBeneath = world.getBlockState(getPosition().down());
                Material mat = blockBeneath.getMaterial();
                if (mat == Material.GRASS || mat == Material.GROUND || mat == Material.SAND || mat == Material.CLAY || mat == Material.ROCK) {
                    Vec3d pos = new Vec3d(0.7D, 0.05D, 0.0D).rotateYaw((float) Math.toRadians(-renderYawOffset - 90));
                    ((WorldServer) world).spawnParticle(
                        EnumParticleTypes.BLOCK_DUST,
                        posX + pos.x, posY + pos.y, posZ + pos.z,
                        8,
                        0.25D, 0.025D, 0.25D,
                        0.1D,
                        Block.getStateId(blockBeneath)
                    );
                }
            }
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (animation == BURROW_ANIMATION) {
            setDead();
        }
    }

    public static boolean isBlockRail(Block block) {
        return block == Blocks.RAIL || block == Blocks.ACTIVATOR_RAIL || block == Blocks.GOLDEN_RAIL || block == Blocks.DETECTOR_RAIL;
    }

    private boolean isBlackPinkInYourArea() {
        Entity e = getRidingEntity();
        if (isMinecart(e)) {
            IBlockState state = ((EntityMinecart) e).getDisplayTile();
            return state.getBlock() == BlockHandler.GROTTOL && state.getValue(BlockGrottol.VARIANT) == BlockGrottol.Variant.BLACK_PINK;
        }
        return false;
    }

    public boolean isInMinecart() {
        return isMinecart(getRidingEntity());
    }

    public boolean hasMinecartBlockDisplay() {
        Entity entity = getRidingEntity();
        return isMinecart(entity) && ((EntityMinecart) entity).getDisplayTile().getBlock() == BlockHandler.GROTTOL;
    }

    private static boolean isMinecart(Entity entity) {
        return entity instanceof EntityMinecartEmpty;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
        if (!isMinecart(entity)) {
            super.collideWithEntity(entity);   
        }
    }

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        if (super.startRiding(entity, force)) {
            if (isMinecart(entity)) {
                EntityMinecart minecart = (EntityMinecart) entity;
                if (minecart.getDisplayTile().getBlock() != BlockHandler.GROTTOL) {
                    minecart.setDisplayTile(BlockHandler.GROTTOL.getDefaultState());
                    minecart.setDisplayTileOffset(minecart.getDefaultDisplayTileOffset());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void dismountEntity(Entity entity) {
        super.dismountEntity(entity);
        if (isMinecart(entity)) {
            ((EntityMinecart) entity).setHasDisplayTile(false);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.GROTTOL;
    }

    @Override
    protected SoundEvent getDeathSound() {
        playSound(MMSounds.EFFECT_GEOMANCY_BREAK, 1f, 1.3f);
        return null;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    public EnumDeathType getDeathType() {
        return death;
    }
}
