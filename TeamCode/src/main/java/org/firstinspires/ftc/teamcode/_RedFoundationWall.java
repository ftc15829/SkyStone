package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="R Found. Wall", group="Foundation") public class _RedFoundationWall extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this, telemetry);
	@Override public void runOpMode() {
		base.init();
		base.run(false, false);
	}
}
