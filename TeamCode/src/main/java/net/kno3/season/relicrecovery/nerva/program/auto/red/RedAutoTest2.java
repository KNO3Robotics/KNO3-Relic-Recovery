package net.kno3.season.relicrecovery.nerva.program.auto.red;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import net.kno3.season.relicrecovery.nerva.program.auto.NervaAutoRed;
import net.kno3.season.relicrecovery.nerva.program.auto.VuforiaScanner;
import net.kno3.season.relicrecovery.nerva.robot.NervaGlyphSystem;
import net.kno3.season.relicrecovery.nerva.robot.NervaIntakeSystem;
import net.kno3.season.relicrecovery.nerva.robot.NervaJewelSystem;
import net.kno3.season.relicrecovery.nerva.robot.NervaPersist;
import net.kno3.util.AngleUtil;
import net.kno3.util.AutoTransitioner;
import net.kno3.util.SimpleColor;
import net.kno3.util.Threading;
import net.kno3.util.TimeStopParameter;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by robotics on 10/29/2017.
 */


//testing 2 glyph auto
@Disabled
@Autonomous(name = "Red Field 2 glyph")
public class RedAutoTest2 extends NervaAutoRed {
    private VuforiaScanner vuforiaScanner;

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
                ////Log.i("vif", "vu scan complete");
                try {
                    vuforiaScanner.scanColor();
                    telemetry.addData("blue blobs", vuforiaScanner.jeweldetector.blueJewel());
                    telemetry.addData("red blobs", vuforiaScanner.jeweldetector.redJewel());
                    //Log.i("vif", "color scan complete");
                    telemetry.addData("Jewel Current", vuforiaScanner.getLastColor() == null ? "NONE" : vuforiaScanner.getLastColor().name());
                } catch (Exception ex) {
                    //Log.i("vif", "Color scan fail");
                    Log.e("vif", "Jewel read fail", ex);
                }
                telemetry.update();
                Thread.yield();
            }
        });
        AutoTransitioner.transitionOnStop(this, "Nerva Teleop");

        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.CLOSED);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);
        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.INIT);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.INIT);

    }

    @Override
    public void main() {
        vuforiaScanner.close();
        drive.modeSpeed();

        //Bring Lift up
        glyphSys.liftEncZero -= 600;
        glyphSys.setLiftPower(-0.5);

        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.CENTER);
        waitFor(.1);
        jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.DOWN);
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.DOWN);
        waitFor(.7);


        //knocks jewel based on read jewel color
        if(vuforiaScanner.getLastColor() == SimpleColor.RED) {
            jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
        }
        else if(vuforiaScanner.getLastColor() == SimpleColor.BLUE) {
            jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.LEFT);
        }
        else {
            SimpleColor jewelColor = jewelSys.getLeftColor();
            waitFor(.15);

            //knocks jewel based on read jewel color
            if (jewelColor == SimpleColor.BLUE) {
                jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
            } else if (jewelColor == SimpleColor.RED) {
                jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.LEFT);
            }
        }

        //puts arm back up
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.CENTER);
        waitFor(.1);
        jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.CENTER);
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.UP);
        waitFor(.1);


        glyphSys.setLiftPower(0);

        glyphSys.setHolderState(NervaGlyphSystem.GlyphLiftHolderState.OPEN);
        drive.driveWithCorrectionFast(-20, 0, 0.05, new TimeStopParameter(1.5));
        drive.stop();
        waitFor(.1);

        drive.driveWithCorrectionFastX(30, 0, 0.05, new TimeStopParameter(2));
        drive.stop();


        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);



        intakeSys.intakeGlyph();
        drive.driveWithCorrectionFast(45, 0, 0.01, true, glyphSys::hasGlyph);
        for(int i = 0; i < 3; i++) {
            if(!glyphSys.hasGlyph()) {
                drive.driveWithCorrectionFast(-15, 0 - i*5, 0.01, true, glyphSys::hasGlyph);
                if(glyphSys.hasGlyph()) break;
                drive.turnPIDslow(0 - i*5);
                drive.driveWithCorrectionFast(15, 0 - i*5, 0.01, true, glyphSys::hasGlyph);
            }
        }


        drive.turnPIDslow(180);
        drive.stop();

        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.CLOSED);
        intakeSys.stop();

        glyphSys.setLiftPower(-.5);
        waitFor(1);
        glyphSys.setLiftPower(0);

        drive.driveRobotOriented(.015, .35, 0);
        waitFor(3);
        drive.stop();


        drive.driveForY(-1);
        drive.stop();

        drive.turnPIDslow(180);

        //drive against wall
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((drive.getHeading() >= 177) &&((System.currentTimeMillis()-startTime)<1500))
        {
            drive.driveRobotOriented(0.35, 0, 0);
        }
        drive.stop();

        drive.driveForY(-12);
        drive.stop();

        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.BACK);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.BACK);



        //drives to column based on read pictograph
        RelicRecoveryVuMark vuMark = vuforiaScanner.getLastValid();

        //drives to column based on read pictograph
        switch(vuMark) {
            case LEFT:
                drive.driveWithCorrectionSlowX(12.5, 180);
                drive.turnPIDslow(160);
                drive.driveWithCorrectionFast(12, 225, 0.05, new TimeStopParameter(1));
                drive.stop();
                break;
            case CENTER:
                drive.driveWithCorrectionSlowX(5, 180);
                drive.turnPIDslow(215);
                drive.driveWithCorrectionFast(12, 225, 0.05, new TimeStopParameter(1));
                drive.stop();
                break;
            case RIGHT:
                drive.driveWithCorrectionSlowX(12.5, 180);
                drive.turnPIDslow(215);
                drive.driveWithCorrectionFast(12, 225, 0.05, new TimeStopParameter(1));
                drive.stop();
                break;
            default:
                drive.driveWithCorrectionSlowX(5, 180);
                drive.turnPIDslow(215);
                drive.driveWithCorrectionFast(12, 225, 0.05, new TimeStopParameter(1));
                drive.stop();
                break;
        }



        //open glyph clamp
        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.OPEN);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);

        //back up a little bit
        drive.driveForY(-6);
        drive.stop();


        //transition to teleop
        glyphSys.zeroLiftEnc();
        NervaPersist.lastWasAuto = true;
        NervaPersist.lastAngle = AngleUtil.normalize(drive.getHeading() - 90);
    }

}
