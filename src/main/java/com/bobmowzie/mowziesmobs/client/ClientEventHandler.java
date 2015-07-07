package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.common.item.MMItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.event.Render3dItemEvent;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
    @SubscribeEvent
    public void onItemRender(Render3dItemEvent.Pre event)
    {
        if (event.item == MMItems.itemWroughtAxe)
        {
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
            {
                GL11.glTranslatef(0f, -1.5f, 0f);
            }
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED)
            {
                GL11.glTranslatef(-0.1f, -1f, 0.1f);
            }
            if (event.type == IItemRenderer.ItemRenderType.INVENTORY)
            {
                GL11.glTranslatef(0.8f, -1.4f, 0f);
                GL11.glScalef(0.4f, 0.4f, 0.4f);
                GL11.glRotatef(40f, 1f, 0f, 0f);
            }
            if (event.type == IItemRenderer.ItemRenderType.ENTITY)
            {
                GL11.glTranslatef(0f, -0.5f, 0f);
                GL11.glScalef(0.4f, 0.4f, 0.4f);
            }
        }

        if (event.item == MMItems.itemWroughtHelm)
        {
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
            {
                GL11.glRotatef(90f, 0f, 1f, 0f);
                GL11.glTranslatef(-1f, -0.8f, 0f);
            }
            if (event.type == IItemRenderer.ItemRenderType.EQUIPPED)
            {
                GL11.glRotatef(-115f, 0f, 1f, 0f);
                GL11.glScalef(2f, 2f, 2f);
                GL11.glTranslatef(0f, -1.3f, -0.8f);
            }
            if (event.type == IItemRenderer.ItemRenderType.INVENTORY)
            {
                GL11.glTranslatef(-0.4f, -1.2f, 0f);
                GL11.glScalef(0.97f, 0.97f, 0.97f);
            }
            if (event.type == IItemRenderer.ItemRenderType.ENTITY)
            {
                GL11.glTranslatef(0f, -1.3f, 0f);
            }
        }
    }
}
