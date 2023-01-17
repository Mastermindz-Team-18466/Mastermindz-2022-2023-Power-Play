package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.teleop.HorizontalSlides;

/*
Variables:
update() method is the main method for controlling the outtake system of the robot.
outtakePos and outtakeInstructions are enums that are used to keep track of the current state of the outtake system.
currentTime variable is used to keep track of the current time, which is used to time certain actions in the outtake system.
prevAction variable keeps track of the time of the previous action, which is used to time certain actions in the outtake system.

Switch and Case Statement:
switch(outtakePos) is used to determine which state the outtake system is currently in.
switch(outtakeInstructions) is used to determine which instruction to execute in the current state.

Components:
turret, v4b, claw, horizontalSlides, verticalSlides are objects representing different subsystems of the robot.
driver is a TeleOpFieldCentric object which is used to get the robot's current position and orientation.

Time-based Instructions:
if (System.currentTimeMillis() - prevAction > 250) is used to time certain actions in the outtake system, waiting 250 milliseconds before proceeding to the next instruction.

Commands:
horizontalSlides.control(driver.drive.getPoseEstimate(), true) is used to extend the horizontal slides to grab the object.
verticalSlides.control(VerticalSlides.State.BOTTOM) is used to lower the vertical slides to the bottom position.
claw.control(Claw.State.CLOSE) is used to close the claw to grab the object.
horizontalSlides.left_servo.setPosition(0.37) and horizontalSlides.right_servo.setPosition(0.27) are used to retract the horizontal slides after the object is grabbed.
 */

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
    private TeleOpFieldCentric driver;
    private final long startTime = System.currentTimeMillis();
    private long prevAction = System.currentTimeMillis();
    public static boolean left;

    public Outtake(HardwareMap hardwareMap, TeleOpFieldCentric driver, Turret turret, Claw claw, V4B v4b, HorizontalSlides horizontalSlides, VerticalSlides verticalSlides) {
        this.turret = turret;
        this.horizontalSlides = horizontalSlides;
        this.verticalSlides = verticalSlides;
        this.driver = driver;

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
                        turret.control(driver.drive.getPoseEstimate());
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.EXTEND_HORIZONTAL_SLIDES;
                        break;
                    case EXTEND_HORIZONTAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            horizontalSlides.control(driver.drive.getPoseEstimate(), true);
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
                            horizontalSlides.left_servo.setPosition(0.37);
                            horizontalSlides.right_servo.setPosition(0.27);

                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        }
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
                        break;
                }
                break;

            case REDO_FOR_GRAB:
                switch(outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control(driver.drive.getPoseEstimate());
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.RETRACT_VERTICAL_SLIDES;
                        break;
                    case RETRACT_VERTICAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            verticalSlides.control(VerticalSlides.State.BOTTOM);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        }
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
                        break;
                }
                break;

            case PLACE_ON_POLE:
                switch (outtakeInstructions) {
                    case TURN_TURRET:
                        turret.control(driver.drive.getPoseEstimate());
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
                            horizontalSlides.control(driver.drive.getPoseEstimate(), false);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        }
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
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
                            horizontalSlides.left_servo.setPosition(0.37);
                            horizontalSlides.right_servo.setPosition(0.27);

                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.RETRACT_VERTICAL_SLIDES;
                        }
                        break;
                    case RETRACT_VERTICAL_SLIDES:
                        if (System.currentTimeMillis() - prevAction > 250) {
                            verticalSlides.control(VerticalSlides.State.BOTTOM);
                            prevAction = System.currentTimeMillis();
                            outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        }
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
                        break;
                }
                break;

            case OPEN_CLAW:
                switch (outtakeInstructions) {
                    case OPEN_CLAW:
                        claw.control(Claw.State.OPEN);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
                        break;
                }
                break;

            case CLOSE_CLAW:
                switch (outtakeInstructions) {
                    case CLOSE_CLAW:
                        claw.control(Claw.State.CLOSE);
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
                        break;
                }
                break;

            case RESET_TURRET:
                switch(outtakeInstructions) {
                    case RESET_TURRET:
                        turret.reset();
                        prevAction = System.currentTimeMillis();
                        outtakeInstructions = outtakeInstructionsEnum.NOTHING;
                        break;
                    case NOTHING:
                        if (System.currentTimeMillis() - prevAction > 500) {}
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
        REDO_FOR_GRAB,
        RESET_TURRET,
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

        RESET_TURRET,
        NOTHING
    }
}