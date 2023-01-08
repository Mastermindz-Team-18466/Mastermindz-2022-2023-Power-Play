package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class V4B {
    public static double left_position;
    public static double right_position;
    Servo left;
    Servo right;
    Gamepad gamepad;

    public V4B(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;

        left = hardwareMap.get(Servo.class, "v4b_left");
        right = hardwareMap.get(Servo.class, "v4b_right");
    }

    public void control(State state) {
        if (state == State.EXTEND) {
            left.setPosition(0.8);
            right.setPosition(0.8);
        }
        if (state == State.RETRACT) {
            left.setPosition(0);
            right.setPosition(0);
        }

        left_position = left.getPosition();
        right_position = right.getPosition();
    }

    public enum State {
        EXTEND,
        RETRACT
    }
}
