package net.kno3.season.relicrecovery.nerva.program.auto.red;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
@Autonomous(name = "Red Field 2 glyph 2")
public class RedAutoField2G extends NervaAutoRed {
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
        waitFor(1);



        //knocks jewel based on read jewel color
        /*
        if(vuforiaScanner.getLastColor() == SimpleColor.RED) {
            jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
        }
        else if(vuforiaScanner.getLastColor() == SimpleColor.BLUE) {
            jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.LEFT);
        }
        else {
        */
            SimpleColor jewelColorL = jewelSys.getLeftColor();
            SimpleColor jewelColorR = jewelSys.getRightColor();
            waitFor(.15);

            //knocks jewel based on read jewel color
            if (jewelColorL == SimpleColor.BLUE || jewelColorR == SimpleColor.RED) {
                jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.LEFT);
            } else if (jewelColorL == SimpleColor.RED || jewelColorR == SimpleColor.BLUE ) {
                jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
            }
        //}


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

        drive.driveWithCorrectionFastX(26, 0, 0.05, new TimeStopParameter(2));
        drive.stop();


        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);

        drive.driveWithCorrectionFast(27, 0, 0.05, true, new TimeStopParameter(1.5));
        drive.turnPIDslow(45);

        intakeSys.intakeGlyph();
        drive.driveWithCorrectionFast(10, 45, 0.01, true, glyphSys::hasGlyph);
        /*for(int i = 0; i < 3; i++) {
            if(!glyphSys.hasGlyph()) {
                drive.driveWithCorrectionFast(-15, 35 + i*5, 0.01, true, glyphSys::hasGlyph);
                if(glyphSys.hasGlyph()) break;
                drive.turnPIDslow(35 + i*5);
                drive.driveWithCorrectionFast(15, 35 + i*5, 0.01, true, glyphSys::hasGlyph);
            }
        }*/

        //drive.driveWithCorrectionFast(-10, 45, 0.01, true, new TimeStopParameter(1));

        drive.turnPIDslow(181);
        drive.stop();

        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.CLOSED);
        intakeSys.stop();

        glyphSys.setLiftPower(-.5);
        waitFor(1);
        glyphSys.setLiftPower(0);

        //drive.driveRobotOriented(.015, .35, 0);
        //waitFor(3);
        drive.driveWithCorrectionSlow(0.4, 100, 181, 0.05, true, new TimeStopParameter(3));
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

        drive.driveForY(-13);
        drive.stop();

        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.BACK);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.BACK);



        //drives to column based on read pictograph
        RelicRecoveryVuMark vuMark = vuforiaScanner.getLastValid();

        //drives to column based on read pictograph
        switch(vuMark) {
            case LEFT:
                //drive.driveWithCorrectionSlowX(12.5, 180);
                drive.turnPIDslow(200);
                drive.driveWithCorrectionSlow(22, 200, 0.05, new TimeStopParameter(1.5));
                drive.driveRobotOriented(0, 0.2, 0.2);
                waitFor(0.25);
                drive.driveRobotOriented(0, 0.2, -0.2);
                waitFor(0.25);
                drive.driveRobotOriented(0, 0.2, 0.2);
                waitFor(0.25);
                drive.stop();
                break;
            case RIGHT:
                drive.driveWithCorrectionSlowX(8.5, 180);
                drive.turnPIDslow(210);
                drive.driveWithCorrectionSlow(22, 210, 0.05, new TimeStopParameter(1.5));
                drive.stop();
                break;
            case CENTER:
            default:
                //drive.driveWithCorrectionSlowX(5, 180);
                drive.turnPIDslow(216);
                drive.driveWithCorrectionSlow(22, 216, 0.05, new TimeStopParameter(1.5));
                drive.stop();
                break;
        }




        //open glyph clamp
        glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.OPEN);
        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);

        //back up a little bit
        drive.driveForY(-4.5);
        drive.stop();


        //transition to teleop
        glyphSys.zeroLiftEnc();
        NervaPersist.lastWasAuto = true;
        NervaPersist.lastAngle = AngleUtil.normalize(drive.getHeading() - 90);
    }

}
