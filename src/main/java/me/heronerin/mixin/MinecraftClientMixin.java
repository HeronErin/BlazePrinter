package me.heronerin.mixin;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import me.heronerin.BlazePrinter;
import me.heronerin.PseudoRot;
import me.heronerin.gui.ConfigGui;
import me.heronerin.gui.InputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.heronerin.BlazePrinter.MOD_ID;
import static me.heronerin.BlazePrinter.OPEN_CONFIG;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {


    @Inject(at=@At(value="HEAD"), method = "tick")
    void tick(CallbackInfo ci){
        PseudoRot.tick();
    }

    @Inject(method = "onInitFinished", at = @At("HEAD"))
    private void init_minecraft(CallbackInfo ci) {
        OPEN_CONFIG.getKeybind().setCallback((keyAction, iKeybind) -> {
            System.out.println("Open sesame!");
            if (MinecraftClient.getInstance().world != null)
                MinecraftClient.getInstance().setScreen(new ConfigGui());
            return MinecraftClient.getInstance().world != null;
        });
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            System.out.println("INIT sesame!");
            ConfigManager.getInstance().registerConfigHandler(MOD_ID, new BlazePrinter());
            InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        });
    }
}
