package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

//@Disabled
@Autonomous(name="Skystone-Wall R", group="Red") public class RedSkystoneWall extends LinearOpMode {
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
			h.initAuto(hardwareMap);
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
			/* Instructions - Red SkyStone Wall */
			a.movF(3.1, 1.0);
			double sTime = a.findSkystone(3, 0.6);//4.2 far stone 3.7 middle 2.2 end
			if (sTime >= 0) {
				a.movF(3.0, 1.0);
				a.pickUp();
				a.movB(0.5, 1.0);
			} else {
				a.movF(2.5, 1.0);
			}
			a.trnR(1.0,0.9);
			a.movR(6.0, 1.0);
			a.movF(sTime > 3.5 ? 12.7 : 10.0, 0.4);
			a.drop();
			a.movB(4.0, 1.0);
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