package me.miran.anchorwars.shop.upgradesManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class DisableShield implements Listener {

    Main main;

    public DisableShield(Main main) {
        this.main = main;
    }

    @EventHandler
    public void playerBlock(EntityDamageByEntityEvent e) {


        if (main.gameStart) {
            if (e.getEntityType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
                Player p = (Player) e.getEntity();
                Player k = (Player) e.getDamager();
                if (p.isBlocking()) {
                    if (main.pl.getP(k).hasUpgrade("disableShield")) {
                        Random r = new Random();

                        ItemStack shield;
                        if (p.getInventory().getItemInMainHand().getType() == Material.SHIELD) {
                            shield = p.getInventory().getItemInMainHand();

                        } else if (p.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                            shield = p.getInventory().getItemInOffHand();

                        } else {
                            return;
                        }

                        ItemMeta shieldM = shield.getItemMeta();

                        int level = main.pl.getP(k).getUpgrade("disableShield");


                        int damage = (int) Math.round(e.getDamage() * (level + Math.random()));


                        org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) shieldM;

                        int maxDurability = Material.SHIELD.getMaxDurability();

                        short getDurability = (short) ((short) maxDurability - damageable.getDamage());

                        if (getDurability < 0) {
                            getDurability = (short) maxDurability;
                        }

                        int i;
                        for (i = damage; i > 0; i--) {
                            int durability = shieldM.getEnchantLevel(Enchantment.DURABILITY);
                            int chance = (100 / (durability + 1));//how unbreaking is calculated according to mc wiki
                            int breaking = r.nextInt(100) + 1;


                            if (breaking < chance) {
                                getDurability--;

                            }
                        }
                        shield.setDurability((short) (maxDurability - getDurability));
                        if (p.getInventory().getItemInMainHand().getType() == Material.SHIELD) {

                            p.getInventory().setItemInMainHand(shield);

                        } else { //off hand
                            p.getInventory().setItemInOffHand(shield);

                        }

                    }
                }
            }
        }
    }
}
