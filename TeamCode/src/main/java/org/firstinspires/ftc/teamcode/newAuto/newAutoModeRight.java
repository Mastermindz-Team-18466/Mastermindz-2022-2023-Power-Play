package org.firstinspires.ftc.teamcode.newAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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

@Autonomous(name = "newAutoModeRight", group = "Concept")
//@Disabled
public class newAutoModeRight extends LinearOpMode {

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

    int v4bHeightCheck = 0;

    boolean cyclePos = true;

    Pose2d startPose = new Pose2d(-3 * 23.5, 1.5 * 23.5, Math.toRadians(0));

    boolean park = true;

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

        turret.turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

        //increase value to overshoot
        inOutTake.turretIntakeOffset -= 228;

        inOutTake.horizontalIntakeOffset -= 0.025;
        inOutTake.v4bIntakeOffset += 0.1;


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
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:"+tagOfInterest.id);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:"+tagOfInterest.id);
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:"+tagOfInterest.id);
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
            telemetry.addLine("Tag snapshot:\n"+tagOfInterest.id);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        int position = tagOfInterest.id;

        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
                    //make more negative to make turret undershoot
                    inOutTake.turretOuttakeOffset += -35;
                    inOutTake.horizontalOuttakeOffset -= 0.035;

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.LEFT_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                })
                .forward(52)
                .strafeRight(5)
                .build()
        );

        waitForStart();

        double cycles = 0;
        double previousAction = 3500;
        long startTime = System.currentTimeMillis();


        while (opModeIsActive()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - startTime >= 3500 && cycles < 5 && currentTime - startTime < 27250) {
                if (cyclePos && currentTime - previousAction >= 2850) {

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_RIGHT_INTAKE);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);

                    previousAction = System.currentTimeMillis();

                    cyclePos = false;
                } else if (!cyclePos && currentTime - previousAction >= 2750) {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.RIGHT_STACK_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);

                    previousAction = System.currentTimeMillis();

                    cyclePos = true;
                    cycles++;
                }
            } else if (currentTime - startTime >= 27250 && park) {
                System.out.println("Entered");
                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);

                switch (position) {
                    case 1:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .strafeLeft(26.5)
                                .build()
                        );
                        break;
                    case 2:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .strafeLeft(3)
                                .build()
                        );
                        break;
                    case 3:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .strafeRight(16.5)
                                .build()
                        );
                        break;
                }

                park = false;
            }

            if (cycles == 2 && v4bHeightCheck == 0) {
                inOutTake.v4bIntakeOffset -= 0.065;
                v4bHeightCheck++;
            }

            drive.update();
            inOutTake.update();

        }
    }
}