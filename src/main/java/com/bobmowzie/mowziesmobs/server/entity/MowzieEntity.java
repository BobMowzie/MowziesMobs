package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemSpawnEgg;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.ArrayList;
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
	public Vec3d moveVec = new Vec3d(0, 0, 0);
	public boolean playsHurtAnimation = true;
	public Vec3d[] socketPosArray = new Vec3d[]{};
	protected boolean prevOnGround;
	protected boolean prevPrevOnGround;
	protected boolean willLandSoon;
	private int animationTick;
	private Animation animation = NO_ANIMATION;
	private List<IntermittentAnimation> intermittentAnimations = new ArrayList<>();

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
			ItemStack stack = new ItemStack(ItemHandler.INSTANCE.spawnEgg);
			ItemSpawnEgg.applyEntityIdToItemStack(stack, res);
			return stack;
		}
		return ItemStack.EMPTY;
	}

	protected String getPickedEntityId() {
		return getEntityString();
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
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

	protected void onAnimationFinish(Animation animation) {
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
	}

	public int getAttack() {
		return 0;
	}

	public double getAngleBetweenEntities(Entity first, Entity second) {
		return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
	}

	public List<EntityPlayer> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
		List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(distanceX, distanceY, distanceZ));
		List<EntityPlayer> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityPlayer && getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (EntityPlayer) entityNeighbor).collect(Collectors.toList());
		return listEntityPlayers;
	}

	public List<EntityLivingBase> getAttackableEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
		List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(distanceX, distanceY, distanceZ));
		List<EntityLivingBase> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityLivingBase && ((EntityLivingBase) entityNeighbor).attackable() && (!(entityNeighbor instanceof EntityPlayer) || !((EntityPlayer) entityNeighbor).isCreative()) && getDistance(entityNeighbor) <= radius).map(entityNeighbor -> (EntityLivingBase) entityNeighbor).collect(Collectors.toList());
		return listEntityLivingBase;
	}

	public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
		return getEntitiesNearby(EntityLivingBase.class, distanceX, distanceY, distanceZ, radius);
	}

	public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
		return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().expand(r, r, r), e -> e != this && getDistance(e) <= r);
	}

	public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
		return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().expand(dX, dY, dZ), e -> e != this && getDistance(e) <= r && e.posY <= posY + dY);
	}

	@Override
	protected void onDeathUpdate() {
		++deathTime;

		if (deathTime == getDeathAnimation().getDuration() - 20) {
			int experience;

			if (!world.isRemote && (recentlyHit > 0 || isPlayer()) && canDropLoot() && world.getGameRules().getBoolean("doMobLoot")) {
				experience = getExperiencePoints(attackingPlayer);

				while (experience > 0) {
					int j = EntityXPOrb.getXPSplit(experience);
					experience -= j;
					world.spawnEntity(new EntityXPOrb(world, posX, posY, posZ, j));
				}
			}
			if (!world.isRemote && world.getGameRules().getBoolean("doMobLoot")) {
				dropLoot();
			}
			this.setDead();

			for (experience = 0; experience < 20; ++experience) {
				double d2 = rand.nextGaussian() * 0.02D;
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double) (rand.nextFloat() * width * 2.0F) - (double) width, posY + (double) (rand.nextFloat() * height), posZ + (double) (rand.nextFloat() * width * 2.0F) - (double) width, d2, d0, d1);
			}
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

	protected void dropLoot() {

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
