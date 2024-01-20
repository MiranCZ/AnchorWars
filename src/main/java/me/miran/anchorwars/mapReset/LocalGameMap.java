package me.miran.anchorwars.mapReset;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap {

    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalGameMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        if (loadOnInit) load();
    }

    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis()
        );

        try {
            if (!FileUtil.copy(sourceWorldFolder, activeWorldFolder)) {
                return false;
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to load GameMap from source folder " + sourceWorldFolder.getName());
            e.printStackTrace();
            return false;
        }
        this.bukkitWorld = Bukkit.createWorld(
                new WorldCreator(activeWorldFolder.getName())
        );

        if (bukkitWorld != null) this.bukkitWorld.setAutoSave(false);

        return isLoaded();
    }

    public void unload() {
        if (bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if (activeWorldFolder != null) me.miran.anchorwars.mapReset.FileUtil.delete(activeWorldFolder);


        bukkitWorld = null;
        activeWorldFolder = null;
    }

    public boolean restoreFromSource() {
        unload();
        return load();
    }

    public boolean isLoaded() {
        return getWorld() != null;
    }

    public World getWorld() {
        return bukkitWorld;
    }


}
