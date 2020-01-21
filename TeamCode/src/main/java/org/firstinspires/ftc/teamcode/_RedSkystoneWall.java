package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Skystone-Wall R", group="Red") public class _RedSkystoneWall extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this);
	@Override
	public void runOpMode() {
		base.init();
		base.run(false, false);
	}
}
