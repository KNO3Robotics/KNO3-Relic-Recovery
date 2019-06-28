package net.kno3.season.relicrecovery.nerva.program;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by robotics on 2/25/2018.
 */
@TeleOp(name = "floodlight")
public class FloodLight extends OpMode{
    private DcMotor light;

    @Override
    public void init() {
        light = hardwareMap.dcMotor.get("light");
    }

    @Override
    public void loop() {
        light.setPower(.25);

    }
}
