package me.miran.anchorwars.gameManager.gameSetuping;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Setup implements CommandExecutor, Listener {



    Main main;

    public Setup (Main main) {
        this.main = main;
    }

    public GameSetup game = new GameSetup(main);

    public Inventory inv = Bukkit.createInventory(null, 36,"Game Setup");

    public void setInv () {
        inv.clear();

        setItem(Material.OAK_SIGN, "Join Sign", 11);
        setItem(Material.BEDROCK, "Waiting Lobby", 12);
        setItem(Material.ENDER_PEARL, "Main Lobby", 13);
        setItem(Material.IRON_BARS, "World Center", 14);
        setItem(Material.WHITE_WOOL, "Teams", 15);
        setItem(Material.TERRACOTTA, "Brick Generator", 20);
        setItem(Material.IRON_BLOCK, "Iron Generator", 21);
        setItem(Material.GOLD_BLOCK, "Gold Generator", 22);
        setItem(Material.DIAMOND_BLOCK, "Diamond Generator", 23);
        setItem(Material.NETHERITE_BLOCK, "Netherite Generator", 24);

    }

    public void setItem (Material material,String name, int slot) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();;
        meta.setDisplayName(name);

        stack.setItemMeta(meta);
        inv.setItem(slot, stack);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(p.isOp()) {

                main.teamData.restoreTeams();

            setInv();
            p.openInventory(inv);

            } else {
                p.sendMessage("You need to be op to perform this command");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You need to be a player to perform this command!");
        }

        return false;
    }

    @EventHandler
    public void invClick (InventoryClickEvent e) {

        if (e.getView().getTitle().equals("Game Setup") && e.getWhoClicked().isOp() && !main.gameStart) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();

            int slot = e.getSlot();
            if (e.getInventory().getItem(slot) == null) {
                return;
            }

            p.getInventory().clear();
            p.closeInventory();

            switch (slot) {
                case 11: {

                game.giveItem(Material.OAK_SIGN, "Join Sign", ChatColor.AQUA, p, 4, false, 1);
                p.sendMessage(ChatColor.GREEN + "Break block while holding it to set it");
                p.sendMessage(ChatColor.GREEN + "People can join through this sign");
                break;
                } case 12: {

                    game.giveItem(Material.BEDROCK, "Waiting Lobby", ChatColor.AQUA, p, 4, false, 1);
                    p.sendMessage(ChatColor.GREEN + "Break block while holding it to set it");
                    p.sendMessage(ChatColor.GREEN + "People will be teleported here if the click join sign");
                    break;
                } case 13: {

                    game.giveItem(Material.ENDER_PEARL, "Main Lobby", ChatColor.AQUA, p, 4, false, 1);
                    p.sendMessage(ChatColor.GREEN + "Break block while holding it to set it");
                    p.sendMessage(ChatColor.GREEN + "People will be teleported here, when they join");
                    break;
                } case 14: {

                    game.giveItem(Material.IRON_BARS, "World Center", ChatColor.AQUA, p, 4, false, 1);
                    p.sendMessage(ChatColor.GREEN + "Break block while holding it to set it");
                    p.sendMessage(ChatColor.GREEN + "Center of world border and center of map");
                    break;
                } case 15: {
                        //teams
                    main.setup.startTeamsMenu(p);
                    p.sendMessage(ChatColor.GREEN + "Break block while holding it to set it");
                    //game.giveItem(Material.STONE, "wip", ChatColor.AQUA, p, 4, false, 1);
                    break;
                } case 20: {
                    giveGen(p, Material.TERRACOTTA);
                    break;
                } case 21: {

                    giveGen(p, Material.IRON_BLOCK);
                    break;
                }case 22: {

                    giveGen(p, Material.GOLD_BLOCK);
                    break;
                }case 23: {

                    giveGen(p, Material.DIAMOND_BLOCK);
                    break;
                } case 24: {

                    giveGen(p, Material.NETHERITE_BLOCK);
                    break;

                }
                }

            }
        }

        public void giveGen (Player p, Material block) {
            game.giveItem(block,"0" ,ChatColor.RESET, p, 4,false,1);
            game.giveItem(block,"1" ,ChatColor.RESET, p, 5,false,1);
            game.giveItem(Material.RED_CONCRETE, "Remove Generator", ChatColor.RED, p, 0, false, 1);
            p.sendMessage(ChatColor.GREEN + "Break block while holding it to set it");
        }
    }



