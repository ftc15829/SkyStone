package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="R Skystone Mid", group="Skystone") public class _RedSkystoneMid extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		base.run(false, true, SkystoneBase.Modes.STRAFE);
	}
}
