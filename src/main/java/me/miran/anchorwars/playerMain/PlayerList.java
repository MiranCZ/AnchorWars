package me.miran.anchorwars.playerMain;

import me.miran.anchorwars.core.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerList implements Listener {

    private final static HashMap<Player, PlayerManager> playerList = new HashMap<>();
    Main main;

    public PlayerList(Main main) {
        this.main = main;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        playerList.put(e.getPlayer(), new PlayerManager(e.getPlayer(), main));

    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {

        playerList.remove(e.getPlayer());
    }

    public PlayerManager getP(Player p) {

        return playerList.get(p);
    }


}
