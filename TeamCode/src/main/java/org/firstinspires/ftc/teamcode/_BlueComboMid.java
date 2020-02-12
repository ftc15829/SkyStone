package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="B Combo Mid", group="Combo") public class _BlueComboMid extends LinearOpMode {
	private FoundationBase fbase = new FoundationBase(this, telemetry);
	private SkystoneBase sbase = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		sbase.init();
		fbase.run(true, true);
		sbase.bridge(true, true);
		sbase.run(true, true);
	}
}