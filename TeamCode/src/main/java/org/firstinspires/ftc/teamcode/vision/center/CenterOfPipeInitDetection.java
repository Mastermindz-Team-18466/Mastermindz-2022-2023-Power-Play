package org.firstinspires.ftc.teamcode.vision.center;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.center.pipeline.CenterOfConeBluePipeline;
import org.firstinspires.ftc.teamcode.vision.center.pipeline.CenterOfPipePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
@TeleOp
public class CenterOfPipeInitDetection extends LinearOpMode {
    OpenCvCamera camera;
    CenterOfPipePipeline centerOfPipePipeline;

    private PIDController controller;

    public double centerToPipe = 0;

    public static double p = 0.004, i = 0, d = 0.0001;
    public static double f = 0.1;

    public static double targetVoltage = 0.04;

    private final double ticks_in_degrees = 384.5 / 180;

    public double power = 0;

    DcMotor turretMotor;
    AnalogInput analogInput;


    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        centerOfPipePipeline = new CenterOfPipePipeline();
        camera.setPipeline(centerOfPipePipeline);

        turretMotor = hardwareMap.get(DcMotor.class, "turret");
        analogInput = hardwareMap.get(AnalogInput.class, "analog_input");

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        initialize();

        waitForStart();

        if (gamepad1.a) {
            targetVoltage = 0;
        }

        else if (gamepad1.b) {
            targetVoltage = 3.3 / 2;
        }

        else if (gamepad1.x) {
            targetVoltage = 3.3 * 3 / 4;
        }

        while (opModeIsActive())
        {
            if (centerOfPipePipeline.x == 0 && centerOfPipePipeline.y == 0) {
                telemetry.addLine("Pipe had not been seen");
                telemetry.update();
            }
            else {
                repeat();
                centerToPipe = 3.3 / 2 - analogInput.getVoltage();
                telemetry.addLine(String.format("\nCenter X: %.2f", centerOfPipePipeline.x / 800 * 3.3));
                telemetry.addLine(String.format("\nCenter To Pipe: %.2f", centerToPipe));
                telemetry.addLine(String.format("\nPower: %.2f", power));

                turretMotor.setPower(power);

                telemetry.update();
            }
        }
    }

    public void initialize() {
        controller = new PIDController(p, i, d);
    }

    public void repeat() {
        controller.setPIDF(p, i, d, f);
        double pid = controller.calculate(centerOfPipePipeline.x, targetVoltage);
        double ff = Math.cos(Math.toRadians(targetVoltage / ticks_in_degrees));

        power = pid;
    }
}