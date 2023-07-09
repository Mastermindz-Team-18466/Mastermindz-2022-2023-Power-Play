package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "SetTo0", group = "Test")
public class Test1 extends LinearOpMode {

    public static double armLeftPos = 0.05;
    public static double armRightPos = 0.7;
    private Servo rightHoriz;
    private Servo leftHoriz;
    private Servo claw, clawSpin, armLeft, armRight;

    @Override
    public void runOpMode() throws InterruptedException {
        rightHoriz = hardwareMap.servo.get("rightServo");
        leftHoriz = hardwareMap.servo.get("leftServo");
        claw = hardwareMap.servo.get("claw");
        clawSpin = hardwareMap.servo.get("clawSpin");
        armLeft = hardwareMap.servo.get("armLeft");
        armRight = hardwareMap.servo.get("armRight");

        rightHoriz.setDirection(Servo.Direction.REVERSE);

        boolean toggled = false;

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                rightHoriz.setPosition(0);
            } else if (gamepad1.b) {
                leftHoriz.setPosition(0.1);
            } else if (gamepad1.x) {
                claw.setPosition(0.2);
            } else if (gamepad1.y) {
                clawSpin.setPosition(0);
            } else if (gamepad1.right_bumper) {
                armLeft.setPosition(0.05);
            } else if (gamepad1.left_bumper) {
                armRight.setPosition(0.7);
            }
        }
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Running:", telemetry);
        telemetry.update();
    }
}
