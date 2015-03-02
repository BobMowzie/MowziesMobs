package com.bobmowzie.mowziesmobs.proxy;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.client.renderer.entity.RenderFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.tile.TileTest;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void entityRegistry()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new RenderFoliaath(new ModelFoliaath(), 1.0F));
   //     RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, new RenderBabyFoliaath(new ModelBabyFoliaath(), 0F));
    }

    @Override
    public void tileEntityRegistry()
    {
        GameRegistry.registerTileEntity(TileTest.class, "TileTest");
    }

    @Override
    public void itemRegistry()
    {

    }
}
