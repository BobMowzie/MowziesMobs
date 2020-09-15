package com.ilexiconn.llibrary.server.command;

import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@FunctionalInterface
public interface ICommandExecutor {
    /**
     * Called when a command with the correct arguments gets executed.
     *
     * @param server    the server instance
     * @param sender    the command sender
     * @param arguments the arguments used
     * @throws CommandException if something goes wrong
     */
    void execute(MinecraftServer server, ICommandSender sender, CommandArguments arguments) throws CommandException;
}
