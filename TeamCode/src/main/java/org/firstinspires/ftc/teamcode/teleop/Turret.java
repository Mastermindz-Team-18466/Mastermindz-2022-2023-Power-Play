package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.List;

public class Turret {
    public static final double ticks_in_degrees = 769 / 720;
    public static double kp = 0.0008, ki = 0, kd = 0.00001;
    public static double f = 0;
    public static double ticks;
    public static double target = 0;
    public PIDFController controller;
    DcMotor turret_motor;
    Gamepad gamepad;
    TeleOpFieldCentric driver;

    public Turret(boolean left, Gamepad gamepad, HardwareMap hardwareMap) {
        controller = new PIDFController(kp, ki, kd, f);
        turret_motor = hardwareMap.get(DcMotor.class, "leftLinear_slide");
        this.gamepad = gamepad;
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

    public static double getAngle(double[] a, double[] b, double[] c) {
        double ang = Math.toDegrees(Math.atan2(c[1] - b[1], c[0] - b[0]) - Math.atan2(a[1] - b[1], a[0] - b[0]));
        return -180 + ang;
    }

    public void control() {
        List<Pose2d> poses = new ArrayList<>();

        poses.add(new Pose2d(0, 1 * 23.5));
        poses.add(new Pose2d(0, -1 * 23.5));
        poses.add(new Pose2d(1 * 23.5, 0));
        poses.add(new Pose2d(-1 * 23.5, 0));

        Pose2d poseEstimate = driver.drive.getPoseEstimate();

        Pose2d closestPose = closestPose(poses, poseEstimate);
        double pipe_vector_x = closestPose.getX();
        double pipe_vector_y = closestPose.getY();

        double degrees = turret_motor.getCurrentPosition() / ticks_in_degrees;
        double x = Math.sin(degrees * Math.PI / 180);
        double y = Math.cos(degrees * Math.PI / 180);

        driver.drive.update();
        poseEstimate = driver.drive.getPoseEstimate();

        double angle = getAngle(new double[]{pipe_vector_x - poseEstimate.getX(), pipe_vector_y - poseEstimate.getY()}, new double[]{0, 0}, new double[]{poseEstimate.getX() + x, poseEstimate.getY() + y});

        if (degrees + angle > 360 || degrees + angle < -360) {
            if (angle > 0) {
                angle = angle - 360;
            } else {
                angle = 360 + angle;
            }
        }

        ticks = turret_motor.getCurrentPosition() + angle * ticks_in_degrees;

        loop(ticks);
    }

    public void fine_tune() {
        turret_motor.setPower(gamepad.left_stick_x);
    }

    public void loop(double ticks) {
        controller.setPIDF(kp, ki, kd, f);

        double pos = turret_motor.getCurrentPosition();

        double pid = controller.calculate(pos, ticks);

        double power = pid;

        turret_motor.setPower(power);

        if (turret_motor.getCurrentPosition() >= ticks - 100 && turret_motor.getCurrentPosition() <= ticks + 100) {
            turret_motor.setPower(0);
        } else {
            loop(ticks);
        }
    }
}
