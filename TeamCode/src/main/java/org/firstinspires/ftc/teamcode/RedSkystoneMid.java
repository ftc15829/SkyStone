package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled
@Autonomous(name="Red Skystone Mid") public class RedSkystoneMid extends LinearOpMode {
	/*Initializations*/
	private Telemetry tele = telemetry;
	private __Hardware__ h = new __Hardware__(tele, this);
	private __AutoBase__ a = new __AutoBase__(h, this);
	private __Skystone__ s = new __Skystone__();

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
			/* Instructions - Red SkyStone Mid */
			a.movF(3.1, 1.0);
			double sTime = a.findSkystone(3, 0.6);//4.2 far stone 3.7 middle 2.2 end
			a.movF(3.0, 1.0);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(0.5,1.0);
			a.trnR(1.0,0.9);
			a.movF(sTime > 3.5 ? 12.7 : 10.0, 0.4);
			a.drop();

			h.tSub("Moving under Bridge");
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