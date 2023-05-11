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
import me.zombie_striker.customitemmanager.CustomBaseObject;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.miscitems.Grenade;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class EquipmentPage implements Listener{
    private Main plugin = Main.getInstance();
    private Double grenadePrice = 45.0;
    private Double smokePrice = 60.0;
    private Double flashPrice = 60.0;
    private Double molotovPrice = 60.0;
    private Double proxMinePrice = 60.0;
    private Double inciGPrice = 60.0;
    private final Inventory inv;
    private final Component title = Component.text("Equipment Page", NamedTextColor.GOLD, TextDecoration.ITALIC);
    private HashMap<CustomBaseObject, Double> equipment = new HashMap<CustomBaseObject, Double>();

    public EquipmentPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof EquipmentPage);
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
        inv.setItem(10, createMiscItem((Grenade) QualityArmory.getCustomItemByName("grenade"), grenadePrice));
        inv.setItem(11, createMiscItem((Grenade) QualityArmory.getCustomItemByName("smokegrenade"), smokePrice));
        inv.setItem(12, createMiscItem((Grenade) QualityArmory.getCustomItemByName("flashbang"), flashPrice));
        inv.setItem(13, createMiscItem((Grenade) QualityArmory.getCustomItemByName("molotov"), molotovPrice));
        inv.setItem(14, createMiscItem((Grenade) QualityArmory.getCustomItemByName("proxymine"), proxMinePrice));
        inv.setItem(15, createMiscItem((Grenade) QualityArmory.getCustomItemByName("incendarygrenade"), inciGPrice));
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
            if (QualityArmory.isMisc(clicked)) {
                CustomBaseObject gre = QualityArmory.getCustomItem(clicked);
                if (equipment.containsKey(gre)) {
                    if (Main.getEconomy().getBalance(whoClicked) >= equipment.get(gre)) {
                        Main.getEconomy().withdrawPlayer(whoClicked, equipment.get(gre));
                        whoClicked.getInventory().addItem(QualityArmory.getCustomItemAsItemStack(gre));
                        whoClicked.sendMessage(Component.text("You have bought a " + gre.getDisplayName() + " for $"
                                + equipment.get(gre), NamedTextColor.GREEN, TextDecoration.ITALIC));
                    } else {
                        whoClicked.sendMessage(Component.text("You do not have enough money to buy this ammo!",
                                NamedTextColor.RED, TextDecoration.ITALIC).hoverEvent(Component.text("You need $",NamedTextColor.RED, TextDecoration.ITALIC).append(Component.text(equipment.get(gre),NamedTextColor.RED, TextDecoration.ITALIC))));
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

    private ItemStack createMiscItem(Grenade cbo, Double price) {
        ItemStack gunItem = QualityArmory.getCustomItemAsItemStack(cbo);
        ItemMeta meta = gunItem.getItemMeta();
        meta.displayName(Component.text("       " + cbo.getDisplayName()));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Price: ", NamedTextColor.GREEN, TextDecoration.ITALIC)
                .append(Component.text(price + " for 50", NamedTextColor.WHITE, TextDecoration.ITALIC)));
        meta.lore(lore);
        gunItem.setItemMeta(meta);
        equipment.put(cbo, price);
        return gunItem;

    }
}
