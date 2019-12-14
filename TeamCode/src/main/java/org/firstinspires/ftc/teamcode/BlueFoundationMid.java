package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled
@Autonomous(name="Blue Foundation Mid") public class BlueFoundationMid extends LinearOpMode {
	/*Initializations*/
	private Telemetry tele = telemetry;
	private __Hardware__ h = new __Hardware__(tele, this);
	private __AutoBase__ a = new __AutoBase__(h, this);
	private __Foundation__ f = new __Foundation__();

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
			/* Instructions - Blue Foundation Mid */
			h.tStatus("Latching");
			a.movB(f.movB1, f.p1);
			a.movR(f.movH2, f.p2);
			a.movB(f.movB3, f.p3);
			a.movB(f.movB4, f.p4);
			a.latch();

			h.tStatus("Unlatching");
			a.movF(f.movF5, f.p5);
			a.movF(f.movF6, f.p6);
			a.unlatch();

			h.tStatus("Line");
			a.movL(f.movH7, f.p7);
			a.movB(f.movB8, f.p8);
			a.movL(f.movH9, f.p9);

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