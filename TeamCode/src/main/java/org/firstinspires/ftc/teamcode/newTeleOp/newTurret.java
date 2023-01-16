package org.firstinspires.ftc.teamcode.newTeleOp;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class newTurret {
    private static final double Kp = 0.02;
    private static final double Ki = 0.00;
    private static final double Kd = 0.00003;
    private static final double Kf = 0.1;

    private double integral = 0;
    private double previousError = 0;

    private DcMotorEx turretMotor;

    private PIDFController controller;

    public newTurret(HardwareMap hardwareMap) {
        controller = new PIDFController(Kp, Ki, Kd, Kf);

        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor");
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void set(int targetPosition) {

        if (targetPosition >= 1201) {
            targetPosition = 1201;
        }
        if (targetPosition <= -1201) {
            targetPosition = -1201;
        }

        double error = targetPosition - turretMotor.getCurrentPosition();
        integral = integral + error;
        double derivative = error - previousError;
        previousError = error;

        double power = Kp * error + Ki * integral + Kd * derivative + Kf;

        turretMotor.setPower(power / 1.5);
    }
}