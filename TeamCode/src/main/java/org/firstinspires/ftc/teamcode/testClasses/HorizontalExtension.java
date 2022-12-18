package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;

@Config
@TeleOp(name = "Horiz_Test", group = "Test")
public class HorizontalExtension extends LinearOpMode {

    private Servo rightServo;
    private Servo leftServo;

    public static double endPos1= 0.72;
    public static double endPos2 = 0.27;
    public static double offset = 0.1;


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
