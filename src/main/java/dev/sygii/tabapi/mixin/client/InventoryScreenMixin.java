package dev.sygii.tabapi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.sygii.tabapi.api.Tab;
import dev.sygii.tabapi.util.DrawTabHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;

// Could inject into Screen class but imo uneccessary
@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements Tab {

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        DrawTabHelper.onTabButtonClick(client, this, this.x, this.y, mouseX, mouseY, this.focusedSlot != null);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        DrawTabHelper.drawTab(client, context, this, x, y, mouseX, mouseY);
    }
}
