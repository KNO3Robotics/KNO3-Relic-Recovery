package net.kno3.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by robotics on 3/3/2018.
 */

public class MultiIMU implements IAdafruitIMU {
    private List<IAdafruitIMU> imus;
    private long lastCheck = 0;

    public MultiIMU(IAdafruitIMU... imus) {
        this.imus = Lists.newArrayList(imus);
    }

    @Override
    public float getHeading() {
        /*float headingSum = 0;
        for(IAdafruitIMU imu : imus) headingSum += imu.getHeading();
        return headingSum / imus.size();*/
        return imus.size() == 0 ? 0 : imus.get(0).getHeading();
    }

    @Override
    public void zeroHeading() {
        for(IAdafruitIMU imu : imus) imu.zeroHeading();
    }

    @Override
    public float getPitch() {
        float pitchSum = 0;
        for(IAdafruitIMU imu : imus) pitchSum += imu.getPitch();
        return pitchSum / imus.size();
    }

    @Override
    public void zeroPitch() {
        for(IAdafruitIMU imu : imus) imu.zeroPitch();
    }

    @Override
    public float getRoll() {
        float rollSum = 0;
        for(IAdafruitIMU imu : imus) rollSum += imu.getRoll();
        return rollSum / imus.size();
    }

    @Override
    public void zeroRoll() {
        for(IAdafruitIMU imu : imus) imu.zeroRoll();
    }

    public void update() {
        for(IAdafruitIMU imu : imus) imu.update();

        boolean isOneOutsideOfRange = false;
        for(IAdafruitIMU imu : imus) if(imu.getHeading() > 5 && imu.getHeading() < 355) isOneOutsideOfRange = true;

        if(isOneOutsideOfRange)
            for (int i = 0; i < imus.size(); i++)
                if(imus.get(i).getHeading() == 0) imus.remove(i--);
    }
}
