package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.List;

// This class is responsible for controlling the horizontal slides of the robot. It uses a servo to extend and retract the slides.
// It also uses a distance sensor to detect the distance between the robot and a target.
public class HorizontalSlides {
    public static double position;
    Servo left_servo, right_servo, claw;
    Gamepad gamepad;

    // Offset is used to adjust the position of the left servo to align with the right servo
    public static double offset = 0.1;

    DistanceSensor distance;

    public enum State {
        EXTENDED, RETRACTED
    }

    // Constructor initializes all the required hardware
    public HorizontalSlides(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;

        // initialize distance sensor
        distance = hardwareMap.get(RevColorSensorV3.class, "Distance");

        // initialize left and right servos
        left_servo = hardwareMap.get(Servo.class, "leftServo");
        right_servo = hardwareMap.get(Servo.class, "rightServo");

        // set the direction of the right servo to reverse
        right_servo.setDirection(Servo.Direction.REVERSE);

        // initialize claw servo
        claw = hardwareMap.get(Servo.class, "claw");
    }

    // This method finds the closest pose from a list of poses to a target pose
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

    // This method finds the distance between two poses
    private static double distance(Pose2d pose1, Pose2d pose2) {
        return Math.sqrt(Math.pow(pose1.getX() - pose2.getX(), 2) + Math.pow(pose1.getY() - pose2.getY(), 2));
    }

    // This method calculates the position of the servo based on the robot's pose and the closest pipe
    private double returnPosition(Pose2d robot) {
        // Add predefined pipe positions to the list
        List<Pose2d> poses = new ArrayList<>();

        poses.add(new Pose2d(0, 1 * 23.5));
        poses.add(new Pose2d(0, -1 * 23.5));
        poses.add(new Pose2d(1 * 23.5, 0));
        poses.add(new Pose2d(-1 * 23.5, 0));

        // Find the closest pose to the robot
        Pose2d pipe = closestPose(poses, robot);

        // Limit the distance to a maximum value
        double d = distance(robot, pipe);
        d = Math.min(34.252, d) / (34.252 / 0.45) + 0.27;

        return d;
    }

    // This method controls the position of the servo to extend or retract the slides
    public void control(Pose2d robot, boolean withIr) {
        left_servo.setPosition(returnPosition(robot) + offset);
        right_servo.setPosition(returnPosition(robot));
    }
}
