package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="B Found. Mid", group="Foundation") public class _BlueFoundationMid extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this, telemetry);
	@Override public void runOpMode() {
		base.run(true, true, FoundationBase.Modes.REGULAR);
	}
}