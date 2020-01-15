package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled
@Autonomous(name="Red Foundation Mid") public class RedFoundationMid extends LinearOpMode {
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
			/* Instructions - Red Foundation Mid */
			a.movB(1.0, 1.0);
			a.movL(9.0, 1.0);
			a.movB(6.0, 1.0);
			a.movB(0.8, 0.5);

			a.latch();
			a.movF(7.0, 1.0);
			a.movF(1.4, 0.5);
			a.unlatch();

			a.movR(8.0, 1.0);
			a.movB(5.5, 1.0);
			a.movR(5.0, 1.0);

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