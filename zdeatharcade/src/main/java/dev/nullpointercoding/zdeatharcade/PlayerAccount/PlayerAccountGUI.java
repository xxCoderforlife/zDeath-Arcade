package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Bank.BankGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import net.milkbowl.vault.economy.Economy;

public class PlayerAccountGUI implements Listener{

    private final Inventory inv;
    private Main plugin = Main.getInstance();
    private Economy econ = plugin.getEconomy();
    private Player p;
    private Component title = Component.text("§c§lMONEY MANAGAMENT SYSTEM");

    public PlayerAccountGUI(Player p){
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
        .anyMatch(handler -> handler.getListener() instanceof PlayerAccountGUI);
        if(!isEventRegistered){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        this.p = p;
        inv = Bukkit.createInventory(null, 9, title);
    }
    

    private void addItems(){
        inv.setItem(0, balanceItem());
        inv.setItem(4, bankAccount());

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(!(e.getView().title().equals(title))){return;}
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null || clicked.getItemMeta().displayName() == null){return;}
        if(clicked.getItemMeta().displayName().equals(balanceItem().getItemMeta().displayName())){
            OtherPlayerAccounts otherPlayerAccounts = new OtherPlayerAccounts();
            otherPlayerAccounts.openGUI(p);

        }
        if(clicked.getItemMeta().displayName().equals(bankAccount().getItemMeta().displayName())){
            BankGUI bankGUI = new BankGUI(p);
            bankGUI.openGUI(p);
        }
        if(clicked.getItemMeta().displayName().equals(noBankAccountFound().getItemMeta().displayName())){
            p.closeInventory();
            p.sendTitlePart(TitlePart.TITLE, Component.text("§a§oCreating your account now..."));
            p.sendTitlePart(TitlePart.SUBTITLE, Component.text("§2§oHave a nice Day User: " + p.getUniqueId().toString()));
            econ.createBank(p.getUniqueId().toString(), p);
        }
    }

    public void openGUI(HumanEntity p){
        if(doesPlayerHaveABankAccount()){
            addItems();
        }else{
            addItems();
            inv.setItem(4, noBankAccountFound());
        }
        p.openInventory(inv);
    }

    private ItemStack balanceItem(){
        ItemStack balance = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) balance.getItemMeta();
        meta.displayName(Component.text("§c§lAccount Balance"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.space());
        lore.add(Component.text("§7Your current balance is: " + econ.getBalance(p)));
        meta.lore(lore);
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/8b5d160bbdaa308350325ee7a96f6059004a31338615d43564a4c722e28f7cec");
        meta.setPlayerProfile(profile);
        balance.setItemMeta(meta);
        return balance;
    }
    private ItemStack bankAccount(){
        ItemStack bank = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) bank.getItemMeta();
        meta.displayName(Component.text("§c§lBank Account"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.space());
        lore.add(Component.text("§7Your current balance is: " + econ.bankBalance(p.getUniqueId().toString()).balance));
        meta.lore(lore);
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/3b1309dac556911e55398038c4367f892d96cd5e8034fc232db920736879944c");
        meta.setPlayerProfile(profile);
        bank.setItemMeta(meta);
        return bank;


    }
    private ItemStack noBankAccountFound(){
        ItemStack bank = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) bank.getItemMeta();
        meta.displayName(Component.text("§c§lClick to Create Bank Account"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.space());
        lore.add(Component.text("§7Click to create a bank account!"));
        meta.lore(lore);
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/3b1309dac556911e55398038c4367f892d96cd5e8034fc232db920736879944c");
        meta.setPlayerProfile(profile);
        bank.setItemMeta(meta);
        return bank;
    }

    public Boolean doesPlayerHaveABankAccount(){
        for(File f : plugin.getBankDataFolder().listFiles()){
            if(f.getName().equals(p.getUniqueId().toString() + ".yml")){
                return true;
            }
        }
        return false;
    }


    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time
    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createProfile(RANDOM_UUID); // Get a new player profile
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
