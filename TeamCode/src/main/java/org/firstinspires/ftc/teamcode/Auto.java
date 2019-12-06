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
	private AutoBase.SepThreadMov sepThreadMov = a.new SepThreadMov();

	// Runs when initialized
	@Override
	public void runOpMode() {
		// Initiate hardware
		try {
			h.init(hardwareMap);
			h.initAuto(hardwareMap);
			/*// Start telemetry thread // Depreciated
			Thread t = new Thread(tUpdate);
			t.start();*/
		} catch (Exception e) {
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
		h.tStatus("Ready");
		waitForStart();

		try {

/*Instructions*/
			// Red Skystone
			a.movF(3,1);
			a.findSkystone(sepThreadMov, 3);
			a.pickUp();
			a.movB(3, 1);
			a.movR(20, 1);
			a.drop();
			a.movL(10, 1);

/*End*/
		// Catches exceptions as plain-text
		} catch (Exception e) {
			h.tErr("Auto Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}