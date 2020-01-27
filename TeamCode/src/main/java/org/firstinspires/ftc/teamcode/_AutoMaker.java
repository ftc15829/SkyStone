package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="AutoMaker", group="Debug") public class _AutoMaker extends LinearOpMode {
	__DriveBase__ d = new __DriveBase__(this, telemetry);
	@Override public void runOpMode() {
		d.init();
		try {
			while (opModeIsActive()) {
				d.update(1);
			}
		} catch (Exception e) {
			d.except(e);
		}
	}
}
