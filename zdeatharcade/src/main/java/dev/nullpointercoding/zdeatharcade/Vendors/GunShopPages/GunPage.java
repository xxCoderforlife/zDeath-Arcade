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
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class GunPage implements Listener {

    private final Inventory inv;
    private Main plugin = Main.getInstance();
    private final Component title = Component.text("Guns Guns Guns");
    private Double fn57Price = 5000.0;
    private Double aa12Price = 8000.0;
    private Double ak47Price = 6000.0;
    private Double ak47uPrice = 5000.0;
    private Double glockPrice = 3000.0;
    private Double m4a1sPrice = 10000.0;
    private Double mac10Price = 8000.0;
    private Double uziPrice = 5000.0;
    private Double stenPrice = 6000.0;
    private Double awpPrice = 8000.0;
    private Double henryriflePrice = 25000.0;
    private Double mp5kPrice = 9000.0;
    private Double mp40Price = 7000.0;
    private Double umpPrice = 6000.0;
    private Double p30Price = 10000.0;
    private Double fnfalPrice = 12000.0;
    private final Component star = Component.text('★', NamedTextColor.YELLOW, TextDecoration.BOLD);
    private HashMap<Gun, Double> guns = new HashMap<Gun, Double>();

    public GunPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof GunPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 45, title);
    }

    public void openInventory(Player whoOpened) {
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(10));
        whoOpened.openInventory(inv);
    }

    private void addItems() {
        inv.setItem(11, createGunItem(QualityArmory.getGunByName("aa12"), aa12Price));
        inv.setItem(12, createGunItem(QualityArmory.getGunByName("ak47"), ak47Price));
        inv.setItem(13, createGunItem(QualityArmory.getGunByName("ak47u"), ak47uPrice));
        inv.setItem(14, createGunItem(QualityArmory.getGunByName("glock"), glockPrice));
        inv.setItem(15, createGunItem(QualityArmory.getGunByName("m4a1s"), m4a1sPrice));
        inv.setItem(16, createGunItem(QualityArmory.getGunByName("mac10"), mac10Price));
        inv.setItem(19, createGunItem(QualityArmory.getGunByName("uzi"), uziPrice));
        inv.setItem(20, createGunItem(QualityArmory.getGunByName("sten"), stenPrice));
        inv.setItem(21, createGunItem(QualityArmory.getGunByName("awp"), awpPrice));
        inv.setItem(22, createGunItem(QualityArmory.getGunByName("henryrifle"), henryriflePrice));
        inv.setItem(23, createGunItem(QualityArmory.getGunByName("mp5k"), mp5kPrice));
        inv.setItem(24, createGunItem(QualityArmory.getGunByName("mp40"), mp40Price));
        inv.setItem(25, createGunItem(QualityArmory.getGunByName("ump"), umpPrice));
        inv.setItem(28, createGunItem(QualityArmory.getGunByName("p30"), p30Price));
        inv.setItem(29, createGunItem(QualityArmory.getGunByName("fnfal"), fnfalPrice));
        inv.setItem(10, createGunItem(QualityArmory.getGunByName("fnfiveseven"), fn57Price));
        inv.setItem(34, CustomInvFunctions.getBackButton());

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
            if (QualityArmory.isGun(clicked)) {
                Gun gun = QualityArmory.getGun(clicked);
                if (guns.containsKey(gun)) {
                    if (Main.getEconomy().getBalance(whoClicked) >= guns.get(gun)) {
                        Main.getEconomy().withdrawPlayer(whoClicked, guns.get(gun));
                        whoClicked.getInventory().addItem(gun.getItemStack());
                        whoClicked.sendMessage(
                                Component.text("You have bought a " + gun.getDisplayName() + " for $" + guns.get(gun)));
                    } else {
                        whoClicked.sendMessage(Component.text("You do not have enough money to buy this gun",
                                NamedTextColor.RED, TextDecoration.ITALIC).hoverEvent(Component.text("You need $")));
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

    private ItemStack createGunItem(Gun gun, Double price) {
        ItemStack gunItem = gun.getItemStack();
        ItemMeta meta = gunItem.getItemMeta();
        meta.displayName(star.append(Component.text(" " + gun.getDisplayName() + " ").append(star)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Price: ", NamedTextColor.GREEN)
                .append(Component.text("$" + price, NamedTextColor.WHITE)));
        lore.add(Component.text("Ammo Type: ", NamedTextColor.YELLOW)
                .append(Component.text(gun.getAmmoType().getName(), NamedTextColor.WHITE)));
        lore.add(Component.text("Damage: ", NamedTextColor.RED)
                .append(Component.text(gun.getDamage(), NamedTextColor.WHITE)));
        lore.add(Component.text("Fire Rate: ", NamedTextColor.GREEN)
                .append(Component.text(gun.getFireRate(), NamedTextColor.WHITE)));
        lore.add(Component.text("Recoil: ", NamedTextColor.DARK_RED)
                .append(Component.text(gun.getRecoil(), NamedTextColor.WHITE)));
        lore.add(Component.text("Reload Time: ", NamedTextColor.AQUA)
                .append(Component.text(gun.getReloadTime(), NamedTextColor.WHITE)));
        lore.add(Component.text("Magazine Size: ", NamedTextColor.GOLD)
                .append(Component.text(gun.getMaxBullets(), NamedTextColor.WHITE)));
        lore.add(Component.text("Max Range: ", NamedTextColor.GRAY)
                .append(Component.text(gun.getMaxDistance() + " Blocks", NamedTextColor.WHITE)));
        meta.lore(lore);
        gunItem.setItemMeta(meta);
        guns.put(gun, price);
        return gunItem;

    }
}
