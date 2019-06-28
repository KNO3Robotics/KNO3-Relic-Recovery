package net.kno3.season.relicrecovery.nerva.program.auto.red;

/**
 * Created by robotics on 10/29/2017.
 */
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
@Disabled
@Autonomous(name = "Red Auto Field 3G")
public class RedAutoField3G extends NervaAutoRed {
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
      waitFor(.15);

      //gets jewel color
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
      Threading.async(() -> {
                  jewelSys.setRedKnocker(NervaJewelSystem.KnockerPosition.CENTER);
                  waitFor(.1);
                  jewelSys.setLeftArm(NervaJewelSystem.ArmPosition.UP);
                  waitFor(.1);
              });


      glyphSys.setLiftPower(0);

      //drives backwards off ramp
      drive.driveWithCorrectionSlow(-25,0);
      waitFor(.15);

      //Turn 180
      drive.turnPIDslow(180);
      drive.stop();

      //Open Intake
      intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.BACK);
      intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.BACK);

      //Bring Lift Down
      glyphSys.setLiftPower(0.6);
      waitFor(1);
      glyphSys.setLiftPower(0);

      drive.driveWithCorrectionSlowX(9, 180,.05, new TimeStopParameter(1.4));

      //drives into glass wall
      drive.driveRobotOriented(0, .4, 0);
      waitFor(0.75);
      drive.stop();
      waitFor(0.15);

      drive.driveForY(-1);
      drive.stop();
      waitFor(.15);

      drive.turnPIDslow(180);
      drive.stop();
      waitFor(.15);

      //drive against wall
      long startTime = System.currentTimeMillis(); //fetch starting time
      while((drive.getHeading() <= 183) &&((System.currentTimeMillis()-startTime)<1500))
      {
          drive.driveRobotOriented(-0.35, 0, 0);
      }
      drive.stop();
      waitFor(.15);

      drive.driveForY(-7);
      drive.stop();
      waitFor(0.15);

      drive.turnPIDslow(180);
      drive.stop();
      waitFor(.15);

      //drives to column based on read pictograph
      RelicRecoveryVuMark vuMark = vuforiaScanner.getLastValid();

      //drives to column based on read pictograph
      switch(vuMark) {
          case LEFT:
              drive.driveWithCorrectionSlowX(-19.25, 180);
              drive.stop();
              break;
          case CENTER:
              drive.driveWithCorrectionSlowX(-11.75, 180);
              drive.stop();
              break;
          case RIGHT:
              drive.driveWithCorrectionSlowX(-4.5, 180);
              drive.stop();
              break;
          default:
              drive.driveWithCorrectionSlowX(-11.75, 180);
              drive.stop();
              break;
      }

      //sets heading back to initial heading
      //drive.turnPIDslow(180);
      //drive.stop();
      waitFor(0.15);

      //drives forward into cryptobox
      drive.driveWithCorrectionSlow(10, 180, 0.05, new TimeStopParameter(1));
      drive.stop();
      waitFor(0.15);

      //open glyph clamp
      glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.OPEN);
      waitFor(.15);

      //back up a little bit
      drive.driveForY(-10);
      drive.stop();
      waitFor(0.15);

      drive.turnPIDslow(30);

      intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);
      intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
      intakeSys.intakeGlyph();

      drive.driveWithCorrectionFast(48, 30, 0.01, true, glyphSys::hasGlyph);
      for(int i = 0; i < 3; i++) {
          if(!glyphSys.hasGlyph()) {
              drive.driveWithCorrectionFast(-15, 30 - i*5, 0.01, true, glyphSys::hasGlyph);
              if(glyphSys.hasGlyph()) break;
              drive.turnPIDslow(30 - i*5);
              drive.driveWithCorrectionFast(15, 30 - i*5, 0.01, true, glyphSys::hasGlyph);
          }
      }
      drive.driveForY(-5);

      glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.CLOSED);
      waitFor(0.1);
      glyphSys.setHolderState(NervaGlyphSystem.GlyphLiftHolderState.CLOSED);
      glyphSys.setLiftPower(-1);
      drive.turnPIDslow(45);
      glyphSys.setLiftPower(0);
      glyphSys.setHolderState(NervaGlyphSystem.GlyphLiftHolderState.OPEN);
      drive.driveWithCorrectionFast(12, 30, 0.01, true, glyphSys::hasGlyph);
      for(int i = 0; i < 3; i++) {
          if(!glyphSys.hasGlyph()) {
              drive.driveWithCorrectionFast(-15, 30 + i*5, 0.01, true, glyphSys::hasGlyph);
              if(glyphSys.hasGlyph()) break;
              drive.turnPIDslow(30 + i*5);
              drive.driveWithCorrectionFast(15, 30 + i*5, 0.01, true, glyphSys::hasGlyph);
          }
      }
      glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.CLOSED);
      glyphSys.setLiftPower(-0.5);
      drive.turnPIDslow(200);
      glyphSys.setLiftPower(0);
      drive.driveWithCorrectionFast(30, 180, true);
      intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.BACK);
      intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.BACK);
      intakeSys.stop();




      //drives into glass wall
      drive.driveRobotOriented(0, .4, 0);
      waitFor(0.75);
      drive.stop();
      waitFor(0.15);

      drive.driveForY(-1);
      drive.stop();
      waitFor(.15);

      drive.turnPIDslow(180);
      drive.stop();
      waitFor(.15);

      //drive against wall
      startTime = System.currentTimeMillis(); //fetch starting time
      while((drive.getHeading() >= 177) &&((System.currentTimeMillis()-startTime)<1500))
      {
          drive.driveRobotOriented(0.35, 0, 0);
      }
      drive.stop();
      waitFor(.15);

      drive.driveForY(-7);
      drive.stop();
      waitFor(0.15);

      drive.turnPIDslow(180);
      drive.stop();
      waitFor(.15);

      //drives to column based on read pictograph
      switch(vuMark) {
          case LEFT:
              drive.driveWithCorrectionSlowX(19.25, 180);
              drive.stop();
              break;
          case CENTER:
              drive.driveWithCorrectionSlowX(11.75, 180);
              drive.stop();
              break;
          case RIGHT:
              drive.driveWithCorrectionSlowX(4.5, 180);
              drive.stop();
              break;
          default:
              drive.driveWithCorrectionSlowX(11.75, 180);
              drive.stop();
              break;
      }

      //sets heading back to initial heading
      //drive.turnPIDslow(180);
      //drive.stop();
      waitFor(0.15);

      //drives forward into cryptobox
      drive.driveWithCorrectionSlow(10, 180, 0.05, new TimeStopParameter(1));
      drive.stop();
      waitFor(0.15);

      //open glyph clamp
      glyphSys.setTopClamp(NervaGlyphSystem.TopClampState.OPEN);
      glyphSys.setBottomClamp(NervaGlyphSystem.BottomClampState.OPEN);
      waitFor(.15);

      //back up a little bit
      drive.driveForY(-10);
      drive.stop();
      waitFor(0.15);




      /*
      //Close Intake
      intakeSys.setLeftArm(NervaIntakeSystem.ArmPositions.OUT);
      intakeSys.setRightArm(NervaIntakeSystem.ArmPositions.OUT);
      intakeSys.spitGlyph();
      waitFor(1);
      intakeSys.stop();
      waitFor(.15);

      drive.driveForY(-3.5);
      drive.stop();*/


      //transition to teleop
      glyphSys.zeroLiftEnc();
      NervaPersist.lastWasAuto = true;
      NervaPersist.lastAngle = AngleUtil.normalize(drive.getHeading() - 90);

  }

}
