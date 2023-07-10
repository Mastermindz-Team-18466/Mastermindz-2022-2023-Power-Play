package org.firstinspires.ftc.teamcode.newTeleOp;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class newVerticalSlides {
    private PIDController controller;

    private static final double p = 0.01, i = 0, d = 0;
    private static final double f = 0.00004;

    public DcMotorEx liftMotor1;
    public DcMotorEx liftMotor2;
    public DcMotorEx liftMotorTop;

    public double previousTargetPos;

    public double publicPower;

    public newVerticalSlides(HardwareMap hardwareMap) {
        controller = new PIDController(p, i, d);

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "leftLinear_slide");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "rightLinear_slide");
        liftMotorTop = hardwareMap.get(DcMotorEx.class, "topLinear_slide");


        liftMotorTop.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void set(double targetPosition) {

        if (targetPosition >= 2150) {
            targetPosition = 2150;
        }
        if (targetPosition <= 10) {
            targetPosition = 10;
        }

        previousTargetPos = targetPosition;

        controller.setPID(p, i, d);
        double slidePos = liftMotor1.getCurrentPosition();

        double pid = controller.calculate(slidePos, targetPosition);

        double power = pid + f;

        publicPower = power;

        liftMotor1.setPower(-power);
        liftMotor2.setPower(-power);
        liftMotorTop.setPower(-power);
    }
}