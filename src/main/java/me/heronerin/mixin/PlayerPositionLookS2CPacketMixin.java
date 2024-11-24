package me.heronerin.mixin;

import me.heronerin.PseudoRot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(PlayerPositionLookS2CPacket.class)
public class PlayerPositionLookS2CPacketMixin {
     @Final @Shadow private float yaw;
     @Final @Shadow private float pitch;


    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At("RETURN"))
    public void PlayerPositionLookS2CPacket(PacketByteBuf buf, CallbackInfo ci) {
        PseudoRot.server_pitch = this.pitch;
        PseudoRot.server_yaw= this.yaw;
    }

    
}
