//package org.firstinspires.ftc.teamcode.vision.auto;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.Storage;
//import org.firstinspires.ftc.teamcode.teleop.Claw;
//import org.firstinspires.ftc.teamcode.teleop.HorizontalSlides;
//import org.firstinspires.ftc.teamcode.teleop.Outtake;
//import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;
//import org.firstinspires.ftc.teamcode.teleop.Turret;
//import org.firstinspires.ftc.teamcode.teleop.V4B;
//import org.firstinspires.ftc.teamcode.teleop.VerticalSlides;
//import org.firstinspires.ftc.teamcode.vision.apriltags.AprilTagDetectionPipeline;
//import org.openftc.apriltag.AprilTagDetection;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//
//import java.util.ArrayList;
//
//@Autonomous(name = "AutonomousModeRight", group = "Concept")
////@Disabled
//class AutonomousModeRight extends LinearOpMode {
//    OpenCvCamera webcam;
//
//    static final double FEET_PER_METER = 3.28084;
//
//    int LEFT = 1;
//    int MIDDLE = 2;
//    int RIGHT = 3;
//
//    AprilTagDetection tagOfInterest = null;
//
//    TeleOpFieldCentric driver;
//    public Outtake outtake = new Outtake(hardwareMap, new Turret(false, gamepad2, hardwareMap), new Claw(gamepad2, hardwareMap), new V4B(gamepad2, hardwareMap), new HorizontalSlides(false, gamepad2, hardwareMap), new VerticalSlides(gamepad2, hardwareMap));
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
//
//        AprilTagDetectionPipeline aprilTagDetectionPipeline = new AprilTagDetectionPipeline(0.166, 587.272, 578.272, 402.145, 221.506);
//        webcam.setPipeline(aprilTagDetectionPipeline);
//
//        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
//        {
//            @Override
//            public void onOpened()
//            {
//                webcam.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
//            }
//
//            @Override
//            public void onError(int errorCode)
//            {
//
//            }
//        });
//
//        Storage.setLeft(false);
//
//        telemetry.setMsTransmissionInterval(50);
//
//        /*
//         * The INIT-loop:
//         * This REPLACES waitForStart!
//         */
//        while (!isStarted() && !isStopRequested())
//        {
//            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
//
//            if(currentDetections.size() != 0)
//            {
//                boolean tagFound = false;
//
//                for(AprilTagDetection tag : currentDetections)
//                {
//                    if(tag.id == LEFT || tag.id == RIGHT || tag.id == MIDDLE)
//                    {
//                        tagOfInterest = tag;
//                        tagFound = true;
//                        break;
//                    }
//                }
//
//                if(tagFound)
//                {
//                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
//                }
//                else
//                {
//                    telemetry.addLine("Don't see tag of interest :(");
//
//                    if(tagOfInterest == null)
//                    {
//                        telemetry.addLine("(The tag has never been seen)");
//                    }
//                    else
//                    {
//                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
//                    }
//                }
//
//            }
//            else
//            {
//                telemetry.addLine("Don't see tag of interest :(");
//
//                if(tagOfInterest == null)
//                {
//                    telemetry.addLine("(The tag has never been seen)");
//                }
//                else
//                {
//                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
//                }
//
//            }
//
//            telemetry.update();
//        }
//
//        /*
//         * The START command just came in: now work off the latest snapshot acquired
//         * during the init loop.
//         */
//
//        /* Update the telemetry */
//        if(tagOfInterest != null)
//        {
//            telemetry.addLine("Tag snapshot:\n");
//            telemetry.update();
//        }
//        else
//        {
//            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
//            telemetry.update();
//        }
//
//        driver.drive.setPoseEstimate(new Pose2d(0, 0));
//
//        waitForStart();
//
//        double cycles = 0;
//        long startTime = System.currentTimeMillis();
//
//        while (opModeIsActive()) {
//            int position = tagOfInterest.id;
//            long currentTime = System.currentTimeMillis();
//
//            Pose2d currentPose = driver.drive.getPoseEstimate();
//
//            driver.drive.followTrajectorySequenceAsync(driver.drive.trajectorySequenceBuilder(currentPose)
//                    .forward(23.5 * 2.5)
//                    .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                        outtake.setOuttakePos(Outtake.outtakePosEnum.PLACE_ON_POLE);
//                        outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.TURN_TURRET);
//                    })
//                    .build()
//            );
//
//            if (currentTime - startTime >= 2600 && !driver.drive.isBusy()) {
//                currentPose = driver.drive.getPoseEstimate();
//
//                driver.drive.followTrajectorySequenceAsync(driver.drive.trajectorySequenceBuilder(currentPose)
//                        .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                            outtake.setOuttakePos(Outtake.outtakePosEnum.REDO_FOR_GRAB);
//                            outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.TURN_TURRET);
//                        })
//                        .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                            outtake.setOuttakePos(Outtake.outtakePosEnum.CLOSE_CLAW);
//                            outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.CLOSE_CLAW);
//                        })
//                        .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                            outtake.setOuttakePos(Outtake.outtakePosEnum.PLACE_ON_POLE);
//                            outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.TURN_TURRET);
//                        })
//                        .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                            outtake.setOuttakePos(Outtake.outtakePosEnum.OPEN_CLAW);
//                            outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.OPEN_CLAW);
//                        })
//                        .build()
//                );
//                cycles = cycles + 1;
//            }
//
//            outtake.setOuttakePos(Outtake.outtakePosEnum.NEUTRAL);
//            outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.CLOSE_CLAW);
//            driver.drive.update();
//
//            currentPose = driver.drive.getPoseEstimate();
//            switch (position) {
//                case 1:
//                    driver.drive.followTrajectorySequenceAsync(driver.drive.trajectorySequenceBuilder(currentPose)
//                            .strafeLeft(23.5)
//                            .build()
//                    );
//                case 2:
//                    driver.drive.followTrajectorySequenceAsync(driver.drive.trajectorySequenceBuilder(currentPose)
//                            .build()
//                    );
//                case 3:
//                    driver.drive.followTrajectorySequenceAsync(driver.drive.trajectorySequenceBuilder(currentPose)
//                            .strafeRight(23.5)
//                            .build()
//                    );
//                default:
//                    break;
//
//            }
//        }
//    }
//}
