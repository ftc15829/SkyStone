package org.firstinspires.ftc.teamcode;
import android.sax.TextElementListener;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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
	public __Hardware__(LinearOpMode linearOpMode, Telemetry telemetry) { opmode = linearOpMode; t = telemetry; }
	LinearOpMode opmode;
	Telemetry t;
	// Initialize hardware
	int drive_ticks = 108;
	double powerbase = .36;
	DcMotor drive_lf, drive_rb, drive_rf, drive_lb;
	DcMotor scissor, lSlide_l, lSlide_r;
	Servo fHook_l, fHook_r;
	CRServo grab_l, grab_r;
	// Initialize Vision
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
	boolean dpad_u, dpad_d, dpad_l, dpad_r;
	double _lStick_x, _lStick_y, _rStick_x, _rStick_y, _lTrigger, _rTrigger; // Gamepad 2
	boolean _lBumper, _rBumper, _button_a, _button_b, _button_x, _button_y;
	boolean _dpad_u, _dpad_d, _dpad_l, _dpad_r;

	// Telemetry
//	Telemetry.Item status = t.addData("Status", "");
//	Telemetry.Item subStatus = t.addData("Sub", "");

	/* Init Functions */
	void init(HardwareMap hardwareMap) {
		// Telemetry Configuration
//		t.setAutoClear(true);
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
	void tStatus(String value) {
//		status.setValue(value);
		t.update();
	}
	void tSub(String value) {
//		subStatus.setValue(value);
		t.update();
	}
	void tErr(String msg, Exception e) {
		t.addData(msg + " Error", "\n" + e);
		t.update();
	}

	void tRunTime(int update) {
		t.addData("Runtime", opmode.getRuntime());
		if (update == 1)
			t.update();
	} void tRunTime() { tRunTime(0); }

	void tRunTime(ElapsedTime elapsedTime, int update) {
		t.addData("Elapsed Time", elapsedTime.seconds());
		if (update == 1)
			t.update();
	} void tRunTime(ElapsedTime elapsedTime) { tRunTime(elapsedTime, 0); }

	void tSnapRuntime(ElapsedTime elapsedTime) {
		Telemetry.Item runTimeCP = t.addData("RunTimeCP", elapsedTime.seconds());
		t.update();
	}
	void tSnapDrivePos() {
//		Telemetry.Item drivePosCP = t.addData("DrivePosCP", String.format("\n| %6d | %6d |\n| %6d | %6d |",
//				drive_lf.getCurrentPosition() / drive_ticks, drive_rf.getCurrentPosition() / drive_ticks,
//				drive_lb.getCurrentPosition() / drive_ticks, drive_rb.getCurrentPosition() / drive_ticks));
		t.update();
	}

	void tDrivePower(int update) {
//		t.addData("Drive Power",
//				String.format("\n| %6d | %6d |\n| %6d | %6d |",
//						drive_lf.getPower(), drive_rf.getPower(),
//						drive_rb.getPower(), drive_lb.getPower()));
		if (update == 1)
			t.update();
	} void tDrivePower() { tDrivePower(0); }

	void tDrivePos(int update) {
//		t.addData("Drive Position", String.format("\n| %6d | %6d |\n| %6d | %6d |",
//				drive_lf.getCurrentPosition() / drive_ticks, drive_rf.getCurrentPosition() / drive_ticks,
//				drive_lb.getCurrentPosition() / drive_ticks, drive_rb.getCurrentPosition() / drive_ticks));
		if (update == 1)
			t.update();
	} void tDrivePos() { tDrivePos(0); }

	void tPos(DcMotor motor, int update) {
		t.addData("Pos", motor.getCurrentPosition());
		if (update == 1)
			t.update();
	} void tPos(DcMotor motor) { tPos(motor, 0); }

	void tCaminfo(int update) {
		t.addData("Pos", SkystonePos);
		t.addData("left", SkystoneLeft);
		t.addData("right", SkystoneRight);
		t.addData("Area", SkystoneArea);
		t.addData("Confidence", SkystoneConfidence);
		if (update == 1)
			t.update();
	} void tCaminfo() { tCaminfo(0); }

	/* Update Gamepad Values */
	void updateGamepad(Gamepad gamepad1, Gamepad gamepad2) {
		// Gamepad1
		lStick_x = -gamepad1.left_stick_x;
		lStick_y = -gamepad1.left_stick_y;
		rStick_x = -gamepad1.right_stick_x;
        rStick_y = gamepad1.right_stick_y;

		dpad_u = gamepad1.dpad_up;
		dpad_d = gamepad1.dpad_down;
		dpad_l = gamepad1.dpad_left;
		dpad_r = gamepad1.dpad_right;

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

		_dpad_u = gamepad2.dpad_up;
		_dpad_u = gamepad2.dpad_down;
		_dpad_u = gamepad2.dpad_left;
		_dpad_u = gamepad2.dpad_right;

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
