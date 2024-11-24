package me.heronerin;

import net.minecraft.client.MinecraftClient;

// When printing you need to decouple client rotation with server rotation.
// This class is the public controller for this, soo PlayerMoveC2SPacketMixin and PlayerPositionLookS2CPacketMixin
// for how rotation faking is actually done.
public class PseudoRot {
    public static boolean do_fake_rotation = false;
    public static float server_yaw = 0;
    public static float server_pitch = 0;

    public static void start_faking(){
        server_yaw = MinecraftClient.getInstance().player.getYaw();
        server_pitch = MinecraftClient.getInstance().player.getPitch();
        do_fake_rotation = true;
    }
    public static void stop_faking(){
        do_fake_rotation = false;
        MinecraftClient.getInstance().player.setYaw(server_yaw);
        MinecraftClient.getInstance().player.setPitch(server_pitch);
    }

    public static void tick(){}
}
