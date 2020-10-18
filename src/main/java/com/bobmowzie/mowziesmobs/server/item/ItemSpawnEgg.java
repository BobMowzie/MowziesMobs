package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntityEggInfo;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.UUID;

public class ItemSpawnEgg extends Item {
    public ItemSpawnEgg(Item.Properties properties) {
        super(properties);
    }

    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subItems) {
        if (this.getCreativeTabs().contains(tab)) {
            Iterator<MowzieEntityEggInfo> iterator = EntityHandler.INSTANCE.getEntityEggInfoIterator();
            while (iterator.hasNext()) {
                MowzieEntityEggInfo info = iterator.next();
                ItemStack stack = new ItemStack(this);
                applyEntityIdToItemStack(stack, info.id);
                subItems.add(stack);
            }
        }
    }

//    @Override
//    public ITextComponent getDisplayName(ItemStack stack) {
//        String name = I18n.format(getTranslationKey() + ".name").trim();
//        ResourceLocation entityName = getEntityIdFromItem(stack);
//        if (entityName != null) {
//            name = name + " " + I18n.format("entity." + entityName.getPath() + ".name");
//        }
//        return name;
//    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity playerIn = context.getPlayer();
        Hand hand = context.getHand();
        Direction facing = context.getFace();
        ItemStack stack = playerIn.getHeldItem(hand);
        BlockPos pos = context.getPos();
        World worldIn = context.getWorld();
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return ActionResultType.FAIL;
        } else {
            BlockState state = worldIn.getBlockState(pos);
            if (state.getBlock() == Blocks.SPAWNER) {
                TileEntity blockEntity = worldIn.getTileEntity(pos);
                if (blockEntity instanceof MobSpawnerTileEntity) {
                    AbstractSpawner spawner = ((MobSpawnerTileEntity) blockEntity).getSpawnerBaseLogic();
                    spawner.setEntityType(getEntityIdFromItem(stack));
                    blockEntity.markDirty();
                    worldIn.notifyBlockUpdate(pos, state, state, 3);
                    if (!playerIn.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
            pos = pos.offset(facing);
            double d0 = 0.0D;
            if (facing == Direction.UP && state.getBlock() instanceof FenceBlock) {
                d0 = 0.5D;
            }
            Entity entity = spawnCreature(worldIn, getEntityIdFromItem(stack), pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D);
            if (entity != null) {
                if (entity instanceof LivingEntity && stack.hasDisplayName()) {
                    entity.setCustomName(stack.getDisplayName());
                }
                applyItemEntityDataToEntity(worldIn, playerIn, stack, entity);
                if (!playerIn.abilities.isCreativeMode) {
                    stack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (worldIn.isRemote) {
            return new ActionResult<>(ActionResultType.PASS, itemStackIn);
        } else {
            RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, true);
            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult hitInfo = (BlockRayTraceResult)raytraceresult.hitInfo;
                BlockPos blockpos = hitInfo.getPos();
                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
                    return new ActionResult<>(ActionResultType.PASS, itemStackIn);
                } else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, hitInfo.getFace(), itemStackIn)) {
                    Entity entity = spawnCreature(worldIn, getEntityIdFromItem(itemStackIn), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
                    if (entity == null) {
                        return new ActionResult<>(ActionResultType.PASS, itemStackIn);
                    } else {
                        if (entity instanceof LivingEntity && itemStackIn.hasDisplayName()) {
                            entity.setCustomName(itemStackIn.getDisplayName());
                        }
                        applyItemEntityDataToEntity(worldIn, playerIn, itemStackIn, entity);
                        if (!playerIn.abilities.isCreativeMode) {
                            itemStackIn.shrink(1);
                        }
                        playerIn.addStat(Stats.ITEM_USED.get(this));
                        return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
                    }
                } else {
                    return new ActionResult<>(ActionResultType.FAIL, itemStackIn);
                }
            } else {
                return new ActionResult<>(ActionResultType.PASS, itemStackIn);
            }
        }
    }

    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable PlayerEntity player, ItemStack stack, @Nullable Entity targetEntity) {
        MinecraftServer server = entityWorld.getServer();
        if (server != null && targetEntity != null) {
            CompoundNBT compound = stack.getTag();
            if (compound != null && compound.contains("EntityTag", 10)) {
                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !server.getPlayerList().canSendCommands(player.getGameProfile()))) {
                    return;
                }
                CompoundNBT nbttagcompound1 = targetEntity.writeWithoutTypeId(new CompoundNBT());
                UUID uuid = targetEntity.getUniqueID();
                nbttagcompound1.merge(compound.getCompound("EntityTag"));
                targetEntity.setUniqueId(uuid);
                targetEntity.read(nbttagcompound1);
            }
        }
    }

    @Nullable
    public static Entity spawnCreature(World world, @Nullable ResourceLocation name, double x, double y, double z) {
        if (EntityHandler.INSTANCE.hasEntityEggInfo(name)) {
            Entity entity = EntityHandler.INSTANCE.createEntity(name, world);
            if (entity instanceof LivingEntity) {
                MobEntity entityLiving = (MobEntity) entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360), 0.0F);
                entityLiving.rotationYawHead = entityLiving.rotationYaw;
                entityLiving.renderYawOffset = entityLiving.rotationYaw;
                entityLiving.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(entityLiving)), SpawnReason.SPAWN_EGG,null, null);
                world.addEntity(entity);
                entityLiving.playAmbientSound();
            }
            return entity;
        }
        return null;
    }

    @Nullable
    public static ResourceLocation getEntityIdFromItem(ItemStack stack) {
        CompoundNBT compound = stack.getTag();
        if (compound == null || !compound.contains("EntityTag", NBT.TAG_COMPOUND)) {
            return null;
        } else {
            CompoundNBT nbttagcompound1 = compound.getCompound("EntityTag");
            String id = nbttagcompound1.getString("id");
            ResourceLocation res = new ResourceLocation(id);
            return StringUtils.equals(id, res.toString()) ? res : null;
        }
    }

    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation id) {
        CompoundNBT compound = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        CompoundNBT entityCompound = new CompoundNBT();
        entityCompound.putString("id", id.toString());
        compound.put("EntityTag", entityCompound);
        stack.setTag(compound);
    }
}
