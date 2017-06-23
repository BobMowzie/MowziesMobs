package com.bobmowzie.mowziesmobs.server.property;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.property.power.Power;
import com.bobmowzie.mowziesmobs.server.property.power.PowerGeomancy;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class MowziePlayerProperties extends EntityProperties<EntityPlayer> {
    public static final int SWING_HIT_TICK = 10;
    private static final int SWING_LENGTH = 20;
    @NBTProperty
    public int untilSunstrike = 0;
    @NBTProperty
    private int prevTime;
    @NBTProperty
    private int time;
    public boolean mouseRightDown = false;
    public boolean mouseLeftDown = false;

    public int tribeCircleTick;
    public List<EntityBarakoanToPlayer> tribePack = new ArrayList<>();
    public int tribePackRadius = 3;

    public EntityIceBreath icebreath;
    public boolean usingIceBreath;

    public PowerGeomancy geomancy = new PowerGeomancy(this);
    public Power[] powers = new Power[]{geomancy};

    public static float fnc1(float x) {
        return x * ((45 - 27 * x) * x - 18);
    }

    public static float fnc2(float x) {
        return MathHelper.sin(x * (float) Math.PI);
    }

    public static float fnc3(float x, float incline, float decline, float steepness) {
        return (float) (1 / (1 + Math.exp(-steepness * (x - incline))) - (1 / (1 + Math.exp(-steepness * (x - decline)))));
    }

    @Override
    public void init() {

    }

    @Override
    public String getID() {
        return "mm:player";
    }

    @Override
    public Class<EntityPlayer> getEntityClass() {
        return EntityPlayer.class;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.saveNBTData(this, compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.loadNBTData(this, compound);
    }

    public void update() {
        prevTime = time;
    }

    public void swing() {
        time = SWING_LENGTH;
    }

    public int getTick() {
        return time;
    }

    public float getSwingPercentage(float partialRenderTicks) {
        return (prevTime + (time - prevTime) * partialRenderTicks) / SWING_LENGTH;
    }

    public void decrementTime() {
        time--;
    }

    @Override
    public int getTrackingTime() {
        return 0;
    }

    public int getPackSize() {
        return tribePack.size();
    }

    public void removePackMember(EntityBarakoanToPlayer tribePlayer) {
        tribePack.remove(tribePlayer);
        //sortPackMembers();
    }

    public void addPackMember(EntityBarakoanToPlayer tribePlayer) {
        tribePack.add(tribePlayer);
        //sortPackMembers();
    }

    private void sortPackMembers() {
        double theta = 2 * Math.PI / tribePack.size();
        for (int i = 0; i < tribePack.size(); i++) {
            int nearestIndex = -1;
            double smallestDiffSq = Double.MAX_VALUE;
            double targetTheta = theta * i;
            double x = getEntity().posX + tribePackRadius * Math.cos(targetTheta);
            double z = getEntity().posZ + tribePackRadius * Math.sin(targetTheta);
            for (int n = 0; n < tribePack.size(); n++) {
                EntityBarakoanToPlayer tribePlayer = tribePack.get(n);
                double diffSq = (x - tribePlayer.posX) * (x - tribePlayer.posX) + (z - tribePlayer.posZ) * (z - tribePlayer.posZ);
                if (diffSq < smallestDiffSq) {
                    smallestDiffSq = diffSq;
                    nearestIndex = n;
                }
            }
            if (nearestIndex == -1) {
                throw new ArithmeticException("All pack members have NaN x and z?");
            }
            tribePack.add(i, tribePack.remove(nearestIndex));
        }
    }
}
