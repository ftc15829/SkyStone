package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="B Found. Mid", group="Foundation") public class _BlueFoundationMid extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this, telemetry);
	@Override public void runOpMode() {
		base.init();
		base.run(true, true);
	}
}