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
import org.firstinspires.ftc.teamcode.teleop.TeleOpFieldCentric;

@TeleOp(name = "newTeleOpMode", group = "Conceplt")
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
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);

        turret = new newTurret(hardwareMap);
        clawAndV4B = new clawAndV4B(hardwareMap);
        verticalSlides = new newVerticalSlides(hardwareMap);
        horizontalSlides = new newHorizontalSlides(hardwareMap);
        distance = hardwareMap.get(RevColorSensorV3.class, "Distance");

        turret.turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        verticalSlides.liftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        verticalSlides.liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        verticalSlides.liftMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//        verticalSlides.liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        verticalSlides.liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        inOutTake = new IntakeAndOuttake(turret, clawAndV4B, verticalSlides, horizontalSlides, distance);

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

                    if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE){
                            inOutTake.horizontalIntakeOffset -= 0.025;
                        }
                        else if(inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT){
                            inOutTake.horizontalOuttakeOffset -= 0.025;
                        }
                    }

                    if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.horizontalIntakeOffset += 0.025;
                        }
                        else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.horizontalOuttakeOffset += 0.025;
                        }
                    }

                    if (currentGamepad2.x && !previousGamepad2.x) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.turretIntakeOffset -= 20;
                        }
                        else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.turretOuttakeOffset -= 20;
                        }
                    }
                    if (currentGamepad2.b && !previousGamepad2.b) {
                        if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.INTAKE) {
                            inOutTake.turretIntakeOffset += 20;
                        }
                        else if (inOutTake.aInstructions == IntakeAndOuttake.Instructions.DEPOSIT) {
                            inOutTake.turretOuttakeOffset += 20;
                        }
                    }

                    if (gamepad1.dpad_left) {
                        clawAndV4B.claw.setPosition(0.9);
                    }
                    if (gamepad1.dpad_left) {
                        clawAndV4B.claw.setPosition(0.5);
                    }

                    if (currentGamepad2.y && !previousGamepad2.y) {
                        inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
                        inOutTake.setaInstructions(IntakeAndOuttake.Instructions.CLOSED);
                        inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.INITIAL_CLOSE);
                        cycleCheck = 0;
                        closedToIntakeCheck = 1;

                    }

                    //cycle
                    if (currentGamepad2.a && !previousGamepad2.a) {
                        //intake
                        System.out.println("Inside Loop!");
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
                        }

                        else if (cycleCheck == 1){
//                            inOutTake.setVerticalPos(IntakeAndOuttake.verticalPos.GROUND);
//                            inOutTake.setInstructions(IntakeAndOuttake.Instructions.DEPOSIT);
//                            inOutTake.setSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);

                            inOutTake.setaVerticalPos(IntakeAndOuttake.verticalPos.TOP);
                            inOutTake.setaInstructions(IntakeAndOuttake.Instructions.DEPOSIT);
                            inOutTake.setaSpecificInstruction(IntakeAndOuttake.specificInstructions.CLOSE_CLAW);
                            cycleCheck = 0;
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
            telemetry.update();
        }
    }
}