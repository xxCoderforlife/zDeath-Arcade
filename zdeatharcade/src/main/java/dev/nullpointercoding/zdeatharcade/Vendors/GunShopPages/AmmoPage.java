package dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.GunVendor;
import me.zombie_striker.qg.ammo.Ammo;
import me.zombie_striker.qg.api.QualityArmory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class AmmoPage implements Listener {
    private Main plugin = Main.getInstance();
    private Double ninemmPrice = 45.0;
    private Double fivefivesixPrice = 60.0;
    private Double sevenSixTwoPrice = 70.0;
    private Double shellPrice = 65.0;
    private final Inventory inv;
    private final Component title = Component.text("Ammo Page", NamedTextColor.GOLD, TextDecoration.ITALIC);
    private HashMap<Ammo, Double> ammos = new HashMap<Ammo, Double>();

    public AmmoPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof AmmoPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 27, title);
    }

    public void openInventory(Player whoOpened) {
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(10));
        whoOpened.openInventory(inv);
    }

    private void addItems() {
        inv.setItem(10, createAmmoItem(QualityArmory.getAmmoByName("9mm"), ninemmPrice));
        inv.setItem(11, createAmmoItem(QualityArmory.getAmmoByName("556"), fivefivesixPrice));
        inv.setItem(12, createAmmoItem(QualityArmory.getAmmoByName("762"), sevenSixTwoPrice));
        inv.setItem(13, createAmmoItem(QualityArmory.getAmmoByName("shell"), shellPrice));
        inv.setItem(16, CustomInvFunctions.getBackButton());

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().title().equals(title)) {
            e.setCancelled(true);
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getItemMeta().displayName() == null) {
                return;
            }
            Player whoClicked = (Player) e.getWhoClicked();
            if (QualityArmory.isAmmo(clicked)) {
                Ammo ammo = QualityArmory.getAmmo(clicked);
                if (ammos.containsKey(ammo)) {
                    if (Main.getEconomy().getBalance(whoClicked) >= ammos.get(ammo)) {
                        Main.getEconomy().withdrawPlayer(whoClicked, ammos.get(ammo));
                        QualityArmory.addAmmoToInventory(whoClicked, ammo, 50);
                        whoClicked.sendMessage(Component.text("You have bought 50 " + ammo.getDisplayName() + " for $"
                                + ammos.get(ammo), NamedTextColor.GREEN, TextDecoration.ITALIC));
                    } else {
                        whoClicked.sendMessage(Component.text("You do not have enough money to buy this ammo!",
                                NamedTextColor.RED, TextDecoration.ITALIC).hoverEvent(Component.text("You need $",NamedTextColor.RED, TextDecoration.ITALIC).append(Component.text(ammos.get(ammo),NamedTextColor.RED, TextDecoration.ITALIC))));
                    }
                }
            }
            if (clicked.getItemMeta().displayName()
                    .equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
                whoClicked.closeInventory();
                new GunVendor().openInventory(whoClicked);
            }
        }
    }

    private ItemStack createAmmoItem(Ammo ammo, Double price) {
        ItemStack gunItem = ammo.getItemStack();
        ItemMeta meta = gunItem.getItemMeta();
        meta.displayName(Component.text("       " + ammo.getDisplayName()));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Price: ", NamedTextColor.GREEN, TextDecoration.ITALIC)
                .append(Component.text(price + " for 1", NamedTextColor.WHITE, TextDecoration.ITALIC)));
        meta.lore(lore);
        gunItem.setItemMeta(meta);
        ammos.put(ammo, price);
        return gunItem;

    }
}
