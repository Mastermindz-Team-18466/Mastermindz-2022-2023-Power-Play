package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class newHorizontalSlides {
    public static double offset = 0;
    private Servo rightServo;
    private Servo leftServo;
    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;

    public double TURNING_POINT = 0.85;
    double position;
    double prevAction = System.currentTimeMillis();
    public static boolean auto = true;


    public newHorizontalSlides(HardwareMap hardwareMap) {
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo.setDirection(Servo.Direction.REVERSE);
    }

    public void set(double targetPosition) {

        double position = rightServo.getPosition();

        if (targetPosition >= 0.51) {
            targetPosition = 0.51;
        }
        if (targetPosition <= 0) {
            targetPosition = 0;
        }

        targetPosition = 0.5 + ((targetPosition - 0.05) * (0.5)) / (0.46);

        if (auto) {
            if (System.currentTimeMillis() - prevAction > 50 / 3 / 1.5) {
                prevAction = System.currentTimeMillis();
                if (position < targetPosition - 0.05) {
                    position += 0.01;
                } else if (position > targetPosition + 0.05) {
                    position -= 0.01;
                } else {
                    position = targetPosition;
                }
            }
        } else {
            position = targetPosition;
        }


        rightServo.setPosition(position);
        leftServo.setPosition(position);
    }
}