package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

//@Disabled
@Autonomous(name="Skystone-Wall B", group="Blue") public class BlueSkystoneWall extends LinearOpMode {
	/*Initializations*/
	private __Hardware__ h = new __Hardware__(telemetry, this);
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
			a.movF(3.5, 1.0, 2.4);
			double sEnc = a.findSkystone(1,0.3);
			if (sEnc >= 0) {
				a.movF(3.2, 1.0, 1.5);
				a.pickUp();
				a.movB(1.3, 1.0, 0.7);
			} else {
				a.movF(2.0, 1.0, 1.5);
			}
			a.trnL(1.0,0.9, 1.6);
			a.movL(6.0, 1.0, 2.6);
			a.movF(sEnc > /*me*/4.5 ? ( sEnc > /*fm*/5.0 ?/*middle*/12.0 : /*far*/16.0) : /*end*/10.0, 1.0, 4.6);
			if (sEnc >= 0) {
				a.drop();
				a.movB(3.6, 1.0);
			}
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
