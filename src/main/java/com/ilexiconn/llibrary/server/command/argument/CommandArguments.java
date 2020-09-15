package com.ilexiconn.llibrary.server.command.argument;

import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class CommandArguments {
    private List<Argument<?>> arguments;

    public CommandArguments(List<Argument<?>> arguments) {
        this.arguments = arguments;
    }

    /**
     * @param name the argument name to check
     * @return true if the user filled in this argument
     */
    public boolean hasArgument(String name) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the argument with the class type, and parse it using the registered parser.
     *
     * @param name the argument name
     * @param <T>  the argument type
     * @return the argument value, null if it can't be found
     */
    public <T> T getArgument(String name) {
        for (Argument<?> argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return (T) argument.getValue();
            }
        }
        return null;
    }
}
