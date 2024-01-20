package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class ScoreBoard {



    Main main;

    public ScoreBoard(Main main) {
        this.main = main;
    }

    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private int id;

    boolean updating = false;
     ArrayList<Player> scoreboards = new ArrayList<>();

    public void addScoreBoard (Player p) {

        if (!updating) {
            updating = true;
            startUpdating();
        }
        scoreboards.add(p);

    }

    public void reset () {
        scoreboards = new ArrayList<>();
        updating = false;
        seconds = 0;
        minutes = 0;
        hours = 0;
    }

    public void startUpdating () {
       id =  Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {

            public void run() {
                if(!updating || scoreboards.size() == 0) {
                    Bukkit.getScheduler().cancelTask(id);
                    return;
                }

                String time = "";

                seconds++;
                if (seconds >= 60) {
                    seconds = 0;
                    minutes++;

                    if(hours == 0) {
                        if (minutes == 5) {
                            main.phase.setPhase(1);
                        }else
                        if(minutes == 8) {
                            main.phase.setPhase(2);
                        } else if (minutes == 20) {
                            main.phase.setPhase(3);
                        } else if (minutes == 40) {
                            main.phase.setPhase(4);
                        }

                    } else if (hours == 1 &&main.phase.getPhase() !=5) {
                        main.phase.setPhase(5);
                    }
                }
                if (minutes >= 60) {
                    minutes = 0;
                    hours++;
                }

                //seconds
            if (seconds ==0) {
                time = "00";
            } else if (seconds <10) {
                time = "0" + seconds;
            }else {
                time = seconds + "";
            }
            //minutes
                if (minutes ==0) {
                    time =  "00:" + time;
                } else if (minutes <10) {
                    time = "0" + minutes + ":" + time;
                } else {
                    time = minutes + ":" + time;
                }
                time = hours + ":" + time;

                for(Player p : scoreboards) {
                    if (!p.isOnline()) {
                        scoreboards.remove(p);
                        continue;
                    }
                    int position = DataManager.teams.size() + 10;

                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    Scoreboard board = manager.getNewScoreboard();
                    Objective obj = board.registerNewObjective("Game", "dummy", ChatColor.DARK_RED + "" + ChatColor.BOLD + "Anchor Wars");
                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);



                    for (Player player : scoreboards) {
                        String teamOfP = main.pl.getP(player).getTeam();
                        Team team;
                        if (board.getTeam(teamOfP) != null) {
                            team = board.getTeam(teamOfP);

                        } else {
                            team = board.registerNewTeam(teamOfP);
                        }

                        String color = main.teams.getConfig().getString("Teams." + teamOfP + ".color");
                        String name = DataManager.names.get(teamOfP);
                        if(name == null) {
                            continue;
                        }
                        name = name.substring(0, 1).toUpperCase();

                        team.setPrefix(ChatColor.translateAlternateColorCodes('&', color + "[" + name + "]"));
                        team.setAllowFriendlyFire(false);
                        team.setCanSeeFriendlyInvisibles(true);

                        team.addPlayer(player);
                        player.setScoreboard(board);
                    }

                    Score gameTime = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&6Game Time: &a" + time));
                    gameTime.setScore(position);

                    position--;

                    Score phase = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&6Phase: &a"  + main.phase.getPhase()));
                    phase.setScore(position);

                    position--;

                    Score space = obj.getScore("");
                    space.setScore(position);

                    position--;

                    for (String team : DataManager.teams ) {
                        String color = main.teams.getConfig().getString("Teams." + team + ".color");
                        String name = DataManager.names.get(team);
                        Score anchor = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&l" + color + name.toUpperCase() + "&r " + main.customMe.getAnchorLevel(team) + "% (&7"  + main.customMe.getTeamPlayers(team).size() + "&r)" ));
                        anchor.setScore(position);

                        position--;

                    }

                    Score space1 = obj.getScore("");
                    space1.setScore(position);

                    position--;


                    if (!main.pl.getP(p).getTeam().equals("null") && !main.pl.getP(p).getTeam().equals("SPECTATOR")) {
                        String team = main.pl.getP(p).getTeam();
                        String color = main.teams.getConfig().getString("Teams." + team + ".color");

                        Score yourTeam = obj.getScore(ChatColor.translateAlternateColorCodes('&', color + "Your Team:"));
                        yourTeam.setScore(position);
                        position--;

                        for (Player teamMate : main.customMe.getTeamPlayers(main.pl.getP(p).getTeam())) {
                            int tHealth = (int) Math.round(teamMate.getHealth() + 0.4);
                            Score name = obj.getScore(ChatColor.GRAY + "   " + teamMate.getName() + " " + getHealthColor(tHealth) + tHealth);
                            name.setScore(position);

                            position--;
                        }

                    } else {
                        int pHealth = (int) Math.round(p.getHealth() + 0.4);
                        Score name = obj.getScore(ChatColor.GRAY + "   " + p.getName() + " " + getHealthColor(pHealth) + pHealth);
                        name.setScore(position);
                    }



                    p.setScoreboard(board);


                }

            }

        }, 0L,20L);
    }


    public net.md_5.bungee.api.ChatColor getHealthColor (int health) {
        if (health >= 15) {
            return net.md_5.bungee.api.ChatColor.DARK_GREEN;
        } else if (health >= 10) {
           return net.md_5.bungee.api.ChatColor.GREEN;
        } else if (health >= 5) {
            return net.md_5.bungee.api.ChatColor.RED;
        } else {
            return net.md_5.bungee.api.ChatColor.DARK_RED;
        }
    }

}
