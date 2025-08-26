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
    private final LibZItemStackRunner stackRunner;
    private final LibZShouldShowRunner showRunner;

    public LibZTab(Text title, @Nullable Identifier texture, int preferedPos, LibZRunner runner, LibZItemStackRunner stackRunner, LibZShouldShowRunner showRunner, Class<?>... screenClasses) {
        super(Identifier.of("libz", title.getString()), title, texture, preferedPos, screenClasses);
        this.runner = runner;
        this.stackRunner = stackRunner;
        this.showRunner = showRunner;
    }

    @Override
    public void onClick(MinecraftClient client) {
        this.runner.asd(client);
    }

    @Override
    public boolean shouldShow(MinecraftClient client) {
        return this.showRunner.run(client);
    }

    @Override
    @Nullable
    public ItemStack getItemStack(MinecraftClient client) {
        return stackRunner.run(client);
    }

    public interface LibZRunner {
        void asd(MinecraftClient client);
    }

    public interface LibZItemStackRunner {
        ItemStack run(MinecraftClient client);
    }

    public interface LibZShouldShowRunner {
        boolean run(MinecraftClient client);
    }

}
