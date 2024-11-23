package me.heronerin.mixin;


import com.mojang.blaze3d.systems.RenderSystem;
import me.heronerin.printer.MainPrinter;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.joml.Vector4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable {
    @Inject(at = {@At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0)}, method = "renderWorld")
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        GameRenderer renderer = (GameRenderer) (Object) this;
        ClientWorld world = renderer.getClient().world;
        Camera camera = renderer.getCamera();
        if (world == null || camera == null) return;

        // Get the camera and player position
        PlayerEntity player = renderer.getClient().player;
        double camX = camera.getPos().x;
        double camY = camera.getPos().y;
        double camZ = camera.getPos().z;

        // Take into account the player's velocity
        double playerVelocityX = player.getVelocity().x;
        double playerVelocityY = player.getVelocity().y;
        double playerVelocityZ = player.getVelocity().z;


        RenderSystem.enableDepthTest();
        VertexConsumerProvider.Immediate vertexConsumers = renderer.getClient().getBufferBuilders().getEntityVertexConsumers();
        for (Pair<BlockPos, Vector4f> cube: MainPrinter.getInstance().cubes_to_render) {
            // Define the block position and outline box
            BlockPos blockPos = cube.getLeft();
            Box outlineBox = new Box(blockPos);

            // Adjust the block's position based on velocity and camera position
            // This can help mitigate misalignment due to movement
            double adjustedX = outlineBox.minX - camX - playerVelocityX;
            double adjustedY = outlineBox.minY - camY - playerVelocityY;
            double adjustedZ = outlineBox.minZ - camZ - playerVelocityZ;
            double adjustedMaxX = outlineBox.maxX - camX - playerVelocityX;
            double adjustedMaxY = outlineBox.maxY - camY - playerVelocityY;
            double adjustedMaxZ = outlineBox.maxZ - camZ - playerVelocityZ;

            // Draw the box outline
            WorldRenderer.renderFilledBox(
                    matrices,
                    vertexConsumers.getBuffer(RenderLayer.getLines()), // Use line layer for outlines
                    adjustedX,
                    adjustedY,
                    adjustedZ,
                    adjustedMaxX,
                    adjustedMaxY,
                    adjustedMaxZ,
                    cube.getRight().x ,cube.getRight().y, cube.getRight().z, cube.getRight().w
            );
        }
        vertexConsumers.draw(); // Finish rendering
    }
}
