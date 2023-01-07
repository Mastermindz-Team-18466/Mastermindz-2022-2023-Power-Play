//package org.firstinspires.ftc.teamcode.testClasses;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.arcrobotics.ftclib.controller.PIDFController;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//@TeleOp
//public class gamepadVertTest extends LinearOpMode {
//
//    private DcMotorEx liftMotor1;
//    private DcMotorEx liftMotor2;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        liftMotor1 = hardwareMap.get(DcMotorEx.class, "liftMotor1");
//        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");
//
//        liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        waitForStart();
//        while (opModeIsActive()) {
//            liftMotor1.setPower(gamepad1.left_stick_y);
//            liftMotor2.setPower(gamepad1.left_stick_y);
//        }
//    }
//}
