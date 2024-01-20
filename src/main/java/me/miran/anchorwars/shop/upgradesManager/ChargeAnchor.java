package me.miran.anchorwars.shop.upgradesManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.Material;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Random;

public class ChargeAnchor implements Listener {

    Main main;

    public ChargeAnchor (Main main) {
        this.main = main;
    }

    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.RESPAWN_ANCHOR) {
            Player p = e.getPlayer();
            if (main.pl.getP(p).hasUpgrade("chargeAnchor")) {
                Random r = new Random();
                int random = r.nextInt(10);
                if (random < 2) {

                    RespawnAnchor anchor = (RespawnAnchor) e.getBlock().getBlockData();
                    anchor.setCharges(4);
                     e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.RESPAWN_ANCHOR);
                    e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setBlockData(anchor);


               }
            }
        }
    }

}
