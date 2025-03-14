package dev.sygii.tabapi;

import dev.sygii.tabapi.api.InventoryTab;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TestTab extends InventoryTab {

    public TestTab(Text title, int preferedPos, Class<?>... screenClasses) {
        super(title, null, preferedPos, screenClasses);
    }

    @Override
    public void onClick(MinecraftClient client) {
        client.setScreen(new InventoryScreen(client.player));
    }

    @Override
    public ItemStack getItemStack(MinecraftClient client) {
        return Items.CRAFTING_TABLE.getDefaultStack();
    }

}
