package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.util.LayerRange;
import me.heronerin.Utils;
import me.heronerin.printer.MainPrinter;
import me.heronerin.schematic_generators.BaseGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class SchematicGUI extends ConfigGui.Tab{



    private static class GuiLayer extends GuiRenderLayerEditBase{
        public final int MAX_PREVIEW_WIDTH = 8;

        ConfigGui parent;
        WidgetDropDownList<BaseGenerator> dropDownMenu;
        GuiLayer(ConfigGui parent) {
            this.parent = parent;
            this.title = parent.getTitleString();

            Utils.setStashedDropdownMenuCallback((index) ->{
                BaseGenerator generator =  MainPrinter.getInstance().generatorOptions.get(index);
                int resolution = generator.getWidthTilingResolution();
                int size = MAX_PREVIEW_WIDTH - MAX_PREVIEW_WIDTH % resolution;

                List<Pair<BlockPos, Block>> blocks = generator.generate(size, size);
                BaseGenerator.recenterAtOrigin(blocks, size, size);
                blockViewWidget.blocks = blocks;
            });

            dropDownMenu = new WidgetDropDownList<BaseGenerator>(
                    10, 50, 150, 20, -1,
                    10,
                    MainPrinter.getInstance().generatorOptions,
                    BaseGenerator::getDisplayName
            );

            blockViewWidget = new BlockViewWidget(10, 75, 150, 150, 3.0f, new ArrayList<>());
        }
        private BlockViewWidget blockViewWidget;

        @Override
        public void initGui() {
            super.initGui();

            ConfigGui.createButtons(this, this.parent);

            this.addWidget(blockViewWidget);


            this.addWidget(dropDownMenu);
        }
        @Override
        protected LayerRange getLayerRange() {
            return null;
        }

        @Override
        public void render(DrawContext drawContext, int x, int y, float partialTicks){
            super.render(drawContext, x, y, partialTicks);
            double scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor();


//
//
//            MatrixStack matrices = drawContext.getMatrices();
//            int width = (int)(50 * scaleFactor);
//            int height = (int)(50 * scaleFactor);
//            int x_max = x + width;
//            int y_max = y + height;
//
//            RenderUtils.drawOutline(x, y, width, height, 0xFFFFFFFF);
//            matrices.push();
//            // Translate the matrix master schematic render location
//            matrices.translate((float) (x + width/2), (float) (y + height/2), (float) (150));
//
//            // Apply scaling
//            matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
//
//            matrices.scale((float) (4.0D * scaleFactor), (float) (4.0D * scaleFactor), (float) (4.0D * scaleFactor));
//            // Apply rotation
//            matrices.multiply(RotationAxis.of(new Vector3f(0.5f, -1f, 0)).rotationDegrees(45));
////
//            for (int X = 0; X < 10; X++) {
//                for (int Y = 0; Y < 10; Y++) {
//                    matrices.push();
//                    matrices.translate(X, 0, Y);
//
//                    // Render the block
//                    this.client.getBlockRenderManager().renderBlockAsEntity(
//                            (X+Y)%2==0 ? Blocks.TRIPWIRE.getDefaultState() : Blocks.YELLOW_WOOL.getDefaultState(),
//                            matrices,
//                            drawContext.getVertexConsumers(),
//                            15728880, // Light value
//                            OverlayTexture.DEFAULT_UV
//                    );
//
//                    matrices.pop(); // Revert transformations for the next block
//                }
//            }
//            matrices.pop(); // Revert the initial push


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
