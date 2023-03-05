package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo_Test", group = "Test")
@Disabled
public class ServoTest extends LinearOpMode {

    private Servo testServo;


    @Override
    public void runOpMode() throws InterruptedException {
        testServo = hardwareMap.servo.get("testServo");

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                testServo.setPosition(0);
            } else if (gamepad1.b) {
                testServo.setPosition(1);
            } else if (gamepad1.x) {
                testServo.setPosition(0.5);
            }
        }
    }
}
