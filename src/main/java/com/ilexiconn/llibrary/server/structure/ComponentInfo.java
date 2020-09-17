package com.ilexiconn.llibrary.server.structure;

import com.ilexiconn.llibrary.server.structure.rule.PlaceRule;
import com.ilexiconn.llibrary.server.structure.rule.RepeatRule;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class ComponentInfo {
    public HashMap<BlockPos, BlockList> blocks;
    public List<RepeatRule> repeats;
    public EnumFacing front;
    public EnumFacing top;

    public ComponentInfo() {
        this.front = EnumFacing.EAST;
        this.top = EnumFacing.UP;
        this.blocks = new HashMap<>();
        this.repeats = new ArrayList<>();
        this.repeats.add(new PlaceRule());
    }
}
