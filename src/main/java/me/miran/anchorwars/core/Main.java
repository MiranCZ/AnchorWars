package me.miran.anchorwars.core;

import me.miran.anchorwars.*;
import me.miran.anchorwars.commands.*;
import me.miran.anchorwars.events.DropItem;
import me.miran.anchorwars.events.Mix;
import me.miran.anchorwars.events.PlayerInteract;
import me.miran.anchorwars.gameManager.*;
import me.miran.anchorwars.gameManager.gameSetuping.GameSetup;
import me.miran.anchorwars.gameManager.gameSetuping.Setup;
import me.miran.anchorwars.gameManager.kitManager.kitsManager.KitsManager;
import me.miran.anchorwars.gameManager.teamManager.DataManager;
import me.miran.anchorwars.gameManager.teamManager.TeamsManager;
import me.miran.anchorwars.generators.Generators;
import me.miran.anchorwars.generators.fileManager.GensManager;
import me.miran.anchorwars.mapReset.GameMap;
import me.miran.anchorwars.mapReset.LocalGameMap;
import me.miran.anchorwars.playerMain.PlayerList;
import me.miran.anchorwars.shop.OpenShop;
import me.miran.anchorwars.shop.OpenUpgradeShop;
import me.miran.anchorwars.shop.ShopManager;
import me.miran.anchorwars.shop.upgradesManager.*;
import me.miran.anchorwars.shop.ymlManager.ShopsManager;
import me.miran.anchorwars.worldsManager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public final class Main extends JavaPlugin {


    public PlayerList pl = new PlayerList(this);
    public GameSetup setup = new GameSetup(this);
    public DenyManager deny = new DenyManager(this);
    public KitsManager kits = new KitsManager(this);
    public Phases phase = new Phases(this);
    public DataManager teamData = new DataManager(this);
    public TeamsManager teams = new TeamsManager(this);
    public ShopsManager shops = new ShopsManager(this);
    public PlayerRespawn respawn = new PlayerRespawn(this);
    public CustomMethods customMe = new CustomMethods(this);
    public OpenUpgradeShop uShop = new OpenUpgradeShop(this);
    public OpenShop shop = new OpenShop(this);
    public ScoreBoard scoreBoard = new ScoreBoard(this);
    public Generators gen = new Generators(this);
    public GensManager gens = new GensManager(this);
    public WorldManager worlds = new WorldManager(this);
    public StartGame startGame = new StartGame(this);
    public EndGame endGame = new EndGame(this);
    public LocationManager loc = new LocationManager();
    public InventoryManager inv = new InventoryManager();


    //Booleans
    public Boolean gameStart = false;
    public Boolean remove = false;
    public Boolean pvp = false;
    public Boolean anchorDmg = false;
    public Boolean reload = false;

    //Locations:
    public Location joinSign = null;
    public Location waitingLobby = null;
    public Location worldBCenter = null;
    public Location mainLobby = null;


    public ArrayList<Entity> droppedItems = new ArrayList<>();


    public HashMap<Block, Integer> genUid = new HashMap<>();
    public HashMap<Integer, Integer> genTime = new HashMap<>();
    public HashMap<Integer, Material> genItem = new HashMap<>();
    public HashMap<Integer, Location> genLoc = new HashMap<>();
    public HashMap<Integer, Integer> genStarterLvl = new HashMap<>();


    public ArrayList<Block> playerPlaced = new ArrayList<>();


    //Double
    public double worldBSize = -1;
    public double eCrystalPower = 6;
    public double yBorder = 0;


    public Integer hitCoolDown = 10;
    public Integer minPlayerCount = 1;


    public GameMap map = null;
    public GameMap lobby = null;
    public GameMap wLobby = null;

    public GameState gameState = GameState.WAITING;


    public void onEnable() {
        createMaps();
        loadVariables();
        registerCommands();
        registerEvents();

       // gen.loadGens();
        this.saveDefaultConfig();//creates config.yml
    }

    private void createMaps() {
        getDataFolder().mkdirs();
        File gameMapsFolder = new File(getDataFolder(), "gameMaps");
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }
        map = new LocalGameMap(gameMapsFolder, worlds.getConfig().getString("gameMap"), false);
        wLobby = new LocalGameMap(gameMapsFolder, worlds.getConfig().getString("waitingLobby"), false);
        lobby = new LocalGameMap(gameMapsFolder, worlds.getConfig().getString("lobby"), true);

        if (!lobby.isLoaded()) {
            System.out.println("FUCK SHIT FUCK FUCK SHIT FUCK");
        } else {
            System.out.println("LOADED");
        }
    }

    private void registerCommands() {
        //set
        getServer().getPluginCommand(".setYaw").setExecutor(new Yaw(this));//needs check
        getServer().getPluginCommand(".setYBorder").setExecutor(new YBorder(this));
        getServer().getPluginCommand(".setCrystalPower").setExecutor(new CrystalPower(this));
        getServer().getPluginCommand(".setTeam").setExecutor(new SetTeam(this));//needs check
        getServer().getPluginCommand(".setAnchorPercentage").setExecutor(new SetAnchorPercentage(this));
        getServer().getPluginCommand(".setCombatCoolDown").setExecutor(new CombatCoolDown(this));
        getServer().getPluginCommand(".setBorderSize").setExecutor(new WorldBorderSize(this));
        getServer().getPluginCommand(".setMinPlayersWaiting").setExecutor(new SetMinPlayersWaiting(this));

        //admin commands
        getServer().getPluginCommand(".clearBlocks").setExecutor(new ClearPPlacedBlocks(this));
        getServer().getPluginCommand(".teleport").setExecutor(new Teleport(this));
        getServer().getPluginCommand(".setup").setExecutor(new Setup(this));

        //@a commands
        getServer().getPluginCommand("shout").setExecutor(new Shout());
        getServer().getPluginCommand("leave").setExecutor(new Leave(this));
        getServer().getPluginCommand("hub").setExecutor(new Leave(this));
        getServer().getPluginCommand("lobby").setExecutor(new Leave(this));

    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerList(this), this);
        getServer().getPluginManager().registerEvents(new ExplosiveArrows(this), this);
        getServer().getPluginManager().registerEvents(new Mix(this), this);
        getServer().getPluginManager().registerEvents(new OpenShop(this), this);
        getServer().getPluginManager().registerEvents(new DestroyAnchor(this), this);
        getServer().getPluginManager().registerEvents(new CrystalPower(this), this);
        getServer().getPluginManager().registerEvents(new Generators(this), this);
        getServer().getPluginManager().registerEvents(new RocketLauncher(this), this);
        getServer().getPluginManager().registerEvents(new DisableShield(this), this);
        getServer().getPluginManager().registerEvents(new ChargeAnchor(this), this);
        getServer().getPluginManager().registerEvents(new DeathMassages(this), this);
        getServer().getPluginManager().registerEvents(new AutoTotem(this), this);
        getServer().getPluginManager().registerEvents(new ShopManager(this), this);
        getServer().getPluginManager().registerEvents(new DropItem(this), this);
        getServer().getPluginManager().registerEvents(new GameSetup(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new Setup(this), this);
        getServer().getPluginManager().registerEvents(new MapProtection(this), this);
        getServer().getPluginManager().registerEvents(new ChatPrefix(this), this);
        getServer().getPluginManager().registerEvents(new YBorder(this), this);
        getServer().getPluginManager().registerEvents(new ItemPickUp(this), this);
    }


    public void onDisable() {
        map.unload();
        wLobby.unload();
        lobby.unload();
        if (reload) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer("Server reload detected! You can join back after a few seconds.");
            }
        }

        // Plugin shutdown logic
        this.saveVariables();
        gen.saveGens();
        teamData.saveTeams();


    }


    public void loadVariables() {
        //Upgrades
        //Locations
        joinSign = loc.decompileLoc(getConfig().getString("Locations" + ".joinSign"));

        waitingLobby = loc.decompileLoc(getConfig().getString("Locations" + ".waitingLobby"));
        waitingLobby.setWorld(wLobby.getWorld());

        worldBCenter = loc.decompileLoc(getConfig().getString("Locations" + ".worldBCenter"));

        mainLobby = loc.decompileLoc(getConfig().getString("Locations" + ".mainLobby"));
        mainLobby.setWorld(lobby.getWorld());


        //Double
        worldBSize = getConfig().getDouble("Double" + ".worldBSize");
        yBorder = getConfig().getDouble("Double" + ".yBorder");
        minPlayerCount = getConfig().getInt("Int." + "playersWaiting");


    }

    public void saveVariables() {


        getConfig().set("Long." + "hitCoolDown", hitCoolDown);
        getConfig().set("Double." + "eCrystalPower", eCrystalPower);
        getConfig().set("Double." + "yBorder", yBorder);
        getConfig().set("Locations." + "joinSign", loc.compileLoc(joinSign, true));
        getConfig().set("Locations." + "waitingLobby", loc.compileLoc(waitingLobby, true));
        getConfig().set("Locations." + "worldBCenter", loc.compileLoc(worldBCenter, true));
        getConfig().set("Locations." + "mainLobby", loc.compileLoc(mainLobby, true));
        getConfig().set("Double." + "worldBSize", +worldBSize);
        getConfig().set("Int." + "playersWaiting", +minPlayerCount);
        saveConfig();

        this.gen.saveGens();
    }


}

