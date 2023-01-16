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

    @Override
    public void runOpMode() throws InterruptedException {
        v4b = new clawAndV4B(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                v4b.v4bTargetPos(0.27);
            } else if (gamepad1.b) {
                v4b.v4bTargetPos(0.72);
            }
        }
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Running:", telemetry);
        telemetry.update();
    }
}
