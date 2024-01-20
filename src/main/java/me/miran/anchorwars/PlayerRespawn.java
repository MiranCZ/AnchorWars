package me.miran.anchorwars;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.kitManager.Kits;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class PlayerRespawn {

    Main main;
    Random rand = new Random();
    int r;
    private final Kits kit = new Kits(/*main*/);
    public PlayerRespawn(Main main) {
        this.main = main;
    }

    public void respawnPlayer(Player p, int looting) {

        if (!main.gameStart) {
            return;
        }

        ArrayList<Material> itemsToDrop = new ArrayList<>();
        itemsToDrop.add(Material.BRICK);
        itemsToDrop.add(Material.IRON_INGOT);
        itemsToDrop.add(Material.GOLD_INGOT);
        itemsToDrop.add(Material.DIAMOND);
        itemsToDrop.add(Material.NETHERITE_INGOT);


        String team = main.pl.getP(p).getTeam();

        int anchorDmg = main.customMe.getAnchorLevel(team);
        int i;
        int n;


        if (anchorDmg <= 0) {
            p.spigot().respawn();
            p.setGameMode(GameMode.SPECTATOR);
            String color = main.teams.getConfig().getString("Teams." + team + ".color");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "[&cAnchorWars&r]" + color + p.getName() + "&r is gone!"));
            main.pl.getP(p).setTeam("SPECTATOR");
            p.sendTitle(ChatColor.RED + "You died!", ChatColor.GRAY + "You can spectate the game now", 5, 30, 5);

            ArrayList<String> teams = new ArrayList<>();


            for (String existTeam : DataManager.teams) {
                if (main.customMe.getTeamPlayers(existTeam).size() > 0) {
                    teams.add(existTeam);

                }
            }

            if (teams.size() == 1) {

                String teamName = DataManager.names.get(teams.get(0));
                Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + ChatColor.GOLD + teamName + " has won the game!");


                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (main.customMe.getTeamPlayers(teams.get(0)).contains(player)) {
                        player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "VICTORY", "", 5, 30, 5);
                    } else {
                        player.sendTitle(ChatColor.RED + "GAME OVER", "", 5, 30, 5);
                    }
                }
            } else if (teams.size() == 0) {
                Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + ChatColor.RED + "Something went wrong! So count it as tie.");
            }

            if (teams.size() <= 1) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                    @Override
                    public void run() {
                        end();
                    }
                }, 300L);
            }

            Location loc = main.worldBCenter;
            loc.setWorld(main.map.getWorld());
            p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + 3, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);


            return;
        }
        if (anchorDmg > 25) {

            main.customMe.setAnchor(team, anchorDmg - 1, false);
        }

        ItemStack[] playerInv = p.getInventory().getContents();

        for (n = 0; n < itemsToDrop.size(); n++) {

            int has = 0;
            Material dropI = itemsToDrop.get(n);

            for (i = 0; i < playerInv.length - 1; i++) {
                ItemStack itemSlot = playerInv[i];
                if (itemSlot == null || !itemSlot.getType().equals(dropI))
                    continue;
                playerInv[i] = new ItemStack(Material.AIR, 0);
                has += itemSlot.getAmount();

            }
            if (has > 0) {
                if (looting > 0) {
                    r = rand.nextInt(has);
                    r = r - (r / looting);
                    double hasD = has;
                    double bonus = Math.round((hasD / 100 * (looting * 10 + r)));
                    has = (int) (has + bonus);


                }
                p.getWorld().dropItem(p.getLocation().add(0.5, 1, 0.5), new ItemStack(dropI, has));

            }


            p.getInventory().remove(new ItemStack(dropI, has));


        }
        p.getInventory().setContents(playerInv);
        if (main.phase.getPhase() >= 2) {
            kit.applyKit(p);
        }

        p.spigot().respawn();
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {

                tpToBase(p);
            }
        }, 1L);
    }

    public void tpToBase(Player p) {
        String team = main.pl.getP(p).getTeam();
        Location loc;

        if (team.equals("null")) {
            return;
        }

        loc = DataManager.spawns.get(team);

        if (loc == null) {
            loc = main.worldBCenter;
        }
        loc.setWorld(main.map.getWorld());


        p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 4));
        if (main.phase.getPhase() > 3) {
            p.addPotionEffect(PotionEffectType.GLOWING.createEffect(9999999, 1));

        }
    }

    public void end() {
        main.endGame.endGame();
    }


}
