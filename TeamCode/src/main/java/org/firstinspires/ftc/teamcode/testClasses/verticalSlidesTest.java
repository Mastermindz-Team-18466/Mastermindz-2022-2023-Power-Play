package org.firstinspires.ftc.teamcode.testClasses;

//starting position is 80

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ElevatorFeedforward;
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

    private static final double kS = 0.1; // position error gain
    private static final double kG = 0.05; // velocity error gain
    private static final double kA = 0.01; // acceleration error gain
    private static final double kV = 0.1; // jerk error gain
    private ElevatorFeedforward feedforward;
    private static final double maxForce = 769/740;
    double negativeMaxForce = -maxForce;


    @Override
    public void init() {
        controller = new PIDFController(kp, ki, kd, f);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        feedforward = new ElevatorFeedforward(kS, kG, kA, kV);
    }

    @Override
    public void loop() {
        controller.setPIDF(kp, ki, kd, f);

        int pos1 = liftMotor2.getCurrentPosition();

        double pid1 = controller.calculate(pos1, target);

        double power1 = pid1;

        liftMotor1.setPower(power1);
        liftMotor2.setPower(power1);

        double feedforwardForce = feedforward.calculate(liftMotor2.getVelocity());
        feedforwardForce = Math.max(-maxForce, Math.min(maxForce, feedforwardForce));
        liftMotor1.setPower(feedforwardForce);
        liftMotor2.setPower(feedforwardForce);

        telemetry.addData("pos1", pos1);
        telemetry.addData("target", target);
        telemetry.addData("pos2", liftMotor2.getCurrentPosition());
        telemetry.addData("p", power1);
        telemetry.update();
    }
}