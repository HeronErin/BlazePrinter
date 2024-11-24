package me.heronerin;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.heronerin.printer.MainPrinter;
import net.fabricmc.api.ClientModInitializer;


import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector4f;

import java.util.ArrayList;

import static me.heronerin.Utils.blockArgumentToPos;


public class BlazePrinter implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(
                        ClientCommandManager.literal("p")
                                // /p clear
                                .then(ClientCommandManager.literal("clear").executes((ignored)->{
                                    MainPrinter.getInstance().blocks.clear();
                                    MainPrinter.getInstance().resetRenderBlocks();
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

                                ))
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
//                                    context.getSource().sendFeedback(Text.literal("Help: Use /p org [x] [y] [z]"));
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
        BlockPos bp = blockArgumentToPos(context.getArgument("position", PosArgument.class));
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
