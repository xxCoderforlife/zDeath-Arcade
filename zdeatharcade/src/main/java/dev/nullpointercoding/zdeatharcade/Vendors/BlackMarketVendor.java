package dev.nullpointercoding.zdeatharcade.Vendors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.NPCConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages.BlackMarketPages.BuyPage;
import dev.nullpointercoding.zdeatharcade.Vendors.GunShopPages.BlackMarketPages.SellPage;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieDrops;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BlackMarketVendor implements Listener {
    private Inventory inv;
    private Double coalOrePrice = 1.0;
    private Double ironOrePrice = 1.5;
    private Double redstoneOrePrice = 1.8;
    private Double emerabldPrice = 2.0;
    private static Villager blackMarketVendor;
    ZombieDrops zDrops = new ZombieDrops();
    private final Component title = Component.text("     Black Market", NamedTextColor.DARK_PURPLE,
            TextDecoration.ITALIC);
    private final static Component key = Component.text('♛',NamedTextColor.YELLOW,TextDecoration.BOLD);

    private static final Component name = Component.text(" Black Market Dealer ", NamedTextColor.LIGHT_PURPLE,
            TextDecoration.ITALIC).toBuilder().build();
    private final static Component fullName = key.append(name).append(key);

    public BlackMarketVendor() {
        inv = Bukkit.createInventory(null, 27, title);
    }

    @EventHandler
    public void onVendorClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            LivingEntity entity = (LivingEntity) e.getRightClicked();
            if (entity.customName() == null) {
                return;
            }
            if (entity.customName().equals(fullName)) {
                openInventory(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().title().equals(title)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemMeta clickedMeta = e.getCurrentItem().getItemMeta();
            Player whoClicked = (Player) e.getWhoClicked();
            if (clickedMeta.displayName().equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())) {
                whoClicked.closeInventory();
            }
            if (clickedMeta.displayName().equals(buyGUI().getItemMeta().displayName())) {
                whoClicked.closeInventory();
                new BuyPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(sellGUI().getItemMeta().displayName())) {
                new SellPage().openInventory(whoClicked);
            }
            if (clickedMeta.displayName().equals(dailyDeal().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.redstoneOreDrop(), redstoneOrePrice);
            }
            if (clickedMeta.displayName().equals(buyTokens().getItemMeta().displayName())) {
                checkForItems(whoClicked, zDrops.emeraldOreDrop(), emerabldPrice);

            }
        }
    }

    public Inventory getInventory() {
        return inv;
    }

    public void openInventory(Player player) {
        addItem();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(0));
        player.openInventory(inv);
    }

    private void addItem() {
        inv.setItem(0, vendorHead());
        inv.setItem(10, buyGUI());
        inv.setItem(12, sellGUI());
        inv.setItem(14, dailyDeal());
        inv.setItem(16, CustomInvFunctions.getBackButton());
        inv.setItem(26, buyTokens());
    }

    public static Villager spawnblackMarketVendor(Player player, Integer npcID) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("blackmarketvendor.yml")) {
                player.sendMessage(Component.text("Black Market Vendor Spawned already spawned!"));
                return blackMarketVendor;
            }
        }
        Location spawnLoc = player.getLocation().add(1, 0.0, 0);
        blackMarketVendor = (Villager) player.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        blackMarketVendor.setVillagerType(Type.JUNGLE);
        blackMarketVendor.setProfession(Profession.WEAPONSMITH);
        blackMarketVendor.setPersistent(true);
        blackMarketVendor.setCollidable(false);
        blackMarketVendor.setSilent(true);
        blackMarketVendor.setAI(false);
        blackMarketVendor.customName(key.append(name).append(key));
        blackMarketVendor.setCustomNameVisible(true);
        new NPCConfigManager("blackmarketvendor", player.getWorld(), npcID, spawnLoc.getX(), spawnLoc.getY(),
                spawnLoc.getZ());
        return blackMarketVendor;
    }

    public static void removeblackMarketVendor(Player p) {
        for (File f : Main.getInstance().getNPCDataFolder().listFiles()) {
            if (f.getName().equalsIgnoreCase("blackmarketvendor.yml")) {
                f.delete();
                blackMarketVendor.remove();
            }
        }
        for(LivingEntity le : p.getWorld().getLivingEntities()){
            if(le.customName().equals(fullName)){
                le.remove();
            }
        }
    }

    public static Villager getLevel1Vendor() {
        return blackMarketVendor;
    }

    private ItemStack buyGUI() {
        ItemStack buyHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) buyHead.getItemMeta();
        meta.displayName(key.append(Component.text("Buy Items", NamedTextColor.GREEN, TextDecoration.ITALIC)).append(key));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Buy Exctoic Items", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/89e23af2797f10f588fa4c5a8fa9c515785258e459baac02f6963156a4babe25");
        meta.setPlayerProfile(profile);
        buyHead.setItemMeta(meta);
        return buyHead;
    }

    private ItemStack sellGUI() {
        ItemStack sellHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) sellHead.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(key.append(Component.text("Sell Items", NamedTextColor.GREEN, TextDecoration.ITALIC)).append(key));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell Exctoic Items", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/b423289510c54b67df023580979c465d0481c769c865bf4b465cf478749f1c4f");
        meta.setPlayerProfile(profile);
        sellHead.setItemMeta(meta);
        return sellHead;
    }

    private ItemStack dailyDeal() {
        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta meta = beacon.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(key.append(Component.text("Daily Deal", NamedTextColor.GREEN, TextDecoration.ITALIC)).append(key));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to see the Daily Deal", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        beacon.setItemMeta(meta);
        return beacon;
    }

    private ItemStack buyTokens(){
        ItemStack token = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = token.getItemMeta();
        meta.displayName(Component.text("Buy Tokens", NamedTextColor.GREEN, TextDecoration.ITALIC));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Click to buy Tokens", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        token.setItemMeta(meta);
        return token;
    }

    private ItemStack vendorHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(key.append(name).append(key));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Buy and Sell Excotic Items", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/25fafa2be55bd15aea6e2925f5d24f8068e0f4a2616f3b92b380d94912f0ec5f");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public Double getCoalOrePrice() {
        return coalOrePrice;
    }

    public Double getIronOrePrice() {
        return ironOrePrice;
    }

    public Double getRedStoneOrePrice() {
        return redstoneOrePrice;
    }

    public Double getEmeramldOrePrice() {
        return emerabldPrice;
    }

    private void checkForItems(Player whoToCheck, ItemStack itemToCheckFor, Double price) {
        Boolean doesPlayerHaveItem = false;
        for (ItemStack s : whoToCheck.getInventory().getContents()) {
            if (s == null || s.getItemMeta() == null || !s.getItemMeta().hasDisplayName()) {
                continue;
            }
            if (s.getItemMeta().displayName() != null
                    && s.getItemMeta().displayName().equals(itemToCheckFor.getItemMeta().displayName())) {
                doesPlayerHaveItem = true;
                break;
            }
        }
        if (doesPlayerHaveItem) {
            whoToCheck.getInventory().removeItem(itemToCheckFor);
            whoToCheck.sendMessage(Component.text("You sold ").append(itemToCheckFor.getItemMeta().displayName())
                    .append(Component.text(" for ", NamedTextColor.GREEN))
                    .append(Component.text("$", NamedTextColor.GREEN))
                    .append(Component.text(price.doubleValue(), NamedTextColor.GREEN)));
            whoToCheck.playSound(whoToCheck.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            PlayerConfigManager config = new PlayerConfigManager(whoToCheck.getUniqueId().toString());
            config.addBalance(price);
        } else {
            whoToCheck.sendMessage(Component.text("You do not have any ", NamedTextColor.RED, TextDecoration.ITALIC)
                    .append(itemToCheckFor.getItemMeta().displayName()
                            .hoverEvent(Component.text("Go get some!").color(NamedTextColor.DARK_PURPLE)
                                    .decorate(TextDecoration.ITALIC)))
                    .append(Component.text(" to sell!", NamedTextColor.RED)));
            whoToCheck.playSound(whoToCheck, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
        }
    }
}
