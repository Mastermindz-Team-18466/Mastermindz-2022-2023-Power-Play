package org.firstinspires.ftc.teamcode.testClasses;

//starting position is 80

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
@Config
public class verticalSlidesTest extends OpMode {
    public static double kp = 0.0008, ki = 0, kd = 0.00001;
    public static double f = 0;
    public static double target = 0;
    public final double ticks_in_degrees = 769 / 720;
    private PIDFController controller;
    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    @Override
    public void init() {
        controller = new PIDFController(kp, ki, kd, f);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPIDF(kp, ki, kd, f);

        int pos1 = liftMotor2.getCurrentPosition();

        double pid1 = controller.calculate(pos1, target);

        double power1 = pid1;

        liftMotor1.setPower(power1);
        liftMotor2.setPower(power1);

        telemetry.addData("pos1", pos1);
        telemetry.addData("target", target);
        telemetry.addData("pos2", liftMotor2.getCurrentPosition());
        telemetry.addData("p", power1);
        telemetry.update();
    }
}