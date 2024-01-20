package me.miran.anchorwars;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class DenyManager {

    Main main;
    ArrayList<Player> justSent = new ArrayList<>();
    HashMap<Player, ArrayList<String>> denyList = new HashMap<>();
    public DenyManager(Main main) {
        this.main = main;
    }

    public void add(Player p, String deny) {
        if (!justSent.contains(p) || !denyList.get(p).contains(deny)) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', deny));
            justSent.add(p);
            ArrayList<String> list = new ArrayList<>();
            if (denyList.get(p) != null) {
                list = denyList.get(p);
            }
            list.add(deny);
            denyList.put(p, list);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run() {
                    reset(p);
                }
            }, 20L);
        }
    }

    private void reset(Player p) {
        denyList.get(p).clear();
        justSent.remove(p);
    }

}
