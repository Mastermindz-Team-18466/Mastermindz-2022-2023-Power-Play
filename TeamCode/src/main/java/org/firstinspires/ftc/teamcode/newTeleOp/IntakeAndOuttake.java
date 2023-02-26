package org.firstinspires.ftc.teamcode.newTeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;

@Config
public class IntakeAndOuttake {
    public newTurret turret;
    public newVerticalSlides verticalSlides;
    public clawAndArm clawAndArm;
    public newHorizontalSlides horizontalSlides;
    public RevColorSensorV3 distance;


    public double turretIntakeOffset = 0;
    public double horizontalIntakeOffset = 0;
    public double armIntakeOffset = 0;
    public double verticalIntakeOffset = 0;

    public double turretOuttakeOffset = 0;
    public double horizontalOuttakeOffset = 0;
    public double armOuttakeOffset = 0;
    public double verticalOuttakeOffset = 0;

    public double turretTargetPos;
    public double verticalTargetPos;
    public double horizontalTargetPos;
    public double armTargetPos;
    public double clawTargetPos;
    public double clawSpin;

    public verticalPos aVerticalPos;
    public Instructions aInstructions;
    public specificInstructions aSpecificInstruction;

    private long prevAction = System.currentTimeMillis();

    public IntakeAndOuttake(newTurret turret, clawAndArm clawAndArm, newVerticalSlides verticalSlides, newHorizontalSlides horizontalSlides, RevColorSensorV3 distance) {
        aVerticalPos = IntakeAndOuttake.verticalPos.GROUND;
        aInstructions = IntakeAndOuttake.Instructions.CLOSED;
        aSpecificInstruction = specificInstructions.INITIAL_CLOSE;

        this.clawAndArm = clawAndArm;
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
                                horizontalTargetPos = 0;
                                clawTargetPos = 0.15;
                                clawSpin = 0;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200){
                                    armTargetPos = 0.72;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
                    case INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.5 + armIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.5 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 150) {
                                    clawTargetPos = 0.37;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    turretTargetPos = 0 + turretIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200){
                                    clawTargetPos = 0.2;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 1 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 1 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 1200) {
                                    horizontalTargetPos = 0.51 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS;
                                }
                                break;
                            case HOLD_POS:
                                horizontalTargetPos = 0.51 + horizontalIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 1 + armIntakeOffset;
                                verticalTargetPos = 0 + verticalIntakeOffset;
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = 0 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = 0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                    verticalTargetPos = 0;
                                    armTargetPos = 0 + armIntakeOffset;
                                    clawTargetPos = 0.5;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 0 + armIntakeOffset;
                                horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    aSpecificInstruction = specificInstructions.HOLD_POS_CLOSED;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case HOLD_POS_CLOSED:
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 0;
                                armTargetPos = 0 + armIntakeOffset;
                                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                break;
                        }
                        break;

                    case AUTO_RIGHT_INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.4 + armIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.4 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.37;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 0.65 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                armTargetPos = 0.65 + armIntakeOffset;
                                horizontalTargetPos = 0.65 + horizontalIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                break;
                            case TURRET_RESET_DELAY:
                                armTargetPos = 0.65 + armIntakeOffset;
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    armTargetPos = 0.55 + armIntakeOffset;
                                    verticalTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY3;
                                }
                                break;
                            case TURRET_RESET_DELAY3:
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                armTargetPos = 0.72 + armIntakeOffset;
                                verticalTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos =   -(-400 + turretIntakeOffset);
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                verticalTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 1200) {
                                    horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                    armTargetPos = 0.55 + armIntakeOffset;
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
                                    armTargetPos = 0.55 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                armTargetPos = 0.55 + armIntakeOffset;
                                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case LEFT_INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.4 + armIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.4 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.37;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 0.65 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                armTargetPos = 0.65 + armIntakeOffset;
                                horizontalTargetPos = 0.65 + horizontalIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                break;
                            case TURRET_RESET_DELAY:
                                armTargetPos = 0.65 + armIntakeOffset;
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    armTargetPos = 0.55 + armIntakeOffset;
                                    verticalTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY3;
                                }
                                break;
                            case TURRET_RESET_DELAY3:
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                armTargetPos = 0.72 + armIntakeOffset;
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
                                    armTargetPos = 0.55 + armIntakeOffset;
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
                                    armTargetPos = 0.55 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                armTargetPos = 0.55 + armIntakeOffset;
                                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.5;
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
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    clawSpin = 1;
                                    armTargetPos = 0.4 + armOuttakeOffset;
                                    horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.4 + armOuttakeOffset;
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = 30 + turretOuttakeOffset;
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = 30 + turretOuttakeOffset;
                                armTargetPos = 0.4 + armOuttakeOffset;
                                break;
                        }
                        break;

                    case STRAIGHT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.15;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    clawSpin = 1;
                                    armTargetPos = 0.4 + armOuttakeOffset;
                                    horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.4 + armOuttakeOffset;
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = 0 + turretOuttakeOffset;
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = 0 + turretOuttakeOffset;
                                armTargetPos = 0.4 + armOuttakeOffset;
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
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                armTargetPos = 0.72 + armOuttakeOffset;
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
                                aSpecificInstruction = specificInstructions.RAISE_ARM;
                                break;
                            case RAISE_ARM:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    verticalTargetPos = 1000 + verticalTargetPos;
                                    horizontalTargetPos = 0.42 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = 350 + turretOuttakeOffset;
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = 400 + turretOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                armTargetPos = 0.72 + armOuttakeOffset;
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
                                aSpecificInstruction = specificInstructions.RAISE_ARM;
                                break;
                            case RAISE_ARM:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    verticalTargetPos = 1000 + verticalTargetPos;
                                    horizontalTargetPos = 0.42 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(350 + turretOuttakeOffset);
                                    verticalTargetPos = 3200 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 3200 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                armTargetPos = 0.72 + armOuttakeOffset;
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
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    clawSpin = 1;
                                    armTargetPos = 0.4 + armOuttakeOffset;
                                    horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.4 + armOuttakeOffset;
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = 0 + turretOuttakeOffset;
                                    verticalTargetPos = 1800 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                turretTargetPos = 0 + turretOuttakeOffset;
                                verticalTargetPos = 1800 + verticalTargetPos;
                                armTargetPos = 0.4 + armOuttakeOffset;
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
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 2130 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 2130 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                armTargetPos = 0.72 + armOuttakeOffset;
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
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    clawSpin = 1;
                                    armTargetPos = 0.4 + armOuttakeOffset;
                                    horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.4 + armOuttakeOffset;
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = 0 + turretOuttakeOffset;
                                    verticalTargetPos = 1060 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0 + horizontalOuttakeOffset;
                                turretTargetPos = 0 + turretOuttakeOffset;
                                verticalTargetPos = 1060 + verticalTargetPos;
                                armTargetPos = 0.4 + armOuttakeOffset;
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
                                    armTargetPos = 0.72 + armOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 1060 + verticalTargetPos;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                armTargetPos = 0.72 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 1060 + verticalTargetPos;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                armTargetPos = 0.72 + armOuttakeOffset;
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
                armTargetPos = 0.5 + armIntakeOffset;
                horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                turretTargetPos = 0 + turretIntakeOffset;
                verticalTargetPos = 0 + verticalOuttakeOffset;
                switch (aSpecificInstruction) {
                    case OPEN_CLAW:
                        armTargetPos = 0.5 + armIntakeOffset;
                        horizontalTargetPos = 0.45 + horizontalIntakeOffset;
                        turretTargetPos = 0 + turretIntakeOffset;
                        verticalTargetPos = 0 + verticalOuttakeOffset;

                        clawTargetPos = 0.175;
                        break;
                    case CLOSE_CLAW:
                        armTargetPos = 0.5 + armIntakeOffset;
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
        clawAndArm.armTargetPos(armTargetPos);
        clawAndArm.clawControl(clawTargetPos);
        clawAndArm.clawSpin(clawSpin);
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
        AUTO_RIGHT_INTAKE,
        LEFT_INTAKE,
        LEFT_STACK_DEPOSIT,
        RIGHT_STACK_DEPOSIT,
        DEPOSIT,
        LEFT_DEPOSIT,
        STRAIGHT_DEPOSIT,
        PARK
    }

    public enum specificInstructions {
        INITIAL_CLOSE,
        ARM_DELAY,
        DEPOSIT_CONE,
        NO_DEPOSIT_CONE,
        CLOSED_TO_INTAKE,
        ARM_DOWN_TO_UP,
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
        RAISE_ARM,

        //custom
        OPEN_CLAW,

        HORIZONTAL_TUNE,
        HORIZONTAL_TUNE_CLOSED,
        HOLD_POS,
        HOLD_POS_CLOSED
    }
}