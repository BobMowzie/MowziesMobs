package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.packet.PacketDecreaseTimer;
import com.bobmowzie.mowziesmobs.packet.PacketIncreaseTimer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityFoliaath extends MMEntityBase
{
	public IntermittentAnimation openMouth = new IntermittentAnimation(15, 70, 20, 1);
	public ControlledAnimation active = new ControlledAnimation(10);
	public float targetDistance;

	public EntityFoliaath(World world)
	{
		super(world);
		this.getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		tasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityCreature.class, 0, true));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.worldObj.isRemote) {
			if (getAnimID() == 0) {
				this.openMouth.runAnimation();
			} else {
				this.openMouth.stopAnimation();
			}
		}
		this.renderYawOffset = 0;
		this.rotationYaw = 0;

		if (this.getAttackTarget() instanceof EntityFoliaath) this.setAttackTarget(null);
		if (getAttackTarget() != null)
		{
			this.setRotationYawHead((float) (Math.atan2(getAttackTarget().posZ - this.posZ, getAttackTarget().posX - this.posX) * (180 / Math.PI) + 90));
			targetDistance = (float) Math.sqrt((getAttackTarget().posZ - this.posZ) * (getAttackTarget().posZ - this.posZ) + (getAttackTarget().posX - this.posX) * (getAttackTarget().posX - this.posX));
			if (targetDistance <= 7) MowziesMobs.networkWrapper.sendToAll(new PacketIncreaseTimer(this.getEntityId()));
			else MowziesMobs.networkWrapper.sendToAll(new PacketDecreaseTimer(this.getEntityId()));
		}
		else
		{
			MowziesMobs.networkWrapper.sendToAll(new PacketDecreaseTimer(this.getEntityId()));
		}
	}
}

