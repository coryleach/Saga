/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import java.util.logging.*;

//imports from this project
import org.sk89q.*;

//External Imports
import java.util.*;
import org.bukkit.*;
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
    private HashMap<String,SagaPlayer> sagaPlayers;

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

        PluginManager pm = getServer().getPluginManager();
        Plugin test = null;

        //Test for specific plugins
        test = pm.getPlugin("GroupManager");
        if ( test != null ) {
            log.info("Legends found Group Manager plugin!");
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

        try {

            // Register events
            //pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);


    	} catch ( Exception e ) {

            Saga.severe("Exception Registering Events: ");
            Saga.severe(e.getMessage());
            e.printStackTrace();

        }

        try {

            //Register Command Classes to the command map
            //commandMap.register(SagaCommands.class);

    	} catch ( Exception e ) {

            Saga.severe("Exception Registering Command Classes: ");
            Saga.severe(e.getMessage());
            e.printStackTrace();

    	}

        // TODO: Place any custom enable code here including the registration of any events

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

        //Second try, attempt to load data from file
        /*if ( playerDataExists(player) ) {

            if ( !loadPlayerData(player) ) {
                log.severe("Failed to load player data!");
            }

            wrappedPlayer = playerMap.get(player.getName());

            if ( wrappedPlayer != null ) {
                wrappedPlayer.setHandle(wrappedPlayer.getHandle());
                return wrappedPlayer;
            }

        }*/

        //Finally just make a new wrap around player
        wrappedPlayer = new SagaPlayer(player);
        sagaPlayers.put(player.getName(), wrappedPlayer);

        return wrappedPlayer;

    }

    public void removePlayer(Player player) {

        if ( player == null ) {
            Saga.warning("Cannot remove null player");
            return;
        }

        SagaPlayer sagaPlayer = sagaPlayers.remove(player.getName());

        //TODO: Save Player Info Here
        /*if ( !SagaPlayer.save() ) {
            Saga.severe("Failed to save player legends data!");
        }*/

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

    //Debug/Log Output Functions
    static public void info(String string) {
        log.info(string);
    }

    static public void severe(String string) {
        log.severe(string);
    }

    static public void warning(String string) {
        log.warning(string);
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


}
