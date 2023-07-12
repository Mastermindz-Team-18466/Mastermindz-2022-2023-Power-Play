package org.firstinspires.ftc.teamcode.newTeleOp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.newAuto.Trajectories;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

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

    DriveConstants driveConstants;


    int closedToIntakeCheck = 1;
    int cycleCheck = 0;

    @Override
    public void runOpMode() {
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);

        turret = new newTurret(hardwareMap);
        clawAndArm = new clawAndArm(hardwareMap);
        verticalSlides = new newVerticalSlides(hardwareMap);
        horizontalSlides = new newHorizontalSlides(hardwareMap);

        newHorizontalSlides.auto = false;

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

                    if (currentGamepad1.start && !previousGamepad1.start) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.FALLEN_CONE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.SET_TO_POS);
                    }

                    if (currentGamepad1.back && !previousGamepad1.back) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.FALLEN_CONE_PICK);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                    }

                    if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DESCORE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DESCORE_POSE);
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DESCORE) {
                            inOutTake.verticalDescoreOffset += 200;
                        }
                    }

                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DESCORE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DESCORE_POSE);
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DESCORE) {
                            inOutTake.verticalDescoreOffset -= 200;
                        }
                    }

                    if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DESCORE_UP);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                    }

                    if (currentGamepad2.left_stick_button && !previousGamepad2.left_stick_button) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.EXTEND_INTAKE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);
                    }

                    if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.horizontalIntakeOffset += 0.02;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.horizontalOuttakeOffset += 0.02;
                        }
                    }

                    if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.horizontalIntakeOffset -= 0.02;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.horizontalOuttakeOffset -= 0.02;
                        }
                    }

                    if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                        if (inOutTake.aSpecificInstruction == IntakeAndOuttake.specificInstructions.RETRACT_HORIZONTAL_SLIDES || inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.verticalOuttakeOffset += 100;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.TELE_STACK_INTAKE) {
                            inOutTake.teleStackOffset += 100;
                        }
                    }

                    if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                        if (inOutTake.aSpecificInstruction == IntakeAndOuttake.specificInstructions.RETRACT_HORIZONTAL_SLIDES || inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.verticalOuttakeOffset -= 100;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.TELE_STACK_INTAKE) {
                            inOutTake.teleStackOffset -= 100;
                        }
                    }

                    if (currentGamepad2.x && !previousGamepad2.x) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.turretIntakeOffset -= 20;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.turretOuttakeOffset -= 20;
                        }
                    }
                    if (currentGamepad2.b && !previousGamepad2.b) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.turretIntakeOffset += 20;
                        } else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.turretOuttakeOffset += 20;
                        }
                    }

                    if (currentGamepad2.y && !previousGamepad2.y) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);
                        cycleCheck = 0;
                        closedToIntakeCheck = 1;
                    }

                    if (gamepad1.left_trigger > 0.2 || inOutTake.horizontalTargetPos > 0.2) {
                        driver.drive.slowMode = 4.8;
                    } else if ((gamepad1.right_stick_x > 0.05 || gamepad1.right_stick_x < -0.05) && !(gamepad1.right_trigger > 0.5) && !(gamepad1.left_trigger > 0.2)) {
                        driver.drive.slowMode = 2;
                    } else {
                        driver.drive.slowMode = 1;
                    }

                    if (currentGamepad2.back && !previousGamepad2.back) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.BOTTOM);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.RETRACT_HORIZONTAL_SLIDES);
                    }

                    if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.MID);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.RETRACT_HORIZONTAL_SLIDES);
                    }

                    if (currentGamepad2.start && !previousGamepad2.start) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.TOP_STRAIGHT);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.RETRACT_HORIZONTAL_SLIDES);
                    }

                    if (currentGamepad2.left_trigger > .75 && !(previousGamepad2.left_trigger > .75)) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DRIVING);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);
                    }

                    if (currentGamepad2.right_trigger > .75 && !(previousGamepad2.right_trigger > .75)) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.STRAIGHT_DEPOSIT);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);
                    }

                    if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED_INTAKE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);
                    }

                    if (currentGamepad1.guide && !previousGamepad1.guide) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.PARK);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);
                    }

                    if (currentGamepad1.back && !previousGamepad1.back) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.PARK);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);
                    }

                    if (currentGamepad1.b && !previousGamepad1.b) {
                        inOutTake.turretOuttakeOffset = 585;
                    }

                    if (currentGamepad1.x && !previousGamepad1.x) {
                        inOutTake.turretOuttakeOffset = -585;
                    }

                    if (currentGamepad1.a && !previousGamepad1.a) {
                        inOutTake.turretOuttakeOffset = 0;
                    }

                    if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.GROUND_DEPOSIT);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                    }

                    if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.GROUND_INTAKE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                    }

                    if (currentGamepad1.y && !previousGamepad1.y) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.TELE_STACK_INTAKE);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INTAKE_EXTENSION);
                    }

                    if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.TELE_STACK_DEPOSIT);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                        inOutTake.teleStackOffset -= 100;
                    }


                    //cycle
                    if (currentGamepad2.a && !previousGamepad2.a) {
                        //intake
                        if (cycleCheck == 0) {
                            if (closedToIntakeCheck == 1) {
                                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.INTAKE);
                                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSED_TO_INTAKE);
                                closedToIntakeCheck = 0;
                                cycleCheck = 1;
                            } else {
                                inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                                inOutTake.setaInstructions(IntakeAndOuttake.Instructions.INTAKE);
                                inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.DEPOSIT_CONE);
                                cycleCheck = 1;
                            }
                        } else if (cycleCheck == 1) {
                            inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                            inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DEPOSIT);
                            inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                            cycleCheck = 0;
                        }
                    }

                    break;
            }

            telemetry.addData("VerticalTargetPos:", inOutTake.verticalTargetPos);
            telemetry.addData("VerticalCurrentPos:", verticalSlides.liftMotor1.getCurrentPosition());
            telemetry.addData("Power", verticalSlides.publicPower);
//            telemetry.addData("ServoPos:", clawAndArm.armRight.getPosition());
            telemetry.update();
        }
    }
}