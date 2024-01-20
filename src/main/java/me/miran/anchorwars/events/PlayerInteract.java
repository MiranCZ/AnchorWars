package me.miran.anchorwars.events;

import me.miran.anchorwars.GameState;
import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.kitManager.Kits;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerInteract implements Listener {


    Main main;

    boolean starting = false;
    Integer waiting = 0;
    Inventory chooseTeam;
    int countDown;
    private final Kits kit = new Kits(/*main*/);
    private int taskId;

    public PlayerInteract(Main main) {
        this.main = main;
    }

    @EventHandler
    public void clickAtBlockWithFunction(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (main.joinSign != null) {
                Block bl = e.getClickedBlock();
                if (main.joinSign.getX() == bl.getLocation().getX() && main.joinSign.getY() == bl.getLocation().getY() && main.joinSign.getZ() == bl.getLocation().getZ()) { //Using only .Location was buggy.
                    if (bl.getType() == Material.OAK_WALL_SIGN) {
                        if (main.waitingLobby != null) {

                            if (main.gameState == GameState.PLAYING) {
                                if (!main.map.isLoaded()) {
                                    return;
                                }

                                e.setCancelled(true);
                                p.setGameMode(GameMode.SPECTATOR);
                                p.sendTitle(ChatColor.RED + "Game is in progress!", ChatColor.GRAY + "You are now spectator!", 40, 40, 20);
                                main.pl.getP(p).setTeam("SPECTATOR");
                                Location loc = main.worldBCenter;
                                loc.setWorld(main.map.getWorld());
                                p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
                                main.scoreBoard.addScoreBoard(p);

                                return;
                            }

                            if (main.customMe.isInLobby(p)) {
                                return;
                            }

                            e.setCancelled(true);

                            if (!main.wLobby.isLoaded()) {
                                main.wLobby.load();
                            }

                            Location loc = main.waitingLobby;
                            loc.setWorld(main.wLobby.getWorld());
                            p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
                            p.getInventory().clear();
                            main.customMe.addToLobby(p);

                            main.teamData.restoreTeams();

                            ItemStack wool = new ItemStack(Material.WHITE_WOOL);
                            ItemStack bed = new ItemStack(Material.RED_BED);
                            ItemStack chest = new ItemStack(Material.CHEST);

                            ItemMeta woolM = wool.getItemMeta();
                            ItemMeta bedM = bed.getItemMeta();
                            ItemMeta chestM = chest.getItemMeta();

                            woolM.setDisplayName("Choose team");
                            bedM.setDisplayName(ChatColor.RED + "Leave");
                            chestM.setDisplayName("Kit");

                            wool.setItemMeta(woolM);
                            bed.setItemMeta(bedM);
                            chest.setItemMeta(chestM);

                            p.getInventory().setItem(0, chest);
                            p.getInventory().setItem(4, wool);
                            p.getInventory().setItem(8, bed);

                            waiting++;


                            Sign sign = (Sign) main.joinSign.getBlock().getWorld().getBlockAt(main.joinSign.getBlock().getLocation()).getState();
                            sign.setLine(3, ChatColor.DARK_GREEN + "" + waiting + "/" + main.minPlayerCount + " players");
                            sign.update();

                            Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + waiting + "/" + main.minPlayerCount + " players waiting");

                            if (waiting >= main.minPlayerCount && !starting) {
                                this.prepareStart();
                                starting = true;
                            }


                        }
                    }
                }


            }
        } else {
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand.getType() == Material.AIR) {
                return;
            }
            if (hand.getType() == Material.WHITE_WOOL && Objects.requireNonNull(hand.getItemMeta()).getDisplayName().equals("choose team") || Objects.requireNonNull(hand.getItemMeta()).getDisplayName().toLowerCase().contains("team")) {


                chooseTeam = Bukkit.createInventory(null, getSize(), "Choose team");

                int i = 0;

                for (String team : DataManager.teams) {
                    ItemStack stack = new ItemStack(DataManager.items.get(team));
                    ItemMeta meta = stack.getItemMeta();

                    String color = main.teams.getConfig().getString("Teams." + team + ".color");

                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', color + DataManager.names.get(team) + " team"));
                    stack.setItemMeta(meta);

                    chooseTeam.setItem(i, stack);
                    i++;
                }
                p.openInventory(chooseTeam);


            } else if (p.getInventory().getItemInMainHand().getType() == Material.RED_BED) {
                waiting--;

                if (waiting < 0) {
                    waiting = 0;
                }


                Location loc = main.joinSign;
                if (loc == null) {
                    return;
                }

                if (!main.lobby.isLoaded()) {
                    main.lobby.load();
                }

                loc.setWorld(main.lobby.getWorld());
                e.setCancelled(true);
                p.getInventory().clear();
                main.pl.getP(p).setTeam("null");
                main.customMe.tpToLobby(p);

                Sign sign = (Sign) loc.getBlock().getWorld().getBlockAt(loc.getBlock().getLocation()).getState();
                sign.setLine(3, ChatColor.DARK_GREEN + "" + waiting + "/" + main.minPlayerCount + " players");
                sign.update();


                Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + waiting + "/" + main.minPlayerCount + " players waiting");
            } else if (p.getInventory().getItemInMainHand().getType() == Material.CHEST) {
                Inventory kit = Bukkit.createInventory(e.getPlayer(), 27, "Kit Selector");

                int i = 0;
                boolean stop = false;
                while (!stop) {
                    String path = main.kits.getConfig().getString("Kits.allowed." + i);
                    if (path == null) {
                        stop = true;
                        continue;
                    }

                    int slot = main.kits.getConfig().getInt("Kits." + path + ".slot");
                    Material m = Material.valueOf(main.kits.getConfig().getString("Kits." + path + ".item"));

                    ItemStack stack = new ItemStack(m);
                    ItemMeta meta = stack.getItemMeta();

                    meta.setDisplayName(main.shop.getItemName(path));

                    ArrayList<String> lore = new ArrayList<>();

                    int n = 0;
                    boolean loop = true;
                    while (loop) {
                        String text = main.kits.getConfig().getString("Kits." + path + ".description." + n);

                        if (text == null) {
                            loop = false;
                            continue;
                        }

                        lore.add(ChatColor.GRAY + text);

                        n++;
                    }
                    meta.setLore(lore);

                    stack.setItemMeta(meta);
                    kit.setItem(slot, stack);


                    i++;

                }

                p.openInventory(kit);

            }
        }
    }

    public int getSize() {
        int size = DataManager.teams.size();

        while (size % 9 != 0) {
            size++;
        }

        return size;
    }

    @EventHandler
    public void invClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("Choose team")) {
            e.setCancelled(true);

            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 5, 1);

            Material team = e.getClickedInventory().getItem(e.getSlot()).getType();

            String teamS = DataManager.teamByItem.get(team);
            if (teamS != null) {
                main.pl.getP(p).setTeam(teamS);
                p.getInventory().setItem(4, new ItemStack(e.getClickedInventory().getItem(e.getSlot())));
            }

            p.closeInventory();

        } else if (e.getView().getTitle().equals("Kit Selector")) {

            e.setCancelled(true);
            int slot = e.getSlot();
            int i = 0;
            boolean stop = false;
            boolean found = false;

            while (!stop) {
                String path = main.kits.getConfig().getString("Kits.allowed." + i);
                if (path == null) {
                    stop = true;
                    continue;
                }

                if (main.kits.getConfig().getInt("Kits." + path + ".slot") == slot) {
                    found = true;
                    stop = true;
                    continue;
                }
                i++;
            }

            if (found) {
                String name = e.getClickedInventory().getItem(slot).getItemMeta().getDisplayName().toLowerCase();
                kit.setKit(p, name);
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "SSuccessfully selected kit " + net.md_5.bungee.api.ChatColor.of(Color.ORANGE) + main.shop.getItemName(name));
            }

        } else if (main.customMe.isInLobby(p)) {
            e.setCancelled(true);
        }
    }

    public void prepareStart() {
        countDown = 30;
        main.gameState = GameState.COUNTDOWN;
        main.customMe.setGens();

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {

            public void run() {

                if (countDown % 5 == 0 || countDown <= 5) {

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5, 1);
                    }

                    Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + "Teleporting to game in " + countDown);

                }
                countDown--;

                if (countDown <= 0) {
                    if (!main.map.isLoaded()) {
                        main.map.load();
                    }


                    main.startGame.setUpGame(main.map.getWorld());
                    waiting = 0;
                    starting = false;

                    Bukkit.getScheduler().cancelTask(taskId);
                    return;
                }
                if (waiting < main.minPlayerCount) {
                    Bukkit.broadcastMessage("[" + ChatColor.RED + "AnchorWars" + ChatColor.RESET + "]" + "Not enough players! Count Down stopped.");
                    main.gameState = GameState.WAITING;
                    starting = false;
                    Bukkit.getScheduler().cancelTask(taskId);
                }

            }

        }, 0L, 20L);


    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (main.customMe.isInLobby(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void itemThrow(EntityDropItemEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();

            if (main.customMe.isInLobby(p)) {
                e.setCancelled(true);
            }
        }
    }

}



