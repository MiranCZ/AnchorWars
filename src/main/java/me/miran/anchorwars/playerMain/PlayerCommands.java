package me.miran.anchorwars.playerMain;

import me.miran.anchorwars.gameManager.kitManager.Kit;

import java.util.ArrayList;
import java.util.HashMap;

public interface PlayerCommands {
    boolean setTeam(String team);
    void setAction(PlayerAction action);
    void sendMessage(HashMap<String, Boolean> message);
    void setUpgrade(String upgrade, int lvl);
    void setKit(Kit kit);
    boolean isInGame();
    boolean isInLobby();
    int getUpgrade(String upgrade);
    String getTeam();
    Kit getKit();
    PlayerAction getAction();
    boolean hasUpgrade(String upgrade);

}
