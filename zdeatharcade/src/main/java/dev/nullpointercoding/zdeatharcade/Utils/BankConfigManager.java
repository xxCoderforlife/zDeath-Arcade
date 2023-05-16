package dev.nullpointercoding.zdeatharcade.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.nullpointercoding.zdeatharcade.Main;

public class BankConfigManager {

    private Main plugin = Main.getInstance();
    private File configFile;
    private FileConfiguration config;
    private String configName;
    private Boolean isConfigCreated;

    public BankConfigManager(String configName) {
        this.configName = configName;
        configHandler();
    }

    public String getConfigName() {
        return configName;
    }

    public FileConfiguration getConfig() {
        return (YamlConfiguration) config;
    }

    public File getFile() {
        return configFile;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.saveResource(configName, false);
        }
    }

    private void configHandler() {
        configFile = new File(plugin.getBankDataFolder() + File.separator + configName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
            isConfigCreated = true;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Boolean isConfigCreated() {
        return isConfigCreated;
    }
}
