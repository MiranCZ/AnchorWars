package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

public class Phases {

    private static int phase = 0;
    Main main;
    private int id = 0;

    public Phases(Main main) {
        this.main = main;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int setPhase) {

        phase = setPhase;
        switch (setPhase) {
            case 0: {
                main.pvp = false;
                main.anchorDmg = false;

                break;
            }
            case 1: {
                main.pvp = true;
                main.anchorDmg = false;
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Watch out! PVP is now enabled!");

                break;
            }
            case 2: {
                main.pvp = true;
                main.anchorDmg = true;

                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Respawn Anchors can be damaged now!");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Items can be lost!");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Kits are active!");

                break;
            }
            case 3: {
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Respawn Anchor damage is now 5!");

                break;
            }
            case 4: {
                Location loc = main.worldBCenter;
                loc.setY(loc.getY() + 1);
                loc.setWorld(main.map.getWorld());
                main.map.getWorld().spawnEntity(loc, EntityType.WITHER);
                main.map.getWorld().spawnEntity(loc, EntityType.WITHER);
                Bukkit.broadcastMessage(loc + "");

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (main.pl.getP(p).isInGame() && p.getGameMode() == GameMode.SURVIVAL) {
                        p.addPotionEffect(PotionEffectType.GLOWING.createEffect(9999999, 1));
                    }
                }

                main.customMe.destroyAllAnchors();

                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "All Respawn Anchors are destroyed!");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Two withers were spawned on middle!");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Every player has glowing!");

                break;
            }
            case 5: {
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Every minute every one is teleported to middle and wither is spawned!");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Blocks can be destroyed by explosion!");

                World world = main.map.getWorld();
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Player || entity instanceof Wither) continue;

                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).setHealth(0);
                    }
                }

                world.getWorldBorder().setSize(30, 5 * 60);
                runPhase();
                break;
            }

        }
        if (phase > 5) {
            phase = 5;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 1);
            p.sendTitle(ChatColor.DARK_GREEN + "Phase " + phase, "", 3, 50, 3);
        }

    }

    public void runPhase() {

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {

                if (phase != 5) {
                    Bukkit.getScheduler().cancelTask(id);
                }

                Location loc = main.worldBCenter;

                loc.setWorld(main.map.getWorld());
                main.map.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), EntityType.WITHER);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (main.pl.getP(p).isInGame() && p.getGameMode() == GameMode.SURVIVAL) {


                        for (int x = -3; x < 6; x++) {
                            Location l = p.getLocation();
                            Entity tnt = l.getWorld().spawn(new Location(l.getWorld(), l.getX() + x, l.getY(), l.getZ() + x), TNTPrimed.class);
                            ((TNTPrimed) tnt).setFuseTicks(300);
                        }

                        p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(10, 4));

                        p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                }


            }
        }, 0L, 1200L);

    }

}
