package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

// This class is responsible for controlling the claw of the robot. It uses a servo to open and close the claw.
// It also uses a distance sensor to detect the distance between the claw and an object.
public class Claw {
    public static double position;
    Servo claw;
    Gamepad gamepad;
    DistanceSensor distance;

    // Constructor initializes all the required hardware
    public Claw(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;

        // initialize claw servo
        claw = hardwareMap.get(Servo.class, "claw");

        // initialize distance sensor
        distance = hardwareMap.get(DistanceSensor.class, "Distance");
    }

    public void control(State state) {
        if (state == State.OPEN) {
            // set the direction of the claw servo
            claw.setDirection(Servo.Direction.FORWARD);

            // set the position of the claw servo to open the claw
            claw.setPosition(0.9);
        }
        if (state == State.CLOSE) {
            // set the position of the claw servo to close the claw
            claw.setPosition(0.4);
        }

        // update the position variable
        position = claw.getPosition();
    }

    public enum State {
        OPEN, CLOSE
    }
}
