package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.newTeleOp.clawAndArm;

@Config
@Disabled
@TeleOp(name = "V4BTest", group = "Test")
public class V4BTest extends LinearOpMode {

    clawAndArm v4b;

    public double endPos1 = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        v4b = new clawAndArm(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                endPos1 += 0.01;
            }
            if (gamepad1.b) {
                endPos1 -= 0.01;
            }

            v4b.armTargetPos(endPos1);

            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
            telemetry.addData("endPos", endPos1);
            telemetry.update();
        }
    }
}
