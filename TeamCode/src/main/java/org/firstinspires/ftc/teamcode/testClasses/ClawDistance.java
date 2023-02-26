package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.newTeleOp.clawAndArm;

@Config
@TeleOp
public class ClawDistance extends LinearOpMode {

    clawAndArm clawAndArm;

    public static double endPos = 0.45;
    public static double startPos = 0.22;
    public static double armPos = 0.25;

    @Override
    public void runOpMode() {
        clawAndArm = new clawAndArm(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while (opModeIsActive()) {
            telemetry.update();
            clawAndArm.armTargetPos(armPos);
            if (gamepad1.a) {
                clawAndArm.clawControl(endPos);
            } else if (gamepad1.b) {  // Otherwise, stop the motor
                clawAndArm.clawControl(startPos);
            }
        }
    }
}