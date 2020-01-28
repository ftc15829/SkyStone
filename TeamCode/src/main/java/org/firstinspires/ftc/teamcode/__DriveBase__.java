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

	void init() {
		// Initiate hardware
		try {
			h.tStatus("Initializing");
			h.init(opmode.hardwareMap);
		} catch(Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			opmode.sleep(15_000);
			opmode.stop();
		}

		h.tStatus("Ready | Drive");
		while (!opmode.isStarted()) {
			h.tRunTime();
			opmode.idle();
		}
		opmode.resetStartTime();
		h.tStatus("Running");

	}

	void except(Exception e) {
		h.tStatus("Error");
		h.tErr("Runtime", e);
		opmode.sleep(15_000);
		opmode.stop();
	}

	void update() {
		// Updates hardware
		h.updateGamepad(opmode.gamepad1, opmode.gamepad2);
		updateDrive();
		updateAux();
		updateCrServos();
		updateServos();
		h.tDrivePower();
	}

/* AutoMaker */
	void update(int isAlt) { h.tSub("Updating Auto Maker");
		a.driveModeRUE();

		h.updateGamepad(opmode.gamepad1, opmode.gamepad2);
		updateDriveCardinal();
		updateAux();
		updateCrServos();
		updateServos();

		// Telemetry
//		h.t.setAutoClear(false);
		if (h.button_y) {
			h.t.addLine("Snapshot Taken");
			h.tSnapRuntime(elapsedTime);
			h.tSnapDrivePos();
			a.driveModeSRE();
			elapsedTime.reset();
		}
	}

/* Update */
	// Update Auxilary Motors
	void updateAux() { h.tSub("Updating Aux Motors");
		// Sets scissor-lift's motor powers
		h.scissor.setPower(-h._lStick_y);
		h.lSlide_l.setPower(-h._rStick_y);
		h.lSlide_r.setPower(h._rStick_y);
	}
	// Update CR Servos
	void updateCrServos() { h.tSub("Updating CRServos");
		// Sets grabber positions
		double p = 0.3;
		h.grab_l.setPower(h._rTrigger != 0.0 ? p : (h._lTrigger != 0.0 ? -p : 0.0));
		h.grab_r.setPower(h._rTrigger != 0.0 ? -p : (h._lTrigger != 0.0 ? p : 0.0));
	}
	// Update Servos
	boolean fHookT = true; // Toggle (actual value doesn't matter)
	void updateServos() { h.tSub("Updating Servos");
		// Sets f-hook positions
		if (h.button_b) {
			h.fHook_l.setPosition(fHookT ? 1.0 : 0.0);
			h.fHook_r.setPosition(fHookT ? 0.0 : 1.0);
			fHookT = !fHookT;
			opmode.sleep(300);
		}
	}
	// Update Drive
	void updateDriveCardinal() { h.tSub("Updating Cardinal Drive");
		if (h.dpad_u) {
			a.mov(0, 1.0);
		} else if (h.dpad_d) {
			a.mov(2, 1.0);
		} else if (h.dpad_l) {
			a.mov(3, 1.0);
		} else if (h.dpad_r) {
			a.mov(1, 1.0);
		} else {
			a.halt(0);
		}
	}
	void updateDrive() { h.tSub("Updating Drive");
		h.drive_lf.setPower(getPower(0));
		h.drive_rb.setPower(getPower(1));
		h.drive_lb.setPower(getPower(2));
		h.drive_rf.setPower(getPower(3));
	}
	double getPower(int i) {
		double power;
		double mod = 0.6;
		double MOD = 0.3;
		switch (i) {
			case 0: power = h.lStick_y - h.lStick_x - h.rStick_x; break; // drive_lf
			case 1: power = h.lStick_y - h.lStick_x + h.rStick_x; break; // drive_rb
			case 2: power = h.lStick_y + h.lStick_x - h.rStick_x; break; // drive_lb
			case 3: power = h.lStick_y + h.lStick_x + h.rStick_x; break; // drive_rf
			default: power = 0; break;
		}
		power = power > 1 ? 1 : power;
		power *= h.lTrigger != 0 ? MOD : (h.rTrigger != 0 ? mod : 1);
		return power;
	}
}
