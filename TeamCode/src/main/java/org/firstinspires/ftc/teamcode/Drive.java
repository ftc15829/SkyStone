package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@TeleOp(name="Drive") public class Drive extends LinearOpMode {

/*Initializations*/
	private Telemetry t = telemetry;
	private Hardware h = new Hardware(t, this);
	private AutoBase a = new AutoBase(h, this);

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

/*Assisted Actions*/
	private void skystoneAlign() {
		AutoBase.SepThreadMov sepThreadMov = a.new SepThreadMov();
		double p = 0.8;
		h.startStream();
		sleep(800); // Time for stream to start up
		Thread t = new Thread(sepThreadMov);
		sepThreadMov.dir = 1; // 1 = right, 3 = left
		sepThreadMov.p = p;
		sepThreadMov.isRunning = true;
		t.run(); h.tSub("Scanning " + sepThreadMov.dir);

		double startTime = getRuntime();
		boolean found = false;

		while((h.sDetect.foundRectangle().area() < 7000 || h.sDetect.getScreenPosition().x < 79 || h.sDetect.getScreenPosition().x > 82) && getRuntime() - startTime < 6) {
			h.tCaminfo();
			idle();

			if(getRuntime() - startTime == 2) {
				sepThreadMov.dir = 3;
			}

		} h.tSub("End of Scan");
		found = getRuntime() - startTime < 6 ? true : false;
		if(found) {
			a.movF(1.0, 1.0);
		}
		sepThreadMov.isRunning = false;
		h.stopStream();
	}

/*Update*/
 	// Detect Action
	private void detectAction() {
		if(h._button_x && !(h.lSlide_l.isBusy() || h.lSlide_r.isBusy())) {
			a.pickUp();
		}
		if(h._button_y && !(h.lSlide_l.isBusy() || h.lSlide_r.isBusy())) {
			a.drop();
		}
		if(h._button_b && !(a.drive_isBusy())) {
			skystoneAlign();
		}
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
		h.grab_l.setPower(h._rTrigger != 0.0 ? 1.0 : (h._lTrigger != 0.0 ? -1.0 : 0.0));
		h.grab_r.setPower(h._rTrigger != 0.0 ? -1.0 : (h._lTrigger != 0.0 ? 1.0 : 0.0));
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
