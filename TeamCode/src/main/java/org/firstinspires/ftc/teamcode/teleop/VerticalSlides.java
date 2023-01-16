package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalSlides {
    // PIDF controller constants
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
        // Initialize the PID controller
        controller = new PIDController(kp, ki, kd);

        // Initialize the left linear slide motor
        left_linear_slide = hardwareMap.get(DcMotorEx.class, "leftLinear_slide");
        left_linear_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_linear_slide.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initialize the right linear slide motor
        right_linear_slide = hardwareMap.get(DcMotorEx.class, "rightLinear_slide");
        right_linear_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.gamepad = gamepad;
    }

    public void control(State state) {
        // Depending on the state passed as a parameter, set the target position for the linear slides
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
        // Set the PIDF controller constants
        controller.setPID(kp, ki, kd);

        // Get the current position of the left linear slide
        double slidePos = left_linear_slide.getCurrentPosition();

        // Calculate the pid value using the current position and target position
        double pid = controller.calculate(slidePos, targetPosition);

        // Calculate the power to set the motor to by adding the PID output and the feedforward value
        power = pid + f;

        // Set the power to the left and right linear slide motors
        left_linear_slide.setPower(-power);
        right_linear_slide.setPower(-power);

        // Update the current position of the left and right linear slide motors
        left_position = left_linear_slide.getCurrentPosition();
        right_position = right_linear_slide.getCurrentPosition();

        // Check if the slide motors are within 100 ticks of the target position, if so stop the motors
        if (left_linear_slide.getCurrentPosition() >= targetPosition - 100 && left_linear_slide.getCurrentPosition() <= targetPosition + 100) {
            left_linear_slide.setPower(0);
            right_linear_slide.setPower(0);
        } else {
            // If the motors are not within 100 ticks of the target position, continue running the loop
            loop(targetPosition);
        }
    }

    // Enum to define the different states of the vertical slides
    public enum State {
        BOTTOM,
        LOW,
        MID,
        HIGH
    }
}
