package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.debug.ModelGrapher;
import com.bobmowzie.mowziesmobs.client.model.entity.*;
import com.bobmowzie.mowziesmobs.client.model.extension.ModelPlayerExtension;
import com.bobmowzie.mowziesmobs.client.model.item.ModelBarakoaMask;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtAxe;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtHelm;
import com.bobmowzie.mowziesmobs.client.playeranimation.PlayerAnimationHandlerClient;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.common.ServerProxy;
import com.bobmowzie.mowziesmobs.common.entity.*;
import com.bobmowzie.mowziesmobs.common.item.MMItems;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    private static final ModelWroughtHelm modelWroughtHelm = new ModelWroughtHelm();
    private static final ModelBarakoaMask modelBarakoaMask = new ModelBarakoaMask();

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
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new RenderDart());

        RenderHelper.registerItem3dRenderer(MMItems.itemWroughtAxe, new ModelWroughtAxe(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtAxe.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemWroughtHelm, new ModelWroughtHelm(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtHelm.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemBarakoaMask1, new ModelBarakoaMask(), new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman1.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemBarakoaMask2, new ModelBarakoaMask(), new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman2.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemBarakoaMask3, new ModelBarakoaMask(), new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman3.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemBarakoaMask4, new ModelBarakoaMask(), new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman4.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemBarakoaMask5, new ModelBarakoaMask(), new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman5.png"));
        RenderHelper.registerModelExtension(new ModelPlayerExtension());
        PlayerAnimationHandlerClient playerAnimationHandlerClient = new PlayerAnimationHandlerClient();
        FMLCommonHandler.instance().bus().register(playerAnimationHandlerClient);
        MinecraftForge.EVENT_BUS.register(playerAnimationHandlerClient);
    }

    public ModelBiped getArmorModel(int i)
    {
        if (i == 0) return modelWroughtHelm;
        if (i == 1) return modelBarakoaMask;
        else return modelWroughtHelm;
    }
}
