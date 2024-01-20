package me.miran.anchorwars.commands;

import me.miran.anchorwars.core.Main;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.awt.Color;

public class GameStart implements CommandExecutor {

    Main main;


    public GameStart (Main main) {
        this.main = main;

    }




    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {

                p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] " + ChatColor.DARK_GRAY + "If anything went wrong try restarting your server or looking at config.yml");

int x = 0;
                for (Entity entity : main.droppedItems) {
                    x =x + 1;
                    Bukkit.broadcastMessage(""+x);
                    entity.remove();
                }
                main.droppedItems.clear();
                p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Dropped items are " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "cleared" + ChatColor.WHITE + "." );

                main.gameStart = true;
                p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Game start is now " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "true" + ChatColor.WHITE + "." );

                    World world = p.getWorld();

/*if (main.redShop != null) {
    Villager villager = (Villager) world.spawnEntity(main.redShop, EntityType.VILLAGER);
    villager.setAI(true);
    villager.setVillagerLevel(2);
    villager.setInvulnerable(true);
    villager.setProfession(Villager.Profession.WEAPONSMITH);
    villager.setCustomName("shop");
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Red shop was successfully created at " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: " + main.redShop.getX() + ", Y: " + main.redShop.getY() +", Z: " + main.redShop.getZ() + ChatColor.WHITE + "." );
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Red shop is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
}

if (main.blueShop != null) {
    Villager villager1 = (Villager) world.spawnEntity(main.blueShop, EntityType.VILLAGER);
    villager1.setAI(true);
    villager1.setVillagerLevel(2);
    villager1.setInvulnerable(true);
    villager1.setProfession(Villager.Profession.WEAPONSMITH);
    villager1.setCustomName("shop");
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Blue shop was successfully created at " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: " +  main.blueShop.getX() + ", Y: "  + main.blueShop.getY() +", Z: " +  main.blueShop.getZ() + ChatColor.WHITE + "." );
}else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Blue shop is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
}

if (main.yellowShop != null) {
    Villager villager2 = (Villager) world.spawnEntity(main.yellowShop, EntityType.VILLAGER);
    villager2.setAI(true);
    villager2.setVillagerLevel(2);
    villager2.setInvulnerable(true);
    villager2.setProfession(Villager.Profession.WEAPONSMITH);
    villager2.setCustomName("shop");
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Yellow shop was successfully created at " + net.md_5.bungee.api.ChatColor.of(Color.orange)  + "X:" + main.yellowShop.getX() + ", Y: " + main.yellowShop.getY() +", Z: " + main.yellowShop.getZ() + ChatColor.WHITE + "." );
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Yellow shop is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
}

if (main.redUShop != null) {
    Villager villager3 = (Villager) world.spawnEntity(main.redUShop, EntityType.VILLAGER);
    villager3.setAI(true);
    villager3.setVillagerLevel(2);
    villager3.setInvulnerable(true);
    villager3.setProfession(Villager.Profession.TOOLSMITH);
    villager3.setCustomName("upgrade shop");

    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Red upgrade shop was successfully created at " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: " + main.redUShop.getX() + ", Y: " + main.redUShop.getY() +", Z: " + main.redUShop.getZ() + ChatColor.WHITE + "." );
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Red upgrade shop is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
}

if (main.blueUShop != null) {
                    Villager villager4 = (Villager) world.spawnEntity(main.blueUShop, EntityType.VILLAGER);
                    villager4.setAI(true);
                    villager4.setVillagerLevel(2);
                    villager4.setInvulnerable(true);
                    villager4.setProfession(Villager.Profession.TOOLSMITH);
                    villager4.setCustomName("upgrade shop");
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Blue upgrade shop was successfully created at " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: " + main.blueUShop.getX() + ", Y: "  + main.blueUShop.getY() +", Z: " + main.blueUShop.getZ() + ChatColor.WHITE + "." );
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Blue upgrade shop is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
}

if (main.yellowUShop != null) {
                    Villager villager5 = (Villager) world.spawnEntity(main.yellowUShop, EntityType.VILLAGER);
                    villager5.setAI(true);
                    villager5.setVillagerLevel(2);
                    villager5.setInvulnerable(true);
                    villager5.setProfession(Villager.Profession.TOOLSMITH);
                    villager5.setCustomName("upgrade shop");
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Yellow upgrade shop was successfully created at " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: "  + main.yellowUShop.getX() + ", Y: " + main.yellowUShop.getY() +", Z: " + main.yellowUShop.getZ() + ChatColor.WHITE + "." );
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Yellow upgrade shop is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
}*/

if (main.worldBCenter != null) {
    world.getWorldBorder().setCenter(main.worldBCenter);
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] World border center was set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: " + main.worldBCenter.getX() + ", Z: " + main.worldBCenter.getZ() + ChatColor.WHITE + ".");
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] World border center is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
    world.getWorldBorder().setCenter(0, 0);
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] World border center was automatically set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "X: 0, Z: 0" + ChatColor.WHITE + ".");
}
if(main.worldBSize > 0) {
    world.getWorldBorder().setSize(main.worldBSize);
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] World border size was set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + main.worldBSize + ChatColor.WHITE + ".");
} else {
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] World border size is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
    world.getWorldBorder().setSize(1);
    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] World border size was automatically set to " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "1" + ChatColor.WHITE + ".");
}

//Change block to sign
                if (main.joinSign != null) {
                    main.joinSign.getBlock().getWorld().getBlockAt(main.joinSign.getBlock().getLocation()).setType(Material.OAK_WALL_SIGN);

                    //Get an instance of the sign, so you can edit it
                    Sign sign = (Sign) main.joinSign.getBlock().getWorld().getBlockAt(main.joinSign.getBlock().getLocation()).getState();

                    //Set sign
                    sign.setLine(3, ChatColor.RED + "Game is running!");

                    //Update the state of the sign
                    sign.update();
                    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Join sign is now " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "full" + ChatColor.WHITE + "." );
                } else {
                    p.sendMessage("[" + ChatColor.RED + "Debugger" + ChatColor.WHITE + "] Join sign is " + net.md_5.bungee.api.ChatColor.of(Color.orange) + "null" + ChatColor.WHITE + ", ignoring it." );
                }


            } else {
                p.sendMessage(ChatColor.RED + "Only operators can start games!");
            }

            }
        return false;
    }

}