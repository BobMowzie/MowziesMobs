package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class EntityBoulderBase extends EntityGeomancyBase implements IEntityAdditionalSpawnData {
    private static final byte ACTIVATE_ID = 67;

    public BlockState storedBlock;
    public float animationOffset = 0;
    public GeomancyTier boulderSize = GeomancyTier.SMALL;
    protected int finishedRisingTick = 4;
    public int risingTick = 0;
    public boolean active = false;

    public static final HashMap<GeomancyTier, EntityDimensions> SIZE_MAP = new HashMap<>();
    static {
        SIZE_MAP.put(GeomancyTier.NONE, EntityDimensions.scalable(1, 1));
        SIZE_MAP.put(GeomancyTier.SMALL, EntityDimensions.scalable(1, 1));
        SIZE_MAP.put(GeomancyTier.MEDIUM, EntityDimensions.scalable(2, 1.5f));
        SIZE_MAP.put(GeomancyTier.LARGE, EntityDimensions.scalable(3, 2.5f));
        SIZE_MAP.put(GeomancyTier.HUGE, EntityDimensions.scalable(4, 3.5f));
    }

    public EntityBoulderBase(EntityType<? extends EntityBoulderBase> type, Level world) {
        super(type, world);
        finishedRisingTick = 4;
        animationOffset = random.nextFloat() * 8;
        setTier(GeomancyTier.SMALL);
        setBoundingBox(makeBoundingBox());
    }

    public EntityBoulderBase(EntityType<? extends EntityBoulderBase> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos);
        finishedRisingTick = 4;
        animationOffset = random.nextFloat() * 8;
        setTier(tier);
        setSizeParams();
        setBoundingBox(makeBoundingBox());
    }

    @Override
    public boolean canBeCollidedWith() {
        return active;
    }

    public boolean checkCanSpawn() {
        if (!level().getEntitiesOfClass(EntityBoulderBase.class, getBoundingBox().deflate(0.01)).isEmpty()) return false;
        return level().noCollision(this, getBoundingBox().deflate(0.01));
    }

    public void setSizeParams() {
        GeomancyTier size = getTier();
        if (size == GeomancyTier.MEDIUM) {
            finishedRisingTick = 8;
        }
        else if (size == GeomancyTier.LARGE) {
            finishedRisingTick = 12;
        }
        else if (size == GeomancyTier.HUGE) {
            finishedRisingTick = 90;
        }
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        EntityDimensions dim = EntityBoulderBase.SIZE_MAP.get(getTier());
        return dim.makeBoundingBox(this.position());
    }

    public void activate() {
        active = true;
        level().broadcastEntityEvent(this, ACTIVATE_ID);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == ACTIVATE_ID) {
            active = true;
        }
    }

    public boolean isFinishedRising() {
        return risingTick >= finishedRisingTick;
    }

    @Override
    public void tick() {
        if (firstTick) {
            setSizeParams();
            boulderSize = getTier();
        }
        if (storedBlock == null) storedBlock = getBlock();

        super.tick();
        setBoundingBox(makeBoundingBox());
        move(MoverType.SELF, getDeltaMovement());

        if (boulderSize == GeomancyTier.HUGE && risingTick < finishedRisingTick) {
            float f = this.getBbWidth() / 2.0F;
            AABB aabb = new AABB(getX() - (double)f, getY() - 0.5, getZ() - (double)f, getX() + (double)f, getY() + Math.min(risingTick/(float)finishedRisingTick * 3.5f, 3.5f), getZ() + (double)f);
            setBoundingBox(aabb);
        }

        if (risingTick < finishedRisingTick + 2) {
            List<Entity> popUpEntities = level().getEntities(this, getBoundingBox());
            for (Entity entity:popUpEntities) {
                if (entity.isPickable() && !(entity instanceof EntityBoulderBase)) {
                    if (boulderSize != GeomancyTier.HUGE) entity.move(MoverType.SHULKER_BOX, new Vec3(0, 2 * (Math.pow(2, -risingTick * (0.6 - 0.1 * boulderSize.ordinal()))), 0));
                    else entity.move(MoverType.SHULKER_BOX, new Vec3(0, 0.6f, 0));
                }
            }
        }

        if (risingTick == 1) {
            for (int i = 0; i < 20 * getBbWidth(); i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 1.3 * getBbWidth(), 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, storedBlock), getX() + particlePos.x, getY() - 1, getZ() + particlePos.z, particlePos.x, 2, particlePos.z);
            }
            if (boulderSize == GeomancyTier.SMALL) {
                playSound(MMSounds.EFFECT_GEOMANCY_SMALL_CRASH.get(), 1.5f, 1.3f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 1f);
            } else if (boulderSize == GeomancyTier.MEDIUM) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_2.get(), 1.5f, 1.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL.get(), 1.5f, 0.8f);
            } else if (boulderSize == GeomancyTier.LARGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1.get(), 1.5f, 0.9f);
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 1.5f, 1.5f);
                EntityCameraShake.cameraShake(level(), position(), 10, 0.05f, 0, 20);
            } else if (boulderSize == GeomancyTier.HUGE) {
                playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG.get(), 2f, 0.5f);
                playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_1.get(), 2, 0.8f);
                EntityCameraShake.cameraShake(level(), position(), 15, 0.05f, 50, 30);
            }
            if (level().isClientSide) {
                AdvancedParticleBase.spawnParticle(level(), ParticleHandler.RING2.get(), getX(), getY() - 0.9f, getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * getBbWidth()), true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * getBbWidth()) * 10f), false)
                });
            }
        }
        if (risingTick == 30 && boulderSize == GeomancyTier.HUGE) {
            playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_2.get(), 2, 0.7f);
        }

        int dripTick = risingTick - 2;
        if (boulderSize == GeomancyTier.HUGE) dripTick -= 20;
        int dripNumber = (int)(getBbWidth() * 6 * Math.pow(1.03 + 0.04 * 1/getBbWidth(), -(dripTick)));
        if (dripNumber >= 1 && dripTick > 0) {
            dripNumber *= random.nextFloat();
            for (int i = 0; i < dripNumber; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 0.6 * getBbWidth(), 0, 0);
                particlePos = particlePos.yRot((float)(random.nextFloat() * 2 * Math.PI));
                float offsetY;
                if (boulderSize == GeomancyTier.HUGE && risingTick < finishedRisingTick) offsetY = random.nextFloat() * (getBbHeight()-1) - getBbHeight() * (finishedRisingTick - risingTick)/finishedRisingTick;
                else offsetY = random.nextFloat() * (getBbHeight()-1);
                level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, storedBlock), getX() + particlePos.x, getY() + offsetY, getZ() + particlePos.z, 0, -1, 0);
            }
        }
        if (active) {
            risingTick++;
        }
    }

    @Override
    protected void explode() {
        if (active) super.explode();
        else discard();
    }

    @Override
    public void remove(RemovalReason p_146834_) {
        super.remove(p_146834_);
        if (caster instanceof EntitySculptor sculptor) {
            sculptor.boulders.remove(this);
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(risingTick);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        risingTick = buffer.readInt();
    }
}
