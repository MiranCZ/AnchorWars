package me.miran.anchorwars.mapManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;



public class MapManager {

   private LoadMap loadMap = new LoadMap();
  private  SaveMap saveMap = new SaveMap();

    private Main main;
    private FileConfiguration mapConfig = null;
    private File configFile = null;

    public MapManager(Main main) {
        this.main = main;
        saveDefaultConfig();
    }



    public void reloadConfig() {
        if (this.configFile == null)  {
           this.configFile = new File(this.main.getDataFolder(), "map.yml") ;
        }
        this.mapConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.main.getResource("map.yml");

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
           this.main.getLogger().log(Level.SEVERE, "Could not save config (map) to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig () {
        if(this.configFile == null) {
            this.configFile = new File(this.main.getDataFolder(), "map.yml");
        }
        if (!this.configFile.exists()) {
            this.main.saveResource("map.yml", false);
        }
    }

    public void save() {
        saveMap.save();
    }

    public void load() {
        loadMap.load();
    }

}
