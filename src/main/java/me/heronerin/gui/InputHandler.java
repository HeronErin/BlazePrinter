package me.heronerin.gui;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import me.heronerin.BlazePrinter;

public class InputHandler implements IKeybindProvider {
    private static InputHandler instance = new InputHandler();
    public static InputHandler getInstance(){
        return instance;
    }



    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (IHotkey hotkey : BlazePrinter.HOTKEYS)
            manager.addKeybindToMap(hotkey.getKeybind());

    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(BlazePrinter.MOD_ID, "litematica.hotkeys.category.generic_hotkeys", BlazePrinter.HOTKEYS);
    }

}
