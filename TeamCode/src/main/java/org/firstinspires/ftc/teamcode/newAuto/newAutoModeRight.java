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
    boolean tagFound = false;
    int position;
    boolean lastConeCheck = false;
    private double verticalOffset = 635;
    boolean preload_deposit = false;
    boolean turned = false;
    boolean moved = false;
    boolean moved2 = false;
    boolean movedTrans = false;
    boolean pidOnPose = true;
    double posConstraint = 70;
    boolean park2 = true;

    Pose2d endPosition;

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.autoCheck = true;

        drive.setPoseEstimate(startPose);

        newHorizontalSlides.auto = true;

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

        if (tagFound) {
            position = tagOfInterest.id;
        } else {
            position = 2;
        }

        endPosition = new Pose2d(1.5 * 23.5 - 5, -3 * 23.5 + 59, Math.PI / 2 + Math.toRadians(85));
        Pose2d endPosition1 = new Pose2d(1.5 * 23.5 - 1, -3 * 23.5 + 59, Math.PI / 2 + Math.toRadians(85));
        drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(startPose)
                .UNSTABLE_addTemporalMarkerOffset(3, () -> {
                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.PRELOAD);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                })
                .lineToSplineHeading(endPosition1)
                .setConstraints(new TrajectoryVelocityConstraint() {
                    @Override
                    public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                        return 30;
                    }
                }, new TrajectoryAccelerationConstraint() {
                    @Override
                    public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                        return 30;
                    }
                })
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

            if (currentTime - startTime >= 0 && cycles <= 5) {

                if (!drive.isBusy() && pidOnPose) {
                    drive.followTrajectorySequenceAsync(
                            drive.trajectorySequenceBuilder(currentPose)
                                    .setConstraints(new TrajectoryVelocityConstraint() {
                                        @Override
                                        public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                                            return 30;
                                        }
                                    }, new TrajectoryAccelerationConstraint() {
                                        @Override
                                        public double get(double v, @NonNull Pose2d pose2d, @NonNull Pose2d pose2d1, @NonNull Pose2d pose2d2) {
                                            return 30;
                                        }
                                    })
//                                    .setTurnConstraint(Math.toRadians(330), Math.toRadians(270))
                                    .lineToSplineHeading(endPosition)
                                    .build()
                    );
                }


               if (currentTime - startTime >= 4100 && currentTime - startTime < 24500) {

                    if (cyclePos && currentTime - previousAction >= 2000) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_RIGHT_INTAKE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);

                        previousAction = System.currentTimeMillis();

                        cyclePos = false;
                    } else if (!cyclePos && currentTime - previousAction >= 2000) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.RIGHT_STACK_DEPOSIT);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);

                        previousAction = System.currentTimeMillis();

                        cyclePos = true;
                        cycles++;
                        verticalOffset -= 105;
                  }
                } if (currentTime - startTime >= 24500 && park) {
                    System.out.println("Entered");

                    inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                    inOutTake.setaInstructions(IntakeAndOuttake.Instructions.AUTO_TO_TELE);
                    inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);

                    endPosition = new Pose2d(1.5 * 23.5 - 2, -3 * 23.5 + 49, Math.PI + Math.toRadians(3));

                    drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                            .resetConstraints()
                            .lineToSplineHeading(endPosition)
                            .build()
                    );

                    park = false;

                } else if (currentTime - startTime >= 27250 && park2) {
                    switch (position) {
                        case 3:
                            drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                    //.lineToConstantHeading(new Vector2d(endPosition.getX(), endPosition.getY()))
                                    .build()
                            );
                            break;
                        case 2:
                            endPosition = new Pose2d(endPosition.getX() - 23.5, endPosition.getY() - 3, endPosition.getHeading());
                            drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                    //.resetConstraints()
                                    .lineToConstantHeading(new Vector2d(endPosition.getX(), endPosition.getY()))
                                    .build()
                            );
                            break;
                        case 1:
                            endPosition = new Pose2d(endPosition.getX() - 23.5 * 2, endPosition.getY() - 3, endPosition.getHeading());
                            drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                                    //.resetConstraints()
                                    .lineToConstantHeading(new Vector2d(endPosition.getX(), endPosition.getY()))
                                    //.lineToSplineHeading(new Pose2d(-(1.5 * 23.5 - 21.5), -3 * 23.5 + 49 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90)))
                                    .build()
                            );
                            break;
                    }

                    park2 = false;
                }

            }


            drive.update();
            inOutTake.update();

        }
    }
}
