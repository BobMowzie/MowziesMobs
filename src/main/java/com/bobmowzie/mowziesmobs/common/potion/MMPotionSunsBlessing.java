package com.bobmowzie.mowziesmobs.common.potion;

import com.bobmowzie.mowziesmobs.common.property.MMPlayerExtention;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSummonSunstrike;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MMPotionSunsBlessing extends MMPotion
{
    private static final int SUNSTRIKE_COOLDOWN = 50;

    public MMPotionSunsBlessing(int id)
    {
        super(id, false, 0xFFDF42);
        setPotionName("potion.sunsBlessing");
        setIconIndex(0, 0);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        System.out.println(((MMPlayerExtention)event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike);
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.world.isRemote && event.entityPlayer.inventory.getCurrentItem() == null && event.entityPlayer.isPotionActive(MMPotions.sunsBlessing) && ((MMPlayerExtention)event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike <= 0)
        {
            MowziesMobs.networkWrapper.sendToServer(new MessagePlayerSummonSunstrike());
            ((MMPlayerExtention)event.entityPlayer.getExtendedProperties("mm:player")).untilSunstrike = SUNSTRIKE_COOLDOWN;
        }
    }
}
