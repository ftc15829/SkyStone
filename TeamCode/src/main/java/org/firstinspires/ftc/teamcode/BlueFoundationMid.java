package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

//@Disabled
@Autonomous(name="Foundation-Mid B", group="Blue") public class BlueFoundationMid extends LinearOpMode {
	/*Initializations*/
	private Telemetry tele = telemetry;
	private __Hardware__ h = new __Hardware__(tele, this);
	private __AutoBase__ a = new __AutoBase__(h, this);

	// Runs when initialized
	@Override
	public void runOpMode() {
		// Initiate hardware
		try {
			h.init(hardwareMap);
			a.unlatch();
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
		h.tStatus("Ready");
		waitForStart();
		resetStartTime();
		try {
			h.tStatus("Running");
			/* Instructions - Blue Foundation Mid */
			a.movB(1.0, 0.9, 0.8);
			a.movR(11.5, 0.9, 3.8);
			a.movB(5.4, 0.9, 2.5);
			a.movB(1.1, 0.5, 1.2);
			a.latch();
			a.customTrn(0.3, 1.0, 3400);
			a.movB(3.8, 1.0, 1.8);
			a.unlatch();
			a.movF(1.0, 1.0, 0.8);
			a.movL(5.4, 1.0, 1.5);
			a.movF(9.0, 1.0, 3.5);
			h.tStatus("Done!");
			/* End */
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}