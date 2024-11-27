package me.heronerin.schematic_generators;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CompactCactiGen extends BaseGenerator{
    @Override
    public int getWidthTilingResolution() {
        return 1;
    }

    @Override
    public Integer getYTilingResolution() {
        return 4;
    }

    @Override
    public String getDisplayName() {
        return "Compact cactus farm";
    }

    @Override
    public List<Pair<BlockPos, Block>> generate(int width, int height) {
        List<Pair<BlockPos, Block>> ret = new ArrayList<>();
        for (int x = 0; x < width; x++) for (int z = 0; z < height; z++){
            boolean isEven = (x + z) % 2 == 0;
            ret.add(new Pair<>(
                    new BlockPos(x, 0, z),
                    isEven ? Blocks.AIR : Blocks.SAND
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 1, z),
                    isEven ? Blocks.TRIPWIRE : Blocks.CACTUS
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 2, z),
                    isEven ? Blocks.SAND : Blocks.AIR
            ));
            ret.add(new Pair<>(
                    new BlockPos(x, 3, z),
                    isEven ? Blocks.CACTUS : Blocks.TRIPWIRE
            ));

        }
        return ret;

    }
}
