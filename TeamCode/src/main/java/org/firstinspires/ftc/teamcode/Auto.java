package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="Auto") public class Auto extends LinearOpMode {
/*Initializations*/
	private Telemetry tele = telemetry;
	private Hardware h = new Hardware(tele, this);
	private AutoBase a = new AutoBase(h, this);
//	private AutoBase.AutoTelemetry tUpdate = a.new AutoTelemetry(); // Depreciated

	// Runs when initialized
	@Override
	public void runOpMode() {
		// Initiate hardware
		waitForStart();
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

		try {
			h.tStatus("Running");
/*Instructions*/
			// Red Skystone
			a.movF(3.3, 1);
			a.findSkystone(3,0.6);
			a.pickUp(); h.tStatus("Moving to Foundation");
			a.trnR(1.0,0.9);
			a.movF(12.0, 1);
			a.drop(); h.tSub("Moving under Bridge");
			a.movB(4.0, 1); h.tStatus("Done!");

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