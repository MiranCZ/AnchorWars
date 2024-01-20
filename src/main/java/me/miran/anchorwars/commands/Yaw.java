package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Yaw implements CommandExecutor {

    Main main;
    public Yaw (Main main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
if (sender.isOp()) {
    if (args.length >= 3) {
    if (args[0].equals("team")) {
       String s = main.teams.getConfig().getString(args[1]);
        Location l = main.loc.decompileLoc(s);
        l.setYaw(Float.parseFloat(args[2]));
        String str = main.loc.compileLoc(l, true);
        main.teams.getConfig().set(args[1], str);
        main.teams.saveConfig();
    } else if (args[0].equals("default")) {
        String s = main.getConfig().getString(args[1]);
        Location l = main.loc.decompileLoc(s);
        l.setYaw(Float.parseFloat(args[2]));
        String str = main.loc.compileLoc(l, true);
        main.getConfig().set(args[1], str);
        main.saveConfig();
    }
    }
}
        return false;
    }
}
