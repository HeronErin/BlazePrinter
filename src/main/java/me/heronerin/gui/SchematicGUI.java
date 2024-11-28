package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.util.LayerRange;
import me.heronerin.Utils;
import me.heronerin.printer.MainPrinter;
import me.heronerin.printer.SchematicWorld;
import me.heronerin.schematic_generators.BaseGenerator;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchematicGUI extends ConfigGui.Tab{



    private static class GuiLayer extends GuiRenderLayerEditBase{
        public final int MAX_PREVIEW_WIDTH = 8;

        ConfigGui parent;
        WidgetDropDownList<BaseGenerator> dropDownMenu;
        BlockViewWidget blockViewWidget = new BlockViewWidget(10, 50, 150, 150, 3.0f, new ArrayList<>());


        ButtonGeneric clearWorldSchematics = new ButtonGeneric(160, 50, 150, 20, "Clear world schematics");
        ButtonGeneric previewOnWorld = new ButtonGeneric(160, 72,  150, 20, "Preview on world");

        GuiTextFieldInteger widthField = new GuiTextFieldInteger(160, 96, 150 / 2 - 1, 20, this.textRenderer);
        GuiTextFieldInteger baseField = new GuiTextFieldInteger(160+ 150 / 2  +  2, 96 , 150 / 2 - 1, 20, this.textRenderer);


        // These get disabled if a schematic is not selected and/or an origin is not set
        final List<ButtonGeneric> schematic_btns = List.of(
                previewOnWorld
        );
        BaseGenerator current_generator;
        GuiLayer(ConfigGui parent) {
            this.parent = parent;
            this.title = parent.getTitleString();

            Utils.setStashedDropdownMenuCallback((index) ->{
                current_generator =  MainPrinter.getInstance().generatorOptions.get(index);
                int resolution = current_generator.getWidthTilingResolution();
                int size = MAX_PREVIEW_WIDTH - MAX_PREVIEW_WIDTH % resolution;

                List<Pair<BlockPos, Block>> blocks = current_generator.generate(size, size);
                BaseGenerator.recenterAtOrigin(blocks, size, size);
                blockViewWidget.blocks = blocks;
                for (ButtonGeneric btn : schematic_btns)
                    btn.setEnabled(true);
            });

            dropDownMenu = new WidgetDropDownList<>(
                    10, 202, 150, 20, 400,
                    10,
                    MainPrinter.getInstance().generatorOptions,
                    BaseGenerator::getDisplayName
            );
            for (ButtonGeneric btn : schematic_btns)
                btn.setEnabled(false);


            widthField.setEditable(false);
            baseField.setEditable(false);
        }

        @Override
        public void initGui() {
            super.initGui();

            ConfigGui.createButtons(this, this.parent);
            this.addButton(clearWorldSchematics, (btn, mbtn)->{
//                return false;
            });
            this.addButton(previewOnWorld, (btn, mbtn)->{
                MainPrinter.getInstance().currentSchematicWorld = new SchematicWorld(
                        current_generator.generate(16, 16)
                );

            });
            this.addTextField(widthField, null);
            this.addTextField(baseField, null);


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
