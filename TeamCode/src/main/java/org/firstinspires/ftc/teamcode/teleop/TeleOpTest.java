
package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@TeleOp (name = "TeleOp", group = "Concept")
//@Disabled
public class TeleOpTest extends LinearOpMode {

    enum Mode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL,
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    TeleOpFieldCentric driver;
    Outtake outtake;

    @Override
    public void runOpMode() {
        TeleOpFieldCentric driver = new TeleOpFieldCentric(hardwareMap, new SampleMecanumDrive(hardwareMap), gamepad1);
        driver.drive.setPoseEstimate(new Pose2d(0, 0));

        outtake = new Outtake(hardwareMap, new Turret(gamepad1, hardwareMap), new Claw(gamepad1, hardwareMap),
                new V4B(gamepad1, hardwareMap), new HorizontalSlides(gamepad1, hardwareMap), new VerticalSlides(gamepad1, hardwareMap));

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

            //To do: Distance Sensor

            telemetry.addData("Battery Status", )
            telemetry.addData("X: ", poseEstimate.getX());
            telemetry.addData("Y: ", poseEstimate.getY());
            telemetry.addData("Robot Heading: ", poseEstimate.getHeading());
            telemetry.addData("Turret Rotation: ", "Min: -180; Current: " + outtake.turret.ticks / outtake.turret.ticks_in_degrees + "; Max: 180");
            telemetry.addData("Vertical Extension (Left): ", outtake.verticalSlides.left_position);
            telemetry.addData("Vertical Extension (Right): ", outtake.verticalSlides.right_position);
            telemetry.addData("Horizontal Extension: ", outtake.horizontalSlides.position);
            telemetry.addData("Claw Position: ", "Min: 0; Current: " + outtake.claw.position + "; Max: 0.8");
            telemetry.addData("V4B Position (Left): ", outtake.v4b.left_position);
            telemetry.addData("V4B Position (Right): ", outtake.v4b.right_position);


            outtake.update();
        }

        telemetry.addData(">", "Done");
        telemetry.update();



    }
}