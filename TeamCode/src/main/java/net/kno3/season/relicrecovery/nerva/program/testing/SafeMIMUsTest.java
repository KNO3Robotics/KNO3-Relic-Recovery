package net.kno3.season.relicrecovery.nerva.program.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import net.kno3.util.AdafruitIMU;
import net.kno3.util.IAdafruitIMU;
import net.kno3.util.MultiIMU;
import net.kno3.util.SafeAdafruitIMU;

/**
 * Created by robotics on 3/3/2018.
 */
@TeleOp(name = "SafeIMUs Test Multi")
public class SafeMIMUsTest extends OpMode {
    IAdafruitIMU imu;
    IAdafruitIMU backimu;
    IAdafruitIMU sideimu;

    IAdafruitIMU multiIMU;

    @Override
    public void init() {
        imu = new SafeAdafruitIMU(hardwareMap, "imu", true);
        telemetry.addData("IMU Ready", true);
        telemetry.update();

        backimu = new SafeAdafruitIMU(hardwareMap, "imuback", true);
        telemetry.addData("IMU Ready", true);
        telemetry.addData("IMU back Ready", true);
        telemetry.update();

        sideimu = new SafeAdafruitIMU(hardwareMap, "imuside", true);
        telemetry.addData("IMU Ready", true);
        telemetry.addData("IMU back Ready", true);
        telemetry.addData("IMU side Ready", true);
        telemetry.update();

        multiIMU = new MultiIMU(imu, backimu, sideimu);
    }

    private long last = System.currentTimeMillis();

    @Override
    public void loop() {
        multiIMU.update();

        telemetry.addData("IMU heading", imu.getHeading());
        telemetry.addData("IMU pitch", imu.getPitch());
        telemetry.addData("IMU roll", imu.getRoll());
        telemetry.addData("IMU back heading", backimu.getHeading());
        telemetry.addData("IMU back pitch", backimu.getPitch());
        telemetry.addData("IMU back roll", backimu.getRoll());
        telemetry.addData("IMU side heading", sideimu.getHeading());
        telemetry.addData("IMU side pitch", sideimu.getPitch());
        telemetry.addData("IMU side roll", sideimu.getRoll());
        telemetry.addData("Multi heading", multiIMU.getHeading());
        telemetry.addData("Multi pitch", multiIMU.getPitch());
        telemetry.addData("Multi roll", multiIMU.getRoll());

        telemetry.addData("Time", System.currentTimeMillis() - last);
        last = System.currentTimeMillis();

        telemetry.update();
    }
}
