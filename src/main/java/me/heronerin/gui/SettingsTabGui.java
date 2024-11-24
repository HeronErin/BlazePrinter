package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import me.heronerin.BlazePrinter;

import java.util.List;

public class SettingsTabGui extends ConfigGui.Tab {
    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public List<GuiConfigsBase.ConfigOptionWrapper> getConfigs() {

        return GuiConfigsBase.ConfigOptionWrapper.createFor(BlazePrinter.HOTKEYS);
    }

}
