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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin implements AutoCloseable {
    @Inject(at = {@At(value = "TAIL")}, method = "render")
    private void onRenderWorld(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        ClientWorld world = MinecraftClient.getInstance().world;

        if (world == null || MinecraftClient.getInstance().cameraEntity == null) return;


        for (Pair<BlockPos, Vector4f> cube: MainPrinter.getInstance().cubes_to_render) {
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
    }
}
