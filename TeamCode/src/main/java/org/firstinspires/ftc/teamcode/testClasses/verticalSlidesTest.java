package org.firstinspires.ftc.teamcode.testClasses;

//starting position is 80

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
@Config
public class verticalSlidesTest extends OpMode {
    private PIDController controller;

    public static final double p = 0.01, i = 0, d = 0;
    public static final double f = 0.00004;

    public static double targetPosition = 0;

    private DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2, liftMotorTop;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "leftLinear_slide");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "rightLinear_slide");
        liftMotorTop = hardwareMap.get(DcMotorEx.class, "topLinear_slide");

        liftMotorTop.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        double slidePos = liftMotor1.getCurrentPosition();

        double pid = controller.calculate(slidePos, targetPosition);

        double power = pid + f;

        liftMotor1.setPower(-power);
        liftMotor2.setPower(-power);
        liftMotorTop.setPower(-power);

        telemetry.addData("targetPos", targetPosition);
        telemetry.addData("currentPos", slidePos);
        telemetry.update();
    }
}
