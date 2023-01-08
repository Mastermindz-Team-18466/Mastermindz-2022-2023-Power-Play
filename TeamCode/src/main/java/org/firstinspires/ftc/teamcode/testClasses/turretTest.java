package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class turretTest extends LinearOpMode {

    private DcMotorEx turretMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor");

        waitForStart();
        while (opModeIsActive()) {
            turretMotor.setPower(gamepad1.left_stick_y);
        }
    }
}
