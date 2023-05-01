package dev.nullpointercoding.zdeatharcade.Listeners;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.ShootingRange.TheRange;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.SavePlayerInventoryToFile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import net.milkbowl.vault.economy.Economy;

public class PlayerListeners implements Listener {

    private HashMap<Player, UUID> playersInRange = TheRange.playersInRange;

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
            if(econ.createPlayerAccount(p)){
                Bukkit.getConsoleSender().sendMessage("Account created for: " + p.getName());
                PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId().toString());
                PCM.updatePlayerDataFile(p);
            }else{
                Bukkit.getConsoleSender().sendMessage("Account already exists for: " + p.getName());
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("Economy is null.");
        }
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
    }

}
