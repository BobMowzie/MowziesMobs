package com.ilexiconn.llibrary.server.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The functional interfaces from Java are disallowing to throw exceptions. We have to wrap them ourselves...
 *
 * @author WorldSEnder
 */
public class ExceptionlessFunctions {
    public interface ThrowingRunnable<E extends Throwable> {
        void accept() throws E;
    }

    public interface ThrowingConsumer<T, E extends Throwable> {
        void accept(T t) throws E;
    }

    public interface ThrowingSupplier<T, E extends Throwable> {
        T get() throws E;
    }

    public interface ThrowingFunction<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    public static <E extends Throwable> Runnable uncheckedRunnable(ThrowingRunnable<E> t) throws E {
        return () -> {
            try {
                t.accept();
            } catch (Throwable exception) {
                ExceptionlessFunctions.throwActualException(exception);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> uncheckedConsumer(ThrowingConsumer<T, E> consumer) throws E {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Throwable exception) {
                ExceptionlessFunctions.throwActualException(exception);
            }
        };
    }

    public static <T, E extends Exception> Supplier<T> uncheckedSupplier(ThrowingSupplier<T, E> supplier) throws E {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable exception) {
                return ExceptionlessFunctions.throwActualException(exception);
            }
        };
    }

    public static <T, R, E extends Exception> Function<T, R> uncheckedFunction(ThrowingFunction<T, R, E> function) throws E {
        return t -> {
            try {
                return function.apply(t);
            } catch (Throwable exception) {
                return ExceptionlessFunctions.throwActualException(exception);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception, T> T throwActualException(Throwable exception) throws E {
        throw (E) exception;
    }
}
