package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import com.bobmowzie.mowziesmobs.packet.foliaath.PacketDecreaseTimer;
import com.bobmowzie.mowziesmobs.packet.foliaath.PacketIncreaseTimer;
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
    public int lastTimeDecrease = 0;

	public EntityFoliaath(World world)
	{
		super(world);
		this.getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		tasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityCreature.class, 0, true));
	}

    public void entityInit()
    {
        super.entityInit();
    }

	public void onUpdate()
    {
		super.onUpdate();
		if (worldObj.isRemote)
        {
			if (getAnimID() == 0) openMouth.runAnimation();
			else openMouth.stopAnimation();
		}
		renderYawOffset = 0;
		rotationYaw = 0;

        if (getAttackTarget() instanceof EntityFoliaath) setAttackTarget(null);
		if (getAttackTarget() != null)
        {
            setRotationYawHead((float) (Math.atan2(getAttackTarget().posZ - posZ, getAttackTarget().posX - posX) * (180 / Math.PI) + 90));
            targetDistance = (float) Math.sqrt((getAttackTarget().posZ - posZ) * (getAttackTarget().posZ - posZ) + (getAttackTarget().posX - posX) * (getAttackTarget().posX - posX));

            if (targetDistance <= 7)
            {
                sendPacket(new PacketIncreaseTimer(getEntityId()));
                lastTimeDecrease = 0;
            }
            else if (lastTimeDecrease <= 10)
            {
                sendPacket(new PacketDecreaseTimer(getEntityId()));
                lastTimeDecrease++;
            }
        }
        else if (lastTimeDecrease <= 10)
        {
            sendPacket(new PacketDecreaseTimer(getEntityId()));
            lastTimeDecrease++;
        }
	}

    public void sendPacket(AbstractPacket packet)
    {
        if (!worldObj.isRemote)
        {
            System.out.println("[MowziesMobs]: Sending packet " + packet);
            MowziesMobs.networkWrapper.sendToAll(packet);
        }
    }
}

