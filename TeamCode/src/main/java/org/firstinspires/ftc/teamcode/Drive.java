package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@TeleOp(name="Drive") public class Drive extends LinearOpMode {

/*Initializations*/
	private Telemetry t = telemetry;
	private __Hardware__ h = new __Hardware__(t, this);
	private __AutoBase__ a = new __AutoBase__(h, this);

/*Main*/
	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
		try {
			h.init(hardwareMap);
			h.initAuto(hardwareMap);
			resetStartTime();
		} catch(Exception e) {
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
		h.tStatus("Ready");
		waitForStart();

		h.tStatus("Running");
		try {

			// Update Loop
			while (opModeIsActive()) {
				// Updates gamepad
				h.updateGamepad(gamepad1, gamepad2);

				// Updates hardware
				updateDrive();
				updateAux();
				updateCrServos();
				updateServos();
				detectAction();

				// Updates telemetry
				h.tDrivePower();
			}

		// Catches exceptions as plain-text
		} catch(Exception e) {
			h.tStatus("Error");
			h.tErr("Runtime", e);
			sleep(15_000);
			stop();
		}
	}

/* Assisted Actions */

/* Update */
 	// Detect Action
	private void detectAction() {

	}
	// Update Auxilary Motors
	private void updateAux() {
		h.tSub("Updating Aux Motors");
		// Sets scissor-lift's motor powers
		h.scissor.setPower(-h._lStick_y);
		h.lSlide_l.setPower(-h._rStick_y);
		h.lSlide_r.setPower(h._rStick_y);
	}
	// Update CR Servos
	private void updateCrServos() {
		h.tSub("Updating CRServos");
		// Sets grabber positions
		double p = 0.3;
		h.grab_l.setPower(h._rTrigger != 0.0 ? p : (h._lTrigger != 0.0 ? -p : 0.0));
		h.grab_r.setPower(h._rTrigger != 0.0 ? -p : (h._lTrigger != 0.0 ? p : 0.0));
	}
	// Update Servos
	private boolean fHookT = true; // Toggle (actual value doesn't matter)
	private void updateServos() {
		h.tSub("Updating Servos");
		// Sets f-hook positions
		if (h.button_b) {
			h.fHook_l.setPosition(fHookT ? 1.0 : 0.0);
			h.fHook_r.setPosition(fHookT ? 0.0 : 1.0);
			fHookT = !fHookT;
			sleep(300);
		}
	}
	// Update Drive
	private void updateDrive() {
		h.tSub("Updating Drive");
		h.drive_lf.setPower(getPower(0));
		h.drive_rb.setPower(getPower(1));
		h.drive_lb.setPower(getPower(2));
		h.drive_rf.setPower(getPower(3));
	}
	private double getPower(int i) {
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
