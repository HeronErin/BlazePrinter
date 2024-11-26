package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.List;

import static me.heronerin.BlazePrinter.MOD_ID;

public class ConfigGui extends GuiConfigsBase {
    public static abstract class Tab {
        public abstract String getTitle();
        public abstract List<ConfigOptionWrapper> getConfigs();
        public void initGui(ConfigGui screen){};
    }

    public static void createButtons(GuiBase gui, ConfigGui config) {
        int x = 10;
        for (Tab t : TABS)
            x += createButton(gui, config, x, 26, -1, t);
    }
    public static int createButton(GuiBase gui, ConfigGui config, int x, int y, int width, final Tab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getTitle());
        button.setEnabled(tab != config.current_tab);
        gui.addButton(button, (ignored1, ignored2) -> {
            config.current_tab = tab;
            config.reCreateListWidget(); // apply the new config width
            config.getListWidget().resetScrollbarPosition();
            config.initGui();
            if (gui != config)
                GuiBase.openGui(config);
        });

        return button.getWidth() + 2;
    }
    private static final ImmutableList<? extends Tab> TABS = ImmutableList.of(
            new SettingsTabGui(),
            new OriginPositioningTabGui(),
            new SchematicGUI()
    );
    private Tab current_tab = TABS.get(0);

    public ConfigGui() {
        super(10, 50, MOD_ID, null, "Blaze Printer");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        createButtons(this, this);
        current_tab.initGui(this);
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return current_tab.getConfigs();
    }
}
