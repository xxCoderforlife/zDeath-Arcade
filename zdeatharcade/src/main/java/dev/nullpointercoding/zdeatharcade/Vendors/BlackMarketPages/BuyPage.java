package dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketPages;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.CustomInvFunctions;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.Pages;
import dev.nullpointercoding.zdeatharcade.Vendors.BlackMarketVendor;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.guns.utils.WeaponSounds;
import me.zombie_striker.qg.guns.utils.WeaponType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

public class BuyPage implements Listener {

    private final Component title = Component.text("        Buy Screen", NamedTextColor.GREEN, TextDecoration.BOLD);
    private Main plugin = Main.getInstance();
    private File gunConfigFile;
    private FileConfiguration gunConfig;
    private String itemName;
    private ArrayList<ItemStack> customGuns;
    private ArrayList<File> customGunFiles = new ArrayList<File>();
    private HashMap<Gun, Double> guns = new HashMap<Gun, Double>();
    private Gun gunV;


    public BuyPage() {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof BuyPage);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        for (File f : plugin.getCustomGunsFolder().listFiles()) {
            if (f.getName().endsWith(".yml")) {
                gunConfigFile = f;
            }
            try {
                gunConfig = new YamlConfiguration();
                gunConfig.load(gunConfigFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemName = gunConfig.getString("name");
            customGunFiles.add(f);
            if (QualityArmory.getGuns().next().getName().equals(itemName)) {
                gunV = QualityArmory.getGunByName(itemName);
                return;
            } else {
                gunV = convertCustomGuntoQAGun();
            }

        }
    }

    public void openInventory(Player player) {
        player.sendTitlePart(TitlePart.TITLE, Component.text("Loading Black Market....", NamedTextColor.LIGHT_PURPLE));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Please wait", NamedTextColor.DARK_PURPLE));
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
        new BukkitRunnable(){

            @Override
            public void run() {
                    customGuns = new ArrayList<ItemStack>();
                    customGuns.add(createGunItem(gunV, gunConfig.getDouble("price")));
                    guns.put(gunV, gunConfig.getDouble("price"));
                new Pages(player,customGuns, title);
            }
        }.runTaskLater(plugin, 20 * 4);
            
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (QualityArmory.isCustomItem(clicked)) {
            Player whoClicked = (Player) e.getWhoClicked();
            PlayerConfigManager pcm = new PlayerConfigManager(whoClicked.getUniqueId().toString());
            CustomBaseObject cbo = QualityArmory.getCustomItem(clicked);
            if (cbo instanceof Gun) {
                Gun gun = (Gun) cbo;
                if (guns.containsKey(gun)) {
                    if (pcm.getTokens() >= guns.get(gun)) {
                        pcm.setTokens(pcm.getTokens() - guns.get(gun));
                        whoClicked.getInventory().addItem(gun.getItemStack());
                        whoClicked.sendMessage(
                                Component.text("You have bought a " + gun.getDisplayName() + " for " + guns.get(gun) + "tokens"));
                    } else {
                        whoClicked.sendMessage(Component.text("You do not have enough Tokens to buy this gun",
                                NamedTextColor.RED, TextDecoration.ITALIC).hoverEvent(Component.text("Buy Tokens at the Black Market Dealer",NamedTextColor.GRAY)));
                    }
                }
            }
        }
        if(clicked.getItemMeta().displayName().equals(CustomInvFunctions.getBackButton().getItemMeta().displayName())){
            Player whoClicked = (Player) e.getWhoClicked();
            whoClicked.closeInventory(Reason.PLUGIN);
            new BlackMarketVendor().openInventory(whoClicked);
        }
    }


    private Gun convertCustomGuntoQAGun() {
        QualityArmory.createAndLoadNewGun(gunConfig.getString("name"), gunConfig.getString("displayname"),
                Material.valueOf(gunConfig.getString("material")), gunConfig.getInt("id"),
                WeaponType.valueOf(gunConfig.getString("weapontype")), WeaponSounds.GUN_BIG,
                gunConfig.getBoolean("enableIronSights"), gunConfig.getString("ammotype"), gunConfig.getInt("damage"),
                gunConfig.getInt("60"), gunConfig.getInt("price")).done();
        return QualityArmory.getGunByName(itemName);
    }

    private ItemStack createGunItem(Gun gun, Double price) {
        ItemStack gunItem = gun.getItemStack();
        ItemMeta meta = gunItem.getItemMeta();
        meta.displayName(Component.text("       " + gun.getDisplayName()));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Price: ", NamedTextColor.GREEN)
                .append(Component.text(price.intValue() + " Tokens", NamedTextColor.GOLD)));
        lore.add(Component.text("Ammo Type: ", NamedTextColor.YELLOW)
                .append(Component.text(gun.getAmmoType().getName(), NamedTextColor.WHITE)));
        lore.add(Component.text("Damage: ", NamedTextColor.RED)
                .append(Component.text(gun.getDamage(), NamedTextColor.WHITE)));
        lore.add(Component.text("Fire Rate: ", NamedTextColor.GREEN)
                .append(Component.text(gun.getFireRate(), NamedTextColor.WHITE)));
        lore.add(Component.text("Recoil: ", NamedTextColor.DARK_RED)
                .append(Component.text(gun.getRecoil(), NamedTextColor.WHITE)));
        lore.add(Component.text("Reload Time: ", NamedTextColor.AQUA)
                .append(Component.text(gun.getReloadTime(), NamedTextColor.WHITE)));
        lore.add(Component.text("Magazine Size: ", NamedTextColor.GOLD)
                .append(Component.text(gun.getMaxBullets(), NamedTextColor.WHITE)));
        lore.add(Component.text("Max Range: ", NamedTextColor.GRAY)
                .append(Component.text(gun.getMaxDistance() + " Blocks", NamedTextColor.WHITE)));
        meta.lore(lore);
        gunItem.setItemMeta(meta);
        guns.put(gun, price);
        return gunItem;

    }
}
