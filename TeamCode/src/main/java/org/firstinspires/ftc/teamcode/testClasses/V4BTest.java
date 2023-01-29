package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.newTeleOp.clawAndV4B;

@Config
@TeleOp(name = "V4BTest", group = "Test")
public class V4BTest extends LinearOpMode {

    clawAndV4B v4b;

    public double endPos1 = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        v4b = new clawAndV4B(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                endPos1 += 0.01;
            }
            if (gamepad1.b) {
                endPos1 -= 0.01;
            }

            v4b.v4bTargetPos(endPos1);

            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
            telemetry.addData("endPos", endPos1);
            telemetry.update();
        }
    }
}
