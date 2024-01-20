package me.miran.anchorwars;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.playerMain.PlayerManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomMethods {

    private final HashMap<Material, ArrayList<ItemStack>> genPrizes = new HashMap<>();
    private final HashMap<Material, ArrayList<Integer>> resetTime = new HashMap<>();
    private final HashMap<Block, Material> genItems = new HashMap<>();
    private final HashMap<Block, Integer> genLvl = new HashMap<>();
    private final ArrayList<Player> lobbyPlayers = new ArrayList<>();
    Main main;
    private HashMap<String, Integer> anchorLvl = new HashMap<>();
    public CustomMethods(Main main) {
        this.main = main;
    }

    public void reset() {
        anchorLvl = new HashMap<>();
        lobbyPlayers.clear();

    }

    public void setGens() {

        ArrayList<Material> genList = new ArrayList<>();
        int i = 0;
        String id = "." + i;

        while (main.gens.getConfig().getString("Generators" + ".allowedGens" + id) != null) {
            Material m = Material.valueOf(main.gens.getConfig().getString("Generators" + ".allowedGens" + id));
            genList.add(m);

            i++;
            id = "." + i;
        }

        for (i = 0; i < genList.size(); i++) {
            int n = 0;
            id = "." + n;

            int cost = main.gens.getConfig().getInt("Generators" + ".data." + genList.get(i).toString() + id + ".cost");
            String costMaterialS = main.gens.getConfig().getString("Generators" + ".data." + genList.get(i).toString() + id + ".costMaterial");
            int time = main.gens.getConfig().getInt("Generators" + ".data." + genList.get(i).toString() + id + ".time");

            if (time == 0) {
                time = 999;
            }

            String timeCheck = main.gens.getConfig().getString("Generators" + ".data" + genList.get(i).toString() + id + ".time");

            ArrayList<Integer> timeList = new ArrayList<>();
            ArrayList<ItemStack> prizeList = new ArrayList<>();

            n = 0;

            while (costMaterialS != null || timeCheck != null) {
                timeList.add(n, time);

                prizeList.add(n, new ItemStack(Material.valueOf(costMaterialS), cost));

                n++;
                id = "." + n;
                cost = main.gens.getConfig().getInt("Generators" + ".data." + genList.get(i).toString() + id + ".cost");
                costMaterialS = main.gens.getConfig().getString("Generators" + ".data." + genList.get(i).toString() + id + ".costMaterial");
                time = main.gens.getConfig().getInt("Generators" + ".data." + genList.get(i).toString() + id + ".time");

                timeCheck = main.gens.getConfig().getString("Generators" + ".data" + genList.get(i).toString() + id + ".time");
            }


            resetTime.put(genList.get(i), timeList);
            genPrizes.put(genList.get(i), prizeList);
        }
    }


    public ArrayList<Player> getTeamPlayers(String team) {
        ArrayList<Player> players = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerManager manager = main.pl.getP(p);
            if (manager.getTeam().equals(team)) {
                players.add(p);
            }
        }
        return players;
    }


    public void addToLobby(Player p) {
        lobbyPlayers.add(p);
    }

    public void removeFromLobby(Player p) {
        lobbyPlayers.remove(p);
    }

    public boolean isInLobby(Player p) {
        return lobbyPlayers.contains(p);
    }


    public int getGenLvl(Block b) {
        int lvl;

        if (genLvl.get(b) != null) {
            lvl = genLvl.get(b);
        } else {
            lvl = -1;
        }
        return lvl;
    }

    public void setGenLvl(Block b, int lvl) {

        if (genLvl.get(b) != null) {
            genLvl.put(b, lvl);

        } else {
            genLvl.remove(b);
            genLvl.put(b, lvl);

        }


    }

    public Material getGenItem(Block b) {
        Material item = null;
        if (genItems.get(b) != null) {
            item = genItems.get(b);
        }


        return item;
    }

    public void setGenItem(Block b, Material item) {
        genItems.put(b, item);
    }


    public ArrayList getStarterValue(Material m) {
        ArrayList<Integer> values = new ArrayList<>();
        if (resetTime.get(m) != null) {
            values = resetTime.get(m);

        }
        return values;
    }

    public ArrayList getGenUpgradePrize(Material item) {

        ArrayList<ItemStack> values = new ArrayList<>();
        if (genPrizes.get(item) != null) {
            values = genPrizes.get(item);
        }


        return values;
    }

    public void createAnchor(String team) {
        anchorLvl.put(team, 100);
    }

    public void setAnchor(String team, int level, boolean bypass) {

        if (anchorLvl.get(team) > level && !main.anchorDmg && !bypass) {
            return;
        }

        if (level > 100) {
            level = 100;
        } else if (level < 0) {
            level = 0;
        }
        if (anchorLvl.get(team) != null) {
            anchorLvl.put(team, level);
        }
    }

    public void destroyAllAnchors() {
        anchorLvl.replaceAll((s, v) -> 0);
    }


    public int getAnchorLevel(String team) {

        int level;
        if (anchorLvl.get(team) != null) {
            level = anchorLvl.get(team);
        } else {
            level = -1;
        }

        return level;
    }


    public double getEnchantCost(Player p) {
        ItemStack handI = p.getInventory().getItemInMainHand();
        if (!Enchantment.DURABILITY.canEnchantItem(handI)) {
            return -1;
        }
        Double cost = 10.0;

        ItemMeta itemM = handI.getItemMeta();
        Integer maxLvl = 0;
        Integer lvl = 0;
        Integer enchantAmount = 0;
        if (itemM.hasEnchants()) {
            for (Enchantment e : itemM.getEnchants().keySet()) {

                maxLvl = maxLvl + e.getMaxLevel();
                lvl = lvl + itemM.getEnchantLevel(e);
                enchantAmount = enchantAmount + 1;
            }
            Double maxLvlD = Double.parseDouble(maxLvl.toString());
            Double lvlD = Double.parseDouble(lvl.toString());

            cost = 10 * (((lvlD + maxLvlD) / maxLvlD) * enchantAmount);
        }

        return cost;
    }

    public void tpToLobby(Player p) {
        if (!main.lobby.isLoaded()) {
            main.lobby.load();
        }
        if (isInLobby(p)) {
            removeFromLobby(p);
        }
        Location loc = main.mainLobby;

        if (loc == null) {
            if (p.isOp()) {
                p.sendMessage(ChatColor.RED + "ERROR! Main Lobby is not set up, please set it up! It can cause unexpected errors!");
            } else {
                p.sendMessage(ChatColor.RED + "ERROR! Could not find main lobby, so you were spawned on default world. Please contact server staff about this issue!");
            }
            return;
        }

        loc.setWorld(main.lobby.getWorld());
        p.getInventory().clear();
        p.setExp(0);
        p.getActivePotionEffects().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() + 1.5, loc.getZ() + 0.5, loc.getYaw(), loc.getPitch()));

    }

    public void resetJoinSign() {
        Location loc = main.joinSign;
        if (!main.lobby.isLoaded()) {
            main.lobby.load();

        }

        loc.setWorld(main.lobby.getWorld());
        loc.getWorld().getBlockAt(loc).setType(Material.OAK_WALL_SIGN);


        Sign sign = (Sign) loc.getWorld().getBlockAt(loc).getState();


        sign.setLine(0, ChatColor.RED + "Anchor Wars");
        sign.setLine(1, ChatColor.GREEN + "" + ChatColor.BOLD + "Click here to ");
        sign.setLine(2, ChatColor.GREEN + "" + ChatColor.BOLD + "join!");
        sign.setLine(3, ChatColor.DARK_GREEN + "0/" + main.minPlayerCount + " players");

        WallSign wallSign = (org.bukkit.block.data.type.WallSign) sign.getBlockData();

        wallSign.setFacing(getBlockFace(loc));

        sign.setBlockData(wallSign);

        sign.update();


    }

    private BlockFace getBlockFace(Location location) {
        BlockFace face;

        face = getSide(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ() - 1), BlockFace.SOUTH, BlockFace.NORTH);
        face = getSide(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ() + 1), BlockFace.NORTH, face);
        face = getSide(new Location(location.getWorld(), location.getX() - 1, location.getY(), location.getZ()), BlockFace.EAST, face);
        face = getSide(new Location(location.getWorld(), location.getX() + 1, location.getY(), location.getZ()), BlockFace.WEST, face);
        return face;
    }

    private BlockFace getSide(Location location, BlockFace ifTrue, BlockFace ifFalse) {
        if (location.getWorld().getBlockAt(location).getType().isSolid()) {
            return ifTrue;
        }

        return ifFalse;
    }

    public boolean isInt(String string) {
        if (string == null) {
            return false;
        }

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isDouble(String string) {
        if (string == null) {
            return false;
        }

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }


}


