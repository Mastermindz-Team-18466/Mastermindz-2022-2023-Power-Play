package org.firstinspires.ftc.teamcode.newTeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;

@Config
public class IntakeAndOuttake {
    public newTurret turret;
    public newVerticalSlides verticalSlides;
    public clawAndV4B clawAndV4B;
    public newHorizontalSlides horizontalSlides;
    public RevColorSensorV3 distance;


    public double turretIntakeOffset = 0;
    public double horizontalIntakeOffset = 0;
    public double v4bIntakeOffset = 0;

    public double turretOuttakeOffset = 0;
    public double horizontalOuttakeOffset = 0;
    public double v4bOuttakeOffset = 0;
    public double verticalOuttakeOffset = 0;

    public double turretTargetPos;
    public double verticalTargetPos;
    public double horizontalTargetPos;
    public double v4bTargetPos;
    public double clawTargetPos;

    public verticalPos aVerticalPos;
    public Instructions aInstructions;
    public specificInstructions aSpecificInstruction;

    private long prevAction = System.currentTimeMillis();

    public IntakeAndOuttake(newTurret turret, clawAndV4B clawAndV4B, newVerticalSlides verticalSlides, newHorizontalSlides horizontalSlides, RevColorSensorV3 distance) {
        aVerticalPos = IntakeAndOuttake.verticalPos.GROUND;
        aInstructions = IntakeAndOuttake.Instructions.CLOSED;
        aSpecificInstruction = specificInstructions.INITIAL_CLOSE;

        this.clawAndV4B = clawAndV4B;
        this.turret = turret;
        this.verticalSlides = verticalSlides;
        this.horizontalSlides = horizontalSlides;
        this.distance = distance;
    }

    public void update() {

        switch (aVerticalPos) {
            case GROUND:
                switch (aInstructions) {
                    case CLOSED:
                        switch (aSpecificInstruction) {
                            case INITIAL_CLOSE:
                                verticalTargetPos = 0;
                                turretTargetPos = 0;
                                horizontalTargetPos = 0.72;
                                clawTargetPos = 0.9;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.V4B_DOWN_TO_UP;
                                break;
                            case V4B_DOWN_TO_UP:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    v4bTargetPos = 0.72;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
                    case INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                verticalTargetPos = verticalSlides.previousTargetPos - 40;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.5;
                                    verticalTargetPos = verticalSlides.previousTargetPos + 40;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                turretTargetPos = -400 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                break;
                            case TURRET_RESET_DELAY:
                                turretTargetPos = -400 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1200) {
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = -400 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = -400 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = -400 + turretIntakeOffset;
                                v4bTargetPos = 0.55 + v4bIntakeOffset;
                                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
                    case DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.9;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = aSpecificInstruction.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.6 + horizontalIntakeOffset;
                                    turretTargetPos = 400;
                                }
                                break;
                        }
                        break;
                }
                break;
            case TOP:
                switch (aInstructions) {
                    case DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.8;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = aSpecificInstruction.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    v4bTargetPos = 0.62 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.61 + horizontalIntakeOffset;
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = aSpecificInstruction.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.62 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.61 + horizontalIntakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    turretTargetPos = 400 + turretOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = aSpecificInstruction.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.62 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.61 + horizontalIntakeOffset;
                                verticalTargetPos = 3200 + verticalOuttakeOffset;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.6 + horizontalIntakeOffset;
                                }
                                break;
                        }
                        break;
                }

        }

        turret.set(turretTargetPos);
        verticalSlides.set(verticalTargetPos);
        horizontalSlides.set(horizontalTargetPos);
        clawAndV4B.v4bTargetPos(v4bTargetPos);
        clawAndV4B.clawControl(clawTargetPos);
    }

    public enum verticalPos {
        GROUND,
        BOTTOM,
        MID,
        TOP
    }

    public enum Instructions {
        CLOSED,
        INTAKE,
        STACK_INTAKE,
        DEPOSIT,
        PARK
    }

    public enum specificInstructions {
        INITIAL_CLOSE,
        DEPOSIT_CONE,
        NO_DEPOSIT_CONE,
        CLOSED_TO_INTAKE,
        V4B_DOWN_TO_UP,
        DELAY_DEPOSIT_TO_NO_DEPOSIT,
        TURRET_RESET_DELAY,
        TURRET_RESET_DELAY2,
        INCREASE_DEPOSIT_ACCURACY,
        DELAY1,

        //deposit
        CLOSE_CLAW,
        RETRACT_HORIZONTAL_SLIDES,
        DELAY_TURRET_DEPOSIT,
        EXTEND_HORIZONTAL_SLIDES
    }

    public void setaVerticalPos(verticalPos pos) {
        aVerticalPos = pos;
    }

    public void setaInstructions(Instructions instruction) {
        aInstructions = instruction;
    }

    public void setaSpecificInstruction(specificInstructions specificInstructions) {
        aSpecificInstruction = specificInstructions;
    }
}
