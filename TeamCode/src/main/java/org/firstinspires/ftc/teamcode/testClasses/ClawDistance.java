package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Disabled
@TeleOp(name = "ClawTest", group = "Test")
public class ClawDistance extends LinearOpMode {

    public static double endPos1 = 1; //21.5 in
    public static double endPos2 = 0.1;
    private Servo claw;

    @Override
    public void runOpMode() throws InterruptedException {
        claw = hardwareMap.servo.get("claw");

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                claw.setPosition(endPos1);
            } else if (gamepad1.b) {
                claw.setPosition(endPos2);
            }
        }
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Running:", telemetry);
        telemetry.addData("endPos1:", endPos1);
        telemetry.addData("endPos2:", endPos2);
        telemetry.update();
    }
}
