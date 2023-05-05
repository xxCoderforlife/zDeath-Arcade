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

public class OtherPlayerAccounts implements Listener{


    private Main plugin = Main.getInstance();
    private final Inventory inv;
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
            meta.displayName(Component.text("§c§l" + p.displayName()));
            meta.setOwningPlayer(p);
            skull.setItemMeta(meta);
            inv.addItem(skull);
        }
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
    }


    @EventHandler
    public void onClickEvent(InventoryClickEvent e){
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null || clicked.getItemMeta().displayName() == null){return;}
        Player target = Bukkit.getPlayer(clicked.getItemMeta().displayName().toString().replace("§c§l", ""));
        PlayerProfile playerProfile = new PlayerProfile(target);
        playerProfile.openGUI(p);

    }
}
