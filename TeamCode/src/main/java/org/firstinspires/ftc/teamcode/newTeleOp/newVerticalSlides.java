package org.firstinspires.ftc.teamcode.newTeleOp;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class newVerticalSlides {
    private PIDController controller;

    private static final double p = 0.01, i = 0, d = 0.0001;
    private static final double f = 0.01;

    public DcMotorEx liftMotor1;
    private DcMotorEx liftMotor2;

    public int previousTargetPos;

    public newVerticalSlides(HardwareMap hardwareMap) {
        controller = new PIDController(p, i, d);

        liftMotor1 = hardwareMap.get(DcMotorEx.class, "leftLinear_slide");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "rightLinear_slide");


        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

//        liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        liftMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void set(int targetPosition) {

        if (targetPosition > 3200) {
            targetPosition = 3200;
        }
        if (targetPosition < 0) {
            targetPosition = 0;
        }

        previousTargetPos = targetPosition;

        controller.setPID(p, i, d);
        double slidePos = liftMotor1.getCurrentPosition();

        double pid = controller.calculate(slidePos, targetPosition);

        double power = pid + f;

        liftMotor1.setPower(-power);
        liftMotor2.setPower(-power);
    }
}