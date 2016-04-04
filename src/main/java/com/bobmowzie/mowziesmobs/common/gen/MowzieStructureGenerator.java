package com.bobmowzie.mowziesmobs.common.gen;

import com.bobmowzie.mowziesmobs.common.gen.structure.StructureWroughtnautRoom;
import com.bobmowzie.mowziesmobs.common.gen.structure.barakoa.StructureBarakoThrone;
import com.bobmowzie.mowziesmobs.common.gen.structure.barakoa.StructureBarakoaHouse;
import coolalias.structuregenapi.util.Structure;
import coolalias.structuregenapi.util.StructureGeneratorBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class MowzieStructureGenerator extends StructureGeneratorBase {
    /**
     * List storing all structures currently available
     */
    public static final List<Structure> structures = new LinkedList();

    static {
        Structure wroughtRoom = new Structure("Wroughtnaut Room");
        wroughtRoom.addBlockArray(StructureWroughtnautRoom.blockArray);
        structures.add(wroughtRoom);

        Structure barakoaHouse1 = new Structure("Barakoa House 1");
        barakoaHouse1.addBlockArray(StructureBarakoaHouse.blockArray1);
        structures.add(barakoaHouse1);

        Structure barakoaSkulls = new Structure("Barakoa Skulls");
        barakoaSkulls.addBlockArray(StructureBarakoaHouse.blockArray2);
        structures.add(barakoaSkulls);

        Structure barakoThrone = new Structure("Barako Throne");
        barakoThrone.addBlockArray(StructureBarakoThrone.blockArray);
        structures.add(barakoThrone);

        Structure barakoaFire = new Structure("Barakoa Fire");
        barakoaFire.addBlockArray(StructureBarakoaHouse.blockArray3);
        structures.add(barakoaFire);

        Structure barakoaHouseExtra = new Structure("Barakoa House Extra");
        barakoaHouseExtra.addBlockArray(StructureBarakoaHouse.blockArray4);
        structures.add(barakoaHouseExtra);
    }

    public MowzieStructureGenerator() {
        // TODO Auto-generated constructor stub
    }

    public MowzieStructureGenerator(Entity entity, int[][][][] blocks) {
        super(entity, blocks);
        // TODO Auto-generated constructor stub
    }

    public MowzieStructureGenerator(Entity entity, int[][][][] blocks, int structureFacing) {
        super(entity, blocks, structureFacing);
        // TODO Auto-generated constructor stub
    }

    public MowzieStructureGenerator(Entity entity, int[][][][] blocks, int structureFacing, int offX, int offY, int offZ) {
        super(entity, blocks, structureFacing, offX, offY, offZ);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getRealBlockID(int fakeID, int customData1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onCustomBlockAdded(World world, int x, int y, int z, int fakeID, int customData1, int customData2) {
        // TODO Auto-generated method stub
    }
}