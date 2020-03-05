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
			h.init(opmode.hardwareMap);
			h.initAuto(opmode.hardwareMap);
			a.unlatch();
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
		h.tStatus("Ready | Skystone");
		while (!opmode.isStarted()) {
			h.tRunTime();
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
		try
		{
			h.tStatus("Running");

			/* Instructions - SkyStone */
			a.movR(6.1, 1.25, 2.5);

			int sPos = a.findSkystone(BLUE,0.6, false);
				a.side_setup();
			a.movR(2.3, 2, 2);
					a.side_grab();
			a.movL(2.3, 2, 2);

			switch (sPos) {
				case 1:
					a.movV(14.0, 1.5, 3.0, BLUE);
					a.movH(2,2,2, BLUE);
					break;
				case 2:
					a.movV(16.5, 1.5, 4.0, BLUE);
					a.movH(2,2,2, BLUE);
					break;
				case 3:
					a.movV(18.0, 1.5, 5, BLUE);
					a.movH(2,2,2, BLUE);
					break;
			}
			a.side_drop();
			a.movL(1,2,1);
			a.trnL(1,2,2);
			a.movB(1.5,2,2);
			a.latch();

			//move foundation: grab and rotate foundation 180 degrees (see Ethan)

			//park on tape: wall or mid


			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}

	private void findSkystone_strafe() {
		try {
			h.tStatus("Running");

			/* Instructions - SkyStone */
			a.movF(3.7, 1.0, 2.4);

			double sPos = a.findSkystone(BLUE,0.6, true);

			if (sPos == -1) a.movF(0.8, 1.0, 1.5);
			else {
				a.movF(2.1, 1.2, 1.5);
				a.front_grab();
				a.movB(1.3, 1.2, 0.7);
			}

			a.trnH(1.05, 2.0, 1.0, !BLUE);
			if (!MID) a.movH(6.4, 2.0, 2.4, !BLUE);
			// 1 = farthest from wall, 3 = nearest
			a.movF(sPos == 1 ? 9.5 : (sPos == 2 ? 10.5 : sPos == 3 ? 12.5 : 9.0), 2, 2.0);
			// Don't try to front_drop the skystone off if findSkystone failed
			if (sPos != -1) {
				a.front_drop();
				a.movB(3.3, 1.5, 1.7);
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
