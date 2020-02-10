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

			/* Instructions - Foundation *///FIXME
			a.movB(1.0, 1.5, 2.5);
			if (blue) a.movR(1.5, 1.5, 3.8);
			else a.movL(3.5, 1.5, 3.8); // 2.6
			a.movB(2.8, 1.2, 3.5);
			a.movB(2.8, 0.6, 3.6);
			a.latch();
			a.movF(2.8, 1.3, 3.0);
			if (blue) a.customTrn(-0.5, 1.2, 3400);
			else a.customTrn(1.2, -0.5, 2300);
			a.movB(4.0, 1.0, 1.8);
			a.unlatch();
			a.movF(1.0, 1.0, 0.8);
			if (blue == mid) a.movL(mid ? 5.7 : 1.3, 1.0, 1.5);
			else if (blue != mid) a.movR(mid ? 0.0 : 1.3, 1.5, 1.5);
			a.movF(mid ? 8.5 : 9.5, 1.0, 3.5);


			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}
}
