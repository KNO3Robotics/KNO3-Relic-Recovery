package net.kno3.season.relicrecovery.nerva.program.auto.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;

import net.kno3.season.relicrecovery.nerva.program.auto.NervaAutoBlue;
import net.kno3.season.relicrecovery.nerva.program.auto.NervaAutoRed;
import net.kno3.season.relicrecovery.nerva.program.auto.VuforiaScanner;
import net.kno3.season.relicrecovery.nerva.robot.Nerva;
import net.kno3.season.relicrecovery.nerva.robot.NervaGlyphSystem;
import net.kno3.season.relicrecovery.nerva.robot.NervaIntakeSystem;
import net.kno3.season.relicrecovery.nerva.robot.NervaJewelSystem;
import net.kno3.season.relicrecovery.nerva.robot.NervaPersist;
import net.kno3.util.AngleUtil;
import net.kno3.util.AutoTransitioner;
import net.kno3.util.SimpleColor;
import net.kno3.util.Threading;
import net.kno3.util.TimeStopParameter;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by robotics on 10/29/2017.
 */
@Disabled
@Autonomous(name = "Blue Auto Audience 3G")
public class BlueAutoAudience3G extends NervaAutoBlue {
    private VuforiaScanner vuforiaScanner;

    private ColorSensor columnSensor;
    @Override
    public void postInit() {
        super.postInit();
        vuforiaScanner = new VuforiaScanner(hardwareMap, telemetry);
        Threading.async(() -> {
            while(!vuforiaScanner.isInitialized()) {
                if(isStopRequested()) {
                    return;
                }
                Thread.yield();
            }
            while(!isStarted() && !isStopRequested()) {
                vuforiaScanner.scan();
                RelicRecoveryVuMark vuMark = vuforiaScanner.getLastValid();
                telemetry.addData("Vuforia Current", vuforiaScanner.getLastValid().name());
                telemetry.update();
                Thread.yield();
            }
        });
        AutoTransitioner.transitionOnStop(this, "Nerva Teleop");

        columnSensor = hardwareMap.colorSensor.get("column color");
        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.CLOSED);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);
        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.INIT);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.INIT);
    }

    @Override
    public void main() {
        drive.modeSpeed();

        //Bring Lift up
        glyphSys.liftEncZero -= 600;
        glyphSys.setLiftPower(-0.5);

        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.CENTER);
        waitFor(.1);
        jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.DOWN);
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.DOWN);
        waitFor(1);

        //gets jewel color
        SimpleColor jewelColorL = jewelSys.getLeftColor();
        SimpleColor jewelColorR = jewelSys.getRightColor();
        waitFor(.15);

        //knocks jewel based on read jewel color
        if (jewelColorL == SimpleColor.BLUE || jewelColorR == SimpleColor.RED) {
            jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
        } else if (jewelColorL == SimpleColor.RED || jewelColorR == SimpleColor.BLUE ) {
            jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.LEFT);
        }

        //puts arm back up
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.CENTER);
        waitFor(.1);
        jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.CENTER);
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.UP);
        waitFor(.1);


        glyphSys.setLiftPower(0);

        //drives to column based on read pictograph
        RelicRecoveryVuMark vuMark = vuforiaScanner.getLastValid();


        glyphSys.setLiftPower(0.6);
        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.BACK);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.BACK);

        switch(vuMark) {
            case LEFT:
                drive.driveWithCorrectionFast(38.5, 0, 0.05, new TimeStopParameter(1.8));
                drive.stop();
                break;
            case CENTER:
                drive.driveWithCorrectionFast(31, 0, 0.05, new TimeStopParameter(1.8));
                drive.stop();
                break;
            case RIGHT:
                drive.driveWithCorrectionFast(22.5, 0, 0.05, new TimeStopParameter(1.8));
                drive.stop();
                break;
            default:
                drive.driveWithCorrectionFast(31, 0, 0.05, new TimeStopParameter(1.8));
                drive.stop();
                break;
        }

        //Bring Lift Down
        glyphSys.setLiftPower(0);
        drive.stop();

        //Turn 270
        drive.turnPIDslow(270);
        drive.stop();

        //drives forward into cryptobox
        //drive.driveRobotOriented(0, 0.6, 0);
        //waitFor(1.5);
        drive.driveWithCorrectionFast(17, 270, 0.05, new TimeStopParameter(1.2));
        drive.stop();

        //open glyph clamp
        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.OPEN);
        //back up a little bit
        drive.driveForY(-3.5);
        drive.stop();

        //Close Intake
        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);
        intakeSys.spitGlyph();

        waitFor(.8);

        drive.driveWithCorrectionFast(-8, 270, 0.05, new TimeStopParameter(.8));
        drive.stop();
        intakeSys.stop();


        drive.turnPIDslow(90);
        drive.stop();

        //drive.resetPos();

        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.AUTO);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.AUTO);

        intakeSys.intakeGlyph();
        drive.driveWithCorrectionFast(20, 80, 0.05, () -> glyphSys.hasGlyph());

        intakeSys.straighten();
        glyphSys.setHolderState(NervaGlyphSystem.GlyphLiftHolderState.CLOSED);
        drive.driveForY(-8);

        intakeSys.stop();
        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.CLOSED);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.HALF);
        waitFor(0.1);
        glyphSys.setLiftPower(-1);
        waitFor(.75);
        glyphSys.setLiftPower(0);
        glyphSys.setHolderState(NervaGlyphSystem.GlyphLiftHolderState.OPEN);


        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.AUTO);
        intakeSys.intakeGlyph();

        drive.turnPIDslow(100);
        drive.driveWithCorrectionFast(14, 110, 0.05, () -> glyphSys.hasGlyph());
        waitFor(.5);
        waitFor(.2);
        drive.driveWithCorrectionFast(-10, 90, 0.05, new TimeStopParameter(2));
        drive.stop();
        intakeSys.stop();
        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.CLOSED);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.CLOSED);
        glyphSys.setLiftPower(-0.6);
        waitFor(.5);
        glyphSys.setLiftPower(0);

        //displace = drive.getPosY();

        drive.turnPIDslow(270);
        drive.stop();

        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.BACK);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.BACK);
        //drive.driveWithCorrectionFastX(-displace, 270, 0.05, new TimeStopParameter(1.5));
        drive.stop();
        waitFor(.15);
        switch(vuMark) {
            default:
            case CENTER:
            case LEFT:
                drive.turnPIDslow(285);
                drive.driveWithCorrectionFast(35, 280, .05, new TimeStopParameter(1.75));
                break;
            case RIGHT:
                drive.turnPIDslow(255);
                drive.driveWithCorrectionFast(35, 250, .05, new TimeStopParameter(1.75));
                break;
        }

        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.OPEN);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);
        //back up a little bit
        drive.driveForY(-3.5);
        drive.stop();

        //Close Intake
            /*
            intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
            intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);
            intakeSys.spitGlyph();
            waitFor(1);
            glyphSys.setLiftPower(.45);
            drive.driveForY(-4);
            glyphSys.setLiftPower(0);
            intakeSys.stop();
            */
        glyphSys.zeroLiftEnc();
        //transition to teleop
        NervaPersist.lastWasAuto = true;
        NervaPersist.lastAngle = AngleUtil.normalize(drive.getHeading() - 90);
    }

}