package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;


@Autonomous(group = "drive")
public class trajTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            Pose2d currentPose = drive.getPoseEstimate();
            drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(currentPose)
                    .forward(23.5 * 2.5)
                    .strafeRight(23.5 * 0.5)
                    .strafeLeft(23.5 * 0.5)
                    .splineTo(new Vector2d(0, 0), Math.toRadians(180))
                    .build()
            );

            drive.update();

//            sleep(2000);
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(traj.end(), true)
//
//            );
        }
    }
}