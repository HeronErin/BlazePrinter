package me.heronerin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.joml.Vector4f;

import java.util.Map;

public class Utils {
    public static Map<Block, Vector4f> block_to_vec = Map.of(
            Blocks.CACTUS, new Vector4f(0, 1, 0, 0.5f),
            Blocks.TRIPWIRE, new Vector4f(211, 211, 211, 0.5f)
    );
    public static final Vector4f WRONG_COLOR = new Vector4f(0.8f, 0, 0, 0.5f);
    public static final Vector4f ORIGIN_PREVIEW = new Vector4f(255/256f, 215/256f, 0, 0.5f);



    public static Vector4f block_to_color(Block b){
        return block_to_vec.getOrDefault(b, new Vector4f(0, 0, 0, 0.5f));
    }



    public interface OnDropdownMenuChangeCallback {
        void onSelectionChange(int index);
    }
    private static OnDropdownMenuChangeCallback stashedDropdownMenuChangeCallback = null;
    public static synchronized OnDropdownMenuChangeCallback getAndResetStashedDropdownMenuCallback(){
        OnDropdownMenuChangeCallback s = stashedDropdownMenuChangeCallback;
        stashedDropdownMenuChangeCallback = null;
        return s;
    }
    public static synchronized void setStashedDropdownMenuCallback(OnDropdownMenuChangeCallback osci){
        stashedDropdownMenuChangeCallback = osci;
    }


}
