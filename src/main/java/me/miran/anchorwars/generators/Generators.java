package me.miran.anchorwars.generators;

import me.miran.anchorwars.core.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Generators implements Listener {

    private static int taskID;
    private static boolean isGenerating = false;

    private static HashMap<Inventory, Boolean> maxLevel = new HashMap<>();
    private static HashMap<Inventory, Block> bl = new HashMap<>();
    private static HashMap<Inventory, Integer> generatorTime = new HashMap<>();
    private static HashMap<Inventory, Material> generatorItem = new HashMap<>();
    private static HashMap<Inventory, ItemStack> cost = new HashMap<>();
    private static HashMap<Inventory, Integer> uid = new HashMap<>();


    Main main;

    public Generators(Main main) {
        this.main = main;
    }

    public void reset () {
        maxLevel= new HashMap<>();
        bl = new HashMap<>();
        generatorTime = new HashMap<>();
        generatorItem = new HashMap<>();
        cost = new HashMap<>();
        uid = new HashMap<>();
        loadGens();
    }

    public void saveGens() {
        ArrayList<Integer> uids = new ArrayList<>();

        int i;

        for ( i = 0; i < main.genUid.size(); i++ ) {
            uids.add(i);
        }

        for ( i = 0; i < uids.size(); i++ ) {
            int id = uids.get(i);
            String idS = id + ".";
            main.gens.getConfig().set("Generators.locations." + idS + "item", main.genItem.get(id).toString());
            main.gens.getConfig().set("Generators.locations." + idS + "level", main.genStarterLvl.get(id));
            main.gens.getConfig().set("Generators.locations." + idS + "location", main.loc.compileLoc(main.genLoc.get(id), false) );


        }
        main.gens.saveConfig();
    }

    public void loadGens() {
        int i = 0;

        System.out.println("----- LOADING GENS ------");
        System.out.println(main.gens.getConfig().getValues(true));

        while (true) {

            String id = "." + i;


            String itemS = main.gens.getConfig().getString("Generators.locations." + id + ".item");
            System.out.println("CURRENT ITEM: "+ itemS);

            int lvl = main.gens.getConfig().getInt("Generators.locations." + id + ".level");
            Location loc = main.loc.decompileLoc(main.gens.getConfig().getString("Generators.locations." + id + ".location"));

            if (loc == null || itemS == null) {
                break;
            }

            loc.setWorld(main.map.getWorld());

            Material item = Material.valueOf(itemS);//for some reasson material has problem with being null
            int prepare = (int) main.customMe.getStarterValue(item).get(lvl);


            if (!main.genLoc.containsValue(loc)) {
                this.createGen(prepare, item, loc, lvl);
            }
            i++;

        }
    }

    public void startGen() {
        if (!isGenerating) {
            isGenerating = true;
            generateItem();
        }
    }

    public void stopGen() {
        if (isGenerating) {
            isGenerating = false;
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }


    public void generateItem() {

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {

                if(!main.map.isLoaded()) {
                    main.map.load();
                }

                for ( Map.Entry<Integer, Integer> entry : main.genTime.entrySet() ) {
                    int key = entry.getKey(); //uid
                    int value = entry.getValue();

                    Location l = main.genLoc.get(key);
                    l.setWorld(main.map.getWorld());
                    Block b = l.getBlock();


                    if (value < 900) {
                        value = value - 1;
                    }

                    if (value <= 0 && main.customMe.getGenLvl(b) >= 0) {
                        ItemStack item = new ItemStack(main.genItem.get(key));
                        b.getWorld().dropItem(b.getLocation().add(0.5, 1, 0.5), item);
                        value = (Integer) main.customMe.getStarterValue(item.getType()).get(main.customMe.getGenLvl(b));

                    }

                    main.genTime.put(key, value);
                }


            }

        }, 0L, 20L);
    }

    public void createGen(Integer time, Material item, Location location, int level) {

        int uid = main.genTime.size();
        main.genTime.put(uid, time);
        main.genItem.put(uid, item);
        main.genLoc.put(uid, location);
        main.genStarterLvl.put(uid, level);
        main.genUid.put(location.getBlock(), uid);
        main.customMe.setGenLvl(location.getBlock(), level);
        main.customMe.setGenItem(location.getBlock(), item);

        //saveGens();

    }

    public void removeGen(Block block) {
        int uid = main.genUid.get(block);

        main.genTime.remove(uid);
        main.genItem.remove(uid);
        main.genLoc.remove(uid);
        main.customMe.setGenLvl(block, -1);
        main.customMe.setGenItem(block, Material.AIR);
    }


    @EventHandler
    public void generatorClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Player p = e.getPlayer();

            Block b = e.getClickedBlock();
            /*this.bl = e.getClickedBlock();
            //Material m = bl.getType();
            maxLevel = false;*/

            if (main.genUid.get(b) != null) {

                Inventory generator = Bukkit.createInventory(p, 27, "Generator");

                boolean mxLvl = false;


                if (!(p.getInventory().equals(generator))) {
                    String item = main.shop.getItemName(main.customMe.getGenItem(b).toString());
                    ChatColor color = ChatColor.WHITE;


                    int id = main.genUid.get(b);
                    if (main.customMe.getGenUpgradePrize(main.customMe.getGenItem(b)).size() == 0) {
                        return;
                    }

                    ItemStack costItem = (ItemStack) main.customMe.getGenUpgradePrize(main.customMe.getGenItem(b)).get(main.customMe.getGenLvl(b));
                    if (costItem == null || costItem.equals(new ItemStack(Material.AIR)) /* || main.customMe.getStarterValue(main.customMe.getGenItem(b)).size() >= main.customMe.getGenLvl(b)*/) {
                        mxLvl = true;
                    }


                    Material genItem = main.customMe.getGenItem(b);

                    int genTime = 1000;
                    if (!mxLvl) {
                        genTime = (Integer) main.customMe.getStarterValue(main.customMe.getGenItem(b)).get(main.customMe.getGenLvl(b) + 1);
                    }

                    ItemStack upgrade = new ItemStack(Material.EXPERIENCE_BOTTLE);
                    ItemMeta upgradeM = upgrade.getItemMeta();
                    upgradeM.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Upgrade");
                    if (!mxLvl) {
                        upgradeM.setLore(Arrays.asList(ChatColor.GRAY + "Next upgrade: " + genTime + "s", ChatColor.GRAY + "Upgrade costs " + costItem.getAmount() + " " + main.shop.getItemName(costItem.getType().toString())));
                    } else {
                        upgradeM.setLore(Arrays.asList(ChatColor.GRAY + "This generator is on maximum level"));
                    }
                    upgrade.setItemMeta(upgradeM);

                    ItemStack time = new ItemStack(b.getType());
                    ItemMeta timeM = upgrade.getItemMeta();

                    timeM.setDisplayName(color + "" + ChatColor.BOLD + item + " generator");


                    int generatingTime = (Integer) main.customMe.getStarterValue(main.customMe.getGenItem(b)).get(main.customMe.getGenLvl(b));

                    if (generatingTime < 900) {
                        timeM.setLore(Arrays.asList(ChatColor.GRAY + "Current generating time: " + generatingTime + "s"));
                    } else {
                        timeM.setLore(Arrays.asList(ChatColor.GRAY + "Current generating time: 0s"));
                    }
                    time.setItemMeta(timeM);

                    generator.clear();
                    generator.setItem(11, time);
                    generator.setItem(15, upgrade);

                    bl.put(generator, e.getClickedBlock());
                    maxLevel.put(generator, mxLvl);
                    uid.put(generator, id);
                    cost.put(generator, costItem);
                    generatorItem.put(generator, genItem);
                    generatorTime.put(generator, genTime);

                    p.openInventory(generator);

                }


            }
        }
    }


    @EventHandler
    public void upgradeClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Generator") && e.getInventory().getHolder() == e.getWhoClicked()) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if (e.getSlot() == 15) {

                Inventory inv = e.getClickedInventory();

                boolean mxLvl = maxLevel.get(inv);


                if (!mxLvl) {
                    ItemStack costItem = cost.get(inv);
                    if (/*p.getInventory().contains(cost) ||*/ p.getInventory().containsAtLeast(costItem, costItem.getAmount())) {
                        /*main.getGeneratorManager().removeGenerator(bl);
                        main.getGeneratorManager().addGenerator(bl, new ItemStack(generatorItem), generatorTime);
                        main.generator.remove(bl);
                        main.generator.put(bl, generatorItem);*/
                        int genTime = generatorTime.get(inv);
                        int id = uid.get(inv);
                        Block b = bl.get(inv);


                        main.genTime.put(id, genTime);
                        int level;
                        if (main.customMe.getGenLvl(b) > 0) {
                            level = main.customMe.getGenLvl(b) + 1;
                        } else {
                            level = 1;
                        }
                        main.customMe.setGenLvl(b, level);

                        Firework fw = (Firework) p.getLocation().getWorld().spawnEntity(main.genLoc.get(main.genUid.get(b)).add(0.5, 1.5, 0.5), EntityType.FIREWORK);

                        main.genLoc.get(main.genUid.get(b)).add(-0.5, -1.5, -0.5);

                        FireworkMeta fwm = fw.getFireworkMeta();

                        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(0, 255, 0)).trail(true).build());

                        fw.setMetadata("nodamage", new FixedMetadataValue(main, true));
                        fw.setFireworkMeta(fwm);
                        fw.detonate();

                        p.getInventory().removeItem(costItem);

                        p.closeInventory();
                        bl.remove(inv);
                        maxLevel.remove(inv);
                        uid.remove(inv);
                        cost.remove(inv);
                        generatorItem.remove(inv);
                        generatorTime.remove(inv);

                    } else {

                        int has = 0;
                        for ( int i = 0; i < 36; i++ ) {
                            ItemStack slot = p.getInventory().getItem(i);
                            if (slot == null || !slot.isSimilar(new ItemStack(costItem.getType())))
                                continue;
                            has += slot.getAmount();
                        }

                        Double cost1 = Math.floor(costItem.getAmount() - has);


                        String costI = costItem.getType().toString().toLowerCase().replace("_ingot", "");

                        p.sendMessage(ChatColor.RED + "You need " + cost1.intValue() + " more " + costI + "!");

                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "This generator is on maximum level!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5, 1);

                }
            }

        }
    }



    @EventHandler
    public void invClose (InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("Generator") && e.getInventory().getHolder() == e.getPlayer()) {
            Inventory inv = e.getInventory();
            bl.remove(inv);
            maxLevel.remove(inv);
            uid.remove(inv);
            cost.remove(inv);
            generatorItem.remove(inv);
            generatorTime.remove(inv);
        }
        }
    }



