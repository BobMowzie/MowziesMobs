package com.bobmowzie.mowziesmobs.server.item;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntityEggInfo;

public class ItemSpawnEgg extends Item {
    public ItemSpawnEgg() {
        setUnlocalizedName("monsterPlacer");
        setRegistryName("spawn_egg");
        setHasSubtypes(true);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List subItems) {
        Iterator<MowzieEntityEggInfo> iterator = EntityHandler.INSTANCE.getEntityEggInfoIterator();
        while (iterator.hasNext()) {
            MowzieEntityEggInfo info = iterator.next();
            ItemStack stack = new ItemStack(item, 1);
            applyEntityIdToItemStack(stack, info.entityName);
            subItems.add(stack);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = I18n.format(getUnlocalizedName() + ".name").trim();
        String entityName = getEntityIdFromItem(stack);
        if (entityName != null) {
            name = name + " " + I18n.format("entity.mowziesmobs." + entityName + ".name");
        }
        return name;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState state = worldIn.getBlockState(pos);
            if (state.getBlock() == Blocks.MOB_SPAWNER) {
                TileEntity blockEntity = worldIn.getTileEntity(pos);
                if (blockEntity instanceof TileEntityMobSpawner) {
                    MobSpawnerBaseLogic spawner = ((TileEntityMobSpawner) blockEntity).getSpawnerBaseLogic();
                    spawner.setEntityName(getEntityIdFromItem(stack));
                    blockEntity.markDirty();
                    worldIn.notifyBlockUpdate(pos, state, state, 3);
                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.stackSize--;
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
            pos = pos.offset(facing);
            double d0 = 0.0D;
            if (facing == EnumFacing.UP && state.getBlock() instanceof BlockFence) {
                d0 = 0.5D;
            }
            Entity entity = spawnCreature(worldIn, getEntityIdFromItem(stack), pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D);
            if (entity != null) {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                    entity.setCustomNameTag(stack.getDisplayName());
                }
                applyItemEntityDataToEntity(worldIn, playerIn, stack, entity);
                if (!playerIn.capabilities.isCreativeMode) {
                    --stack.stackSize;
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else {
            RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, true);
            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
                } else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn)) {
                    Entity entity = spawnCreature(worldIn, getEntityIdFromItem(itemStackIn), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
                    if (entity == null) {
                        return new ActionResult(EnumActionResult.PASS, itemStackIn);
                    } else {
                        if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName()) {
                            entity.setCustomNameTag(itemStackIn.getDisplayName());
                        }
                        applyItemEntityDataToEntity(worldIn, playerIn, itemStackIn, entity);
                        if (!playerIn.capabilities.isCreativeMode) {
                            itemStackIn.stackSize--;
                        }
                        playerIn.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
                    }
                } else {
                    return new ActionResult(EnumActionResult.FAIL, itemStackIn);
                }
            } else {
                return new ActionResult(EnumActionResult.PASS, itemStackIn);
            }
        }
    }

    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity) {
        MinecraftServer server = entityWorld.getMinecraftServer();
        if (server != null && targetEntity != null) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null && compound.hasKey("EntityTag", 10)) {
                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !server.getPlayerList().canSendCommands(player.getGameProfile()))) {
                    return;
                }
                NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
                UUID uuid = targetEntity.getUniqueID();
                nbttagcompound1.merge(compound.getCompoundTag("EntityTag"));
                targetEntity.setUniqueId(uuid);
                targetEntity.readFromNBT(nbttagcompound1);
            }
        }
    }

    @Nullable
    public static Entity spawnCreature(World world, @Nullable String name, double x, double y, double z) {
        if (EntityHandler.INSTANCE.hasEntityEggInfo(name)) {
            Entity entity = EntityHandler.INSTANCE.createEntity(name, world);
            if (entity instanceof EntityLivingBase) {
                EntityLiving entityLiving = (EntityLiving) entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360), 0.0F);
                entityLiving.rotationYawHead = entityLiving.rotationYaw;
                entityLiving.renderYawOffset = entityLiving.rotationYaw;
                entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)), null);
                world.spawnEntityInWorld(entity);
                entityLiving.playLivingSound();
            }
            return entity;
        }
        return null;
    }

    @Nullable
    public static String getEntityIdFromItem(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null || !compound.hasKey("EntityTag", 10)) {
            return null;
        } else {
            NBTTagCompound nbttagcompound1 = compound.getCompoundTag("EntityTag");
            return !nbttagcompound1.hasKey("id", 8) ? null : nbttagcompound1.getString("id");
        }
    }

    public static void applyEntityIdToItemStack(ItemStack stack, String name) {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound entityCompound = new NBTTagCompound();
        entityCompound.setString("id", name);
        compound.setTag("EntityTag", entityCompound);
        stack.setTagCompound(compound);
    }
}
