package com.bobmowzie.mowziesmobs.common.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSolarBeam;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSummonSunstrike;
import com.bobmowzie.mowziesmobs.common.property.MMPlayerExtention;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class MMPotionSunsBlessing extends MMPotion
{
    private static final int SUNSTRIKE_COOLDOWN = 90;
    private static final int LASER_COOLDOWN = 150;

    public MMPotionSunsBlessing(int id)
    {
        super(id, false, 0xFFDF42);
        setPotionName("potion.sunsBlessing");
        setIconIndex(0, 0);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.world.isRemote && event.entityPlayer.inventory.getCurrentItem() == null && event.entityPlayer.isPotionActive(MMPotions.sunsBlessing) && ((MMPlayerExtention)event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike <= 0)
        {
            if (event.entityPlayer.isSneaking()) {
                MowziesMobs.networkWrapper.sendToServer(new MessagePlayerSolarBeam());
                ((MMPlayerExtention) event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike = LASER_COOLDOWN;
            }
            else {
                MowziesMobs.networkWrapper.sendToServer(new MessagePlayerSummonSunstrike());
                ((MMPlayerExtention) event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike = SUNSTRIKE_COOLDOWN;
            }
        }
    }
}
