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
			h.initTelemetry();
			h.initHardware(opmode.hardwareMap);
			a.unlatch();
		} catch (Exception e) { h.except(e); }

		h.tStatus("Ready - " + (BLUE ? "Blue" : "Red") + "Found " + (MID ? "Mid" : "Wall"));
		h.update();
		while (!opmode.isStarted()) {
			h.tRunTime(); h.update();
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
		try { /* Instructions - Foundation */
			h.tStatus("Running"); h.update();
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
		} catch (Exception e) { h.except(e); }
	}

	private void tape() {
		try { /* Instructions - Tape */
			h.tStatus("Running"); h.update();
			if (!MID)
				a.movF(2.0, 2.0, 2.0);
			else {
				a.movF(5.2, 1.5, 3.0);
				a.movH(3.5, 2.0, 2.0, RIGHT);
			}

			h.tStatus("Done!");
		} catch (Exception e) { h.except(e); }
	}
}
