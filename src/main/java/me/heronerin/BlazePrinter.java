package me.heronerin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import me.heronerin.gui.ConfigGui;
import me.heronerin.gui.InputHandler;
import me.heronerin.printer.MainPrinter;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.io.File;
import java.util.List;


public class BlazePrinter implements IConfigHandler {

    public static final ConfigHotkey OPEN_CONFIG = new ConfigHotkey("Open menu", "O", "Opens this menu");
    public static final ConfigHotkey TOGGLE_PRINT = new ConfigHotkey("Toggle printer", "P,O", "Toggles printer");
    public static final ConfigBoolean PREVIEW_ORIGIN = new ConfigBoolean("Preview Origin", true, "Show an orange box at the origin");
    public static final ConfigBoolean PREVIEWS = new ConfigBoolean("Do Preview", true, "If disabled disables all preview rendering");

    public static final ConfigDouble X_ROT = new ConfigDouble("X ROT", 0, -180, 180, true, "");
    public static final ConfigDouble Y_ROT = new ConfigDouble("Y ROT", 0, -180, 180, true, "");
    public static final ConfigDouble Z_ROT = new ConfigDouble("Z ROT", 0, -180, 180, true, "");

    public static ImmutableList<ConfigHotkey> HOTKEYS = ImmutableList.of(
            OPEN_CONFIG,
            TOGGLE_PRINT
    );
    public static List<ConfigBase> MISC = List.of(
            PREVIEW_ORIGIN,
            PREVIEWS,

            X_ROT,
            Y_ROT,
            Z_ROT
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
                ConfigUtils.readConfigBase(root, "Generic misc", MISC);
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



    public static void init(){
        OPEN_CONFIG.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (MinecraftClient.getInstance().world != null)
                MinecraftClient.getInstance().setScreen(new ConfigGui());
            return MinecraftClient.getInstance().world != null;
        });
        TOGGLE_PRINT.getKeybind().setCallback(MainPrinter::togglePrinter);
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            ConfigManager.getInstance().registerConfigHandler(MOD_ID, new BlazePrinter());
            InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        });
    }



}
