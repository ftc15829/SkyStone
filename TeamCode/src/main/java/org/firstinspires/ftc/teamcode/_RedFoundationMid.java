package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Foundation-Mid R", group="Red") public class _RedFoundationMid extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this);
	@Override
	public void runOpMode() {
		base.init();
		base.run(false, true);
	}
}