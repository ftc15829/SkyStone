package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SkystoneBase {
	SkystoneBase(LinearOpMode linearOpMode, Telemetry telemetry) {
		opmode = linearOpMode;
		h = new __Hardware__(opmode, telemetry);
		a = new __AutoBase__(h, opmode);
	}
	LinearOpMode opmode;
	__Hardware__ h;
	__AutoBase__ a;
	private boolean nobridge = true;

	public void init() {
		try {
			h.init(opmode.hardwareMap);
			h.initAuto(opmode.hardwareMap);
			a.unlatch();
		} catch (Exception e) {
			h.tStatus("Error");
			h.tErr("HardwareMap", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
		h.tStatus("Ready | Skystone");
		while (!opmode.isStarted()) {
			h.tRunTime();
			opmode.idle();
		}
		opmode.resetStartTime();
	}

	//************************************************************
	//****************New side camera code area*******************
	//************************************************************

	public void run(boolean blue, boolean mid, boolean n) {
		try
		{
			h.tStatus("Running");

			/* Instructions - SkyStone */
			a.movR(6.1, 1.25, 2.5);

			int sPos = a.findSkystone(blue,0.6, true);

			a.pickUp1();a.drop2();
			a.movR(2.3, 2, 2);

			       a.pickUp2();//new pickup
a.drop1();
			a.movL(2.3, 2, 2);


				if (blue) {
					switch (sPos) {
						case 1: {
							a.movF(12.0, 1.5, 3.0);
							break;
						}
						case 2: {
							a.movF(14.5, 1.5, 4.0);
							break;
						}
						case 3: {
							a.movF(16.0, 1.5, 5);
							break;
						}
					}

				}
				else {
					switch (sPos) {
						case 1: {
							a.movB(12.0, 1.5, 3.0);
							break;
						}
						case 2: {
							a.movB(14.5, 1.5, 4.0);
							break;
						}
						case 3: {
							a.movB(16.0, 1.5, 5);
							break;
						}
					}
				}

				a.drop2();//new drop
a.pickUp1();
				if (blue) {
					switch (sPos) {
						case 1: {
							a.movB(15.0, 1.5, 3.0);
							break;
						}
						case 2: {
							a.movB(17.5, 1.5, 4.0);
							break;
						}
						case 3: {
							a.movB(14.0, 1.5, 5);
							break;
						}
					}

				} else {
					switch (sPos) {
						case 1: {
							a.movF(15.0, 1.5, 3.0);
							break;
						}
						case 2: {
							a.movF(17.5, 1.5, 4.0);
							break;
						}
						case 3: {
							a.movF(14.0, 1.5, 5);
							break;
						}
					}
				}
				a.movR(1, 2.3, 1.0);
				        a.pickUp1(); a.drop2();               //new pickup
				a.movL(1, 2.3, 1.0);

				if (blue) {
					switch (sPos) {
						case 1: {
							a.movF(15.0, 1.5, 3.0);
							break;
						}
						case 2: {
							a.movF(17.5, 1.5, 4.0);
							break;
						}
						case 3: {
							a.movF(14.0, 1.5, 5);
							break;
						}
					}

				} else {
					switch (sPos) {
						case 1: {
							a.movB(15.0, 1.5, 3.0);
							break;
						}
						case 2: {
							a.movB(17.5, 1.5, 4.0);
							break;
						}
						case 3: {
							a.movB(14.0, 1.5, 5);
							break;
						}
					}
				}

				a.drop2();//new drop
a.pickUp1();
				//move foundation: grab and rotate foundation 180 degrees (see Ethan)

				//park on tape: wall or mid


			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}



	public void run(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - SkyStone */
			if (nobridge) a.movF(3.7, 1.0, 2.4); // 3.5

			double sPos = a.findSkystone(blue,0.6);

			if (sPos == -1) a.movF(0.8, 1.0, 1.5); // 1.7
			else {
				a.movF(2.1, 1.2, 1.5); // 2.2
				a.pickUp();
				a.movB(1.3, 1.2, 0.7);
			}

			if (blue) a.trnL(1.05, 2.0, 1.0);
			else a.trnR(1.05, 2.0, 1.0);

			if (!mid) {
				if (blue) a.movL(6.4, 2.0, 2.4);
				else a.movR(6.4, 2.0, 2.4);
			}
			// 1 = farthest from wall, 3 = nearest
			a.movF(sPos == 1 ? 9.5 : (sPos == 2 ? 10.5 : sPos == 3 ? 12.5 : 9.0), 2, 2.0);
			// Don't try to drop the skystone off if findSkystone failed
			if (sPos != -1) {
				a.drop();
				a.movB(3.3, 1.5, 1.7);
			}

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}

	// Bridge from foundation to skystone
	void bridge(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - Bridge */
			nobridge = false;
			a.movF(3.0, 2.0, 4.0);

			if (blue) a.trnL(1.0, 1.5, 2.0);
			else a.trnR(1.0, 1.5, 2.0);

			if (!mid) a.movF(3.0, 1.5, 3.0);

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}

	void speed(boolean blue, boolean mid) {
		try {
			h.tStatus("Running");

			/* Instructions - Speed */
			// block 1
			a.movF(5.6, 1.0, 2.4);
			a.pickUp();
			a.movB(1.3, 1.2, 0.7);

			if (blue) a.trnL(1.0, 2.0, 2.0);
			else a.trnR(1.0, 2.0, 2.0);

			if (!mid) {
				if (blue) a.movL(6.0, 2.0, 2.6);
				else a.movR(6.0, 2.0, 2.6);
			}

			a.movF(8.5, 1.0, 2.4);
			a.drop();
			// block 2

			a.movB(8.5, 1.0, 2.4);

			if (!mid) {
				if (blue) a.movR(6.0, 2.0, 2.6);
				else a.movL(6.0, 2.0, 2.6);
			}

			if (blue) a.trnR(1.0, 2.0, 2.0);
			else a.trnL(1.0, 2.0, 2.0);

			a.movF(1.3, 1.2, 0.7);

			a.pickUp();
			a.movB(1.3, 1.2, 0.7);

			if (blue) a.trnL(1.0, 2.0, 2.0);
			else a.trnR(1.0, 2.0, 2.0);

			if (!mid) {
				if (blue) a.movL(6.0, 2.0, 2.6);
				else a.movR(6.0, 2.0, 2.6);
			}

			a.movF(8.5, 1.0, 2.4);
			a.drop();

			// block 3
			a.movB(8.5, 1.0, 2.4);

			if (!mid) {
				if (blue) a.movR(6.0, 2.0, 2.6);
				else a.movL(6.0, 2.0, 2.6);
			}

			if (blue) a.trnR(1.0, 2.0, 2.0);
			else a.trnL(1.0, 2.0, 2.0);

			a.movF(1.3, 1.2, 0.7);

			a.pickUp();
			a.movB(1.3, 1.2, 0.7);

			if (blue) a.trnL(1.0, 2.0, 2.0);
			else a.trnR(1.0, 2.0, 2.0);

			if (!mid) {
				if (blue) a.movL(6.0, 2.0, 2.6);
				else a.movR(6.0, 2.0, 2.6);
			}

			a.movF(8.5, 1.0, 2.4);
			a.drop();

			h.tStatus("Done!");
		} catch (Exception e) { // Catches exceptions as plain-text
			h.tStatus("Error");
			h.tErr("Auto Runtime", e);
			opmode.sleep(15_000);
			opmode.stop();
		}
	}
}
