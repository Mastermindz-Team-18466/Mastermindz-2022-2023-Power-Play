package org.firstinspires.ftc.teamcode.testClasses;

//starting position is 80

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;
import java.util.List;

public class verticalSlidesTest extends OpMode {
    private PIDController controller;

    public static double kp = 0.0008, ki = 0, kd = 0.00001;
    public static double f = 0;

    public static double target = 0;
    public final double ticks_in_degrees = 769 / 720;

    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    @Override
    public void init() {
        controller = new PIDController(kp, ki, kd);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(kp, ki, kd);

        int pos1 = liftMotor1.getCurrentPosition();

        double pid1 = controller.calculate(pos1, target);

        double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

        double power1 = pid1 + ff;

        liftMotor1.setPower(power1);
        liftMotor2.setPower(power1);

        telemetry.addData("pos1", pos1);
        telemetry.addData("target", target);
        telemetry.addData("pos2", liftMotor2.getCurrentPosition());
        telemetry.addData("p", power1);
        telemetry.update();
    }
}