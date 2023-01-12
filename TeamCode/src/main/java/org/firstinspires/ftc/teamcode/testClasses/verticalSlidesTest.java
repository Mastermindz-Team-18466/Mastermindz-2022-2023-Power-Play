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

import java.util.Arrays;
import java.util.List;

@TeleOp
@Config
public class verticalSlidesTest extends OpMode {
    private PIDController controller;

    public static double p = 0.01, i = 0, d = 0.0001;
    public static double f = 0.01;

    public static double targetPosition = 0;

    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

//        liftMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        double slidePos = liftMotor1.getCurrentPosition();

        double pid = controller.calculate(slidePos, targetPosition);

        double power = pid + f;

        liftMotor1.setPower(-power);
        liftMotor2.setPower(-power);

        telemetry.addData("targetPos", targetPosition);
        telemetry.addData("currentPos", slidePos);
        telemetry.update();
    }
}
