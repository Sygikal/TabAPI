package dev.sygii.tabapi.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LibZTab extends InventoryTab {
    private final LibZRunner runner;

    public LibZTab(Text title, @Nullable Identifier texture, int preferedPos, LibZRunner runner, Class<?>... screenClasses) {
        super(title, texture, preferedPos, screenClasses);
        this.runner = runner;
    }

    @Override
    public void onClick(MinecraftClient client) {
        this.runner.asd(client);
    }

    public interface LibZRunner {
        void asd(MinecraftClient client);
    }

}
