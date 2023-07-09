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

    public clawAndArm(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        clawSpin = hardwareMap.get(Servo.class, "clawSpin");
        armLeft = hardwareMap.servo.get("armLeft");
        armRight = hardwareMap.servo.get("armRight");

        armLeft.setDirection(Servo.Direction.REVERSE);
        armRight.setDirection(Servo.Direction.REVERSE);
    }

    public void clawControl(double clawTargetPos) {

        aClawTargetPos = clawTargetPos;


        if (aClawTargetPos >= 0.5) {
            aClawTargetPos = 0.555;
        }
        if (aClawTargetPos < 0.2) {
            aClawTargetPos = 0.21;
        }

        aClawTargetPos = 0.1 + ((aClawTargetPos - 0.42) * (0.9)) / (0.28);

        claw.setPosition(aClawTargetPos);
    }

    public void armTargetPos(double targetPos) {

        aTargetPos = targetPos;

        if (aTargetPos > 0.75) {
            aTargetPos = 0.75;
        }
        if (aTargetPos < 0.07) {
            aTargetPos = 0.07;
        }

        aTargetPos = 0.05 + ((aTargetPos - 0.07) * (0.65)) / (0.68);

        armLeft.setPosition(aTargetPos);
        armRight.setPosition(aTargetPos);
    }

    public void clawSpin(double clawSpinPos) {
        if (clawSpinPos == 1) {
            clawSpinPos = 0.7;
        }
        clawSpin.setPosition(clawSpinPos);
    }
}