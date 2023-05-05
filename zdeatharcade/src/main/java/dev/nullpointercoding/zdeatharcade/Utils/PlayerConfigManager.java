package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import dev.nullpointercoding.zdeatharcade.Main;

public class PlayerConfigManager {
    

    private Main plugin = Main.getInstance();
    private File configFile;
    private FileConfiguration config;
    private String configName;


    public PlayerConfigManager(String configName){
        this.configName = configName;
        configHandler();
    }

    public String getConfigName(){
        return configName;
    }
    public FileConfiguration getConfig(){
        return (YamlConfiguration) config;
    }

    public void saveConfig(){
        try{
            config.save(configFile);
        }catch(IOException e){
            plugin.saveResource(configName, false);
        }

    }

    private void configHandler(){
        configFile = new File(plugin.getPlayerDataFolder() + File.separator + configName + ".yml");
        if(!configFile.exists()){
            configFile.getParentFile().mkdirs();
            try{
                configFile.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        config = new YamlConfiguration();
        try{
            config.load(configFile);
        }catch(IOException  | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public void updatePlayerDataFile(Player p){
        YamlConfiguration playerConfig = (YamlConfiguration) config;

        String configPath = new String(configName + '.');
        playerConfig.set(configPath + "Player", p.getName());
        playerConfig.set(configPath + "UUID", p.getUniqueId().toString());
        playerConfig.set(configPath + "Zombie-Kills", 0);
        playerConfig.set(configPath + "Deaths", 0);
        playerConfig.set(configPath + "Balance", 1500);
        playerConfig.set(configPath + "Bounty.Has-Bounty", false);
        playerConfig.set(configPath + "Bounty.Bounty-Amount", 0);

        saveConfig();


    }
    public Double getBalance(){
        return getConfig().getDouble(configName + ".Balance");
    }

    public void setBalance(Double balance){
        getConfig().set(configName + ".Balance", balance);
        saveConfig();
    }

    public Double getKills(){
        return getConfig().getDouble(configName + ".Zombie-Kills");
    }

    public void setKills(){
        Player p = Bukkit.getPlayer(configName);
        getConfig().set(configName + ".Zombie-Kills", p.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE));
        saveConfig();
    }

    public Double getDeaths(){
        return getConfig().getDouble(configName + ".Deaths");
    }

    public void setDeaths(Double deaths){
        getConfig().set(configName + ".Deaths", deaths);
        saveConfig();
    }

    public Boolean hasBounty(){
        return getConfig().getBoolean(configName + ".Bounty.Has-Bounty");
    }

    public void setHasBounty(Boolean hasBounty,Double bountyAmount){
        getConfig().set(configName + ".Bounty.Has-Bounty", hasBounty);
        getConfig().set(configName + ".Bounty.Bounty-Amount", bountyAmount);
        saveConfig();
    }

    public Double getBounty(){
        return getConfig().getDouble(configName + ".Bounty.Bounty-Amount");
    }
}

