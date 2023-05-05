package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI.AccountType;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.kyori.adventure.text.Component;

public class PlayerProfileManager implements Listener{
    
    private Main plugin = Main.getInstance();
    private final Inventory inv;
    private final Component title;
    private VaultHook econ = new VaultHook();
    private Player p;

    public PlayerProfileManager(Player p){
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
        .anyMatch(handler -> handler.getListener() instanceof PlayerProfileManager);
        if(!isEventRegistered){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        this.p = p;
        title = Component.text(p.getName() + "'s Profile");
        inv = Bukkit.createInventory(null, 54, title);

    }

    public void addItems(){
        inv.setItem(10, playerHead());
        inv.setItem(19, payPlayer());
        inv.setItem(49, setBounty());
        inv.setItem(20, requestPayMent());
        inv.setItem(45, backToPlayers());
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
        if(clicked.getItemMeta().displayName().equals(backToPlayers().getItemMeta().displayName())){
            whoClicked.closeInventory(Reason.PLUGIN);
            OtherPlayerAccounts otherPlayerAccounts = new OtherPlayerAccounts();
            otherPlayerAccounts.openGUI(whoClicked);
        }
    }

    private ItemStack requestPayMent(){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§e§lRequest Payment"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to request payment from a player"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/d7d7f8fd87fe7e34f9113dd385aab7b24ef221c19d455175b2578af7ff46eecf");
        meta.setPlayerProfile(profile);
        meta.lore(lore);
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
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack payPlayer(){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(Component.text("§a§lPay Player"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to pay a player"));
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/d7d7f8fd87fe7e34f9113dd385aab7b24ef221c19d455175b2578af7ff46eecf");
        meta.setPlayerProfile(profile);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack setBounty(){
        ItemStack fire = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta)fire.getItemMeta();
        meta.displayName(Component.text("§c§lSet Bounty"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to set a bounty on a player"));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/2c57e391e36801da12714cf7bcaed71e2c57fde4815afb692445f2b1393cd520");
        meta.setPlayerProfile(profile);
        fire.setItemMeta(meta);
        return fire;
    }

    private ItemStack backToPlayers(){
        ItemStack bar = new ItemStack(Material.BARRIER);
        ItemMeta meta = bar.getItemMeta();
        meta.displayName(Component.text("§c§lBack"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to go back to the players menu"));
        meta.lore(lore);
        bar.setItemMeta(meta);
        return bar;
    }

    public void openGUI(Player p){
        addItems();
        p.openInventory(inv);
    }
        private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time
    private static PlayerProfile getProfile(String url) {
        com.destroystokyo.paper.profile.PlayerProfile profile = (com.destroystokyo.paper.profile.PlayerProfile) Bukkit.createProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }
}
