package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class OtherPlayerAccounts implements Listener{


    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private Player whoClicked;
    private final Component title = Component.text("§c§lPLAYER ACCOUNTS");

    public OtherPlayerAccounts(){
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
        .anyMatch(handler -> handler.getListener() instanceof OtherPlayerAccounts);
        if(!isEventRegistered){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        inv = Bukkit.createInventory(null, 27, title);
    }

    private void addItems(){
        for(Player p : Bukkit.getOnlinePlayers()){
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.displayName(Component.text("§c§l" + p.getName()));
            meta.setOwningPlayer(p);
            skull.setItemMeta(meta);
            inv.addItem(skull);
        }
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
        whoClicked = p;
    }


    @EventHandler
    public void onClickEvent(InventoryClickEvent e){
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null || clicked.getItemMeta().displayName() == null){return;}
        String name = PlainTextComponentSerializer.plainText().serialize(clicked.getItemMeta().displayName());
        String cleanName = ChatColor.stripColor(name);
        Bukkit.getConsoleSender().sendMessage(cleanName);
        Player target = Bukkit.getPlayer(cleanName);
        PlayerProfile playerProfile = new PlayerProfile(target);
        playerProfile.openGUI(p);

    }
}
