package me.miran.anchorwars.gameManager;


import me.miran.anchorwars.GameState;
import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerTeleportEvent;


public class StartGame {

    Main main;


    public StartGame(Main main) {
        this.main = main;
    }

    public void setUpGame(World world) {

        for (Entity entity : main.droppedItems) {

            entity.remove();
        }
        main.droppedItems.clear();
        main.gameState = GameState.PLAYING;
        main.wLobby.unload();
        main.gameStart = true;

        for (Player p : main.waitingLobby.getWorld().getPlayers()) {
            p.getInventory().clear();
            main.customMe.removeFromLobby(p);

            if (main.pl.getP(p).getTeam().equals("null") || main.pl.getP(p).getTeam().equals("SPECTATOR")) {
                String team = DataManager.teams.get(0);
                int minPlayers = main.customMe.getTeamPlayers(DataManager.teams.get(0)).size();
                for (String teams : DataManager.teams) {
                    int amount = main.customMe.getTeamPlayers(teams).size();
                    if (amount < minPlayers) {
                        team = teams;
                    }
                }
                main.pl.getP(p).setTeam(team);
            }


            main.gen.loadGens();

            setGamerules(world);
            world.setTime(1200);
            world.setClearWeatherDuration(5000);


            p.setGameMode(GameMode.SPECTATOR);

            Location loc = main.worldBCenter;


            loc.setWorld(main.map.getWorld());

            p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + 3, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);


        }
        for (String teams : DataManager.teams) {
            main.customMe.createAnchor(teams);
            if (main.customMe.getTeamPlayers(teams).size() <= 0) {
                main.customMe.setAnchor(teams, 0, true);
                Location loc = DataManager.anchors.get(teams);
                loc.setWorld(main.map.getWorld());
                loc.getWorld().getBlockAt(loc).setType(Material.AIR);
            } else {
                Location loc = DataManager.anchors.get(teams);
                loc.setWorld(main.map.getWorld());
                loc.getWorld().getBlockAt(loc).setType(Material.RESPAWN_ANCHOR);

                RespawnAnchor anchorD = (RespawnAnchor) loc.getBlock().getBlockData();
                anchorD.setCharges(4);
                loc.getWorld().getBlockAt(loc).setBlockData(anchorD);
            }
        }


        world.getWorldBorder().setCenter(main.worldBCenter);
        world.getWorldBorder().setSize(main.worldBSize);

        for (Location loc : DataManager.shops.values()) {
            Location l = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ(), loc.getYaw(), loc.getPitch());
            Villager villager = (Villager) world.spawnEntity(l, EntityType.VILLAGER);
            villager.setAI(true);
            villager.setVillagerLevel(2);
            villager.setInvulnerable(true);
            villager.setProfession(Villager.Profession.WEAPONSMITH);
            villager.setCustomName("shop");
            main.droppedItems.add(villager);
        }

        for (Location loc : DataManager.upgradeShops.values()) {
            Location l = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ(), loc.getYaw(), loc.getPitch());
            Villager villager = (Villager) world.spawnEntity(l, EntityType.VILLAGER);
            villager.setAI(true);
            villager.setVillagerLevel(2);
            villager.setInvulnerable(true);
            villager.setProfession(Villager.Profession.TOOLSMITH);
            villager.setCustomName("upgrade shop");
            main.droppedItems.add(villager);
        }


//Change block to sign
        if (main.joinSign != null) {
            main.joinSign.getBlock().getWorld().getBlockAt(main.joinSign.getBlock().getLocation()).setType(Material.OAK_WALL_SIGN);

            //Get an instance of the sign, so you can edit it
            Sign sign = (Sign) main.joinSign.getBlock().getWorld().getBlockAt(main.joinSign.getBlock().getLocation()).getState();

            //Set sign
            sign.setLine(3, ChatColor.RED + "Game is running!");

            //Update the state of the sign
            sign.update();

            countToStart();
        }


    }

    int countDown;
    private int taskId;

    public void countToStart() {
        countDown = 10;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {

            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, 1);
                }
                Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + "Game starts in " + countDown);

                countDown--;

                if (countDown <= 0) {
                    Bukkit.getScheduler().cancelTask(taskId);
                    startGame();
                }

            }

        }, 0L, 20L);
    }


    public void startGame() {

        main.gen.startGen();

        for (Player p : main.map.getWorld().getPlayers()) {

            main.scoreBoard.addScoreBoard(p);

            Location loc = DataManager.spawns.get(main.pl.getP(p).getTeam());


            if (loc == null) {
                loc = main.worldBCenter;
            }
            loc.setWorld(main.map.getWorld());


            p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + 3, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
            p.setGameMode(GameMode.SURVIVAL);
            p.getEnderChest().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.getInventory().clear();

            main.phase.setPhase(0);
        }
    }

    private void setGamerules(World world) {
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
    }

}
