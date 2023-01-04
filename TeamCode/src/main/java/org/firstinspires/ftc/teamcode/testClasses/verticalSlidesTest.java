package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;
import java.util.List;

public class verticalSlidesTest extends OpMode {
    private PIDController controller;
    public static double kp = 0.1;

    public static double target = 0;
    public final double ticks_in_degrees = 769 / 720;

    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        move();
    }

    public void setLiftMotorPower(double power) {
        liftMotor1.setPower(power);
        liftMotor2.setPower(power);
    }

    public void stop() {
        liftMotor1.setPower(0);
        liftMotor2.setPower(0);
    }

    public List<Integer> getCurrentPosition() {
        return Arrays.asList(liftMotor1.getCurrentPosition(), liftMotor2.getCurrentPosition());
    }

    public void move() {
        double averagePosition = getPosition();
        double p = kp * (target - averagePosition);
        System.out.println(averagePosition);
        setLiftMotorPower(p);
    }

    public double getPosition() {
        return (getCurrentPosition().get(0) + (getCurrentPosition().get(1) * -1)) / 2 / 19.5;
    }
}