package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FoundationBase {
	FoundationBase(LinearOpMode opMode) { this.opmode = opMode; }
	LinearOpMode opmode;
	__Hardware__ h = new __Hardware__(opmode);
	__AutoBase__ a = new __AutoBase__(h, opmode);

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
		h.tStatus("Ready");
		opmode.waitForStart();
		opmode.resetStartTime();
	}

	public void run(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - Foundation */
			a.movB(1.0, 0.9, 0.8);
			if (blue) a.movR(11.5, 0.9, 3.8);
			else a.movL(11.5, 0.9, 3.8);
			a.movB(5.4, 0.9, 2.5);
			a.movB(1.1, 0.5, 1.2);
			a.latch();
			if (blue) a.customTrn(0.3, 1.0, 3400);
			else a.customTrn(1.0, 0.3, 3400);
			a.movB(3.8, 1.0, 1.8);
			a.unlatch();
			a.movF(1.0, 1.0, 0.8);
			if (blue == mid) a.movL(mid ? 5.4 : 1.3, 1.0, 1.5);
			else if (blue != mid) a.movR(mid ? 5.4 : 1.3, 1.0, 1.5);
			a.movF(mid ? 9.0 : 9.5, 1.0, 3.5);

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}
}