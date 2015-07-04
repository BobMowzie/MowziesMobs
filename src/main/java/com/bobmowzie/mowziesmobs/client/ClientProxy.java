package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtAxe;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtHelm;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderFoliaath;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderWroughtnaut;
import com.bobmowzie.mowziesmobs.common.ServerProxy;
import com.bobmowzie.mowziesmobs.common.entity.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.common.entity.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.common.item.MMItems;
import cpw.mods.fml.client.registry.RenderingRegistry;
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

    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, new RenderBabyFoliaath(new ModelBabyFoliaath(), 0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new RenderFoliaath(new ModelFoliaath(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, new RenderWroughtnaut(new ModelWroughtnaut(), 1.0F));
        RenderHelper.registerItem3dRenderer(MMItems.itemWroughtAxe, new ModelWroughtAxe(), new ResourceLocation(MowziesMobs.getModId() + "textures/items/modeled/textureWroughtAxe.png"));
        RenderHelper.registerItem3dRenderer(MMItems.itemWroughtHelm, new ModelWroughtHelm(), new ResourceLocation(MowziesMobs.getModId() + "textures/items/modeled/textureWroughtHelm.png"));
    }

    public ModelBiped getArmorModel()
    {
        return modelWroughtHelm;
    }
}
