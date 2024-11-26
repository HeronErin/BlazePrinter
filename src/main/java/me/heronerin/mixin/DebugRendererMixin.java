package me.heronerin.mixin;


import me.heronerin.printer.MainPrinter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.heronerin.BlazePrinter.PREVIEWS;
import static me.heronerin.BlazePrinter.PREVIEW_ORIGIN;


@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin implements AutoCloseable {

    // Render a filled in box
    @Unique
    private void renderCube(Pair<BlockPos, Vector4f> cube, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ){
            BlockPos blockPos = cube.getLeft();
            Box outlineBox = new Box(blockPos);

            DebugRenderer.drawBox(
                    matrices,
                    vertexConsumers,
                    outlineBox.minX - cameraX,
                    outlineBox.minY - cameraY,
                    outlineBox.minZ - cameraZ,
                    outlineBox.maxX - cameraX,
                    outlineBox.maxY - cameraY,
                    outlineBox.maxZ - cameraZ,
                    cube.getRight().x ,cube.getRight().y, cube.getRight().z, cube.getRight().w
            );
    }

    // Render the outline of a box
    @Unique
    private void renderBox(Pair<BlockPos, Vector4f> cube, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ){
        BlockPos blockPos = cube.getLeft();
        Box outlineBox = new Box(blockPos);

        WorldRenderer.drawBox(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getLines()),
                outlineBox.minX - cameraX,
                outlineBox.minY - cameraY,
                outlineBox.minZ - cameraZ,
                outlineBox.maxX - cameraX,
                outlineBox.maxY - cameraY,
                outlineBox.maxZ - cameraZ,
                cube.getRight().x ,cube.getRight().y, cube.getRight().z, cube.getRight().w
        );
    }

    @Inject(at = {@At(value = "TAIL")}, method = "render")
    private void onRenderWorld(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null || MinecraftClient.getInstance().cameraEntity == null) return;


        if (!PREVIEWS.getBooleanValue()) return;

        for (Pair<BlockPos, Vector4f> cube : MainPrinter.getInstance().cubes_to_render){
            renderCube(cube, matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        }
        if (PREVIEW_ORIGIN.getBooleanValue())
            if (MainPrinter.getInstance().origin_preview != null)
                renderBox(MainPrinter.getInstance().origin_preview, matrices, vertexConsumers, cameraX, cameraY, cameraZ);

    }
}
