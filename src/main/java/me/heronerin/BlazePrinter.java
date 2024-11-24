package me.heronerin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.util.JsonUtils;
import me.heronerin.gui.ConfigGui;
import me.heronerin.gui.InputHandler;
import me.heronerin.printer.MainPrinter;
import fi.dy.masa.malilib.util.FileUtils;
import net.fabricmc.api.ClientModInitializer;


import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.*;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.io.File;

import static me.heronerin.Utils.blockArgumentToPos;


public class BlazePrinter implements ClientModInitializer, IConfigHandler {

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

    @Override
    public void onInitializeClient() {
        OPEN_CONFIG.getKeybind().setCallback((keyAction, iKeybind) -> {
            System.out.println("OPEN SESAME!!");
            if (MinecraftClient.getInstance().world != null)
                MinecraftClient.getInstance().setScreen(new ConfigGui());
            return MinecraftClient.getInstance().world != null;
        });
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            ConfigManager.getInstance().registerConfigHandler(MOD_ID, this);

            InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());

        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(
                    ClientCommandManager.literal("p")
                            // /p clear
                            .then(ClientCommandManager.literal("clear").executes(context->{
                                MainPrinter.getInstance().blocks.clear();
                                MainPrinter.getInstance().resetRenderBlocks();
                                context.getSource().sendFeedback(Text.of("Cleared printer"));
                                return 0;
                            }))
                            .then(ClientCommandManager.literal("on").executes(context->{
                                PseudoRot.start_faking();
                                context.getSource().sendFeedback(Text.of("Enabled printer"));
                                return 0;
                            }))
                            .then(ClientCommandManager.literal("off").executes(context->{
                                PseudoRot.stop_faking();
                                context.getSource().sendFeedback(Text.of("Disabled printer"));
                                return 0;
                            }))
                            // /p grid ~ ~ ~ minecraft:stone true
                            .then(ClientCommandManager.literal("grid").then(
                                ClientCommandManager.argument("position", BlockPosArgumentType.blockPos()).then(
                                    ClientCommandManager.argument("block", BlockStateArgumentType.blockState(Utils.getCommandLineRegFromReg(Registries.BLOCK)))
                                            .then(
                                                    ClientCommandManager.argument("size", IntegerArgumentType.integer(2)).executes(BlazePrinter::grid)
                                                            .then(ClientCommandManager.argument("invert", BoolArgumentType.bool()).executes(BlazePrinter::grid))
                                            )
                                )

                            ).then(
                                ClientCommandManager.literal("org").then(
                                        ClientCommandManager.argument("block", BlockStateArgumentType.blockState(Utils.getCommandLineRegFromReg(Registries.BLOCK)))
                                                .then(
                                                        ClientCommandManager.argument("size", IntegerArgumentType.integer(2)).executes(BlazePrinter::grid)
                                                                .then(ClientCommandManager.argument("invert", BoolArgumentType.bool()).executes(BlazePrinter::grid))
                                                )
                                )
                                    )
                            )
                            .then(

                                    ClientCommandManager.literal("org")
                                            .then(
                                                    // p org round
                                                    ClientCommandManager.literal("round").executes(BlazePrinter::roundorg)
                                            )
                                            .then(
                                                    // p org ~ ~ ~
                                                    ClientCommandManager.argument("position", BlockPosArgumentType.blockPos())
                                                            .executes(BlazePrinter::setorg)

                                            )
                                            .executes(BlazePrinter::setorg) // Executes when no arguments are provided after "org"
                            )
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.literal("""
                                    BlazePrinter Help:
                                    /p - Displays this menu
                                    /p clear - Clears all blocks and resets render blocks.
                                    /p grid <position> <block> <size> [invert] - Creates a grid starting at <position> with <block>, of <size> dimensions, optionally inverted.
                                    /p grid org <block> <size> [invert] - Creates an origin-centered grid with <block>, of <size> dimensions, optionally inverted.
                                    /p org round - Sets the origin to the nearest block position (rounded).
                                    /p org <position> - Sets the origin to the specified <position>."""));
                                return 1;
                            })
            )
        );


    }
    private static int grid(CommandContext<FabricClientCommandSource> context){
        if (MainPrinter.getInstance().orgin == null){
            context.getSource().sendError(Text.of("Must have origin set before grid can be set"));
            return 1;
        }
        BlockPos bp = MainPrinter.getInstance().orgin;
        try{
            bp = blockArgumentToPos(context.getArgument("position", PosArgument.class));
        }catch (IllegalArgumentException ignored){}

        BlockState bs = context.getArgument("block", BlockStateArgument.class).getBlockState();
        Integer size = context.getArgument("size", Integer.class);
        Boolean doInvert = false;
        try{
            doInvert = context.getArgument("invert", Boolean.class);
        } catch (IllegalArgumentException ignored){}

        MainPrinter.getInstance().addGrid(bp, bs.getBlock(), size, doInvert);

        return 0;
    }
    private static int roundorg(CommandContext<FabricClientCommandSource> context){
        return 0;
    }
    private static int setorg(CommandContext<FabricClientCommandSource> context) {
        PosArgument pos = null;
        try {
            pos = context.getArgument("position", PosArgument.class);

        } catch (IllegalArgumentException ignored){}

        BlockPos bp = pos != null ? blockArgumentToPos(pos) : BlockPos.ofFloored(MinecraftClient.getInstance().player.getPos());

        MainPrinter.getInstance().orgin = bp;
        MainPrinter.getInstance().resetRenderBlocks();

        return 0;
    }


}
