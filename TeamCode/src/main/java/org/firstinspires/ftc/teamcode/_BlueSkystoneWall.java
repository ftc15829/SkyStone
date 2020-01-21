package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Skystone-Wall B", group="Blue") public class _BlueSkystoneWall extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this);
	@Override
	public void runOpMode() {
		base.init();
		base.run(true, false);
	}
}
