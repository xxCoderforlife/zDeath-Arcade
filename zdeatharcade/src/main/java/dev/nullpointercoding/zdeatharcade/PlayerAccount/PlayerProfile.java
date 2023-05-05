package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI.AccountType;
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
        inv.setItem(0, playerHead());
        inv.setItem(25, payPlayer());
        inv.setItem(7, setBounty());
        inv.setItem(6, requestPayMent());
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e){
        Player whoClicked = (Player) e.getWhoClicked();
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if(clicked.getItemMeta().displayName().equals(requestPayMent().getItemMeta().displayName())){
            BankAccountGUI bankGUI = new BankAccountGUI(p);
            bankGUI.setIsDespoisting(true, AccountType.PLAYER);
            bankGUI.openGUI(whoClicked);
        }
        if(clicked.getItemMeta().displayName().equals(payPlayer().getItemMeta().displayName())){
            BankAccountGUI bankGUI = new BankAccountGUI(p);
            bankGUI.setIsDespoisting(false, AccountType.PLAYER);
            bankGUI.openGUI(whoClicked);        
        }
        if(clicked.getItemMeta().displayName().equals(setBounty().getItemMeta().displayName())){
            BankAccountGUI bankGUI = new BankAccountGUI(p);
            bankGUI.setIsDespoisting(true, AccountType.BOUNTY);
            bankGUI.openGUI(whoClicked);
        }
    }

    private ItemStack requestPayMent(){
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§c§lRequest Payment"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack playerHead(){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§c§l" + p.getName()));
        meta.setOwningPlayer(p);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Health: " + p.getHealth() + "/" + p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
        lore.add(Component.text("Strength: " + p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue()));
        lore.add(Component.text("Food: " + p.getFoodLevel()));
        lore.add(Component.text("Cash: " + econ.getBalance(p)));
        lore.add(Component.text("Banked Cash: " + econ.bankBalance(p.getUniqueId().toString()).balance));
        lore.add(Component.text("Zombie Kills: " + p.getStatistic(org.bukkit.Statistic.KILL_ENTITY,EntityType.ZOMBIE)));
        lore.add(Component.text("Player Kills: " + p.getStatistic(org.bukkit.Statistic.PLAYER_KILLS)));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack payPlayer(){
        ItemStack item = new ItemStack(Material.BLUE_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(Component.text("§c§lPay Player"));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack setBounty(){
        ItemStack fire = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = fire.getItemMeta();
        meta.displayName(Component.text("§c§lSet Bounty"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        fire.setItemMeta(meta);
        return fire;
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
    }
}
