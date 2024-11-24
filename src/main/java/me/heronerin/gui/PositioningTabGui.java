package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.util.LayerRange;
import me.heronerin.Utils;
import me.heronerin.printer.MainPrinter;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import static me.heronerin.gui.GuiElements.createPositionEditor;

// I'll be real, this is a damned mess


public class PositioningTabGui extends ConfigGui.Tab{
    private static class GuiLayer extends GuiRenderLayerEditBase{
        ConfigGui parent;
        // A loose version of origin that typically BUT NOT ALWAYS corresponds to the origin
        private GuiElements.BlockPositionMutable originMut;
        GuiLayer(ConfigGui parent){
            this.parent = parent;
            this.title = parent.getTitleString();
            this.originMut = new GuiElements.BlockPositionMutable();
            if (MainPrinter.getInstance().orgin != null)
                this.originMut.mergeFromBlockPos(MainPrinter.getInstance().orgin);
        }

        @Override
        public void initGui() {
            super.initGui();
            ConfigGui.createButtons(this, this.parent);
            createPositionEditor(this, 30, 50, originMut, new GuiElements.PositionEditCallback() {


                @Override
                public void updatePosition(GuiElements.BlockPositionMutable bp) {
                    originMut = bp;
                    BlockPos maybeOrigin = bp.toBlockPos();
                    MainPrinter.getInstance().origin_preview = null;

                    if (maybeOrigin == null) return;
                    MainPrinter.getInstance().origin_preview = new Pair<>(maybeOrigin, Utils.ORIGIN_PREVIEW);
                    MainPrinter.getInstance().orgin = maybeOrigin;
                    MainPrinter.getInstance().resetRenderBlocks();

                }
            });


        }

        @Override
        protected LayerRange getLayerRange() {

            return null;
        }
    }
    @Override
    public String getTitle() {
        return "Origin Positioning";
    }

    @Override
    public List<GuiConfigsBase.ConfigOptionWrapper> getConfigs() {
        return List.of();
    }
    public void initGui(ConfigGui screen){
        GuiConfigsBase.openGui(new GuiLayer(screen));
    }
}
