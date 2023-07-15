package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "analogTest", group = "Test")
@Disabled
public class analogPortTest extends LinearOpMode {

    private RevTouchSensor touch;


    @Override
    public void runOpMode() throws InterruptedException {
        touch = hardwareMap.get(RevTouchSensor.class, "touch");
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Touch", touch.getValue());
            telemetry.update();
        }
    }
}
