package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.LayerRange;
import me.heronerin.Utils;
import me.heronerin.printer.MainPrinter;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class OriginPositioningTabGui extends ConfigGui.Tab{
    private static class GuiLayer extends GuiRenderLayerEditBase{
        final private PositionEditorGUI.PositionEditCallback callback = (x, y, z) -> {
            BlockPos maybeOrigin = GuiLayer.this.originElement.toBlockPos();
            MainPrinter.getInstance().origin_preview = null;

            if (maybeOrigin == null) return;
            MainPrinter.getInstance().origin_preview = new Pair<>(maybeOrigin, Utils.ORIGIN_PREVIEW);
            MainPrinter.getInstance().orgin = maybeOrigin;
            MainPrinter.getInstance().resetRenderBlocks();
        };

        ConfigGui parent;

        // A loose version of origin that typically BUT NOT ALWAYS corresponds to the origin
        private final PositionEditorGUI originElement;
        GuiLayer(ConfigGui parent){
            this.parent = parent;
            this.title = parent.getTitleString();
            this.originElement = new PositionEditorGUI(null, null, null);
            if (MainPrinter.getInstance().orgin != null)
                this.originElement.formBlockPos(MainPrinter.getInstance().orgin);
        }

        @Override
        public void initGui() {
            super.initGui();
            ConfigGui.createButtons(this, this.parent);
            this.originElement.createPositionEditor(this, 30, 50, callback);
            int y = 50 + this.originElement.getHeight();
            ButtonGeneric roundBtn = new ButtonGeneric(30 - 2 + this.originElement.getLabelWidth(), y + 2, 70 + 16, 16, "Round chunkwise");
            this.addButton(roundBtn, (btn, mb) -> {
                BlockPos bp = this.originElement.toBlockPos();
                if (bp == null) return;
                bp = new BlockPos(
                        bp.getX() / 16 * 16,
                        bp.getY(),
                        bp.getZ() / 16 * 16
                );
                this.originElement.formBlockPos(bp);
                this.originElement.sync();
                callback.updatePosition(bp.getX(), bp.getY(), bp.getZ());

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
