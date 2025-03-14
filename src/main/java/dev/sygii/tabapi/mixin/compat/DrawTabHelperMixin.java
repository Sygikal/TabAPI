package dev.sygii.tabapi.mixin.compat;

import net.libz.util.DrawTabHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(DrawTabHelper.class)
public class DrawTabHelperMixin {

    @Inject(method = "drawTab", at = @At("HEAD"), cancellable = true, remap = false)
    private static void draw(MinecraftClient client, DrawContext context, Screen screenClass, int x, int y, int mouseX, int mouseY, CallbackInfo ci) {
        dev.sygii.tabapi.util.DrawTabHelper.drawTab(client, context, screenClass, x, y, mouseX, mouseY);
        //ci.cancel();
    }

    @Inject(method = "onTabButtonClick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void click(MinecraftClient client, Screen screenClass, int x, int y, double mouseX, double mouseY, boolean focused, CallbackInfo ci) {
        dev.sygii.tabapi.util.DrawTabHelper.onTabButtonClick(client, screenClass, x, y, mouseX, mouseY, focused);
        //ci.cancel();
    }
}
