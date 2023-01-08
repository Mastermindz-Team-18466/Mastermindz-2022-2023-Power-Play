package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalSlides {
    public static double kp = 0.0008, ki = 0, kd = 0.00001;
    public static double f = 0;
    public static double left_position;
    public static double right_position;
    public PIDFController controller;
    DcMotor left_linear_slide, right_linear_slide;
    Gamepad gamepad;
    int targetPosition = 0;

    public VerticalSlides(Gamepad gamepad, HardwareMap hardwareMap) {
        left_linear_slide = hardwareMap.get(DcMotor.class, "leftLinear_slide");
        left_linear_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_linear_slide.setDirection(DcMotorSimple.Direction.FORWARD);

        //Right Linear Slide
        right_linear_slide = hardwareMap.get(DcMotor.class, "rightLinear_slide");
        right_linear_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_linear_slide.setDirection(DcMotorSimple.Direction.REVERSE);

        this.gamepad = gamepad;
    }

    public void control(State state) {
        if (state == State.BOTTOM) {
            targetPosition = 0;
            loop(targetPosition);
        } else if (state == State.LOW) {
            targetPosition = 1250;
            loop(targetPosition);
        } else if (state == State.MID) {
            targetPosition = 2500;
            loop(targetPosition);
        } else if (state == State.HIGH) {
            targetPosition = 5000;
            loop(targetPosition);
        }
    }

    public void loop(double targetPosition) {
        controller.setPIDF(kp, ki, kd, f);

        double pos = left_linear_slide.getCurrentPosition();

        double pid = controller.calculate(pos, targetPosition);

        double power = pid;

        left_position = left_linear_slide.getCurrentPosition();
        right_position = right_linear_slide.getCurrentPosition();

        left_linear_slide.setPower(power);
        right_linear_slide.setPower(power);

        if (left_linear_slide.getCurrentPosition() >= targetPosition - 50 && left_linear_slide.getCurrentPosition() <= targetPosition + 50) {
            left_linear_slide.setPower(0);
            right_linear_slide.setPower(0);
        } else {
            loop(targetPosition);
        }
    }

    public enum State {
        BOTTOM,
        LOW,
        MID,
        HIGH
    }
}
