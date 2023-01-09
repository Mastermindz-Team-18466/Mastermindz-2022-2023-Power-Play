package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Outtake {
    public final Turret turret;
    public final V4B v4b;
    public final Claw claw;
    public final HorizontalSlides horizontalSlides;
    public final VerticalSlides verticalSlides;
    public long currentTime = 0;
    HardwareMap hardwareMap;
    private outtakePosEnum outtakePos;
    private outtakeInstructionsEnum outtakeInstructions;
    private final long startTime = System.currentTimeMillis();
    private long prevAction = System.currentTimeMillis();
    public boolean left;

    public Outtake(boolean left, HardwareMap hardwareMap, Turret turret, Claw claw, V4B v4b, HorizontalSlides horizontalSlides, VerticalSlides verticalSlides) {
        this.turret = turret;
        this.horizontalSlides = horizontalSlides;
        this.verticalSlides = verticalSlides;

        outtakePos = outtakePosEnum.NEUTRAL;
        outtakeInstructions = outtakeInstructionsEnum.ZEROING_TURRET;
        this.claw = claw;
        this.v4b = v4b;
        this.left = left;
    }

    public void update() {
        switch (outtakePos) {
            case GRAB_CLAW:
                switch (outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control(left);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.EXTEND_HORIZONTAL_SLIDES;
                        break;
                    case EXTEND_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(HorizontalSlides.State.EXTENDED, true, left);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.CLOSE_CLAW;
                        }
                        break;
                    case CLOSE_CLAW:
                        if (System.currentTimeMillis() - prevAction > 500) {
                            claw.control(Claw.State.CLOSE);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.RETRACT_HORIZONTAL_SLIDES;
                        }
                        break;
                    case RETRACT_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(HorizontalSlides.State.RETRACTED, false, left);
                        }
                        break;
                }
                break;

            case REDO_FOR_GRAB:
                switch(outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control(left);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.EXTEND_VERTICAL_SLIDES;
                        break;
                    case RETRACT_VERTICAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            verticalSlides.control(VerticalSlides.State.BOTTOM);
                        }
                        break;
                }
                break;

            case PLACE_ON_POLE:
                switch (outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control(left);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.EXTEND_VERTICAL_SLIDES;
                        break;
                    case EXTEND_VERTICAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            verticalSlides.control(VerticalSlides.State.HIGH);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.EXTEND_HORIZONTAL_SLIDES;
                        }
                        break;
                    case EXTEND_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 500) {
                            horizontalSlides.control(HorizontalSlides.State.EXTENDED, false, left);
                        }
                        break;
                }
                break;

            case NEUTRAL:
                switch (outtakeInstructions) {
                    case CLOSE_CLAW:
                        claw.control(Claw.State.CLOSE);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.RETRACT_HORIZONTAL_SLIDES;
                        break;
                    case RETRACT_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(HorizontalSlides.State.RETRACTED, false, left);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.RETRACT_VERTICAL_SLIDES;
                        }
                        break;
                    case RETRACT_VERTICAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            verticalSlides.control(VerticalSlides.State.BOTTOM);
                        }
                        break;
                }
                break;

            case OPEN_CLAW:
                switch (outtakeInstructions) {
                    case OPEN_CLAW:
                        claw.control(Claw.State.OPEN);
                        break;
                }
                break;

            case CLOSE_CLAW:
                switch (outtakeInstructions) {
                    case CLOSE_CLAW:
                        claw.control(Claw.State.CLOSE);
                        break;
                }
                break;
        }
    }

    public void setOuttakePos(outtakePosEnum pos) {
        outtakePos = pos;
    }

    public void setOuttakeInstructions(outtakeInstructionsEnum instruction) {
        outtakeInstructions = instruction;
    }

    public enum outtakePosEnum {
        NEUTRAL,
        GRAB_CLAW,
        PLACE_ON_POLE,
        OPEN_CLAW,
        CLOSE_CLAW,
        REDO_FOR_GRAB
    }

    public enum outtakeInstructionsEnum {
        // Grabbing Cone
        TURN_TURRET,
        OPEN_CLAW,
        EXTEND_HORIZONTAL_SLIDES,
        CLOSE_CLAW,
        RETRACT_HORIZONTAL_SLIDES,

        // Place on Pole
        EXTEND_VERTICAL_SLIDES,
        RETRACT_VERTICAL_SLIDES,
        ZEROING_TURRET,
    }
}