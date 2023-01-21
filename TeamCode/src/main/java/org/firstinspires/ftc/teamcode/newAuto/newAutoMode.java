package org.firstinspires.ftc.teamcode.newAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
import java.util.Vector;

@Autonomous(name = "newAutoMode", group = "Concept")
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

    Pose2d startPose = new Pose2d(-3 * 23.5, 1.5 * 23.5, Math.toRadians(0));

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

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

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        webcam.setPipeline(aprilTagDetectionPipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
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

        int position = tagOfInterest.id;

        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
                    inOutTake.turretOuttakeOffset -= 90;
                    inOutTake.horizontalOuttakeOffset += 0.045;

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                })
                .forward(52)
                .build()
        );

        waitForStart();

        double cycles = 0;
        long startTime = System.currentTimeMillis();

        while (opModeIsActive()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - startTime >= 5000 && cycles < 5) {
                inOutTake.turretIntakeOffset -= 200;
                inOutTake.horizontalIntakeOffset -= 0.18;
                inOutTake.v4bIntakeOffset += 0.1;

                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.INTAKE);
                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);

                cycles = cycles + 5;
            }

            drive.update();
            inOutTake.update();
        }
    }
}