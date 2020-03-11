package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class __DriveBase__ {
	__DriveBase__(LinearOpMode linearOpMode, Telemetry telemetry) {
		opmode = linearOpMode;
		h = new __Hardware__(opmode, telemetry);
		a = new __AutoBase__(h, opmode);
	}
	__Hardware__ h;
	__AutoBase__ a;
	LinearOpMode opmode;

	ElapsedTime elapsedTime = new ElapsedTime();

	void run() {
		// Initiate hardware
		try {
			h.initTelemetry();
			h.initHardware(opmode.hardwareMap);
		} catch(Exception e) { h.except(e); }
		h.ready();
		h.tStatus("Running"); h.update();
		try {
			while (opmode.opModeIsActive()) {
				// Updates hardware
				h.updateGamepad(opmode.gamepad1, opmode.gamepad2);
				updateDrive();
				updateAux();
				updateCrServos();
				updateServos();
			}
		} catch (Exception e) { h.except(e); }
	}

/* Update */
	// Update Auxilary Motors
	void updateAux() {
		// Sets scissor-lift'side_setup motor powers
		h.scissor.setPower(h._lStick_y);
		if (h._lStick_y > 0) { h.tSub("Going Down!"); h.update(); }
		else if (h._lStick_y < 0) { h.tSub("Going Up!"); h.update(); }
		h.lSlide_l.setPower(-h._rStick_y);
		h.lSlide_r.setPower(h._rStick_y);
	}
	// Update CR Servos
	void updateCrServos() {
		// Sets grabber positions
		double p = 0.3;
		h.grab_l.setPower(h._rTrigger != 0.0 ? p : (h._lTrigger != 0.0 ? -p : 0.0));
		h.grab_r.setPower(h._rTrigger != 0.0 ? -p : (h._lTrigger != 0.0 ? p : 0.0));
	}
	// Update Servos
	private boolean clampIsClosed = true;
	private boolean handIsRaised = true;
	private boolean fHookT = true; // Toggle (actual value doesn't matter)
	void updateServos() {
		// Sets AutoClamp and AutoHand's positions
		// Gamepad2 dpad update and down control AutoHand, left and right control AutoClam
		if (h._button_y) {
			h.tLog(Integer.toString(h.autoHand.getCurrentPosition()));
			h.t.update();
			opmode.sleep(220);
		}
		if (h._button_a && clampIsClosed) {
			a.motorPosition(h.autoHand, handIsRaised ? -0.4 : 0.0, 0.2, 1.0);
			handIsRaised = !handIsRaised;
			opmode.sleep(220);
		}
		if (h._button_x && !handIsRaised) {
			h.autoClamp.setPosition(clampIsClosed ? 0.0 : 0.93);
			clampIsClosed = !clampIsClosed;
			opmode.sleep(60);
		}
		// Sets f-hook positions
		if (h.button_b) {
			h.fHook_l.setPosition(fHookT ? 1.0 : 0.0);
			h.fHook_r.setPosition(fHookT ? 0.0 : 1.0);
			fHookT = !fHookT;
			opmode.sleep(220);
		}
	}

	void updateDrive() {
		h.drive_lf.setPower(getPower(0));
		h.drive_rb.setPower(getPower(1));
		h.drive_lb.setPower(getPower(2));
		h.drive_rf.setPower(getPower(3));
	} double getPower(int i) {
		double power;
		switch (i) {
			case 0: power = -h.lStick_y + h.lStick_x + h.rStick_x; break; // drive_lf
			case 1: power = -h.lStick_y + h.lStick_x - h.rStick_x; break; // drive_rb
			case 2: power = -h.lStick_y - h.lStick_x + h.rStick_x; break; // drive_lb
			case 3: power = -h.lStick_y - h.lStick_x - h.rStick_x; break; // drive_rf
			default: power = 0; break;
		}
		// 0.2 is buffer, constants are speed modifiers for different situations
		double SLOW = (h.lStick_x >= 0.2 || h.lStick_x <= -0.2) ? 0.3 * 1.9 : 0.3; // Crab : Normal Slow = SLOW
		double slow = h.rTrigger != 0 ? 0.65 : 0.8; // not as slow : normal = slow
		power *= h.lTrigger != 0 ? SLOW : slow;
		if (power > 1.0) power = 1.0;
		return power;
	}
}
