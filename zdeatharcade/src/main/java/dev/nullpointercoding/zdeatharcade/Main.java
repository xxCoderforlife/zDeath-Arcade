package dev.nullpointercoding.zdeatharcade;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import dev.nullpointercoding.zdeatharcade.Commands.Commands;
import dev.nullpointercoding.zdeatharcade.Commands.TabCommands;
import dev.nullpointercoding.zdeatharcade.Listeners.PlayerListeners;
import dev.nullpointercoding.zdeatharcade.PlayerAccount.PlayerAccountCommands;
import dev.nullpointercoding.zdeatharcade.ShootingRange.RangeGunSmith;
import dev.nullpointercoding.zdeatharcade.ShootingRange.TheRange;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.EcoCommands;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.EcoTabCommands;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultChat;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import dev.nullpointercoding.zdeatharcade.Zombies.ZombieHandler;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static Random r;
    private static Random spawmChance;
    private static Economy econ = null;
    private VaultChat vaultChat;
    private Boolean isRangeSpawnSet;
    private File zombieSpawnLocations = new File(getDataFolder() + File.separator + "Zombie Spawn Locations");
    private File rangeConfigFolder = new File(getDataFolder() + File.separator + "Shooting Range");
    private File playerInventoryDataFolder = new File(getDataFolder() + File.separator + "Player Inventory Data");
    private File NPCDataFolder = new File(getDataFolder() + File.separator + "NPC Data");
    private File PlayerDataFolder = new File(getDataFolder() + File.separator + "Player Data");
    private File bankDataFolder = new File(getDataFolder() + File.separator + "Bank Data");

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        r = new Random();
        spawmChance = new Random();
        createConfigFolders();
        RegisterCommandsandEvents();
        if (!setupEconomy()) {
            System.out.println("Vault not found! Disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
        }
        System.out.println("ZDeathArcade has been enabled!");

    }

    @Override
    public void onLoad() {
        // Plugin startup logic
        System.out.println("ZDeathArcade has been loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("ZDeathArcade has been disabled!");
    }

    public static Main getInstance() {
        return plugin;
    }

    public static Random getRandom() {
        return r;
    }

    public static Random getSpawnChance() {
        return spawmChance;
    }

    private void registerEcon() {
        Bukkit.getServer().getServicesManager().register(Economy.class, new VaultHook(), plugin, ServicePriority.Highest);
    }

    private void RegisterCommandsandEvents() {
        getCommand("zdeatharcade").setExecutor(new Commands());
        getCommand("zdeatharcade").setTabCompleter(new TabCommands());
        getCommand("economy").setExecutor(new EcoCommands());
        getCommand("economy").setTabCompleter(new EcoTabCommands());
        getCommand("account").setExecutor(new PlayerAccountCommands());
        getServer().getPluginManager().registerEvents(new ZombieHandler(), this);
        getServer().getPluginManager().registerEvents(new TheRange(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new RangeGunSmith(), this);
        registerEcon();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void createConfigFolders() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aZDeathArcade folder has been created!");
        }
        if (!zombieSpawnLocations.exists()) {
            zombieSpawnLocations.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aZombie Spawn Locations folder has been created!");

        }
        if (!playerInventoryDataFolder.exists()) {
            playerInventoryDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayer Inventory Data folder has been created!");

        }
        if (!NPCDataFolder.exists()) {
            NPCDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aNPCData folder has been created!");

        }
        if (!PlayerDataFolder.exists()) {
            PlayerDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayerData folder has been created!");
        }
        if(!bankDataFolder.exists()){
            bankDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aBankData folder has been created!");
        }
        if (!rangeConfigFolder.exists()) {
            rangeConfigFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aShooting Range folder has been created!");
            isRangeSpawnSet = false;
        } else {
            for (File f : rangeConfigFolder.listFiles()) {
                if (f.getName().equalsIgnoreCase("config.yml")) {
                    Bukkit.getConsoleSender().sendMessage("§aShooting Range config.yml has been found!");
                    Bukkit.getConsoleSender().sendMessage("§aSetting ShootingRanage to true");
                    isRangeSpawnSet = true;
                    return;

                }
            }
        }

    }

    public File getZombieSpawnLocationFolder() {
        return zombieSpawnLocations;
    }

    public File getRangeConfigFolder() {
        return rangeConfigFolder;
    }

    public Boolean getIsRangeSpawnSet() {
        return isRangeSpawnSet;
    }

    public File getPlayerInventoryDataFolder() {
        return playerInventoryDataFolder;
    }

    public File getNPCDataFolder() {
        return NPCDataFolder;
    }

    public File getPlayerDataFolder() {
        return PlayerDataFolder;
    }
    public File getBankDataFolder(){
        return bankDataFolder;
    }

    public static Economy getEconomy() {
        return econ;
    }

}
