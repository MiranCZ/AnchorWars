package me.miran.anchorwars.shop.upgradesManager;

import me.miran.anchorwars.core.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ItemPickUp implements Listener {
    Main main;


    public ItemPickUp(Main main) {
        this.main = main;
    }

    @EventHandler
    public void playerPick (EntityPickupItemEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {

            if(main.gameStart) {


                Player p = (Player) e.getEntity();

                if (main.pl.getP(p).hasUpgrade("picker")) {

                    Material item = e.getItem().getItemStack().getType();


                    String name = item.toString().toLowerCase();

                    if (name.contains("_ingot") || item == Material.BRICK || item == Material.DIAMOND) {

                        // if (p.getInventory().contains(new ItemStack(Material.SHULKER_BOX))) {
                        ItemStack[] items = p.getInventory().getContents();

                        int i = 0;

                        for ( ItemStack stack : items ) {
                            i++;
                            if (stack == null) {
                                continue;
                            }
                            if (stack.getType() == Material.SHULKER_BOX) {
                                int slot = i - 1;


                                ItemStack itemStack = p.getInventory().getItem(slot);
                                BlockStateMeta bsm = (BlockStateMeta) itemStack.getItemMeta();
                                ShulkerBox box = (ShulkerBox) bsm.getBlockState();

                                int space = 0;

                                ShulkerBox shulker = (ShulkerBox) bsm.getBlockState();

                                for ( ItemStack items1 : shulker.getInventory().getContents() ) {
                                    if (items1 == null) {
                                        space += 64;
                                        continue;
                                    }
                                    if (items1.getType() == item || items1.getAmount() < 64) {
                                        space += (64 - items1.getAmount());
                                    }
                                }

                                if (space < e.getItem().getItemStack().getAmount()) {
                                    continue;
                                }


                                //box.getInventory().setContents(box.getInventory().getContents());
                                box.getInventory().addItem(e.getItem().getItemStack());
                                bsm.setBlockState(box);
                                box.update();

                                itemStack.setItemMeta(bsm);

                                p.getInventory().remove(p.getInventory().getItem(slot));
                                p.getInventory().setItem(slot, itemStack);

                                e.setCancelled(true);
                                e.getItem().remove();

                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 5);
                                return;

                            }
                        }
                        //}
                    }
                }
            }

        }
    }

}
