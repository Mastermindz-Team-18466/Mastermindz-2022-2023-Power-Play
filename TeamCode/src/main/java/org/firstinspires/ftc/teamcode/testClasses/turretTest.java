package org.firstinspires.ftc.teamcode.testClasses;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.newTeleOp.newTurret;

@TeleOp
@Config
public class turretTest extends LinearOpMode {

//    public static double Kp = 0.02;
//    public static double Ki = 0.00;
//    public static double Kd = 0.00003;
//    public static double Kf = 0.1;
//    public static double targetPosition = 0;
//
//    private DcMotorEx turretMotor;

    newTurret turret;

    @Override
    public void runOpMode() throws InterruptedException {
//        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor");
//        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        double integral = 0;
//        double previousError = 0;
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        turret = new newTurret(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
//            double error  = targetPosition - turretMotor.getCurrentPosition();
//            integral = integral + error;
//            double derivative = error - previousError;
//            previousError = error;
//
//            double power = Kp * error + Ki * integral + Kd * derivative + Kf;
//
//            turretMotor.setPower(power / 1.5);
//
//            telemetry.addData("Current pos", turretMotor.getCurrentPosition());
//            telemetry.addData("Power", power);
//            telemetry.addData("Error", error);
//            telemetry.addData("Target Pos", targetPosition);

            turret.set(100);

            telemetry.update();
        }
    }
}
