package me.miran.anchorwars.worldsManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class WorldManager {

    private final LoadWorld loadWorld = new LoadWorld();
    private final SaveWorld saveWorld = new SaveWorld();

    private final Main main;
    private FileConfiguration mapConfig = null;
    private File configFile = null;

    public WorldManager(Main main) {
        this.main = main;
        saveDefaultConfig();
    }


    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.main.getDataFolder(), "world.yml");
        }
        this.mapConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.main.getResource("world.yml");

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.mapConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.mapConfig == null) {
            reloadConfig();
        }

        return this.mapConfig;
    }

    public void saveConfig() {
        if (this.mapConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            this.main.getLogger().log(Level.SEVERE, "Could not save config (world) to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.main.getDataFolder(), "world.yml");
        }
        if (!this.configFile.exists()) {
            this.main.saveResource("world.yml", false);
        }
    }

    public void save() {
        saveWorld.save();
    }

    public void load() {
        loadWorld.load();
    }

}



