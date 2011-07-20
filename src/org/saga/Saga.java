/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.logging.*;

//imports from this project
import org.sk89q.*;
import org.saga.utility.*;
import org.saga.exceptions.*;

//External Imports
import java.util.*;
import java.io.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.anjocaido.groupmanager.*;
import org.anjocaido.groupmanager.data.*;
import org.anjocaido.groupmanager.dataholder.*;
import org.anjocaido.groupmanager.dataholder.worlds.*;


/**
 *
 * @author Cory
 */
public class Saga extends JavaPlugin {

    //Static Members
    public static final Logger log = Logger.getLogger("Saga");
    public static boolean debugging = true;
    public static Saga instance;

    //Instance Members
    private static CommandsManager<Player> commandMap;
    public WorldsHolder worldsHolder;
    private boolean playerInformationLoadingDisabled;
    private boolean playerInformationSavingDisabled;
    private Properties balanceProperties;
    private HashMap<String,SagaPlayer> sagaPlayers;
    private SagaPlayerListener playerListener;

    public Saga() {

    }

    static public Saga plugin() {
        return instance;
    }

    @Override
    public void onDisable() {

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        sagaPlayers = null;

        //Remove Global Instance
        Saga.instance = null;

    	//Say Goodbye
        Saga.info("Saga Goodbye!");

    }

    @Override
    public void onEnable() {
        
        //Say Hello!
        Saga.info("Saga Hello!");

        //Set Global Plugin Instance Variable
        Saga.instance = this;

        //Allocate Instance Variables
        sagaPlayers = new HashMap<String,SagaPlayer>();

        PluginManager pluginManager = getServer().getPluginManager();
        Plugin test = null;

        //Test for specific plugins
        test = pluginManager.getPlugin("GroupManager");
        if ( test != null ) {
            Saga.info("Saga found Group Manager plugin!");
            worldsHolder = ((GroupManager)test).getWorldsHolder();
        }

        //Setup Command Manager
        commandMap = new CommandsManager<Player>() {
            @Override
            public boolean hasPermission(Player player, String perm) {

                if ( worldsHolder != null ) {
                    OverloadedWorldHolder world = worldsHolder.getWorldData(player);
                    User user = world.getUser(player.getName());
                    return world.getPermissionsHandler().checkUserPermission(user, perm);
                } else  {
                    return player.isOp();
                }

            }

        };

        // Read balance information:
        balanceProperties = new Properties();
        try {
            balanceProperties = WriterReader.readBalanceInformation();
        } catch (FileNotFoundException e) {
            Saga.severe("Missing balance information.");
            //TODO Create new file with default balance information and add .info for it
        }catch (IOException e) {
            Saga.exception("Balance information load failure.",e);
        }

        //Create listeners
        playerListener = new SagaPlayerListener(this);

        // Register events
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);

        //Register Command Classes to the command map
        commandMap.register(SagaCommands.class);

    }

    public Properties getBalanceProperties() {
        return balanceProperties;
    }

    public SagaPlayer wrapPlayer(Player player) {

        if ( player == null ) {
            Saga.warning("Can't wrap null player");
            return null;
        }

        //First try to get from loaded player map data
        SagaPlayer wrappedPlayer = sagaPlayers.get(player.getName());

        if ( wrappedPlayer != null ) {
            return wrappedPlayer;
        }

        Saga.debug("wrapping player "+player.getName());

        //Second try, attempt to load data from file
        try {
            wrappedPlayer = SagaPlayer.load(player.getName());
        } catch( SagaPlayerNotFoundException e ) {
            //Player With New Data
            wrappedPlayer = new SagaPlayer(player,balanceProperties);
        }

        return wrappedPlayer;

    }

    public void addPlayer(Player player) {

    	// Wrap the player and add him:
    	sagaPlayers.put(player.getName(), wrapPlayer(player));
        Saga.debug("adding player "+player.getName());

    }

    public void removePlayer(Player player) {

        if ( player == null ) {
            Saga.warning("Cannot remove null player");
            return;
        }

        SagaPlayer sagaPlayer = sagaPlayers.remove(player.getName());

        if ( sagaPlayer == null ) {
            Saga.warning("SagaPlayer does not exist for player!",player);
            return;
        }

        //Try to save player data
        try {
            sagaPlayer.save();
        } catch ( IOException e ) {
            Saga.exception("Exception while writing player " + player.getName() + " data to disk.",e);
        }

    }

    //This code handles commands
    public boolean handleCommand(Player player, String[] split, String command) {

        try {

            split[0] = split[0].substring(1);

            // Quick script shortcut
            if (split[0].matches("^[^/].*\\.js$")) {
                String[] newSplit = new String[split.length + 1];
                System.arraycopy(split, 0, newSplit, 1, split.length);
                newSplit[0] = "cs";
                newSplit[1] = newSplit[1];
                split = newSplit;
            }

            // No command found!
            if (!commandMap.hasCommand(split[0])) {
                return false;
            }

            try {
                commandMap.execute(split, player, this, wrapPlayer(player));
                String logString = "[Saga Command] " + player.getName() + ": " + command;
                Saga.info(logString);
            } catch (CommandPermissionsException e) {
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            } catch (MissingNestedCommandException e) {
                player.sendMessage(e.getUsage());
            } catch (CommandUsageException e) {
                player.sendMessage(e.getMessage());
                player.sendMessage(e.getUsage());
            } catch (WrappedCommandException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
                e.printStackTrace();
                throw e.getCause();
            } catch (UnhandledCommandException e) {
                player.sendMessage(ChatColor.RED + "Unhandled command exception");
                return false;
            } finally {

            }

        } catch (Throwable excp) {

            player.sendMessage("Problem handling command: " + command);
            player.sendMessage(excp.getMessage());
            excp.printStackTrace();
            return false;

        }

        return true;

    }

    /**
     * True, if player information loading is disabled.
     *
     * @return the playerInformationLoadingDisabled
     */
    public boolean isPlayerInformationLoadingDisabled() {
            return playerInformationLoadingDisabled;
    }

    /**
     * True, if player information saving is disabled.
     *
     * @return the playerInformationSavingDisabled
     */
    public boolean isPlayerInformationSavingDisabled() {
            return playerInformationSavingDisabled;
    }

    /**
     * Disables the loading and saving of player information.
     *
     */
    public void disablePlayerInformationSavingLoading() {

            if( playerInformationLoadingDisabled && playerInformationSavingDisabled ){
                    return;
            }

            playerInformationLoadingDisabled = true;
            playerInformationSavingDisabled = true;

            Saga.debug("Disabling player information saving and loading.");

    }

    //Debug/Log Output Functions
    static public void info(String string) {
        log.info(string);
    }

    static public void info(String string,Player player) {
        string = "(" + player.getName() + ")" + string;
        log.info(string);
    }

    static public void severe(String string) {
        log.severe(string);
    }

    static public void severe(String string,Player player) {
        string = "(" + player.getName() + ")" + string;
        log.severe(string);
    }

    static public void warning(String string) {
        log.warning(string);
    }

    static public void warning(String string,Player player) {
        string = "(" + player.getName() + ")" + string;
        log.warning(string);
    }

    static public void exception(String string, Exception e) {
        string = string + " [" + e.getClass().getSimpleName() + "]" + e.getMessage();
        log.severe(string);
    }

    static public void debug(String string) {

        if ( !debugging ) {
            return;
        }

        //TODO: Send to list of debuggers

        string = "[DEBUG] " + string;

        //Set to log
        log.info(string);

    }

    static public void debug(String string, Player player) {

        if ( !debugging ) {
            return;
        }


        string = "[DEBUG] " + string;

        //Set to log
        log.info(string);

    }

}
