package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class ClawDistance extends LinearOpMode {
    DistanceSensor distance;
    Servo claw;

    @Override
    public void runOpMode() {
        distance = hardwareMap.get(DistanceSensor.class, "Distance");
        claw = hardwareMap.get(Servo.class, "Claw");

        waitForStart();
        while (opModeIsActive()) {
            if (distance.getDistance(DistanceUnit.CM) < 2.2) {
                claw.setPosition(0.8);
            } else {  // Otherwise, stop the motor
                claw.setPosition(0);
            }
        }
    }
}