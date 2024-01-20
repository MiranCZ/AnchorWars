package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.mapReset.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport implements CommandExecutor {

    Main main;


    public Teleport(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp() && sender instanceof Player) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Invalid use! Usage: /teleport <world> <x> <y> <z>");
                return false;
            } else if (args.length < 4) {
                args[1] = "0";
                args[2] = "0";
                args[3] = "0";
            }
            Player p = (Player) sender;
            if (main.worlds.getConfig().getString("default").equals(args[0])) {
                Location loc = new Location(null, Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
                loc.setWorld(Bukkit.getWorld(main.worlds.getConfig().getString("default")));
                p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                return false;

            }

            GameMap world = null;
            if (main.worlds.getConfig().getString("lobby").equals(args[0])) {
                world = main.lobby;
            } else if (main.worlds.getConfig().getString("waitingLobby").equals(args[0])) {
                world = main.wLobby;
            } else if (main.worlds.getConfig().getString("gameMap").equals(args[0])) {
                world = main.map;
            }
            if (world == null) {
                p.sendMessage(ChatColor.RED + "World named " + args[0] + " doesn't exist!");
                return false;
            }

            if (!world.isLoaded()) {
                world.load();
            }

            if (!main.customMe.isInt(args[1]) && !main.customMe.isInt(args[2]) && !main.customMe.isInt(args[3])) {
                p.sendMessage(ChatColor.RED + "Please use valid coordinates! Use: .teleport <world> <x> <y> <z>");
                return false;
            }

            Location loc = new Location(null, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            loc.setWorld(world.getWorld());
            p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);


        }

        return false;
    }
}
