package me.heronerin.mixin;

import me.heronerin.PseudoRot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin {
    @Shadow @Final protected double x;
    @Shadow @Final protected double y;
    @Shadow @Final protected double z;
    @Shadow @Final protected float yaw;
    @Shadow @Final protected float pitch;
    @Shadow @Final protected boolean onGround;
    @Shadow @Final protected boolean changePosition;
    @Shadow @Final protected boolean changeLook;

    @Mixin(PlayerMoveC2SPacket.Full.class)
    public static class FullMixin extends PlayerMoveC2SPacketMixin{
        @Inject(at = @At(value = "HEAD"), method = "write", cancellable = true)
        public void write(PacketByteBuf buf, CallbackInfo ci){
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            buf.writeFloat(PseudoRot.do_fake_rotation ? PseudoRot.server_yaw :  this.yaw);
            buf.writeFloat(PseudoRot.do_fake_rotation ? PseudoRot.server_pitch :  this.pitch);
            buf.writeByte(this.onGround ? 1 : 0);
            ci.cancel();
        }
    }

    @Mixin(PlayerMoveC2SPacket.LookAndOnGround.class)
    public static class LookAndOnGroundMixin extends PlayerMoveC2SPacketMixin{
        @Inject(at = @At(value = "HEAD"), method = "write", cancellable = true)
        public void write(PacketByteBuf buf, CallbackInfo ci){
            buf.writeFloat(PseudoRot.do_fake_rotation ? PseudoRot.server_yaw :  this.yaw);
            buf.writeFloat(PseudoRot.do_fake_rotation ? PseudoRot.server_pitch :  this.pitch);
            buf.writeByte(this.onGround ? 1 : 0);
            ci.cancel();
        }
    }

}
