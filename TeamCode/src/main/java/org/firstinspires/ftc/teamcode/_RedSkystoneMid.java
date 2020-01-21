package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="S-Mid R", group="Red") public class _RedSkystoneMid extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this);
	@Override
	public void runOpMode() {
		base.init();
		base.run(false, true);
	}
}
