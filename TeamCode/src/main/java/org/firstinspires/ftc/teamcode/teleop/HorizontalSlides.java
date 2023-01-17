package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;

import java.util.ArrayList;
import java.util.List;

public class HorizontalSlides {
    public static double offset = 0.1;
    public static Servo right_servo;
    public static Servo left_servo;
    public static TeleOpFieldCentric driver;
    private Gamepad gamepad;

    public HorizontalSlides(HardwareMap hardwareMap) {
        right_servo = hardwareMap.servo.get("rightServo");
        left_servo = hardwareMap.servo.get("leftServo");
        right_servo.setDirection(Servo.Direction.REVERSE);
        driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad);
        driver.drive.setPoseEstimate(driver.drive.getPoseEstimate());
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
    private double returnPosition() {
        // Add predefined pipe positions to the list
        List<Pose2d> poses = new ArrayList<>();

        poses.add(new Pose2d(0, 1 * 23.5));
        poses.add(new Pose2d(0, -1 * 23.5));
        poses.add(new Pose2d(1 * 23.5, 0));
        poses.add(new Pose2d(-1 * 23.5, 0));

        driver.drive.update();

        System.out.println("Driver Pos: " + driver.drive.getPoseEstimate());
        Pose2d robot = new Pose2d(driver.drive.getPoseEstimate().getX() + 14, driver.drive.getPoseEstimate().getY());

        // Find the closest pose to the robot
        Pose2d pipe = closestPose(poses, robot);

        // Limit the distance to a maximum value
        System.out.println("Dist: " + distance(robot, pipe));
        return distance(robot, pipe);
    }

    public void control(Pose2d robot, boolean ir) {
        double targetPosition = returnPosition();

        if (targetPosition > 0.72) {
            targetPosition = 0.72;
        }
        if (targetPosition < 0.27) {
            targetPosition = 0.27;
        }

        right_servo.setPosition(targetPosition);
        left_servo.setPosition(targetPosition + offset);
    }
}