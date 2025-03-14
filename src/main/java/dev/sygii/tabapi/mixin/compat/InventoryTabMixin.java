package dev.sygii.tabapi.mixin.compat;

import dev.sygii.tabapi.api.InventoryTabAccess;
import net.libz.api.InventoryTab;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Debug(export=true)
@Mixin(InventoryTab.class)
public class InventoryTabMixin implements InventoryTabAccess {

    @Shadow
    private Class<?>[] screenClasses;

    @Override
    public Class<?>[] getScreenClasses() {
        return screenClasses;
    }
}
