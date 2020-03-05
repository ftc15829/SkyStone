package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FoundationBase {
	FoundationBase(LinearOpMode linearOpMode, Telemetry telemetry) {
		opmode = linearOpMode;
		h = new __Hardware__(opmode, telemetry);
		a = new __AutoBase__(h, opmode);
	}
	LinearOpMode opmode;
	__Hardware__ h;
	__AutoBase__ a;

	private boolean BLUE, MID, RIGHT;

	enum Modes { REGULAR, TAPE }

	private void init() {
		try {
			h.init(opmode.hardwareMap);
			a.unlatch();
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
		h.tStatus("Ready | Foundation");
		while (!opmode.isStarted()) {
			h.tRunTime();
			opmode.idle();
		}
		opmode.resetStartTime();
	}

	public void run(Boolean blue, Boolean mid, Modes mode) {
		if (blue != null) BLUE = blue;
		if (mid != null) MID = mid;
		init();
		switch(mode) {
			case REGULAR: foundation(); break;
			case TAPE: RIGHT = blue; tape(); break;
			default: opmode.stop();
		}
	}

	private void foundation() {
		try {
			h.tStatus("Running");

			/* Instructions - Foundation */
//			a.movB(1.0, 1.5, 2.0);

			a.movH(5.0, 1.5, 3.0, BLUE);

			a.movB(3.8, 1.2, 2.8);
			a.movB(2.8, 0.5, 3.6);
			a.latch();
			a.cTrnH(1.9, 90, 2.0, 3.5, !BLUE);
			a.movB(8.0, 1.0, 2.9);
			a.unlatch();
			a.movF(1.0, 1.0, 0.7);

			if (MID) a.movH(6.2, 1.5, 3, !BLUE);
			else a.movH(1,2,1, BLUE);

			a.movF(8.5, 1.0, 3.5);
			a.movH(0.5, 2, 1, !(BLUE && MID));
			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}

	private void tape() {
		try {
			h.tStatus("Running");

			/* Instructions - Tape */
			if (!MID)
				a.movF(2.0, 2.0, 2.0);
			else {
				a.movF(5.2, 1.5, 3.0);
				a.movH(3.5, 2.0, 2.0, RIGHT);
			}

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}
}
