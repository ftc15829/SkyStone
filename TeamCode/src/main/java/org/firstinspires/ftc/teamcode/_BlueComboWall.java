package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="B Combo Wall", group="Combo") public class _BlueComboWall extends LinearOpMode {
	private FoundationBase fbase = new FoundationBase(this, telemetry);
	private SkystoneBase sbase = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		sbase.init();
		fbase.run(true, false);
		sbase.bridge(true, false);
		sbase.run(true, false);
	}
}
