package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="R Combo Wall", group="Combo") public class _RedComboWall extends LinearOpMode {
	private FoundationBase fbase = new FoundationBase(this, telemetry);
	private SkystoneBase sbase = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		sbase.init();
		fbase.run(false, false);
		sbase.bridge(false, false);
		sbase.run(false, false);
	}
}
