/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import org.bukkit.event.player.*;

/**
 *
 * @author Cory
 */
public class SagaPlayerListener extends PlayerListener {

    protected Saga plugin;

    public SagaPlayerListener(Saga plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        String[] split = event.getMessage().split(" ");

        if ( plugin.handleCommand(event.getPlayer(), split, event.getMessage()) ) {
            event.setCancelled(true);
        }

    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {

        plugin.addPlayer(event.getPlayer());

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

        plugin.removePlayer(event.getPlayer());

    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {

    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {


    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {


    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

}
