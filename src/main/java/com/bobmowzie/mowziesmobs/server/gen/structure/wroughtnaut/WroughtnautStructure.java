package com.bobmowzie.mowziesmobs.server.gen.structure.wroughtnaut;

import net.ilexiconn.llibrary.server.structure.StructureBuilder;
import net.minecraft.init.Blocks;

public class WroughtnautStructure {
    public static StructureBuilder getStructure() {
        StructureBuilder builder = new StructureBuilder();

        builder.startComponent()
                .fillCube(0, 0, 0, 18, 1, 18, Blocks.stone)
                .endComponent();

        return builder;
    }
}
