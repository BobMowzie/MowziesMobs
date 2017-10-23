package com.bobmowzie.mowziesmobs.server.entity;

/**
 * Created by pau101
 */
public final class LegSolverQuadruped extends LegSolver {
	public final Leg backLeft, backRight, frontLeft, frontRight;

	public LegSolverQuadruped(float frontZOffset, float frontXOffset, float backZOffset, float backXOffset) {
		super(
				new Leg(backZOffset, backXOffset),
				new Leg(backZOffset, -backXOffset),
				new Leg(frontZOffset, frontXOffset),
				new Leg(frontZOffset, -frontXOffset)
		);
		this.backLeft = this.legs[0];
		this.backRight = this.legs[1];
		this.frontLeft = this.legs[2];
		this.frontRight = this.legs[3];
	}
}