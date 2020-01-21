package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="F-Wall B", group="Blue") public class _BlueFoundationWall extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this);
	@Override
	public void runOpMode() {
		base.init();
		base.run(true, false);
	}
}
