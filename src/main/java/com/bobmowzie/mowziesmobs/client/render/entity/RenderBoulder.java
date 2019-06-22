package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBoulder;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.TreeMap;

@SideOnly(Side.CLIENT)
public class RenderBoulder extends Render<EntityBoulder> {
    private static final ResourceLocation TEXTURE_DIRT = new ResourceLocation("textures/blocks/dirt.png");
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/blocks/stone.png");
    private static final ResourceLocation TEXTURE_SANDSTONE = new ResourceLocation("textures/blocks/sandstone.png");
    private static final ResourceLocation TEXTURE_CLAY = new ResourceLocation("textures/blocks/clay.png");
    Map<String, ResourceLocation> texMap;

    ModelBoulder model;

    public RenderBoulder(RenderManager mgr) {
        super(mgr);
        model = new ModelBoulder();
        texMap = new TreeMap<String, ResourceLocation>();
        texMap.put(Blocks.STONE.getTranslationKey(), TEXTURE_STONE);
        texMap.put(Blocks.DIRT.getTranslationKey(), TEXTURE_DIRT);
        texMap.put(Blocks.CLAY.getTranslationKey(), TEXTURE_CLAY);
        texMap.put(Blocks.SANDSTONE.getTranslationKey(), TEXTURE_SANDSTONE);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBoulder entity) {
//        if (entity.storedBlock != null) {
//            return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(entity.storedBlock).;
//        }
//        else return TEXTURE_DIRT;
        if (entity.storedBlock != null) {
            ResourceLocation tex = texMap.get(entity.storedBlock.getBlock().getTranslationKey());
            if (tex != null) return tex;
        }
        return TEXTURE_DIRT;
    }

    @Override
    public void doRender(EntityBoulder entity, double x, double y, double z, float yaw, float delta) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        model.render(entity, 0.0625F, delta);
        GlStateManager.popMatrix();
    }
}
