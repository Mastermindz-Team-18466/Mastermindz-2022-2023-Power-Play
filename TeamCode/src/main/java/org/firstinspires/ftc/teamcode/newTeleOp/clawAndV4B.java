package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class clawAndV4B {
    //    RevColorSensorV3 distance;
    Servo claw;
    Servo v4b;

    public clawAndV4B(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        v4b = hardwareMap.servo.get("v4b_left");
    }

    public void clawControl(double clawTargetPos) {
            claw.setPosition(clawTargetPos);
    }

    public void v4bTargetPos(double targetPos) {
        if (targetPos > 0.72) {
            targetPos = 0.72;
        }
        if (targetPos < 0.27) {
            targetPos = 0.27;
        }

        v4b.setPosition(targetPos);
    }
}