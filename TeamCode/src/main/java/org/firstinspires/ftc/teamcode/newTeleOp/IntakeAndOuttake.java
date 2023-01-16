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


    public int turretIntakeOffset = 0;
    public int horizontalIntakeOffset = 0;
    public int v4bIntakeOffset = 0;

    public int verticalOffset = 0;

    public int turretTargetPos;
    public int verticalTargetPos;
    public double horizontalTargetPos;
    public double v4bTargetPos;
    public double clawTargetPos;


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
        DELAY1
    }

    private static verticalPos verticalPos;
    private static Instructions Instructions;
    private static specificInstructions specificInstruction;

    private long prevAction = System.currentTimeMillis();

    public IntakeAndOuttake(newTurret turret, clawAndV4B clawAndV4B, newVerticalSlides verticalSlides, newHorizontalSlides horizontalSlides, RevColorSensorV3 distance) {
        verticalPos = IntakeAndOuttake.verticalPos.GROUND;
        Instructions = IntakeAndOuttake.Instructions.CLOSED;
        specificInstruction = specificInstructions.INITIAL_CLOSE;

        this.clawAndV4B = clawAndV4B;
        this.turret = turret;
        this.verticalSlides = verticalSlides;
        this.horizontalSlides = horizontalSlides;
        this.distance = distance;
    }

    public void update() {

        switch (verticalPos) {
            case GROUND:
                switch (Instructions) {
                    case CLOSED:
                        switch (specificInstruction) {
                            case INITIAL_CLOSE:
                                verticalTargetPos = 0;
                                turretTargetPos = 0;
                                horizontalTargetPos = 0.72;
                                clawTargetPos = 0.9;
                                prevAction = System.currentTimeMillis();
                                specificInstruction = specificInstructions.V4B_DOWN_TO_UP;
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
                        switch (specificInstruction) {
                            case DEPOSIT_CONE:
                                verticalTargetPos = verticalSlides.previousTargetPos - 150;
                                prevAction = System.currentTimeMillis();
                                specificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.5;
                                    verticalTargetPos = verticalSlides.previousTargetPos + 150;
                                    prevAction = System.currentTimeMillis();
                                    specificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    specificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                turretTargetPos = 0 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                specificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                break;
                            case TURRET_RESET_DELAY:
                                turretTargetPos = 0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = 0 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                specificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = 0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    v4bTargetPos = 0.42 + v4bIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    specificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = 0 + turretIntakeOffset;
                                v4bTargetPos = 0.42 + v4bIntakeOffset;
                                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
//                    case DEPOSIT:


                }
                break;
//            case TOP:
        }

        turret.set(turretTargetPos);
        verticalSlides.set(verticalTargetPos);
        horizontalSlides.set(horizontalTargetPos);
        clawAndV4B.v4bTargetPos(v4bTargetPos);
        clawAndV4B.clawControl(clawTargetPos);
    }

    public void setVerticalPos(verticalPos pos) {
        verticalPos = pos;
    }

    public void setInstructions(Instructions instruction) {
        Instructions = instruction;
    }

    public void setSpecificInstruction(specificInstructions specificInstructions) {
        specificInstruction = specificInstructions;
    }


}
