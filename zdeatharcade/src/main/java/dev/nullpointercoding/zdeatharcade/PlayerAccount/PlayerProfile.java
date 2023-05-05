package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.kyori.adventure.text.Component;

public class PlayerProfile implements Listener{
    
    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private final Component title;
    private VaultHook econ = new VaultHook();
    private Player p;

    public PlayerProfile(Player p){
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
        .anyMatch(handler -> handler.getListener() instanceof PlayerProfile);
        if(!isEventRegistered){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        this.p = p;
        title = Component.text(p.getName() + "'s Profile");
        inv = Bukkit.createInventory(null, 54, title);

    }

    public void addItems(){
        inv.addItem(playersCash());
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e){
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
    }

    private ItemStack playersCash(){
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§c§lCash"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§a" + econ.getBalance(p)));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
    }
}
