package me.heronerin.gui;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.render.RenderUtils;
import me.heronerin.schematic_generators.BaseGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

public class BlockViewWidget extends WidgetBase {
    private final float blockScale;
    public List<Pair<BlockPos, Block>> blocks;
    public BlockViewWidget(int x, int y, int width, int height, float blockScale, List<Pair<BlockPos, Block>> blocks) {
        super(x, y, width, height);
        this.blockScale = blockScale;
        this.blocks = blocks;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext){
        double scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor();

        RenderUtils.drawOutline(x, y, width, height, 0xFFFFFFFF);
        RenderUtils.drawRect(x, y, width, height, 0x99_00_00_00);

        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();

        // Translate the matrix master schematic render location
        matrices.translate((float) (x + width/2), (float) (y + height/2), (float) (150));
//
        // Apply scaling
        matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
        matrices.scale((float) (this.blockScale * scaleFactor), (float) (this.blockScale * scaleFactor), (float) (this.blockScale * scaleFactor));

        // Rotation
        matrices.multiply(RotationAxis.of(new Vector3f(0.5f, -1f, 0)).rotationDegrees(45));
        // Rotate about origin (center of widget)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) Util.getMeasuringTimeMs() / 10f));

        // Render the blocks
        for (Pair<BlockPos, Block> block : blocks){
            matrices.push();
            BlockPos bp = block.getLeft();
            matrices.translate(bp.getX(), bp.getY(), bp.getZ());

            mc.getBlockRenderManager().renderBlockAsEntity(
                    block.getRight().getDefaultState(),
                    matrices,
                    drawContext.getVertexConsumers(),
                    15728880, // Light value
                    OverlayTexture.DEFAULT_UV
            );


            matrices.pop();
        }


        matrices.pop();
    }

}
