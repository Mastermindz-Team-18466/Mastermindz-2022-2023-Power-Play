package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class clawAndArm {
    //    RevColorSensorV3 distance;
    Servo claw;
    Servo clawSpin;
    Servo armRight;
    Servo armLeft;

    double aTargetPos;
    double aClawTargetPos;

    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50 / 6;     // period of each cycle
    public static double MAX_POS = 0.7;     // Maximum rotational position
    public static double MIN_POS = 0.05;

    double position = (MAX_POS + MIN_POS) / 2;
    double prevAction = System.currentTimeMillis();
    boolean done = false;

    public clawAndArm(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        clawSpin = hardwareMap.get(Servo.class, "clawSpin");
        armLeft = hardwareMap.servo.get("armLeft");
        armRight = hardwareMap.servo.get("armRight");

        claw.setDirection(Servo.Direction.REVERSE);
        armRight.setDirection(Servo.Direction.REVERSE);
    }

    public void clawControl(double clawTargetPos) {

        aClawTargetPos = clawTargetPos;


        if (aClawTargetPos >= 0.5) {
            aClawTargetPos = 0.5;
        }
        if (aClawTargetPos < 0.2) {
            aClawTargetPos = 0.2;
        }

        aClawTargetPos = 0.1 + ((aClawTargetPos - 0.2) * (0.5)) / (0.3);

        claw.setPosition(aClawTargetPos);
    }

    public void armTargetPos(double targetPos) {

        aTargetPos = targetPos;

        if (aTargetPos > 0.75) {
            aTargetPos = 0.75;
        }

        aTargetPos = 0.56 + ((aTargetPos - 0.1) * (-0.56)) / (0.635);

        if (newHorizontalSlides.auto) {
            if (System.currentTimeMillis() - prevAction > 50 / 3 / 2.5 / 4) {
                prevAction = System.currentTimeMillis();
                if (position < aTargetPos - 0.1) {
                    position += 0.0165;
                } else if (position > aTargetPos + 0.1) {
                    position -= 0.0165;
                } else {
                    position = aTargetPos;
                }
            }
        } else {
            position = aTargetPos;
        }

        armRight.setPosition(position);
        armLeft.setPosition(position);
    }

    public void clawSpin(double clawSpinPos) {
        if (clawSpinPos == 0) {
            clawSpinPos = 0.02;
        } else if (clawSpinPos == 1) {
            clawSpinPos = 0.7;
        }
        clawSpin.setPosition(clawSpinPos);
    }
}