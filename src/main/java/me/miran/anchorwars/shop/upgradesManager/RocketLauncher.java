package me.miran.anchorwars.shop.upgradesManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Random;


public class RocketLauncher implements Listener {

    Main main;

    public RocketLauncher(Main main) {
        this.main = main;
    }

    @EventHandler
    public void playerCrossbowShoot(EntityShootBowEvent e) {

        if (e.getEntityType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();
            if (main.pl.getP(p).hasUpgrade("rocketLauncher")) {
                if (e.getBow().getType() == Material.CROSSBOW) {
                    Random r = new Random();
                    int random = r.nextInt(10);
                    if (random < 8) {

                        this.spawnFirework(p.getLocation(), Color.fromBGR(204, 0, 204), e.getProjectile().getVelocity());
                        e.setCancelled(true);

                    }
                }
            }
        }
    }


    private void spawnFirework(Location location, Color color, Vector velocity) {

        Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);

        FireworkMeta fwm = fw2.getFireworkMeta();
        fwm.setPower(1);
        fwm.addEffect(FireworkEffect.builder().withColor(color).trail(true).build());

        fw2.setVelocity(velocity);
        fw2.setFireworkMeta(fwm);

    }


}
