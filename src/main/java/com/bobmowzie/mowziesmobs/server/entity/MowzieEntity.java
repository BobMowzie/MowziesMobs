package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemSpawnEgg;
import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MowzieEntity extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity, IntermittentAnimatableEntity {
    private static final byte START_IA_HEALTH_UPDATE_ID = 4;

    public int frame;
    public float targetDistance;
    public float targetAngle;
    public AnimationAI currentAnim = null;
    public boolean active;
    public EntityLivingBase blockingEntity = null;
    private int animationTick;
    private Animation animation = NO_ANIMATION;
    private List<IntermittentAnimation> intermittentAnimations = new ArrayList<>();
    public Vec3d moveVec = new Vec3d(0, 0, 0);
    public boolean playsHurtAnimation = true;
    protected boolean dropAfterDeathAnim = true;

    public Vec3d[] socketPosArray = new Vec3d[]{};

    protected boolean prevOnGround;
    protected boolean prevPrevOnGround;
    protected boolean willLandSoon;
    
    private boolean killDataRecentlyHitFlag;
    private int killDataLootingLevel;
    private DamageSource killDataCause;
    private EntityPlayer killDataAttackingPlayer;

    public MowzieEntity(World world) {
        super(world);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        String id = getPickedEntityId();
        if (id == null) {
            return ItemStack.EMPTY;
        }
        ResourceLocation res = new ResourceLocation(id);
        if (EntityHandler.INSTANCE.hasEntityEggInfo(res)) {
            ItemStack stack = new ItemStack(ItemHandler.SPAWN_EGG);
            ItemSpawnEgg.applyEntityIdToItemStack(stack, res);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    protected String getPickedEntityId() {
        return getEntityString();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    protected ConfigHandler.SpawnData getSpawnConfig() {
        return null;
    }

    @Override
    public boolean getCanSpawnHere() {
        ConfigHandler.SpawnData spawnData = getSpawnConfig();
        if (spawnData != null) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.getEntityBoundingBox().minY);
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);

            // Dimension check
            List<Integer> dimensionIDs = Ints.asList(spawnData.dimensions);
            if (!dimensionIDs.contains(world.provider.getDimension())) {
                return false;
            }

            // Height check
            float heightMax = spawnData.heightMax;
            float heightMin = spawnData.heightMin;
            if (posY > heightMax && heightMax >= 0) {
                return false;
            }
            if (posY < heightMin) {
                return false;
            }

            // Light level check
            if (spawnData.needsDarkness && !isValidLightLevel()) {
                return false;
            }

            // Block check
            ResourceLocation blockName = world.getBlockState(pos.down()).getBlock().getRegistryName();
            List<String> allowedBlocks = Arrays.asList(spawnData.allowedBlocks);
            if (blockName == null) return false;
            if (!allowedBlocks.isEmpty() && !allowedBlocks.contains(blockName.getPath())) return false;

            // See sky
            if (spawnData.needsSeeSky && !world.canSeeSky(pos)) {
                return false;
            }
            if (spawnData.needsCantSeeSky && world.canSeeSky(pos)) {
                return false;
            }
        }

        return super.getCanSpawnHere();
    }

    protected boolean isValidLightLevel()
    {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering())
            {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    @Override
    public void onUpdate() {
        prevPrevOnGround = prevOnGround;
        prevOnGround = onGround;
        super.onUpdate();
        frame++;
        if (getAnimation() != NO_ANIMATION) {
            animationTick++;
            if (world.isRemote && animationTick >= animation.getDuration()) {
                setAnimation(NO_ANIMATION);
            }
        }
        if (getAttackTarget() != null) {
            targetDistance = getDistance(getAttackTarget());
            targetAngle = (float) getAngleBetweenEntities(this, getAttackTarget());
        }
        willLandSoon = !onGround && world.collidesWithAnyBlock(getEntityBoundingBox().offset(new Vec3d(motionX, motionY, motionZ)));
    }

    protected void onAnimationFinish(Animation animation) {}

    @Override
    public void writeSpawnData(ByteBuf buf) {
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        prevRotationYaw = rotationYaw;
        prevRenderYawOffset = renderYawOffset = prevRotationYawHead = rotationYawHead;
    }

    public int getAttack() {
        return 0;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public boolean isNotColliding() {
        return !this.world.containsAnyLiquid(this.getEntityBoundingBox()) && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        System.out.println("Spawned " + getName() + " at " + getPosition());
        System.out.println("Block " + world.getBlockState(getPosition().down()).toString());
        return super.onInitialSpawn(difficulty, livingdata);
    }

    public boolean attackEntityAsMob(Entity entityIn, float damageMultiplier) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * damageMultiplier;
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
    }

    public List<EntityPlayer> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(distanceX, distanceY, distanceZ));
        List<EntityPlayer> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityPlayer && getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (EntityPlayer) entityNeighbor).collect(Collectors.toList());
        return listEntityPlayers;
    }

    public List<EntityLivingBase> getAttackableEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(distanceX, distanceY, distanceZ));
        List<EntityLivingBase> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityLivingBase && ((EntityLivingBase)entityNeighbor).attackable() && (!(entityNeighbor instanceof EntityPlayer) || !((EntityPlayer)entityNeighbor).isCreative()) && getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (EntityLivingBase) entityNeighbor).collect(Collectors.toList());
        return listEntityLivingBase;
    }

    public  List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(EntityLivingBase.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r && e.posY <= posY + dY);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (getHealth() <= 0.0F) {
            Animation death;
            if ((death = getDeathAnimation()) != null) {
                onDeathUpdate(death.getDuration() - 20);   
            } else {
                onDeathUpdate(20);
            }
        }
    }

    private void onDeathUpdate(int deathDuration) {
        onDeathAIUpdate();
        if (++deathTime >= deathDuration) {
            boolean isPlayerKill = recentlyHit > 0;
            if (!world.isRemote && isPlayerKill && canDropLoot() && world.getGameRules().getBoolean("doMobLoot")) {
                for (int remaining = getExperiencePoints(attackingPlayer), value; remaining > 0; remaining -= value) {
                    world.spawnEntity(new EntityXPOrb(world, posX, posY, posZ, value = EntityXPOrb.getXPSplit(remaining)));
                }
            }

            if (!world.isRemote && dropAfterDeathAnim && world.getGameRules().getBoolean("doMobLoot")) {
                attackingPlayer = killDataAttackingPlayer;
                dropLoot(killDataRecentlyHitFlag, killDataLootingLevel, killDataCause);
            }

            setDead();
            for (int n = 0; n < 20; n++) {
                double d2 = rand.nextGaussian() * 0.02D;
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double) (rand.nextFloat() * width * 2.0F) - (double) width, posY + (double) (rand.nextFloat() * height), posZ + (double) (rand.nextFloat() * width * 2.0F) - (double) width, d2, d0, d1);
            }
        }
    }

    protected void onDeathAIUpdate() {}

    @Override
    protected final void onDeathUpdate() {}

    @Override
    public void onDeath(DamageSource cause)
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.dead)
        {
            Entity entity = cause.getTrueSource();
            EntityLivingBase entitylivingbase = this.getAttackingEntity();

            if (this.scoreValue >= 0 && entitylivingbase != null)
            {
                entitylivingbase.awardKillScore(this, this.scoreValue, cause);
            }

            if (entity != null)
            {
                entity.onKillEntity(this);
            }

            this.dead = true;
            this.getCombatTracker().reset();

            if (!this.world.isRemote)
            {
                int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(this, entity, cause);

                captureDrops = true;
                capturedDrops.clear();

                if (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))
                {
                    boolean flag = this.recentlyHit > 0;
                    if (dropAfterDeathAnim) {
                        killDataRecentlyHitFlag = flag;
                        killDataLootingLevel = i;
                        killDataCause = cause;
                        killDataAttackingPlayer = this.attackingPlayer;
                    }
                    else {
                        this.dropLoot(flag, i, cause);
                    }
                }

                captureDrops = false;

                if (!net.minecraftforge.common.ForgeHooks.onLivingDrops(this, cause, capturedDrops, i, recentlyHit > 0))
                {
                    for (EntityItem item : capturedDrops)
                    {
                        world.spawnEntity(item);
                    }
                }
            }

            this.world.setEntityState(this, (byte)3);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        boolean attack = super.attackEntityFrom(source, damage);
        if (attack) {
            if (getHealth() > 0.0F && getAnimation() == NO_ANIMATION && playsHurtAnimation) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getHurtAnimation());
            } else if (getHealth() <= 0.0F) {
                if (currentAnim != null) {
                    currentAnim.resetTask();
                }
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getDeathAnimation());
            }
        }
        return attack;
    }

    protected void addIntermittentAnimation(IntermittentAnimation animation) {
        animation.setID((byte) intermittentAnimations.size());
        intermittentAnimations.add(animation);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id >= START_IA_HEALTH_UPDATE_ID && id - START_IA_HEALTH_UPDATE_ID < intermittentAnimations.size()) {
            intermittentAnimations.get(id - START_IA_HEALTH_UPDATE_ID).start();
            return;
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public byte getOffsetEntityState() {
        return START_IA_HEALTH_UPDATE_ID;
    }

    public void circleEntity(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset, float moveSpeedMultiplier) {
        int directionInt = direction ? 1 : -1;
        this.getNavigator().tryMoveToXYZ(target.posX + radius * Math.cos(directionInt * circleFrame * 0.5 * speed / radius + offset), target.posY, target.posZ + radius * Math.sin(directionInt * circleFrame * 0.5 * speed / radius + offset), speed * moveSpeedMultiplier);
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<EntityLivingBase> nearbyEntities = getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            entity.motionX = -0.1 * Math.cos(angle);
            entity.motionZ = -0.1 * Math.sin(angle);
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (animation == NO_ANIMATION) {
            onAnimationFinish(this.animation);
        }
        this.animation = animation;
        setAnimationTick(0);
    }

    public abstract Animation getDeathAnimation();

    public abstract Animation getHurtAnimation();

}
