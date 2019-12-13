package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="Blue Skystone") public class BlueSkystone extends LinearOpMode {
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
			// Blue Skystone
			a.movF(3.1, 1);
			double sTime = a.findSkystone(1,0.6);//4.2 far stone 3.7 middle 2.2 end
			a.movF(3.0, 1);
			a.pickUp();

			h.tStatus("Moving to Foundation");
			a.movB(.5,1);
			a.trnL(1.0,0.9);
			a.movF(sTime > 3.5 ? 12.0 : 11.0, 1)
			a.drop();

			h.tSub("Moving under Bridge");
			a.movB(4.0, 1);
			
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