package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SkystoneBase {
	SkystoneBase(LinearOpMode opMode) { this.opmode = opMode; }
	LinearOpMode opmode;
	__Hardware__ h = new __Hardware__(opmode);
	__AutoBase__ a = new __AutoBase__(h, opmode);

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
		h.tStatus("Ready");
		opmode.waitForStart();
		opmode.resetStartTime();
	}

	public void run(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - SkyStone */
			a.movF(3.5, 1.0, 2.4);
			double sEnc = a.findSkystone(blue ? 1 : 3,0.3);
			if (sEnc >= 0) {
				a.movF(3.2, 1.0, 1.5);
				a.pickUp();
				a.movB(1.3, 1.0, 0.7);
			} else a.movF(2.0, 1.0, 1.5);
			if (blue) a.trnL(1.0, 0.9, 1.6);
			else a.trnR(1.0, 0.9, 1.6);
			if (!mid && blue) a.movL(6.0, 1.0, 2.6);
			if (!mid && !blue) a.movR(6.0, 1.0, 2.6);
			a.movF(sEnc > 4.5 ? ( sEnc > 5.0 ? 12.0 : 16.0) : 10.0, 1.0, 4.6);
			if (sEnc >= 0) {
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
}