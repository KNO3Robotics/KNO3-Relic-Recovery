package net.kno3.util;

public interface IAdafruitIMU {
    float getHeading();
    void zeroHeading();
    float getPitch();
    void zeroPitch();
    float getRoll();
    void zeroRoll();
    void update();
}
