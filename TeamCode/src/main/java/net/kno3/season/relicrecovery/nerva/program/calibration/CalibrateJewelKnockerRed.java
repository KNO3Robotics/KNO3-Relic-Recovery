package net.kno3.season.relicrecovery.nerva.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import net.kno3.robot.RobotSettings;
import net.kno3.season.relicrecovery.nerva.robot.Nerva;
import net.kno3.util.Threading;

/**
 * Created by robotics on 10/30/2017.
 */
@TeleOp(group = "calibration", name = "Calibrate Red Jewel Knocker")
public class CalibrateJewelKnockerRed extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();
        waitForStart();

        RobotSettings settings = new RobotSettings("Nerva");

        ServoDefaults.resetAllServos(hardwareMap, settings);

        while(opModeIsActive()) {
            Servo jewelArm = hardwareMap.servo.get(Nerva.JEWEL_KNOCKER_LEFT_KEY);
            double leftSetpoint  = settings.getDouble("jewel_knocker_red_left");
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    leftSetpoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    leftSetpoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                jewelArm.setPosition(leftSetpoint);
                telemetry.addData("Left Setpoint: ", leftSetpoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double rightSetPoint = settings.getDouble("jewel_knocker_red_right");
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    rightSetPoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    rightSetPoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                jewelArm.setPosition(rightSetPoint);
                telemetry.addData("Right Setpoint: ", rightSetPoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double zeroSetPoint = settings.getDouble("jewel_knocker_red_center");
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    zeroSetPoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    zeroSetPoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                jewelArm.setPosition(zeroSetPoint);
                telemetry.addData("Center Setpoint: ", zeroSetPoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double downSetPoint = settings.getDouble("jewel_knocker_red_down");
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    downSetPoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    downSetPoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                jewelArm.setPosition(downSetPoint);
                telemetry.addData("Down Setpoint: ", downSetPoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double idleSetPoint = settings.getDouble("jewel_knocker_red_idle");
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    idleSetPoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    idleSetPoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                jewelArm.setPosition(idleSetPoint);
                telemetry.addData("Idle Setpoint: ", idleSetPoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            double initSetPoint = settings.getDouble("jewel_knocker_red_init");
            while(opModeIsActive() && !gamepad1.a) {
                if(Math.abs(gamepad1.left_stick_x) > 0.05) {
                    initSetPoint += gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) / 3000;
                }
                if(Math.abs(gamepad1.right_stick_x) > 0.05) {
                    initSetPoint += gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x) / 15000;
                }
                jewelArm.setPosition(initSetPoint);
                telemetry.addData("Init Setpoint: ", initSetPoint);
                telemetry.addData("2", "Press A to continue");
                telemetry.update();
            }

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            telemetry.addData("3", "press B to test");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.b);

            jewelArm.setPosition(leftSetpoint);
            Threading.delay(4);
            jewelArm.setPosition(rightSetPoint);
            Threading.delay(4);
            jewelArm.setPosition(zeroSetPoint);
            Threading.delay(4);
            jewelArm.setPosition(downSetPoint);
            Threading.delay(4);
            jewelArm.setPosition(idleSetPoint);
            Threading.delay(4);
            jewelArm.setPosition(initSetPoint);
            Threading.delay(4);

            settings.setDouble("jewel_knocker_red_right", rightSetPoint);
            settings.setDouble("jewel_knocker_red_left", leftSetpoint);
            settings.setDouble("jewel_knocker_red_center", zeroSetPoint);
            settings.setDouble("jewel_knocker_red_down", downSetPoint);
            settings.setDouble("jewel_knocker_red_idle", idleSetPoint);
            settings.setDouble("jewel_knocker_red_init", initSetPoint);
            settings.save();

            telemetry.addData("5", "Saved! Press Y to try again.");
            telemetry.update();
            while(opModeIsActive() && !gamepad1.y);
        }
    }

}
