package me.miran.anchorwars.shop.upgradesManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class ExplosiveArrows implements Listener {

    Main main;

    public ExplosiveArrows (Main main) {
        this.main = main;
    }

    @EventHandler
    public void arrowHit (ProjectileHitEvent e) {
        if(e.getEntityType() == EntityType.ARROW && e.getEntity().getShooter() instanceof Player && !e.getEntity().hasMetadata("used")) {
            Player p = (Player) e.getEntity().getShooter();
            if (main.pl.getP(p).hasUpgrade("explosive")) {

                Location loc = e.getEntity().getLocation();
                int power = main.pl.getP(p).getUpgrade("explosive");

                Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                    @Override
                    public void run() {
                        explode(loc, power);
                    }
                }, 3L);


                e.getEntity().setMetadata("used", new FixedMetadataValue(main, true)); //prevents from bouncing and this type of stuff

            }
        }
    }

    public void explode (Location loc, int power) {
        World w = loc.getWorld();
        w.createExplosion(loc, (float) (power - 0.5), false, false);
    }

}
