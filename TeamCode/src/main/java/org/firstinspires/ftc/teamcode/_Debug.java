package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Disabled
@Autonomous(name="Debug", group="Debug") public class _Debug extends LinearOpMode {
	/*Initializations*/
	private __Hardware__ h = new __Hardware__(this, telemetry);
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
			/* Instructions - Debug */
//			while(opModeIsActive()) {
//				h.updateTfDetect();
//				h.tCaminfo(1);
//			}
			a.cTrnR(2.5, 90, 1.5, 3.5);
			/* End */
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Debug Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}