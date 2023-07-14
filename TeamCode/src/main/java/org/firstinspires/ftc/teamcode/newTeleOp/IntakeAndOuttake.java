package org.firstinspires.ftc.teamcode.newTeleOp;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeAndOuttake {
    public newTurret turret;
    public newVerticalSlides verticalSlides;
    public clawAndArm clawAndArm;
    public newHorizontalSlides horizontalSlides;


    public double turretIntakeOffset = 0;
    public double horizontalIntakeOffset = 0;
    public double armIntakeOffset = 0;
    public double verticalIntakeOffset = 0;

    public double verticalDescoreOffset = 0;

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

    public double teleStackOffset = 0;

    public verticalPos aVerticalPos;
    public Instructions aInstructions;
    public specificInstructions aSpecificInstruction;

    private long prevAction = System.currentTimeMillis();

    public IntakeAndOuttake(newTurret turret, clawAndArm clawAndArm, newVerticalSlides verticalSlides, newHorizontalSlides horizontalSlides) {
        aVerticalPos = IntakeAndOuttake.verticalPos.GROUND;
        aInstructions = IntakeAndOuttake.Instructions.CLOSED;
        aSpecificInstruction = specificInstructions.INITIAL_CLOSE;

        this.clawAndArm = clawAndArm;
        this.turret = turret;
        this.verticalSlides = verticalSlides;
        this.horizontalSlides = horizontalSlides;
    }

    public void update() {

        switch (aVerticalPos) {
            case GROUND:
                switch (aInstructions) {
                    case DESCORE:
                        switch (aSpecificInstruction) {
                            case DESCORE_POSE:
                                verticalTargetPos = 100 + verticalDescoreOffset;
                                armTargetPos = 0.12;
                                clawSpin = 0;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.OPEN_CLAW;
                                break;
                        }
                        break;
                    case DESCORE_UP:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.DESCORE_POSE;
                                break;
                            case DESCORE_POSE:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    verticalTargetPos = 2000;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.ARM_DOWN_TO_UP;
                                }
                                break;
                            case ARM_DOWN_TO_UP:
                                if (System.currentTimeMillis() - prevAction > 1333) {
                                    armTargetPos = 0.5;
                                }
                                break;
                        }
                        break;
                    case FALLEN_CONE:
                        switch (aSpecificInstruction) {
                            case SET_TO_POS:
                                verticalTargetPos = 690;
                                horizontalTargetPos = 0.3;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    armTargetPos = 0.1;
                                    clawTargetPos = 0.2;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.ARM_DOWN;
                                }
                                break;
                            case ARM_DOWN:
                                if (System.currentTimeMillis() - prevAction > 1000) {
                                    armTargetPos = -0.1;
                                }
                                break;
                        }
                        break;
                    case FALLEN_CONE_PICK:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DOWN_TO_UP;
                                break;
                            case ARM_DOWN_TO_UP:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.41;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    verticalTargetPos = 10;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
                    case AUTO_CLOSE:
                        switch (aSpecificInstruction) {
                            case INITIAL_CLOSE:
                                verticalTargetPos = 10;
                                turretTargetPos = 585;
                                horizontalTargetPos = 0.05;
                                clawTargetPos = 0.5;
                                clawSpin = 0.69;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.7;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
                    case PARK:
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.51;
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                clawSpin = 0;
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    armTargetPos = 0.17 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 600) {
                                    clawTargetPos = 0.2;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSED_TO_INTAKE;
                                }
                                break;
                            case CLOSED_TO_INTAKE:
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    armTargetPos = 0.115;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INITIAL_CLOSE;
                                }
                                break;
                            case INITIAL_CLOSE:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    horizontalTargetPos = 0.47;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case OTHER_SIDE:
                        switch (aSpecificInstruction) {
                            case INITIAL_CLOSE:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.HORIZONTAL_TUNE;
                            case HORIZONTAL_TUNE:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawSpin = 0.69;
                                    verticalTargetPos = 300;
                                    horizontalTargetPos = 0.1;
                                    turretTargetPos = 1300;
                                    armTargetPos = 0.3;
                                    aSpecificInstruction = specificInstructions.ARM_DELAY;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 330) {
                                    armTargetPos = 0.7;
                                    verticalTargetPos = 10;
                                    turretTargetPos = 1500;
                                    horizontalTargetPos = 0.05;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                if (System.currentTimeMillis() - prevAction > 600) {
                                    turretTargetPos = 0;
                                }
                                break;
                        }
                        break;

                    case AUTO_TURRET_POS:
                        switch (aSpecificInstruction) {
                            case INITIAL_CLOSE:
                                verticalTargetPos = 0;
                                turretTargetPos = 630;
                                horizontalTargetPos = 0.05;
                                clawTargetPos = 0.5;
                                clawSpin = 0.69;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.7;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case LEFT_AUTO_CLOSE:
                        switch (aSpecificInstruction) {
                            case INITIAL_CLOSE:
                                verticalTargetPos = 0;
                                turretTargetPos = -585;
                                horizontalTargetPos = 0.05;
                                clawTargetPos = 0.5;
                                clawSpin = 0.69;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.7;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case CLOSED:
                        switch (aSpecificInstruction) {
                            case INITIAL_CLOSE:
                                verticalTargetPos = 0;
                                horizontalTargetPos = 0.05;
                                clawTargetPos = 0.5;
                                clawSpin = 0;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.7;
                                    turretTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case DRIVING:
                        turretTargetPos = 0;
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                clawTargetPos = 0.5;
                                turretOuttakeOffset = 0;
                                turretTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    horizontalTargetPos = 0.05;
                                    verticalTargetPos = 10;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;

                            case CLOSE_CLAW:
                                turretTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawSpin = 0.69;
                                    armTargetPos = 0.65;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case AUTO_TO_TELE:
                        turretTargetPos = 0;
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                turretOuttakeOffset = 0;
                                turretTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 350) {
                                    horizontalTargetPos = 0.05;
                                    clawTargetPos = 0.5;
                                    verticalTargetPos = 10;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;

                            case CLOSE_CLAW:
                                turretTargetPos = 0;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawSpin = 0.69;
                                    armTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case CLOSED_INTAKE:
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.075;
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                clawSpin = 0;
                                armTargetPos = 0.3;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    armTargetPos = 0.17 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.ARM_DOWN;
                                }
                                break;
                            case ARM_DOWN:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    armTargetPos = 0.13 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case NO_EXTEND_INTAKE:
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.05;
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                clawSpin = 0;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    armTargetPos = 0.1 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                        }
                        break;

                    case EXTEND_INTAKE:
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2;
                                armTargetPos = 0.3 + armIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                clawSpin = 0;
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.4;
                                    armTargetPos = 0.17;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.ARM_DOWN;
                                }
                                break;
                            case ARM_DOWN:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    armTargetPos = 0.135;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;
                    case TRANSFORMER_MOVE:
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2;
                                armTargetPos = 0.17 + armIntakeOffset;
                                verticalTargetPos = 10;
                                clawSpin = 0;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.25;
                                    armTargetPos = 0.13;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    horizontalTargetPos = 0.51;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW_CLOSED;
                                }
                                break;
                            case OPEN_CLAW_CLOSED:
                                if (System.currentTimeMillis() - prevAction > 600) {
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HORIZONTAL_TUNE;
                                }
                                break;
                            case HORIZONTAL_TUNE:
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    armTargetPos = 0.13;
                                    horizontalTargetPos = 0.05;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.ARM_DOWN;
                                }
                                break;
                            case ARM_DOWN:
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    armTargetPos = 0.11;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW;
                                }
                                break;
                            case OPEN_CLAW:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RAISE_ARM;
                                }
                                break;
                            case RAISE_ARM:
                                if (System.currentTimeMillis() - prevAction > 600) {
                                    armTargetPos = 0.45;
                                    horizontalTargetPos = 0.05;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HORIZONTAL_TUNE_CLOSED;
                                }
                                break;
                            case HORIZONTAL_TUNE_CLOSED:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                        }
                        break;

                    case TELE_STACK_INTAKE:
                        switch (aSpecificInstruction) {
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.05;
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 490 + teleStackOffset;
                                clawSpin = 0;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                verticalTargetPos = 490 + teleStackOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    armTargetPos = 0.1 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                        }
                        break;
                    case TELE_STACK_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                break;
                            case INTAKE_EXTENSION:
                                if (System.currentTimeMillis() - prevAction > 275) {
                                    verticalTargetPos = 900;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    clawSpin = 0.69;
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.05;
                                    turretTargetPos = 0 + turretOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    break;
                                }
                        }
                        break;

                    case INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.75 + armIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.75 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.2;
                                    verticalTargetPos = 10;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = 0 + turretIntakeOffset;
                                    clawTargetPos = 0.5;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 0.16 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2;
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 0.16 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW;
                                }
                            case OPEN_CLAW:
                                horizontalTargetPos = 0.2;
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 0.16 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.43 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS;
                                }
                                break;
                            case HOLD_POS:
                                horizontalTargetPos = 0.43 + horizontalIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 0.16 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = 250 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = 0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                    verticalTargetPos = 10;
                                    armTargetPos = 0.16 + armIntakeOffset;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 0.16 + armIntakeOffset;
                                horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.25;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW_CLOSED;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case OPEN_CLAW_CLOSED:
                                horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                turretTargetPos = 0 + turretIntakeOffset;
                                armTargetPos = 0.16 + armIntakeOffset;
                                verticalTargetPos = 10;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    horizontalTargetPos = 0.43 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS_CLOSED;
                                }
                                break;
                            case HOLD_POS_CLOSED:
                                turretTargetPos = 0 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                armTargetPos = 0.16 + armIntakeOffset;
                                horizontalTargetPos = 0.43 + horizontalIntakeOffset;
                                break;
                        }
                        break;

                    case LEFT_TELEOP_INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.66 + armIntakeOffset;
                                verticalTargetPos = 1600;
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.66 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    armTargetPos = 0.735 + armIntakeOffset;
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.2;
                                    verticalTargetPos = 10;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    turretTargetPos = -0 + turretIntakeOffset;
                                    armTargetPos = 0.55 + armIntakeOffset;
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
                                turretTargetPos = -0 + turretIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 50) {
                                    armTargetPos = 0.15 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2;
                                turretTargetPos = -0 + turretIntakeOffset;
                                armTargetPos = 0.15 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.25;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW;
                                }
                            case OPEN_CLAW:
                                horizontalTargetPos = 0.225;
                                turretTargetPos = -0 + turretIntakeOffset;
                                armTargetPos = 0.15 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    horizontalTargetPos = 0.355 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS;
                                }
                                break;
                            case HOLD_POS:
                                horizontalTargetPos = 0.355 + horizontalIntakeOffset;
                                turretTargetPos = -0 + turretIntakeOffset;
                                armTargetPos = 0.15 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = -0 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = -0 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 75) {
                                    horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                    verticalTargetPos = 10;
                                    armTargetPos = 0.15 + armIntakeOffset;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = -0 + turretIntakeOffset;
                                armTargetPos = 0.15 + armIntakeOffset;
                                horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.25;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW_CLOSED;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case OPEN_CLAW_CLOSED:
                                horizontalTargetPos = 0.2 + horizontalIntakeOffset;
                                turretTargetPos = -0 + turretIntakeOffset;
                                armTargetPos = 0.15 + armIntakeOffset;
                                verticalTargetPos = 10;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    horizontalTargetPos = 0.355 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS_CLOSED;
                                }
                                break;
                            case HOLD_POS_CLOSED:
                                turretTargetPos = -0 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                armTargetPos = 0.15 + armIntakeOffset;
                                horizontalTargetPos = 0.355 + horizontalIntakeOffset;
                                break;
                        }
                        break;

                    case AUTO_RIGHT_INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.66 + armIntakeOffset;
                                verticalTargetPos = 1790;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.66 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 150) {
                                    armTargetPos = 0.72 + armIntakeOffset;
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 150) {
                                    horizontalTargetPos = 0.2;
//                                    turretTargetPos = 915 + turretIntakeOffset;
//                                    turretTargetPos = 920;
                                    verticalTargetPos = 10 + verticalIntakeOffset;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;
                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 50) {
                                    armTargetPos = 0.1 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                armTargetPos = 0.1 + armIntakeOffset;
//                                turretTargetPos = 915 + turretIntakeOffset;
//                                turretTargetPos = 920;
                                if (System.currentTimeMillis() - prevAction > 130) {
                                    turretTargetPos = 1260;
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 1260;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                armTargetPos = 0.1 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 50) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 1260;
                                armTargetPos = 0.1 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 130) {
                                    clawTargetPos = 0.23;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW;
                                }
                                break;
                            case OPEN_CLAW:
                                horizontalTargetPos = 0.25;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 1260;
                                armTargetPos = 0.1 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    horizontalTargetPos = 0.25 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS;
                                }
                                break;
                            case HOLD_POS:
                                horizontalTargetPos = 0.485 + horizontalIntakeOffset;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 1260;
                                armTargetPos = 0.1 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = 860 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = 860 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                    verticalTargetPos = 10;
                                    armTargetPos = 0.1 + armIntakeOffset;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = 860 + turretIntakeOffset;
                                armTargetPos = 0.1 + armIntakeOffset;
                                horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.2;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW_CLOSED;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case OPEN_CLAW_CLOSED:
                                horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                turretTargetPos = 860 + turretIntakeOffset;
                                armTargetPos = 0.1 + armIntakeOffset;
                                verticalTargetPos = 10;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    horizontalTargetPos = 0.43 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS_CLOSED;
                                }
                                break;
                            case HOLD_POS_CLOSED:
                                turretTargetPos = 860 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                armTargetPos = 0.1 + armIntakeOffset;
                                horizontalTargetPos = 0.3 + horizontalIntakeOffset;
                                break;
                        }
                        break;

                    case AUTO_LEFT_INTAKE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.66 + armIntakeOffset;
                                verticalTargetPos = 1600;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.66 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 175) {
                                    armTargetPos = 0.72 + armIntakeOffset;
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    horizontalTargetPos = 0.2;
//                                    turretTargetPos = 915 + turretIntakeOffset;
                                    turretTargetPos = 260;
                                    verticalTargetPos = 10 + verticalIntakeOffset;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                verticalTargetPos = 10 + verticalIntakeOffset;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 260;
                                if (System.currentTimeMillis() - prevAction > 115) {
                                    armTargetPos = 0.11 + armIntakeOffset;
                                    clawTargetPos = 0.5;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 260;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                armTargetPos = 0.11 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 50) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                horizontalTargetPos = 0.2;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 260;
                                armTargetPos = 0.11 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.2;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW;
                                }
                            case OPEN_CLAW:
                                horizontalTargetPos = 0.25;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 260;
                                armTargetPos = 0.11 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    horizontalTargetPos = 0.3 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS;
                                }
                                break;
                            case HOLD_POS:
                                horizontalTargetPos = 0.455 + horizontalIntakeOffset;
//                                turretTargetPos = 915 + turretIntakeOffset;
                                turretTargetPos = 260;
                                armTargetPos = 0.11 + armIntakeOffset;
                                verticalTargetPos = 10 + verticalIntakeOffset;
                                break;
                            case CLOSED_TO_INTAKE:
                                turretTargetPos = 299 + turretIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY2;
                                break;
                            case TURRET_RESET_DELAY2:
                                turretTargetPos = 299 + turretIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                    verticalTargetPos = 10;
                                    armTargetPos = 0.1 + armIntakeOffset;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                turretTargetPos = 299 + turretIntakeOffset;
                                armTargetPos = 0.1 + armIntakeOffset;
                                horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    clawTargetPos = 0.23;
                                    aSpecificInstruction = specificInstructions.OPEN_CLAW_CLOSED;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;
                            case OPEN_CLAW_CLOSED:
                                horizontalTargetPos = 0.125 + horizontalIntakeOffset;
                                turretTargetPos = 299 + turretIntakeOffset;
                                armTargetPos = 0.1 + armIntakeOffset;
                                verticalTargetPos = 10;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    horizontalTargetPos = 0.43 + horizontalIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.HOLD_POS_CLOSED;
                                }
                                break;
                            case HOLD_POS_CLOSED:
                                turretTargetPos = 299 + turretIntakeOffset;
                                verticalTargetPos = 10;
                                armTargetPos = 0.1 + armIntakeOffset;
                                horizontalTargetPos = 0.3 + horizontalIntakeOffset;
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
                                    clawTargetPos = 0.25;
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
                                    verticalTargetPos = 10;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY3;
                                }
                                break;
                            case TURRET_RESET_DELAY3:
                                horizontalTargetPos = 0.72 + horizontalIntakeOffset;
                                armTargetPos = 0.72 + armIntakeOffset;
                                verticalTargetPos = 10;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = -(-400 + turretIntakeOffset);
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                turretTargetPos = -(-400 + turretIntakeOffset);
                                verticalTargetPos = 10;
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
                                    verticalTargetPos = 10;
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
                                    clawTargetPos = 0.25;
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
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 400) {
                                    clawSpin = 0.69;
                                    horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    turretTargetPos = 0 + turretOuttakeOffset;
                                    verticalTargetPos = 1690 + verticalOuttakeOffset;
                                    armTargetPos = 0.32 + armOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                verticalTargetPos = 1690 + verticalOuttakeOffset;
                                turretTargetPos = 0 + turretOuttakeOffset;
                                armTargetPos = 0.66 + armOuttakeOffset;
                                break;
                        }
                        break;

                    case GROUND_INTAKE:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                armTargetPos = 0.1;
                                horizontalTargetPos = 0.05;
                                break;
                        }
                        break;

                    case GROUND_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.25;
                                horizontalTargetPos = 0.05;
                                verticalTargetPos = 300;
                                break;
                        }
                        break;

                    case LAST_CONE:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.66 + armIntakeOffset;
                                verticalTargetPos = 1600;
                                if (System.currentTimeMillis() - prevAction > 140) {
                                    armTargetPos = 0.72 + armIntakeOffset;
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                if (System.currentTimeMillis() - prevAction > 350) {
                                    clawTargetPos = 0.5;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 0.5 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                clawTargetPos = 0.5;
                                if (System.currentTimeMillis() - prevAction > 320) {
                                    horizontalTargetPos = 0.05;
                                    armTargetPos = 0.5;
                                    verticalTargetPos = 10;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;

                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;

                        }
                        break;

                    case STRAIGHT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.72 + armIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.72 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                if (System.currentTimeMillis() - prevAction > 350) {
                                    clawTargetPos = 0.5;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 0.62 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                clawTargetPos = 0.5;
                                if (System.currentTimeMillis() - prevAction > 320) {
                                    horizontalTargetPos = 0.05;
                                    armTargetPos = 0.62 + armIntakeOffset;
                                    verticalTargetPos = 10;
                                    turretTargetPos = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;

                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;

                        }
                        break;
                    case AUTO_STRAIGHT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case DEPOSIT_CONE:
                                armTargetPos = 0.72 + armIntakeOffset;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.INCREASE_DEPOSIT_ACCURACY;
                                break;
                            case INCREASE_DEPOSIT_ACCURACY:
                                armTargetPos = 0.72 + armIntakeOffset;
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_DEPOSIT_TO_NO_DEPOSIT;
                                }
                                break;
                            case DELAY_DEPOSIT_TO_NO_DEPOSIT:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    horizontalTargetPos = 0.25;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.NO_DEPOSIT_CONE;
                                }
                                break;
                            case NO_DEPOSIT_CONE:
                                horizontalTargetPos = 0.2;
                                if (System.currentTimeMillis() - prevAction > 350) {
                                    clawTargetPos = 0.5;
                                    clawSpin = 0;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.TURRET_RESET_DELAY;
                                }
                                break;
                            case TURRET_RESET_DELAY:
                                horizontalTargetPos = 0.2;
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    armTargetPos = 0.62 + armIntakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.INTAKE_EXTENSION;
                                }
                                break;
                            case INTAKE_EXTENSION:
                                clawTargetPos = 0.5;
                                if (System.currentTimeMillis() - prevAction > 320) {
                                    horizontalTargetPos = 0.05;
                                    armTargetPos = 0.62 + armIntakeOffset;
                                    verticalTargetPos = 10;
                                    turretTargetPos = 1260;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.CLOSE_CLAW;
                                }
                                break;

                            case CLOSE_CLAW:
                                if (System.currentTimeMillis() - prevAction > 250) {
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                }
                                break;

                        }
                        break;
                    case TOP_STRAIGHT:
                        switch (aSpecificInstruction) {
                            case RETRACT_HORIZONTAL_SLIDES:
                                clawSpin = 0.69;
                                armTargetPos = 0.66 + armOuttakeOffset;
                                horizontalTargetPos = 0.05;
                                turretTargetPos = 0 + turretOuttakeOffset;
                                verticalTargetPos = 1690 + verticalOuttakeOffset;
                                prevAction = System.currentTimeMillis();
                                break;
                        }
                        break;

                    case LEFT_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                if (System.currentTimeMillis() - prevAction > 500) {
                                    armTargetPos = 0.56 + armOuttakeOffset;
                                    horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                armTargetPos = 0.56 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                if (System.currentTimeMillis() - prevAction > 300) {
                                    turretTargetPos = -(400 + turretOuttakeOffset);
                                    verticalTargetPos = 1700 + verticalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                armTargetPos = 0.56 + armOuttakeOffset;
                                horizontalTargetPos = 0.72 + horizontalOuttakeOffset;
                                verticalTargetPos = 1700 + verticalOuttakeOffset;
                                turretTargetPos = -(400 + turretOuttakeOffset);
                                if (System.currentTimeMillis() - prevAction > 100) {
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.EXTEND_HORIZONTAL_SLIDES;
                                }
                                break;
                            case EXTEND_HORIZONTAL_SLIDES:
                                armTargetPos = 0.56 + armOuttakeOffset;
                                verticalTargetPos = 1700 + verticalOuttakeOffset;
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
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    verticalTargetPos = 1790 + verticalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                            case DELAY1:
                                if (System.currentTimeMillis() - prevAction > 250) {
//                                    turretTargetPos = 1120 + turretOuttakeOffset;
                                    turretTargetPos = 45;
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                verticalTargetPos = 1790 + verticalIntakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 45;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.2 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                verticalTargetPos = 1790 + verticalIntakeOffset;
                                horizontalTargetPos = 0.2 + horizontalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 45;
                                if (System.currentTimeMillis() - prevAction > 230) {
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                verticalTargetPos = 1790 + verticalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 45;
                                armTargetPos = 0.66 + armOuttakeOffset;
                                break;
                        }
                        break;

                    case SIXTH_CONE:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    verticalTargetPos = 1200 + verticalOuttakeOffset;
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                            case DELAY1:
                                if (System.currentTimeMillis() - prevAction > 250) {
//                                    turretTargetPos = 1120 + turretOuttakeOffset;
                                    turretTargetPos = -30;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                verticalTargetPos = 700 + verticalIntakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = -30;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.15 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                verticalTargetPos = 1200 + verticalIntakeOffset;
                                horizontalTargetPos = 0.15 + horizontalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = -30;
                                if (System.currentTimeMillis() - prevAction > 220) {
                                    verticalTargetPos = 1710 + verticalOuttakeOffset;
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                verticalTargetPos = 1720 + verticalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = -30;
                                armTargetPos = 0.66 + armOuttakeOffset;
                                break;
                        }
                        break;

                    case RIGHT_STACK_DEPOSIT:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    verticalTargetPos = 1790;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                            case DELAY1:
                                if (System.currentTimeMillis() - prevAction > 250) {
//                                    turretTargetPos = 1120 + turretOuttakeOffset;
                                    turretTargetPos = 1260;
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                verticalTargetPos = 1790;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 1260;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.2 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                verticalTargetPos = 1790;
                                horizontalTargetPos = 0.2 + horizontalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 1260;
                                if (System.currentTimeMillis() - prevAction > 230) {
                                    armTargetPos = 0.66 + armOuttakeOffset;
                                    horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                verticalTargetPos = 1790;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 1260;
                                armTargetPos = 0.66 + armOuttakeOffset;
                                break;
                        }
                        break;

                    case PRELOAD:
                        switch (aSpecificInstruction) {
                            case CLOSE_CLAW:
                                clawTargetPos = 0.5;
                                prevAction = System.currentTimeMillis();
                                aSpecificInstruction = specificInstructions.ARM_DELAY;
                                break;
                            case ARM_DELAY:
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    verticalTargetPos = 1165 + verticalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY1;
                                }
                                break;
                            case DELAY1:
                                if (System.currentTimeMillis() - prevAction > 250) {
//                                    turretTargetPos = 1120 + turretOuttakeOffset;
                                    turretTargetPos = 1260;
                                    clawSpin = 0.69;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.RETRACT_HORIZONTAL_SLIDES;
                                }
                                break;
                            case RETRACT_HORIZONTAL_SLIDES:
                                verticalTargetPos = 700 + verticalIntakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 1260;
                                if (System.currentTimeMillis() - prevAction > 200) {
                                    armTargetPos = 0.5 + armOuttakeOffset;
                                    horizontalTargetPos = 0.1 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.DELAY_TURRET_DEPOSIT;
                                }
                                break;
                            case DELAY_TURRET_DEPOSIT:
                                verticalTargetPos = 700 + verticalIntakeOffset;
                                horizontalTargetPos = 0.1 + horizontalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 1260;
                                if (System.currentTimeMillis() - prevAction > 2000) {
                                    verticalTargetPos = 1720 + verticalOuttakeOffset;
                                    armTargetPos = 0.4 + armOuttakeOffset;
                                    horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                    prevAction = System.currentTimeMillis();
                                    aSpecificInstruction = specificInstructions.LOWER_VERTICAL;
                                }
                                break;
                            case LOWER_VERTICAL:
                                horizontalTargetPos = 0.05 + horizontalOuttakeOffset;
                                verticalTargetPos = 1720 + verticalOuttakeOffset;
//                                turretTargetPos = 1120 + turretOuttakeOffset;
                                turretTargetPos = 1260;
                                armTargetPos = 0.66 + armOuttakeOffset;
                                break;
                        }
                        break;
                }
                break;

            case MID:
                switch (aSpecificInstruction) {
                    case RETRACT_HORIZONTAL_SLIDES:
                        clawSpin = 0.69;
                        armTargetPos = 0.66 + armOuttakeOffset;
                        horizontalTargetPos = 0.05;
                        turretTargetPos = 0 + turretOuttakeOffset;
                        verticalTargetPos = 850 + verticalOuttakeOffset;
                        prevAction = System.currentTimeMillis();
                        break;
                }
                break;

            case BOTTOM:
                switch (aSpecificInstruction) {
                    case RETRACT_HORIZONTAL_SLIDES:
                        clawSpin = 0.69;
                        armTargetPos = 0.66 + armOuttakeOffset;
                        horizontalTargetPos = 0.05;
                        turretTargetPos = 0 + turretOuttakeOffset;
                        verticalTargetPos = 10 + verticalOuttakeOffset;
                        prevAction = System.currentTimeMillis();
                        break;
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
        DESCORE_UP,
        CLOSED,
        INTAKE,
        AUTO_RIGHT_INTAKE,
        OTHER_SIDE,
        LEFT_INTAKE,
        LEFT_STACK_DEPOSIT,
        RIGHT_STACK_DEPOSIT,
        DEPOSIT,
        LEFT_DEPOSIT,
        TOP_STRAIGHT,
        CLOSED_INTAKE,
        NO_EXTEND_INTAKE,
        EXTEND_INTAKE,
        TRANSFORMER_MOVE,
        TELE_STACK_INTAKE,
        STRAIGHT_DEPOSIT,
        AUTO_STRAIGHT_DEPOSIT,
        GROUND_DEPOSIT,
        GROUND_INTAKE,
        DRIVING,
        AUTO_TO_TELE,
        AUTO_CLOSE,
        LEFT_AUTO_CLOSE, AUTO_LEFT_INTAKE,
        TELE_STACK_DEPOSIT,
        PARK,
        AUTO_TURRET_POS,
        LEFT_TELEOP_INTAKE, LAST_CONE,
        PRELOAD,
        SIXTH_CONE,
        DESCORE,
        FALLEN_CONE, FALLEN_CONE_PICK,
    }

    public enum specificInstructions {
        SET_TO_POS,
        ARM_DOWN,
        DESCORE_POSE,
        INITIAL_CLOSE,
        ARM_DELAY,
        DEPOSIT_CONE,
        NO_DEPOSIT_CONE,
        CLOSED_TO_INTAKE,
        CLOSED_INTAKE,
        ARM_DOWN_TO_UP,
        DELAY_DEPOSIT_TO_NO_DEPOSIT,
        TURRET_RESET_DELAY,
        TURRET_RESET_DELAY2,
        TURRET_RESET_DELAY3,
        INTAKE_EXTENSION,
        INCREASE_DEPOSIT_ACCURACY,
        DELAY1,

        //deposit
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
        HOLD_POS_CLOSED,

        OPEN_CLAW_CLOSED

    }
}