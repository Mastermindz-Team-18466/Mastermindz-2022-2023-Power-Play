package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.newTeleOp.clawAndArm;

@Config
@TeleOp
public class ArmTest extends LinearOpMode {

    clawAndArm clawAndArm;

    public static double endPos = 0;
//    public static double startPos = 1;

    @Override
    public void runOpMode() {
        clawAndArm = new clawAndArm(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            clawAndArm.armTargetPos(endPos);
        }
    }
}