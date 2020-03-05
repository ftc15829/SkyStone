package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Blue All Mid", group="All") public class _BlueMidAll extends LinearOpMode {
	private SkystoneBase base = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		base.run(true, true, SkystoneBase.Modes.COMBO);
	}
}