package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.List;

// This class is responsible for controlling the turret of the robot. It uses a PIDF controller to rotate the turret to a target angle.
// It also uses a DcMotor to rotate the turret and a SampleMecanumDrive to get the robot's current position.
public class Turret {
    // Constants for the PIDF controller and the conversion from degrees to ticks
    public static final double ticks_in_degrees = 2403.125/360;
    public static double kp = 0.02;
    public static double ki = 0.00;
    public static double kd = 0.00003;
    public static double f = 0.1;
    public static double ticks;

    // PIDF controller, DcMotor and SampleMecanumDrive objects
    public PIDFController controller;
    DcMotor turret_motor;
    Gamepad gamepad;

    public Turret(Gamepad gamepad, HardwareMap hardwareMap) {
        // initialize PIDF controller
        controller = new PIDFController(kp, ki, kd, f);

        // initialize turret motor
        turret_motor = hardwareMap.get(DcMotor.class, "turretMotor");
        this.gamepad = gamepad;
    }

    // This method calculates the closest pose from a list of poses to a target pose using the distance formula.
    public static Pose2d closestPose(List<Pose2d> poses, Pose2d targetPose) {
        // Initialize the closest pose to the first pose in the list
        Pose2d closestPose = poses.get(0);
        double closestDistance = distance(poses.get(0), targetPose);

        // Iterate through the list of poses
        for (int i = 1; i < poses.size(); i++) {
            Pose2d pose = poses.get(i);

            // Calculate the distance
            double poseDistance = distance(pose, targetPose);

            // Check if the current pose is closer than the closest pose
            if (poseDistance < closestDistance) {
                // Update
                closestDistance = poseDistance;
                closestPose = pose;
            }
        }
        return closestPose;
    }

    // This method calculates the distance between two poses using the distance formula.
    private static double distance(Pose2d pose1, Pose2d pose2) {
        return Math.sqrt(Math.pow(pose1.getX() - pose2.getX(), 2) + Math.pow(pose1.getY() - pose2.getY(), 2));
    }

    // This method calculates the angle between two vectors using the dot product and cross product.
    public static double angleBetween(double[] a, double[] b) {
        // Calculate the dot product
        double dotProduct = a[0] * b[0] + a[1] * b[1];

        // Calculate the magnitudes of the vectors
        double magnitudeA = Math.sqrt(a[0] * a[0] + a[1] * a[1]);
        double magnitudeB = Math.sqrt(b[0] * b[0] + b[1] * b[1]);

        // Calculate the angle (in radians)
        double angle = Math.toDegrees(Math.acos(dotProduct / (magnitudeA * magnitudeB)));

        // Determine the direction of the angle using the cross product
        double crossProduct = a[0] * b[1] - a[1] * b[0];
        if (crossProduct < 0) {
            // Angle is positive (clockwise)
            return Math.toDegrees(angle);
        } else {
            // Angle is negative (counterclockwise)
            return Math.toDegrees(-angle);
        }
    }

    // This method takes in the current pose of the robot and returns the number of ticks needed for the turret to rotate to the closest target
    public double returnTicks(Pose2d robot) {
        // A list of possible target poses
        List<Pose2d> poses = new ArrayList<>();

        poses.add(new Pose2d(0, 1 * 23.5));
        poses.add(new Pose2d(0, -1 * 23.5));
        poses.add(new Pose2d(1 * 23.5, 0));
        poses.add(new Pose2d(-1 * 23.5, 0));

        // Finding the closest target pose
        Pose2d closestPose = closestPose(poses, robot);

        // Rewriting Pose2d as initial and terminal points
        double[] pipe = {closestPose.getX(), closestPose.getY()};
        double[] me = {robot.getX(), robot.getY()};
        double degrees = turret_motor.getCurrentPosition() / ticks_in_degrees;

        // Using the initial and terminal points to create vectors
        double[] a = {23.5 * Math.sin(Math.toRadians(degrees)), 23.5 * Math.cos(Math.toRadians(degrees))};
        double[] b = {pipe[0] - me[0], pipe[1] - me[1]};

        // Calculating the angle between the robot's current position and the closest target
        double angle = angleBetween(a, b);

        // Limiting the angle to a range between 180 and -180 degrees
        if (degrees + angle > 180 || degrees + angle < -180) {
            if (angle > 0) {
                angle = angle - 360;
            } else {
                angle = 360 + angle;
            }
        }

        // Returning the number of ticks needed to rotate the turret to the target angle
        ticks = turret_motor.getCurrentPosition() + angle * ticks_in_degrees;
        return ticks;
    }

    public void control(Pose2d robot) {
        // loop(returnTicks(robot));
    }

    public void fine_tune() {
        turret_motor.setPower(gamepad.left_stick_x);
    }

    public void loop(double ticks) {
        controller.setPIDF(kp, ki, kd, f);

        double pos = turret_motor.getCurrentPosition();

        double pid = controller.calculate(pos, ticks);

        double power = pid;

        // Limit the power to 66.7% of its initial speed
        turret_motor.setPower(power / 1.5);

        // Check if the DcMotor is within a 200 tick range of the target
        if (turret_motor.getCurrentPosition() >= ticks - 100 && turret_motor.getCurrentPosition() <= ticks + 100) {
            // If so, stop
            turret_motor.setPower(0);
        } else {
            loop(ticks);
        }
    }

    public void reset() {
        turret_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
