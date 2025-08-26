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
        TabAPI.LOGGER.info("Mapping {} tab from LibZ to TabAPI for {}", tab.getTitle(), ((InventoryTabAccess) (tab)).getScreenClasses());
        TabAPI.registerInventoryTab(new LibZTab(tab.getTitle(), tab.getTexture(), tab.getPreferedPos(), tab::onClick, tab::getItemStack, tab::shouldShow, ((InventoryTabAccess)(tab)).getScreenClasses()));
        ci.cancel();
    }

    @Inject(method = "registerOtherTab", at=@At("HEAD"), remap = false, cancellable = true)
    private static void injectOther(InventoryTab tab, Class<?> parentClass, CallbackInfo ci) {
        TabAPI.LOGGER.info("Mapping {} other tab from LibZ to TabAPI for {}", tab.getTitle(), ((InventoryTabAccess) (tab)).getScreenClasses());
        TabAPI.registerOtherTab(new LibZTab(tab.getTitle(), tab.getTexture(), tab.getPreferedPos(), tab::onClick, tab::getItemStack, tab::shouldShow, ((InventoryTabAccess)(tab)).getScreenClasses()), parentClass);
        ci.cancel();
    }
}
