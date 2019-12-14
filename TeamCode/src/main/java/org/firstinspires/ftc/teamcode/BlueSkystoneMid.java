package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled
@Autonomous(name="Blue Skystone Mid") public class BlueSkystoneMid extends LinearOpMode {
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
//			h.tDebug();
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
			/* Instructions - Blue SkyStone Mid */
			h.tStatus("Scanning");
			a.movF(s.movF1, s.p1);
			double sTime = a.findSkystone(1, s.pS);//4.2 far stone 3.7 middle 2.2 end
			a.movF(s.movF2, s.p2);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(s.movB3,s.p3);
			a.trnL(s.trnH4,s.p4);
			a.movF(sTime > 3.5 ? s.movT5 : s.movF5, s.p5);
			a.drop();

			h.tSub("Moving under Bridge");
			a.movB(s.movB7, s.p7);

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