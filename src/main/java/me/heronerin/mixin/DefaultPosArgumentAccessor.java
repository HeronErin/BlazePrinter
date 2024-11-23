package me.heronerin.mixin;

import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.DefaultPosArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DefaultPosArgument.class)
public interface DefaultPosArgumentAccessor {
    @Accessor
    public CoordinateArgument getX();
    @Accessor
    public CoordinateArgument getY();
    @Accessor
    public CoordinateArgument getZ();
}
