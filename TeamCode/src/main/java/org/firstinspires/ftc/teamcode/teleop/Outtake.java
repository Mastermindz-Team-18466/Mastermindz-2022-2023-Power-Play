package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;
import java.util.List;

@Config
public class Outtake {
    public final Turret turret;
    public final V4B v4b;
    public final Claw claw;
    public final HorizontalSlides horizontalSlides;
    public final VerticalSlides verticalSlides;

    public enum outtakePosEnum {
        NEUTRAL,
        GRAB_CLAW,
        PLACE_ON_POLE,
        OPEN_CLAW,
        CLOSE_CLAW
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

    private outtakePosEnum outtakePos;
    private outtakeInstructionsEnum outtakeInstructions;

    HardwareMap hardwareMap;

    private long startTime = System.currentTimeMillis();
    private long prevAction = System.currentTimeMillis();
    public long currentTime = 0;

    public Outtake(HardwareMap hardwareMap, Turret turret, Claw claw, V4B v4b, HorizontalSlides horizontalSlides, VerticalSlides verticalSlides) {
        this.turret = turret;
        this.horizontalSlides = horizontalSlides;
        this.verticalSlides = verticalSlides;

        outtakePos = outtakePosEnum.NEUTRAL;
        outtakeInstructions = outtakeInstructionsEnum.ZEROING_TURRET;
        this.claw = claw;
        this.v4b = v4b;
    }

    public void update() {
        switch (outtakePos) {
            case GRAB_CLAW:
                switch (outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control();
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructions.OPEN_CLAW;
                        break;
                    case OPEN_CLAW:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            claw.control(Claw.State.OPEN);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructions.EXTEND_HORIZONTAL_SLIDES;
                        }
                        break;
                    case EXTEND_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(HorizontalSlides.State.EXTENDED);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructions.CLOSE_CLAW;
                        }
                        break;
                    case CLOSE_CLAW:
                        if (System.currentTimeMillis() - prevAction > 500) {
                            claw.control(Claw.State.CLOSE);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructions.RETRACT_HORIZONTAL_SLIDES;
                        }
                        break;
                    case RETRACT_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(HorizontalSlides.State.RETRACTED);
                        }
                        break;
                }
                break;

            case PLACE_ON_POLE:
                switch (outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control();
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructions.EXTEND_VERTICAL_SLIDES;
                        break;
                    case EXTEND_VERTICAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            verticalSlides.control(VerticalSlides.State.HIGH);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructions.EXTEND_HORIZONTAL_SLIDES;
                        }
                        break;
                    case EXTEND_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 500) {
                            horizontalSlides.control(HorizontalSlides.State.EXTENDED);
                        }
                        break;
                }
                break;

            case NEUTRAL:
                switch (outtakeInstructions) {
                    case CLOSE_CLAW:
                        claw.control(Claw.State.CLOSE);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructions.RETRACT_HORIZONTAL_SLIDES;
                        break;
                    case RETRACT_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(HorizontalSlides.State.RETRACTED);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructions.RETRACT_VERTICAL_SLIDES;
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
}