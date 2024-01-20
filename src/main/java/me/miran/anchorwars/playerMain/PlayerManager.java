package me.miran.anchorwars.playerMain;

import me.miran.anchorwars.gameManager.kitManager.Kit;
import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerManager implements PlayerCommands{
   private final Main main;
   private final Player p;
   private String team = "null";
   private Kit kit = null;
   private PlayerAction action;
   private HashMap<String, Integer> upgrades = new HashMap<>();
   private int protectedFor = 0;

   private int id = 0;

    public boolean isProtected() {
        return !(protectedFor == 0);
    }

    public void reset () {
        team = "null";
        upgrades = new HashMap<>();
    }

    public void setProtected(double seconds) {
        int prot = (int) Math.round( seconds *2);

        if (protectedFor < prot) {
            protectedFor = prot;
        }

        if (id != 0) {

        }
    }



    public PlayerManager(Player p, Main main) {
        this.p = p;
        this.main = main;
    }


    public boolean setTeam(String team) {
        if (main.teams.getConfig().getString("Teams." + team + ".name") != null) {
            this.team = team;
            return true;
        }
        return false;
    }


    public void setAction(PlayerAction action) {
    this.action = action;
    }


    public void sendMessage(HashMap<String, Boolean> message) {
        //lang and stuff

    }


    public void setUpgrade(String upgrade, int lvl) {
    upgrades.put(upgrade, lvl);
    }


    public void setKit(Kit kit) {
    this.kit = kit;
    }


    public boolean isInGame() {
        return !(!main.map.isLoaded() || main.map.getWorld() != p.getWorld());
    }


    public boolean isInLobby() {
        return main.wLobby.isLoaded() && main.wLobby.getWorld() == p.getWorld();
    }

    public boolean hasUpgrade (String upgrade) {
        return upgrades.get(upgrade) != null && upgrades.get(upgrade) != 0;
    }


    public int getUpgrade(String upgrade) {
        if (upgrades.get(upgrade) != null) {
            return upgrades.get(upgrade);
        }
        return 0;
    }


    public String getTeam() {
        return team;
    }


    public Kit getKit() {
        return kit;
    }

    public PlayerAction getAction () {
        return action;
    }

    public void generateItem() {

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {
                protectedFor--;
                if (protectedFor == 0) {
                    Bukkit.getScheduler().cancelTask(id);
                    id = 0;
                }


                }
            }, 0L, 10L);

    }

}
