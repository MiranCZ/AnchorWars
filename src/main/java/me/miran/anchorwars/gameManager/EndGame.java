package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.GameState;
import me.miran.anchorwars.core.Main;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class EndGame {

    Main main;

    public EndGame (Main main) {
        this.main = main;
    }

    public void endGame() {

        for ( Entity entity : main.droppedItems ) {

            entity.remove();
        }
        main.droppedItems.clear();
        main.gameStart = false;

        World world = main.worldBCenter.getWorld();

        world.getWorldBorder().reset();

        main.pvp = false;

        for ( Player p : main.map.getWorld().getPlayers() ) {
            p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            main.pl.getP(p).reset();
            main.customMe.tpToLobby(p);
        }

        main.map.unload();
        main.wLobby.load();
        main.gameState = GameState.WAITING;
        main.gameStart = false;
        main.yBorder = 150;
        main.scoreBoard.reset();
        main.phase.setPhase(0);
        main.gen.reset();


        main.customMe.resetJoinSign();
        main.customMe.reset();




    }

}
