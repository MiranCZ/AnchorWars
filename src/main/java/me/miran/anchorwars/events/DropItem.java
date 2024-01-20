package me.miran.anchorwars.events;

import me.miran.anchorwars.core.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class DropItem implements Listener {


    Main main;

    public DropItem(Main main) {
        this.main = main;

    }


    @EventHandler
    public void dropItem(EntitySpawnEvent e) {

        if (e.getEntityType() == EntityType.DROPPED_ITEM) {
            main.droppedItems.add(e.getEntity());

        }
    }


}
