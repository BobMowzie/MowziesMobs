package thehippomaster.AnimationAPI.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.IModelCustom;

/**
 * ModelObjRenderer.java
 * A simple ModelRenderer-compatible class that allows the modder to link custom models to ModelRenderer models. Very useful
 * if the modder wants to replace a previous ModelRenderer with a custom model piece without having to redo all of the
 * translations, scales, and rotations. However, this model renders the methods addBox, setTextureOffset, etc useless, as it
 * only applies the translations and rotations originally in the ModelRenderer.
 * 
 * @author thehippomaster21
 */
@SideOnly(Side.CLIENT)
public class ModelObjRenderer extends ModelRenderer {
	
	public ModelObjRenderer(ModelBase bass) {
		this(bass, null, 1F);
	}
	
	public ModelObjRenderer(ModelBase bass, IModelCustom shape) {
		this(bass, shape, 1F);
	}
	
	public ModelObjRenderer(ModelBase bass, IModelCustom shape, float scale) {
		super(bass);
		theScale = scale;
		model = shape;
	}
	
	public void setScale(float scale) {
		theScale = scale;
	}
	
	@Override
	public void render(float scale) {
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.compiled)
				{
					this.compileDisplayList(scale);
				}

				GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
				int i;

				if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
				{
					if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
					{
						GL11.glPushMatrix();
						GL11.glScalef(theScale, theScale, theScale);
						GL11.glCallList(displayList);
						GL11.glPopMatrix();

						if (this.childModels != null)
						{
							for (i = 0; i < this.childModels.size(); ++i)
							{
								((ModelRenderer)this.childModels.get(i)).render(scale);
							}
						}
					}
					else
					{
						GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
						GL11.glPushMatrix();
						GL11.glScalef(theScale, theScale, theScale);
						GL11.glCallList(displayList);
						GL11.glPopMatrix();

						if (this.childModels != null)
						{
							for (i = 0; i < this.childModels.size(); ++i)
							{
								((ModelRenderer)this.childModels.get(i)).render(scale);
							}
						}

						GL11.glTranslatef(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
					}
				}
				else
				{
					GL11.glPushMatrix();
					GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

					if (this.rotateAngleZ != 0.0F)
					{
						GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
					}

					if (this.rotateAngleY != 0.0F)
					{
						GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
					}

					if (this.rotateAngleX != 0.0F)
					{
						GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
					}

					GL11.glPushMatrix();
					GL11.glScalef(theScale, theScale, theScale);
					GL11.glCallList(displayList);
					GL11.glPopMatrix();

					if (this.childModels != null)
					{
						for (i = 0; i < this.childModels.size(); ++i)
						{
							((ModelRenderer)this.childModels.get(i)).render(scale);
						}
					}

					GL11.glPopMatrix();
				}

				GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
			}
		}
	}
	
	@Override
	public void renderWithRotation(float scale) {
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.compiled)
				{
					this.compileDisplayList(scale);
				}

				GL11.glPushMatrix();
				GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

				if (this.rotateAngleY != 0.0F)
				{
					GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
				}

				if (this.rotateAngleX != 0.0F)
				{
					GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
				}

				if (this.rotateAngleZ != 0.0F)
				{
					GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
				}

				GL11.glPushMatrix();
				GL11.glScalef(theScale, theScale, theScale);
				GL11.glCallList(displayList);
				GL11.glPopMatrix();
				GL11.glPopMatrix();
			}
		}
	}
	
	protected void compileDisplayList(float scale) {
		displayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE);
		
		GL11.glPushMatrix();
		GL11.glScalef(0.76F, 0.76F, 0.76F);
		GL11.glRotatef(180F, 1F, 0F, 0F);
		model.renderAll();
		GL11.glPopMatrix();
		
		GL11.glEndList();
		compiled = true;
	}
	
	public IModelCustom model;
	private float theScale;
	
	private int displayList;
	private boolean compiled;
}
