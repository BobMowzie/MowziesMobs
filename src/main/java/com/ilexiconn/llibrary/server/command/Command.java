package com.ilexiconn.llibrary.server.command;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.command.argument.Argument;
import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.ilexiconn.llibrary.server.command.argument.IArgumentParser;
import net.ilexiconn.llibrary.server.util.ListHashMap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Command extends CommandBase {
    private String name;
    private String commandUsage;
    private int permissionLevel = 4;
    private ListHashMap<String, IArgumentParser<?>> requiredArguments = new ListHashMap<>();
    private ListHashMap<String, IArgumentParser<?>> optionalArguments = new ListHashMap<>();
    private ICommandExecutor executor;

    private Command(String name) {
        this.name = name;
    }

    /**
     * Create a new command instance.
     *
     * @param name the command name
     * @return the new command instance
     */
    public static Command create(String name) {
        return new Command(name);
    }

    /**
     * Set the required permission level for this command.
     *
     * @param permissionLevel the required permission level
     * @return this command instance
     */
    public Command setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        return this;
    }

    /**
     * Add a required argument to this command. If it isn't filled in, the command won't be executed. Required arguments
     * have to be registered before optional ones.
     *
     * @param argument the argument name
     * @param type the type for this argument
     * @param <T> the argument type
     * @return this command instance
     */
    public <T> Command addRequiredArgument(String argument, Class<T> type) {
        if (!this.optionalArguments.isEmpty()) {
            LLibrary.LOGGER.error("Please register required arguments before optional ones! Skipping argument {} with type {}.", argument, type);
            return this;
        }
        IArgumentParser<T> argumentParser = CommandHandler.INSTANCE.getParserForType(type);
        if (argumentParser != null) {
            this.requiredArguments.put(argument, argumentParser);
        } else {
            LLibrary.LOGGER.error("Unable to find argument parser for type {}. Skipping argument.", type);
        }
        return this;
    }

    /**
     * Add an optional argument to this command. If it isn't set, the command will be executed.
     *
     * @param argument the argument name
     * @param type the type for this argument
     * @param <T> the argument type
     * @return this command instance
     */
    public <T> Command addOptionalArgument(String argument, Class<T> type) {
        IArgumentParser<T> argumentParser = CommandHandler.INSTANCE.getParserForType(type);
        if (argumentParser != null) {
            this.optionalArguments.put(argument, argumentParser);
        } else {
            LLibrary.LOGGER.error("Unable to find argument parser for type {}. Skipping argument.", type);
        }
        return this;
    }

    Command setExecutor(ICommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return this.permissionLevel;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        if (this.commandUsage == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("/");
            builder.append(this.getName());
            for (String requiredArgument : this.requiredArguments.keySet()) {
                builder.append(" ");
                builder.append("<");
                builder.append(requiredArgument);
                builder.append(">");
            }
            for (String optionalArgument : this.optionalArguments.keySet()) {
                builder.append(" ");
                builder.append("[");
                builder.append(optionalArgument);
                builder.append("]");
            }
            this.commandUsage = builder.toString();
            return this.commandUsage;
        } else {
            return this.commandUsage;
        }
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < this.requiredArguments.size()) {
            throw new WrongUsageException(this.getUsage(sender));
        } else if (args.length > this.requiredArguments.size() + this.optionalArguments.size()) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            List<Argument<?>> arguments = Lists.newArrayList();
            for (int i = 0; i < args.length; i++) {
                if (i < this.requiredArguments.size()) {
                    Map.Entry<String, IArgumentParser<?>> entry = this.requiredArguments.getEntry(i);
                    try {
                        arguments.add(new Argument<>(entry.getKey(), entry.getValue().parseArgument(server, sender, args[i])));
                    } catch (CommandException e) {
                        sender.sendMessage(new TextComponentString(e.getLocalizedMessage()).setStyle(new Style().setColor(TextFormatting.RED)));
                    }
                } else {
                    Map.Entry<String, IArgumentParser<?>> entry = this.optionalArguments.getEntry(i - this.requiredArguments.size());
                    try {
                        arguments.add(new Argument<>(entry.getKey(), entry.getValue().parseArgument(server, sender, args[i])));
                    } catch (CommandException e) {
                        sender.sendMessage(new TextComponentString(e.getLocalizedMessage()).setStyle(new Style().setColor(TextFormatting.RED)));
                    }
                }
            }
            this.executor.execute(server, sender, new CommandArguments(arguments));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length <= this.requiredArguments.size()) {
            return this.requiredArguments.getValue(args.length - 1).getTabCompletion(server, sender, args, pos);
        } else if (args.length <= this.requiredArguments.size() + this.optionalArguments.size()) {
            return this.optionalArguments.getValue(args.length - this.requiredArguments.size() - 1).getTabCompletion(server, sender, args, pos);
        } else {
            return Collections.emptyList();
        }
    }
}
