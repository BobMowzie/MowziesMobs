package com.ilexiconn.llibrary.client.lang;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author gegy1000
 * @since 1.1.0
 */
@OnlyIn(Dist.CLIENT)
public class RemoteLanguageContainer {
    public LangContainer[] languages;

    public class LangContainer {
        public String locale;
        public String downloadURL;
    }
}
