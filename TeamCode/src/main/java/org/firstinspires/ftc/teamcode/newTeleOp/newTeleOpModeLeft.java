package org.firstinspires.ftc.teamcode.newTeleOp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.newAuto.Trajectories;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.newTeleOp.TeleOpFieldCentric;

@TeleOp(name = "*newTeleOpModeLeft", group = "Concept")
//@Disabled
public class newTeleOpModeLeft extends LinearOpMode {

    enum Mode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL,
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    TeleOpFieldCentric driver;
    Trajectories trajectories;

    IntakeAndOuttake inOutTake;
    newTurret turret;
    clawAndArm clawAndArm;
    newVerticalSlides verticalSlides;
    newHorizontalSlides horizontalSlides;


    int closedToIntakeCheck = 1;
    int cycleCheck = 0;

    @Override
    public void runOpMode() {
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);

        turret = new newTurret(hardwareMap);
        clawAndArm = new clawAndArm(hardwareMap);
        verticalSlides = new newVerticalSlides(hardwareMap);
        horizontalSlides = new newHorizontalSlides(hardwareMap);
        turret.turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        verticalSlides.liftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        verticalSlides.liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        verticalSlides.liftMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//        verticalSlides.liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        verticalSlides.liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        inOutTake = new IntakeAndOuttake(turret, clawAndArm, verticalSlides, horizontalSlides);

        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);


        driver.drive.setPoseEstimate(PoseStorage.currentPose);

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();


        while (!isStarted() && !isStopRequested() && !opModeIsActive()) {
            inOutTake.update();
        }


        waitForStart();

        while (opModeIsActive()) {
            driver.drive.update();
            inOutTake.update();

            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);

            previousGamepad2.copy(currentGamepad2);
            currentGamepad2.copy(gamepad2);

            Pose2d poseEstimate = driver.drive.getPoseEstimate();

            switch (currentMode) {
                case DRIVER_CONTROL:
                    driver.move();
//                    if (gamepad1.b || gamepad1.x) {
//                        currentMode = Mode.AUTONOMOUS_CONTROL;
//                    }

                    if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_INTAKE) {
                            inOutTake.horizontalIntakeOffset += 0.02;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_DEPOSIT) {
                            inOutTake.horizontalOuttakeOffset += 0.02;
                        }
                    }

                    if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_INTAKE) {
                            inOutTake.horizontalIntakeOffset -= 0.02;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_DEPOSIT) {
                            inOutTake.horizontalOuttakeOffset -= 0.02;
                        }
                    }

                    if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_DEPOSIT) {
                            inOutTake.verticalOuttakeOffset += 35;
                        }
                    }

                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_DEPOSIT) {
                            inOutTake.verticalOuttakeOffset -= 35;
                        }
                    }

                    if (currentGamepad1.b && !previousGamepad1.b) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_INTAKE) {
                            inOutTake.turretIntakeOffset -= 20;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_DEPOSIT) {
                            inOutTake.turretOuttakeOffset -= 20;
                        }
                    }
                    if (currentGamepad1.x && !previousGamepad1.x) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_INTAKE) {
                            inOutTake.turretIntakeOffset += 20;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.LEFT_DEPOSIT) {
                            inOutTake.turretOuttakeOffset += 20;
                        }
                    }

                    if (currentGamepad1.y && !previousGamepad1.y) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);
                        cycleCheck = 0;
                        closedToIntakeCheck = 1;

                    }

                    //cycle
                    if (currentGamepad1.a && !previousGamepad1.a) {
                        //intake
                        if (cycleCheck == 0) {
                            if (closedToIntakeCheck == 1) {
                                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.LEFT_INTAKE);
                                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSED_TO_INTAKE);
                                closedToIntakeCheck = 0;
                                cycleCheck = 1;
                            } else {
                                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.LEFT_INTAKE);
                                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);
                                cycleCheck = 1;
                            }
                        } else if (cycleCheck == 1) {
                            inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                            inOutTake.setaInstructions(IntakeAndOuttake.Instructions.LEFT_DEPOSIT);
                            inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                            cycleCheck = 0;
                        }
                    }


                    break;
            }

            telemetry.addData("VerticalTargetPos:", inOutTake.verticalTargetPos);
            telemetry.addData("VerticalCurrentPos:", verticalSlides.liftMotor1.getCurrentPosition());
            telemetry.addData("Power", verticalSlides.publicPower);
            telemetry.addData("ServoPos:", clawAndArm.armRight.getPosition());
            telemetry.update();
        }
    }
}