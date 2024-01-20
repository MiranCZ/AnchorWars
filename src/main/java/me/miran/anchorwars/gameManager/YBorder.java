package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.Color;

public class YBorder implements CommandExecutor, Listener {

    Main main;

    public YBorder(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp()) {
            if (main.customMe.isInt(args[0])) {
                main.yBorder = Integer.parseInt(args[0]);
                if (main.yBorder > 257 || main.yBorder < 0) {
                    sender.sendMessage(ChatColor.RED + "Y border must be between 256 and 0!");
                    main.yBorder = 0;
                }
                sender.sendMessage(ChatColor.GREEN + "Y border was successfully set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + main.yBorder);
            } else {
                sender.sendMessage(ChatColor.RED + args[0] + " is not number! Use: /.setYBorder <number>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You need to be an server operator to execute this command!");
        }
        return false;
    }


    @EventHandler
    public void playerCrossBorder(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getLocation().getY() <= main.yBorder) {
            if (p.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || p.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {


                Location loc = DataManager.spawns.get(main.pl.getP(p).getTeam());

                if (loc == null) {
                    loc = main.worldBCenter;
                }

                //teleport player 100 blocks up
                p.teleport(new Location(p.getWorld(), loc.getX(), loc.getY() + 10, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
                //pop totem
                p.damage(100);
                //add slow falling
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                    @Override
                    public void run() {
                        addSLow(p);
                    }
                }, 1L);
            } else if (main.pl.getP(p).hasUpgrade("autoTotem")) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.TOTEM_OF_UNDYING), 1)) {

                    Location loc = DataManager.spawns.get(main.pl.getP(p).getTeam());

                    if (loc == null) {
                        loc = main.worldBCenter;
                    }


                    p.getActivePotionEffects().clear();
                    p.setHealth(1);
                    p.addPotionEffect((new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 900, 1)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.ABSORPTION, 100, 1)));
                    p.playEffect(EntityEffect.TOTEM_RESURRECT);
                    p.sendMessage(ChatColor.GREEN + "Auto-totem saved you!");
                    p.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                    p.teleport(new Location(p.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                        @Override
                        public void run() {
                            addSLow(p);
                        }
                    }, 1L);
                }
            } else {
                if (main.gameStart && main.map.isLoaded()) {

                    if (p.getWorld() == main.map.getWorld()) {

                        EntityDamageEvent Void = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.VOID, 1);
                        p.setLastDamageCause(Void);
                        p.setHealth(1);
                        p.getActivePotionEffects().clear();
                        p.damage(100);

                    }
                }

            }
        }

    }

    public void addSLow(Player p) {
        p.removePotionEffect(PotionEffectType.SLOW_FALLING);
        p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_FALLING, 5, 4)));
    }
}
