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
			// Possible values found in findSkystone
			double sPos = a.findSkystone(blue,0.6);
			// If findSkystone failed, don't try to grab skystone
			if (sPos == -1) a.movF(1.7, 1.0, 1.5);
			else {
				a.movF(2.2, 1.0, 1.5);
				a.pickUp();
				a.movB(1.3, 1.0, 0.7);
			}
			// Will turn left if the team is Blue, right otherwise
			if (blue) a.trnL(1.1, 1.0, 2.0);
			else a.trnR(1.1, 1.0, 2.0);
			// If end goal is wall
			if (!mid) {
				// Will turn left if the team is Blue, right otherwise
				if (blue) a.movL(6.0, 1.0, 2.6);
				else a.movR(6.0, 1.0, 2.6);
			}
			// 1 = farthest from wall
			a.movF(sPos == 1 ? 5.5 : (sPos == 2 ? 7.5 : 9.5), 1.0, 4.6);
			// Don't try to drop the skystone off if findSkystone failed
			if (sPos != -1) {
				a.drop();
				a.movB(3.6, 1.0);
			}

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}

	public void bridge(boolean blue, boolean mid) {
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
}
