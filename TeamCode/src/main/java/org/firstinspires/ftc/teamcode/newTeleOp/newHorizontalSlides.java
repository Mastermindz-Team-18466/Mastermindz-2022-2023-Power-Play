package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class newHorizontalSlides {
    public static double offset = 0.1;
    private Servo rightServo;
    private Servo leftServo;

    public newHorizontalSlides(HardwareMap hardwareMap) {
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo.setDirection(Servo.Direction.REVERSE);
    }

    public void set(double targetPosition) {

        if (targetPosition >= 0.72) {
            targetPosition = 0.72;
        }
        if (targetPosition <= 0.27) {
            targetPosition = 0.27;
        }

        rightServo.setPosition(targetPosition);
        leftServo.setPosition(targetPosition + offset);
    }
}