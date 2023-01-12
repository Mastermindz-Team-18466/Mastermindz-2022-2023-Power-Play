package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class V4B {
    public static double left_position;
    public static double right_position;
    Servo left;
    Gamepad gamepad;

    public V4B(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;

        left = hardwareMap.get(Servo.class, "v4b_left");
    }

    public void control(State state) {
        if (state == State.EXTEND) {
            left.setPosition(0.8);
        }
        if (state == State.RETRACT) {
            left.setPosition(1);
        }

        left_position = left.getPosition();
    }

    public enum State {
        EXTEND,
        RETRACT
    }
}
