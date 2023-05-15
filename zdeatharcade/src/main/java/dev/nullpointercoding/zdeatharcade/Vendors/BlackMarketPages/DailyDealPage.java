package dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketVendor;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import me.zombie_striker.qg.api.QualityArmory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

public class DailyDealPage implements Listener {

    private final Inventory dailyDealInv;
    private Main plugin = Main.getInstance();
    private final Component title = Component.text("Daily Deal");
    private HashMap<CustomBaseObject, Integer> randoObject = new HashMap<CustomBaseObject, Integer>();
    private File dailyDealFile;
    private FileConfiguration dailyDealConfig;
    private Double newItemPrice;
    private static Random r;

    private static int randoInt(int min, int max) {
        int randoNum = r.nextInt((max - min) + 1) + min;
        return randoNum;
    }

    public DailyDealPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof DailyDealPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        dailyDealInv = Bukkit.createInventory(null, 9, title);
        for (File f : plugin.getDailyDealFolder().listFiles()) {
            if (f.getName().endsWith(".yml")) {
                dailyDealFile = f;
            }
            try {
                dailyDealConfig = new YamlConfiguration();
                dailyDealConfig.load(dailyDealFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bukkit.getConsoleSender().sendMessage("Loaded Daily Deal Config" + dailyDealFile.getName());

        }
    }

    public Inventory getDailyDealInv() {
        return dailyDealInv;
    }

    public void openInventory(Player player) {
        player.closeInventory();
        player.sendTitlePart(TitlePart.TITLE, Component.text("Loading Daily Deal...", NamedTextColor.LIGHT_PURPLE));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Please wait", NamedTextColor.DARK_PURPLE));
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
        player.sendActionBar(Component.text("This might take a while....", NamedTextColor.LIGHT_PURPLE));
        new BukkitRunnable(){

            @Override
            public void run() {
                dailyDealInv.setItem(8, CustomInvFunctions.getBackButton());
                dailyDealInv.setItem(4, createDailyDealItem());
                player.stopAllSounds();
                player.openInventory(dailyDealInv);
            }
        }.runTaskLater(plugin, 20 * 4);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().title().equals(title)) {
            e.setCancelled(true);
            Player whoClicked = (Player) e.getWhoClicked();
            ItemStack clicked = e.getCurrentItem();
            if (e.getCurrentItem() != null) {
                if (QualityArmory.isCustomItem(e.getCurrentItem())) {
                    PlayerConfigManager pcm = new PlayerConfigManager(whoClicked.getUniqueId().toString());
                    if (e.getCurrentItem().getItemMeta().displayName()
                            .equals(createDailyDealItem().getItemMeta().displayName())) {
                        if (pcm.getTokens() >= newItemPrice) {
                            pcm.setTokens(pcm.getTokens() - newItemPrice);
                            whoClicked.getInventory().addItem(
                                    QualityArmory.getCustomItemAsItemStack(dailyDealConfig.getString("QAItem")));
                            whoClicked.closeInventory();
                        } else {
                            whoClicked.sendMessage("You do not have enough tokens to buy this item!");
                        }
                    }
                }
                if(clicked.getItemMeta().displayName().equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())){
                    whoClicked.closeInventory(Reason.PLUGIN);
                    new BlackMarketVendor().openInventory(whoClicked);
                }
            }
        }
    }

    private ItemStack createDailyDealItem() {
        ItemStack item = new ItemStack(QualityArmory.getCustomItemAsItemStack(dailyDealConfig.getString("QAItem")));
        ItemMeta meta = item.getItemMeta();
        Double oldPrice = dailyDealConfig.getDouble("oldPrice");
        Double newPrice = oldPrice / 2;
        newItemPrice = newPrice;
        meta.displayName(Component.text(dailyDealConfig.getString("displayName")));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Old Price: " + dailyDealConfig.getString("oldPrice") + " Tokens", NamedTextColor.RED));
        lore.add(Component.text("New Price: " + newPrice + " Tokens", NamedTextColor.GREEN));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void resetDailyDeal() {
        dailyDealConfig.set("QAItem", getRandomQAItem());
        dailyDealConfig.set("displayName", QualityArmory.getCustomItemAsItemStack(dailyDealConfig.getString("QAItem"))
                .getItemMeta().displayName());
        dailyDealConfig.set("oldPrice", QualityArmory.getCustomItemAsItemStack(dailyDealConfig.getString("QAItem"))
                .getItemMeta().lore().get(0).toString().replace("Old Price: ", "").replace(" Tokens", ""));
        try {
            dailyDealConfig.save(dailyDealFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRandomQAItem() {
        for (CustomBaseObject cbo : QualityArmory.getCustomItemsAsList()) {
            if (!(cbo.getName().endsWith("_e"))) {
                randoObject.put(cbo, randoInt(1, 100));
                continue;
            }
        }
        List<Integer> nums = new ArrayList<Integer>(randoObject.values());
        for (CustomBaseObject cbo : randoObject.keySet()) {
            if (randoObject.get(cbo) == Collections.max(nums)) {
                return cbo.getName();
            }
        }
        return null;
    }

}
