package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="Red Skystone") public class RedSkystone extends LinearOpMode {
	/*Initializations*/
	private Telemetry tele = telemetry;
	private Hardware h = new Hardware(tele, this);
	private AutoBase a = new AutoBase(h, this);
//	private AutoBase.AutoTelemetry tUpdate = a.new AutoTelemetry(); // Depreciated

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
			/*Instructions*/
			// Red Skystone
			a.movF(3.1, 1);
			double sTime = a.findSkystone(3,0.6);//4.2 far stone 3.7 middle 2.2 end
			a.movF(3.0, 1);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(.5,1);
			a.trnR(1.0,0.9);

			boolean egg = false;
			if (sTime > 3.5 && sTime < 5)
				a.movF(12.0, 1);
			else if (sTime < 3.5)
				a.movF(11.0,1);
			else if (sTime > 5)
				a.movF(14,1);
			else {
				a.movF(11.5, 1);
				egg = true;
			}

			a.drop();

			h.tSub("Moving under Bridge");
			a.movB(4.0, 1);
			if (egg) {
				a.trnL(0.1, 1);
			}
			h.tStatus("Done!");
			/*End*/
				// Catches exceptions as plain-text
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}