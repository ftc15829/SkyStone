package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SkystoneBase {
	SkystoneBase(LinearOpMode linearOpMode, Telemetry telemetry) {
		opmode = linearOpMode;
		h = new __Hardware__(opmode, telemetry);
		a = new __AutoBase__(h, opmode);
	}
	LinearOpMode opmode;
	__Hardware__ h;
	__AutoBase__ a;

	private boolean BLUE, MID;

	enum Modes { COMBO, STRAFE, THROTTLE }

	private void init() {
		try {
			h.initTelemetry();
			h.initHardware(opmode.hardwareMap);
			h.initVision();
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

	public void run(boolean blue, boolean mid, Modes mode) {
		BLUE = blue; MID = mid;
		init();
		switch(mode) {
			case STRAFE: findSkystone_strafe(); break;
			case COMBO: combo(); break;
			default: opmode.stop();
		}
	}

	private void combo() {
		try { /* Instructions - SkyStone */
			h.tStatus("Running"); h.update();
			a.movR(6.1, 1.25, 2.5);

			int sPos = a.findSkystone(BLUE,0.6, false);
			a.side_setup();
			a.movR(2.3, 2, 2);
			a.side_grab();
			a.movL(2.3, 2, 2);

			switch (sPos) {
				case 1: a.movV(14.0, 1.5, 3.0, BLUE); break;
				case 2: a.movV(16.5, 1.5, 4.0, BLUE); break;
				case 3: a.movV(18.0, 1.5, 5, BLUE); break;
				default: break; // FIXME
			}
			a.movH(2,2,2, BLUE);
			a.side_drop();
			a.movL(1,2,1);
			a.trnL(1,2,2);
			a.movB(1.5,2,2);
			a.latch();

			// move foundation: grab and rotate foundation 180 degrees
			h.tStatus("Done!");
		} catch (Exception e) { h.except(e); }
	}

	private void findSkystone_strafe() {
		try { /* Instructions - SkyStone */
			h.tStatus("Running");
			a.movF(3.7, 1.0, 2.4);

			int sPos = a.findSkystone(BLUE,0.6, true);

			if (sPos == -1) a.movF(0.8, 1.0, 1.5);
			else {
				a.movF(2.1, 1.2, 1.5);
				a.front_grab();
				a.movB(1.3, 1.2, 0.7);
			}

			a.trnH(1.05, 2.0, 1.0, !BLUE);
			if (!MID) a.movH(6.4, 2.0, 2.4, !BLUE);
			// 1 = farthest from wall, 3 = nearest
			switch (sPos) {
				case 1: a.movF(9.5, 2, 3.0); break;
				case 2: a.movF(10.5, 2, 4.0); break;
				case 3: a.movF(12.5, 2, 5.0); break;
				default: a.movF(13.0, 2, 5.5); break;
			}
			// Don't try to drop the skystone off if findSkystone failed
			if (sPos != -1) {
				a.front_drop();
				a.movB(3.3, 1.5, 1.7);
			}

			h.tStatus("Done!");
		} catch (Exception e) { h.except(e); }
	}
}
