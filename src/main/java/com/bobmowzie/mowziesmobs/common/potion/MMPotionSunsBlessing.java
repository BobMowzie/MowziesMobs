package com.bobmowzie.mowziesmobs.common.potion;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSummonSunstrike;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MMPotionSunsBlessing extends MMPotion
{
    public MMPotionSunsBlessing(int id)
    {
        super(id, false, 0xFFDF42);
        setPotionName("potion.sunsBlessing");
        setIconIndex(0, 0);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && event.world.isRemote && event.entityPlayer.inventory.getCurrentItem() == null && event.entityPlayer.isPotionActive(MMPotions.sunsBlessing))
        {
            MowziesMobs.networkWrapper.sendToServer(new MessagePlayerSummonSunstrike());
        }
    }
}
