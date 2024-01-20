package me.miran.anchorwars.events;

import me.miran.anchorwars.core.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class Mix implements Listener {

    Main main;

    public Mix (Main main) {
        this.main = main;
    }

    @EventHandler
    public void onHorseSpawn (EntitySpawnEvent e) {
        if (e.getEntityType() == EntityType.HORSE) {
            Horse horse = (Horse) e.getEntity();
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        }
    }

    @EventHandler
    public void mobSpawn (CreatureSpawnEvent e) {
        if ((e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DEFAULT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerJoined (PlayerJoinEvent e){

            Player p = e.getPlayer();


            main.customMe.tpToLobby(p);
            main.customMe.resetJoinSign();

        //}
    }

    @EventHandler
    public void craft (CraftItemEvent e){
        e.setCancelled(true);
    }


    @EventHandler
    public void villagerTransform (EntityTransformEvent e) {
        if (e.getEntity().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage (EntityDamageByEntityEvent e) {
    if (e.getEntityType() == EntityType.VILLAGER) {
        e.setCancelled(true);
    } else if (e.getDamager() instanceof Firework) {
        Firework fw = (Firework) e.getDamager();
        if (fw.hasMetadata("nodamage")) {
            e.setCancelled(true);
        }
    }
}

@EventHandler
    public void food(FoodLevelChangeEvent e) {
        if(e.getEntityType() == EntityType.PLAYER ) {
            if(!main.map.isLoaded() || main.map.getWorld() != e.getEntity().getWorld()) {
                e.setCancelled(true);
            }

        }
}

@EventHandler
    public void onPlayerTakeDamage (EntityDamageEvent e) {
        if(e.getEntityType() == EntityType.PLAYER  ) {
            if (!main.map.isLoaded() || e.getEntity().getLocation().getWorld() != main.map.getWorld()) {
                Player p = (Player) e.getEntity();
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setCancelled(true);
                    if (p.getWorld() == main.lobby.getWorld()) {
                        Location loc = main.mainLobby;
                        loc.setWorld(main.lobby.getWorld());

                        loc.setY(loc.getY() + 1.5);
                        p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        loc.setY(loc.getY() - 1.5);
                    } else if (p.getWorld() == main.wLobby.getWorld()) {
                        Location loc = main.waitingLobby;
                        loc.setWorld(main.wLobby.getWorld());
                        loc.setY(loc.getY() + 1.5);
                        p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        loc.setY(loc.getY() - 1.5);
                    }
                } else {
                    e.setCancelled(true);
                }


            }
        }
}



@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("/reload") && e.getPlayer().isOp()) {
    main.reload = true;
        }
}

@EventHandler
    public void pDmg (EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
          if (  main.pl.getP((Player) e.getEntity()).isProtected()) {
              e.setCancelled(true);
          }
        }
}


}
