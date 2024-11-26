package me.heronerin.gui;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import me.heronerin.BlazePrinter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsTabGui extends ConfigGui.Tab {
    @Override
    public String getTitle() {
        return "Settings";
    }

    private final List<ConfigBase> TOTAL_SETTINGS = Streams.concat(BlazePrinter.HOTKEYS.stream(), BlazePrinter.MISC.stream()).toList();
    @Override
    public List<GuiConfigsBase.ConfigOptionWrapper> getConfigs() {
        return GuiConfigsBase.ConfigOptionWrapper.createFor(TOTAL_SETTINGS);
    }

}
