package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Arrays;
import java.util.List;

public class HorizontalSlides {
    DcMotor left_servo, right_servo;
    Gamepad gamepad;
    public static double kp = 0.1;

    public enum State {
        RETRACTED,
        EXTENDED
    }

    int targetPosition = 0;

    public HorizontalSlides(Gamepad gamepad, HardwareMap hardwareMap) {
        this.gamepad = gamepad;
    }
}
