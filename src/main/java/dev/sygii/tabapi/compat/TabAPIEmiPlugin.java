package dev.sygii.tabapi.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.mixin.accessor.HandledScreenAccessor;
import dev.sygii.tabapi.TabAPI;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TabAPIEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        if (!TabAPI.inventoryTabs.isEmpty()) {
            registry.addExclusionArea(InventoryScreen.class, (screen, consumer) -> {
                int left = screen.width / 2 - 138;
                int top = screen.height / 2 - 83 - 21;
                consumer.accept(new Bounds(top, left, TabAPI.inventoryTabs.size() * 25, 21));
            });
        }
        TabAPI.otherTabs.forEach((screenClass, tabList) -> {
            registry.addExclusionArea((Class) screenClass.getClass(), (screen, consumer) -> {
                if (screen instanceof HandledScreen handledScreen) {
                    int left = ((HandledScreenAccessor) screen).getX();
                    int top = ((HandledScreenAccessor) screen).getY() - 21;
                    consumer.accept(new Bounds(left, top, tabList.size() * 25, 21));
                } else {
                    int left = screen.width / 2 - 138;
                    int top = screen.height / 2 - 83 - 21;
                    consumer.accept(new Bounds(left, top, tabList.size() * 25, 21));
                }
            });

        });
    }
}
