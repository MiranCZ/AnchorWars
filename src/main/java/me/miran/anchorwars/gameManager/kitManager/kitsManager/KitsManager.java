package me.miran.anchorwars.gameManager.kitManager.kitsManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class KitsManager {
    private LoadKits loadKits = new LoadKits();
    private SaveKits saveKits = new SaveKits();

    private Main main;
    private FileConfiguration mapConfig = null;
    private File configFile = null;

    public KitsManager(Main main) {
        this.main = main;
        saveDefaultConfig();
    }



    public void reloadConfig() {
        if (this.configFile == null)  {
            this.configFile = new File(this.main.getDataFolder(), "kits.yml") ;
        }
        this.mapConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.main.getResource("kits.yml");

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.mapConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig () {
        if (this.mapConfig == null) {
            reloadConfig();
        }

        return this.mapConfig;
    }

    public void saveConfig() {
        if (this.mapConfig == null || this.configFile == null){
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            this.main.getLogger().log(Level.SEVERE, "Could not save config (kits) to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig () {
        if(this.configFile == null) {
            this.configFile = new File(this.main.getDataFolder(), "kits.yml");
        }
        if (!this.configFile.exists()) {
            this.main.saveResource("kits.yml", false);
        }
    }

    public void save() {
        saveKits.save();
    }

    public void load() {
        loadKits.load();
    }

}

