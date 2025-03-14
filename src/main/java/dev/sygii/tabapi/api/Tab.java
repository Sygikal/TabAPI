package dev.sygii.tabapi.api;

import dev.sygii.tabapi.util.DrawTabHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to get implemented by ScreenHandlers which will have a tab.
 *
 * @version 1.0
 */
public interface Tab {

    /**
     * Method to get overriden of the existing screen while returning this.getClass() and overriden by the new screen accessible via the tab return the parent class.
     * 
     * @return the parent screen class
     */

    default void render(DrawContext context, int mouseX, int mouseY, float delta, MinecraftClient client, int x, int y, Screen parent) {
        DrawTabHelper.drawTab(client, context, parent, x, y, mouseX, mouseY);
    }

    @Nullable
    default Class<?> getParent() {
        return null;
    }
}
