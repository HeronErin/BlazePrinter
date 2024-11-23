package me.heronerin.printer;

import me.heronerin.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPrinter {
    private static MainPrinter instance = new MainPrinter();
    public static MainPrinter getInstance(){return instance;}


    // ALL BLOCK ARE RELATIVE TO THE ORIGIN
    public ArrayList<Pair<BlockPos, Vector4f>> cubes_to_render = new ArrayList<>();
    public Map<BlockPos, PrintBlock> blocks = new HashMap<>();

    public BlockPos orgin = null;

    // Called when world is changed
    public void updateBlock(BlockPos pos, BlockState state){
        pos = pos.subtract(orgin);
        if (!blocks.containsKey(pos)) return;
        PrintBlock pb = blocks.get(pos);

        pb.is_placed = pb.type == state.getBlock();
        resetRenderBlocks();
    }

    public void resetRenderBlocks(){
        cubes_to_render.clear();
        if (orgin != null)
            for (PrintBlock block : blocks.values())
                if (!block.is_placed)
                    cubes_to_render.add(new Pair<>(block.pos.add(orgin), Utils.block_to_color(block.type)));

    }
    public void addGrid(BlockPos bp, Block block, int size, boolean isInitialSet){
        for (int relx = 0; relx < size; relx++){
            for (int relz =  (relx + (isInitialSet ? 1 : 0)) % 2 ; relz < size; relz+=2){
                BlockPos relbp = bp.subtract(orgin).add(relx, 0, relz);
                blocks.put(relbp, new PrintBlock(relbp, block));
            }
        }
        resetRenderBlocks();
    }



}
