package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@TeleOp(name="Drive") public class Drive extends LinearOpMode {

	// Define hardware and telemetry
	private Telemetry t = telemetry;
	private Hardware h = new Hardware(t, this);

	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
		try {
			h.init(hardwareMap);
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
			h.tErr("Runtime", e);
			sleep(15_000);
			stop();
		}
	}

 /*
 * ACTIONS
*/
 	// Pick Up
 	private void pickUp() {

 		// Note: might think about combining this and AutoBase's pickUp() and drop() functions
	}
 	// Drop
	private void drop() {}

 /*
 * UPDATE
*/
 	// DETECT Action
	private void detectAction() {
		/*
		if b then pickup
		if y then drop
		etc.
		 */
	}
	// UPDATE auxMotors
	private void updateAux() {
		// Sets scissor-lift's motor powers
		h.scissor.setPower(-h._lStick_y);
		h.lSlide_l.setPower(-h._rStick_y);
		h.lSlide_r.setPower(h._rStick_y);
	}
	// UPDATE crServos
	private void updateCrServos() {
		// Sets grabber positions
		h.grab_l.setPower(h._rTrigger != 0.0 ? 1.0 : (h._lTrigger != 0.0 ? -1.0 : 0.0));
		h.grab_r.setPower(h._rTrigger != 0.0 ? -1.0 : (h._lTrigger != 0.0 ? 1.0 : 0.0));
	}
	// UPDATE servos
	private boolean a = true; // Toggle (actual value doesn't matter)
	private void updateServos() {
		// Sets f-hook positions
		if (h.button_b) {
			h.fHook_l.setPosition(a ? 1.0 : 0.0);
			h.fHook_r.setPosition(a ? 0.0 : 1.0);
			a = !a;
			sleep(50);
		}
	}
	// UPDATE drive
	private void updateDrive() {
		h.drive_lf.setPower(getPower(0));
		h.drive_rb.setPower(getPower(1));
		h.drive_lb.setPower(getPower(2));
		h.drive_rf.setPower(getPower(3));
	}
		// HELPER drive
	private double getPower(int i) {
		double power;
		double mod = 0.6;
		double MOD = 0.3;
		switch (i) {
			case 0: power = -h.lStick_y + h.lStick_x + h.rStick_x; break; // drive_lf
			case 1: power = -h.lStick_y + h.lStick_x - h.rStick_x; break; // drive_rb
			case 2: power = h.lStick_y - h.lStick_x + h.rStick_x; break; // drive_lb
			case 3: power = h.lStick_y - h.lStick_x - h.rStick_x; break; // drive_rf
			default: power = 0; break;
		}
		power = power > 1 ? 1 : power;
		power *= h.lTrigger != 0 ? MOD : (h.rTrigger != 0 ? mod : 1);
		return power;
	}

}
