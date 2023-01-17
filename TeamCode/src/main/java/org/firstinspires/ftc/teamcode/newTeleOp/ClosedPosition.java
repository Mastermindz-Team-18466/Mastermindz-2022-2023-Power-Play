package org.firstinspires.ftc.teamcode.newTeleOp;

import com.qualcomm.hardware.rev.RevColorSensorV3;

public class ClosedPosition extends Super {
    public ClosedPosition(newTurret turret, org.firstinspires.ftc.teamcode.newTeleOp.clawAndV4B clawAndV4B, newVerticalSlides verticalSlides, newHorizontalSlides horizontalSlides, RevColorSensorV3 distance) {
        super(turret, clawAndV4B, verticalSlides, horizontalSlides, distance);
    }

    public void update() {
        super.rotateTurret(0);
        super.moveHorizontalSlides(0);
        super.moveVerticalSlides(0);
        super.changeClawPosition(0);
        super.changeV4BPosition(1);
    }
}
