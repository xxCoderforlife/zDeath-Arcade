package dev.nullpointercoding.zdeatharcade.Bank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;

public class BankGUI implements Listener{

    private Main plugin = Main.getInstance();
    private Player p;
    private Economy econ = Main.getEconomy();
    private final Inventory inv;
    private Component title = Component.text("§c§l         BANK MENU");

    public BankGUI(Player p){
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
        .anyMatch(handler -> handler.getListener() instanceof BankGUI);
        if(!isEventRegistered){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }else{
            Bukkit.getConsoleSender().sendMessage("§c§lERROR: §7The BankGUI event is already registered!");
        }
        this.p = p;
        inv = Bukkit.getServer().createInventory(null, 9, title);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null){return;}
        BankAccountGUI bankAccountGUI = new BankAccountGUI(p);
        if(clicked.getItemMeta().displayName().equals(depositToBank().getItemMeta().displayName())){
            bankAccountGUI.setIsDespoisting(true);
            bankAccountGUI.openGUI(p);
        }
        if(clicked.getItemMeta().displayName().equals(withdrawFromBank().getItemMeta().displayName())){
            bankAccountGUI.setIsDespoisting(false);
            bankAccountGUI.openGUI(p);

        }
    }
    

    private void addItems(){
        inv.setItem(0, depositToBank());
        inv.setItem(4, withdrawFromBank());
        inv.setItem(8, balaItemStack());
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
    }

    private ItemStack depositToBank(){
        ItemStack is = new ItemStack(Material.GREEN_WOOL);
        ItemMeta im = is.getItemMeta();
        im.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("§a§lDeposit to Bank"));
        is.setItemMeta(im);
        return is;

    }

    private ItemStack withdrawFromBank(){
        ItemStack is = new ItemStack(Material.RED_WOOL);
        ItemMeta im = is.getItemMeta();
        im.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("§c§lWithdraw from Bank"));
        is.setItemMeta(im);
        return is;

    }

    private ItemStack balaItemStack(){
        ItemStack is = new ItemStack(Material.GOLD_INGOT);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("§e§oBalance: " + econ.getBalance(p)));
        is.setItemMeta(im);
        return is;
    }
}
