package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

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

    public Turret(Gamepad gamepad, HardwareMap hardwareMap) {
        controller = new PIDFController(kp, ki, kd, f);
        turret_motor = hardwareMap.get(DcMotor.class, "leftLinear_slide");
        this.gamepad = gamepad;
        driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad);
        driver.drive.setPoseEstimate(new Pose2d(0, 0));
    }

    public static double getAngle(double[] a, double[] b, double[] c) {
        double ang = Math.toDegrees(Math.atan2(c[1] - b[1], c[0] - b[0]) - Math.atan2(a[1] - b[1], a[0] - b[0]));
        return -180 + ang;
    }

    public void control() {
        double pipe_vector_x = 23.5;
        double pipe_vector_y = -70.5;

        double degrees = turret_motor.getCurrentPosition() / ticks_in_degrees;
        double x = Math.sin(degrees * Math.PI / 180);
        double y = Math.cos(degrees * Math.PI / 180);

        driver.drive.update();
        Pose2d poseEstimate = driver.drive.getPoseEstimate();

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

    public void loop(double ticks) {
        controller.setPIDF(kp, ki, kd, f);

        double pos = turret_motor.getCurrentPosition();

        double pid = controller.calculate(pos, ticks);

        double power = pid;

        turret_motor.setPower(power);

        if (turret_motor.getCurrentPosition() >= ticks - 5 && turret_motor.getCurrentPosition() <= ticks + 5) {
            turret_motor.setPower(0);
        } else {
            loop(ticks);
        }
    }
}
