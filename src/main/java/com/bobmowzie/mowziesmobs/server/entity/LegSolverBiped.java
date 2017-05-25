package com.bobmowzie.mowziesmobs.server.entity;

/**
 * Created by Josh on 5/15/2017.
 */
public final class LegSolverBiped extends LegSolver {
    public final Leg left, right;

    public LegSolverBiped(float forward, float side) {
        super(new Leg(forward, side), new Leg(forward, -side));
        this.left = this.legs[0];
        this.right = this.legs[1];
    }
}
