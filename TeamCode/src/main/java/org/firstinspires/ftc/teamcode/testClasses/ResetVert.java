package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class ResetVert extends LinearOpMode {

    DcMotorEx motor1, motor2;

    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");

        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        motor2.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();

        while (opModeIsActive()) {

        }
    }
}
