package com.bobmowzie.mowziesmobs.server.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public final class GuiHandler implements IGuiHandler {
    private static final SimpleRegistry<ResourceLocation, GuiType> GUIS = new SimpleRegistry<>();

    public static final GuiTypeEntity<EntityBarakoaya> BARAKOA_TRADE = new GuiTypeEntity(EntityBarakoaya.class);
    public static final GuiTypeEntity<EntityBarako> BARAKO_TRADE = new GuiTypeEntity(EntityBarako.class);

    static {
        GUIS.register(0, new ResourceLocation(MowziesMobs.MODID, "barakoa"), BARAKOA_TRADE);
        GUIS.register(1, new ResourceLocation(MowziesMobs.MODID, "barako"), BARAKO_TRADE);
    }

    @Override
    public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        GuiType type = GUIS.getObjectById(id);
        if (type != null) {
            return type.createContainer(world, player, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        GuiType type = GUIS.getObjectById(id);
        if (type != null) {
            return type.createGui(world, player, x, y, z);
        }
        return null;
    }

    public interface ContainerHolder {
        Container createContainer(World world, PlayerEntity player, int x, int y, int z);

        ContainerScreen createGui(World world, PlayerEntity player, int x, int y, int z);
    }

    public static abstract class GuiType<T extends ContainerHolder> {
        private final Class<T> clazz;

        protected GuiType(Class<T> clazz) {
            this.clazz = clazz;
        }

        public final Container createContainer(World world, PlayerEntity player, int x, int y, int z) {
            T obj = getAsType(world, player, x, y, z);
            if (obj != null) {
                return obj.createContainer(world, player, x, y, z);
            }
            return null;
        }

        public final ContainerScreen createGui(World world, PlayerEntity player, int x, int y, int z) {
            T obj = getAsType(world, player, x, y, z);
            if (obj != null) {
                return obj.createGui(world, player, x, y, z);
            }
            return null;
        }

        private T getAsType(World world, PlayerEntity player, int x, int y, int z) {
            Object obj = get(world, player, x, y, z);
            if (obj != null && clazz.isAssignableFrom(obj.getClass())) {
                return (T) obj;
            }
            return null;
        }

        protected abstract Object get(World world, PlayerEntity player, int x, int y, int z);
    }

    public static class GuiTypeBlock<T extends TileEntity & ContainerHolder> extends GuiType<T> {
        public GuiTypeBlock(Class<T> blockEntityClass) {
            super(blockEntityClass);
        }

        @Override
        protected TileEntity get(World world, PlayerEntity player, int x, int y, int z) {
            return world.getTileEntity(new BlockPos(x, y, z));
        }
    }

    public static class GuiTypeEntity<T extends Entity & ContainerHolder> extends GuiType<T> {
        public GuiTypeEntity(Class<T> entityClass) {
            super(entityClass);
        }

        @Override
        protected Entity get(World world, PlayerEntity player, int x, int y, int z) {
            return world.getEntityByID(x);
        }
    }

    public static <T extends Entity & ContainerHolder> void open(GuiTypeEntity<T> type, PlayerEntity player, T entity) {
        open(type, player, entity.getEntityId(), 0, 0);
    }

    public static <T extends Entity & ContainerHolder> void open(GuiTypeEntity<T> type, PlayerEntity player, T entity, int y) {
        open(type, player, entity.getEntityId(), y, 0);
    }

    public static <T extends Entity & ContainerHolder> void open(GuiTypeEntity<T> type, PlayerEntity player, T entity, int y, int z) {
        open(type, player, entity.getEntityId(), y, z);
    }

    public static <T extends TileEntity & ContainerHolder> void open(GuiHandler.GuiTypeBlock<T> type, PlayerEntity player, T blockEntity) {
        BlockPos pos = blockEntity.getPos();
        open(type, player, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void open(GuiHandler.GuiType type, PlayerEntity player, int x, int y, int z) {
        player.openGui(MowziesMobs.instance(), GUIS.getIDForObject(type), player.world, x, y, z);
    }
}
