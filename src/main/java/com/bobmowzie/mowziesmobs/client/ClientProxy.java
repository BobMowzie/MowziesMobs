package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoayaTrade;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.client.sound.*;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemBlowgun;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {
    private Entity referencedMob = null;

    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);

        modbus.register(MMModels.class);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FrozenRenderHandler.INSTANCE);
    }

    @Override
    public void onLateInit(final IEventBus modbus) {
        for (EntityRenderer<?> entity : Minecraft.getInstance().getRenderManager().renderers.values()) {
            if (entity instanceof LivingRenderer) {
                ((LivingRenderer) entity).addLayer(new FrozenRenderHandler.LayerFrozen((LivingRenderer) entity));
            }
        }

        IItemPropertyGetter pulling = ItemModelsProperties.func_239417_a_(Items.BOW, new ResourceLocation("pulling"));
        ItemModelsProperties.registerProperty(ItemHandler.BLOWGUN.asItem(), new ResourceLocation("pulling"), pulling);
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getInstance().getSoundHandler().play(new SunstrikeSound(strike));
    }

    @Override
    public void playIceBreathSound(Entity entity) {
        Minecraft.getInstance().getSoundHandler().play(new IceBreathSound(entity));
    }

    @Override
    public void playBoulderChargeSound(PlayerEntity player) {
        Minecraft.getInstance().getSoundHandler().play(new SpawnBoulderChargeSound(player));
    }

    @Override
    public void playNagaSwoopSound(EntityNaga naga) {
        Minecraft.getInstance().getSoundHandler().play(new NagaSwoopSound(naga));
    }

    @Override
    public void playBlackPinkSound(AbstractMinecartEntity entity) {
        Minecraft.getInstance().getSoundHandler().play(new BlackPinkSound(entity));
    }

    @Override
    public void minecartParticles(ClientWorld world, AbstractMinecartEntity minecart, float scale, double x, double y, double z, BlockState state, BlockPos pos) {
        final int size = 3;
        float offset =  -0.5F * scale;
        for (int ix = 0; ix < size; ix++) {
            for (int iy = 0; iy < size; iy++) {
                for (int iz = 0; iz < size; iz++) {
                    double dx = (double) ix / size * scale;
                    double dy = (double) iy / size * scale;
                    double dz = (double) iz / size * scale;
                    Vector3d minecartMotion = minecart.getMotion();
                    Minecraft.getInstance().particles.addEffect(new DiggingParticle(
                            world,
                            x + dx + offset, y + dy + offset, z + dz + offset,
                            dx + minecartMotion.getX(), dy + minecartMotion.getY(), dz + minecartMotion.getZ(),
                            state
                    ) {}.setBlockPos(pos));
                }
            }
        }
    }

    @Override
    public void solarBeamHitWroughtnaught(LivingEntity caster) {
        if (caster == Minecraft.getInstance().player) {
            long now = System.currentTimeMillis();
            if (now - ClientEventHandler.INSTANCE.lastWroughtnautHitTime > 500) {
                ClientEventHandler.INSTANCE.startWroughtnautHitTime = now;
            }
            ClientEventHandler.INSTANCE.lastWroughtnautHitTime = now;
        }
    }

    public void setTPS(float tickRate) {

    }

    public Entity getReferencedMob() {
        return referencedMob;
    }

    public void setReferencedMob(Entity referencedMob) {
        this.referencedMob = referencedMob;
    }
}
