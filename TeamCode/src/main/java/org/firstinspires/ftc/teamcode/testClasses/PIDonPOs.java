package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;

@TeleOp (name = "pidonposition", group = "Test")
@Config
public class PIDonPOs extends LinearOpMode {
    double kpx = 0;
    double kix = 0;
    double kdx = 0;
    double kpy = 0;
    double kiy = 0;
    double kdy = 0;
    double kpt = 0;
    double kit = 0;
    double kdt = 0;
    double xTargetPosition = 0;
    double yTargetPosition = 0;
    double tTargetPosition = 0;

    @Override
    public void runOpMode() throws InterruptedException{
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);

        PIDController xControl = new PIDController(kpx, kix, kdx);
        PIDController yControl = new PIDController(kpy, kiy, kdy);
        PIDController tControl = new PIDController(kpt, kit, kdt);

        double x = xControl.calculate(driver.drive.getPoseEstimate().getX(), xTargetPosition);
        double y = yControl.calculate(driver.drive.getPoseEstimate().getY(), yTargetPosition);
        double t = tControl.calculate(driver.drive.getPoseEstimate().getHeading(), tTargetPosition);
        double x_rotated = x * Math.cos(driver.drive.getPoseEstimate().getHeading()) - y * Math.sin(driver.drive.getPoseEstimate().getHeading());
        double y_rotated = x * Math.sin(driver.drive.getPoseEstimate().getHeading()) + y * Math.cos(driver.drive.getPoseEstimate().getHeading());

        driver.drive.motors.get(0).setPower(x_rotated + y_rotated + t);
        driver.drive.motors.get(1).setPower(x_rotated - y_rotated + t);
        driver.drive.motors.get(2).setPower(x_rotated - y_rotated - t);
        driver.drive.motors.get(3).setPower(x_rotated + y_rotated - t);
    }

}
