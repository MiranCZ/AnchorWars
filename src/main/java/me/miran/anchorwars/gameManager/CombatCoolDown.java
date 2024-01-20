package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class CombatCoolDown implements CommandExecutor{


        private final Main main;
        public CombatCoolDown (Main main) {this.main = main;}

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

                if (sender.isOp()) {
                    if(main.customMe.isInt(args[0])) {

                        main.hitCoolDown = Integer.parseInt(args[0]);
                        sender.sendMessage(ChatColor.GREEN + "Combat time was successfully set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + main.hitCoolDown);

                    } else {
                        sender.sendMessage(ChatColor.RED + "Incorrect use of command, please use: /.setCombatTime <time>");
                    }

                }

            return false;
        }




}
