package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;


public class DeathMassages implements Listener {


    HashMap<String, Player> hitTag = new HashMap<>();

    HashMap<String, Integer> coolDown = new HashMap<>();

    Main main;

    public DeathMassages(Main main) {
        this.main = main;
        new BukkitRunnable() {

            @Override
            public void run() {

                for (String str : coolDown.keySet()) {
                    if (coolDown.get(str) <= 1) {
                        coolDown.remove(str);
                        hitTag.remove(str);

                        continue;
                    }


                    coolDown.put(str, coolDown.get(str) - 1);
                }
            }
        }.runTaskTimer(main, 0, 20);
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent e) {

        int lootingN = 0;

        Player p = e.getEntity();

        if (p.getLastDamageCause() == null) {
            p.spigot().respawn();
            p.getActivePotionEffects().clear();
            p.damage(100);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run() {

                    main.respawn.respawnPlayer(p, 0);
                }
            }, 5L);
            return;
        }

        EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
        String teamColor = "&r";
        if (!main.pl.getP(p).getTeam().equals("null")) {
            String team = main.pl.getP(p).getTeam();
            teamColor = main.teams.getConfig().getString("Teams." + team + ".color");
        }

        if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            if (hitTag.get(p.getName()) != null) {
                Player killer = hitTag.get(p.getName());

                lootingN = killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);

                String killerTeamColor = "&r";
                if (e.getDeathMessage().contains(killer.getName())) {
                    if (!main.pl.getP(killer).getTeam().equals("null")) {
                        String team = main.pl.getP(killer).getTeam();


                        killerTeamColor = main.teams.getConfig().getString("Teams." + team + ".color");

                    }
                    e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', teamColor + p.getName() + "&r" + e.getDeathMessage().replace(p.getName(), "").replace(killer.getName(), "") + killerTeamColor + killer.getName()));

                    int finalLootingN = lootingN;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                        @Override
                        public void run() {

                            main.respawn.respawnPlayer(p, finalLootingN);
                        }
                    }, 5L);

                    return;
                }
            }

        } else if (cause.equals(EntityDamageEvent.DamageCause.CUSTOM)) {
            e.setDeathMessage(p.getName() + e.getDeathMessage().replace(p.getName(), "").replace("died", "fell out of the world"));
        }

        if (hitTag.get(p) != null) {
            Player killer = hitTag.get(p);
            String killerTeamColor = "&r";
            if (DataManager.teams.contains(main.pl.getP(killer).getTeam())) {
                String aTeam = main.pl.getP(killer).getTeam();

                killerTeamColor = main.teams.getConfig().getString("Teams." + aTeam + ".color");

            }
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', teamColor + p.getName() + "&r" + e.getDeathMessage().replace(p.getName(), "") + " while fighting with ") + killerTeamColor + killer.getName());
            return;
        }
        e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', teamColor + p.getName() + "&r" + e.getDeathMessage().replace(p.getName(), "")));

        int finalLootingN = lootingN;
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                main.respawn.respawnPlayer(p, finalLootingN);
            }
        }, 5L);

    }


    @EventHandler
    public void playerDamagePlayer(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            if (!main.pvp) {
                e.setCancelled(true);
            }

            Player victim = (Player) e.getEntity();
            Player attacker = (Player) e.getDamager();
            coolDown.put(victim.getName(), main.hitCoolDown);


            hitTag.put(victim.getName(), attacker);

        }

    }

}