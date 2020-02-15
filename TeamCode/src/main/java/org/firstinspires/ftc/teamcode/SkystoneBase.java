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
	private boolean nobridge = true;

	public void init() {
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

	public void run(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - SkyStone */
			if (nobridge) a.movF(3.5, 1.0, 2.4);

			double sPos = a.findSkystone(blue,0.6);

			if (sPos == -1) a.movF(1.7, 1.0, 1.5);
			else {
				a.movF(2.2, 1.2, 1.5);
				a.pickUp();
				a.movB(1.3, 1.2, 0.7);
			}

			if (blue) a.trnL(1.05, 2.0, 1.0);
			else a.trnR(1.05, 2.0, 1.0);

			if (!mid) {
				if (blue) a.movL(6.4, 2.0, 2.4);
				else a.movR(6.4, 2.0, 2.4);
			}
			// 1 = farthest from wall, 3 = nearest
			a.movF(sPos == 1 ? 9.5 : (sPos == 2 ? 10.5 : 12.5), 3, 2.0);
			// Don't try to drop the skystone off if findSkystone failed
			if (sPos != -1) {
				a.drop();
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

	// Bridge from foundation to skystone
	void bridge(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - Bridge */
			nobridge = false;
			a.movF(3.0, 2.0, 4.0);

			if (blue) a.trnL(1.0, 1.5, 2.0);
			else a.trnR(1.0, 1.5, 2.0);

			if (!mid) a.movF(3.0, 1.5, 3.0);

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}

	void speed(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - Speed */
			// block 1
			a.movF(5.6, 1.0, 2.4);
			a.pickUp();
			a.movB(1.3, 1.2, 0.7);

			if (blue) a.trnL(1.0, 2.0, 2.0);
			else a.trnR(1.0, 2.0, 2.0);

			if (!mid) {
				if (blue) a.movL(6.0, 2.0, 2.6);
				else a.movR(6.0, 2.0, 2.6);
			}

			a.movF(8.5, 1.0, 2.4);
			a.drop();
			// block 2

			a.movB(8.5, 1.0, 2.4);

			if (!mid) {
				if (blue) a.movR(6.0, 2.0, 2.6);
				else a.movL(6.0, 2.0, 2.6);
			}

			if (blue) a.trnR(1.0, 2.0, 2.0);
			else a.trnL(1.0, 2.0, 2.0);

			a.movF(1.3, 1.2, 0.7);

			a.pickUp();
			a.movB(1.3, 1.2, 0.7);

			if (blue) a.trnL(1.0, 2.0, 2.0);
			else a.trnR(1.0, 2.0, 2.0);

			if (!mid) {
				if (blue) a.movL(6.0, 2.0, 2.6);
				else a.movR(6.0, 2.0, 2.6);
			}

			a.movF(8.5, 1.0, 2.4);
			a.drop();

			// block 3
			a.movB(8.5, 1.0, 2.4);

			if (!mid) {
				if (blue) a.movR(6.0, 2.0, 2.6);
				else a.movL(6.0, 2.0, 2.6);
			}

			if (blue) a.trnR(1.0, 2.0, 2.0);
			else a.trnL(1.0, 2.0, 2.0);

			a.movF(1.3, 1.2, 0.7);

			a.pickUp();
			a.movB(1.3, 1.2, 0.7);

			if (blue) a.trnL(1.0, 2.0, 2.0);
			else a.trnR(1.0, 2.0, 2.0);

			if (!mid) {
				if (blue) a.movL(6.0, 2.0, 2.6);
				else a.movR(6.0, 2.0, 2.6);
			}

			a.movF(8.5, 1.0, 2.4);
			a.drop();

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}
}
