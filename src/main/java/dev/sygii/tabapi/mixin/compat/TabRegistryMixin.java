package dev.sygii.tabapi.mixin.compat;

import dev.sygii.tabapi.TabAPI;
import dev.sygii.tabapi.api.InventoryTabAccess;
import dev.sygii.tabapi.api.LibZTab;
import net.libz.api.InventoryTab;
import net.libz.registry.TabRegistry;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Debug(export=true)
@Mixin(TabRegistry.class)
public class TabRegistryMixin {

    @Inject(method = "registerInventoryTab", at=@At("HEAD"), remap = false, cancellable = true)
    private static void inject(InventoryTab tab, CallbackInfo ci) {
        System.out.println(Arrays.toString(((InventoryTabAccess) (tab)).getScreenClasses()));
        TabAPI.registerInventoryTab(new LibZTab(tab.getTitle(), tab.getTexture(), tab.getPreferedPos(), tab::onClick, ((InventoryTabAccess)(tab)).getScreenClasses()));
        ci.cancel();
    }
}
