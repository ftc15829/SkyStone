package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@TeleOp(name="Drive") public class Drive extends LinearOpMode {
	__DriveBase__ d = new __DriveBase__(this, telemetry);
	@Override public void runOpMode() {
		d.init();
		try {
			while (opModeIsActive()) {
				d.update();
			}
		} catch(Exception e) {
			d.except(e);
		}
	}
}
