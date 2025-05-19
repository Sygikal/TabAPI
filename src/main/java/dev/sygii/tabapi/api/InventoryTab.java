package dev.sygii.tabapi.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * InventoryTab class to be extended to create a new tab for a screen.
 * 
 * <p>
 * Register with the TabRegistry.
 *
 * @version 1.0
 */
@Environment(EnvType.CLIENT)
public class InventoryTab {

    private final Class<?>[] screenClasses;
    @Nullable
    private final Identifier id;
    private final Text title;
    @Nullable
    private final Identifier texture;
    private final int preferedPos;
    private final boolean customTexture;

    /**
     * Constructor to create a new inventory tab.
     * 
     * @param title         Text to be rendered on tab hover.
     * @param texture       Identifier of the tab icon texture which has a size of 14x14.
     * @param preferedPos   Number of the prefered position. 0 = far left.
     * @param screenClasses Screen class list of the inventory tab screen.
     */

    public InventoryTab(@Nullable Identifier id, Text title, @Nullable Identifier texture, int preferedPos, Class<?>... screenClasses) {
        this(id, title, texture, preferedPos, false, screenClasses);
    }

    public InventoryTab(@Nullable Identifier id, Text title, @Nullable Identifier texture, int preferedPos, boolean customTexture, Class<?>... screenClasses) {
        this.id = id;
        this.screenClasses = screenClasses;
        this.title = title;
        this.texture = texture;
        this.preferedPos = preferedPos;
        this.customTexture = customTexture;
    }

    @Nullable
    public Identifier getID() {
        return this.id;
    }

    public Text getTitle() {
        return this.title;
    }

    @Nullable
    public Identifier getTexture() {
        return this.texture;
    }

    public boolean isCustomTexture() {
        return this.customTexture;
    }

    public void customRender(DrawContext context, int x, int y, int mouseX, int mouseY) {

    }

    @Nullable
    public ItemStack getItemStack(MinecraftClient client) {
        return null;
    }

    public int getPreferedPos() {
        return this.preferedPos;
    }

    public boolean shouldShow(MinecraftClient client) {
        return true;
    }

    public void onClick(MinecraftClient client) {
    }

    public boolean isSelected(Screen screen) {
        return this.isSelectedScreen(screen.getClass());
    }

    public boolean canClick(Screen screen, MinecraftClient client) {
        return !isSelectedScreen(screen.getClass());
    }

    public Class<?>[] getScreenClasses() {
        return screenClasses;
    }

    public boolean isSelectedScreen(Class<?> screenClass) {
        for (int i = 0; i < screenClasses.length; i++) {
            if (screenClasses[i].equals(screenClass)) {
                return true;
            }
        }
        return false;
    }

}
