package org.firstinspires.ftc.teamcode.vision.center;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@TeleOp
@Disabled
public class CenterOfPipeNoWebcam extends LinearOpMode {

    private PIDController controller;

    public double centerToPipe = 0;

    public static double p = 0.004, i = 0, d = 0.0001;
    public static double f = 0.1;

    public static double targetVoltage = 0.04;

    private final double ticks_in_degrees = 384.5 / 180;

    public double power = 0;
    public String direction = "clockwise";

    DcMotor turretMotor;
    AnalogInput analogInput;


    @Override
    public void runOpMode()
    {

        turretMotor = hardwareMap.get(DcMotor.class, "turret");
        analogInput = hardwareMap.get(AnalogInput.class, "analog_input");

        initialize();

        waitForStart();

        while (opModeIsActive())
        {
            if (gamepad1.a) {
                targetVoltage = 0;
            }

            else if (gamepad1.b) {
                targetVoltage = 3.3 / 2;
            }

            else if (gamepad1.x) {
                targetVoltage = 3.3 * 3 / 4;
            }

            repeat();
            centerToPipe = 3.3 / 2 - analogInput.getVoltage();
            telemetry.addLine(String.format("\nVoltage: %.2f", analogInput.getVoltage()));
            telemetry.addLine(String.format("\nPower: %.2f", power));

            turretMotor.setPower(power);

            telemetry.update();
        }
    }

    public void initialize() {
        controller = new PIDController(p, i, d);
    }

    public void repeat() {
        double error = targetVoltage - analogInput.getVoltage();
        double pid = controller.calculate(error);
        double ff = Math.cos(Math.toRadians(targetVoltage / ticks_in_degrees));

        power = pid + ff;
    }
}