package me.heronerin.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static fi.dy.masa.malilib.gui.GuiBase.COLOR_WHITE;

public class PositionEditorGUI {
    private Integer labelWidth = null;
    Integer[] coords;
    private GuiTextFieldInteger[] fields = null;
    public PositionEditorGUI(
            @Nullable Integer x,
            @Nullable Integer y,
            @Nullable Integer z){
        coords = new Integer[]{x, y, z};
    }

    @Nullable GuiTextFieldInteger xField;

    public interface PositionEditCallback{
        void updatePosition( @Nullable Integer x,
                             @Nullable Integer y,
                             @Nullable Integer z);
    }

    public void createPositionEditor(GuiBase gui, int x, int y, PositionEditCallback positionEditCallback){
        final String[] coords_string = {"X: ", "Y: ", "Z: "};

        fields = new GuiTextFieldInteger[3];
        int wx = 0;
        for (int i = 0; i < coords_string.length; i++) {
            wx = gui.addLabel(x, y + 1 + i*16, -1, 16, COLOR_WHITE, coords_string[i]).getWidth();

            fields[i] = new GuiTextFieldInteger(x + wx, y + i*16, 68, 16, gui.textRenderer);
            fields[i].setText(coords[i] == null ? "" : String.valueOf(coords[i]));

            int finalI = i;
            gui.addTextField(fields[i], new ITextFieldListener() {
                @Override
                public boolean onTextChange(TextFieldWidget textField) {
                    PositionEditorGUI.this.coords[finalI] = textField.getText().isEmpty() ? null : Integer.parseInt(textField.getText());

                    positionEditCallback.updatePosition(PositionEditorGUI.this.coords[0], PositionEditorGUI.this.coords[1], PositionEditorGUI.this.coords[2]);
                    return false;
                }
            });
            gui.addButton(new ButtonGeneric(x + wx + 70, y + i * 16 + 1, Icons.BUTTON_PLUS_MINUS_16, "Left click = minus\nRight click = add"), new IButtonActionListener() {
                @Override
                public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
                    int amount = mouseButton == 1 ? -1 : 1;
                    if (GuiBase.isCtrlDown()) { amount *= 100; }
                    if (GuiBase.isShiftDown()) { amount *= 10; }
                    if (GuiBase.isAltDown()) { amount *= 5; }

                    Integer from = PositionEditorGUI.this.coords[finalI];
                    PositionEditorGUI.this.coords[finalI] = from == null ? null : from + amount;
                    PositionEditorGUI.this.sync();

                    positionEditCallback.updatePosition(PositionEditorGUI.this.coords[0], PositionEditorGUI.this.coords[1], PositionEditorGUI.this.coords[2]);
                }
            });
        }

        ButtonGeneric moveToPlayer = new ButtonGeneric(x + wx - 2, y + 3 * 16 + 2, 70 + 16, 16, "Move to player");
        gui.addButton(moveToPlayer, (button1, mouseButton) -> {
            this.formBlockPos(MinecraftClient.getInstance().player.getBlockPos());
            positionEditCallback.updatePosition(PositionEditorGUI.this.coords[0], PositionEditorGUI.this.coords[1], PositionEditorGUI.this.coords[2]);

        });
        labelWidth = wx;
    }
    public int getHeight(){
        return 16 + 3 * 16 + 2;
    }
    public Integer getLabelWidth(){
        return labelWidth;
    }
    public void formBlockPos(BlockPos bp){
        coords[0] = bp.getX();
        coords[1] = bp.getY();
        coords[2] = bp.getZ();
        this.sync();
    }
    @Nullable
    public BlockPos toBlockPos(){
        if(coords[0] == null || coords[1] == null || coords[2] == null)
            return null;
        return new BlockPos(coords[0], coords[1], coords[2]);
    }
    public void sync(){
        if (fields != null)
            for (int i = 0; i < fields.length; i++)
                fields[i].setText(coords[i] == null ? "" : ""+coords[i]);

    }
}
