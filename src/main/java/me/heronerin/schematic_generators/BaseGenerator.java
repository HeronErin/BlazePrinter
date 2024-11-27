package me.heronerin.schematic_generators;

import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseGenerator {
    // Smallest unit the generator can be broken up into.
    // Ex: If resolution is set to 2 generate will never be asked to generate with a width of 3
    public abstract int getWidthTilingResolution();

    // If null the printer will not attempt to stack one generated object on top of another
    // Otherwise returns how high up to repeat the schematic
    public @Nullable Integer getYTilingResolution(){
        return null;
    }

    public abstract String getDisplayName();


    // Result coordinates MUST be positive, and MUST be cornered at the origin.
    // Note: height in this context means Z NOT Y
    public abstract List<Pair<BlockPos, Block>> generate(int width, int height);

    // Note: height in this context means Z NOT Y
    public static void recenterAtOrigin(List<Pair<BlockPos, Block>> blocks, int width, int height){
        // Note: Odd sized widths/heights cause weirdness
        int offsetX = -width/2;
        int offsetZ = -height/2;
        for (Pair<BlockPos, Block> block : blocks){
            block.setLeft(block.getLeft().add(offsetX, 0, offsetZ));
        }
    }
}
