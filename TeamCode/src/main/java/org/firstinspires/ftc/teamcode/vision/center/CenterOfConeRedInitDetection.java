package org.firstinspires.ftc.teamcode.vision.center;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.center.pipeline.CenterOfConeRedPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp
public class CenterOfConeRedInitDetection extends LinearOpMode {
    OpenCvCamera camera;
    CenterOfConeRedPipeline centerOfConePipeline;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        centerOfConePipeline = new CenterOfConeRedPipeline();

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

        telemetry.setMsTransmissionInterval(50);

        while (!isStarted() && !isStopRequested())
        {
            telemetry.addLine(String.format("\nCenter X: %.2f", centerOfConePipeline.x));
            telemetry.addLine(String.format("\nCenter Y: %.2f", centerOfConePipeline.y));
            telemetry.update();
            sleep(20);
        }

        while (opModeIsActive()) {sleep(20);}
    }

}
