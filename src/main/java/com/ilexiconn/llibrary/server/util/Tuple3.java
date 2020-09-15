package com.ilexiconn.llibrary.server.util;

/**
 * @param <A> the type of the first object
 * @param <B> the type of the second object
 * @param <C> the type of the third object
 * @author iLexiconn
 * @since 1.0.0
 */
public class Tuple3<A, B, C> {
    private A a;
    private B b;
    private C c;

    public Tuple3() {
        this(null, null, null);
    }

    public Tuple3(A a, B b, C c) {
        this.set(a, b, c);
    }

    /**
     * @return the first object in this tuple
     */
    public A getA() {
        return this.a;
    }

    /**
     * Set the first object in this tuple.
     *
     * @param a the new object
     */
    public void setA(A a) {
        this.a = a;
    }

    /**
     * @return the second object in this tuple
     */
    public B getB() {
        return this.b;
    }

    /**
     * Set the second object in this tuple.
     *
     * @param b the new object
     */
    public void setB(B b) {
        this.b = b;
    }

    /**
     * @return the third object in this tuple
     */
    public C getC() {
        return this.c;
    }

    /**
     * Set the third object in this tuple.
     *
     * @param c the new object
     */
    public void setC(C c) {
        this.c = c;
    }

    /**
     * Set all three tuple values at once.
     *
     * @param a the new object
     * @param b the new object
     * @param c the new object
     */
    public void set(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}

