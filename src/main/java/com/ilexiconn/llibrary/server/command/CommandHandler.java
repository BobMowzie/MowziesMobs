package com.ilexiconn.llibrary.server.command;

import com.ilexiconn.llibrary.server.command.argument.ArgumentParsers;
import com.ilexiconn.llibrary.server.command.argument.IArgumentParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum CommandHandler {
    INSTANCE;

    private Map<Class<?>, IArgumentParser<?>> argumentParserMap = new HashMap<>();

    /**
     * Register an argument parser.
     *
     * @param type           the argument type
     * @param argumentParser the argument parser
     * @param <T>            the argument type
     */
    public <T> void registerArgumentParser(Class<T> type, IArgumentParser<T> argumentParser) {
        this.argumentParserMap.put(type, argumentParser);
    }

    /**
     * Get an argument parser for the specific enum.
     *
     * @param enumClass the enum class
     * @param <T>       the enum type
     * @return the enum parser
     */
    public <T extends Enum<T>> IArgumentParser<T> getEnumParser(Class<T> enumClass) {
        return new IArgumentParser<T>() {
            private String values[] = Arrays.stream(enumClass.getEnumConstants()).map(T::name).map(s -> s.toLowerCase(Locale.ENGLISH)).toArray(String[]::new);

            @Override
            public T parseArgument(MinecraftServer server, ICommandSender sender, String argument) {
                return Enum.valueOf(enumClass, argument.toUpperCase(Locale.ENGLISH));
            }

            @Override
            public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
                return CommandBase.getListOfStringsMatchingLastWord(args, this.values);
            }
        };
    }

    /**
     * Get the argument parser for a specific type. Returns null if none can be found.
     *
     * @param type the argument type
     * @param <T>  the argument type
     * @return the argument parser, null if it can't be found
     */
    public <T> IArgumentParser<T> getParserForType(Class<T> type) {
        IArgumentParser<T> argumentParser = ArgumentParsers.getBuiltinParser(type);
        if (argumentParser != null) {
            return argumentParser;
        } else if (this.argumentParserMap.containsKey(type)) {
            return (IArgumentParser<T>) this.argumentParserMap.get(type);
        } else {
            return null;
        }
    }

    /**
     * Registers the given command, must be called from FMLServerStartingEvent
     *
     * @param event    the FMLServerStartingEvent
     * @param command  the command to register
     * @param executor a CommandExecutor to execute this command
     */
    public void registerCommand(FMLServerStartingEvent event, Command command, ICommandExecutor executor) {
        event.registerServerCommand(command.setExecutor(executor));
    }
}
