package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="Auto") public class Auto extends LinearOpMode {
	// Initializations
	private Telemetry t = telemetry;
	private Hardware h = new Hardware(t, this);
	private AutoBase a = new AutoBase(h, this);
	private AutoBase.AutoTelemetry tUpdate = a.new AutoTelemetry();
	private AutoBase.SepThreadMov sepThreadMov = a.new SepThreadMov();

	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
		try {
			h.init(hardwareMap);
			h.initAuto(hardwareMap);
			// Start telemetry thread
			Thread t = new Thread(tUpdate);
			t.start();
		} catch(Exception e) {
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
		h.tStatus("Ready");
		waitForStart();

		try {
			// Instructions:
			// Red Skystone
			/*
			a.findSkystone(sepThreadMov, "Red");
			a.pickUp();
			a.movB_(200, 1);
			a.movR_(500, 1);
			a.drop();
			a.movL_(3_000, 1);
			// */
			// Red Foundation
			/*

			// */
			// Blue Skystone
			/*f
			a.findSkystone(sepThreadMov, "Blue");
			a.pickUp();
			a.movB(5, 1);
			a.movL(20, 1);
			a.drop();
			a.movR(15, 1);
			// */
			// Blue Foundation
			/*

			// */

			// TESTING GOES HERE:
			//a.movF(10, 1);
			a.modeSRE(h.drive_lf);
			h.drive_lf.setTargetPosition(20*28);
			a.modeRTP(h.drive_lf);
			h.drive_lf.setPower(0.7);
			while (h.drive_lf.isBusy()) {
				h.tStatus(Integer.toString(h.drive_lf.getCurrentPosition()));
				idle();
			}
			h.drive_lf.setPower(0);

		// Catches exceptions as plain-text
		} catch(Exception e) {
			h.tErr("Auto Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}
