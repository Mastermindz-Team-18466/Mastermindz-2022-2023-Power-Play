package org.firstinspires.ftc.teamcode.newTeleOp;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class newTurret {
    public static final double ticks_in_degrees = 2403.125/360;
    private static final double Kp = 0.0041;
    private static final double Ki = 0.000002;
    private static final double Kd = 0.000179;
    private static final double Kf = 0.00001;


    public DcMotorEx turretMotor;

    private PIDFController controller;

    public newTurret(HardwareMap hardwareMap) {
        controller = new PIDController(Kp, Ki, Kd);

        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor");

    }

    public void set(double targetPosition) {

        if (targetPosition >= 1201) {
            targetPosition = 1201;
        }
        if (targetPosition <= -1201) {
            targetPosition = -1201;
        }

        double pid = controller.calculate(turretMotor.getCurrentPosition(), targetPosition);

        double power = pid + Kf;

        Range.clip(power, -0.4, 0.4);

        turretMotor.setPower(power);
    }
}