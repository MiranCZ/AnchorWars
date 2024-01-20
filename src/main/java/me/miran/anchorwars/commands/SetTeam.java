package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeam implements CommandExecutor {

    Main main;


    public SetTeam(Main main) {
        this.main = main;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (validTeam(args[0])) {
                        main.pl.getP(p).setTeam(args[0]);
                        main.scoreBoard.addScoreBoard(p);
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(0);
                        p.sendMessage(ChatColor.GREEN + "Your team was changed to " + args[0]);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "If you are not  a player use /setTeam <name> team");
                    return false;
                }

            } else if (args.length == 2) {
                Player p = Bukkit.getPlayer(args[0]);


                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Player named " + args[0] + " is not online!");
                    return false;
                }
                if (validTeam(args[1])) {
                    main.pl.getP(p).setTeam(args[1]);
                    main.scoreBoard.addScoreBoard(p);
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setHealth(0);
                    p.sendMessage(ChatColor.GREEN + "Your team was changed to " + args[1]);
                }

                return false;
            } else {
                sender.sendMessage(ChatColor.RED + "Use /setTeam <name> team");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You need op to perform this command!");
        }

        return false;
    }

    public boolean validTeam(String team) {
        return team.equals("RED") || team.equals("BLUE") || team.equals("YELLOW");
    }

}
