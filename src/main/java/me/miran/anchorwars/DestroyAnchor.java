package me.miran.anchorwars;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;


public class DestroyAnchor implements Listener {

    Main main;
    ArrayList<Player> damagedAnchor = new ArrayList<>();

    public DestroyAnchor(Main main) {
        this.main = main;
    }

    @EventHandler
    public void clickOnAnchor(PlayerInteractEvent e) {
        if (main.gameStart) {
            Block b = e.getClickedBlock();
            Player p = e.getPlayer();
            if (damagedAnchor.contains(p)) {
                e.setCancelled(true);
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (b.getType() == Material.RESPAWN_ANCHOR) {

                    String protcTeam;
                    String atcTeam = main.pl.getP(p).getTeam();

                    Location protcLoc = b.getLocation();


                    protcTeam = DataManager.teamByAnchor.get(main.loc.compileLoc(protcLoc, false)); //gets team owning attacked anchor


                    if (protcTeam == null || atcTeam == null) {

                        return;
                    }


                    if (protcTeam.equals(atcTeam)) {
                        main.deny.add(p, "&cYou can not attack your own anchor!");
                        e.setCancelled(true);
                        return;
                    }

                    if (!atcTeam.equals("null") && !atcTeam.equals("spectator") && p.getGameMode() != GameMode.SPECTATOR) {
                        if (main.customMe.getAnchorLevel(protcTeam) <= 0) {
                            return;
                        }
                        e.setCancelled(true);
                        World w = main.map.getWorld();

                        int dmg = 1;
                        if (main.phase.getPhase() > 2) {
                            dmg = 5;
                        }

                        main.customMe.setAnchor(protcTeam, main.customMe.getAnchorLevel(protcTeam) - dmg, false);

                        int anchorLvl = main.customMe.getAnchorLevel(protcTeam);
                        int charges = 4;

                        if (anchorLvl > 50) {
                            if (anchorLvl < 76) {
                                charges = 3;
                            }
                        } else if (anchorLvl > 25) {
                            charges = 2;
                        } else if (anchorLvl > 0) {
                            charges = 1;
                        }

                        //p.sendMessage(ChatColor.RED +  "You need to wait 1 second before attacking again.");
                        damagedAnchor.add(p);

                        int finalCharges = charges;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                            @Override
                            public void run() {
                                setCharges(b.getLocation(), finalCharges);
                            }
                        }, 1L);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                            @Override
                            public void run() {
                                removeFromList(p);
                            }
                        }, 20L);

                        for (Player teamP : main.customMe.getTeamPlayers(protcTeam)) {
                            teamP.playSound(teamP.getLocation(), Sound.BLOCK_ANVIL_LAND, 5, 1);
                        }

                        if (main.customMe.getAnchorLevel(protcTeam) == 0) {
                            ChatColor color = ChatColor.valueOf(protcTeam);

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle(color + "" + ChatColor.BOLD + protcTeam + ChatColor.DARK_RED + " respawn anchor", ChatColor.GRAY + "was destroyed!", 7, 45, 20);
                                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 5, 1);

                            }
                            for (Player victim : main.customMe.getTeamPlayers(protcTeam)) {

                                if (main.pl.getP(p).hasUpgrade("sweetHome")) {

                                    Location loc = DataManager.spawns.get(protcTeam);
                                    if (loc != null) {
                                        victim.teleport(new Location(main.map.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
                                    }
                                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 4));
                                    victim.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 4));
                                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 6));
                                }
                            }
                        }

                        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
                        w.createExplosion(b.getLocation(), 5, true);
                    } else {
                        World w = p.getWorld();
                        w.createExplosion(b.getLocation(), 2, false);
                    }
                }

            }
        }
    }


    public void removeFromList(Player p) {
        damagedAnchor.remove(p);
    }

    public void setCharges(Location loc, int charges) {


        Block b = loc.getBlock();

        if (charges <= 0) {
            b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
            return;
        }

        b.getWorld().getBlockAt(b.getLocation()).setType(Material.RESPAWN_ANCHOR);

        RespawnAnchor anchorD = (RespawnAnchor) loc.getBlock().getBlockData();
        anchorD.setCharges(charges);
        b.getWorld().getBlockAt(loc).setBlockData(anchorD);

    }
}
