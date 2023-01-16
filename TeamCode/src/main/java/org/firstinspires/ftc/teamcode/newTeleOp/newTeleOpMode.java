package org.firstinspires.ftc.teamcode.newTeleOp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.newAuto.Trajectories;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;

@TeleOp(name = "newTeleOpMode", group = "Concept")
//@Disabled
public class newTeleOpMode extends LinearOpMode {

    enum Mode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL,
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    TeleOpFieldCentric driver;
    Trajectories trajectories;

    IntakeAndOuttake inOutTake;
    newTurret turret;
    clawAndV4B clawAndV4B;
    newVerticalSlides verticalSlides;
    newHorizontalSlides horizontalSlides;
    RevColorSensorV3 distance;


    int closedToIntakeCheck = 1;
    int cycleCheck = 0;

    @Override
    public void runOpMode() {

        PhotonCore.enable();
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);

        turret = new newTurret(hardwareMap);
        clawAndV4B = new clawAndV4B(hardwareMap);
        verticalSlides = new newVerticalSlides(hardwareMap);
        horizontalSlides = new newHorizontalSlides(hardwareMap);
        distance = hardwareMap.get(RevColorSensorV3.class, "Distance");

        inOutTake = new IntakeAndOuttake(turret, clawAndV4B, verticalSlides, horizontalSlides, distance);

        inOutTake.setVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
        inOutTake.setInstructions(IntakeAndOuttake.Instructions.CLOSED);
        inOutTake.setSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);

        driver.drive.setPoseEstimate(PoseStorage.currentPose);

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();


        waitForStart();

        while (opModeIsActive()) {
            driver.drive.update();
            inOutTake.update();

            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);

            previousGamepad2.copy(currentGamepad2);
            currentGamepad2.copy(gamepad1);

            Pose2d poseEstimate = driver.drive.getPoseEstimate();

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            switch (currentMode) {
                case DRIVER_CONTROL:
                    driver.move();
//                    if (gamepad1.b || gamepad1.x) {
//                        currentMode = Mode.AUTONOMOUS_CONTROL;
//                    }

                    if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
                        inOutTake.horizontalIntakeOffset -= 0.025;
                    }
                    if (currentGamepad2.dpad_right && !previousGamepad2.a) {
                        inOutTake.horizontalIntakeOffset += 0.025;
                    }

                    if (currentGamepad2.a && !previousGamepad2.x) {
                        inOutTake.turretIntakeOffset -= 8;
                    }
                    if (currentGamepad2.b && !previousGamepad2.b) {
                        inOutTake.turretIntakeOffset += 8;
                    }

                    if (gamepad1.dpad_left) {
                        clawAndV4B.claw.setPosition(0.9);
                    }
                    if (gamepad1.dpad_left) {
                        clawAndV4B.claw.setPosition(0.5);
                    }

                    //cycle
                    if (currentGamepad2.b && !previousGamepad2.b) {
                        //intake\
                        if (cycleCheck == 0) {
                            if (closedToIntakeCheck == 1) {
                                inOutTake.setVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                                inOutTake.setInstructions(IntakeAndOuttake.Instructions.INTAKE);
                                inOutTake.setSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSED_TO_INTAKE);
                                closedToIntakeCheck = 0;
                                cycleCheck = 1;
                            } else {
                                inOutTake.setVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                                inOutTake.setInstructions(IntakeAndOuttake.Instructions.INTAKE);
                                inOutTake.setSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);
                                cycleCheck = 1;
                            }
                        }
                    }


                    break;
                case AUTONOMOUS_CONTROL:
                    if (!driver.drive.isBusy()) {
                        currentMode = Mode.DRIVER_CONTROL;
                    }
            }
            telemetry.addData("VerticalTargetPos:", inOutTake.verticalTargetPos);
            telemetry.addData("VerticalCurrentPos:", verticalSlides.liftMotor1.getCurrentPosition());
            telemetry.addData("ServoPos:", clawAndV4B.v4b.getPosition());
            telemetry.addData("currentVerticalPos:");
            telemetry.update();
        }
    }
}