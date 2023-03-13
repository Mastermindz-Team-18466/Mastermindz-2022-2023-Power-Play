package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.newTeleOp.clawAndArm;

@Config
@TeleOp
public class ClawDistance extends LinearOpMode {

    clawAndArm clawAndArm;

//    public static double endPos = 0.45;
    public static double startPos = 0.44;

    @Override
    public void runOpMode() {
        clawAndArm = new clawAndArm(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            clawAndArm.clawControl(startPos);
            clawAndArm.armTargetPos(0.7);
            clawAndArm.clawSpin(1);
        }
    }
}