package me.miran.anchorwars.gameManager.gameSetuping;


//import me.miran.anchorwars.core.Main;

import me.miran.anchorwars.core.Main;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class GameSetup implements Listener {

    Main main;


    public GameSetup(Main main) {
        this.main = main;
    }


    private static final HashMap<Player, Integer> pages = new HashMap<>();


    @EventHandler
    public void breakBlock(BlockBreakEvent e) {


        Player p = e.getPlayer();
        Block bl = e.getBlock();
        if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
            if (p.getInventory().getItemInMainHand().getType() == Material.OAK_SIGN) {
                e.setCancelled(true);
                main.joinSign = bl.getLocation();


                main.customMe.resetJoinSign();


            } else if (p.getInventory().getItemInMainHand().getType() == Material.BEDROCK) {
                e.setCancelled(true);
                Block bl1 = e.getBlock();
                main.waitingLobby = bl1.getLocation();


            } else if (p.getInventory().getItemInMainHand().getType() == Material.TERRACOTTA) {
                e.setCancelled(true);

                int time = 1000;
                int level = 0;
                String handItem = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();


                if (handItem.equals("1")) {
                    level = 1;
                    time = 10;
                } else if (handItem.equals("2")) {
                    level = 2;
                    time = 5;
                } else if (handItem.equals("3")) {
                    level = 3;
                    time = 2;
                } else if (handItem.equals("4")) {
                    level = 4;
                    time = 1;
                }


                main.gen.createGen(time, Material.BRICK, bl.getLocation(), level);


                e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.TERRACOTTA);


            } else if (p.getInventory().getItemInMainHand().getType() == Material.IRON_BLOCK) {
                e.setCancelled(true);

                int time = 1000;
                int level = 0;
                String handItem = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();


                if (handItem.equals("1")) {
                    level = 1;
                    time = 8;
                } else if (handItem.equals("2")) {
                    level = 2;
                    time = 6;
                } else if (handItem.equals("3")) {
                    level = 3;
                    time = 3;
                } else if (handItem.equals("4")) {
                    level = 4;
                    time = 1;
                }


                main.gen.createGen(time, Material.IRON_INGOT, bl.getLocation(), level);


                e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.IRON_BLOCK);


            } else if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_BLOCK) {
                e.setCancelled(true);

                int time = 1000;
                int level = 0;
                String handItem = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();


                if (handItem.equals("1")) {
                    level = 1;
                    time = 20;
                } else if (handItem.equals("2")) {
                    level = 2;
                    time = 10;
                } else if (handItem.equals("3")) {
                    level = 3;
                    time = 5;
                } else if (handItem.equals("4")) {
                    level = 4;
                    time = 3;
                }


                main.gen.createGen(time, Material.DIAMOND, bl.getLocation(), level);

                e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.DIAMOND_BLOCK);


            } else if (p.getInventory().getItemInMainHand().getType() == Material.NETHERITE_BLOCK) {
                e.setCancelled(true);

                int time = 1000;
                int level = 0;
                String handItem = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();


                if (handItem.equals("1")) {
                    level = 1;
                    time = 30;
                } else if (handItem.equals("2")) {
                    level = 2;
                    time = 20;
                } else if (handItem.equals("3")) {
                    level = 3;
                    time = 10;
                } else if (handItem.equals("4")) {
                    level = 4;
                    time = 5;
                }


                main.gen.createGen(time, Material.NETHERITE_INGOT, bl.getLocation(), level);

                e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.NETHERITE_BLOCK);


            } else if (p.getInventory().getItemInMainHand().getType() == Material.GOLD_BLOCK) {
                e.setCancelled(true);

                int time = 1000;
                int level = 0;
                String handItem = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();


                if (handItem.equals("1")) {
                    level = 1;
                    time = 10;
                } else if (handItem.equals("2")) {
                    level = 2;
                    time = 5;
                } else if (handItem.equals("3")) {
                    level = 3;
                    time = 2;
                }


                main.gen.createGen(time, Material.GOLD_INGOT, bl.getLocation(), level);

                e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.GOLD_BLOCK);
            } else if (p.getInventory().getItemInMainHand().getType() == Material.RED_CONCRETE) {
                main.gen.removeGen(bl);

            } else if (p.getInventory().getItemInMainHand().getType() == Material.EMERALD) {
                e.setCancelled(true);

                this.setLoc(bl.getLocation(), p, "shop");


            } else if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND) {
                e.setCancelled(true);

                this.setLoc(bl.getLocation(), p, "upgradeShop");

            } else if (p.getInventory().getItemInMainHand().getType() == Material.RESPAWN_ANCHOR) {
                e.setCancelled(true);

                this.setLoc(bl.getLocation(), p, "anchor");

            } else if (p.getInventory().getItemInMainHand().getType() == Material.IRON_BARS) {
                e.setCancelled(true);

                main.worldBCenter = bl.getLocation();


            } else if (p.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                e.setCancelled(true);

                main.mainLobby = bl.getLocation();

            } else if (me.miran.anchorwars.gameManager.teamManager.DataManager.teamByItem.containsKey(p.getInventory().getItemInMainHand().getType())) {
                e.setCancelled(true);

                this.setLoc(bl.getLocation(), p, "spawn");


            }

        }
    }

    public void setLoc(Location loc, Player p, String section) {
        String path = me.miran.anchorwars.gameManager.teamManager.DataManager.teams.get(pages.get(p));

        main.teams.getConfig().set("Teams." + path + ".data." + section, main.loc.compileLoc(loc, false));
        main.teams.saveConfig();
    }

    public void startTeamsMenu(Player p) {
        pages.put(p, 0);

        setTeamsMenu(p);
    }

    public void setTeamsMenu(Player p) {
        p.getInventory().clear();

        int page = pages.get(p);

        String path = DataManager.teams.get(page);

        if (path == null) {
            return;
        }
        path = "." + path;

        Material material = Material.valueOf(main.teams.getConfig().getString("Teams" + path + ".item"));

        String name = main.teams.getConfig().getString("Teams" + path + ".name");
        String color = main.teams.getConfig().getString("Teams" + path + ".color") + "&l";

        this.giveItemTwoPointO(material, ChatColor.translateAlternateColorCodes('&', color + name + " spawn"), p, 2, true);
        this.giveItemTwoPointO(Material.RESPAWN_ANCHOR, ChatColor.translateAlternateColorCodes('&', color + name + " anchor"), p, 3, true);
        this.giveItemTwoPointO(Material.EMERALD, ChatColor.translateAlternateColorCodes('&', color + name + " shop"), p, 4, true);
        this.giveItemTwoPointO(Material.DIAMOND, ChatColor.translateAlternateColorCodes('&', color + name + " upgrade shop"), p, 5, true);

        if (page < DataManager.teams.size() - 1) {
            this.giveItemTwoPointO(Material.YELLOW_CONCRETE, ChatColor.translateAlternateColorCodes('&', "&eNext page"), p, 8, true);
        }

        if (page > 0) {
            this.giveItemTwoPointO(Material.RED_CONCRETE, ChatColor.translateAlternateColorCodes('&', "&4Previous page"), p, 0, true);
        }


    }

    public void giveItemTwoPointO(Material material, String name, Player p, int slot, Boolean enchant) {
        ItemStack stack = new ItemStack(material);

        ItemMeta stackM = stack.getItemMeta();


        stackM.setDisplayName(name);


        if (enchant) {
            stackM.addEnchant(Enchantment.DURABILITY, 1, true);
            stackM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        stack.setItemMeta(stackM);

        p.getInventory().setItem(slot, stack);
    }

    @EventHandler
    public void nextPages(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (p.getInventory().getItemInMainHand().getType() == Material.YELLOW_CONCRETE) {
                    e.setCancelled(true);
                    p.swingMainHand();


                    if (pages.get(p) != null) {
                        int page = pages.get(p) + 1;

                        pages.put(p, page);

                        setTeamsMenu(p);
                    }

                } else if (p.getInventory().getItemInMainHand().getType() == Material.RED_CONCRETE) {
                    e.setCancelled(true);
                    p.swingMainHand();


                    if (pages.get(p) != null) {
                        int page = pages.get(p) - 1;

                        pages.put(p, page);

                        setTeamsMenu(p);
                    }

                }
            }
        }
    }


    public void giveItem(Material material, String name, ChatColor nameColor, Player p, int slot, Boolean bold, int enchant) {

        ItemStack stack = new ItemStack(material);

        ItemMeta stackM = stack.getItemMeta();

        if (bold) {
            stackM.setDisplayName(nameColor + "" + ChatColor.BOLD + name);
        } else {
            stackM.setDisplayName(nameColor + name);
        }

        if (enchant > 0) {
            stackM.addEnchant(Enchantment.DURABILITY, enchant, true);
            stackM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        stack.setItemMeta(stackM);

        p.getInventory().setItem(slot, stack);
    }


}

