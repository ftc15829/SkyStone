# Programming

This section will explain and lay out the process the software development team went through in writing our current codebase. All the way from the first meet to now.

## In The Beginning - League Meet One

Last season, which was our first year as a team, we were unable to abstract out the hardware definitions and configuration into a separate class. This season, however, that was the first thing we did. Compared to trying to figure it out last season, it seemed much more straight forward. We were very excited to see how much we have grown compared to last year, so it was a good start to the season.

### Drive Class

Once we got the hardware class sorted out, we got to work on the drive function. Now that we had one year under our belt, we were able to organize our code with a little more foresight. Our drive function, for example, consisted mainly of update functions in the game loop. We separated them based on motor type, `updateAuxMotors`, `updateCrServos`, `updateServos`, and `updateDrive`. The last of which had a helper function `getDrivePower`. These will be outlined below.

Our `runOpMode` function is shown below. `h` is the name of our abstracted hardware class which will be laid out later on. `h` also handles telemetry (prefixed with `t`) &#8212; `h.tErr` and `h.tDrivePower` in this case. For now, we will focus on the code under the comment "Updates hardware," which are local functions.

```java
@Override void runOpMode() {
    // Initiate hardware
    try {
        h.init(hardwareMap);
    } catch(Exception e) {
        h.tErr("HardwareMap", e);
        sleep(15_000);
        stop();
    }
    h.tStatus("Ready");
    waitForStart();
    h.tStatus("Running");
    try {
        // Update Loop
        while (opModeIsActive()) {
            // Updates gamepad
            h.updateGamepad(gamepad1, gamepad2);
            // Updates hardware
            updateDrive();
            updateAuxMotors();
            updateCrServos();
            updateServos();
            // Updates telemetry
            h.tDrivePower();
        }
        // Catches exceptions as plain-text
    } catch(Exception e) {
        h.tErr("Runtime", e);
        sleep(15_000);
        stop();
    }
}
```

Before that, regarding the try/catch blocks. This season, we vowed to use error handling for once, evident by the two try/catch blocks wrapping the majority of our code. Although it is rudimentary, it was a good start. Later on, we plan to advance our error handling into something more beneficial, especially during autonomous periods.

---

Without getting into Hardware's &mdash; the abstracted hardware class &mdash; territory, the functions included (`update*`) are all housed within the same class. `updateServos` is shown below. It simply toggles the position of the servos we use to latch on to the foundation

```java
private boolean a = true; // Toggle (actual value doesn't matter)
private void updateServos() {
    // Sets f-hook positions
    if (h.button_b) {
        h.fHook_l.setPosition(a ? 1.0 : 0.0);
        h.fHook_r.setPosition(a ? 0.0 : 1.0);
        a = !a;
        sleep(50);
    }
}
```

---

Next is `updateCrServos`. This turns the servos clockwise or counterclockwise depending on the side. It uses nested ternary operators, which could probably be a little more readable. However, the resulting operation is relatively simple so we have not had issues on that front.

```java
private void updateCrServos() {
    // Sets grabber positions
    h.grab_l.setPower(h._rTrigger != 0.0 ? 1.0 : (h.lTrigger != 0.0 ? -1.0 : 0.0));
    h.grab_r.setPower(h._rTrigger != 0.0 ? -1.0 : (h.lTrigger != 0.0 ? 1.0 : 0.0));
}
```

---

Now, `updateAuxMotors`. We have two auxiliary motor sets, one is to move the robots top platform forward and back, `h.lSlide_l` and `h.lSlide_r`, the other moves the scissor contraption up and down, `h.scissor`.

```java
private void updateAuxMotors() {
    // Sets scissor-lift's motor powers
    h.scissor.setPower(-h._lStick_y);
    h.lSlide_l.setPower(-h._rStick_y);
    h.lSlide_r.setPower(h._rStick_y);
}
```

---

The final one, `updateDrive`, is the most involved. It uses a helper function called `getDrivePower`. They are shown below.

```java
private void updateDrive() {
    h.drive_lf.setPower(getDrivePower(0));
    h.drive_rb.setPower(getDrivePower(1));
    h.drive_lb.setPower(getDrivePower(2));
    h.drive_rf.setPower(getDrivePower(3));
}
```

```java
private double getDrivePower(int motorID) {
    double power;
    double modL1 = 0.6;
    double modL2 = 0.3;
    switch (motorID) {
        case 0: power = h.lStick_y - h.lStick_x - h.rStick_x; break; // drive_lf
        case 1: power = h.lStick_y - h.lStick_x + h.rStick_x; break; // drive_rb
        case 2: power = h.lStick_y + h.lStick_x - h.rStick_x; break; // drive_lb
        case 3: power = h.lStick_y + h.lStick_x + h.rStick_x; break; // drive_rf
        default: power = 0; break;
    }
    power = power > 1 ? 1 : power;
    power *= h.lTrigger != 0 ? modL2 : (h.rTrigger != 0 ? modL1 : 1);
    return power;
}
```

The `mod*` variables are at the request of our driving team to include different levels of throttle, enabled by holding down a button. We include two levels (three if you include full speed &mdash; the default), `L1` runs at 60% and `L2` at 30%. As for the switch statement, the algorithm used to obtain the correct arrangement of `-` and `+` was pure trial and error. At first, we were sure there was a more elegant solution (note the line directly below the switch statement is necessary, as the value sometimes goes above 1) but were unable to find one, thus, trial and error it was.

### Hardware Class

The addition of a separate hardware class is definitively the biggest improvement to our code from the previous season so far. First we will lay out the initializations, including the constructor.

```java
public Hardware(Telemetry telemetry, LinearOpMode linearOpMode) {
    t = telemetry; opmode = linearOpMode;
}
LinearOpMode opmode;
Telemetry t;

// Initialize hardware
DcMotor drive_lf, drive_rb, drive_rf, drive_lb;
DcMotor scissor, lSlide_l, lSlide_r;
Servo fHook_l, fHook_r;
CRServo grab_l, grab_r;

// Initialize visual detection
OpenCvCamera phoneCam;
SkystoneDetector sDetect;

// Initialize gamepad values
double lStick_x, lStick_y, rStick_x, lTrigger, rTrigger; // Gamepad 1
boolean button_b;
double _lStick_y, _rStick_y; // Gamepad 2
```

A big reason we were able to make this, is because we finally began to understand how to interact with objects, hence the `Telemetry` and `LinearOpMode` objects passed via the constructor. In our first year, we never would have thought to do that. Below the constructor are the other global variables assigned to null values.

---

We include two separate initialization functions, `init` and `initAuto`.

```java
void init(HardwareMap hardwareMap) {
    // Defines drive motors
    drive_lf = hardwareMap.dcMotor.get("leftFront");
    drive_rb = hardwareMap.dcMotor.get("rightBack");
    drive_lb = hardwareMap.dcMotor.get("leftBack");
    drive_rf = hardwareMap.dcMotor.get("rightFront");
    
    // Drive motor setup
    drive_lf.setDirection(DcMotor.Direction.FORWARD);
    drive_rb.setDirection(DcMotor.Direction.REVERSE);
    drive_lb.setDirection(DcMotor.Direction.FORWARD);
    drive_rf.setDirection(DcMotor.Direction.REVERSE);

    // Defines scissor-lift hardware
    scissor = hardwareMap.dcMotor.get("scissor");
    lSlide_l = hardwareMap.dcMotor.get("slideL");
    lSlide_r = hardwareMap.dcMotor.get("slideR");
    fHook_l = hardwareMap.servo.get("hook1");
    fHook_r = hardwareMap.servo.get("hook2");
    grab_l = hardwareMap.crservo.get("block1");
    grab_r = hardwareMap.crservo.get("block2");
}
```

```java
void initAuto(HardwareMap hardwareMap) {
    WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    phoneCam = new OpenCvWebcam(webcamName, cameraMonitorViewId);
    phoneCam.openCameraDevice();
    sDetect = new SkystoneDetector();
    phoneCam.setPipeline(sDetect);

    phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
}
```

The `init` function includes basic configuration of our hardware devices. `initAuto` presents a good opportunity to credit where we got the majority of out vision related code from, DogeCV. While we do eventually move to using TensorFlow, DogeCV was our main vision library for all of last season and early this season. So, to be honest, `initAuto`, aside from straightforward commands like "startStreaming," was beyond us. None of us have ever used OpenCV or any kind of video stream processing, so our only hope was to rely on DogeCV. We never end up getting this vision detection method working reliably (problems with lighting and no clue how to fix it), so this is simply a mention of the fact that it existed, and we tried...

---

The rest of the hardware class consists of many telemetry function, which we will not go over here, and a function to update the gamepad values. `updateGamepad` is shown below.

```java
void updateGamepad(Gamepad gamepad1, Gamepad gamepad2) { // to be referenced externally
    lStick_x = gamepad1.left_stick_x;
    lStick_y = gamepad1.left_stick_y;
    rStick_x = gamepad1.right_stick_x;
    lTrigger = gamepad1.left_trigger;
    rTrigger = gamepad1.right_trigger;
    _lStick_y = gamepad2.left_stick_y;
    _rStick_y = gamepad2.right_stick_y;
    _rTrigger = gamepad2.right_trigger;
}
```

### Auto class

Our initial autonomous classes included just one instruction set and one helper class. First, we will go over the instruction set, `Auto`.

```java
@Autonomous(name="Auto") public class Auto extends LinearOpMode {
	// Initializations
	private Hardware h = new Hardware(telemetry, this);
	private AutoBase a = new AutoBase(h, this);

	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
		try {
			h.init(hardwareMap);
			h.initAuto(hardwareMap);
		} catch(Exception e) {
			h.tErr("HardwareMap", e);
			sleep(15_000);
			stop();
		}
		h.tStatus("Ready");
		waitForStart();
		try {
			// Instructions:
				// INSTRUCTIONS GO HERE

		// Catches exceptions as plain-text
		} catch(Exception e) {
			h.tErr("Auto Runtime", e);
			sleep(15_000);
			stop();
		}
	}
}
```

It is very similar to Drive's `runOpMode` function, just without the loop. Stripped down to code that's actually ran, it looks like this.

```java
@Autonomous(name="Auto") public class Auto extends LinearOpMode {
	// Initializations
	private Hardware h = new Hardware(telemetry, this);
	private AutoBase a = new AutoBase(h, this);

	// Runs when initialized
	@Override public void runOpMode() {
		// Initiate hardware
        h.init(hardwareMap);
        h.initAuto(hardwareMap);
        
		h.tStatus("Ready");
		waitForStart();
        
        // Instructions:
        	// INSTRUCTIONS GO HERE
	}
}
```

So yeah, pretty simple, the bulk ends up being the error handling. Sadly, we do not have that actual autonomous from this point in time. We wrote all the instructions mid-meet but neglected to save it. With just one autonomous class to work with, we had to switch between two sets anyways. They looked something like this.

```java
// Instructions:
a.movF_(2_500, 1)
a.movR_(2_000, 1)
// OR
a.movF_(2_500, 1)
a.movL_(2_000, 1)
```

We will explain what exactly `AutoBase` and `mov*_` are next.

### AutoBase Class

`AutoBase` is definitely the class we spend our most time in. It's purpose is to abstract out as many instructions as possible, this makes our autonomous instruction sets incredibly simple. This is important for in-the-field adjustments. First, we will go over the helper functions. We have many individual functions, but they fall into only a few templates. We will go over those.

The first ones are drive-specific helper functions. These are things like `driveTargetPos` which is simply shorthand for setting the position of all the drive motors.

```java
void driveTargetPos(double revlf, double revrf, double revlb, double revrb) {
    int mod = 28 * 20
    h.drive_lf.setTargetPosition((int)(revlf * mod));
    h.drive_rf.setTargetPosition((int)(revrf * mod));
    h.drive_lb.setTargetPosition((int)(revlb * mod));
    h.drive_rb.setTargetPosition((int)(revrb * mod));
}
```

We never actually get our motor's encoders working correctly for the first meet, so these functions are untested at this point in time. The purpose of the `* mod` is to convert the revolutions we give it to the ticks that the motors take as input. The other ones, also encoder related, are mode changing functions. So things like this.

```java
void driveModeSRE() {
    h.drive_lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    h.drive_rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    h.drive_lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    h.drive_rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
}
```

More shorthand.

```java
private boolean drive_isBusy() {
    return h.drive_lf.isBusy() || h.drive_rf.isBusy() || h.drive_lb.isBusy() || h.drive_rb.isBusy();
}
```

The last two have to do with time based movement, the method that we used at the meet. As with most, they are just shorthand for the drive motors. 

```java
void drivePower(double plf, double prf, double plb, double prb) {
    h.drive_lf.setPower(plf);
    h.drive_rf.setPower(prf);
    h.drive_lb.setPower(plb);
    h.drive_rb.setPower(prb);
}
```

```java
void halt(long t) {
    opmode.sleep(t);
    h.drive_lf.setPower(0);
    h.drive_rf.setPower(0);
    h.drive_lb.setPower(0);
    h.drive_rb.setPower(0);
}
```

---

Now onto the stuff we actually use in the instruction sets, that is, `mov*` and `mov*_`. `movF` and `movF_` are shown here.

```java
void movF(double rev, double p) {
    driveModeSRE();
    driveTargetPos(rev, rev, rev, rev);
    driveModeRTP();
    drivePower(p, p, p, p);
    while (drive_isBusy()) {
        h.tDrivePos();
        opmode.idle();
    }
    halt(0);
    driveModeRWE();
}
```

```java
void movF_(long t, double p) {
    drivePower(p, p, p, p);
    halt(t);
}
```

This set of functions would be considered a level of abstraction higher than the helper functions described earlier. This makes them rather straight forward, especially the time based ones, since those are already pretty simple without the extra abstraction. These elevate the autonomous to our highest level of abstraction and are the only functions currently used in `Auto`'s instruction sets. For an example of how we adjust these for different directions, here is `movL` and `movL_`.

```java
void movL(double rev, double p) {
    driveModeSRE();
    driveTargetPos(-rev, rev, rev, -rev); // Added (-)'s
    driveModeRTP();
    drivePower(p, p, p, p);
    while (drive_isBusy()) {
        h.tDrivePos();
        opmode.idle();
    }
    halt(0);
    driveModeRWE();
}
```

```java
void movL_(long t, double p) {
    drivePower(-p, p, p, -p); // Added (-)'s
    halt(t);
}
```

## League Meet Two
