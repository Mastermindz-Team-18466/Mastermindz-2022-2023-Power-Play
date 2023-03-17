package org.firstinspires.ftc.teamcode.newAuto;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

@Autonomous(name = "newAutoModeRight", group = "Concept")
//@Disabled
public class newAutoModeRight extends LinearOpMode {

    OpenCvCamera webcam;
    IntakeAndOuttake inOutTake;
    newTurret turret;
    clawAndArm clawAndArm;
    newVerticalSlides verticalSlides;
    newHorizontalSlides horizontalSlides;

    boolean otherSideCheck = true;
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;
    double posCount = 0;
    boolean cyclePos = true;
    Pose2d startPose = new Pose2d(1.5 * 23.5, -3 * 23.5, Math.PI / 2);
    boolean park = true;
    AprilTagDetection tagOfInterest = null;
    private double verticalOffset = 580;

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


        Vector2d endPosition = new Vector2d(1.5 * 23.5 - Math.sqrt(37.5) - 0, -3 * 23.5 + 49 + Math.sqrt(37.5));
        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.RIGHT_STACK_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                })
                .lineToSplineHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 49, Math.PI / 2 + Math.toRadians(40)))
                .setConstraints(new TrajectoryVelocityConstraint() {
                    @Override
                    public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                        return 50;
                    }
                }, new TrajectoryAccelerationConstraint() {
                    @Override
                    public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                        return 50;
                    }
                })
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

            if (currentTime - startTime >= 3000 && cycles <= 4) {

                if (!drive.isBusy()) {
                    drive.followTrajectorySequenceAsync(
                            drive.trajectorySequenceBuilder(currentPose)
                                    .setConstraints(new TrajectoryVelocityConstraint() {
                                        @Override
                                        public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                                            return 50;
                                        }
                                    }, new TrajectoryAccelerationConstraint() {
                                        @Override
                                        public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                                            return 50;
                                        }
                                    })
                                    .setTurnConstraint(Math.toRadians(330), Math.toRadians(270))
                                    .lineToConstantHeading(endPosition)
                                    .build()
                    );
                }

                if (cyclePos && currentTime - previousAction >= 1900) {

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_RIGHT_INTAKE);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);

                    previousAction = System.currentTimeMillis();

                    cyclePos = false;
                } else if (!cyclePos && currentTime - previousAction >= 1720) {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.RIGHT_STACK_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);

                    previousAction = System.currentTimeMillis();

                    cyclePos = true;
                    cycles++;
                    verticalOffset -= 100;
                }
            } else if (currentTime - startTime > 20000 && currentTime - startTime < 28000 && !drive.isBusy() && otherSideCheck) {
                drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                            inOutTake.setaInstructions(IntakeAndOuttake.Instructions.OTHER_SIDE);
                            inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);
                        })
                        .resetConstraints()
                        .lineToLinearHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 45, Math.PI / 2 + Math.toRadians(90)))
                        .lineToLinearHeading(new Pose2d(-(1.5 * 23.5 + 5), -3 * 23.5 + 45, Math.PI / 2 + Math.toRadians(90)))
                        .setConstraints(new TrajectoryVelocityConstraint() {
                            @Override
                            public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                                return 50;
                            }
                        }, new TrajectoryAccelerationConstraint() {
                            @Override
                            public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                                return 50;
                            }
                        })
                        .UNSTABLE_addTemporalMarkerOffset(0.2, () -> {
                            inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                            inOutTake.setaInstructions(IntakeAndOuttake.Instructions.LEFT_STACK_DEPOSIT);
                            inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                        })
                        .lineToLinearHeading(new Pose2d(-(1.5 * 23.5 - Math.sqrt(85) + 2.5 + 2), -3 * 23.5 + 50 + Math.sqrt(85), Math.PI / 2 + Math.toRadians(130)))
                        .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                            otherSideCheck = false;
                            cyclePos = true;
                            verticalOffset = 580;
                        })
                        .build()
                );
            } else if (currentTime - startTime > 23000 && currentTime - startTime < 28000 && otherSideCheck == false) {

                if (cyclePos && currentTime - previousAction >= 1900) {

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_LEFT_INTAKE);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);

                    previousAction = System.currentTimeMillis();

                    cyclePos = false;
                } else if (!cyclePos && currentTime - previousAction >= 1800) {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.LEFT_STACK_DEPOSIT);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);

                    previousAction = System.currentTimeMillis();

                    cyclePos = true;
                    cycles++;
                    verticalOffset -= 100;
                }
            } else if (currentTime - startTime >= 28000 && park) {
                System.out.println("Entered");

                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DRIVING);
                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);

                Pose2d endPose = new Pose2d(-(1.5 * 23.5 + 1.5) + Math.sqrt(45), -3 * 23.5 + 53 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90));
                switch (position) {
                    case 1:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .lineToSplineHeading(endPose)
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 + 25.5), -3 * 23.5 + 49 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90)))
                                .build()
                        );
                        break;
                    case 2:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .lineToSplineHeading(endPose)
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 + 2.5), -3 * 23.5 + 49 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90)))
                                .build()
                        );
                        break;
                    case 3:
                        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                .lineToSplineHeading(endPose)
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 - 21.5), -3 * 23.5 + 49 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90)))
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