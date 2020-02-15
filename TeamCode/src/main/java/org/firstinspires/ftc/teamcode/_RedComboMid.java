package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name="R Combo Mid", group="Combo") public class _RedComboMid extends LinearOpMode {
	private FoundationBase fbase = new FoundationBase(this, telemetry);
	private SkystoneBase sbase = new SkystoneBase(this, telemetry);
	@Override public void runOpMode() {
		sbase.init();
		fbase.run(false, true);
		sbase.bridge(false, true);
		sbase.run(false, true);
	}
}
