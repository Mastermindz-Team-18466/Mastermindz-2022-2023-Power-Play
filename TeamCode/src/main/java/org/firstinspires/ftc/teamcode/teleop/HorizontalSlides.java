package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HorizontalSlides {
    public static double kp = 0.1;
    public static double position;
    DcMotor left_servo, right_servo;
    Gamepad gamepad;
    int targetPosition = 0;

    public HorizontalSlides(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;
    }

    public enum State {
        RETRACTED,
        EXTENDED
    }
}
