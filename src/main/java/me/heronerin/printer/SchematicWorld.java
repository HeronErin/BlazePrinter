package me.heronerin.printer;

import com.mojang.blaze3d.systems.RenderSystem;
import me.heronerin.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.render.OverlayTexture.DEFAULT_UV;
import static net.minecraft.client.render.OverlayTexture.packUv;

public class SchematicWorld {
    Map<BlockPos, PrintBlock> schematicBlocks;

    List<Pair<BlockPos, Block>> blocksToRender;
    List<BlockPos> incorrectlyPlaced;

    public SchematicWorld(List<Pair<BlockPos, Block>> blocks){
        schematicBlocks = new HashMap<>(blocks.size());
        blocksToRender = new ArrayList<>(blocks.size());
        incorrectlyPlaced = new ArrayList<>(blocks.size());

        for (Pair<BlockPos, Block> block : blocks){
            schematicBlocks.put(block.getLeft(), new PrintBlock(block.getLeft(), block.getRight()));
        }
        this.updateAllBlocks();
    }

    public void updateBlock(BlockPos pos, BlockState state){
        if (MainPrinter.getInstance().orgin == null) return;


        pos = pos.subtract(MainPrinter.getInstance().orgin);

        if (!schematicBlocks.containsKey(pos)) return;
        PrintBlock pb = schematicBlocks.get(pos);

        pb.is_correctly_placed = pb.type == state.getBlock();
        pb.is_incorrectly_placed =  !pb.is_correctly_placed && !state.isAir();
        resetRenderBlocks();
    }
    public void updateAllBlocks(){
        if (MinecraftClient.getInstance().world == null) return;
        if (MainPrinter.getInstance().orgin == null) return;

        schematicBlocks.forEach((pos, block)->{
            BlockState bs =  MinecraftClient.getInstance().world.getBlockState(pos.add(MainPrinter.getInstance().orgin));
            block.is_correctly_placed = block.type == bs.getBlock();
            block.is_incorrectly_placed =  !block.is_correctly_placed && !bs.isAir();
        });
        resetRenderBlocks();
    }
    public void resetRenderBlocks(){
        blocksToRender.clear();
        incorrectlyPlaced.clear();
        BlockPos orgin = MainPrinter.getInstance().orgin;
        if (orgin == null) return;


        for (PrintBlock block : schematicBlocks.values()) {
            if (block.is_correctly_placed) continue;

            if (block.is_incorrectly_placed)
                incorrectlyPlaced.add(block.pos.add(orgin));
            else
                blocksToRender.add(new Pair<>(block.pos.add(orgin), block.type));
        }
    }
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ){
        BlockPos orgin = MainPrinter.getInstance().orgin;
        if (orgin == null) return;
        float c = (float) Math.abs(Math.sin(
                Util.getMeasuringTimeMs() / 1000D
        ));
        RenderSystem.setShaderColor( c/2, c/2, 1f, 1f);


        for (Pair<BlockPos, Block> block : blocksToRender){
            matrices.push();

            BlockPos bp = block.getLeft();
            matrices.translate(bp.getX() - cameraX, bp.getY() - cameraY,  bp.getZ() - cameraZ);

            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                    block.getRight().getDefaultState(),
                    matrices,
                    vertexConsumers,
                    15728880, // Light value
                    DEFAULT_UV
            );

            matrices.pop();
        }
        vertexConsumers.draw();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (BlockPos bp : incorrectlyPlaced){
            matrices.push();
            matrices.translate(bp.getX() - cameraX, bp.getY() - cameraY,  bp.getZ() - cameraZ);

            DebugRenderer.drawBox(
                    matrices,
                    vertexConsumers,
                    0, 0, 0,
                    1, 1, 1,
                    1, 0, 0, 0.5f
            );

            matrices.pop();
        }




    }


}
