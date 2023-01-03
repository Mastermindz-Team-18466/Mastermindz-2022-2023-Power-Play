package org.firstinspires.ftc.teamcode.vision.center;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

    public float centerToPipe = 0;

    public static double p = 0.005, i = 0, d = 0.00000001;
    public static double f = 0;

    public static int target = 400;

    private final double ticks_in_degrees = 384.5 / 180;

    public double power = 0;
    public String direction = "clockwise";


    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        centerOfPipePipeline = new CenterOfPipePipeline();
        camera.setPipeline(centerOfPipePipeline);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });

        initialize();

        waitForStart();

        while (opModeIsActive())
        {
            if (centerOfPipePipeline.x == 0 && centerOfPipePipeline.y == 0) {
                telemetry.addLine("Pipe had not been seen");
                telemetry.update();
            }
            else {
                repeat();
                centerToPipe = 400 - centerOfPipePipeline.x;
                telemetry.addLine(String.format("\nCenter X: %.2f", centerOfPipePipeline.x));
                telemetry.addLine(String.format("\nCenter To Pipe: %.2f", centerToPipe));
                telemetry.addLine(String.format("\nPower: %.2f", power)); //pratham write better code
                if (power < 0) {
                    telemetry.addLine("Direction: counterclockwise");
                } else if (power > 0) {
                    telemetry.addLine("Direction: clockwise");
                } else {
                    telemetry.addLine("In the center!");
                }
                telemetry.update();
            }
        }
    }

    public void initialize() {
        controller = new PIDController(p, i, d);
    }

    public void repeat() {
        controller.setPID(p, i, d);
        double pid = controller.calculate(centerOfPipePipeline.x, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degrees));

        power = pid;
    }
}