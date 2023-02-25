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

    public clawAndArm(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        clawSpin = hardwareMap.get(Servo.class, "clawSpin");
        armLeft = hardwareMap.servo.get("armLeft");
        armRight = hardwareMap.servo.get("armRight");

        armRight.setDirection(Servo.Direction.REVERSE);
    }

    public void clawControl(double clawTargetPos) {
            claw.setPosition(clawTargetPos);
    }

    public void armTargetPos(double targetPos) {

        aTargetPos = targetPos;

//        if (aTargetPos > 0.72) {
//            aTargetPos = 0.72;
//        }
//        if (aTargetPos < 0.27) {
//            aTargetPos = 0.27;
//        }

        armLeft.setPosition(aTargetPos);
        armRight.setPosition(aTargetPos);
    }

    public void clawSpin(double clawSpinPos){
        clawSpin.setPosition(clawSpinPos);
    }
}