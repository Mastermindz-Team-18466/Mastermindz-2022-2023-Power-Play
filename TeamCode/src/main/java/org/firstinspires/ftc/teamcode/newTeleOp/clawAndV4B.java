package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class clawAndV4B {
    //    RevColorSensorV3 distance;
    Servo claw;
    Servo v4b;

    double aTargetPos;

    public clawAndV4B(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        v4b = hardwareMap.servo.get("v4b_left");
    }

    public void clawControl(double clawTargetPos) {
            claw.setPosition(clawTargetPos);
    }

    public void v4bTargetPos(double targetPos) {

        aTargetPos = targetPos;

        if (aTargetPos > 0.72) {
            aTargetPos = 0.72;
        }
        if (aTargetPos < 0.27) {
            aTargetPos = 0.27;
        }

        v4b.setPosition(aTargetPos);
    }
}