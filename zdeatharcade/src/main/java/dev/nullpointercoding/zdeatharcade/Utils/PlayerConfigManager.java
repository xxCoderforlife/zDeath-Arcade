package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Statistic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;

public class PlayerConfigManager {

    private Main plugin = Main.getInstance();
    private File configFile;
    private FileConfiguration config;
    private String configName;
    private final String configPath;

    
    public enum configPaths{
        PLAYER("Player"),
        UUID("UUID"),
        CLIENTBRAND("Player-Client-Brand"),
        LASTLOGIN("LastLogin"),
        ZOMBIEKILLS("Zombie-Kills"),
        DEATHS("Deaths"),
        CASH("Cash"),
        TOKENS("Tokens"),
        BOUNTY("Bounty"),
        BOUNTYHASBOUNTY("Bounty.Has-Bounty"),
        BOUNTYAMOUNT("Bounty.Bounty-Amount"),
        SETTINGS("Settings"),
        SETTINGS_PLAYER_VISABLE(".Player-Visable"),
        SETTINGS_ZOMBIE_VISABLE(".Zombie-Visable");

        private String path;

        configPaths(String path){
            this.path = path;
        }

        public String getPath(){
            return path;
        }
    }

    public PlayerConfigManager(String configName) {
        this.configName = configName;
        this.configPath = configName + '.';
        configHandler();
    }

    public String getConfigName() {
        return configName;
    }

    public FileConfiguration getConfig() {
        return (YamlConfiguration) config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.saveResource(configName, false);
        }

    }

    private void configHandler() {
        configFile = new File(plugin.getPlayerDataFolder() + File.separator + configName + ".yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerDataFile(Player p) {
        YamlConfiguration playerConfig = (YamlConfiguration) config;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = now.format(myFormatObj);

        playerConfig.set(configPath + configPaths.PLAYER.getPath(), p.getName());
        playerConfig.set(configPath + configPaths.UUID.getPath(), p.getUniqueId().toString());
        playerConfig.set(configPath + configPaths.CLIENTBRAND.getPath(), p.getClientBrandName());
        playerConfig.set(configPath + configPaths.LASTLOGIN.getPath(), formattedDate);
        playerConfig.set(configPath + configPaths.ZOMBIEKILLS.getPath(), 0);
        playerConfig.set(configPath + configPaths.DEATHS.getPath(), 0);
        playerConfig.set(configPath + configPaths.CASH.getPath(), 1500);
        playerConfig.set(configPath + configPaths.TOKENS.getPath(), 0);
        playerConfig.set(configPath + configPaths.BOUNTYHASBOUNTY.getPath(), false);
        playerConfig.set(configPath + configPaths.BOUNTYAMOUNT.getPath(), 0);
        playerConfig.set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_PLAYER_VISABLE.getPath(), true);
        playerConfig.set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_ZOMBIE_VISABLE.getPath(), true);

        saveConfig();

    }

    public Double getBalance() {
        return VaultHook.round(getConfig().getDouble(configPath + configPaths.CASH.getPath()), 2);
    }

    public void setBalance(Double balance) {
        VaultHook.round(balance, 2);
        getConfig().set(configPath + configPaths.CASH.getPath(), balance.doubleValue());
        saveConfig();
    }

    public void addBalance(Double balance) {
        VaultHook.round(balance, 2);
        getConfig().set(configPath + configPaths.CASH.getPath(), getBalance() + balance.doubleValue());
        saveConfig();
    }

    public void setLastLogin(String dateandtime) {
        getConfig().set(configPath + configPaths.LASTLOGIN.getPath(), dateandtime);
        saveConfig();
    }

    public void setClientBrand(String clientBrand) {
        getConfig().set(configPath + configPaths.CLIENTBRAND.getPath(), clientBrand);
        saveConfig();
    }

    public Double getTokens() {
        return VaultHook.round(getConfig().getDouble(configPath + configPaths.TOKENS.getPath()), 2);
    }

    public void addTokens(Double tokenToAdd){
        VaultHook.round(tokenToAdd, 2);
        getConfig().set(configPath + configPaths.TOKENS.getPath(), getTokens() + tokenToAdd);
        saveConfig();
    }

    public void setTokens(Double tokens) {
        getConfig().set(configPath + configPaths.TOKENS.getPath(), tokens);
        saveConfig();
    }

    public Double getKills() {
        return getConfig().getDouble(configPath + configPaths.ZOMBIEKILLS.getPath());
    }

    public void setKills(Player p) {
        getConfig().set(configPath + configPaths.ZOMBIEKILLS.getPath(), p.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE));
        saveConfig();
    }

    public Double getDeaths() {
        return getConfig().getDouble(configPath + configPaths.DEATHS.getPath());
    }

    public void setDeaths(Double deaths) {
        getConfig().set(configPath + configPaths.DEATHS.getPath(), deaths);
        saveConfig();
    }

    public Boolean hasBounty() {
        return getConfig().getBoolean(configPath + configPaths.BOUNTYHASBOUNTY.getPath());
    }

    public void setHasBounty(Boolean hasBounty, Double bountyAmount) {
        VaultHook.round(bountyAmount, 2);
        getConfig().set(configPath + configPaths.BOUNTYHASBOUNTY.getPath(), hasBounty);
        getConfig().set(configPath + configPaths.BOUNTYAMOUNT.getPath(), bountyAmount);
        saveConfig();
    }

    public Double getBounty() {
        return VaultHook.round(getConfig().getDouble(configPath + configPaths.CASH.getPath()), 2);
    }
    public Boolean isPlayerVisable() {
        return getConfig().getBoolean(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_PLAYER_VISABLE.getPath());
    }
    public Boolean isZombieVisable() {
        return getConfig().getBoolean(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_ZOMBIE_VISABLE.getPath());
    }
    public void setPlayerVisable(Boolean visable) {
        getConfig().set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_PLAYER_VISABLE.getPath(), visable);
        saveConfig();
    }
    public void setZombieVisable(Boolean visable) {
        getConfig().set(configPath + configPaths.SETTINGS.getPath() + configPaths.SETTINGS_ZOMBIE_VISABLE.getPath(), visable);
        saveConfig();
    }

}
