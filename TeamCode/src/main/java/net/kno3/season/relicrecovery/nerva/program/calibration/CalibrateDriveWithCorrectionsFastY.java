package net.kno3.season.relicrecovery.nerva.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import net.kno3.robot.Robot;
import net.kno3.robot.RobotSettings;
import net.kno3.season.relicrecovery.nerva.robot.NervaDriveSystem;
import net.kno3.util.ValuesAdjuster;

import org.firstinspires.ftc.robotcontroller.internal.FtcOpModeRegister;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

/**
 * Created by robotics on 10/29/2017.
 */
@TeleOp(group = "calibration", name = "Calibrate Drive With Corrections Fast Y")
public class CalibrateDriveWithCorrectionsFastY extends LinearOpMode {
    public double kDriveWithCorrectionsFast_maxSpeed,
            kDriveWithCorrectionsFast_p,
            kDriveWithCorrectionsFast_i,
            kDriveWithCorrectionsFast_d,
            kDriveWithCorrectionsFast_maxTurn,
            kDriveWithCorrectionsFast_kp,
            kDriveWithCorrectionsFast_ki,
            kDriveWithCorrectionsFast_kd,
            kDriveWithCorrectionsFast_tolerance;
    public double testDistance = 24;

    @Override
    public void runOpMode() throws InterruptedException {
        FtcOpModeRegister.opModeManager = (OpModeManagerImpl) internalOpModeServices;

        Robot robot = new Robot(this);
        NervaDriveSystem drive = new NervaDriveSystem(robot);
        drive.init();
        drive.modeSpeed();
        telemetry.addData("1", "Press start when imu is ready");
        telemetry.update();
        waitForStart();

        RobotSettings settings = new RobotSettings("Nerva");
        ServoDefaults.resetAllServos(hardwareMap, settings);

        kDriveWithCorrectionsFast_maxSpeed = settings.getDouble("kDriveWithCorrectionsFast_maxSpeed");
        kDriveWithCorrectionsFast_p = settings.getDouble("kDriveWithCorrectionsFast_p");
        kDriveWithCorrectionsFast_i = settings.getDouble("kDriveWithCorrectionsFast_i");
        kDriveWithCorrectionsFast_d = settings.getDouble("kDriveWithCorrectionsFast_d");
        kDriveWithCorrectionsFast_maxTurn = settings.getDouble("kDriveWithCorrectionsFast_maxTurn");
        kDriveWithCorrectionsFast_kp = settings.getDouble("kDriveWithCorrectionsFast_kp");
        kDriveWithCorrectionsFast_ki = settings.getDouble("kDriveWithCorrectionsFast_ki");
        kDriveWithCorrectionsFast_kd = settings.getDouble("kDriveWithCorrectionsFast_kd");
        kDriveWithCorrectionsFast_tolerance = settings.getDouble("kDriveWithCorrectionsFast_tolerance");

        ValuesAdjuster adjuster = new ValuesAdjuster(this, telemetry);
        adjuster.addValue("kDriveWithCorrectionsFast_maxSpeed", "Max Speed", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_p", "Drive P", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_i", "Drive I", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_d", "Drive D", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_maxTurn", "Max Turn", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_kp", "turn P", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_ki", "turn I", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_kd", "turn D", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFast_tolerance", "turn tolerance", 0, 10000);
        adjuster.addValue("testDistance", "Testing Distance (inches)", -10000, 10000);

        while (opModeIsActive()) {
            while (opModeIsActive() && !gamepad1.a) {
                telemetry.addData("2", "Press A to test");
                telemetry.addData("3", "Press Start and back to change the increment");
                telemetry.addData("4", "Press dpad left and right to changed adjusted value");
                telemetry.addData("5", "Press the bumpers to adjust the value");
                adjuster.update(gamepad1);
                telemetry.update();
            }
            drive.kDriveWithCorrectionsFast_maxSpeed = kDriveWithCorrectionsFast_maxSpeed;
            drive.kDriveWithCorrectionsFast_p = kDriveWithCorrectionsFast_p;
            drive.kDriveWithCorrectionsFast_i = kDriveWithCorrectionsFast_i;
            drive.kDriveWithCorrectionsFast_d = kDriveWithCorrectionsFast_d;
            drive.kDriveWithCorrectionsFast_maxTurn = kDriveWithCorrectionsFast_maxTurn;
            drive.kDriveWithCorrectionsFast_kp = kDriveWithCorrectionsFast_kp;
            drive.kDriveWithCorrectionsFast_ki = kDriveWithCorrectionsFast_ki;
            drive.kDriveWithCorrectionsFast_kd = kDriveWithCorrectionsFast_kd;
            drive.kDriveWithCorrectionsFast_tolerance = kDriveWithCorrectionsFast_tolerance;

            Thread.sleep(1000);

            drive.driveWithCorrectionFast(testDistance, 0);
            drive.stop();

            while (opModeIsActive()) {
                telemetry.addData("6", "Press Y to save and exit");
                telemetry.addData("7", "Press B to try again");
                telemetry.update();
                if(gamepad1.y) {
                    settings.setDouble("kDriveWithCorrectionsFast_maxSpeed", kDriveWithCorrectionsFast_maxSpeed);
                    settings.setDouble("kDriveWithCorrectionsFast_p", kDriveWithCorrectionsFast_p);
                    settings.setDouble("kDriveWithCorrectionsFast_i", kDriveWithCorrectionsFast_i);
                    settings.setDouble("kDriveWithCorrectionsFast_d", kDriveWithCorrectionsFast_d);
                    settings.setDouble("kDriveWithCorrectionsFast_maxTurn", kDriveWithCorrectionsFast_maxTurn);
                    settings.setDouble("kDriveWithCorrectionsFast_kp", kDriveWithCorrectionsFast_kp);
                    settings.setDouble("kDriveWithCorrectionsFast_ki", kDriveWithCorrectionsFast_ki);
                    settings.setDouble("kDriveWithCorrectionsFast_kd", kDriveWithCorrectionsFast_kd);
                    settings.setDouble("kDriveWithCorrectionsFast_tolerance", kDriveWithCorrectionsFast_tolerance);
                    settings.save();
                    return;
                }
                if(gamepad1.b) {
                    break;
                }
            }
        }
    }
}
