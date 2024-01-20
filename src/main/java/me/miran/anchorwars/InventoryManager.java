package me.miran.anchorwars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class InventoryManager implements Listener {
    private final HashMap<Player, Inventory> invents = new HashMap<>();

    public void add (Player p, Inventory inv) {
        invents.put(p,inv);
    }

    public void remove (Player p) {
        if (invents.get(p) != null) {
            invents.remove(p);
        }
    }

    public boolean isInv (Player p, Inventory inv) {
        if (invents.get(p) != null) {
            return invents.get(p).toString().equals(inv.toString());
        }
        return false;
    }

    @EventHandler
    private void invClose (InventoryCloseEvent e) {
        if (invents.get(e.getPlayer()) != null) {
            invents.remove(e.getPlayer());

        }
    }

}
