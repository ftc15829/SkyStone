package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class __AutoBase__ {
	/*Initializations*/
	__AutoBase__(__Hardware__ hardware, LinearOpMode linearOpMode) { h = hardware; opmode = linearOpMode; }
	__Hardware__ h;
	LinearOpMode opmode;

	/*Actions*/
	double findSkystone(int dir, double p) { // Searches for a skystone in the given direction. When if finds one it will move towards it
		mov(dir, p); // move left(3) or right(1)
		h.tSub("Scanning");
		do {
			if (h.tfDetect != null) h.updateTfDetect();
			h.tCaminfo();
			h.tRunTime();
			opmode.idle();
		} while ((dir == 3 ? h.SkystonePos < 320 : h.SkystonePos > 420) && h.SkystoneArea < 40_000 && h.SkystoneConfidence < 0.75 && opmode.opModeIsActive());
		halt(0);
		return opmode.getRuntime();
	}

	void pickUp() { h.tSub("Picking up Block"); // Will extend platform, attempt to grab a block, then retract the platform
		platform(2.7, 0.6);
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 1);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 0);
		opmode.sleep(600);
		platform(-2.7, 0.6); h.tSub("");
	}
	void drop() { h.tSub("Dropping Block"); // Will extend platform, release any held block, then retract the platform
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 0);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 1);
		opmode.sleep(600);
	}

	void latch() {
		h.fHook_l.setPosition(0.0);
		h.fHook_r.setPosition(1.0);
		opmode.sleep(1000);
	}
	void unlatch() {
		h.fHook_l.setPosition(1.0);
		h.fHook_r.setPosition(0.0);
		opmode.sleep(1300);
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
	void driveModeRUE() { // Sets drive motors to RTP
		h.drive_lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER); h.drive_rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		h.drive_lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER); h.drive_rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
	void driveModeRWE() { // Sets drive motors to RTP
		h.drive_lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); h.drive_rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		h.drive_lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); h.drive_rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
	void targetPos(DcMotor motor, double rev, int a) {
		// Note: goBuilda and Tetrix will have diff values which is not accounted for here.
		switch (a)
		{
			case 0:
				motor.setTargetPosition((int)(rev * 28));break;
			case 1:
				motor.setTargetPosition((int)(rev * 1440));break;
		}

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
	boolean cDrive_isBusy(double rev) {
		double target = Math.abs(rev * 20 * 28);
		int diff = 500;
		if (h.drive_rf.getCurrentPosition() >= target-diff || h.drive_lf.getCurrentPosition() >= target-diff || h.drive_rb.getCurrentPosition() >= target-diff || h.drive_lb.getCurrentPosition() >= target-diff) {
			return true;
		}
		return false;
	}
	boolean cMotor_isBusy(double rev, DcMotor motor) {
		double target = Math.abs(rev * 20 * 28);
		int diff = 500;
		if (motor.getCurrentPosition() >= target-diff) {
			return true;
		}
		return false;
	}
	/*Auxilary Movement*/

	void platform(double rev, double p) {
		modeSRE(h.lSlide_l); modeSRE(h.lSlide_r); // Change lSlide mode to SRE
		targetPos(h.lSlide_l, rev,1); targetPos(h.lSlide_r, -rev,1); // Set lSlide's target Pos
		modeRTP(h.lSlide_l); modeRTP(h.lSlide_r); // Change lSlide mode to RTP
		power(h.lSlide_l, p); power(h.lSlide_r, p); // Set lSlide's power to p
		while (h.lSlide_l.isBusy() && h.lSlide_r.isBusy()) {
			h.tPos(h.lSlide_l); h.tPos(h.lSlide_r);
			opmode.idle();
		}
		power(h.lSlide_l, 0); power(h.lSlide_r, 0); // Stop lSlide
	}

	/*Encoder Based Movement*/

	void movF(double rev, double p) { movF(rev, p, 0); }
	void movL(double rev, double p) { movL(rev, p, 0); }
	void movR(double rev, double p) { movR(rev, p, 0); }
	void movB(double rev, double p) { movB(rev, p, 0); }
	void trnL(double rev, double p) { trnL(rev, p, 0); }
	void trnR(double rev, double p) { trnR(rev, p, 0); }

	void movF(double rev, double p, double t) {
		double startTime = opmode.getRuntime();
		driveModeSRE();
		driveTargetPos(rev, rev, rev, rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (opmode.getRuntime() < startTime + t || t == 0)) {
			h.tDrivePos();
			opmode.idle();
//			if (cDrive_isBusy(rev)) {
//				halt(0);driveModeSRE();
//				break;
//			}

		}
		halt(0);
		driveModeRWE();
	}
	void movL(double rev, double p, double t) {
		double startTime = opmode.getRuntime();
		driveModeSRE();
		driveTargetPos(-rev, rev, rev, -rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (opmode.getRuntime() < startTime + t || t == 0)) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
		driveModeRWE();
	}
	void movR(double rev, double p, double t) {
		double startTime = opmode.getRuntime();
		driveModeSRE();
		driveTargetPos(rev, -rev, -rev, rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (opmode.getRuntime() < startTime + t || t == 0)) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
		driveModeRWE();
	}
	void movB(double rev, double p, double t) {
		double startTime = opmode.getRuntime();
		driveModeSRE();
		driveTargetPos(-rev, -rev, -rev, -rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (opmode.getRuntime() < startTime + t || t == 0)) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
		driveModeRWE();
	}
	void trnL(double rev, double p, double t) {
		double startTime = opmode.getRuntime();
		rev *= 5.0;
		driveModeSRE();
		driveTargetPos(-rev, rev, -rev, rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (opmode.getRuntime() < startTime + t || t == 0)) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
		driveModeRWE();
	}
	void trnR(double rev, double p, double t){
		double startTime = opmode.getRuntime();
		rev *= 5.0;
		driveModeSRE();
		driveTargetPos(rev, -rev, rev, -rev);
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (opmode.getRuntime() < startTime + t || t == 0)) {
			h.tDrivePos();
			opmode.idle();
		}
		halt(0);
		driveModeRWE();
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
}
