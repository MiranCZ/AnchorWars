package me.miran.anchorwars.commands;


import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.awt.*;

public class CrystalPower implements CommandExecutor, Listener {

    Main main;

    public CrystalPower(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender.isOp()) {
            if (main.customMe.isDouble(args[0])) {


                main.eCrystalPower = Double.parseDouble(args[0]);
                sender.sendMessage(ChatColor.GREEN + "End crystal power was successfully set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + main.eCrystalPower);
                if (main.eCrystalPower > 100) {
                    sender.sendMessage(ChatColor.RED + "WARNING! Normal end crystal power is 6.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Incorrect use of command, please use: /.setCrystalPower <power>");
            }

        }

        return false;
    }

    @EventHandler
    public void crystalPower(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.ENDER_CRYSTAL) {
            e.setCancelled(true);
            e.getEntity().remove();
            World w = e.getEntity().getWorld();
            w.createExplosion(e.getEntity().getLocation(), (float) main.eCrystalPower, false);
        }
    }

}

