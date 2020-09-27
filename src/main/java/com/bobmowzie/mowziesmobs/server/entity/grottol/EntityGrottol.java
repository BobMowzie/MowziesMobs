package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIGrottolFindMinecart;
import com.bobmowzie.mowziesmobs.server.ai.MMAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.MMEntityMoveHelper;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
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
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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

    public EntityGrottol(EntityType<? extends EntityGrottol> type, World world) {
        super(type, world);
        experienceValue = 20;
        stepHeight = 1.15F;

        moveController = new MMEntityMoveHelper(this, 45);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        setPathPriority(PathNodeType.DANGER_OTHER, 1);
        setPathPriority(PathNodeType.WATER, 3);
        setPathPriority(PathNodeType.LAVA, 1);
        setPathPriority(PathNodeType.DANGER_FIRE, 1);
        setPathPriority(PathNodeType.DANGER_CACTUS, 1);
        goalSelector.addGoal(3, new SwimGoal(this));
        goalSelector.addGoal(4, new RandomWalkingGoal(this, 0.3));
        goalSelector.addGoal(1, new EntityAIGrottolFindMinecart(this));
        goalSelector.addGoal(2, new MMAIAvoidEntity<EntityGrottol, PlayerEntity>(this, PlayerEntity.class, 16f, 0.5, 0.7) {
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
            public void tick() {
                super.tick();
                entity.fleeTime++;
            }

            @Override
            public void resetTask() {
                super.resetTask();
                entity.timeSinceFlee = 0;
                fleeCheckCounter = 0;
            }
        });
        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(1, new AnimationTakeDamage<>(this));
        goalSelector.addGoal(1, new AnimationDieAI<>(this));
        goalSelector.addGoal(5, new EntityAIGrottolIdle(this));
        goalSelector.addGoal(2, new SimpleAnimationAI<>(this, BURROW_ANIMATION, false));
    }

    @Override
    public int getMaxFallHeight() {
        return 256;
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return (float) pos.distanceSq(this.getPositionVec(), true);
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
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 * ConfigHandler.MOBS.GROTTOL.healthMultiplier);
        getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
    }

    @Override
    protected ConfigHandler.SpawnData getSpawnConfig() {
        return ConfigHandler.MOBS.GROTTOL.spawnData;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        return getEntitiesNearby(EntityGrottol.class, 20, 20, 20, 20).isEmpty() && super.canSpawn(world, reason);
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0) {
                if (!world.isRemote && isAlive()) {
                    entityDropItem(ItemHandler.CAPTURED_GROTTOL.create(this), 0.0F);
                    BlockState state = BlockHandler.GROTTOL.getDefaultState();
                    SoundType sound = state.getBlock().getSoundType(state, world, new BlockPos(this), entity);
                    world.playSound(
                        null,
                        posX, posY, posZ,
                        sound.getBreakSound(),
                        getSoundCategory(),
                        (sound.getVolume() + 1.0F) / 2.0F,
                        sound.getPitch() * 0.8F
                    );
                    /*if (world instanceof ServerWorld) {
                        ((ServerWorld) world).spawnParticle(
                            EnumParticleTypes.BLOCK_DUST,
                            posX, posY + height / 2.0D, posZ,
                            32,
                            width / 4.0F, height / 4.0F, width / 4.0F,
                            0.05D,
                            Block.getStateId(state)
                        );
                    }*/
                    remove();
                    if (player instanceof ServerPlayerEntity) AdvancementHandler.GROTTOL_KILL_SILK_TOUCH_TRIGGER.trigger((ServerPlayerEntity) player);
                }
                return true;
            }
        }
        return super.hitByEntity(entity);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.canHarvestBlock(Blocks.DIAMOND_ORE.getDefaultState())) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()) > 0) {
                    death = EnumDeathType.FORTUNE_PICKAXE;
                    if (player instanceof ServerPlayerEntity) AdvancementHandler.GROTTOL_KILL_FORTUNE_TRIGGER.trigger((ServerPlayerEntity) player);
                } else {
                    death = EnumDeathType.PICKAXE;
                }
                return super.attackEntityFrom(source, getHealth());
            } else {
                playSound(MMSounds.ENTITY_GROTTOL_UNDAMAGED, 0.4F, 2.0F);
                return false;
            }
        }
        else if (entity instanceof MobEntity) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            Entity e = getRidingEntity();
            if (isMinecart(e)) {
                reader.accept((AbstractMinecartEntity) e);
                setMoveForward(1);
                timeSinceMinecart++;
                boolean onRail = isBlockRail(world.getBlockState(e.getPosition()).getBlock());
                if ((timeSinceMinecart > 3 && e.getMotion().length() < 0.001) || !onRail) {
                    dismountEntity(e);
                    timeSinceMinecart = 0;
                }
                else if (onRail) {
                    e.setMotion(e.getMotion().x * 2.7, 0, e.getMotion().z * 2.7);
                }
            }
        }
//        if (ticksExisted == 1) System.out.println("Grottle at " + getPosition());

        //Sparkle particles
        if (world.isRemote && isAlive() && rand.nextInt(15) == 0) {
            double x = posX + 0.5f * (2 * rand.nextFloat() - 1f);
            double y = posY + 0.8f + 0.3f * (2 * rand.nextFloat() - 1f);
            double z = posZ + 0.5f * (2 * rand.nextFloat() - 1f);
            if (isBlackPinkInYourArea()) {
//                world.spawnParticle(EnumParticleTypes.NOTE, x, y, z, rand.nextDouble() / 2, 0, 0);
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
            BlockState blockBeneath = world.getBlockState(getPosition().down());
            Material mat = blockBeneath.getMaterial();
            if (mat == Material.EARTH || mat == Material.SAND || mat == Material.CLAY || mat == Material.ROCK) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, BURROW_ANIMATION);
            }
        }
        if (!world.isRemote && getAnimation() == BURROW_ANIMATION) {
            if (getAnimationTick() % 4 == 3) {
                playSound(MMSounds.ENTITY_GROTTOL_BURROW, 1, 0.8f + rand.nextFloat() * 0.4f);
                BlockState blockBeneath = world.getBlockState(getPosition().down());
                Material mat = blockBeneath.getMaterial();
                if (mat == Material.EARTH || mat == Material.SAND || mat == Material.CLAY || mat == Material.ROCK) {
                    Vec3d pos = new Vec3d(0.7D, 0.05D, 0.0D).rotateYaw((float) Math.toRadians(-renderYawOffset - 90));
                    /*((ServerWorld) world).spawnParticle(
                        EnumParticleTypes.BLOCK_DUST,
                        posX + pos.x, posY + pos.y, posZ + pos.z,
                        8,
                        0.25D, 0.025D, 0.25D,
                        0.1D,
                        Block.getStateId(blockBeneath)
                    );*/
                }
            }
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (animation == BURROW_ANIMATION) {
            remove();
        }
    }

    public static boolean isBlockRail(Block block) {
        return block == Blocks.RAIL || block == Blocks.ACTIVATOR_RAIL || block == Blocks.POWERED_RAIL || block == Blocks.DETECTOR_RAIL;
    }

    private boolean isBlackPinkInYourArea() {
        Entity e = getRidingEntity();
        if (isMinecart(e)) {
            BlockState state = ((AbstractMinecartEntity) e).getDisplayTile();
            return state.getBlock() == BlockHandler.GROTTOL && state.get(BlockGrottol.VARIANT) == BlockGrottol.Variant.BLACK_PINK;
        }
        return false;
    }

    public boolean isInMinecart() {
        return isMinecart(getRidingEntity());
    }

    public boolean hasMinecartBlockDisplay() {
        Entity entity = getRidingEntity();
        return isMinecart(entity) && ((AbstractMinecartEntity) entity).getDisplayTile().getBlock() == BlockHandler.GROTTOL;
    }

    private static boolean isMinecart(Entity entity) {
        return entity instanceof MinecartEntity;
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
                AbstractMinecartEntity minecart = (AbstractMinecartEntity) entity;
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
            ((AbstractMinecartEntity) entity).setHasDisplayTile(false);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableHandler.GROTTOL;
    }

    @Override
    protected SoundEvent getDeathSound() {
        playSound(MMSounds.ENTITY_GROTTOL_DIE, 1f, 1.3f);
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
