package me.heronerin.printer;

import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import me.heronerin.PseudoRot;
import me.heronerin.Utils;
import me.heronerin.schematic_generators.BaseGenerator;
import me.heronerin.schematic_generators.CompactCactiGen;
import me.heronerin.schematic_generators.FactionCactiFarmGen;
import me.heronerin.schematic_generators.SemiCompactCactiGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPrinter {
    private static final MainPrinter instance = new MainPrinter();
    public static MainPrinter getInstance(){ return instance; }


    // ALL BLOCK ARE RELATIVE TO THE ORIGIN
    public SchematicWorld currentSchematicWorld = null;
    public Pair<BlockPos, Vector4f> origin_preview = null;


    public Map<BlockPos, PrintBlock> blocks = new HashMap<>();

    public BlockPos orgin = null;

    public final List<BaseGenerator> generatorOptions = List.of(
            new FactionCactiFarmGen(),
            new CompactCactiGen(),
            new SemiCompactCactiGen()
    );



//    public void addGrid(BlockPos bp, Block block, int size, boolean isInitialSet){
//        for (int relx = 0; relx < size; relx++){
//            for (int relz =  (relx + (isInitialSet ? 1 : 0)) % 2 ; relz < size; relz+=2){
//                BlockPos relbp = bp.subtract(orgin).add(relx, 0, relz);
//                blocks.put(relbp, new PrintBlock(relbp, block));
//            }
//        }
//        updateAllBlocks();
//    }

    public boolean is_printing = false;
    public static boolean togglePrinter(KeyAction keyAction, IKeybind iKeybind) {
        MainPrinter mp = MainPrinter.getInstance();
        mp.is_printing = !mp.is_printing;
        PseudoRot.do_fake_rotation = mp.is_printing;
        SystemToast.show(
                MinecraftClient.getInstance().getToastManager(),
                SystemToast.Type.TUTORIAL_HINT,
                Text.of("Printer"),
                Text.of("Printer %s!".formatted(mp.is_printing ? "enabled" : "disabled"))
        );

        return true;
    }
}
