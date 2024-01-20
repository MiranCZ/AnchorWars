package me.miran.anchorwars.mapReset;

import org.bukkit.Bukkit;

import java.io.*;

public final class FileUtil {
    public static boolean copy (File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();

            if (files == null) return false;

            for (String file : files) {
                File newSource = new File(source, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }

        } else {
            InputStream in;
            try {
                in = new FileInputStream(source);
            } catch (FileNotFoundException ex) {
                Bukkit.getLogger().severe("[AnchorWars]====================================================");
                Bukkit.getLogger().severe("[AnchorWars] Save was not found! (" + ex + ")");
                Bukkit.getLogger().severe("[AnchorWars]====================================================");
                return false;
            }
            OutputStream out = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
        return true;
    }

    public static  void delete (File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File child : files) {
                delete(child);
            }
        }
        file.delete();
    }
}
