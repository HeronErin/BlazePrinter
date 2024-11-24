package me.heronerin;

import me.heronerin.mixin.DefaultPosArgumentAccessor;
import me.heronerin.mixin.LookingPosArgumentAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4d;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<Block, Vector4f> block_to_vec = Map.of(
            Blocks.CACTUS, new Vector4f(0, 1, 0, 0.5f),
            Blocks.TRIPWIRE, new Vector4f(211, 211, 211, 0.5f)
    );
    public static final Vector4f WRONG_COLOR = new Vector4f(0.8f, 0, 0, 0.5f);
    public static final Vector4f ORIGIN_PREVIEW = new Vector4f(255/256f, 215/256f, 0, 0.5f);



    // Client side argument conversion. This is needed as normal classes expect this to be done server side
    public static @Nullable BlockPos blockArgumentToPos(@Nullable PosArgument dfa){
        if (dfa == null) return null;
        if (dfa instanceof DefaultPosArgument) {
            // Unfortunately minecraft only expects this conversion to be done server side
            DefaultPosArgumentAccessor acc = (DefaultPosArgumentAccessor) dfa;
            PlayerEntity p = MinecraftClient.getInstance().player;

            return BlockPos.ofFloored(new Vec3d(
                    acc.getX().toAbsoluteCoordinate(p.getX()),
                    acc.getY().toAbsoluteCoordinate(p.getY()),
                    acc.getZ().toAbsoluteCoordinate(p.getZ())
            ));
        }else{
            return BlockPos.ofFloored(Utils.toAbsolutePos((LookingPosArgumentAccessor) dfa));

        }
    }
    // Adapted from net.minecraft.command.argument.LookingPosArgument.toAbsolutePos(ServerCommandSource source)
    public static Vec3d toAbsolutePos(LookingPosArgumentAccessor lookingPos) {
        // Fetch client player rotation and position at the eyes
        Vec2f rotation = MinecraftClient.getInstance().player.getRotationClient(); // Equivalent to source.getRotation()
        Vec3d position = EntityAnchorArgumentType.EntityAnchor.FEET.positionAt(MinecraftClient.getInstance().player); // Equivalent to source.getEntityAnchor().positionAt(source)
        // Precompute trigonometric values based on the rotation
        float f = MathHelper.cos((rotation.y + 90.0F) * 0.017453292F); // yaw + 90 in radians
        float g = MathHelper.sin((rotation.y + 90.0F) * 0.017453292F);
        float h = MathHelper.cos(-rotation.x * 0.017453292F);           // pitch in radians
        float i = MathHelper.sin(-rotation.x * 0.017453292F);
        float j = MathHelper.cos((-rotation.x + 90.0F) * 0.017453292F); // pitch + 90 in radians
        float k = MathHelper.sin((-rotation.x + 90.0F) * 0.017453292F);

        // Forward vector
        Vec3d vec3d2 = new Vec3d(f * h, i, g * h); // Direction vector

        // Up vector
        Vec3d vec3d3 = new Vec3d(f * j, k, g * j); // Adjusted up direction

        // Cross product for the right vector
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0); // Adjust for coordinate system

        // Transform offsets using the basis vectors
        double d = vec3d2.x * lookingPos.getZ() + vec3d3.x * lookingPos.getY() + vec3d4.x * lookingPos.getX();
        double e = vec3d2.y * lookingPos.getZ() + vec3d3.y * lookingPos.getY() + vec3d4.y * lookingPos.getX();
        double l = vec3d2.z * lookingPos.getZ() + vec3d3.z * lookingPos.getY() + vec3d4.z * lookingPos.getX();

        // Final world position
        return new Vec3d(position.x + d, position.y + e, position.z + l);
    }


    public static Vector4f block_to_color(Block b){
        return block_to_vec.getOrDefault(b, new Vector4f(0, 0, 0, 0.5f));
    }

    @SuppressWarnings("unchecked")
    public static CommandRegistryAccess getCommandLineRegFromReg(DefaultedRegistry<?> reg){
        return new CommandRegistryAccess() {
            @Override
            public <T> RegistryWrapper<T> createWrapper(RegistryKey<? extends Registry<T>> registryRef) {
                return (RegistryWrapper<T>) reg.getReadOnlyWrapper();
            }
        };
    }

}
