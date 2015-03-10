package com.bobmowzie.mowziesmobs.proxy;

import com.bobmowzie.mowziesmobs.MMBlocks;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.renderer.entity.RenderFoliaath;
import com.bobmowzie.mowziesmobs.client.renderer.entity.RenderWroughtnaut;
import com.bobmowzie.mowziesmobs.client.renderer.item.ItemBabyFoliaathRenderer;
import com.bobmowzie.mowziesmobs.client.renderer.tile.RenderBabyFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.entity.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void entityRegistry()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new RenderFoliaath(new ModelFoliaath(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, new RenderWroughtnaut(new ModelWroughtnaut(), 1.0F));
    }

    @Override
    public void tileEntityRegistry()
    {
        GameRegistry.registerTileEntity(TileBabyFoliaath.class, "tileBabyFoliaath");
        ClientRegistry.bindTileEntitySpecialRenderer(TileBabyFoliaath.class, new RenderBabyFoliaath());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MMBlocks.blockBabyFoliaath), new ItemBabyFoliaathRenderer());
    }

    @Override
    public void itemRegistry()
    {

    }
}
