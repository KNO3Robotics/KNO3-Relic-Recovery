package net.kno3.season.relicrecovery.nerva.program.auto.blue;

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
import org.firstinspires.ftc.robotcore.internal.system.SystemProperties;

/**
 * Created by robotics on 10/29/2017.
 */


//testing 2 glyph auto
@Autonomous(name = "Blue Field 2 Glyph")
public class BlueAutoField2G extends NervaAutoRed {
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
        jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.DOWN);
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.DOWN);
        waitFor(1);


        //knocks jewel based on read jewel color
        /*
        if(vuforiaScanner.getLastColor() == SimpleColor.RED) {
            jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.LEFT);
        }
        else if(vuforiaScanner.getLastColor() == SimpleColor.BLUE) {
            jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
        }
        else {
        */

        SimpleColor jewelColorL = jewelSys.getLeftColor();
        SimpleColor jewelColorR = jewelSys.getRightColor();
        waitFor(.15);

        //knocks jewel based on read jewel color
        if (jewelColorL == SimpleColor.BLUE || jewelColorR == SimpleColor.RED) {
            jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.RIGHT);
        } else if (jewelColorL == SimpleColor.RED || jewelColorR == SimpleColor.BLUE ) {
            jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.LEFT);
        }
    //}

        //puts arm back up
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.CENTER);
        waitFor(.1);
        jewelSys.setBlueKnocker(NervaJewelSystem.KnockerPosition.CENTER);
        waitFor(.1);
        jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.UP);
        waitFor(.1);


        glyphSys.setLiftPower(0);

        glyphSys.setHolderState(NervaGlyphSystem.GlyphLiftHolderState.OPEN);
        drive.driveWithCorrectionFast(20, 0, 0.05, new TimeStopParameter(1.5));
        drive.stop();
        waitFor(.1);

        drive.driveWithCorrectionFastX(26, 0, 0.05, new TimeStopParameter(2));
        drive.stop();

        drive.turnPIDslow(180);

        intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
        intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);

        drive.driveWithCorrectionFast(25, 180, 0.05, true, new TimeStopParameter(1.5));
        drive.turnPIDslow(135);

        intakeSys.intakeGlyph();
        drive.driveWithCorrectionFast(12, 135, 0.01, true, glyphSys::hasGlyph);
        /*for(int i = 0; i < 3; i++) {
            if(!glyphSys.hasGlyph()) {
                drive.driveWithCorrectionFast(-15, 35 + i*5, 0.01, true, glyphSys::hasGlyph);
                if(glyphSys.hasGlyph()) break;
                drive.turnPIDslow(35 + i*5);
                drive.driveWithCorrectionFast(15, 35 + i*5, 0.01, true, glyphSys::hasGlyph);
            }
        }*/

        //drive.driveWithCorrectionFast(-10, 45, 0.01, true, new TimeStopParameter(1));

        drive.turnPIDslow(359);
        drive.stop();

        glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.CLOSED);

        intakeSys.stop();

        glyphSys.setLiftPower(-.5);
        waitFor(1);
        glyphSys.setLiftPower(0);


        //glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);

        //drive.driveRobotOriented(.015, .35, 0);
        //waitFor(3);
        drive.driveWithCorrectionSlow(0.4, 100, 359, 0.05, true, new TimeStopParameter(3) );

        drive.driveWithCorrectionSlow(-1, 0, 0.05, new TimeStopParameter(.7));
        drive.stop();

        drive.turnPIDslow(0);

        //drive against wall
        long startTime = System.currentTimeMillis(); //fetch starting time
        while(((drive.getHeading() <= 3) || (drive.getHeading() >= 300)) &&((System.currentTimeMillis()-startTime)<1500))
        {
            drive.driveRobotOriented(-0.35, 0, 0);
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
            case RIGHT:
                //drive.driveWithCorrectionSlowX(12.5, 180);
                drive.turnPIDslow(340);
                drive.driveWithCorrectionSlow(22, 340, 0.05, new TimeStopParameter(1.5));
                drive.driveRobotOriented(0, 0.2, 0.2);
                waitFor(0.25);
                drive.driveRobotOriented(0, 0.2, -0.2);
                waitFor(0.25);
                drive.driveRobotOriented(0, 0.2, 0.2);
                waitFor(0.25);
                drive.stop();
                break;
            case LEFT:
                drive.driveWithCorrectionSlowX(-10.0, 0);
                drive.turnPIDslow(330);
                drive.driveWithCorrectionSlow(22, 330, 0.05, new TimeStopParameter(1.5));
                drive.stop();
                break;
            case CENTER:
            default:
                //drive.driveWithCorrectionSlowX(5, 180);
                drive.turnPIDslow(336);
                drive.driveWithCorrectionSlow(22, 336, 0.05, new TimeStopParameter(1.5));
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
