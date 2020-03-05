package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Blue Found. Wall", group="Foundation") public class _BlueFoundationWall extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this, telemetry);
	@Override public void runOpMode() {
		base.run(true, false, FoundationBase.Modes.REGULAR);
	}
}
