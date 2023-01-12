package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ElevatorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalSlides {
    public static double kp = 0.01, ki = 0, kd = 0.0001;
    public static double f = 0.01;
    public static double left_position;
    public static double right_position;
    public static double power;
    public PIDController controller;
    DcMotorEx left_linear_slide, right_linear_slide;
    Gamepad gamepad;
    int targetPosition = 0;

    public VerticalSlides(Gamepad gamepad, HardwareMap hardwareMap) {
        controller = new PIDController(kp, ki, kd);
        left_linear_slide = hardwareMap.get(DcMotorEx.class, "leftLinear_slide");
        left_linear_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_linear_slide.setDirection(DcMotorSimple.Direction.REVERSE);

        //Right Linear Slide
        right_linear_slide = hardwareMap.get(DcMotorEx.class, "rightLinear_slide");
        right_linear_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            targetPosition = 3200;
            loop(targetPosition);
        }
    }

    public void loop(double targetPosition) {
        controller.setPID(kp, ki, kd);

        double slidePos = left_linear_slide.getCurrentPosition();

        double pid = controller.calculate(slidePos, targetPosition);

        power = pid + f;

        left_linear_slide.setPower(-power);
        right_linear_slide.setPower(-power);

        left_position = left_linear_slide.getCurrentPosition();
        right_position = right_linear_slide.getCurrentPosition();

        if (left_linear_slide.getCurrentPosition() >= targetPosition - 100 && left_linear_slide.getCurrentPosition() <= targetPosition + 100) {
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
