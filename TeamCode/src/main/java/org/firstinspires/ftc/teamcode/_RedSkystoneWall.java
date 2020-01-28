package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="R Skystone Wall", group="Skystone") public class _RedSkystoneWall extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		base.init();
		base.run(false, false);
	}
}
