package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Blue All Wall", group="All") public class _BlueWallAll extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		base.run(true, false, SkystoneBase.Modes.COMBO);
	}
}
