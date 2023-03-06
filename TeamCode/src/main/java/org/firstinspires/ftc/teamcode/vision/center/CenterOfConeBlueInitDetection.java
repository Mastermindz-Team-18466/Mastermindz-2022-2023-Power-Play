package org.firstinspires.ftc.teamcode.vision.center;

import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.center.pipeline.CenterOfConeBluePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp
@Disabled
public class CenterOfConeBlueInitDetection extends LinearOpMode {
    OpenCvCamera camera;
    CenterOfConeBluePipeline centerOfConePipeline;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        centerOfConePipeline = new CenterOfConeBluePipeline();
        camera.setPipeline(centerOfConePipeline);

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

        waitForStart();

        while (opModeIsActive())
        {
            if (centerOfConePipeline.x == 0 && centerOfConePipeline.y == 0) {
                telemetry.addLine("Cone of Interest had not been seen");
                telemetry.update();
            }
            else {
                float centerToPipe = 400 - centerOfConePipeline.x;

                telemetry.addLine(String.format("\nCenter X: %.2f", centerOfConePipeline.x));
                telemetry.addLine(String.format("\nCenter Y: %.2f", centerOfConePipeline.y));
                telemetry.update();
            }
        }
    }

}
