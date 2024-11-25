package me.heronerin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.FileUtils;


import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.io.File;


public class BlazePrinter implements IConfigHandler {

    public static final ConfigHotkey OPEN_CONFIG = new ConfigHotkey("Open menu", "O", "Opens this menu");
    public static final ConfigHotkey TOGGLE_PRINT = new ConfigHotkey("Toggle printer", "P,O", "Toggles printer");

    public static ImmutableList<ConfigHotkey> HOTKEYS = ImmutableList.of(
            OPEN_CONFIG,
            TOGGLE_PRINT
    );

    private final static String CONFIG = "blaze_printer.json";
    @Override
    public void load() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG);
        System.out.println("LOAD: " + configFile);
        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", HOTKEYS);
            }
        }

    }

    @Override
    public void save() {
        File dir = FileUtils.getConfigDirectory();
        System.out.println("SAVE: " + dir);
        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", HOTKEYS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG));
        }
    }
    public static final String MOD_ID = "blaze-printer";



}
