package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;

@Config
@TeleOp(name = "pidonposition", group = "Test")
public class PIDonPOs extends LinearOpMode {
    public static double kpx = 0;
    public static double kix = 0;
    public static double kdx = 0;
    public static double kpy = 0;
    public static double kiy = 0;
    public static double kdy = 0;
    public static double kpt = 0;
    public static double kit = 0;
    public static double kdt = 0;
    public static double xTargetPosition = 0;
    public static double yTargetPosition = 0;
    public static double tTargetPosition = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d poseEstimate = drive.getPoseEstimate();
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while (opModeIsActive()) {
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

            telemetry.addData("X: ", poseEstimate.getX());
            telemetry.addData("Y: ", poseEstimate.getY());
            telemetry.addData("Robot Heading: ", poseEstimate.getHeading());
            telemetry.addData("Running: ", telemetry);

            telemetry.update();
        }
    }

}