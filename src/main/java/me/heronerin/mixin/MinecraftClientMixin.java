package me.heronerin.mixin;

import me.heronerin.BlazePrinter;
import me.heronerin.PseudoRot;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {


    @Inject(at=@At(value="HEAD"), method = "tick")
    void tick(CallbackInfo ci){
        PseudoRot.tick();
    }

    @Inject(method = "onInitFinished", at = @At("HEAD"))
    private void init_minecraft(CallbackInfo ci) {
        BlazePrinter.init();
    }
}
