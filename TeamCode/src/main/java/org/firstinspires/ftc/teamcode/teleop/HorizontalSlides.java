package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class HorizontalSlides {
    public static double position;
    Servo left_servo, right_servo, claw;
    Gamepad gamepad;
    
    public static double offset = 0.1;

    DistanceSensor distance;
    TeleOpFieldCentric driver;

    public enum State {
        RETRACTED,
        EXTENDED
    }

    public HorizontalSlides(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;
        
        distance = hardwareMap.get(DistanceSensor.class, "Distance");
        left_servo = hardwareMap.get(Servo.class, "leftServo");
        right_servo = hardwareMap.get(Servo.class, "rightServo");
        claw = hardwareMap.get(Servo.class, "Claw");
        
        driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad);
        driver.drive.setPoseEstimate(new Pose2d(0, 0));
    }

    public void control(State state, boolean withIr) {
        if (state == State.EXTENDED) {
            Pose2d pipe = new Pose2d(23.5, -70.5);

            driver.drive.update();
            Pose2d poseEstimate = driver.drive.getPoseEstimate();

            double d = Math.sqrt(Math.pow((pipe.getX() - poseEstimate.getX()), 2) + Math.pow((pipe.getY() - poseEstimate.getY()), 2));

            d = Math.min(21.5, d);

            double ranged_d = d / (21.5 / 0.45) + 0.27;

            right_servo.setPosition(ranged_d);
            left_servo.setPosition(ranged_d + offset);

            if (withIr) ir();
        } else {
            right_servo.setPosition(0.27);
            left_servo.setPosition(0.27 + offset);
        }
        
    }
    
    public void fine_tune() {
        while (gamepad.dpad_left == true) {
            double left_pos = Math.max(0.27, left_servo.getPosition() - 0.05);
            double right_pos = Math.max(0.37, right_servo.getPosition() - 0.05);

            left_servo.setPosition(left_pos);
            right_servo.setPosition(right_pos);
        }

        while (gamepad.dpad_right == true)  {
            double left_pos = Math.min(0.72, left_servo.getPosition() + 0.05);
            double right_pos = Math.min(0.82, right_servo.getPosition() + 0.05);

            left_servo.setPosition(left_pos);
            right_servo.setPosition(right_pos);
        }
    }

    public void ir() {
        if (distance.getDistance(DistanceUnit.CM) < 2.2) {
            claw.setPosition(0.8);
        }
    }
}
