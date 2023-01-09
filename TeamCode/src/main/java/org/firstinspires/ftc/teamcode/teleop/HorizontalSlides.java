package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.List;

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

    public HorizontalSlides(boolean left, Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;
        
        distance = hardwareMap.get(DistanceSensor.class, "Distance");
        left_servo = hardwareMap.get(Servo.class, "leftServo");
        right_servo = hardwareMap.get(Servo.class, "rightServo");
        claw = hardwareMap.get(Servo.class, "Claw");
        
        driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad);

        if (left) driver.drive.setPoseEstimate(new Pose2d(-1.5 * 23.5, -3 * 23.5));
        else driver.drive.setPoseEstimate(new Pose2d(1.5 * 23.5, -3 * 23.5));
    }

    public static Pose2d closestPose(List<Pose2d> poses, Pose2d targetPose) {
        Pose2d closestPose = poses.get(0);
        double closestDistance = distance(poses.get(0), targetPose);
        for (int i = 1; i < poses.size(); i++) {
            Pose2d pose = poses.get(i);
            double poseDistance = distance(pose, targetPose);
            if (poseDistance < closestDistance) {
                closestDistance = poseDistance;
                closestPose = pose;
            }
        }
        return closestPose;
    }

    private static double distance(Pose2d pose1, Pose2d pose2) {
        return Math.sqrt(Math.pow(pose1.getX() - pose2.getX(), 2) + Math.pow(pose1.getY() - pose2.getY(), 2));
    }

    public void control(State state, boolean withIr) {
        if (state == State.EXTENDED) {
            List<Pose2d> poses = new ArrayList<>();

            poses.add(new Pose2d(0, 1 * 23.5));
            poses.add(new Pose2d(0, -1 * 23.5));
            poses.add(new Pose2d(1 * 23.5, 0));
            poses.add(new Pose2d(-1 * 23.5, 0));

            Pose2d poseEstimate = driver.drive.getPoseEstimate();

            Pose2d pipe = closestPose(poses, poseEstimate);

            driver.drive.update();
            poseEstimate = driver.drive.getPoseEstimate();

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
