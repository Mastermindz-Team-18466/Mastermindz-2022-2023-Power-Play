package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "ClawSpin", group = "Test")
public class ClawFlipping extends LinearOpMode {

    public static double startPos = 0; //21.5 in
    public static double endPos = 1;
    private Servo flipServo;

    @Override
    public void runOpMode() throws InterruptedException {
        flipServo = hardwareMap.servo.get("clawSpin");

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                flipServo.setPosition(startPos);
            } else if (gamepad1.b) {
                flipServo.setPosition(endPos);
            }
        }
        telemetry.addData("Running:", telemetry);
        telemetry.addData("endPos:", endPos);
        telemetry.addData("startPos:", startPos);
        telemetry.update();
    }
}
