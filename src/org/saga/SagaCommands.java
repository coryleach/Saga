/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.saga;

import org.sk89q.*;

/**
 *
 * @author Cory
 */
public class SagaCommands {

    @Command(
        aliases = {"saga","sagaCommandAlias"},
        usage = "",
        flags = "",
        desc = "TODO: ",
        min = 0,
        max = 0
    )
    @CommandPermissions({"saga.common.admin"})
    public static void saga(CommandContext args, Saga plugin, SagaPlayer player) {


    }

    @Command(
        aliases = {"sagaAdmin","sagaAdminCommandAlias"},
        usage = "",
        flags = "",
        desc = "TODO: ",
        min = 0,
        max = 0
    )
    @CommandPermissions({"saga.admin.admin"})
    public static void admin(CommandContext args, Saga plugin, SagaPlayer player) {


    }

}
