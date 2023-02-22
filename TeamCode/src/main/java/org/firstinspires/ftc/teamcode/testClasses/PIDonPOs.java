package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name = "pidonposition", group = "Test")
public class PIDonPOs extends LinearOpMode{
    private DcMotor frontLeft;
    private DcMotor rearLeft;
    private DcMotor frontRight;
    private DcMotor rearRight;

    public static double kP = 0.1;
    public static double kD = 0.1;
    public static double kI = 0.05;

    private double error;
    private double prevError;
    private double integral;

    public static double TOLERANCE = 1;
    public static double TARGET_POSITION = 1000;

    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        rearLeft = hardwareMap.get(DcMotor.class, "leftRear");
        rearRight = hardwareMap.get(DcMotor.class, "rightRear");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            error = TARGET_POSITION - frontLeft.getCurrentPosition();

            telemetry.addData("error", error);
            telemetry.addData("pos", frontLeft.getCurrentPosition());
            /*The error is calculated as the difference between the target position and the current position
            of the front left wheel because it is assumed that the front left wheel is the one that will be used
            for control purposes. The purpose of the PID control is to bring the front left wheel to a specific position,
             and the error term represents how far away the wheel is from the desired position.
             By subtracting the target position from the current position of the front left wheel,
             the error term gives an indication of how much correction is needed to bring the wheel to the desired position

             aadi asked why
             */

            double power = kP * error;

            telemetry.addData("p", power);

            frontLeft.setPower(power);
            frontRight.setPower(power);
            rearLeft.setPower(power);
            rearRight.setPower(power);

            prevError = error;
            timer.reset();

            telemetry.update();
        }
    }
}