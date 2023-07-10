package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "SetTo1", group = "Test")
public class Test extends LinearOpMode {

    public static double endPos1 = 0.5; //21.5 in
    public static double endPos2 = 0;
    public static double offset = 0.1;
    private Servo rightHoriz;
    private Servo leftHoriz;
    private Servo claw, clawSpin, armLeft, armRight;
    public static double pos = 0.7;

    @Override
    public void runOpMode() throws InterruptedException {
        rightHoriz = hardwareMap.servo.get("rightServo");
        leftHoriz = hardwareMap.servo.get("leftServo");
        claw = hardwareMap.servo.get("claw");
        clawSpin = hardwareMap.servo.get("clawSpin");
        armLeft = hardwareMap.servo.get("armLeft");
        armRight = hardwareMap.servo.get("armRight");

        rightHoriz.setDirection(Servo.Direction.REVERSE);
        armRight.setDirection(Servo.Direction.REVERSE);

        boolean toggled = false;

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                rightHoriz.setPosition(1);
            } else if (gamepad1.b) {
                leftHoriz.setPosition(1);
            } else if (gamepad1.x) {
                claw.setPosition(0.64);
            } else if (gamepad1.y) {
                clawSpin.setPosition(pos);
            } else if (gamepad1.right_bumper) {
                armLeft.setPosition(1);
            } else if (gamepad1.left_bumper) {
                armRight.setPosition(1);
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
