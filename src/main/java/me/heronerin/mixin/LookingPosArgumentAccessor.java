package me.heronerin.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.LookingPosArgument;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LookingPosArgument.class)
public interface LookingPosArgumentAccessor {
    @Accessor
    double getX();
    @Accessor
    double getY();
    @Accessor
    double getZ();

}
