package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
@Config
@Disabled
public class turretGamepad extends LinearOpMode {
//
    private DcMotorEx turret;
//
    @Override
    public void runOpMode() throws InterruptedException {
        turret = hardwareMap.get(DcMotorEx.class, "turretMotor");
//
        waitForStart();
        while (opModeIsActive()) {
            turret.setPower(gamepad1.left_stick_x);
        }
    }
}
