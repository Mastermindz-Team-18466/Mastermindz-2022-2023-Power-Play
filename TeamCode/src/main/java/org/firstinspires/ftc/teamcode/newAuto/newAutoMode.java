package org.firstinspires.ftc.teamcode.newAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.newTeleOp.IntakeAndOuttake;
import org.firstinspires.ftc.teamcode.newTeleOp.clawAndV4B;
import org.firstinspires.ftc.teamcode.newTeleOp.newHorizontalSlides;
import org.firstinspires.ftc.teamcode.newTeleOp.newTurret;
import org.firstinspires.ftc.teamcode.newTeleOp.newVerticalSlides;
import org.firstinspires.ftc.teamcode.vision.apriltags.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@TeleOp(name = "newAutoMode", group = "Concept")
//@Disabled
public class newAutoMode extends LinearOpMode {

    OpenCvCamera webcam;
    IntakeAndOuttake inOutTake;
    newTurret turret;
    org.firstinspires.ftc.teamcode.newTeleOp.clawAndV4B clawAndV4B;
    newVerticalSlides verticalSlides;
    newHorizontalSlides horizontalSlides;
    RevColorSensorV3 distance;

    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        AprilTagDetectionPipeline aprilTagDetectionPipeline = new AprilTagDetectionPipeline(0.166, 587.272, 578.272, 402.145, 221.506);

        turret = new newTurret(hardwareMap);
        clawAndV4B = new clawAndV4B(hardwareMap);
        verticalSlides = new newVerticalSlides(hardwareMap);
        horizontalSlides = new newHorizontalSlides(hardwareMap);
        distance = hardwareMap.get(RevColorSensorV3.class, "Distance");

        inOutTake = new IntakeAndOuttake(turret, clawAndV4B, verticalSlides, horizontalSlides, distance);

        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);


        drive.setPoseEstimate(PoseStorage.currentPose);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);


        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int i) {

            }
        });

        while (!isStarted() && !isStopRequested()) {
            inOutTake.update();

            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == LEFT || tag.id == RIGHT || tag.id == MIDDLE) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                }

            }

            telemetry.update();
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .forward(2 * 23.5)
                .build()
        );

        waitForStart();

        long startTime = System.currentTimeMillis();
        while (opModeIsActive()) {
            long currentTime = System.currentTimeMillis();
            int position = tagOfInterest.id;

            drive.update();
            inOutTake.update();

            Pose2d poseEstimate = drive.getPoseEstimate();

            if (currentTime - startTime >= 2600 && !drive.isBusy()) {
                Pose2d currentPose = drive.getPoseEstimate();
                drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                        .UNSTABLE_addTemporalMarkerOffset(1, () -> {
                            turret.set(newTurret.ticks_in_degrees * 45);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(3.5, () -> {
                            horizontalSlides.set(0.72);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(4.8, () -> {
                            verticalSlides.set(3200);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(6.8, () -> {
                            clawAndV4B.clawControl(0.9);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(1, () -> {
                            verticalSlides.set(0);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(3.5, () -> {
                            horizontalSlides.set(0.27);
                        })
                        .UNSTABLE_addTemporalMarkerOffset(4.8, () -> {
                            clawAndV4B.clawControl(0);
                        })
                        .build()
                );
            }


            switch (position) {
                case 0:
                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .forward(1 * 23.5)
                            .strafeLeft(1 * 23.5)
                            .build()
                    );
                case 1:
                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .forward(1 * 23.5)
                            .build()
                    );
                case 2:
                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .forward(1 * 23.5)
                            .strafeRight(1 * 23.5)
                            .build()
                    );
            }


        }
        telemetry.addData("VerticalTargetPos:", inOutTake.verticalTargetPos);
        telemetry.addData("VerticalCurrentPos:", verticalSlides.liftMotor1.getCurrentPosition());
        telemetry.update();
    }
}
