package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class gamepadVertTest extends LinearOpMode {

    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2, liftMotorTop;

    @Override
    public void runOpMode() throws InterruptedException {
        liftMotor1 = hardwareMap.get(DcMotorEx.class, "leftLinear_slide");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "rightLinear_slide");
        liftMotorTop = hardwareMap.get(DcMotorEx.class, "topLinear_slide");

        liftMotorTop.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            liftMotor1.setPower(gamepad1.left_stick_y);
            liftMotor2.setPower(gamepad1.left_stick_y);
            liftMotorTop.setPower(gamepad1.left_stick_y);
        }
    }
}
