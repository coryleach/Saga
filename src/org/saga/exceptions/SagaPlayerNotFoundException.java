/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga.exceptions;

/**
 *
 * @author Cory
 */
public class SagaPlayerNotFoundException extends Exception {

    private String playerName;

    public SagaPlayerNotFoundException(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

}
