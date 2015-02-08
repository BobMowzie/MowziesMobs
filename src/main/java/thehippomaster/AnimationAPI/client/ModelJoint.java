package thehippomaster.AnimationAPI.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * ModelJoint.java
 * This is an extension of ModelRenderer that creates two model parts: a virtual model and the real model.
 * The real model is a child of the virtual model, and by doing this, one can create better rotations
 * that do not have to only be based off of the real model.
 * 
 * @author thehippomaster21
 */
@SideOnly(Side.CLIENT)
public class ModelJoint extends ModelRenderer {
	
	public ModelJoint(ModelBase base) {
		this(base, null);
		super.addChild(model = new ModelRenderer(base));
	}
	
	public ModelJoint(ModelBase base, int x, int y) {
		this(base);
		model = new ModelRenderer(base, x, y);
		model.setTextureOffset(x, y);
		super.addChild(model);
	}
	
	public ModelJoint(ModelBase base, String name) {
		super(base, name);
		model = new ModelRenderer(base, name);
		model.setTextureOffset(0, 0);
		model.setTextureSize(base.textureWidth, base.textureHeight);
		super.addChild(model);
	}
	
	public ModelRenderer setModel(ModelRenderer newModel) {
		childModels.remove(model);
		model = newModel;
		super.addChild(newModel);
		return this;
	}
	
	public ModelRenderer setTextureOffset(int x, int y) {
		if(model != null) model.setTextureOffset(x, y);
		return this;
	}
	
	public ModelRenderer setTextureSize(int w, int h) {
		if(model != null) model.setTextureSize(w, h);
		return this;
	}
	
	public ModelRenderer addBox(String name, float x, float y, float z, int w, int h, int d) {
		model.addBox(name, x, y, z, w, h, d);
		return this;
	}
	
	public ModelRenderer addBox(float x, float y, float z, int w, int h, int d) {
		model.addBox(x, y, z, w, h, d);
		return this;
	}
	
	public void addBox(float x, float y, float z, int w, int h, int d, float offset) {
		model.addBox(x, y, z, w, h, d, offset);
	}
	
	public void addChild(ModelRenderer child) {
		model.addChild(child);
	}
	
	public ModelRenderer getModel() {
		return model;
	}
	
	private ModelRenderer model;
}