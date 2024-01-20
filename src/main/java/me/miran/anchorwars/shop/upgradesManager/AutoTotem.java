package me.miran.anchorwars.shop.upgradesManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AutoTotem implements Listener {

    Main main;

    public AutoTotem(Main main) {
        this.main = main;
    }

    @EventHandler
    public void playerDamage(EntityDamageEvent e) {

        if (e.getEntityType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();

            if (main.pl.getP(p).hasUpgrade("autoTotem")) {
                double health = p.getHealth();
                double damage = e.getFinalDamage();

                if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID) || e.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)) {
                    return;
                }

                if (p.getInventory().containsAtLeast(new ItemStack(Material.TOTEM_OF_UNDYING), 1)) {
                    if (damage >= health) {
                        if (p.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && p.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
                            e.setCancelled(true);
                            p.getActivePotionEffects().clear();
                            p.setHealth(1);
                            p.addPotionEffect((new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0)));
                            p.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 900, 1)));
                            p.addPotionEffect((new PotionEffect(PotionEffectType.ABSORPTION, 100, 1)));
                            p.playEffect(EntityEffect.TOTEM_RESURRECT);
                            p.sendMessage(ChatColor.GREEN + "Auto-totem saved you!");
                            p.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                        }
                    }
                }
            }
        }
    }


}
