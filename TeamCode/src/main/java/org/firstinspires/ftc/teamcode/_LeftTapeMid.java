package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

//@Disabled
@Autonomous(name="Left Tape Mid", group="Tape") public class _LeftTapeMid extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this, telemetry);

	@Override
	public void runOpMode() {
		base.init();
		base.tape(true, true);
	}
}