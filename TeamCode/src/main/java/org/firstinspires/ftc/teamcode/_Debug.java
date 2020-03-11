package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Debug", group="Debug") public class _Debug extends LinearOpMode {
	/*Initializations*/

	__Hardware__ h = new __Hardware__(this, telemetry);
	__AutoBase__ a = new __AutoBase__(h, this);

	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
		try {
			h.initTelemetry();
			h.initHardware(hardwareMap);
			h.initVision(hardwareMap);
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
//		telemetry.setAutoClear(false);
//		Telemetry.Item status = telemetry.addData("Status", "");
//		telemetry.update();
		while (!isStarted()) {
//			h.tRunTime();
			idle();
		}
		resetStartTime();
		try {
			/* Instructions - Debug */
//			h.tStatus("notValue");
//			sleep(2000);
//			telemetry.addData("Encoder", "shark");
//			telemetry.update();
//			sleep(2000);
//			h.tStatus("Value");
//			h.tStatus("Halp");
//			telemetry.clear();
			a.motorPosition(h.drive_lf, 1, 1, 3.0);
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