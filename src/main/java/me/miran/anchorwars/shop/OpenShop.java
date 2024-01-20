package me.miran.anchorwars.shop;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class OpenShop implements Listener {

    Main main;

    public OpenShop(Main main) {
        this.main = main;
    }


    @EventHandler
    public void click(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if (e.getView().getTitle().equals("Shop") && main.inv.isInv(p, e.getClickedInventory())) {

            e.setCancelled(true);

            ItemStack clickedStack = e.getClickedInventory().getItem(slot);

            if (clickedStack == null || !clickedStack.hasItemMeta()) {

                return;
            }
            if (!clickedStack.getItemMeta().hasDisplayName()) {

                return;
            }

            String section = this.getSectionByName(clickedStack.getItemMeta().getDisplayName());
            if (section == null) {
                return;
            }

            int i;
            int n = main.shops.getConfig().getInt("Shop" + section + ".size");

            Inventory inv = Bukkit.createInventory(p, main.shops.getConfig().getInt("Shop" + section + ".size"), main.shops.getConfig().getString("Shop" + section + ".name"));


            for (i = 0; i < n; i++) {

                String id = "." + i;
                String materialS = main.shops.getConfig().getString("Shop" + section + ".contents" + id + ".material");

                if (materialS == null) {
                    continue;
                }


                Material material = Material.valueOf(materialS);
                int amount = main.shops.getConfig().getInt("Shop" + section + ".contents" + id + ".amount");

                int cost = main.shops.getConfig().getInt("Shop" + section + ".contents" + id + ".cost");
                Material costMaterial = Material.valueOf(main.shops.getConfig().getString("Shop" + section + ".contents" + id + ".materialCost"));

                ItemStack stack = new ItemStack(material, amount);
                ItemMeta stackM = stack.getItemMeta();
                stackM.setLore(Collections.singletonList("Costs " + cost + " " + getItemName(costMaterial.toString())));
                stack.setItemMeta(stackM);
                inv.setItem(i, stack);

            }
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 5, 1);
            p.openInventory(inv);
            main.inv.remove(p);
            main.inv.add(p, inv);


        } else if (isShop(e.getView().getTitle()) && main.inv.isInv(p, e.getClickedInventory())) {
            e.setCancelled(true);

            if (e.getClickedInventory().getItem(e.getSlot()) == null) {
                return;
            }

            if (e.getClickedInventory().getItem(e.getSlot()).getType() != Material.AIR) {


                String section = this.getSectionByName(e.getView().getTitle());
                String id = "." + e.getSlot();

                int cost = main.shops.getConfig().getInt("Shop" + section + ".contents" + id + ".cost");
                String costMaterialS = main.shops.getConfig().getString("Shop" + section + ".contents" + id + ".materialCost");

                if (costMaterialS == null) {
                    return;
                }
                Material costMaterial = Material.valueOf(costMaterialS);

                Material material = Material.valueOf(main.shops.getConfig().getString("Shop" + section + ".contents" + id + ".material"));
                int amount = main.shops.getConfig().getInt("Shop" + section + ".contents" + id + ".amount");

                if (p.getInventory().containsAtLeast(new ItemStack(costMaterial), cost)) {


                    p.getInventory().addItem(new ItemStack(material, amount));
                    p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 5, 1);
                    p.getInventory().removeItem(new ItemStack(costMaterial, cost));
                } else {


                    int has = 0;
                    for (int i = 0; i < p.getInventory().getSize(); i++) {
                        ItemStack itemSlot = p.getInventory().getItem(i);
                        if (itemSlot == null || !itemSlot.isSimilar(new ItemStack(costMaterial))) {
                            continue;
                        }
                        has += itemSlot.getAmount();
                    }
                    Double cost1 = Math.floor(cost - has);
                    if (cost1 <= 0) {
                        return;
                    }

                    p.sendMessage(ChatColor.RED + "You need " + cost1.intValue() + " more " + getItemName(costMaterial.toString()));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);

                }
            }
        }
    }

    public String getSectionByName(String name) {
        boolean isNull = false;
        int i = 0;
        while (!isNull) {
            String id = "." + i;
            String section = "." + main.shops.getConfig().getString("Shop" + ".main" + ".loadSections" + id);


            String sectionName = main.shops.getConfig().getString("Shop" + section + ".name");


            if (sectionName == null) {
                isNull = true;
                continue;
            }

            if (name.equals(sectionName)) {


                return section;
            }
            i++;
        }
        return null;
    }

    public boolean isShop(String name) {
        boolean b = name.equals("Blocks") || name.equals("Swords") || name.equals("Food") || name.equals("Armor") || name.equals("Bows") || name.equals("Tools") || name.equals("Axes") || name.equals("Special");
        return b;
    }

    public String getItemName(String name) {


        name = name.toLowerCase().replace("_", " ").replace("ingot", "").replace("minecraft:", "");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}

