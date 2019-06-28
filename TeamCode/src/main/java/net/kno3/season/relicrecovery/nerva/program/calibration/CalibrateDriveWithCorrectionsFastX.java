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
@TeleOp(group = "calibration", name = "Calibrate Drive With Corrections Fast X")
public class CalibrateDriveWithCorrectionsFastX extends LinearOpMode {
    public double kDriveWithCorrectionsFastX_maxSpeed,
            kDriveWithCorrectionsFastX_p,
            kDriveWithCorrectionsFastX_i,
            kDriveWithCorrectionsFastX_d,
            kDriveWithCorrectionsFastX_maxTurn,
            kDriveWithCorrectionsFastX_kp,
            kDriveWithCorrectionsFastX_ki,
            kDriveWithCorrectionsFastX_kd,
            kDriveWithCorrectionsFastX_tolerance;
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

        kDriveWithCorrectionsFastX_maxSpeed = settings.getDouble("kDriveWithCorrectionsFastX_maxSpeed");
        kDriveWithCorrectionsFastX_p = settings.getDouble("kDriveWithCorrectionsFastX_p");
        kDriveWithCorrectionsFastX_i = settings.getDouble("kDriveWithCorrectionsFastX_i");
        kDriveWithCorrectionsFastX_d = settings.getDouble("kDriveWithCorrectionsFastX_d");
        kDriveWithCorrectionsFastX_maxTurn = settings.getDouble("kDriveWithCorrectionsFastX_maxTurn");
        kDriveWithCorrectionsFastX_kp = settings.getDouble("kDriveWithCorrectionsFastX_kp");
        kDriveWithCorrectionsFastX_ki = settings.getDouble("kDriveWithCorrectionsFastX_ki");
        kDriveWithCorrectionsFastX_kd = settings.getDouble("kDriveWithCorrectionsFastX_kd");
        kDriveWithCorrectionsFastX_tolerance = settings.getDouble("kDriveWithCorrectionsFastX_tolerance");

        ValuesAdjuster adjuster = new ValuesAdjuster(this, telemetry);
        adjuster.addValue("kDriveWithCorrectionsFastX_maxSpeed", "Max Speed", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_p", "Drive P", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_i", "Drive I", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_d", "Drive D", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_maxTurn", "Max Turn", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_kp", "turn P", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_ki", "turn I", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_kd", "turn D", 0, 10000);
        adjuster.addValue("kDriveWithCorrectionsFastX_tolerance", "turn tolerance", 0, 10000);
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
            drive.kDriveWithCorrectionsFastX_maxSpeed = kDriveWithCorrectionsFastX_maxSpeed;
            drive.kDriveWithCorrectionsFastX_p = kDriveWithCorrectionsFastX_p;
            drive.kDriveWithCorrectionsFastX_i = kDriveWithCorrectionsFastX_i;
            drive.kDriveWithCorrectionsFastX_d = kDriveWithCorrectionsFastX_d;
            drive.kDriveWithCorrectionsFastX_maxTurn = kDriveWithCorrectionsFastX_maxTurn;
            drive.kDriveWithCorrectionsFastX_kp = kDriveWithCorrectionsFastX_kp;
            drive.kDriveWithCorrectionsFastX_ki = kDriveWithCorrectionsFastX_ki;
            drive.kDriveWithCorrectionsFastX_kd = kDriveWithCorrectionsFastX_kd;
            drive.kDriveWithCorrectionsFastX_tolerance = kDriveWithCorrectionsFastX_tolerance;

            Thread.sleep(1000);

            drive.driveWithCorrectionFastX(testDistance, 0);
            drive.stop();

            while (opModeIsActive()) {
                telemetry.addData("6", "Press Y to save and exit");
                telemetry.addData("7", "Press B to try again");
                telemetry.update();
                if(gamepad1.y) {
                    settings.setDouble("kDriveWithCorrectionsFastX_maxSpeed", kDriveWithCorrectionsFastX_maxSpeed);
                    settings.setDouble("kDriveWithCorrectionsFastX_p", kDriveWithCorrectionsFastX_p);
                    settings.setDouble("kDriveWithCorrectionsFastX_i", kDriveWithCorrectionsFastX_i);
                    settings.setDouble("kDriveWithCorrectionsFastX_d", kDriveWithCorrectionsFastX_d);
                    settings.setDouble("kDriveWithCorrectionsFastX_maxTurn", kDriveWithCorrectionsFastX_maxTurn);
                    settings.setDouble("kDriveWithCorrectionsFastX_kp", kDriveWithCorrectionsFastX_kp);
                    settings.setDouble("kDriveWithCorrectionsFastX_ki", kDriveWithCorrectionsFastX_ki);
                    settings.setDouble("kDriveWithCorrectionsFastX_kd", kDriveWithCorrectionsFastX_kd);
                    settings.setDouble("kDriveWithCorrectionsFastX_tolerance", kDriveWithCorrectionsFastX_tolerance);
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
