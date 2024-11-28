package me.heronerin.mixin;

import me.heronerin.printer.MainPrinter;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Inform printer of updated block

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(at = @At("RETURN"), method = "handleBlockUpdate")
    public void handleBlockUpdate(BlockPos pos, BlockState state, int flags, CallbackInfo ci){
        if (MainPrinter.getInstance().currentSchematicWorld != null)
            MainPrinter.getInstance().currentSchematicWorld.updateBlock(pos, state);
    }

}
