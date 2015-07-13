package com.bobmowzie.mowziesmobs.client.debug;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import scala.actors.threadpool.Arrays;
import scala.annotation.meta.field;
import thehippomaster.AnimationAPI.AnimationAPI;

import com.bobmowzie.mowziesmobs.client.model.tools.MowzieModelBase;
import com.bobmowzie.mowziesmobs.client.model.tools.MowzieModelRenderer;
import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public final class ModelGrapher {
	public static final ModelGrapher INSTANCE = new ModelGrapher();

	private HashMap<MowzieModelBase, String[]> fieldNames = new HashMap<MowzieModelBase, String[]>();

	private String xyz = "\u00A7cX \u00A7aY \u00A79Z";

	private int xyzWidth = -1;

	private int tickRange = 40;

	private String[] modelBaseFieldNames = { "field_77045_g", "i", "mainModel" };

	private Field modelBaseField = ReflectionHelper.findField(RendererLivingEntity.class, modelBaseFieldNames);

	private Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), AnimationAPI.fTimer);

	private Entity watchingEntity;

	private MowzieModelRenderer[] parts;

	private String[] names;

	private ArrayList<TickPoint> ticks = new ArrayList<TickPoint>(tickRange);

	private int tick = 0;

	private boolean pause = false;

	private int watchingPart = 0;

	private ModelGrapher() {
	}

	private void setWatchingModel(MMEntityBase entity) {
		ticks.clear();
		watchingEntity = entity;
		if (watchingEntity == null) {
			parts = null;
			names = null;
		} else {
			RenderLiving render = (RenderLiving) RenderManager.instance.entityRenderMap.get(watchingEntity.getClass());
			MowzieModelBase model = (MowzieModelBase) getMainModel(render);
			parts = model.getParts();
			if (fieldNames.containsKey(model)) {
				names = fieldNames.get(model);
			} else {
				names = new String[parts.length];
				fieldNames.put(model, names);
				List<Field> fields = new ArrayList<Field>(Arrays.asList(model.getClass().getDeclaredFields()));
				try {
					for (int i = 0; i < parts.length; i++) {
						MowzieModelRenderer part = parts[i];
						for (Field field : fields) {
							if (field.get(model) == part) {
								names[i] = String.format("%s (%s/%s)", field.getName(), i + 1, parts.length);
								fields.remove(field);
								break;
							}
						}
					}
				} catch (Exception e) {
					throw new Error(e);
				}
			}
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.theWorld == null || mc.thePlayer == null || !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			return;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			List<MMEntityBase> nearEntities = mc.theWorld.getEntitiesWithinAABB(MMEntityBase.class, mc.thePlayer.boundingBox.expand(16, 16, 16));
			if (nearEntities.size() > 0) {
				double minDist = Double.MAX_VALUE;
				MMEntityBase nearestEntity = null;
				for (MMEntityBase entity : nearEntities) {
					double dist = mc.thePlayer.getDistanceSqToEntity(entity);
					if (dist < minDist) {
						minDist = dist;
						nearestEntity = entity;
					}
				}
				System.out.println(nearestEntity);
				setWatchingModel(nearestEntity);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			setWatchingModel(null);
		}
		if (watchingEntity != null) {
			if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
				pause = !pause;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (watchingPart > 0) {
					watchingPart--;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (watchingPart < parts.length - 1) {
					watchingPart++;
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (watchingEntity == null || event.type != RenderGameOverlayEvent.ElementType.TEXT) {
			return;
		}
		GL11.glLineWidth(3);
		int x = 1, y = 1, width = tickRange * 10, height = 100;
		Gui.drawRect(x, y, x + width, y + height, 0x3F000000);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineStipple(16, (short) 0x5555);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2i(x, y + height / 2);
		GL11.glVertex2i(x + width, y + height / 2);
		float mx = x + (width - ((tick + (pause ? 1 : timer.elapsedPartialTicks)) * 10) % width);
		GL11.glVertex2f(mx, x);
		GL11.glVertex2f(mx, x + height);
		mx = x + (width - ((tick + (pause ? 1 : timer.elapsedPartialTicks) + 20) * 10) % width);
		GL11.glVertex2f(mx, x);
		GL11.glVertex2f(mx, x + height);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_STIPPLE);
		GL11.glPushMatrix();
		GL11.glTranslatef(x + (width - ticks.size() * 10), y + height / 2, 0);
		GL11.glBegin(GL11.GL_LINES);
		for (int c = 0; c < 3; c++) {
			GL11.glColor3f(c == 0 ? 1 : 0, c == 1 ? 1 : 0, c == 2 ? 1 : 0);
			if (ticks.size() == 40) {
				TickPoint tickPoint = ticks.get(0);
				if (tickPoint.renders.size() > 0) {
					RenderPoint renderPoint = tickPoint.renders.get(0);
					GL11.glVertex2f(0, renderPoint.values[c][watchingPart] * (height / 2 - 1));
					GL11.glVertex2f(renderPoint.partialRenderTicks * 10, renderPoint.values[c][watchingPart] * (height / 2 - 1));
				}
			}
			for (int i = 0; i < ticks.size(); i++) {
				TickPoint tickPoint = ticks.get(i);
				for (int n = 0; n < tickPoint.renders.size(); n++) {
					RenderPoint render = tickPoint.renders.get(n);
					float[][] values = render.values;
					float xp1 = (i + render.partialRenderTicks) * 10;
					float xp2 = -1;
					float[][] next = null;
					boolean lastRenderInTick = n == tickPoint.renders.size() - 1;
					if (lastRenderInTick) {
						if (i < ticks.size() - 1) {
							TickPoint nextTickPoint = ticks.get(i + 1);
							if (nextTickPoint.renders.size() > 0) {
								RenderPoint nextRenderPoint = nextTickPoint.renders.get(0);
								xp2 = (i + 1 + nextRenderPoint.partialRenderTicks) * 10;
								next = nextRenderPoint.values;
							}
						}
					} else {
						RenderPoint nextRenderPoint = tickPoint.renders.get(n + 1);
						xp2 = (i + nextRenderPoint.partialRenderTicks) * 10;
						next = nextRenderPoint.values;
					}
					if (next == null) {
						xp2 = (i + 1) * 10;
						next = values;
					}
					float[] xyz1 = values[c];
					float[] xyz2 = next[c];
					GL11.glVertex2f(xp1, xyz1[watchingPart] * (height / 2 - 1));
					GL11.glVertex2f(xp2, xyz2[watchingPart] * (height / 2 - 1));
				}
			}
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glColor3f(0, 0, 0);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x + width, y);
		GL11.glVertex2i(x + width, y + height);
		GL11.glVertex2i(x, y + height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		fontRenderer.drawString(names[watchingPart], x + 2, y + 2, 0xFFFFFF, true);
		if (xyzWidth == -1) {
			xyzWidth = fontRenderer.getStringWidth(xyz);
		}
		fontRenderer.drawString(xyz, x + width - xyzWidth - 2, y + 2, 0xFFFFFF, true);
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			return;
		}
		if (watchingEntity != null && watchingEntity.isDead) {
			setWatchingModel(null);
		}
		if (pause) {
			return;
		}
		if (ticks.size() == tickRange) {
			ticks.remove(0);
		}
		ticks.add(new TickPoint());
		tick++;
	}

	@SubscribeEvent
	public void onRenderLivingEvent(RenderLivingEvent.Post event) {
		if (event.entity != watchingEntity || pause) {
			return;
		}
		TickPoint tick = ticks.get(ticks.size() - 1);
		float[][] rotations = new float[3][parts.length];
		for (int i = 0; i < parts.length; i++) {
			ModelRenderer modelRenderer = parts[i];
			rotations[0][i] = MathHelper.wrapAngleTo180_float(modelRenderer.rotateAngleX / (float) Math.PI * 180) / 180;
			rotations[1][i] = MathHelper.wrapAngleTo180_float(modelRenderer.rotateAngleY / (float) Math.PI * 180) / 180;
			rotations[2][i] = MathHelper.wrapAngleTo180_float(modelRenderer.rotateAngleZ / (float) Math.PI * 180) / 180;
		}
		tick.renders.add(new RenderPoint(timer.renderPartialTicks, rotations));
	}

	private ModelBase getMainModel(RenderLiving render) {
		try {
			return (ModelBase) modelBaseField.get(render);
		} catch (Exception e) {
			return null;
		}
	}

	private class TickPoint {
		private List<RenderPoint> renders = new ArrayList<RenderPoint>();
	}

	private class RenderPoint {
		private float partialRenderTicks;
		private float[][] values;

		public RenderPoint(float partialRenderTicks, float[][] values) {
			this.partialRenderTicks = partialRenderTicks;
			this.values = values;
		}
	}
}
