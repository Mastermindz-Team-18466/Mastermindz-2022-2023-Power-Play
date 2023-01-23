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
                                clawTargetPos = 0.15;
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
                                v4bTargetPos = 0.4 + v4bIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                v4bTargetPos = 0.4 + v4bIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.37;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    v4bTargetPos = 0.65 + v4bIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                v4bTargetPos = 0.65 + v4bIntakeOffset;
                                horizontalTargetPos = 0.65 + horizontalIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                break;
                            case TURRET_RESET_DELAY:
                                v4bTargetPos = 0.65 + v4bIntakeOffset;
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
                                    verticalTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY3;
                                }
                                break;
                            case TURRET_RESET_DELAY3:
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                v4bTargetPos = 0.72 + v4bIntakeOffset;
                                verticalTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = -400 + turretIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                turretTargetPos = -400 + turretIntakeOffset;
                                verticalTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 1200) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
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
                                    clawTargetPos = 0.43                                    ;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case LEFT_INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                v4bTargetPos = 0.4 + v4bIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                v4bTargetPos = 0.4 + v4bIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.37;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    v4bTargetPos = 0.65 + v4bIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                v4bTargetPos = 0.65 + v4bIntakeOffset;
                                horizontalTargetPos = 0.65 + horizontalIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                break;
                            case TURRET_RESET_DELAY:
                                v4bTargetPos = 0.65 + v4bIntakeOffset;
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
                                    verticalTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY3;
                                }
                                break;
                            case TURRET_RESET_DELAY3:
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                v4bTargetPos = 0.72 + v4bIntakeOffset;
                                verticalTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = -(-400 + turretIntakeOffset);
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                verticalTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 1200) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    v4bTargetPos = 0.55 + v4bIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                v4bTargetPos = 0.55 + v4bIntakeOffset;
                                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.46                                    ;
                                    prevAction = System.currentTimeMillis();
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
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = 400 + turretOuttakeOffset;
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 3200 + verticalOuttakeOffset;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;

                    case LEFT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 3200 + verticalOuttakeOffset;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;

                    case LEFT_STACK_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RAISE_V4B;
                                break;
                            case RAISE_V4B:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    verticalTargetPos = 1000 + verticalTargetPos;
                                    horizontalTargetPos = 0.42 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = 350 + turretOuttakeOffset;
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 3200 + verticalOuttakeOffset;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;

                    case RIGHT_STACK_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RAISE_V4B;
                                break;
                            case RAISE_V4B:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    verticalTargetPos = 1000 + verticalTargetPos;
                                    horizontalTargetPos = 0.42 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(350 + turretOuttakeOffset);
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 3200 + verticalOuttakeOffset;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;
                }
                break;

            case MID:
                switch (aInstructions) {
                    case DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = 400 + turretOuttakeOffset;
                                    verticalTargetPos = 2130 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 2130 + verticalTargetPos;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 2130 + verticalOuttakeOffset;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;

                    case LEFT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 2130 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 2130 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 2130 + verticalOuttakeOffset;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;
                }
                break;

            case BOTTOM:
                switch (aInstructions) {
                    case DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = 400 + turretOuttakeOffset;
                                    verticalTargetPos = 1060 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 1060 + verticalTargetPos;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 1060 + verticalOuttakeOffset;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;

                    case LEFT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 1060 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 1060 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                v4bTargetPos = 0.72 + v4bOuttakeOffset;
                                verticalTargetPos = 1060 + verticalOuttakeOffset;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    horizontalTargetPos = 0.55 + horizontalOuttakeOffset;
                                }
                                break;
                        }
                        break;
                }
                break;

            case CUSTOM:
                v4bTargetPos = 0.5 + v4bIntakeOffset;
                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                turretTargetPos = 0 + turretIntakeOffset;
                verticalTargetPos = 0 + verticalOuttakeOffset;
                switch (aSpecificInstruction) {
                    case OPEN_CLAW:
                        v4bTargetPos = 0.5 + v4bIntakeOffset;
                        horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                        turretTargetPos = 0 + turretIntakeOffset;
                        verticalTargetPos = 0 + verticalOuttakeOffset;

                        clawTargetPos = 0.175;
                        break;
                    case CLOSE_CLAW:
                        v4bTargetPos = 0.5 + v4bIntakeOffset;
                        horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                        turretTargetPos = 0 + turretIntakeOffset;
                        verticalTargetPos = 0 + verticalOuttakeOffset;

                        clawTargetPos = 0.02;
                }
                break;
        }

        turret.set(turretTargetPos);
        verticalSlides.set(verticalTargetPos);
        horizontalSlides.set(horizontalTargetPos);
        clawAndV4B.v4bTargetPos(v4bTargetPos);
        clawAndV4B.clawControl(clawTargetPos);
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

    public enum verticalPos {
        GROUND,
        BOTTOM,
        MID,
        TOP,
        CUSTOM
    }

    public enum Instructions {
        CLOSED,
        INTAKE,
        LEFT_INTAKE,
        LEFT_STACK_DEPOSIT,
        RIGHT_STACK_DEPOSIT,
        DEPOSIT,
        LEFT_DEPOSIT,
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
        TURRET_RESET_DELAY3,
        INTAKE_EXTENSION,
        INCREASE_DEPOSIT_ACCURACY,
        DELAY1,

        //deposit
        LAST_CONE,
        CLOSE_CLAW,
        RETRACT_HORIZONTAL_SLIDES,
        LOWER_VERTICAL,
        DELAY_TURRET_DEPOSIT,
        EXTEND_HORIZONTAL_SLIDES,

        //stack
        RAISE_V4B,

        //custom
        OPEN_CLAW
    }
}