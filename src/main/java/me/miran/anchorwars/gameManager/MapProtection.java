package me.miran.anchorwars.gameManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;

public class MapProtection implements Listener {

    Main main;

    public MapProtection(Main main) {
        this.main = main;
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {

        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("axe") || p.getInventory().getItemInOffHand().getType().toString().toLowerCase().contains("axe")) {
            e.setCancelled(true);
            return;
        }

        if (!p.isOp() || p.getGameMode() != GameMode.CREATIVE) {
            if (!main.gameStart) {
                e.setCancelled(true);
            }
        }
        Block bl = e.getBlock();
        int i;
        if (bl.getType().hasGravity()) {
            for (i = 1; i < 255; i++) {

                if (bl.getLocation().add(0, -i, 0).getBlock().getType() != Material.AIR) {
                    main.playerPlaced.add(bl.getLocation().add(0, (-i + 1), 0).getBlock());

                    return;
                }
            }
        } else {

            main.playerPlaced.add(bl);
        }

    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        Block bl = e.getBlock();
        Player p = e.getPlayer();

        if (!main.map.isLoaded() || p.getWorld() != main.map.getWorld()) {
            e.setCancelled(true);
            return;
        }

        if (bl.getType() == Material.FIRE || bl.getType() == Material.GRASS || bl.getType() == Material.TALL_GRASS || bl.getType().toString().contains("LEAVES")) {
            e.setCancelled(true);
            p.getWorld().getBlockAt(bl.getLocation()).setType(Material.AIR);
            return;
        }


        if (p.isOp() && p.getGameMode() == GameMode.CREATIVE) {
            main.playerPlaced.remove(bl);
            return;
        }


        if (!main.playerPlaced.contains(bl)) {
            e.setCancelled(true);
            main.deny.add(p, "&cSorry, but you can only break blocks placed by players!");

        }


    }


    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        if (main.phase.getPhase() == 5) {
            return;
        }

        for (Block bl : new ArrayList<>(e.blockList())) {
            if (!main.playerPlaced.contains(bl)) {
                e.blockList().remove(bl);
            } else {
                main.playerPlaced.remove(bl);
            }
        }
    }

    @EventHandler
    public void burnBlock(BlockBurnEvent e) {

        Block bl = e.getBlock();
        if (!main.playerPlaced.contains(bl)) {
            e.setCancelled(true);

        }
    }


    @EventHandler
    public void onBlockExplodeByEntity(EntityExplodeEvent e) {
        if (main.phase.getPhase() == 5) {
            Location l = e.getLocation();
            l.getWorld().createExplosion(l, 5);
            return;
        }

        for (Block bl : new ArrayList<>(e.blockList())) {
            if (!main.playerPlaced.contains(bl)) {
                e.blockList().remove(bl);
            } else {
                main.playerPlaced.remove(bl);
            }
        }
    }

}



