package me.heronerin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.LayerRange;
import me.heronerin.mixin.GameRendererAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashCallable;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static me.heronerin.BlazePrinter.*;

public class SchematicGUI extends ConfigGui.Tab{
    private static class GuiLayer extends GuiRenderLayerEditBase{
        ConfigGui parent;
        GuiLayer(ConfigGui parent) {
            this.parent = parent;
            this.title = parent.getTitleString();
        }
        @Override
        public void initGui() {
            super.initGui();

            ConfigGui.createButtons(this, this.parent);
            this.addButton(new ButtonGeneric(
                    30, 50, -1, 20, "Boo"
            ), (_btn, _poi)->{

            });


        }
        @Override
        protected LayerRange getLayerRange() {
            return null;
        }

        @Override
        public void render(DrawContext drawContext, int x, int y, float partialTicks){
            super.render(drawContext, x, y, partialTicks);
            double scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor();


            MatrixStack matrices = drawContext.getMatrices();
            int width = (int)(50 * scaleFactor);
            int height = (int)(50 * scaleFactor);
            int x_max = x + width;
            int y_max = y + height;

            RenderUtils.drawOutline(x, y, width, height, 0xFFFFFFFF);

            // Translate the matrix master schematic render location
            matrices.translate((float) (x + width/2), (float) (y + height/2), (float) (150));

            // Apply scaling
            matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));

            matrices.scale((float) (4.0D * scaleFactor), (float) (4.0D * scaleFactor), (float) (4.0D * scaleFactor));
            // Apply rotation
            matrices.multiply(RotationAxis.of(new Vector3f(0.5f, -1f, 0)).rotationDegrees(45));

            for (int X = 0; X < 10; X++) {
                for (int Y = 0; Y < 10; Y++) {
                    matrices.push();
                    matrices.translate(X, 0, Y);

                    // Render the block
                    this.client.getBlockRenderManager().renderBlockAsEntity(
                            (X+Y)%2==0 ? Blocks.TRIPWIRE.getDefaultState() : Blocks.YELLOW_WOOL.getDefaultState(),
                            matrices,
                            drawContext.getVertexConsumers(),
                            15728880, // Light value
                            OverlayTexture.DEFAULT_UV
                    );

                    matrices.pop(); // Revert transformations for the next block
                }
            }
            matrices.pop(); // Revert the initial push


        }

    }

    @Override
    public String getTitle() {
        return "Schematic selection";
    }

    @Override
    public List<GuiConfigsBase.ConfigOptionWrapper> getConfigs() {
        return List.of();
    }
    public void initGui(ConfigGui screen){
        GuiConfigsBase.openGui(new GuiLayer(screen));
    }
}
