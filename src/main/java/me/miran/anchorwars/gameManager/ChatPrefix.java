package me.miran.anchorwars.gameManager;


import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatPrefix implements Listener {

    Main main;

    public ChatPrefix (Main main) {
        this.main = main;
    }


    @EventHandler
    public void onChat (AsyncPlayerChatEvent e) {

        if (main.customMe == null)  {
            return;
        }

        e.setCancelled(true);

        String message = e.getMessage();
        Player p = e.getPlayer();
        String team = main.pl.getP(p).getTeam();
        String prefix = null;
        String prefixColor = null;


        ArrayList<Player> sendTo = new ArrayList<>();
        if (main.map.isLoaded() && main.gameStart && main.map.getWorld() == p.getWorld()) {
            if (message.charAt(0) == '!') {
                sendTo.addAll(main.map.getWorld().getPlayers());

            } else {
                sendTo.addAll(main.customMe.getTeamPlayers(team));

            }
        } else {
            sendTo.addAll(p.getWorld().getPlayers());
        }

        if (DataManager.teams.contains(team)) {
            team = "." + team;

            prefix =  main.teams.getConfig().getString("Teams" + team + ".name" );
            prefixColor = main.teams.getConfig().getString("Teams" + team + ".color" );
        } else {
            if(main.map.isLoaded()) {
                if (p.getWorld() == main.map.getWorld()) {
                    prefix = "spectator";
                    prefixColor = "&7";
                }
            }
            if (main.wLobby.isLoaded()) {
                if(main.wLobby.getWorld() == p.getWorld()) {
                    prefix = "WAITING";
                    prefixColor = "&6";
                }
            }
            if (prefix == null) {
                if (main.wLobby.isLoaded()) {
                    sendTo.addAll(main.wLobby.getWorld().getPlayers());
                }
            prefix = "HUB";
            prefixColor = "&2";
            }

        }

       String preMassage = "§8[" + prefixColor + prefix + "§8]" + "&7 " + p.getName() + ": ";

        for (Player player : sendTo) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', preMassage + "&r")  +message);
        }

    }



}