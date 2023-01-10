package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class ClawDistance extends LinearOpMode {
    RevColorSensorV3 distance;
    Servo claw;

    @Override
    public void runOpMode() {
        distance = hardwareMap.get(RevColorSensorV3.class, "Distance");
        claw = hardwareMap.get(Servo.class, "Claw");
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Distance", distance.getDistance(DistanceUnit.CM));
            telemetry.update();
            if (distance.getDistance(DistanceUnit.CM) < 5) {
                claw.setPosition(0.85);
            } else {  // Otherwise, stop the motor
                claw.setPosition(0.2);
            }
        }
    }
}