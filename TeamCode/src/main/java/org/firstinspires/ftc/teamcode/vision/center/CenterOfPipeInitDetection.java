package org.firstinspires.ftc.teamcode.vision.center;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.newTeleOp.newTurret;
import org.firstinspires.ftc.teamcode.vision.center.pipeline.CenterOfPipePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
@TeleOp
public class CenterOfPipeInitDetection extends LinearOpMode {
    OpenCvCamera camera;
    CenterOfPipePipeline centerOfPipePipeline;

    public double centerToPipe = 0;

    public double power = 0;

    double pos;

    newTurret turret;

    boolean notDone = true;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
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
            public void onError(int errorCode)
            {

            }
        });

        turret = new newTurret(hardwareMap);

        pos = turret.turretMotor.getCurrentPosition();

        System.out.println("craxytets: " + pos);

        waitForStart();

        while (opModeIsActive())
        {
            if (notDone) {
                double previousCenter = centerOfPipePipeline.x;

                if (previousCenter < 0.5) {
                    turret.set(pos-100);
                } else {
                    turret.set(pos+100);
                }

                double newCenter = centerOfPipePipeline.x;

                double difference = newCenter - previousCenter;

                double left = 0.5 - newCenter;

                double positionDiff = left / difference * 100;

                telemetry.addData("pd", positionDiff);

                //turret.set(pos + positionDiff);

                notDone = false;
            }

            if (centerOfPipePipeline.x == 0 && centerOfPipePipeline.y == 0) {
                telemetry.addLine("Pipe had not been seen");
                telemetry.update();
            }
            else {
                telemetry.addLine(String.format("\nCenter X: %.2f", centerOfPipePipeline.x / 800));
                telemetry.addLine(String.format("\nCenter To Pipe: %.2f", centerToPipe));
                telemetry.addLine(String.format("\nPower: %.2f", power));

                telemetry.update();
            }
        }
    }
}