package me.heronerin.mixin;

import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import me.heronerin.Utils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


// Ok, there is an explanation of this I swear!!!
// I just wanted a callback...

// Usage: Use Utils.setStashedDropdownMenuCallback() to set the callback function
//        then create a WidgetDropDownListMixin like normal. Now the callback is set

@Mixin(value = WidgetDropDownList.class, remap = false)
public abstract class WidgetDropDownListMixin<T extends Object>{
    @Shadow @Nullable
    protected T selectedEntry;

    @Shadow @Final protected List<T> entries;


    public Utils.OnDropdownMenuChangeCallback onDropdownMenuChangeCallback = null;
    @Inject(at = @At(value = "RETURN"), method = "<init>*")
    public void initMixin(CallbackInfo ci){
        onDropdownMenuChangeCallback = Utils.getAndResetStashedDropdownMenuCallback();

    }

    @Inject(at = @At(value = "RETURN"), method = "setSelectedEntry*")
    public void setSelectedEntry(CallbackInfo ci) {
        if (onDropdownMenuChangeCallback != null)
            onDropdownMenuChangeCallback.onSelectionChange(this.entries.indexOf(this.selectedEntry));
    }
}
