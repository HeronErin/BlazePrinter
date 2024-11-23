package me.heronerin.printer;

import  net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

// A block with the desire to be printed
public class PrintBlock {
    public final BlockPos pos;
    public final Block type;
    public boolean is_placed;


    public PrintBlock(BlockPos pos, Block type) {
        this.pos = pos;
        this.type = type;
        this.is_placed = false;

    }
}
