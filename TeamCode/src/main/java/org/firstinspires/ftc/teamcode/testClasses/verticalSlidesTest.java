package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp
public class verticalSlidesTest extends OpMode {
    private PIDController controller;

    public static double p = 0.005, i = 0, d = 0.00000001;
    public static double f = 0;

    public static double target = 0;
    public final double ticks_in_degrees = 769 / 720;

    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int pos1 = liftMotor1.getCurrentPosition();
        int pos2 = liftMotor2.getCurrentPosition();

        double pid1 = controller.calculate(pos1, target);
        double pid2 = controller.calculate(pos2, target);

        double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

        double power1 = pid1 + ff;
        double power2 = pid2 + ff;

        liftMotor1.setPower(power1);
        liftMotor2.setPower(power2);

        telemetry.addData("pos1", pos1);
        telemetry.addData("pos2", pos2);
        telemetry.addData("target", target);
        telemetry.update();

    }
}
