package dev.sygii.tabapi;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.sygii.tabapi.api.InventoryTab;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import dev.sygii.tabapi.util.SortList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class TabAPI implements ClientModInitializer {

    public static final List<InventoryTab> inventoryTabs = new ArrayList<InventoryTab>();
    public static final List<InventoryTab> sideInventoryTabs = new ArrayList<InventoryTab>();
    public static final HashMap<Class<?>, List<InventoryTab>> otherTabs = new HashMap<Class<?>, List<InventoryTab>>();

    public static final Identifier tabTexture = identifierOf("textures/gui/icons.png");

    public static final boolean isL2tabsloaded = FabricLoader.getInstance().isModLoaded("l2tabs");
    public static final boolean isL2hostilityloaded = FabricLoader.getInstance().isModLoaded("l2hostility");

    public static final String MOD_ID = "tabapi";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static TabAPIConfig config = new TabAPIConfig();

    @Override
    public void onInitializeClient() {
        /*if (isL2tabsloaded) {
            TabAPI.registerInventoryTab(new L2Tab(L2TabsLangData.ATTRIBUTE.get(), TabAttributes::onTabClicked(), Items.IRON_SWORD.getDefaultStack(), 1, AttributeScreen.class));
        }
        if (isL2hostilityloaded) {
            TabAPI.registerInventoryTab(new L2Tab(LHTexts.INFO_TAB_TITLE.get(), Items.ZOMBIE_HEAD.getDefaultStack(), 2, DifficultyScreen.class));
        }*/
        File file = new File("config/tabapi_tabs.json");
        if (file.exists()) {
            config.tabs = readConfig();
        }
        saveConfig(config);
        //TabAPI.registerInventoryTab(new TestTab(Text.translatable("container.crafting"), 0, InventoryScreen.class));
    }

    public static Map<String, Boolean> readConfig() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type typeObject = new TypeToken<HashMap>() {}.getType();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new FileReader("config/tabapi_tabs.json"));
            return gson.fromJson(bufferedReader, typeObject);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveConfig(TabAPIConfig config) {
        //config.tabs.put("asd", true);

        Type typeObject = new TypeToken<HashMap>() {}.getType();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        FileWriter writer = null;
        try {
            writer = new FileWriter("config/tabapi_tabs.json");
            writer.write(gson.toJson(config.tabs, typeObject));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Identifier identifierOf(String name) {
        return Identifier.of(MOD_ID, name);
    }

    /**
     * Registers a player inventory tab.
     *
     * @param tab An instance of an InventoryTab class.
     */
    public static void registerInventoryTab(InventoryTab tab) {
        config.tabs = readConfig();
        if (tab.getID() != null && !config.tabs.containsKey(tab.getID().toString())) {
            config.tabs.put(tab.getID().toString(), true);
            saveConfig(config);
        }
        /*if (LibzClient.isL2loaded) {
            ItemStack item = new ItemStack();
            dev.xkmc.l2tabs.tabs.inventory.TabRegistry.GROUP.registerTab(0, TabInventory::new, () -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get());
        }else {*/
        TabAPI.inventoryTabs.add(tab);
        // Sort prefered pos
        List<Integer> priorityList = new ArrayList<Integer>();
        for (int i = 0; i < TabAPI.inventoryTabs.size(); i++) {
            int preferedPos = TabAPI.inventoryTabs.get(i).getPreferedPos();
            if (preferedPos == -1) {
                preferedPos = 99;
            }
            priorityList.add(preferedPos);
        }
        SortList.concurrentSort(priorityList, TabAPI.inventoryTabs);
        // }
    }

    public static void registerSideInventoryTab(InventoryTab tab) {
        /*if (LibzClient.isL2loaded) {
            ItemStack item = new ItemStack();
            dev.xkmc.l2tabs.tabs.inventory.TabRegistry.GROUP.registerTab(0, TabInventory::new, () -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get());
        }else {*/
        TabAPI.sideInventoryTabs.add(tab);
        // Sort prefered pos
        List<Integer> priorityList = new ArrayList<Integer>();
        for (int i = 0; i < TabAPI.sideInventoryTabs.size(); i++) {
            int preferedPos = TabAPI.sideInventoryTabs.get(i).getPreferedPos();
            if (preferedPos == -1) {
                preferedPos = 99;
            }
            priorityList.add(preferedPos);
        }
        SortList.concurrentSort(priorityList, TabAPI.sideInventoryTabs);
        // }
    }

    /**
     * Registers a tab for all screens.
     *
     * <p>
     * Handled screens do not need to call DrawTabHelper.drawTab on the render method and DrawTabHelper.onTabButtonClick on the mouseClicked method. Client screens need to call DrawTabHelper.drawTab
     * on the render method and DrawTabHelper.onTabButtonClick on the mouseClicked method.
     *
     * @param tab         An instance of an InventoryTab class.
     * @param parentClass The parent class of the screen class where the tab will get added.
     */
    public static void registerOtherTab(InventoryTab tab, Class<?> parentClass) {
        if (TabAPI.otherTabs.get(parentClass) != null) {
            TabAPI.otherTabs.get(parentClass).add(tab);
            // Sort prefered pos
            List<Integer> priorityList = new ArrayList<Integer>();
            for (int i = 0; i < TabAPI.otherTabs.get(parentClass).size(); i++) {
                int preferedPos = TabAPI.otherTabs.get(parentClass).get(i).getPreferedPos();
                if (preferedPos == -1) {
                    preferedPos = 99;
                }
                priorityList.add(preferedPos);
            }
            SortList.concurrentSort(priorityList, TabAPI.otherTabs.get(parentClass));
        } else {
            List<InventoryTab> list = new ArrayList<InventoryTab>();
            list.add(tab);
            TabAPI.otherTabs.put(parentClass, list);
        }
    }

}
