package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "Horiz_Test", group = "Test")
public class HorizontalExtension extends LinearOpMode {

    public static double endPos1 = 0.72; //21.5 in
    public static double endPos2 = 0.27;
    public static double offset = 0.1;
    private Servo rightServo;
    private Servo leftServo;

    @Override
    public void runOpMode() throws InterruptedException {
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo.setDirection(Servo.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                rightServo.setPosition(endPos1);
                leftServo.setPosition(endPos1 + offset);
            } else if (gamepad1.b) {
                rightServo.setPosition(endPos2);
                leftServo.setPosition(endPos2 + offset);
            }
        }
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Running:", telemetry);
        telemetry.addData("endPos1:", endPos1);
        telemetry.addData("endPos2:", endPos2);
        telemetry.addData("offset:", offset);
        telemetry.update();
    }
}
