package org.firstinspires.ftc.teamcode.testClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class analogPortTest extends LinearOpMode {

    private AnalogInput analogInput;
    private DcMotor turretMotor;


    @Override
    public void runOpMode() throws InterruptedException {
        turretMotor = hardwareMap.get(DcMotor.class, "turret");
        analogInput = hardwareMap.get(AnalogInput.class, "analog_input");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                turretMotor.setPower(0.4);
            } else if (gamepad1.b) {
                turretMotor.setPower(-0.4);
            } else {
                turretMotor.setPower(0);
            }
            telemetry.addData("TurretVoltage", analogInput.getVoltage());
            telemetry.update();
        }
    }
}
