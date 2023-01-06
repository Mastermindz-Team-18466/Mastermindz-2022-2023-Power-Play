package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    Servo claw;
    Gamepad gamepad;
    DistanceSensor distance;

    public enum State {
        OPEN,
        CLOSE
    }

    public Claw(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;

        claw = hardwareMap.get(Servo.class, "claw");
        distance = hardwareMap.get(DistanceSensor.class, "distance");
    }

    public void control(State state) {
        if (state == State.OPEN) {
            claw.setDirection(Servo.Direction.FORWARD);
            claw.setPosition(0.8);
        } if (state == State.CLOSE) {
            claw.setPosition(0);
        }
    }
}
