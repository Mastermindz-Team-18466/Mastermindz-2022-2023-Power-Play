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
@TeleOp(name = "ArmTest", group = "Test")
public class ArmTest extends LinearOpMode {
    private Servo rightServo, leftServo;

    //open: 0.07 / 0.0
    //close: 0.07/0.03

    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50 / 6;     // period of each cycle
    public static double MAX_POS = 0.54;     // Maximum rotational position
    public static double MIN_POS = 0.08;

    double position = (MIN_POS + MAX_POS) / 2;

    @Override
    public void runOpMode() throws InterruptedException {
        rightServo = hardwareMap.servo.get("armRight");
        leftServo = hardwareMap.servo.get("armLeft");

        rightServo.setDirection(Servo.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                while (position > MIN_POS) {
                    position -= INCREMENT;
                    rightServo.setPosition(position);
                    leftServo.setPosition(position);
                    sleep(CYCLE_MS);
                }

            } else if (gamepad1.b) {
                while (position < MAX_POS) {
                    position += INCREMENT;
                    rightServo.setPosition(position);
                    leftServo.setPosition(position);
                    sleep(CYCLE_MS);
                }
            }
            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
            telemetry.addData("Running:", telemetry);
            telemetry.update();
        }
    }
}