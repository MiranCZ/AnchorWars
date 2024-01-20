package me.miran.anchorwars.shop;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class OpenUpgradeShop {

    Main main;

    public OpenUpgradeShop (Main main) {
        this.main = main;
    }

    public void openUpgradeShop (Player p) {

        Inventory upgradeShop = Bukkit.createInventory(null,36, "Upgrade Shop");

        int i = 0;
        boolean stop = false;
        while (!stop) {
            String path = main.shops.getConfig().getString("UpgradeShop.allowed." + i);

            if (path == null) {
                stop = true;
                continue;
            }

            String name = main.shops.getConfig().getString("UpgradeShop." + path + ".name");
            Material m = Material.valueOf(main.shops.getConfig().getString("UpgradeShop." + path + ".item"));
            int cost = main.shops.getConfig().getInt("UpgradeShop." + path + ".cost");
            int slot = main.shops.getConfig().getInt("UpgradeShop." + path + ".slot");
            ArrayList<String> lore = new ArrayList<>();

            int n = 0;
            boolean loop = true;
            while (loop) {
                String text = main.shops.getConfig().getString("UpgradeShop." + path + ".description." + n);

                if (text == null) {
                    loop = false;
                    continue;
                }

                lore.add(ChatColor.GRAY + text);

                n++;
            }


            ItemStack stack = new ItemStack(m);
            ItemMeta meta = stack.getItemMeta();


            int lvl = main.pl.getP(p).getUpgrade(path);


            if (lvl >= main.shops.getConfig().getInt("UpgradeShop." + path + ".maxLvl")) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                if (lvl > 0) {

                    stack.setAmount(lvl + 1);
                    lvl++;
                    meta.setDisplayName(meta.getDisplayName() + " " + lvl);

                }

                cost += main.shops.getConfig().getInt("UpgradeShop." + path + ".costHigher") * main.pl.getP(p).getUpgrade(path);
                lore.add(ChatColor.GOLD + "-cost " + cost + " diamonds");
            }

            meta.setDisplayName(name);
            meta.setLore(lore);

            stack.setItemMeta(meta);

            upgradeShop.setItem(slot, stack);



            i++;
        }

        ItemStack enchant = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantM = enchant.getItemMeta();
        enchantM.setDisplayName("Enchant item");



        Double cost = main.customMe.getEnchantCost(p);
        if (cost > 0) {
            enchantM.setLore(Arrays.asList(ChatColor.GRAY + "Enchant item in hand for " + cost.intValue() + " diamonds"));
            enchant.setItemMeta(enchantM);
        }



        enchant.setItemMeta(enchantM);

        upgradeShop.setItem(34, enchant);


        p.openInventory(upgradeShop);
        main.inv.add(p, upgradeShop);
        }


    }

