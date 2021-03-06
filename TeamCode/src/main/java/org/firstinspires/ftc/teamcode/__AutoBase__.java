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
	enum Dir { UP, DOWN, LEFT, RIGHT }
	enum Brand { GOBUILDA, TETRIX }

	/* Actions */
	int findSkystone(boolean blue, double p, boolean strafe) {
		/**Scans for the skystone, starting from the stone farthest from the wall and incrementally
		 * approaches the wall, stopping after scanning the third stone.
		 *
		 * Returns -1 if it didn't find a skystone. A value from {1, 2, 3} is returned if success,
		 * 1 corresponds to the farthest stone from the wall, 3 the third farthest.
		 */
		h.tSub("Finding Skystone"); h.update();
		driveModeSRE();
		for (int i = 1; i <= 3; i++) {
			if (!opmode.opModeIsActive()) break;
			ElapsedTime elapsedTime = new ElapsedTime();
			h.tfDetect.activate();
			while (elapsedTime.seconds() < 1.0 && opmode.opModeIsActive()) {
				h.tRunTime(elapsedTime);
				h.updateTfDetect();
				h.tCaminfo();
				h.update();
				if (h.sArea > 50_000) {
					h.tLog("Skystone detected");
					h.tSub("---");
					h.update();
					halt(0);
					h.tfDetect.deactivate();
					return i;
				}
				opmode.idle();
			}
			h.tfDetect.deactivate();
			if (i == 3)
				break;
			if (strafe)
				if (blue) movR(2.6, 2.0, 2.0);
				else movL(2.6, 2.0, 2.0);
			else
				if (blue) movB(1.4, 1.5, 2.0);
				else movF(1.4, 1.5, 2.0);
		}
		h.tLog("Detection failed");
		h.tSub("---");
		h.update();
		halt(0);
		return -1;
	}

	void side_setup() {
//		h.autoHand.getController().setServoPosition(h.autoHand.getPortNumber(), 0);
//		opmode.sleep(600);
//		h.autoClamp.getController().setServoPosition(h.autoClamp.getPortNumber(), 0);
//		opmode.sleep(600);
	}

	void side_grab() {
//		h.autoClamp.getController().setServoPosition(h.autoClamp.getPortNumber(), 1);
//		opmode.sleep(600);
//		opmode.sleep(300);
//		h.autoHand.getController().setServoPosition(h.autoHand.getPortNumber(), 1);
//		opmode.sleep(600);
	}

	void side_drop() {
//		h.autoHand.getController().setServoPosition(h.autoHand.getPortNumber(), 0);
//		h.autoClamp.getController().setServoPosition(h.autoClamp.getPortNumber(), 0);
//		opmode.sleep(600);
	}

	void front_drop() {
		h.tSub("Dropping Stone"); h.update();
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 0);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 1);
		opmode.sleep(600);
		platform(-1.7, 0.6);
		h.tSub("---"); h.update();
	}

	void front_grab() {
		h.tSub("Grabbing Stone"); h.update();
		platform(2.8, 1.0);
		h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 1);
		h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 0);
		// Allows servos to settle
		opmode.sleep(600);
		platform(-1.1, 1.0);
		h.tSub("---"); h.update();
	}

	void latch() {
		h.tSub("Latching"); h.update();
		h.fHook_l.setPosition(0.0);
		h.fHook_r.setPosition(1.0);
		h.bHook_l.setPower(-1);
		h.bHook_r.setPower(1);
		opmode.sleep(250);
		h.bHook_l.setPower(0);
		h.bHook_r.setPower(0);
		// Allows servos to settle
		opmode.sleep(500);
		h.tSub("---"); h.update();
	}
	void unlatch() {
		h.tSub("Unlatching"); h.update();
		h.fHook_l.setPosition(1.0);
		h.fHook_r.setPosition(0.0);
		h.bHook_l.setPower(1);
		h.bHook_r.setPower(-1);
		opmode.sleep(250);
		h.bHook_l.setPower(0);
		h.bHook_r.setPower(0);
		opmode.sleep(500);
		h.tSub("---"); h.update();
	}

	//*Helper
	// MODE
	void driveModeRTP() { // Sets drive motors to RTP
		modeRTP(h.drive_lf); modeRTP(h.drive_rf);
		modeRTP(h.drive_lb); modeRTP(h.drive_rb);
	} void modeRTP(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); }
	void driveModeSRE() { // Sets drive motors to SRE
		modeSRE(h.drive_lf); modeSRE(h.drive_rf);
		modeSRE(h.drive_lb); modeSRE(h.drive_rb);
	} void modeSRE(DcMotor motor) { motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); }
	void driveModeRUE() { // Sets drive motors to RUE
		modeRUE(h.drive_lf); modeRUE(h.drive_rf);
		modeRUE(h.drive_lb); modeRUE(h.drive_rb);
	} void modeRUE(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER); }
	void driveModeRWE() { // Sets drive motors to RWE
		modeRWE(h.drive_lf); modeRWE(h.drive_rf);
		modeRWE(h.drive_lb); modeRWE(h.drive_rb);
	} void modeRWE(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }

	// POWER
	void drivePower(double plf, double prf, double plb, double prb) {
		// Sets drive motors to given powers
		h.drive_lf.setPower(plf * h.PowerMod); h.drive_rf.setPower(prf * h.PowerMod);
		h.drive_lb.setPower(plb * h.PowerMod); h.drive_rb.setPower(prb * h.PowerMod);
	} void drivePower(double p) { // Sets all drive motors to a power p
		drivePower(p, p, p, p);
	}
	void halt(long time) {
		opmode.sleep(time);
		drivePower(0);
	}

	// TARGET POSITION
	void driveTargetPos(double revlf, double revrf, double revlb, double revrb) {
		// Sets drive'side_setup target position
		targetPos(h.drive_lf, revlf); targetPos(h.drive_rf, revrf);
		targetPos(h.drive_lb, revlb); targetPos(h.drive_rb, revrb);
	} void targetPos(DcMotor motor, double rev, Brand brand/*= GOBUILDA*/) {
		// goBuilda and Tetrix have diff tickrates
		switch (brand) {
			case GOBUILDA: motor.setTargetPosition((int)(rev * h.GobRate)); break;
			case TETRIX: motor.setTargetPosition((int)(rev * h.TetRate)); break;
		}
	} void targetPos(DcMotor motor, double rev) { targetPos(motor, rev, Brand.GOBUILDA); }

	// BUSY
	boolean drive_isBusy() { // Will return True if any drive motor is busy
		int rftp = h.drive_rf.getTargetPosition();
		int rbtp = h.drive_rb.getTargetPosition();
		int lftp = h.drive_lf.getTargetPosition();
		int lbtp = h.drive_lb.getTargetPosition();

		int rfcp = h.drive_rf.getCurrentPosition();
		int rbcp = h.drive_rb.getCurrentPosition();
		int lfcp = h.drive_lf.getCurrentPosition();
		int lbcp = h.drive_lb.getCurrentPosition();
		// FIXME: This just doesn't work
		return !(rftp == rfcp && rbtp == rbcp && lftp == lfcp && lbtp == lbcp);
	}

	/* Auxilary Movement */
	void motorPosition(DcMotor motor, double rev, double p, double t) {
		int start = motor.getCurrentPosition();
		ElapsedTime elapsed = new ElapsedTime();
		targetPos(motor, rev, Brand.TETRIX);
		modeRTP(motor);
		motor.setPower(p);
		while (motor.isBusy() && (elapsed.seconds() < t || t == 0) && opmode.opModeIsActive()) {
			opmode.idle();
		}
		if (elapsed.seconds() >= t) {
			h.tLog(motor.getPortNumber() + " - Timed Out at " + String.format("%f",
			h.encoderErrorCalc(start, motor.getCurrentPosition(), motor.getTargetPosition())));
		}
		motor.setPower(0);
		modeRWE(motor);
	} void motorPosition(DcMotor motor, double rev, double p) {
		motorPosition(motor, rev, p, 0);
	}

	void platform(double rev, double p) {
		h.tSub("Moving Platform");
		ElapsedTime elapsedTime = new ElapsedTime();
		modeSRE(h.lSlide_l); // Change lSlide mode to SRE
		modeSRE(h.lSlide_r);
		targetPos(h.lSlide_l, rev, Brand.TETRIX); // Set lSlide'side_setup target Pos
		targetPos(h.lSlide_r, -rev, Brand.TETRIX);
		modeRTP(h.lSlide_l); // Change lSlide mode to RTP
		modeRTP(h.lSlide_r);
		h.lSlide_l.setPower(p); // Set lSlide'side_setup power to p
		h.lSlide_r.setPower(p);
		while ((h.lSlide_l.isBusy() && h.lSlide_r.isBusy()) && elapsedTime.seconds() < 3.0 && opmode.opModeIsActive()) {
			h.tPos(h.lSlide_l);
			h.tPos(h.lSlide_r);
			h.t.update();
			opmode.idle();
		}
		h.lSlide_l.setPower(0); // Stop lSlide
		h.lSlide_r.setPower(0);
		h.tSub("---");
	}

	/* Drive Movement */
	void mov(Dir dir, double p) {
		p = -p;
		switch(dir) {
			case UP: // Forward
				drivePower(p); break;
			case RIGHT: // Right
				drivePower(p, -p, -p, p); break;
			case DOWN: // Backward
				drivePower(-p); break;
			case LEFT: // Left
				drivePower(-p, p, p, -p); break;
		}
	}

	void movEncoder (List<Double> rev, double p, double t) {
		ElapsedTime elapsed = new ElapsedTime();
		driveModeSRE();
		driveTargetPos(rev.get(0), rev.get(1), rev.get(2), rev.get(3));
		driveModeRTP();
		drivePower(p);
		while (drive_isBusy() && (elapsed.seconds() < t || t == 0) && opmode.opModeIsActive()) {
			opmode.idle();
		}
		if (elapsed.seconds() >= t) h.tLog("Drive timed out at " + String.format("%.2f", (h.drive_lf.getCurrentPosition() / h.drive_lf.getTargetPosition()) * 100) + "%");
		halt(0);
		driveModeRWE();
	}

	void movV(double rev, double p, double t, boolean forward) {
		if (forward) movF(rev, p, t);
		else movB(rev, p, t);
	} void movV(double rev, double p, boolean forward) { movH(rev, p, 0, forward); }

	void movH(double rev, double p, double t, boolean right) {
		if (right) movR(rev, p, t);
		else movL(rev, p, t);
	} void movH(double rev, double p, boolean right) { movH(rev, p, 0, right); }

	void movF(double rev, double p, double t) {
		h.tSub("Moving Forward"); h.update();
		movEncoder(Arrays.asList(-rev, -rev, -rev, -rev), p, t);
		h.tSub("---"); h.update();
	} void movF(double rev, double p) { movF(rev, p, 0); }

	void movL(double rev, double p, double t) {
		h.tSub("Moving Left"); h.update();
		movEncoder(Arrays.asList(rev, -rev, -rev, rev), p, t);
		h.tSub("---"); h.update();
	} void movL(double rev, double p) { movL(rev, p, 0); }

	void movR(double rev, double p, double t) {
		h.tSub("Moving Right"); h.update();
		movEncoder(Arrays.asList(-rev, rev, rev, -rev), p, t);
		h.tSub("---"); h.update();
	} void movR(double rev, double p) { movR(rev, p, 0); }

	void movB(double rev, double p, double t) {
		h.tSub("Moving Backward"); h.update();
		movEncoder(Arrays.asList(rev, rev, rev, rev), p, t);
		h.tSub("---"); h.update();
	} void movB(double rev, double p) { movB(rev, p, 0); }

	void trnH(double rev, double p, double t, boolean right) {
		if (right) trnR(rev, p, t);
		else trnL(rev, p, t);
	} void trnH(double rev, double p, boolean right) { trnH(rev, p, 0, right); }

	void trnL(double rev, double p, double t) {
		// 1 rev = 45 degree rotation
		h.tSub("Turning Left"); h.update();
		rev *= 5;
		movEncoder(Arrays.asList(rev, -rev, rev, -rev), p, t);
		h.tSub("---"); h.update();
	} void trnL(double rev, double p) { trnL(rev, p, 0); }

	void trnR(double rev, double p, double t){
		h.tSub("Turning Right"); h.update();
		rev *= 5;
		movEncoder(Arrays.asList(-rev, rev, -rev, rev), p, t);
		h.tSub("---"); h.update();
	} void trnR(double rev, double p) { trnR(rev, p, 0); }

	void cTrnH(double radius, int degrees, double p, double t, boolean right) {
		if (right) cTrnR(radius, degrees, p, t);
		else cTrnL(radius, degrees, p, t);
	} void cTrnH(double radius, int degrees, double p, boolean right) {
		cTrnH(radius, degrees, p, 0, right);
	}

	void cTrnL(double radius, int degrees, double p, double t) {
		h.tSub("cTurning Left"); h.update();
		ElapsedTime elapsed = new ElapsedTime();
		double mod = 6.6;
		driveModeSRE();
		// (radius * (degrees to radians)) / feet per revolution | (feet / (feet / rev)) -> rev R2 over R1 smaller radius r1 over r2
		double outerArc = radius * (Math.PI / 180) * degrees * mod;
		double innerArc = (radius - 1.5) * ((Math.PI / 180.0) * degrees * mod);
		driveTargetPos(-innerArc, -outerArc, -innerArc, -outerArc);
		driveModeRTP();
		double lP = (1 - (1.5 / radius)) * p;
		drivePower(lP, p, lP, p);
		while (drive_isBusy() && (elapsed.seconds() < t || t == 0) && opmode.opModeIsActive()) {
			opmode.idle();
		}
		if (elapsed.seconds() >= t) h.tLog("Left cTurn Timed Out");
		halt(0);
		driveModeRWE();
		h.tSub("---"); h.update();
	}

	void cTrnR(double radius, int degrees, double p, double t) {
		h.tSub("cTurning Right"); h.update();
		ElapsedTime elapsed = new ElapsedTime();
		double mod = 6.6;
		driveModeSRE();
		// (radius * (degrees to radians)) / feet per revolution | (feet / (feet / rev)) -> rev R2 over R1 smaller radius r1 over r2
		double outerArc = radius * (Math.PI / 180) * degrees * mod;
		double innerArc = (radius - 1.5) * ((Math.PI / 180.0) * degrees * mod);
		driveTargetPos(-outerArc, -innerArc, -outerArc, -innerArc);
		driveModeRTP();
		double rP = (1 - (1.5 / radius)) * p;
//		double rP = (innerArc / outerArc) * p; // ((rev / rev) * power) -> power
		drivePower(p, rP, p, rP);
		while (drive_isBusy() && (elapsed.seconds() < t || t == 0) && opmode.opModeIsActive()) {
			opmode.idle();
		}
		if (elapsed.seconds() >= t) h.tLog("Right cTurn Timed Out");
		halt(0);
		driveModeRWE();
		h.tSub("---"); h.update();
	}

	void movF(long time, double p) {
		mov(Dir.UP, p); halt(time);
	}
	void movL(long time, double p) {
		mov(Dir.LEFT, p); halt(time);
	}
	void movR(long time, double p) {
		mov(Dir.RIGHT, p); halt(time);
	}
	void movB(long time, double p) {
		mov(Dir.DOWN, p); halt(time);
	}
	void trnL(long time, double p) {
		drivePower(-p, p, -p, p); halt(time);
	}
	void trnR(long time, double p) {
		drivePower(p, -p, p, -p); halt(time);
	}
}
