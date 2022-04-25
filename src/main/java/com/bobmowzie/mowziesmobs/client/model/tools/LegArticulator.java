package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.server.entity.LegSolverBiped;
import com.bobmowzie.mowziesmobs.server.entity.LegSolverQuadruped;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.util.Mth;

public final class LegArticulator {
    private LegArticulator() {}

    public static void articulateBiped(MowzieEntity entity, LegSolverBiped legs, AdvancedModelRenderer body, AdvancedModelRenderer leftThigh, AdvancedModelRenderer leftCalf, AdvancedModelRenderer rightThigh, AdvancedModelRenderer rightCalf, float rotThigh, float rotCalf, float delta) {
        float heightLeft = legs.left.getHeight(delta);
        float heightRight = legs.right.getHeight(delta);
        if (heightLeft > 0 || heightRight > 0) {
            float sc = LegArticulator.getScale(entity);
            float avg = LegArticulator.avg(heightLeft, heightRight);
            body.y += 16 / sc * avg;
            articulateLegPair(sc, heightLeft, heightRight, avg, 0, leftThigh, leftCalf, rightThigh, rightCalf, rotThigh, rotCalf);
        }
    }

    // front legs must be connected to body
    public static void articulateQuadruped(
            MowzieEntity entity, LegSolverQuadruped legs, AdvancedModelRenderer body, AdvancedModelRenderer neck,
            AdvancedModelRenderer backLeftThigh, AdvancedModelRenderer backLeftCalf,
            AdvancedModelRenderer backRightThigh, AdvancedModelRenderer backRightCalf,
            AdvancedModelRenderer frontLeftThigh, AdvancedModelRenderer frontLeftCalf,
            AdvancedModelRenderer frontRightThigh, AdvancedModelRenderer frontRightCalf,
            float rotBackThigh, float rotBackCalf,
            float rotFrontThigh, float rotFrontCalf,
            float delta)
    {
        float heightBackLeft = legs.backLeft.getHeight(delta);
        float heightBackRight = legs.backRight.getHeight(delta);
        float heightFrontLeft = legs.frontLeft.getHeight(delta);
        float heightFrontRight = legs.frontRight.getHeight(delta);
        if (heightBackLeft > 0 || heightBackRight > 0 || heightFrontLeft > 0 || heightFrontRight > 0) {
            float sc = LegArticulator.getScale(entity);
            float backAvg = LegArticulator.avg(heightBackLeft, heightBackRight);
            float frontAvg = LegArticulator.avg(heightFrontLeft, heightFrontRight);
            float bodyLength = Math.abs(avg(legs.backLeft.forward, legs.backRight.forward) - avg(legs.frontLeft.forward, legs.frontRight.forward));
            float tilt = (float) (Mth.atan2(bodyLength, backAvg - frontAvg) - Math.PI / 2);
            body.y += 16 / sc * backAvg;
            body.xRot += tilt;
            frontLeftThigh.xRot -= tilt;
            frontRightThigh.xRot -= tilt;
            neck.xRot -= tilt;
            LegArticulator.articulateLegPair(sc, heightBackLeft, heightBackRight, backAvg, -backAvg, backLeftThigh, backLeftCalf, backRightThigh, backRightCalf, rotBackThigh, rotBackCalf);
            LegArticulator.articulateLegPair(sc, heightFrontLeft, heightFrontRight, frontAvg, -frontAvg, frontLeftThigh, frontLeftCalf, frontRightThigh, frontRightCalf, rotFrontThigh, rotFrontCalf);
        }
    }

    private static void articulateLegPair(float sc, float heightLeft, float heightRight, float avg, float offsetY, AdvancedModelRenderer leftThigh, AdvancedModelRenderer leftCalf, AdvancedModelRenderer rightThigh, AdvancedModelRenderer rightCalf, float rotThigh, float rotCalf) {
        float difLeft = Math.max(0, heightRight - heightLeft);
        float difRight = Math.max(0, heightLeft - heightRight);
        leftThigh.y += 16 / sc * (Math.max(heightLeft, avg) + offsetY);
        rightThigh.y += 16 / sc * (Math.max(heightRight, avg) + offsetY);
        leftThigh.xRot -= rotThigh * difLeft;
        leftCalf.xRot += rotCalf * difLeft;
        rightThigh.xRot -= rotThigh * difRight;
        rightCalf.xRot += rotCalf * difRight;
    }

    private static float avg(float a, float b) {
        return (a + b) / 2;
    }

    private static float getScale(MowzieEntity entity) {
        return 1;
    }
}