package org.firstinspires.ftc.teamcode.newAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.newTeleOp.IntakeAndOuttake;
import org.firstinspires.ftc.teamcode.newTeleOp.clawAndArm;
import org.firstinspires.ftc.teamcode.newTeleOp.newHorizontalSlides;
import org.firstinspires.ftc.teamcode.newTeleOp.newTurret;
import org.firstinspires.ftc.teamcode.newTeleOp.newVerticalSlides;
import org.firstinspires.ftc.teamcode.vision.apriltags.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous(name = "newAutoModeLeft", group = "Concept")
//@Disabled
public class newAutoModeLeft extends LinearOpMode {

    OpenCvCamera webcam;
    IntakeAndOuttake inOutTake;
    newTurret turret;
    clawAndArm clawAndArm;
    newVerticalSlides verticalSlides;
    newHorizontalSlides horizontalSlides;

    private double verticalOffset = 610;

    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    double posCount = 0;

    boolean cyclePos = true;

    Pose2d startPose = new Pose2d(-1.5 * 23.5, -3 * 23.5, Math.PI / 2);

    boolean park = true;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        AprilTagDetectionPipeline aprilTagDetectionPipeline = new AprilTagDetectionPipeline(0.166, 587.272, 578.272, 402.145, 221.506);

        turret = new newTurret(hardwareMap);
        clawAndArm = new clawAndArm(hardwareMap);
        verticalSlides = new newVerticalSlides(hardwareMap);
        horizontalSlides = new newHorizontalSlides(hardwareMap);

        inOutTake = new IntakeAndOuttake(turret, clawAndArm, verticalSlides, horizontalSlides);

        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);

        turret.turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);

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

        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_CLOSE);
        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);

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
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:" + tagOfInterest.id);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:" + tagOfInterest.id);
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:" + tagOfInterest.id);
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
            telemetry.addLine("Tag snapshot:\n" + tagOfInterest.id);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        int position = tagOfInterest.id;


        Vector2d endPosition = new Vector2d(-(1.5 * 23.5 - Math.sqrt(37.5) - 0), -3 * 23.5 + 49 + Math.sqrt(37.5));
        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(2.6, () -> {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.RIGHT_STACK_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                })
                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 - 3), -3 * 23.5 + 60, Math.PI / 2 - Math.toRadians(40)))
                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 - 3), -3 * 23.5 + 49, Math.PI / 2 - Math.toRadians(40)))
                .lineToConstantHeading(endPosition)
                .build()
        );

        waitForStart();

        double cycles = 0;
        double previousAction = 3500;
        long startTime = System.currentTimeMillis();


        while (opModeIsActive()) {
            long currentTime = System.currentTimeMillis();

            Pose2d currentPose = drive.getPoseEstimate();

            inOutTake.verticalIntakeOffset = verticalOffset;


            if (currentTime - startTime >= 5000 && cycles < 5 && currentTime - startTime < 25400) {

                if (!drive.isBusy()) {
                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(currentPose)
                            .lineToConstantHeading(endPosition)
                            .build()
                    );
                }

                if (cyclePos && currentTime - previousAction >= 2000) {

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_RIGHT_INTAKE);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);

                    previousAction = System.currentTimeMillis();

                    cyclePos = false;
                } else if (!cyclePos && currentTime - previousAction >= 2250) {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.RIGHT_STACK_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);

                    previousAction = System.currentTimeMillis();

                    cyclePos = true;
                    cycles++;
                    verticalOffset -= 100;
                }
            } else if (currentTime - startTime >= 25400 && currentTime - startTime < 27200) {
                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.STRAIGHT_DEPOSIT);
                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);
            } else if (currentTime - startTime >= 27200 && park) {
                System.out.println("Entered");

                switch (position) {
                    case 1:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .lineToConstantHeading(new Vector2d(-(1.5 * 23.5 - 2), -3 * 23.5 + 50))
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 - 25.5), -3 * 23.5 + 50, Math.PI / 2))
                                .build()
                        );
                        break;
                    case 2:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 - 2), -3 * 23.5 + 50, Math.PI / 2))
                                .build()
                        );
                        break;
                    case 3:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .lineToConstantHeading(new Vector2d(-(1.5 * 23.5 - 2), -3 * 23.5 + 50))
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 + 21.5), -3 * 23.5 + 50, Math.PI / 2))
                                .build()
                        );
                        break;
                }

                park = false;
            }


            drive.update();
            inOutTake.update();

        }
    }
}