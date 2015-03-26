package com.bobmowzie.mowziesmobs.proxy;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.renderer.entity.RenderBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.renderer.entity.RenderFoliaath;
import com.bobmowzie.mowziesmobs.client.renderer.entity.RenderWroughtnaut;
import com.bobmowzie.mowziesmobs.entity.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityWroughtnaut;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void entityRegistry()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, new RenderBabyFoliaath(new ModelBabyFoliaath(), 0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new RenderFoliaath(new ModelFoliaath(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, new RenderWroughtnaut(new ModelWroughtnaut(), 1.0F));
    }

    @Override
    public void tileEntityRegistry()
    {
    }

    @Override
    public void itemRegistry()
    {

    }

    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }
}
