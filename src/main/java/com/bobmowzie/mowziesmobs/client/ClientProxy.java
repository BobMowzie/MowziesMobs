package com.bobmowzie.mowziesmobs.client;

import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.debug.ModelGrapher;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelTribeLeader;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelTribesman;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.model.extension.ModelPlayerExtension;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtAxe;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtHelm;
import com.bobmowzie.mowziesmobs.client.playeranimation.PlayerAnimationHandlerClient;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderFoliaath;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderTribeLeader;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderTribesman;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderWroughtnaut;
import com.bobmowzie.mowziesmobs.common.ServerProxy;
import com.bobmowzie.mowziesmobs.common.entity.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeElite;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeHunter;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeLeader;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeVillager;
import com.bobmowzie.mowziesmobs.common.entity.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.common.item.MMItems;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    private static final ModelWroughtHelm modelWroughtHelm = new ModelWroughtHelm();

    public void init()
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        if (MowziesMobs.isDebugging())
        {
            MinecraftForge.EVENT_BUS.register(ModelGrapher.INSTANCE);
            FMLCommonHandler.instance().bus().register(ModelGrapher.INSTANCE);
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, new RenderBabyFoliaath(new ModelBabyFoliaath(), 0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new RenderFoliaath(new ModelFoliaath(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, new RenderWroughtnaut(new ModelWroughtnaut(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeLeader.class, new RenderTribeLeader(new ModelTribeLeader(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeElite.class, new RenderTribesman(new ModelTribesman(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeHunter.class, new RenderTribesman(new ModelTribesman(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeVillager.class, new RenderTribesman(new ModelTribesman(), 0.6F));
        RenderHelper.registerItem3dRenderer(MMItems.itemWroughtAxe, new ModelWroughtAxe(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtAxe.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemWroughtHelm, new ModelWroughtHelm(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtHelm.png"));
        RenderHelper.registerModelExtension(new ModelPlayerExtension());
        PlayerAnimationHandlerClient playerAnimationHandlerClient = new PlayerAnimationHandlerClient();
        FMLCommonHandler.instance().bus().register(playerAnimationHandlerClient);
        MinecraftForge.EVENT_BUS.register(playerAnimationHandlerClient);
    }

    public ModelBiped getArmorModel()
    {
        return modelWroughtHelm;
    }
}
