package me.miran.anchorwars.gameManager.teamManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {

    public static ArrayList<String> teams = new ArrayList<>();

    public static HashMap<String, String> names = new HashMap<>();
    public static HashMap<String, Location> spawns = new HashMap<>();
    public static HashMap<String, Location> anchors = new HashMap<>();
    public static HashMap<String, Location> shops = new HashMap<>();
    public static HashMap<String, Location> upgradeShops = new HashMap<>();
    public static HashMap<String, Material> items = new HashMap<>();

    public static HashMap<Material, String> teamByItem = new HashMap<>();
    public static HashMap<String, String> teamByAnchor = new HashMap<>();

    public static HashMap<Integer, String> teamId = new HashMap<>();

    Main main;

    public DataManager(Main main) {
        this.main = main;
    }

    public void restoreTeams() {
        int i = 0;
        String id = ".0";


        teams.clear();

        while (main.teams.getConfig().getString("Teams" + ".activeTeams" + id) != null) {
            String team = main.teams.getConfig().getString("Teams" + ".activeTeams" + id);


            teamId.put(i, team);

            teams.add(team);

            i++;
            id = "." + i;
        }


        for (String path : teams) {

            path = "." + path;

            String name = path.replace(".", "");//main.teams.getConfig().getString("Teams" + path + ".name");

            String teamName = main.teams.getConfig().getString("Teams" + path + ".name");
            names.put(name, teamName);

            Location spawn = main.loc.decompileLoc(main.teams.getConfig().getString("Teams" + path + ".data" + ".spawn"));
            if (spawn != null) {
                spawns.put(name, spawn);
            }

            String anchor = main.loc.compileLoc(main.loc.decompileLoc(main.teams.getConfig().getString("Teams" + path + ".data" + ".anchor")), false);
            if (anchor != null) {

                anchors.put(name, main.loc.decompileLoc(anchor));
                teamByAnchor.put(anchor, name);
            }

            Location shop = main.loc.decompileLoc(main.teams.getConfig().getString("Teams" + path + ".data" + ".shop"));
            if (shop != null) {
                shops.put(name, shop);
            }

            Location upgradeShop = main.loc.decompileLoc(main.teams.getConfig().getString("Teams" + path + ".data" + ".upgradeShop"));
            if (upgradeShop != null) {
                upgradeShops.put(name, upgradeShop);
            }
            String s = main.teams.getConfig().getString("Teams" + path + ".item");
            if (s != null) {
                Material item = Material.valueOf(s);
                teamByItem.put(item, name);
                items.put(name, item);
            }


        }


    }

    public void saveTeams() {
        int i;
        for (i = 0; i < teamId.size(); i++) {
            String name = teamId.get(i);
            String id = name + ".";
            main.teams.getConfig().set("Teams." + id + "data." + "spawn", main.loc.compileLoc(spawns.get(name), false));
            main.teams.getConfig().set("Teams." + id + "data." + "anchor", main.loc.compileLoc(anchors.get(name), false));
            main.teams.getConfig().set("Teams." + id + "data." + "shop", main.loc.compileLoc(shops.get(name), false));
            main.teams.getConfig().set("Teams." + id + "data." + "upgradeShop", main.loc.compileLoc(upgradeShops.get(name), false));
        }
        main.teams.saveConfig();
    }

}
