package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;

@TeleOp(name = "TeleOpTest", group = "Concept")
//@Disabled
public class TeleOpTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);
        driver.drive.setPoseEstimate(new Pose2d(0, 0));

        waitForStart();

        while (opModeIsActive()) {
            driver.drive.update();
            Pose2d poseEstimate = driver.drive.getPoseEstimate();

            driver.move();
        }
    }
}