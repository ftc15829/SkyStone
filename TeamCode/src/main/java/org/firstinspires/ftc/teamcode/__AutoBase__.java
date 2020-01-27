package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.List;

public class __AutoBase__ {
	/* Initializations */
	__AutoBase__(__Hardware__ hardware, LinearOpMode linearOpMode) {
		h = hardware; opmode = linearOpMode;
	}
	__Hardware__ h;
	LinearOpMode opmode;

	/* Actions */
	double findSkystone(boolean blue, double p) { h.tSub("Finding Skystone");
		driveModeSRE();
		driveModeRUE();
		ElapsedTime elapsedTime = new ElapsedTime();
		if (blue) mov(1, p);
		else mov(3, p);
		while (elapsedTime.seconds() < 3.0) {
			h.tRunTime(elapsedTime, 1);
			opmode.idle();
		}
		do {
			if (h.tfDetect != null) h.updateTfDetect();
			h.tRunTime(elapsedTime);
			h.tDrivePos();
//			h.tCaminfo();
			h.t.update();
			opmode.idle();
			if (elapsedTime.seconds() > 8.3) {
				h.tSub("Failed");
				halt(0);
				return -1.0;
			}
		} while ((blue ? h.SkystonePos > 440 : h.SkystonePos < 460) && h.SkystoneArea < 40_000 &&
				h.SkystoneConfidence < 0.75 && opmode.opModeIsActive());
		h.tSub("Success");
		halt(0);
		return Math.abs(h.drive_lf.getCurrentPosition() / 560);
	}

	void pickUp() { h.tSub("Picking up Stone");
		platform(2.5, 0.6);
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 1);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 0);
		opmode.sleep(600);
		platform(-1.2, 0.6);
	}
	void drop() { h.tSub("Dropping Stone");
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 0);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 1);
		opmode.sleep(600);
		platform(-1.3, 0.6);
	}

	void latch() { h.tSub("Latching");
		h.fHook_l.setPosition(0.0);
		h.fHook_r.setPosition(1.0);
		opmode.sleep(1300);
	}
	void unlatch() { h.tSub("Unlatching");
		h.fHook_l.setPosition(1.0);
		h.fHook_r.setPosition(0.0);
		opmode.sleep(1300);
	}

	/* Helper */
	// MODE
	void driveModeRTP() { // Sets drive motors to RTP
		modeRTP(h.drive_lf); modeRTP(h.drive_rf);
		modeRTP(h.drive_lb); modeRTP(h.drive_rb);
	} void modeRTP(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); }
	void driveModeSRE() { // Sets drive motors to SRE
		modeSRE(h.drive_lf); modeSRE(h.drive_rf);
		modeSRE(h.drive_lb); modeSRE(h.drive_rb);
	} void modeSRE(DcMotor motor) { motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); }
	void driveModeRUE() { // Sets drive motors to RTP
		modeRUE(h.drive_lf); modeRUE(h.drive_rf);
		modeRUE(h.drive_lb); modeRUE(h.drive_rb);
	} void modeRUE(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER); }
	void driveModeRWE() { // Sets drive motors to RTP
		modeRWE(h.drive_lf); modeRWE(h.drive_rf);
		modeRWE(h.drive_lb); modeRWE(h.drive_rb);
	} void modeRWE(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }

	// POWER
	void drivePower(double plf, double prf, double plb, double prb) {
		// Sets drive motors to given powers
		h.drive_lf.setPower(plf); h.drive_rf.setPower(prf);
		h.drive_lb.setPower(plb); h.drive_rb.setPower(prb);
	} void drivePower(double p) { // Sets all drive motors to p
		drivePower(p, p, p, p);
	}
	void halt(long time) {
		opmode.sleep(time);
		drivePower(0);
	}

	// TARGET POSITION
	void driveTargetPos(double revlf, double revrf, double revlb, double revrb) {
		// Sets drive's target position
		targetPos(h.drive_lf, revlf); targetPos(h.drive_rf, revrf);
		targetPos(h.drive_lb, revlb); targetPos(h.drive_rb, revrb);
	} void targetPos(DcMotor motor, double rev, int a) {
		// NOTE: goBuilda and Tetrix have diff values which is accounted for here.
		switch (a) {
			case 0: motor.setTargetPosition((int)(rev * h.drive_ticks)); break;
			case 1: motor.setTargetPosition((int)(rev * 1440)); break;
		}
	} void targetPos(DcMotor motor, double rev) { targetPos(motor, rev, 0); }

	// BUSY
	boolean drive_isBusy() { // Will return True if any drive motor is busy
		return h.drive_lf.isBusy() || h.drive_rf.isBusy() ||
				h.drive_lb.isBusy() || h.drive_rb.isBusy();
	}

	/* Auxilary Movement */
	void platform(double rev, double p) {
		modeSRE(h.lSlide_l); modeSRE(h.lSlide_r); // Change lSlide mode to SRE
		targetPos(h.lSlide_l, rev,1); targetPos(h.lSlide_r, -rev,1); // Set lSlide's target Pos
		modeRTP(h.lSlide_l); modeRTP(h.lSlide_r); // Change lSlide mode to RTP
		h.lSlide_l.setPower(p); h.lSlide_r.setPower(p); // Set lSlide's power to p
		while (h.lSlide_l.isBusy() && h.lSlide_r.isBusy()) {
			h.tPos(h.lSlide_l); h.tPos(h.lSlide_r); h.t.update();
			opmode.idle();
		}
		h.lSlide_l.setPower(0); h.lSlide_r.setPower(0); // Stop lSlide
	}

	/* Drive Movement */
	void mov(int dir, double p) {
		switch(dir) {
			case 0: // Forward
				drivePower(p); break;
			case 1: // Right
				drivePower(p, -p, -p, p); break;
			case 2: // Backward
				drivePower(-p); break;
			case 3: // Left
				drivePower(-p, p, p, -p); break;
		}
	}

	void movEncoder (List<Double> rev, double p, double t) {
		ElapsedTime elapsedTime = new ElapsedTime();
		driveModeSRE();
		driveTargetPos(rev.get(0), rev.get(1), rev.get(2), rev.get(3));
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (elapsedTime.seconds() < t || t == 0)) {
			opmode.idle();
		}
		halt(0);
		driveModeRWE();
	}

	void movF(double rev, double p, double t) { h.tSub("Moving Forward");
		movEncoder(Arrays.asList(rev, rev, rev, rev), p, t);
	} void movF(double rev, double p) { movF(rev, p, 0); }

	void movL(double rev, double p, double t) { h.tSub("Moving Left");
		movEncoder(Arrays.asList(-rev, rev, rev, -rev), p, t);
	} void movL(double rev, double p) { movL(rev, p, 0); }

	void movR(double rev, double p, double t) { h.tSub("Moving Right");
		movEncoder(Arrays.asList(rev, -rev, -rev, rev), p, t);
	} void movR(double rev, double p) { movR(rev, p, 0); }

	void movB(double rev, double p, double t) { h.tSub("Moving Backward");
		movEncoder(Arrays.asList(-rev, -rev, -rev, -rev), p, t);
	} void movB(double rev, double p) { movB(rev, p, 0); }

	void trnL(double rev, double p, double t) { h.tSub("Turning Left");
		rev *= 5;
		movEncoder(Arrays.asList(-rev, rev, -rev, rev), p, t);
	} void trnL(double rev, double p) { trnL(rev, p, 0); }

	void trnR(double rev, double p, double t){ h.tSub("Turning Right");
		rev *= 5;
		movEncoder(Arrays.asList(rev, -rev, rev, -rev), p, t);
	} void trnR(double rev, double p) { trnR(rev, p, 0); }

	void customTrn(double leftPower, double rightPower, long t) { h.tSub("Custom Turn");
		drivePower(leftPower, rightPower, leftPower, rightPower);
		opmode.sleep(t);
		halt(0);
	}

	void movF(long time, double p) { mov(0, p); halt(time); }
	void movL(long time, double p) { mov(3, p); halt(time); }
	void movR(long time, double p) { mov(1, p); halt(time); }
	void movB(long time, double p) { mov(2, p); halt(time); }
	void trnL(long time, double p) { drivePower(-p, p, -p, p); halt(time); }
	void trnR(long time, double p) { drivePower(p, -p, p, -p); halt(time); }
}
