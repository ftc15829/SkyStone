package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Debug", group="Debug") public class _Debug extends LinearOpMode {
	/*Initializations*/

	__Hardware__ h = new __Hardware__(this, telemetry);
//	__AutoBase__ a = new __AutoBase__(h, this);

	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
		try {
//			h.init(hardwareMap);
//			h.initAuto(hardwareMap);
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
		h.tStatus("Ready | Skystone");
//		h.tSub("OK");
		while (!isStarted()) {
//			h.tRunTime();
			idle();
		}
		resetStartTime();
		try {
			h.tStatus("Running");
			/* Instructions - Debug */
			h.tStatus("Debug");
			sleep(2000);
			/* End */
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Debug Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}