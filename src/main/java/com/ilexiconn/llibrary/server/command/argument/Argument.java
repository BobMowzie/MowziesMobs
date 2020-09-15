package com.ilexiconn.llibrary.server.command.argument;

/**
 * @param <T> the argument type
 * @author iLexiconn
 * @since 1.0.0
 */
public class Argument<T> {
    private String name;
    private T value;

    public Argument(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }
}
