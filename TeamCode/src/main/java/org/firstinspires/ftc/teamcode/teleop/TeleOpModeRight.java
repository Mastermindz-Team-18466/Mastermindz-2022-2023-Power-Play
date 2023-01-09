package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "TeleOp", group = "Concept")
//@Disabled
public class TeleOpModeRight extends LinearOpMode {

    TeleOpFieldCentric driver;
    Outtake outtake;

    @Override
    public void runOpMode() {
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);
        driver.drive.setPoseEstimate(new Pose2d(0, 0));

        outtake = new Outtake(hardwareMap, new Turret(false, gamepad2, hardwareMap), new Claw(gamepad2, hardwareMap), new V4B(gamepad2, hardwareMap), new HorizontalSlides(false, gamepad2, hardwareMap), new VerticalSlides(gamepad2, hardwareMap));

        outtake.setOuttakePos(Outtake.outtakePosEnum.NEUTRAL);
        outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.CLOSE_CLAW);

        waitForStart();

        while (opModeIsActive()) {
            driver.drive.update();
            Pose2d poseEstimate = driver.drive.getPoseEstimate();

            driver.move();

            if (gamepad2.x) {
                outtake.setOuttakePos(Outtake.outtakePosEnum.OPEN_CLAW);
                outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.OPEN_CLAW);
            } else if (gamepad2.b) {
                outtake.setOuttakePos(Outtake.outtakePosEnum.CLOSE_CLAW);
                outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.CLOSE_CLAW);
            } else if (gamepad2.y) {
                outtake.setOuttakePos(Outtake.outtakePosEnum.GRAB_CLAW);
                outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.TURN_TURRET);
            } else if (gamepad2.right_bumper) {
                outtake.setOuttakePos(Outtake.outtakePosEnum.PLACE_ON_POLE);
                outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.TURN_TURRET);
            }

            telemetry.addData("X: ", poseEstimate.getX());
            telemetry.addData("Y: ", poseEstimate.getY());
            telemetry.addData("Robot Heading: ", poseEstimate.getHeading());
            telemetry.addData("Turret Rotation: ", "Min: -180; Current: " + Turret.ticks / Turret.ticks_in_degrees + "; Max: 180");
            telemetry.addData("Vertical Extension (Left): ", VerticalSlides.left_position);
            telemetry.addData("Vertical Extension (Right): ", VerticalSlides.right_position);
            telemetry.addData("Horizontal Extension: ", HorizontalSlides.position);
            telemetry.addData("Claw Position: ", "Min: 0; Current: " + Claw.position + "; Max: 0.8");
            telemetry.addData("V4B Position (Left): ", V4B.left_position);
            telemetry.addData("V4B Position (Right): ", V4B.right_position);


            outtake.update();

            outtake.verticalSlides.left_linear_slide.setPower(gamepad2.left_stick_y);
            outtake.verticalSlides.right_linear_slide.setPower(gamepad2.left_stick_y);
            outtake.turret.turret_motor.setPower(gamepad2.left_stick_x);

            while (gamepad2.dpad_left) {
                double horizontal_servo_left_position = Math.max(0.27, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                double horizontal_servo_right_position = Math.max(0.37, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                outtake.horizontalSlides.left_servo.setPosition(horizontal_servo_left_position);
                outtake.horizontalSlides.right_servo.setPosition(horizontal_servo_right_position);
            }

            while (gamepad2.dpad_right) {
                double horizontal_servo_left_position = Math.min(0.72, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                double horizontal_servo_right_position = Math.min(0.82, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                outtake.horizontalSlides.left_servo.setPosition(horizontal_servo_left_position);
                outtake.horizontalSlides.right_servo.setPosition(horizontal_servo_right_position);
            }

            while (gamepad2.dpad_up) {
                double v4b_left_position = Math.min(0.8, outtake.v4b.left.getPosition() + 0.05);
                double v4b_right_position = Math.min(0.8, outtake.v4b.right.getPosition() + 0.05);
                outtake.v4b.left.setPosition(v4b_left_position);
                outtake.v4b.right.setPosition(v4b_right_position);
            }

            while (gamepad2.dpad_down) {
                double v4b_left_position = Math.max(0, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                double v4b_right_position = Math.max(0, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                outtake.v4b.left.setPosition(v4b_left_position);
                outtake.v4b.right.setPosition(v4b_right_position);
            }
        }

        telemetry.addData(">", "Done");
        telemetry.update();


    }
}