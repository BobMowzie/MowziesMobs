package com.ilexiconn.llibrary.server.snackbar;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Snackbar {
    private String message;
    private int duration;
    private int color;
    private SnackbarPosition position;

    private Snackbar(String message) {
        this.message = message;
        this.duration = 0;
        this.color = 0xFF333333;
        this.position = SnackbarPosition.DOWN;
    }

    /**
     * Create a new snackbar instance. Every snackbar instance can be shown multiple times.
     *
     * @param message the message to display
     * @return the new snackbar instance
     */
    public static Snackbar create(String message) {
        return new Snackbar(message);
    }

    /**
     * @return this snackbar's message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return this snackbar's duration
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Set a custom duration for this snackbar. If it's 0 (default), the text length * 3 will be used.
     *
     * @param duration the custom duration
     * @return this snackbar instance
     */
    public Snackbar setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public SnackbarPosition getPosition() {
        return this.position;
    }

    public Snackbar setColor(int color) {
        this.color = color;
        return this;
    }

    public Snackbar setPosition(SnackbarPosition position) {
        this.position = position;
        return this;
    }
}
