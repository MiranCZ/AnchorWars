package me.miran.anchorwars.gameManager.kitManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Kits {

    private static final HashMap<Player, String> kits = new HashMap<>();

    public void setKit (Player p, String kit) {
kits.put(p, kit);
    }

    private String getKit (Player p) {
        return kits.get(p);
    }

    public void applyKit (Player p) {
    String kit = getKit(p).toLowerCase();

    switch (kit) {
        case "fighter" : {
ItemStack[] inv = p.getInventory().getContents();

int mostDmg = 0;
ItemStack stack = null;

int i = 0;

for(ItemStack item : inv) {
    if(item == null) {
        i++;
        continue;
    }

    int dmg = getItemDamageValue(item.getType(), item.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
  if (dmg > mostDmg) {
      mostDmg = dmg;
      stack = item;
  }
  i++;
}
            ArrayList<ItemStack> item = new ArrayList<>();
item.add(stack);
deleteItems(p, item);

            break;
        } case "armorer" :{
ItemStack[] armor = p.getInventory().getArmorContents();
p.getInventory().setArmorContents(null);
deleteItems(p, new ArrayList<>());
p.getInventory().setArmorContents(armor);
            break;
        } case "repairer" :{

            deleteItems(p, new ArrayList<>());
            for (ItemStack stack : p.getInventory().getContents()) {
                if (stack == null) {
                    continue;
                }
                if (!Enchantment.DURABILITY.canEnchantItem(stack)) {
                    continue;
                }
                Damageable damageable = (Damageable) stack.getItemMeta();

                int maxDur = stack.getType().getMaxDurability();
                int dur = damageable.getDamage();


                if (maxDur/2 < dur) {

                    damageable.setDamage(0);

                    stack.setItemMeta((ItemMeta) damageable);
                }

            }

            break;
        } case "better" :{
            Random r = new Random();
            int i = r.nextInt(3);
            if (i < 2) {
                i = r.nextInt(3);

                if(i == 0) {
                    p.getInventory().clear();
                }

            }
            break;
        }
    }
    }


    private void deleteItems(Player p, ArrayList<ItemStack> keep) {
        ItemStack[] inv = p.getInventory().getContents();

        p.getInventory().clear();
Random rand = new Random();
int r;
int i;
        for (i = 0; i < inv.length; i++) {



            r = rand.nextInt(2);

            if (r== 1 || keep.contains(inv[i])) {
                keep.remove(inv[i]);
                p.getInventory().setItem(i, inv[i]);
            }

        }
    }


    private int getItemDamageValue(Material item, int sharpness) {
        int damageValue = 0;
        if (item != null) {
             if (item == Material.WOODEN_SWORD || item == Material.GOLDEN_SWORD) {
                damageValue = 4;
            } else if (item == Material.STONE_SWORD) {
                damageValue = 5;
            } else if (item == Material.IRON_SWORD) {
                damageValue = 6;
            } else if (item == Material.DIAMOND_SWORD) {
                damageValue = 7;
            }  else if (item == Material.NETHERITE_SWORD) {
                damageValue = 8;
            } else if (item == Material.WOODEN_AXE ||item == Material.GOLDEN_AXE) {
                damageValue = 7;
            } else if (item == Material.STONE_AXE || item == Material.IRON_AXE || item == Material.DIAMOND_AXE) {
                damageValue = 9;
            } else if (item == Material.NETHERITE_AXE) {
                damageValue = 10;
            } else {
                damageValue = 1;
            }
        }
        damageValue += 0.5 * sharpness + 0.5;//according to mc wiki
        return damageValue;
    }


}
