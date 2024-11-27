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

            dropDownMenu = new WidgetDropDownList<>(
                    10, 50, 150, 20, 400,
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

            this.addWidget(dropDownMenu);

            this.addWidget(blockViewWidget);
        }
        @Override
        protected LayerRange getLayerRange() {
            return null;
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
