package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
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
	public __Hardware__(LinearOpMode linearOpMode, Telemetry telemetry) {
		opmode = linearOpMode;
		t = telemetry;
	}
	LinearOpMode opmode;
	Telemetry t;
	private HardwareMap hMap;

	// Hardware Constants
	static double GobRate = 108.0; // Double to avoid integer division
	static double TetRate = 1440.0;
	static double PowerMod = .333; // Max power is 3.0
	double voltage = Double.POSITIVE_INFINITY;

	// Initialize hardware
	DcMotor drive_lf, drive_rb, drive_rf, drive_lb;
	DcMotor scissor, lSlide_l, lSlide_r, autoHand;
	Servo fHook_l, fHook_r, autoClamp;
	CRServo grab_l, grab_r, bHook_l, bHook_r;

	// Initialize Vision
	TFObjectDetector tfDetect;
	float sPos;
	float sLeft;
	float sRight;
	int sArea;
	double sConf;
	private List<Recognition> updatedRecognitions;
	private VuforiaLocalizer vuforia;
	private static final String VUFORIA_KEY = "ARmB8mr/////AAABmZmt2tlP7EgjixU1JYYoSncNXqoxBId990GbqOpAfBytywT8tnE7y51UQmExhGdE3ctKQ5oiMU2LqcaxxW9zPp4+8x4XDsQbYlNwT8uhOE3X+QlME2xhn7unPHRKS9v8bK7R/P+/kmNfzPPDZuPvHSRAYICg6wkLVArTiKP59oP5UN4NZVm7TqE+2bqB3RR9wg9ItU9E8ufs20T8uJpBEzIOk+CMCGvpalbjz+gIv1NDEci9m/z2KMGcmA1bt+XpozDvNEPznZ9enhB9yS3qTDUkNoO/CUndqvMHEfKaTAGnN0oj5ixI3R4fzBx+Xl2LRdUvmav/7CPdnQqt02867My6dezcLg3ovxXMfrtTGgbn";

	// Initialize gamepad values
	double lStick_x, lStick_y, rStick_x, rStick_y, lTrigger, rTrigger; // Gamepad 1
	boolean lBumper, rBumper, button_a, button_b, button_x, button_y;
	boolean dpad_u, dpad_d, dpad_l, dpad_r;
	double _lStick_x, _lStick_y, _rStick_x, _rStick_y, _lTrigger, _rTrigger; // Gamepad 2
	boolean _lBumper, _rBumper, _button_a, _button_b, _button_x, _button_y;
	boolean _dpad_u, _dpad_d, _dpad_l, _dpad_r;

	// Initialize Telemetry
	Telemetry.Item status;
	Telemetry.Item subStatus;

	/* Init Functions */
	void initTelemetry() {
		t.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
		t.log().setCapacity(7);
		status = t.addData("Status", "---");
		subStatus = t.addData("Sub", "---");
		subStatus.setRetained(true);
		status.setRetained(true);
		t.update();
	}
	void initHardware(HardwareMap hardwareMap) {
		hMap = hardwareMap;
		// Fetch configuration
		drive_lf = hMap.dcMotor.get("leftFront"); // Left-Front Drive Motor
		drive_rf = hMap.dcMotor.get("rightFront"); // Right Front Drive Motor
		drive_lb = hMap.dcMotor.get("leftBack"); // Left-Back Drive Motor
		drive_rb = hMap.dcMotor.get("rightBack"); // Right-Back Drive Motor

		scissor = hMap.dcMotor.get("scissor"); // Main Scissor Lift Motor
		lSlide_l = hMap.dcMotor.get("slideL"); // Left Platform Aux Motor
		lSlide_r = hMap.dcMotor.get("slideR"); // Right Platform Aux Motor

		fHook_l = hMap.servo.get("hook1"); // Left-Front Foundation Servo
		fHook_r = hMap.servo.get("hook2"); // Right-Front Foundation Servo
		bHook_l = hMap.crservo.get("fHookLeft"); // Left-Back Foundation Servo
		bHook_r = hMap.crservo.get("fHookRight"); // Right-Back Foundation Servo

		autoClamp = hMap.servo.get("autoClamp"); // Left-Front Drive Motor
		autoHand = hMap.dcMotor.get("autoHand"); // Left-Front Drive Motor
		grab_l = hMap.crservo.get("block1"); // Left-Front Drive Motor
		grab_r = hMap.crservo.get("block2"); // Left-Front Drive Motor

		// Drive motor setup
		drive_lf.setDirection(DcMotor.Direction.FORWARD);
		drive_rf.setDirection(DcMotor.Direction.REVERSE);
		drive_lb.setDirection(DcMotor.Direction.FORWARD);
		drive_rb.setDirection(DcMotor.Direction.REVERSE);
		drive_lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		drive_rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		drive_lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		drive_rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}
	void initVision() {
		/* Initiate Vuforia */
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraName = hMap.get(WebcamName.class, "Webcam 1");
		vuforia = ClassFactory.getInstance().createVuforia(parameters);

		/* Initiate TensorFlow Object Detection */
		int tfodMonitorViewId = hMap.appContext.getResources().getIdentifier(
				"tfodMonitorViewId", "id", hMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfodParameters.minimumConfidence = 0.7;
		tfDetect = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfDetect.loadModelFromAsset("Skystone.tflite", "Stone", "Skystone");

		if (tfDetect != null) tfDetect.deactivate();
	}

	void updateTfDetect() {
		updatedRecognitions = tfDetect.getUpdatedRecognitions();
		if (updatedRecognitions == null) return;
		if (updatedRecognitions.size() > 0) {
			int i = 0;
			for (int j = 0; j < updatedRecognitions.size(); j++) {
				if (updatedRecognitions.get(j).getLabel().equals("Skystone")) {
					i = j;
					break;
				}
			}
			if (updatedRecognitions.get(i).getLabel().equals("Skystone")) {
				float objectRight = updatedRecognitions.get(i).getRight();
				float objectLeft = updatedRecognitions.get(i).getLeft();
				float objectHeight = updatedRecognitions.get(i).getHeight();
				float objectWidth = updatedRecognitions.get(i).getWidth();
				float objectConfidence = updatedRecognitions.get(i).getConfidence();

				sLeft = objectLeft;
				sRight = objectRight;
				sPos = objectLeft + (100 * ((objectWidth) / 2)) / 100;
				sArea = round(objectWidth * objectHeight * 100) / 100;
				sConf = objectConfidence; // Currently only returns 0.87890625
			}
		} else {
			sLeft = 0; sRight = 0; sPos = 0; sArea = 0; sConf = 0;
		}
	}

	void updateBatteryVoltage() {
		double result = Double.POSITIVE_INFINITY;
		for (VoltageSensor sensor : hMap.voltageSensor) {
			double sensorVoltage = sensor.getVoltage();
			if (sensorVoltage > 0) {
				result = Math.min(result, sensorVoltage);
			}
		}
		voltage = result;
	}

	/* Telemetry */
	void update() { t.update(); }
	void tStatus(String value) {
		status.setValue(value);
	}
	void tSub(String value) {
		subStatus.setValue(value);
	}

	void tLog(String value) {
		t.log().add(value);
	}

	void tErr(String msg, Exception e) {
		t.addData(msg + " Error", "\n" + e);
	}

	void except(Exception e) {
		// Catches exceptions as plain-text
		tStatus("Error");
		tErr("Runtime", e);
		update();
		opmode.sleep(15_000);
		opmode.stop();
	}

	void tRunTime() {
		t.addData("Runtime", String.format("%3.2f", opmode.getRuntime()));
	}

	void tRunTime(ElapsedTime elapsedTime) {
		t.addData("Elapsed Time", elapsedTime.seconds());
	}

	void tSnapRuntime(ElapsedTime elapsedTime) {
		Telemetry.Item runTimeCP = t.addData("RunTimeCP", elapsedTime.seconds());
	}
	void tSnapDrivePos() {
//		Telemetry.Item drivePosCP = t.addData("DrivePosCP", String.format("\n| %3.2d | %3.2d | %3.2d | %3.2d |",
//				drive_lf.getCurrentPosition() / GobRate, drive_rf.getCurrentPosition() / GobRate,
//				drive_lb.getCurrentPosition() / GobRate, drive_rb.getCurrentPosition() / GobRate));
	}

	void tDrivePower() {
		t.addData("Drive Power", String.format("| %3.2f | %3.2f | %3.2f | %3.2f |",
						drive_lf.getPower(), drive_rf.getPower(),
						drive_rb.getPower(), drive_lb.getPower()));
	}

	void tDrivePos() {
		t.addData("Drive Position", String.format("| %6d | %6d | %6d | %6d |",
				drive_lf.getCurrentPosition() / GobRate, drive_rf.getCurrentPosition() / GobRate,
				drive_lb.getCurrentPosition() / GobRate, drive_rb.getCurrentPosition() / GobRate));
	}

	void tPos(DcMotor motor) {
		t.addData("Pos", motor.getCurrentPosition());
	}

	void tCaminfo() {
		t.addData("Pos", sPos);
		t.addData("left", sLeft);
		t.addData("right", sRight);
		t.addData("Area", sArea);
		t.addData("Confidence", sConf);
	}

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
		_dpad_d = gamepad2.dpad_down;
		_dpad_l = gamepad2.dpad_left;
		_dpad_r = gamepad2.dpad_right;

		_lTrigger = gamepad2.left_trigger;
		_rTrigger = gamepad2.right_trigger;

        _lBumper = gamepad2.left_bumper;
        _rBumper = gamepad2.right_bumper;

        _button_a = gamepad2.a;
        _button_b = gamepad2.b;
        _button_x = gamepad2.x;
        _button_y = gamepad2.y;
	}
}
