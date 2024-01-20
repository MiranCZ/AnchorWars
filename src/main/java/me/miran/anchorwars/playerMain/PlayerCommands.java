package me.miran.anchorwars.playerMain;

import me.miran.anchorwars.gameManager.kitManager.Kit;

import java.util.HashMap;

public interface PlayerCommands {
    boolean setTeam(String team);

    void sendMessage(HashMap<String, Boolean> message);

    void setUpgrade(String upgrade, int lvl);

    boolean isInGame();

    boolean isInLobby();

    int getUpgrade(String upgrade);

    String getTeam();

    Kit getKit();

    void setKit(Kit kit);

    PlayerAction getAction();

    void setAction(PlayerAction action);

    boolean hasUpgrade(String upgrade);

}
