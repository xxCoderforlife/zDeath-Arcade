package dev.nullpointercoding.zdeatharcade.Listeners;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import com.destroystokyo.paper.profile.PlayerProfile;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Bank.BankAccountGUI;
import dev.nullpointercoding.zdeatharcade.ShootingRange.TheRange;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.SavePlayerInventoryToFile;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import net.milkbowl.vault.economy.Economy;

public class PlayerListeners implements Listener {

    private HashMap<Player, UUID> playersInRange = TheRange.playersInRange;
    private HashMap<Player, Inventory> playerInv = new HashMap<Player, Inventory>();
    private Economy econ = new VaultHook();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        SavePlayerInventoryToFile SPI = new SavePlayerInventoryToFile(p.getUniqueId().toString());
        if (SPI.getPlayersThatQuitConfig() == null)
            SPI.playersThatQuit();
        if (SPI.getPlayersThatQuitConfig().contains("PlayersThatQuitWhileInTheRange." + p.getName())) {
            p.getInventory().clear();
            SPI.getPlayersThatQuitConfig().set("PlayersThatQuitWhileInTheRange." + p.getName(), null);
            try {
                SPI.getPlayersThatQuitConfig().save(SPI.getPlayersThatQuit());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            p.teleportAsync(p.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
            try {
                p.getInventory().setContents(SavePlayerInventoryToFile
                        .itemStackArrayFromBase64(SPI.getConfig().getString(p.getName() + ".InvAsBase64")));
            } catch (IOException ex) {
                Bukkit.getConsoleSender().sendMessage(ex.getStackTrace().toString());
            }
            p.sendTitlePart(TitlePart.TITLE, Component.text("§a§oINVENTORY RESTORED"));
            p.sendTitlePart(TitlePart.SUBTITLE, Component.text("§cYou left the Range"));
            SPI.deletePlayerInventoryFile();
        }
        // Check if Player config exists
        Economy econ = Main.getInstance().getEconomy();
        if (econ != null) { // add null check here
            if (econ.createPlayerAccount(p)) {
                Bukkit.getConsoleSender().sendMessage("Account created for: " + p.getName());
                PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId().toString());
                PCM.updatePlayerDataFile(p);
            } else {
                Bukkit.getConsoleSender().sendMessage("Account already exists for: " + p.getName());
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("Economy is null.");
        }
        joinedWithBounty(p);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (playersInRange.containsKey(p)) {
            SavePlayerInventoryToFile SPI = new SavePlayerInventoryToFile(p.getUniqueId().toString());
            SPI.playersThatQuit();
            YamlConfiguration pConfig = SPI.getPlayersThatQuitConfig();
            pConfig.set("PlayersThatQuitWhileInTheRange." + p.getName(), p.getUniqueId().toString());
            try {
                pConfig.save(SPI.getPlayersThatQuit());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        leftWithBounty(p);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = (Player) e.getPlayer();
        if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent playerKiller = (EntityDamageByEntityEvent) p.getLastDamageCause();
            if (playerKiller.getDamager() instanceof Zombie) {
                PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId().toString());
                PCM.setDeaths(PCM.getDeaths() + 1);
                PCM.saveConfig();
                final Component deathMessage = Component
                        .text("§c§l" + p.getName() + " §7was killed by §c§l" + playerKiller.getDamager().getName()
                                + " and has died " + "§e§o" + PCM.getDeaths().intValue() + " times.");
                playerInv.put(p, p.getInventory());
                e.deathMessage(deathMessage);
            }
        }
        if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent playerKiller = (EntityDamageByEntityEvent) p.getLastDamageCause();
            if (playerKiller.getDamager() instanceof Player) {
                PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId().toString());
                PCM.setDeaths(PCM.getDeaths() + 1);
                PCM.saveConfig();
                final Component deathMessage = Component
                        .text("§c§l" + p.getName() + " §7was killed by §c§l" + playerKiller.getDamager().getName()
                                + " and has died " + "§e§o" + PCM.getDeaths().intValue() + " times.");
                playerInv.put(p, p.getInventory());
                e.deathMessage(deathMessage);
                checkForBounites(p);
                List<ItemStack> d = e.getDrops();
                d.clear();
            }
        }
    }

    @EventHandler
    public void onPlayerReSpawn(PlayerRespawnEvent e) {
        Player p = (Player) e.getPlayer();
        final Double toTake = econ.getBalance(p) * 0.23;
        if (playerInv.containsKey(p)) {
            econ.withdrawPlayer(p, toTake);
            p.sendTitlePart(TitlePart.TITLE, Component.text("§4§lYOU DIED"));
            p.sendTitlePart(TitlePart.SUBTITLE, Component.text("You lost §e§o" + toTake.toString() + "§4§l$"));
            p.teleportAsync(p.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
            p.getInventory().clear();
            p.getInventory().setContents(playerInv.get(p).getContents());
            playerInv.remove(p);
        }
    }

    private void checkForBounites(Player p) {
        for (Entry<Player, HashMap<Player, Double>> m : BankAccountGUI.getBountyList().entrySet()) {
            if (m.getValue().containsKey(p)) {
                econ.withdrawPlayer(m.getKey(), m.getValue().get(p).doubleValue());
                m.getValue().remove(p);
                Bukkit.broadcast(Component.text(p.getKiller() + " has claimed the bounty on " + p.getName()
                        + " and has been rewarded with " + m.getValue().get(p).doubleValue() + "$"));
                econ.depositPlayer(p.getKiller(), m.getValue().get(p).doubleValue());
                p.getKiller().getInventory().addItem(bountyHead());
            }
        }
    }

    private void joinedWithBounty(Player playerWithBounty) {
        PlayerConfigManager pcm = new PlayerConfigManager(playerWithBounty.getUniqueId().toString());
        if (pcm.hasBounty()) {
            Bukkit.broadcast(Component.text(playerWithBounty.getName() + " has joined and still has a bounty of "
                    + pcm.getBounty() + "$"));

        }
    }

    private void leftWithBounty(Player playerWithBounty) {
        PlayerConfigManager pcm = new PlayerConfigManager(playerWithBounty.getUniqueId().toString());
        if (pcm.hasBounty()) {
            Bukkit.broadcast(Component.text(playerWithBounty.getName() + " has left and still has a bounty of "
                    + pcm.getBounty() + "$"));
        }
    }

    private ItemStack bountyHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(Component.text("§c§lBounty Head"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Bounty Board"));
        PlayerProfile profile = getProfile(
                "https://textures.minecraft.net/texture/2c57e391e36801da12714cf7bcaed71e2c57fde4815afb692445f2b1393cd520");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the
                                                                                                     // same "random"
                                                                                                     // UUID all the
                                                                                                     // time

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example:
                                      // https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }

}
