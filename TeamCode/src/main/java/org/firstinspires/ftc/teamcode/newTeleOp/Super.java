package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.hardware.rev.RevColorSensorV3;

public class Super {
    public newTurret turret;
    public newVerticalSlides verticalSlides;
    public clawAndV4B clawAndV4B;
    public newHorizontalSlides horizontalSlides;
    public RevColorSensorV3 distance;

    private long startTime = System.currentTimeMillis();

    public Super(newTurret turret, clawAndV4B clawAndV4B, newVerticalSlides verticalSlides, newHorizontalSlides horizontalSlides, RevColorSensorV3 distance) {
        this.clawAndV4B = clawAndV4B;
        this.turret = turret;
        this.verticalSlides = verticalSlides;
        this.horizontalSlides = horizontalSlides;
        this.distance = distance;
    }

    public void rotateTurret(double angle) {
        turret.set(angle * turret.ticks_in_degrees);
    }

    public void moveHorizontalSlides(double targetPosition) {
        targetPosition = 0.27 + targetPosition * (0.45);
        horizontalSlides.set(targetPosition);
    }

    public void moveVerticalSlides(double targetPosition) {
        targetPosition = targetPosition * 3200;
        verticalSlides.set(targetPosition);
    }

    public void changeV4BPosition(double targetPosition) {
        targetPosition = 0.27 + targetPosition * (0.45);
        clawAndV4B.v4bTargetPos(targetPosition);
    }

    public void changeClawPosition(double targetPosition) {
        targetPosition = 0.4 + targetPosition * (0.5);
        clawAndV4B.clawControl(targetPosition);
    }
}
