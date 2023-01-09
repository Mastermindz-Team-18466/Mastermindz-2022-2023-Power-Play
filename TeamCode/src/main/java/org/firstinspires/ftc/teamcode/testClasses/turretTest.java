package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class turretTest extends LinearOpMode {

    private static final double Kp = 0.01;
    private static final double Ki = 0.005;
    private static final double Kd = 0.001;
    private static final double Kf = 0.00;

    private DcMotorEx turretMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor");
        double integral = 0;
        double previousError = 0;

        waitForStart();
        while (opModeIsActive()) {
            double targetPosition = gamepad2.left_stick_y;
            double error  = targetPosition - turretMotor.getCurrentPosition();
            integral = integral + error;
            double derivative = error - previousError;
            previousError = error;

            double power = Kp * error + Ki * integral + Kd * derivative + Kf;

            turretMotor.setPower(power);
        }
    }
}
