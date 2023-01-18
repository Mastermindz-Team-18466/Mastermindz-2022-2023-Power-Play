package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.newTeleOp.clawAndV4B;

@Config
@TeleOp
public class ClawDistance extends LinearOpMode {

    org.firstinspires.ftc.teamcode.newTeleOp.clawAndV4B clawAndV4B;

    public static double endPos = 0.75;
    public static double v4bEndPos = 0.45;

    @Override
    public void runOpMode() {
        clawAndV4B = new clawAndV4B(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while (opModeIsActive()) {
            clawAndV4B.v4bTargetPos(v4bEndPos);
            telemetry.update();
            if (gamepad1.a) {
                clawAndV4B.clawControl(endPos);
            } else if (gamepad1.b) {  // Otherwise, stop the motor
                clawAndV4B.clawControl(0.4);
            }
        }
    }
}