package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class AutoBase {
	/*Initializations*/
	AutoBase(Hardware hardware, LinearOpMode linearOpMode) { h = hardware; opmode = linearOpMode; }
	Hardware h;
	LinearOpMode opmode;

	/*Actions*/
	void findSkystone(int dir, double p) { // Searches for a skystone in the given direction. When if finds one it will move towards it
		mov(dir, p); h.tSub("Scanning " + dir);
		while(h.sDetect.foundRectangle().area() < 6000 || h.sDetect.getScreenPosition().y < 27 || h.sDetect.getScreenPosition().y > 147) {
			h.tCaminfo();
			opmode.idle();
		} h.tSub("Found! Engaging!");
		halt(0);
		movF(3.0, 1); h.tSub("");
	}

	void pickUp() { h.tSub("Picking up Block"); // Will extend platform, attempt to grab a block, then retract the platform
		platform(10, 0.7);
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 1);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 0);
		platform(-10, 0.7); h.tSub("");
	}
	void drop() { h.tSub("Dropping Block"); // Will extend platform, release any held block, then retract the platform
		platform(10, 0.7);
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 0);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 1);
		platform(-10, 0.7); h.tSub("");
	}

	/*Helper*/
	// Drive Specific
	void driveTargetPos(double revlf, double revrf, double revlb, double revrb) { // Sets drive's target position
		h.drive_lf.setTargetPosition((int)(revlf * 28 * 20)); h.drive_rf.setTargetPosition((int)(revrf * 28 * 20));
		h.drive_lb.setTargetPosition((int)(revlb * 28 * 20)); h.drive_rb.setTargetPosition((int)(revrb * 28 * 20));
	}
	void driveModeRTP() { // Sets drive motors to RTP
		h.drive_lf.setMode(DcMotor.RunMode.RUN_TO_POSITION); h.drive_rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		h.drive_lb.setMode(DcMotor.RunMode.RUN_TO_POSITION); h.drive_rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}
	void driveModeSRE() { // Sets drive motors to SRE
		h.drive_lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); h.drive_rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		h.drive_lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); h.drive_rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
	void drivePower(double plf, double prf, double plb, double prb) { // Sets drive motors to given powers
		h.drive_lf.setPower(plf); h.drive_rf.setPower(prf);
		h.drive_lb.setPower(plb); h.drive_rb.setPower(prb);
	}
	void drivePower(double p) { // Sets all drive motors to p
		h.drive_lf.setPower(p); h.drive_rf.setPower(p);
		h.drive_lb.setPower(p); h.drive_rb.setPower(p);
	}
	boolean drive_isBusy() { // Will return True if any drive motor is busy
		return h.drive_lf.isBusy() || h.drive_rf.isBusy() || h.drive_lb.isBusy() || h.drive_rb.isBusy();
	}
	// General
	void modeSRE(DcMotor motor) {
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
	void modeRTP(DcMotor motor) {
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}
	void targetPos(DcMotor motor, double rev) {
		// Note: goBuilda and Tetrix will have diff values which is not accounted for here.
		motor.setTargetPosition((int)(rev * 28));
	}
	void power(DcMotor motor, double p) {
		motor.setPower(p);
	}
	void mov(int dir, double p) {
		switch(dir) {
			case 0: // Forward
				drivePower(p);
				break;
			case 1: // Right
				drivePower(p, -p, -p, p);
				break;
			case 2: // Backward
				drivePower(-p);
				break;
			case 3: // Left
				drivePower(-p, p, p, -p);
				break;
			default:
				break;
		}
	}

	/*Auxilary Movement*/
	void platform(double rev, double p) {
		modeSRE(h.lSlide_l); modeSRE(h.lSlide_r); // Change lSlide mode to SRE
		targetPos(h.lSlide_l, rev); targetPos(h.lSlide_r, rev); // Set lSlide's target Pos
		modeRTP(h.lSlide_l); modeRTP(h.lSlide_r); // Change lSlide mode to RTP
		power(h.lSlide_l, p); power(h.lSlide_r, p); // Set lSlide's power to p
		while (h.lSlide_l.isBusy() && h.lSlide_r.isBusy()) {
			h.tPos(h.lSlide_l); h.tPos(h.lSlide_r);
			opmode.idle();
		}
		power(h.lSlide_l, 0); power(h.lSlide_r, 0); // Stop lSlide
	}

	/*Encoder Based Movement*/
	void movF(double rev, double p) {
		driveModeSRE();
		driveTargetPos(rev, rev, rev, rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy()) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
	}
	void movL(double rev, double p) {
		driveModeSRE();
		driveTargetPos(-rev, rev, rev, -rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy()) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
	}
	void movR(double rev, double p) {
		driveModeSRE();
		driveTargetPos(rev, -rev, -rev, rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy()) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
	}
	void movB(double rev, double p) {
		driveModeSRE();
		driveTargetPos(-rev, -rev, -rev, -rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy()) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
	}
	void trnL(double rev, double p) {
		rev *= 5.0;
		driveModeSRE();
		driveTargetPos(-rev, rev, -rev, rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy()) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
	}
	void trnR(double rev, double p) {
		rev *= 5.0;
		driveModeSRE();
		driveTargetPos(rev, -rev, rev, -rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy()) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
	}

	/*Time Based Movement*/
	void movF(long time, double p) {
		mov(0, p); halt(time); }
	void movL(long time, double p) {
		mov(3, p); halt(time); }
	void movR(long time, double p) {
		mov(1, p); halt(time); }
	void movB(long time, double p) {
		mov(2, p); halt(time); }
	void trnL(long time, double p) {
		drivePower(-p, p, -p, p); halt(time); }
	void trnR(long time, double p) {
		drivePower(p, -p, p, -p); halt(time); }
	void halt(long time) {
		opmode.sleep(time);
		h.drive_lf.setPower(0); h.drive_rf.setPower(0);
		h.drive_lb.setPower(0); h.drive_rb.setPower(0);
	}

	/*Utility Classes*/
	public class AutoTelemetry implements Runnable { // Depreciated
		public boolean sDetector_xy = false;

		@Override public void run() {
			while (!opmode.isStopRequested()) {
				if (sDetector_xy) h.tWebcam();
				opmode.idle();
			}
		}
	}
}
