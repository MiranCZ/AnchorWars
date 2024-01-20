package me.miran.anchorwars.shop;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.*;

public class ShopManager implements Listener {

    Main main;

    public ShopManager(Main main) {
        this.main = main;
    }


    @EventHandler
    public void openShop(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.MERCHANT) {
            Player p = (Player) e.getPlayer();
            Villager v = (Villager) e.getInventory().getHolder();

            if (v.getProfession() == Villager.Profession.WEAPONSMITH) {
                e.setCancelled(true);
                this.openSections(p);

            } else if (v.getProfession() == Villager.Profession.TOOLSMITH) {
                e.setCancelled(true);
                main.uShop.openUpgradeShop(p);

            }
        }
    }

    public void openSections(Player p) {
        Inventory inv = Bukkit.createInventory(p, main.shops.getConfig().getInt("Shop" + ".main" + ".size"), main.shops.getConfig().getString("Shop" + ".main" + ".name"));

        boolean isNull = false;
        int i = 0;
        while (!isNull) {
            String id = "." + i;
            String section = "." + main.shops.getConfig().getString("Shop" + ".main" + ".loadSections" + id);


            String sM = main.shops.getConfig().getString("Shop" + section + ".representativeItem");


            if (sM == null) {
                isNull = true;
                continue;
            }


            ItemStack itemStack = new ItemStack(Material.valueOf(sM));

            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(main.shops.getConfig().getString("Shop" + section + ".name"));

            itemStack.setItemMeta(itemMeta);

            inv.setItem(main.shops.getConfig().getInt("Shop" + section + ".slotInMain"), itemStack);


            i++;
        }
        p.openInventory(inv);
        main.inv.add(p, inv);
    }

    @EventHandler
    public void invClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if (e.getView().getTitle().equals("Upgrade Shop") && main.inv.isInv(p, e.getClickedInventory())) {
            e.setCancelled(true);


            if (slot == 34) {

                e.setCancelled(true);
                this.enchant(p);
                return;
            }

            boolean stop = false;
            int i = 0;
            while (!stop) {
                String name = main.shops.getConfig().getString("UpgradeShop.allowed." + i);

                if (name == null) {
                    stop = true;
                    continue;
                }

                int itemSLot = main.shops.getConfig().getInt("UpgradeShop." + name + ".slot");

                if (itemSLot == slot) {
                    int costNum = main.shops.getConfig().getInt("UpgradeShop." + name + ".cost");
                    if (main.shops.getConfig().getInt("UpgradeShop." + name + ".maxLvl") > 1) {

                        costNum += main.shops.getConfig().getInt("UpgradeShop." + name + ".costHigher") * main.pl.getP(p).getUpgrade(name);
                    }

                    if (main.pl.getP(p).getUpgrade(name) < main.shops.getConfig().getInt("UpgradeShop." + name + ".maxLvl")) {
                        ItemStack cost = new ItemStack(Material.DIAMOND, costNum);
                        if (p.getInventory().containsAtLeast(cost, cost.getAmount())) {

                            main.pl.getP(p).setUpgrade(name, main.pl.getP(p).getUpgrade(name) + 1);
                            p.getInventory().removeItem(cost);
                            p.closeInventory();
                        } else {
                            p.sendMessage("You do not have enough materials");
                        }
                    }
                }


                i++;
            }
        }


    }


    public void enchant(Player p) {
        ArrayList<Enchantment> enchantments = new ArrayList<>();
        Double cost = main.customMe.getEnchantCost(p);

        if (cost > 0) {


            ItemMeta swordM = p.getInventory().getItemInMainHand().getItemMeta();

            ItemStack costItemStack = new ItemStack(Material.DIAMOND, cost.intValue());
            if (p.getInventory().containsAtLeast(costItemStack, costItemStack.getAmount())) {

                enchantments = this.getEnchantments(p.getInventory().getItemInMainHand().getType());
                enchantments.add(Enchantment.DURABILITY);

                if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LOYALTY) && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.CHANNELING)) {
                    enchantments.remove(Enchantment.RIPTIDE);
                }
                if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.RIPTIDE)) {
                    enchantments.remove(Enchantment.LOYALTY);
                    enchantments.remove(Enchantment.CHANNELING);
                }

                Random r = new Random();

                double biggerCost = enchantments.size() + 0.5;

                int x = r.nextInt(3) + 1;
                int y = (int) (Math.round(x * ((cost * biggerCost) / 100)));
                x = r.nextInt(x + y);


                int maxTimes = 3;

                if (x > maxTimes) {
                    x = maxTimes;
                }
                if (x <= 0) {
                    x = 1;
                }

                int times;


                HashMap<Enchantment, Integer> enchantedWith = new HashMap<>();

                for (times = x; times > 0; times--) {

                    int i = r.nextInt(enchantments.size());
                    Enchantment e = enchantments.get(i);


                    while (p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(e) >= e.getMaxLevel()) {
                        enchantments.remove(e);

                        for (Enchantment enchantment : p.getInventory().getItemInMainHand().getItemMeta().getEnchants().keySet()) {
                            if (enchantment.conflictsWith(e)) {
                                enchantments.remove(e);
                            }
                        }

                        if (enchantments.size() == 0) {
                            p.sendMessage(ChatColor.RED + "This item can not be enchanted any more!");
                            return;
                        }
                        i = r.nextInt(enchantments.size());
                        e = enchantments.get(i);

                    }

                    int level = r.nextInt(e.getMaxLevel());
                    if (level == 0) {
                        level = 1;
                    }

                    if (p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(e) + level > e.getMaxLevel()) {
                        level = e.getMaxLevel();
                    } else if (p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(e) + level <= e.getMaxLevel()) {
                        level = level + p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(e);
                        p.getInventory().getItemInMainHand().getItemMeta().removeEnchant(e);

                    }

                    if (level > e.getMaxLevel()) {
                        level = e.getMaxLevel();
                    }
                    swordM.addEnchant(e, level, true);


                    p.getInventory().getItemInMainHand().setItemMeta(swordM);
                    enchantedWith.put(e, level);
                }
                for (Map.Entry<Enchantment, Integer> entry : enchantedWith.entrySet()) {
                    Enchantment e = entry.getKey();
                    int level = entry.getValue();
                    String lvl = getRomanSymbol(level);
                    if (level == 1 && e.getMaxLevel() == 1) {
                        lvl = "";
                    }


                    p.sendMessage(ChatColor.GREEN + "Item was successfully enchanted with " + net.md_5.bungee.api.ChatColor.of(Color.ORANGE) + main.shop.getItemName(e.getKey().toString()) + " " + lvl);

                }

                p.getInventory().removeItem(costItemStack);
                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 5, 1);
                double newCost = main.customMe.getEnchantCost(p);

                ItemStack item = new ItemStack(Material.ENCHANTING_TABLE);
                ItemMeta itemM = item.getItemMeta();

                itemM.setLore(Collections.singletonList(ChatColor.GRAY + "Enchant item in hand for " + (int) newCost + " diamonds"));
                item.setItemMeta(itemM);
                p.getOpenInventory().setItem(34, item);
                //p.closeInventory();

            } else {
                int has = 0;
                for (int i = 0; i < 36; i++) {
                    ItemStack slot = p.getInventory().getItem(i);
                    if (slot == null || !slot.isSimilar(new ItemStack(Material.DIAMOND)))
                        continue;
                    has += slot.getAmount();
                }
                Double cost1 = Math.floor(cost - has);
                p.sendMessage(ChatColor.RED + "You need " + cost1.intValue() + " more diamonds");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);

            }
        } else {
            p.sendMessage(ChatColor.RED + "This item can not be enchanted");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
        }
    }

    public String getRomanSymbol(int number) {
        if (number == 1) {
            return "I";
        }
        if (number == 2) {
            return "II";
        }
        if (number == 3) {
            return "III";
        }
        if (number == 4) {
            return "IV";
        }
        if (number == 5) {
            return "V";
        }

        return number + "";
    }

    public ArrayList<Enchantment> getEnchantments(Material material) {
        String handI = material.toString().toLowerCase();
        ArrayList<Enchantment> enchantments = new ArrayList<>();
        if (handI.contains("sword")) {
            enchantments.add(Enchantment.DAMAGE_ALL);
            enchantments.add(Enchantment.FIRE_ASPECT);
            enchantments.add(Enchantment.LOOT_BONUS_MOBS);
            enchantments.add(Enchantment.KNOCKBACK);
        } else if (handI.contains("axe") && !handI.contains("pickaxe")) {
            enchantments.add(Enchantment.DAMAGE_ALL);
            enchantments.add(Enchantment.DIG_SPEED);
        } else if (handI.contains("bow") && !handI.contains("cross")) {
            enchantments.add(Enchantment.ARROW_KNOCKBACK);
            enchantments.add(Enchantment.ARROW_DAMAGE);
            enchantments.add(Enchantment.ARROW_FIRE);
            enchantments.add(Enchantment.ARROW_INFINITE);
        } else if (handI.contains("crossbow")) {
            enchantments.add(Enchantment.QUICK_CHARGE);
            enchantments.add(Enchantment.MULTISHOT);
        } else if (handI.contains("helmet")) {
            enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
            enchantments.add(Enchantment.WATER_WORKER);
            enchantments.add(Enchantment.THORNS);
        } else if (handI.contains("chestplate")) {
            enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
            enchantments.add(Enchantment.THORNS);
        } else if (handI.contains("leggings")) {
            enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
            enchantments.add(Enchantment.THORNS);
        } else if (handI.contains("boots")) {
            enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
            enchantments.add(Enchantment.DEPTH_STRIDER);
            enchantments.add(Enchantment.PROTECTION_FALL);
        } else if (handI.contains("pickaxe") || handI.contains("shovel")) {
            enchantments.add(Enchantment.DIG_SPEED);
        }  /*else if (handI.contains("hoe")) {
                    enchantments.add(Enchantment.BINDING_CURSE);
                    enchantments.add(Enchantment.VANISHING_CURSE);
                    enchantments.remove(Enchantment.DURABILITY);
                }*/ else if (handI.contains("trident")) {
            enchantments.add(Enchantment.DAMAGE_ALL);
            enchantments.add(Enchantment.RIPTIDE);

            enchantments.add(Enchantment.LOYALTY);
            enchantments.add(Enchantment.CHANNELING);

        }
        return enchantments;
    }

}





