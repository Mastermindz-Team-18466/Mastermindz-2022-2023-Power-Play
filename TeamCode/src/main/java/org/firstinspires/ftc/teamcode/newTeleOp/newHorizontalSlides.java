package org.firstinspires.ftc.teamcode.newTeleOp;

import static android.os.SystemClock.sleep;

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


    public newHorizontalSlides(HardwareMap hardwareMap) {
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo.setDirection(Servo.Direction.REVERSE);
    }

    public void set(double targetPosition) {

        if (targetPosition >= 0.51) {
            targetPosition = 0.51;
        }
        if (targetPosition <= 0) {
            targetPosition = 0;
        }

        targetPosition = 0.6 + ((targetPosition - 0.05) * (0.4)) / (0.46);

        position = TURNING_POINT;
        while (position < targetPosition) {
            position += INCREMENT;
            if (position > targetPosition) {
                position = targetPosition;
            }
            leftServo.setPosition(position);
            rightServo.setPosition(position);
            sleep(CYCLE_MS);

            rightServo.setPosition(targetPosition);
            leftServo.setPosition(targetPosition);
        }
    }
}