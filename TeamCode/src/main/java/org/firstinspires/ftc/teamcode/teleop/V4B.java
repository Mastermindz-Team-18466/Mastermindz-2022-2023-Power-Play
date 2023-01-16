package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

// This class is responsible for controlling the V4B of the robot. It uses a Servo to control the left and right V4B.
public class V4B {
    // Position of the V4B
    public static double position;

    // Servo for the left V4B
    Servo v4b;

    // Gamepad for input
    Gamepad gamepad;

    public V4B(Gamepad gamepad, HardwareMap hardwareMap) {
        // Initialize gamepad
        this.gamepad = gamepad;

        // Initialize V4B servo
        v4b = hardwareMap.get(Servo.class, "v4b_left");
    }

    // Method to control the V4B based on the given state
    public void control(State state) {
        // If the state is "EXTEND", set the position of the V4B to 0.8
        if (state == State.EXTEND) {
            v4b.setPosition(0.8);
        }

        // If the state is "RETRACT", set the position of the V4B to 1
        if (state == State.RETRACT) {
            v4b.setPosition(1);
        }

        // Update the position of the V4B
        position = v4b.getPosition();
    }

    public enum State {
        EXTEND,
        RETRACT
    }
}
