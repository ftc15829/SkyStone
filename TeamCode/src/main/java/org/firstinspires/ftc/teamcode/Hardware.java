package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.detectors.skystone.SkystoneDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class Hardware {
    // INITIALIZATIONS
    public Hardware(Telemetry telemetry, LinearOpMode linearOpMode) { t = telemetry; opmode = linearOpMode; }
    LinearOpMode opmode;
    Telemetry t;
        // Initialize hardware
    DcMotor drive_lf, drive_rb, drive_rf, drive_lb;
    DcMotor scissor, lSlide_l, lSlide_r;
    Servo fHook_l, fHook_r;
    CRServo grab_l, grab_r;
        // Initialize visual detection
    OpenCvCamera phoneCam;
    SkystoneDetector sDetect;
        // Initialize gamepad values
    double lStick_x, lStick_y, rStick_x, rStick_y, lTrigger, rTrigger; // Gamepad 1
    boolean lBumper, rBumper, button_a, button_b, button_x, button_y;
    double _lStick_x, _lStick_y, _rStick_x, _rStick_y, _lTrigger, _rTrigger; // Gamepad 2
    boolean _lBumper, _rBumper, _button_a, _button_b, _button_x, _button_y;

    void init(HardwareMap hardwareMap) {
        // Defines drive motors
        drive_lf = hardwareMap.dcMotor.get("leftFront");
        drive_rb = hardwareMap.dcMotor.get("rightBack");
        drive_lb = hardwareMap.dcMotor.get("leftBack");
        drive_rf = hardwareMap.dcMotor.get("rightFront");
            // Drive motor setup
        drive_lf.setDirection(DcMotor.Direction.FORWARD);
        drive_rb.setDirection(DcMotor.Direction.REVERSE);
        drive_lb.setDirection(DcMotor.Direction.FORWARD);
        drive_rf.setDirection(DcMotor.Direction.REVERSE);

        // Defines scissor-lift hardware
        scissor = hardwareMap.dcMotor.get("scissor");
        lSlide_l = hardwareMap.dcMotor.get("slideL");
        lSlide_r = hardwareMap.dcMotor.get("slideR");
        fHook_l = hardwareMap.servo.get("hook1");
        fHook_r = hardwareMap.servo.get("hook2");
        grab_l = hardwareMap.crservo.get("block1");
        grab_r = hardwareMap.crservo.get("block2");
    }

    void initAuto(HardwareMap hardwareMap) {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvWebcam(webcamName, cameraMonitorViewId);

        phoneCam.openCameraDevice();

        sDetect = new SkystoneDetector();
        phoneCam.setPipeline(sDetect);

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    // TELEMETRY
    void tDrivePower() {
        t.addData("Main Drive Power",
                String.format("\n| %.2f | %.2f |\n| %.2f | %.2f |",
                drive_lf.getPower(), drive_rf.getPower(),
                drive_rb.getPower(), drive_lb.getPower()));
        t.update();
    } void tErr(String msg, Exception e) {
        t.addData(msg + " Error", "\n" + e);
        t.update();
    }
    void tDrivePos() {
        t.addData("Main Drive Position", String.format("\n| %d | %d |\n| %d | %d |",
                drive_lf.getCurrentPosition(), drive_rf.getCurrentPosition(),
                drive_lb.getCurrentPosition(), drive_rb.getCurrentPosition()));
        t.update();
    }
    void tPos(DcMotor motor) {
        t.addData("Pos", motor.getCurrentPosition());
        t.update();
    }
    void tStatus(String status) {
        t.addData("Status", status);
        t.update();
    }
    void tWebcam() {
        t.addData("Stone Position X", sDetect.getScreenPosition().x);
        t.addData("Stone Position Y", sDetect.getScreenPosition().y);/*
        t.addData("Frame Count", phoneCam.getFrameCount());
        t.addData("FPS", String.format("%.2f", phoneCam.getFps()));
        t.addData("Total frame time ms", phoneCam.getTotalFrameTimeMs());
        t.addData("Pipeline time ms", phoneCam.getPipelineTimeMs());
        t.addData("Overhead time ms", phoneCam.getOverheadTimeMs());
        t.addData("Theoretical max FPS", phoneCam.getCurrentPipelineMaxFps());*/
        t.update();
    }

    // UPDATE GAMEPAD VALUES
    void updateGamepad(Gamepad gamepad1, Gamepad gamepad2) {
        lStick_x = -gamepad1.left_stick_x;
        lStick_y = -gamepad1.left_stick_y;
        rStick_x = -gamepad1.right_stick_x;
//        rStick_y = gamepad1.right_stick_y;
        lTrigger = gamepad1.left_trigger;
        rTrigger = gamepad1.right_trigger;
//        lBumper = gamepad1.left_bumper;
//        rBumper = gamepad1.right_bumper;
//        button_a = gamepad1.a;
        button_b = gamepad1.b;
//        button_x = gamepad1.x;
//        button_y = gamepad1.y;
//        _lStick_x = gamepad2.left_stick_x;
        _lStick_y = gamepad2.left_stick_y;
//        _rStick_x = gamepad2.right_stick_x;
        _rStick_y = gamepad2.right_stick_y;
        _lTrigger = gamepad2.left_trigger;
        _rTrigger = gamepad2.right_trigger;
//        _lBumper = gamepad2.left_bumper;
//        _rBumper = gamepad2.right_bumper;
//        _button_a = gamepad1.a;
//        _button_b = gamepad1.b;
//        _button_x = gamepad1.x;
//        _button_y = gamepad1.y;
    }

}
