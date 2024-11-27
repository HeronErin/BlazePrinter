package me.heronerin.schematic_generators;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class FactionCactiFarmGen extends BaseGenerator{
    @Override
    public int getWidthTilingResolution() {
        return 2;
    }

    @Override
    public String getDisplayName() {
        return "Faction Cactus farm";
    }

    @Override
    public List<Pair<BlockPos, Block>> generate(int width, int height) {
        List<Pair<BlockPos, Block>> ret = new ArrayList<>();
        for (int x = 0; x < width; x++) for (int z = 0; z < height; z+=2){
            final boolean isEven = (x + z) % 2 == 0;
            ret.add(new Pair<>(
                    new BlockPos(x, 0, z),
                    isEven ? Blocks.AIR : Blocks.SAND
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 1, z),
                    isEven ? Blocks.AIR : Blocks.CACTUS
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 2, z),
                    isEven ? Blocks.OAK_FENCE : Blocks.AIR
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 3, z),
                    isEven ? Blocks.SAND : Blocks.AIR
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 4, z),
                    isEven ? Blocks.CACTUS : Blocks.AIR
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 5, z),
                    isEven ? Blocks.AIR : Blocks.OAK_FENCE
            ));
        }

        return ret;
    }
}
