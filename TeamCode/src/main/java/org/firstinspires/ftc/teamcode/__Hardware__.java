package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static java.lang.Math.round;

public class __Hardware__ {
	/* Initializations */
	public __Hardware__(Telemetry telemetry, LinearOpMode linearOpMode) { t = telemetry; opmode = linearOpMode; }
	LinearOpMode opmode;
	Telemetry t;
	// Initialize hardware
	DcMotor drive_lf, drive_rb, drive_rf, drive_lb;
	DcMotor scissor, lSlide_l, lSlide_r;
	Servo fHook_l, fHook_r;
	CRServo grab_l, grab_r;

	float SkystonePos;
	float SkystoneLeft;
	float SkystoneRight;
	int SkystoneArea;
	double SkystoneConfidence;
	List<Recognition> updatedRecognitions;
	VuforiaLocalizer vuforia;
	TFObjectDetector tfDetect;
	private static final String VUFORIA_KEY =
			"ARmB8mr/////AAABmZmt2tlP7EgjixU1JYYoSncNXqoxBId990GbqOpAfBytywT8tnE7y51UQmExhGdE3ctKQ5oiMU2LqcaxxW9zPp4+8x4XDsQbYlNwT8uhOE3X+QlME2xhn7unPHRKS9v8bK7R/P+/kmNfzPPDZuPvHSRAYICg6wkLVArTiKP59oP5UN4NZVm7TqE+2bqB3RR9wg9ItU9E8ufs20T8uJpBEzIOk+CMCGvpalbjz+gIv1NDEci9m/z2KMGcmA1bt+XpozDvNEPznZ9enhB9yS3qTDUkNoO/CUndqvMHEfKaTAGnN0oj5ixI3R4fzBx+Xl2LRdUvmav/7CPdnQqt02867My6dezcLg3ovxXMfrtTGgbn";

	// Initialize gamepad values
	double lStick_x, lStick_y, rStick_x, rStick_y, lTrigger, rTrigger; // Gamepad 1
	boolean lBumper, rBumper, button_a, button_b, button_x, button_y;
	double _lStick_x, _lStick_y, _rStick_x, _rStick_y, _lTrigger, _rTrigger; // Gamepad 2
	boolean _lBumper, _rBumper, _button_a, _button_b, _button_x, _button_y;

	/* Init Functions */
	void init(HardwareMap hardwareMap) {
		// Telemetry Configuration
//		t.setAutoClear(false);
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
		/* Initiate Vuforia */
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
		vuforia = ClassFactory.getInstance().createVuforia(parameters);

		/* Initiate TensorFlow Object Detection */
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfodParameters.minimumConfidence = 0.8;
		tfDetect = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfDetect.loadModelFromAsset("Skystone.tflite", "Stone", "Skystone");

		if (tfDetect != null) tfDetect.activate();
	}

	void updateTfDetect() {
		updatedRecognitions = tfDetect.getUpdatedRecognitions();
		if (updatedRecognitions == null) return;
		if (updatedRecognitions.size() > 0) {
			int i = 0;
			for (int j = 0; j < updatedRecognitions.size(); j++) {
				if (updatedRecognitions.get(j).getLabel() == "Skystone") {
					i = j;
					break;
				}
			}
			if (updatedRecognitions.get(i).getLabel() == "Skystone") {
				float objectRight = updatedRecognitions.get(i).getRight();
				float objectLeft = updatedRecognitions.get(i).getLeft();
				float objectHeight = updatedRecognitions.get(i).getHeight();
				float objectWidth = updatedRecognitions.get(i).getWidth();
				float objectConfidence = updatedRecognitions.get(i).getConfidence();

				SkystoneLeft = objectLeft; SkystoneRight = objectRight;
				SkystonePos = objectLeft + (round(100 * ((objectWidth) / 2)) / 100);
				SkystoneArea = round(objectWidth * objectHeight * 100) / 100;
				SkystoneConfidence = objectConfidence; // Currently only returns 0.87890625
			}
		} else if (updatedRecognitions.size() == 0) {
			SkystonePos = 0;
			SkystoneArea = 0;
			SkystoneConfidence = 0;
		}
	}

	/* Telemetry */
	void tDrivePower() {
		t.addData("Main Drive Power",
				String.format("\n| %.2f | %.2f |\n| %.2f | %.2f |",
						(float)(drive_lf.getPower()), (float)(drive_rf.getPower()),
						(float)(drive_rb.getPower()), (float)(drive_lb.getPower())));
		t.update();
	}
	void tErr(String msg, Exception e) {
		t.addData(msg + " Error", "\n" + e);
		t.update();
	}
	void tDrivePos() {
		t.addData("Main Drive Position", String.format("\n| %.2f | %.2f |\n| %.2f | %.2f |",
				(float)(drive_lf.getCurrentPosition() / 560), (float)(drive_rf.getCurrentPosition() / 560),
				(float)(drive_lb.getCurrentPosition() / 560), (float)(drive_rb.getCurrentPosition() / 560)));
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
	void tSub(String sub) {
		t.addData("Sub", sub);
		t.update();
	}
	void tCaminfo() {
		t.addData("Pos", SkystonePos);
		t.addData("left", SkystoneLeft);
		t.addData("right", SkystoneRight);
		t.addData("Area", SkystoneArea);
		t.addData("Confidence", SkystoneConfidence);
		t.update();
	}
	void tRunTime() {
		t.addData("Time", opmode.getRuntime());
		t.update();
	}

	/* Update Gamepad Values */
	void updateGamepad(Gamepad gamepad1, Gamepad gamepad2) {
		// Gamepad1
		lStick_x = -gamepad1.left_stick_x;
		lStick_y = -gamepad1.left_stick_y;
		rStick_x = -gamepad1.right_stick_x;
        rStick_y = gamepad1.right_stick_y;

		lTrigger = gamepad1.left_trigger;
		rTrigger = gamepad1.right_trigger;

        lBumper = gamepad1.left_bumper;
        rBumper = gamepad1.right_bumper;

        button_a = gamepad1.a;
		button_b = gamepad1.b;
        button_x = gamepad1.x;
        button_y = gamepad1.y;

        // Gamepad2
        _lStick_x = gamepad2.left_stick_x;
		_lStick_y = gamepad2.left_stick_y;
        _rStick_x = gamepad2.right_stick_x;
		_rStick_y = gamepad2.right_stick_y;

		_lTrigger = gamepad2.left_trigger;
		_rTrigger = gamepad2.right_trigger;

        _lBumper = gamepad2.left_bumper;
        _rBumper = gamepad2.right_bumper;

        _button_a = gamepad1.a;
        _button_b = gamepad1.b;
        _button_x = gamepad1.x;
        _button_y = gamepad1.y;
	}
}
