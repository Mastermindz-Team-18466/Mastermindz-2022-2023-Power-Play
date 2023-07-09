package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
//@Disabled
@TeleOp(name = "Horiz_Test", group = "Test")
public class HorizontalExtension extends LinearOpMode {

    public static double endPos1 = 1; //21.5 in
    public static double endPos2 = 0.6;
    private Servo rightServo;
    private Servo leftServo;
    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;

    public double TURNING_POINT = 0.85;
    double position;

    @Override
    public void runOpMode() throws InterruptedException {
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo.setDirection(Servo.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                position = TURNING_POINT;
                while (position < endPos1) {
                    position += INCREMENT;
                    if (position > endPos1) {
                        position = endPos1;
                    }
                    leftServo.setPosition(position);
                    rightServo.setPosition(position);
                    sleep(CYCLE_MS);
                }
            } else if (gamepad1.b) {
                rightServo.setPosition(endPos2);
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
