package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "AxonLeft", group = "Test")
public class ArmTestLeft extends LinearOpMode {
    public static double endPos1 = 0.5; //21.5 in
    public static double endPos2 = 0.4;
    private Servo rightServo, leftServo;

    @Override
    public void runOpMode() throws InterruptedException {
        rightServo = hardwareMap.servo.get("armRight");
        leftServo = hardwareMap.servo.get("armLeft");

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                leftServo.setPosition(endPos1);
            } else if (gamepad1.b) {
                leftServo.setPosition(endPos2);
            }
        }
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Running:", telemetry);
        telemetry.addData("endPos1:", endPos1);
        telemetry.addData("endPos2:", endPos2);
        telemetry.update();
    }
}
