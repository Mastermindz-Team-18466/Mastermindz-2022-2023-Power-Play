package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class Turret {
    DcMotor turret_motor;
    Gamepad gamepad;
    TeleOpFieldCentric driver;

    public final double ticks_in_degrees = 769 / 720;

    public Turret(Gamepad gamepad, HardwareMap hardwareMap) {
        turret_motor = hardwareMap.get(DcMotor.class, "leftLinear_slide");
        this.gamepad = gamepad;
        driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad);
        driver.drive.setPoseEstimate(new Pose2d(0, 0));
    }

    public void control() {
        double pipe_vector_x = 23.5;
        double pipe_vector_y = -70.5;

        double degrees = turret_motor.getCurrentPosition() / ticks_in_degrees;
        double x = Math.sin(degrees * Math.PI / 180);
        double y = Math.cos(degrees * Math.PI / 180);

        driver.drive.update();
        Pose2d poseEstimate = driver.drive.getPoseEstimate();

        double angle = getAngle(new double[] {pipe_vector_x - poseEstimate.getX(), pipe_vector_y - poseEstimate.getY()}, new double[] {0, 0}, new double[] {poseEstimate.getX() + x, poseEstimate.getY() + y});
        double ticks = angle * ticks_in_degrees;

        turret_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        System.out.println(angle);
    }

    public static double getAngle(double[] a, double[] b, double[] c) {
        double ang = Math.toDegrees(Math.atan2(c[1] - b[1], c[0] - b[0]) - Math.atan2(a[1] - b[1], a[0] - b[0]));
        return -180 + ang;
    }
}
