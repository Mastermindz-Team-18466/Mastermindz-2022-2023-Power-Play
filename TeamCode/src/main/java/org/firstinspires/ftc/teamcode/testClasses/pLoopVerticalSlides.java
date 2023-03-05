package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@Disabled
@TeleOp
public class pLoopVerticalSlides extends OpMode {
    public static double kp = 0.0008, ki = 0, kd = 0.00001;
    public static double f = 0;
    public static double target = 0;
    public final double ticks_in_degrees = 769 / 720;
    private PIDController controller, controller1;
    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    @Override
    public void init() {
        controller = new PIDController(kp, ki, kd);
        controller1 = new PIDController(kp, ki, kd);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");

        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(kp, ki, kd);
        controller1.setPID(kp, ki, kd);

        int pos1 = liftMotor1.getCurrentPosition();
        int pos2 = liftMotor2.getCurrentPosition();

        double pid1 = controller.calculate(pos1, target);
        double pid2 = controller1.calculate(pos2, target);

        double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

        double power1 = pid1 + ff;
        double power2 = pid2 + ff;

        liftMotor1.setPower(power1);
        liftMotor2.setPower(power2);

        telemetry.addData("pos1", pos1);
        telemetry.addData("target", target);
        telemetry.addData("pos2", liftMotor2.getCurrentPosition());
        telemetry.addData("p", power1);
        telemetry.update();
    }
}
