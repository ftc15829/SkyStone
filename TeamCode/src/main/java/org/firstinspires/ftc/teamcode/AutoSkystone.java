package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

//@Disabled
@Autonomous(name="Blue Skystone Mid") class BlueSkystoneMid extends LinearOpMode {
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
			a.movF(3.1, 1.0);
			double sTime = a.findSkystone(1, 0.6);//4.2 far stone 3.7 middle 2.2 end
			a.movF(3.0, 1.0);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(0.5,1.0);
			a.trnL(1.0,0.9);
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

//@Disabled
@Autonomous(name="Blue Skystone Wall") class BlueSkystoneWall extends LinearOpMode {
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
			/* Instructions - Blue SkyStone Wall */
			a.movF(3.1, 1.0);
			double sTime = a.findSkystone(1, 0.6);//4.2 far stone 3.7 middle 2.2 end
			a.movF(3.0, 1.0);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(0.5,1.0);
			a.trnL(1.0,0.9);
			a.movL(6.0, 1.0);
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

//@Disabled
@Autonomous(name="Red Skystone Mid") class RedSkystoneMid extends LinearOpMode {
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

//@Disabled
@Autonomous(name="Red Skystone Wall") class RedSkystoneWall extends LinearOpMode {
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
			a.movF(3.0, 1.0);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(0.5, 1.0);
			a.trnR(1.0,0.9);
			a.movR(6.0, 1.0);
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