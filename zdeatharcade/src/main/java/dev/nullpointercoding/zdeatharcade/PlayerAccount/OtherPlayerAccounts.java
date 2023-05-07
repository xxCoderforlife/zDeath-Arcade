package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class OtherPlayerAccounts implements Listener {

    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private final Component title = Component.text("§c§lPLAYER ACCOUNTS");

    public OtherPlayerAccounts() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof OtherPlayerAccounts);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 27, title);
    }

    private void addItems() {
        inv.setItem(18, back());
        for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.displayName(Component.text("§c§l" + p.getName()));
            meta.setOwningPlayer(p);
            skull.setItemMeta(meta);
            inv.addItem(skull);
        }
    }

    public void openGUI(Player p) {
        addItems();
        p.openInventory(inv);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getItemMeta().displayName() == null) {
            return;
        }
        if (clicked.getType() == Material.PLAYER_HEAD) {
            String name = PlainTextComponentSerializer.plainText().serialize(clicked.getItemMeta().displayName());
            String cleanName = ChatColor.stripColor(name);
            Player target = Bukkit.getPlayer(cleanName);
            PlayerProfileManager playerProfile = new PlayerProfileManager(target);
            p.playSound(target, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            playerProfile.openGUI(p);
        }
        if(clicked.getItemMeta().displayName().equals(back().getItemMeta().displayName())){
            p.closeInventory(Reason.PLUGIN);
            PlayerAccountGUI gui = new PlayerAccountGUI(p);
            gui.openGUI(p);
            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
        }

    }

    private ItemStack back() {
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta meta = back.getItemMeta();
        meta.displayName(Component.text("§c§lBACK"));
        back.setItemMeta(meta);
        return back;
    }

}
