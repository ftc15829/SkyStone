package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="Blue Foundation") public class BlueFoundation extends LinearOpMode {
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
			// Blue Foundation
			a.movF(0.5, 1);
			a.trnL(2.0, 1);
			a.movR(7.0, 1);
			a.movB(5.0, 1);
			a.latch();
			a.movF(6.0, 1);
			a.movR(4.0, 1);
			a.unlatch();
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