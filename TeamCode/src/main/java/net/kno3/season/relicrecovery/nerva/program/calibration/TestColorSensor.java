package net.kno3.season.relicrecovery.nerva.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import net.kno3.util.Color;

/**
 * Created by robotics on 11/9/2017.
 */

@TeleOp(group = "calibration", name = "test color sensor")
public class TestColorSensor extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensor left = hardwareMap.colorSensor.get("Left Jewel Color");
        ColorSensor right = hardwareMap.colorSensor.get("Right Jewel Color");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("1", "Left Jewel");
            telemetry.addData("Red", left.red());
            telemetry.addData("Blue", left.blue());
            telemetry.addData("2", "Right Jewel");
            telemetry.addData("Red", right.red());
            telemetry.addData("Blue", right.blue());
            telemetry.update();
        }

    }
}
