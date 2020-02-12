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

	public void init() {
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

	public void run(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - Foundation */
//			a.movB(1.0, 1.5, 2.0);

//			if (blue) a.movR(5.0, 1.5, 3.0);
//			else a.movL(5.0, 1.5, 3.0);

			a.movB(3.8, 1.2, 2.8);
			a.movB(2.8, 0.5, 3.6);
			a.latch();

			if (blue) a.cTrnL(1.9, 90, 2.0, 3.5);
			else a.cTrnR(1.9, 90, 2.0, 3.5);

			a.movB(8.0, 1.0, 2.9);
			a.unlatch();
			a.movF(1.0, 1.0, 0.7);

			if (mid)
				if (blue) a.movL(6.8, 1.5, 3);
				else a.movR(6.8, 1.5, 3);

			a.movF(8.5, 1.0, 3.5);


			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}
}
