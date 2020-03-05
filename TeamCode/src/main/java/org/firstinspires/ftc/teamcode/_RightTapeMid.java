package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Disabled
@Autonomous(name="Right Tape Mid", group="Tape") public class _RightTapeMid extends LinearOpMode {
	private FoundationBase base = new FoundationBase(this, telemetry);
	@Override public void runOpMode() {
		base.run(true, true, FoundationBase.Modes.TAPE);
	}
}