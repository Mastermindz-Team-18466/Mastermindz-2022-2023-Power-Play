package org.firstinspires.ftc.teamcode.testClasses;

//starting position is 80

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;
import java.util.List;

@TeleOp
@Config
public class turretTest extends OpMode {
    private PIDController controller;

    public static double p = 0.0041, i = 0.000002, d = 0.000179;
    public static double f = 0.00001;

    public static double targetPosition = 0;

    private DcMotorEx turret;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        turret = hardwareMap.get(DcMotorEx.class, "turretMotor");

        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        double slidePos = turret.getCurrentPosition();

        double pid = controller.calculate(slidePos, targetPosition);

        double power = pid + f;

        Range.clip(power, -0.4, 0.4);

        turret.setPower(power);

        telemetry.addData("targetPos", targetPosition);
        telemetry.addData("currentPos", slidePos);
        telemetry.addData("power", power);
        telemetry.update();
    }
}