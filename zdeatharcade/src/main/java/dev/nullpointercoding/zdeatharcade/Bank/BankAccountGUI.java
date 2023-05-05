package dev.nullpointercoding.zdeatharcade.Bank;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.milkbowl.vault.economy.Economy;

public class BankAccountGUI implements Listener{
    private Main plugin = Main.getInstance();
    private Double amountToMoveToBank = 0.0;
    private Player p;
    private Boolean isDepositingv;
    private AccountType accountT;
    private Economy econ = new VaultHook();
    private static HashMap<Player,Double> bountyAmount = new HashMap<Player,Double>();
    private static HashMap<Player,HashMap<Player,Double>> playerBounty = new HashMap<Player,HashMap<Player,Double>>();
    private Component title = Component.text("§e§oBANK TRANSACTION MENU");
    private Component error = Component.text("§4§lBROKE ALERT: You ain't got no Bread").hoverEvent(HoverEvent.showText(Component.text("§7§oYou can't move $0")));

    private Inventory inv;

    public enum AccountType{
        BANK(),
        PLAYER(),
        BOUNTY();
    }

    public BankAccountGUI(Player p){
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
        .anyMatch(handler -> handler.getListener() instanceof BankAccountGUI);
        if(!isEventRegistered){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }else{
            Bukkit.getConsoleSender().sendMessage("§c§lERROR: §7The PlayerAccountGUI event is already registered!");
        }
        this.p = p;
        inv = plugin.getServer().createInventory(p, 54, title);
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
    }
    private void addItems(){
        inv.setItem(0, addOne());
        inv.setItem(9, addTen());
        inv.setItem(18, add50());
        inv.setItem(27, add100());
        inv.setItem(36, add1000());
        inv.setItem(8, remove1());
        inv.setItem(17, remove10());
        inv.setItem(26, remove50());
        inv.setItem(35, remove100());
        inv.setItem(44, remove1000());
        if(accountT.equals(AccountType.BANK)){
            if(isDepositingv){
                inv.setItem(49, confirmDeposit());
                inv.setItem(4, depoistAll());
            }else{
                inv.setItem(49, confirmWithdraw());
                inv.setItem(4, withdrawlAll());
            }
        }
        if(accountT.equals(AccountType.PLAYER)){
            if(isDepositingv){
                inv.setItem(49, payPlayer());
            }else{
                inv.setItem(49, reqPlayer());
            }
        }
        if(accountT.equals(AccountType.BOUNTY)){
            inv.setItem(49, confirmBouty());
        }

    }

    @EventHandler
    public void onBankMenuClose(InventoryCloseEvent e){
        if(!(e.getView().title().equals(title))){return;}
        amountToMoveToBank = 0.0;
        if(e.getReason().equals(Reason.PLAYER)){
            p.sendMessage("§c§lERROR: §7You have closed the bank menu!");
        }

    }


    @EventHandler
    public void onBankGUIClick(InventoryClickEvent e){
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null){return;}
        if(clicked.getItemMeta().displayName().equals(addOne().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank + 1;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(addTen().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank + 10;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(add50().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank + 50;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(add100().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank + 100;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(add1000().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank + 1000;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(remove1().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank - 1;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(remove10().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank - 10;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(remove50().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank - 50;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(remove100().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank - 100;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(remove1000().getItemMeta().displayName())){
            amountToMoveToBank = amountToMoveToBank - 1000;
            p.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
        }
        if(clicked.getItemMeta().displayName().equals(confirmDeposit().getItemMeta().displayName())){
            if(amountToMoveToBank > 0){
                if(econ.getBalance(p) >= amountToMoveToBank){
                    econ.withdrawPlayer(p, amountToMoveToBank);
                    econ.bankDeposit(p.getUniqueId().toString(), amountToMoveToBank);
                    p.sendMessage("§a§lSUCCESS: §7You have successfully moved §a§l$" + amountToMoveToBank + " §7to your bank account!");
                    p.closeInventory(Reason.PLUGIN);
                }else{
                    p.closeInventory(Reason.PLUGIN);
                    p.sendMessage(error);
                }
            }else{
                p.sendMessage("§c§lERROR: §7You cannot move a negative amount to your bank account!");
            }
        }
        if(clicked.getItemMeta().displayName().equals(confirmWithdraw().getItemMeta().displayName())){
            if(amountToMoveToBank > 0){
                if(econ.bankBalance(p.getUniqueId().toString()).balance >= amountToMoveToBank){
                    econ.depositPlayer(p, amountToMoveToBank);
                    econ.bankWithdraw(p.getUniqueId().toString(), amountToMoveToBank);
                    p.sendMessage("§a§lSUCCESS: §7You have successfully moved §a§l$" + amountToMoveToBank + " §7to your player account!");
                    p.closeInventory(Reason.PLUGIN);
                }else{
                    p.closeInventory(Reason.PLUGIN);
                    p.sendMessage(error);
                }
            }else{
                p.sendMessage("§c§lERROR: §7You cannot move a negative amount to your player account!");
            }
        }
        if(clicked.getItemMeta().displayName().equals(depoistAll().getItemMeta().displayName())){
                if(econ.getBalance(p) > 0){
                    econ.bankDeposit(p.getUniqueId().toString(), econ.getBalance(p));
                    econ.withdrawPlayer(p, econ.getBalance(p));
                    p.sendMessage("§a§lSUCCESS: §7You have successfully moved §a§l$" + econ.bankBalance(p.getUniqueId().toString()).balance + " §7to your bank account!");
                    p.closeInventory(Reason.PLUGIN);
                }else{
                    p.sendMessage(error);
                    p.closeInventory(Reason.PLUGIN);
                }
        }
        if(clicked.getItemMeta().displayName().equals(withdrawlAll().getItemMeta().displayName())){
            if(econ.bankBalance(p.getUniqueId().toString()).balance > 0){
                econ.depositPlayer(p, econ.bankBalance(p.getUniqueId().toString()).balance);
                econ.bankWithdraw(p.getUniqueId().toString(), econ.bankBalance(p.getUniqueId().toString()).balance);
                p.sendMessage("§a§lSUCCESS: §7You have successfully moved §a§l$" + econ.getBalance(p) + " §7to your player account!");
                p.closeInventory(Reason.PLUGIN);
            }else{
                p.closeInventory(Reason.PLUGIN);
                p.sendMessage(error);
            }
        }
        if(clicked.getItemMeta().displayName().equals(payPlayer().getItemMeta().displayName())){
            Player whoClicked = (Player) e.getWhoClicked();
            if(econ.getBalance(whoClicked) > 0){
                econ.withdrawPlayer(whoClicked, amountToMoveToBank);
                econ.depositPlayer(p, amountToMoveToBank);
                p.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully paid §a§l$" + amountToMoveToBank + " §7to " + whoClicked.getName() + "!"));
                p.closeInventory(Reason.PLUGIN);
            }
        }
        if(clicked.getItemMeta().displayName().equals(reqPlayer().getItemMeta().displayName())){
            Player whoClicked = (Player) e.getWhoClicked();
            p.sendMessage(Component.text(whoClicked.getName() + " wants you to send them " + amountToMoveToBank + "!"));
            whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully requested §a§l$" + amountToMoveToBank + " §7from " + p.getName() + "!"));
            p.closeInventory(Reason.PLUGIN);
        }
        if(clicked.getItemMeta().displayName().equals(confirmBouty().getItemMeta().displayName())){
            Player whoClicked = (Player) e.getWhoClicked();
            if(amountToMoveToBank > 0){
                if(econ.getBalance(whoClicked) >= amountToMoveToBank){                    
                    p.sendMessage("§a§lSUCCESS: §7You have successfully put a Bounty on " + p.getName());
                    Bukkit.broadcast(Component.text(whoClicked + " set a bounty of $" + amountToMoveToBank + " on " + p.getName()));
                    bountyAmount.put(whoClicked, amountToMoveToBank);
                    playerBounty.put(p, bountyAmount);
                    p.closeInventory(Reason.PLUGIN);
                }else{
                    p.closeInventory(Reason.PLUGIN);
                    p.sendMessage(error);
                }
            }else{
                p.sendMessage("§c§lERROR: §7You cannot move a negative amount to the bounty account!");
            }
        }
    }


    private ItemStack addOne(){
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+1"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }
    private ItemStack addTen(){
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+10"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }
    private ItemStack add50(){
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+50"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }
    private ItemStack add100(){
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+100"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }
    private ItemStack add1000(){
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+1000"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }
    private ItemStack remove1(){
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-1"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }
    private ItemStack remove10(){
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-10"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }
    private ItemStack remove50(){
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-50"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }
    private ItemStack remove100(){
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-100"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }
    private ItemStack remove1000(){
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-1000"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }
    private ItemStack confirmDeposit(){
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirm.getItemMeta();
        meta.displayName(Component.text("§a§lCONFIRM DEPOSIT"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        confirm.setItemMeta(meta);
        return confirm;
    }
    private ItemStack confirmWithdraw(){
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirm.getItemMeta();
        meta.displayName(Component.text("§a§lCONFIRM WITHDRAWAL"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        confirm.setItemMeta(meta);
        return confirm;
    }
    private ItemStack withdrawlAll(){
        ItemStack all = new ItemStack(Material.MAGENTA_WOOL);
        ItemMeta meta = all.getItemMeta();
        meta.displayName(Component.text("§d§lWITHDRAW ALL"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
        all.setItemMeta(meta);
        return all;
    }

    private ItemStack depoistAll(){
        ItemStack all = new ItemStack(Material.MAGENTA_WOOL);
        ItemMeta meta = all.getItemMeta();
        meta.displayName(Component.text("§d§lDEPOSIT ALL"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
        all.setItemMeta(meta);
        return all;
    }
    private ItemStack payPlayer(){
        ItemStack conPay = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = conPay.getItemMeta();
        meta.displayName(Component.text("§6§lPAY PLAYER"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        conPay.setItemMeta(meta);
        return conPay;
    }

    private ItemStack reqPlayer(){
        ItemStack conPay = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = conPay.getItemMeta();
        meta.displayName(Component.text("§6§lREQUEST PAYMENT"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        conPay.setItemMeta(meta);
        return conPay;
    }

    private ItemStack confirmBouty(){
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirm.getItemMeta();
        meta.displayName(Component.text("§a§lCONFIRM BOUNTY"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        confirm.setItemMeta(meta);
        return confirm;
    }

    public void setIsDespoisting(Boolean isDespoisting,AccountType accountType){
        isDepositingv = isDespoisting;
        accountT = accountType;
    }

    public static HashMap<Player,HashMap<Player, Double>> getBountyList(){
        return playerBounty;
    }
    public static HashMap<Player,Double> getBountyAmount(){
        return bountyAmount;
    }

}
