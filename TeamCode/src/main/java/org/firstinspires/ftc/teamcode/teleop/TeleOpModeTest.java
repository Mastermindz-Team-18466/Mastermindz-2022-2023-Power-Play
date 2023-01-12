package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "TeleOpTest", group = "Concept")
//@Disabled
public class TeleOpModeTest extends LinearOpMode {

    TeleOpFieldCentric driver;
    Outtake outtake;

    @Override
    public void runOpMode() {
        driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);
        driver.drive.setPoseEstimate(new Pose2d(1.5 * 23.5, -3 * 23.5, Math.toRadians(-90)));

        boolean left = Storage.getLeft();

        outtake = new Outtake(hardwareMap, new Turret(gamepad2, hardwareMap), new Claw(gamepad2, hardwareMap), new V4B(gamepad2, hardwareMap), new HorizontalSlides(gamepad2, hardwareMap), new VerticalSlides(gamepad2, hardwareMap));

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
            } else if (gamepad2.left_bumper) {
                outtake.setOuttakePos(Outtake.outtakePosEnum.NEUTRAL);
                outtake.setOuttakeInstructions(Outtake.outtakeInstructionsEnum.CLOSE_CLAW);
            }

            telemetry.addData("X: ", poseEstimate.getX());
            telemetry.addData("Y: ", poseEstimate.getY());
            telemetry.addData("Robot Heading: ", poseEstimate.getHeading());
            telemetry.addData("Turret Rotation: ", outtake.turret.ticks);
            telemetry.addData("Turret Rotation: ", "Min: -180; Current: " + outtake.turret.ticks / outtake.turret.ticks_in_degrees + "; Max: 180");
            telemetry.addData("Vertical Extension (Left): ", outtake.verticalSlides.left_position);
            telemetry.addData("Vertical Extension (Right): ", outtake.verticalSlides.right_position);
            telemetry.addData("Vertical Extension (Right): ", outtake.verticalSlides.power);
            telemetry.addData("Horizontal Extension: ", outtake.horizontalSlides.position);
            telemetry.addData("Horizontal Extension: ", outtake.horizontalSlides.ranged_d);
            telemetry.addData("Claw Position: ", "Min: 0; Current: " + outtake.claw.position + "; Max: 0.8");
            telemetry.addData("V4B Position (Left): ", outtake.v4b.left_position);
            telemetry.addData("V4B Position (Right): ", outtake.v4b.right_position);

            telemetry.update();


            outtake.update();

            outtake.verticalSlides.left_linear_slide.setPower(gamepad2.left_stick_y);
            outtake.verticalSlides.right_linear_slide.setPower(gamepad2.left_stick_y);
            outtake.turret.turret_motor.setPower(gamepad2.left_stick_x);

            while (gamepad2.dpad_left) {
                double horizontal_servo_left_position = Math.max(0.37, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                double horizontal_servo_right_position = Math.max(0.27, outtake.horizontalSlides.left_servo.getPosition() - 0.05);
                outtake.horizontalSlides.left_servo.setPosition(horizontal_servo_left_position);
                outtake.horizontalSlides.right_servo.setPosition(horizontal_servo_right_position);
            }

            while (gamepad2.dpad_right) {
                double horizontal_servo_left_position = Math.min(0.82, outtake.horizontalSlides.left_servo.getPosition() + 0.05);
                double horizontal_servo_right_position = Math.min(0.72, outtake.horizontalSlides.left_servo.getPosition() + 0.05);
                outtake.horizontalSlides.left_servo.setPosition(horizontal_servo_left_position);
                outtake.horizontalSlides.right_servo.setPosition(horizontal_servo_right_position);
            }

            while (gamepad2.dpad_up) {
                double v4b_left_position = Math.min(0.5, outtake.v4b.left.getPosition() + 0.05);
                outtake.v4b.left.setPosition(v4b_left_position);
            }

            while (gamepad2.dpad_down) {
                double v4b_left_position = Math.max(0, outtake.v4b.left.getPosition() - 0.05);
                outtake.v4b.left.setPosition(v4b_left_position);
            }
        }

        telemetry.addData(">", "Done");
        telemetry.update();


    }
}