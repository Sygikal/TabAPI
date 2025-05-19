package dev.sygii.tabapi.util;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.sygii.tabapi.TabAPI;
import dev.sygii.tabapi.api.InventoryTab;
import dev.sygii.tabapi.api.Tab;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class DrawTabHelper {

    /**
     * Draw a tab on top of a screen.
     * 
     * <p>
     * Required to call on client only screens. Not required on handled screens.
     *
     * @param client      A MinecraftClient instance.
     * @param context     The DrawContext of the render method.
     * @param screenClass The screen class (not the parent).
     * @param x           The left position of the screen.
     * @param y           The top position of the screen.
     * @param mouseX      The x mouse position.
     * @param mouseY      The y mouse position.
     */
    public static void drawTab(MinecraftClient client, DrawContext context, Screen screenClass, int x, int y, int mouseX, int mouseY) {
        //if (!TabAPI.CLIENT.renderTabs) return;
        if (client != null && client.player != null /*&& ConfigInit.CONFIG.inventoryButton*/ && ((Object) screenClass instanceof Tab || (FabricLoader.getInstance().isModLoaded("libz") && screenClass instanceof net.libz.api.Tab))) {
            Class<?> parent = null;
            if ((Object) screenClass instanceof Tab tabAPITab) {
                parent = tabAPITab.getParent();
            } else if (screenClass instanceof net.libz.api.Tab libZTab) {
                parent = libZTab.getParentScreenClass();
            }
            int xPos = x;
            Text shownTooltip = null;

            List<InventoryTab> list = null;
            if (parent != null) {
                if (TabAPI.otherTabs.isEmpty() || !TabAPI.otherTabs.containsKey(parent)) {
                    return;
                }
                list = TabAPI.otherTabs.get(parent);
            } else {
                list = TabAPI.inventoryTabs;
            }
            if (list != null) {
                int real = 0;
                for (InventoryTab inventoryTab : list) {
                    if (inventoryTab.shouldShow(client) && (inventoryTab.getID() != null && TabAPI.config.tabs.get(inventoryTab.getID().toString()).booleanValue())) {

                        boolean isFirstTab = real == 0;
                        //boolean isSelectedTab = inventoryTab.isSelectedScreen(screenClass.getClass());
                        boolean isSelectedTab = inventoryTab.isSelected(screenClass);

                        int textureX = isFirstTab ? 24 : 72;
                        if (isSelectedTab) {
                            textureX -= 24;
                        }

                        context.drawTexture(TabAPI.tabTexture, xPos, isSelectedTab ? y - 23 : y - 21, textureX, 0, 24, isSelectedTab ? 27 : isFirstTab ? 25 : 21);
                        if (inventoryTab.getTexture() != null) {
                            context.drawTexture(inventoryTab.getTexture(), xPos + 5, y - 16, 0, 0, 14, 14, 14, 14);
                        } else if (inventoryTab.getItemStack(client) != null) {
                            context.drawItem(inventoryTab.getItemStack(client), xPos + 4, y - 17);
                        }

                        if (!isSelectedTab && isPointWithinBounds(x, y, xPos - x + 1, -20, 22, 19, (double) mouseX, (double) mouseY)) {
                            shownTooltip = inventoryTab.getTitle();
                        }
                        xPos += 25;
                        real++;
                    }
                }
            }
            if (!TabAPI.sideInventoryTabs.isEmpty()) {
                int yPos = y;
                int real = 0;
                for (InventoryTab inventoryTab : TabAPI.sideInventoryTabs) {
                    if (inventoryTab.shouldShow(client) && (inventoryTab.getID() != null && TabAPI.config.tabs.get(inventoryTab.getID().toString()).booleanValue())) {

                        boolean isFirstTab = real == 0;
                        //boolean isSelectedTab = inventoryTab.isSelectedScreen(screenClass.getClass());
                        boolean isSelectedTab = inventoryTab.isSelected(screenClass);

                        int textureY = isFirstTab ? 51 : 99;
                        if (isSelectedTab) {
                            textureY -= 24;
                        }
                        /*MatrixStack matrix = context.getMatrices();
                        matrix.push();
                        matrix.translate(x - 21, y, 0f);
                        Quaternionf quaternion = new Quaternionf().rotateZ((float)Math.toRadians(270));
                        matrix.multiply(quaternion);
                        matrix.translate(-(x - 21), -(y), 0f);*/

                        context.drawTexture(TabAPI.tabTexture, isSelectedTab ? x - 23 : x - 21, yPos, 0, textureY, isSelectedTab ? 27 : isFirstTab ? 25 : 21, 24);
                        //matrix.pop();
                        if (inventoryTab.isCustomTexture()) {
                            inventoryTab.customRender(context, x - 16, yPos + 5, mouseX, mouseY);
                        }else if (inventoryTab.getTexture() != null) {
                            context.drawTexture(inventoryTab.getTexture(), x - 16, yPos + 5, 0, 0, 14, 14, 14, 14);
                        } else if (inventoryTab.getItemStack(client) != null) {
                            context.drawItem(inventoryTab.getItemStack(client), x - 17, yPos + 4);
                        }

                        if (!isSelectedTab && isPointWithinBounds(x, y, -20, yPos - y + 1, 22, 19, (double) mouseX, (double) mouseY)) {
                            shownTooltip = inventoryTab.getTitle();
                        }
                        yPos += 25;
                        real++;
                    }
                }
            }
            if (shownTooltip != null) {
                context.drawTooltip(client.textRenderer, shownTooltip, mouseX, mouseY);
            }
        }
    }

    /**
     * Tab button click method. Call it at mouseClicked method.
     * 
     * <p>
     * Required to call on client only screens. Not required on handled screens.
     *
     * @param client      A MinecraftClient instance.
     * @param screenClass The screen class (not the parent).
     * @param x           The left position of the screen.
     * @param y           The top position of the screen.
     * @param mouseX      The x mouse position.
     * @param mouseY      The y mouse position.
     * @param focused     If another child is focused.
     */
    public static void onTabButtonClick(MinecraftClient client, Screen screenClass, int x, int y, double mouseX, double mouseY, boolean focused) {
        //if (!TabAPI.CLIENT.renderTabs) return;
        if (client != null /*&& ConfigInit.CONFIG.inventoryButton*/ && !focused && ((Object) screenClass instanceof Tab || (FabricLoader.getInstance().isModLoaded("libz") && screenClass instanceof net.libz.api.Tab))) {
            Class<?> parent = null;
            if ((Object) screenClass instanceof Tab tabAPITab) {
                parent = tabAPITab.getParent();
            } else if (screenClass instanceof net.libz.api.Tab libZTab) {
                parent = libZTab.getParentScreenClass();
            }
            int xPos = x;

            List<InventoryTab> list = null;
            if (parent != null) {
                if (TabAPI.otherTabs.isEmpty() || !TabAPI.otherTabs.containsKey(parent)) {
                    return;
                }
                list = TabAPI.otherTabs.get(parent);
            } else {
                list = TabAPI.inventoryTabs;
            }
            if (list != null) {
                for (InventoryTab inventoryTab : list) {
                    if (inventoryTab.shouldShow(client) && (inventoryTab.getID() != null && TabAPI.config.tabs.get(inventoryTab.getID().toString()).booleanValue())) {
                        boolean isSelectedTab = inventoryTab.isSelectedScreen(screenClass.getClass());
                        if (inventoryTab.canClick(screenClass, client)
                                && isPointWithinBounds(x, y, xPos - x + 1, isSelectedTab ? -24 : -20, 22, isSelectedTab ? 23 : 19, (double) mouseX, (double) mouseY)) {
                            inventoryTab.onClick(client);
                        }
                        xPos += 25;
                    }
                }
            }
            if (!TabAPI.sideInventoryTabs.isEmpty()) {
                int yPos = y;
                for (InventoryTab inventoryTab : TabAPI.sideInventoryTabs) {
                    if (inventoryTab.shouldShow(client) && (inventoryTab.getID() != null && TabAPI.config.tabs.get(inventoryTab.getID().toString()).booleanValue())) {
                        boolean isSelectedTab = inventoryTab.isSelectedScreen(screenClass.getClass());
                        if (inventoryTab.canClick(screenClass, client)
                                && isPointWithinBounds(x, y, isSelectedTab ? -24 : -20, yPos - y + 1, isSelectedTab ? 23 : 19, 22, (double) mouseX, (double) mouseY)) {
                            inventoryTab.onClick(client);
                        }
                        yPos += 25;
                    }
                }
            }
        }
    }

    private static boolean isPointWithinBounds(int xPos, int yPos, int x, int y, int width, int height, double pointX, double pointY) {
        return (pointX -= (double) xPos) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) yPos) >= (double) (y - 1) && pointY < (double) (y + height + 1);
    }
}
