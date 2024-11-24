package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

// I'll be real, this is a damned mess

public class GuiElements {
    public static class BlockPositionMutable{
        @Nullable Integer x;
        @Nullable Integer y;
        @Nullable Integer z;
        @Nullable BlockPos toBlockPos(){
            if (x == null || y == null || z == null) return null;
            return new BlockPos(x, y, z);
        }
        void mergeFromBlockPos(BlockPos bp){
            this.x = bp.getX();
            this.y = bp.getY();
            this.z = bp.getZ();
        }
    }
    public interface PositionEditCallback{
        void updatePosition(BlockPositionMutable bp);
    }

    public static void createPositionEditor(GuiBase gui, int x, int y, BlockPositionMutable initial, PositionEditCallback positionEditCallback){
        final String[] coords = {"X: ", "Y: ", "Z: "};

        String[] coord =  {
                initial.x != null ? String.valueOf(initial.x) : "",
                initial.y != null ? String.valueOf(initial.y) : "",
                initial.z != null ? String.valueOf(initial.z) : ""
        };
        GuiTextFieldGeneric[] coordFields = new GuiTextFieldGeneric[3];
        int wx = 0;
        for (int i = 0; i < coords.length; i++) {
            wx = gui.addLabel(x, y + 1 + i*16, -1, 16, 0xFFFFAA00, coords[i]).getWidth();

            coordFields[i] = new GuiTextFieldInteger(x + wx, y + i*16, 68, 16, gui.textRenderer);
            coordFields[i].setText(String.valueOf(coord[i]));

            int finalI = i;
            gui.addTextField(coordFields[i], new ITextFieldListener() {
                @Override
                public boolean onTextChange(TextFieldWidget textField) {
                    coord[finalI] = textField.getText();
                    initial.x = coord[0].isEmpty() ? null : Integer.parseInt(coord[0]);
                    initial.y = coord[1].isEmpty() ? null : Integer.parseInt(coord[1]);
                    initial.z = coord[2].isEmpty() ? null : Integer.parseInt(coord[2]);
                    positionEditCallback.updatePosition(initial);
                    return false;
                }
            });
            gui.addButton(new ButtonGeneric(x + wx + 70, y + i * 16 + 1, Icons.BUTTON_PLUS_MINUS_16, "Left click = minus\nRight click = add"), new IButtonActionListener() {
                @Override
                public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
                    if (coord[finalI].isEmpty()) return;

                    int amount = mouseButton == 1 ? -1 : 1;
                    if (GuiBase.isCtrlDown()) { amount *= 100; }
                    if (GuiBase.isShiftDown()) { amount *= 10; }
                    if (GuiBase.isAltDown()) { amount *= 5; }

                    coord[finalI] = String.valueOf(Integer.parseInt(coord[finalI]) + amount);
                    coordFields[finalI].setText(coord[finalI]);

                    initial.x = coord[0].isEmpty() ? null : Integer.parseInt(coord[0]);
                    initial.y = coord[1].isEmpty() ? null : Integer.parseInt(coord[1]);
                    initial.z = coord[2].isEmpty() ? null : Integer.parseInt(coord[2]);
                    positionEditCallback.updatePosition(initial);
                }
            });
        }

        ButtonGeneric button = new ButtonGeneric(x + wx - 2, y + 3 * 16, 70 + 16, 16, "Move to player");
        gui.addButton(button, (button1, mouseButton) -> {
            initial.mergeFromBlockPos(MinecraftClient.getInstance().player.getBlockPos());
            coord[0] = "" + initial.x;
            coord[1] = "" + initial.y;
            coord[2] = "" + initial.z;
            coordFields[0].setText(coord[0]);
            coordFields[1].setText(coord[1]);
            coordFields[2].setText(coord[2]);
            positionEditCallback.updatePosition(initial);
        });
    }
}
