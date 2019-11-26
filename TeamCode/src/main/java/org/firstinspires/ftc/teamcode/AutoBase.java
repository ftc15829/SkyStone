package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.openftc.easyopencv.OpenCvCameraRotation;
import static java.lang.Thread.sleep;

public class AutoBase {
    // INITIALIZATIONS
    AutoBase(Hardware hardware, LinearOpMode linearOpMode) { h = hardware; opmode = linearOpMode; }
    Hardware h;
    LinearOpMode opmode;

    // ACTIONS
    void pickUp(AutoBase.SepThreadMov sepThreadMov) {
        // Align to Skystone
        int screenWidth = 320;
        int screenHeight = 240;
        double p = 1;
        h.phoneCam.startStreaming(screenWidth, screenHeight, OpenCvCameraRotation.UPRIGHT);
        Thread t = new Thread(sepThreadMov);
        while (h.sDetect.getScreenPosition().x > screenWidth / 2 + 50 || h.sDetect.getScreenPosition().x < screenWidth / 2 - 50) {
            if (h.sDetect.getScreenPosition().x > screenWidth / 2 + 50) {
                sepThreadMov.dir = 0;
                sepThreadMov.p = p;
                t.run();
            } else if (h.sDetect.getScreenPosition().x < screenWidth / 2 - 50) {
                sepThreadMov.dir = 1;
                sepThreadMov.p = p;
                t.run();
            }
        }
        t.stop();

        // Extend Scissor
        platform(1, 0.2);

        // Grab
        h.grab_l.setPower(h._rTrigger != 0.0 ? 1.0 : (h._lTrigger != 0.0 ? -1.0 : 0.0));
        h.grab_r.setPower(h._rTrigger != 0.0 ? -1.0 : (h._lTrigger != 0.0 ? 1.0 : 0.0));
        // Retract Scissor
        platform(-1, -0.2);
    }
    void drop() {
        // Fix
    }
    // General Helper
    void modeSRE(DcMotor motor) { motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); }
    void modeRTP(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); }
    void targetPos(DcMotor motor, double rev) { motor.setTargetPosition((int)(rev * 28)); }
    void power(DcMotor motor, double p) { motor.setPower(p); }

    void platform(double rev, double p) {
        modeSRE(h.lSlide_l); modeSRE(h.lSlide_r);
        targetPos(h.lSlide_l, rev); targetPos(h.lSlide_r, rev);
        modeRTP(h.lSlide_l); modeRTP(h.lSlide_r);
        power(h.lSlide_l, p); power(h.lSlide_r, p);
        while (h.lSlide_l.isBusy() && h.lSlide_r.isBusy()) {
            h.tPos(h.lSlide_l); h.tPos(h.lSlide_r);
            opmode.idle();
        }
        power(h.lSlide_l, 0); power(h.lSlide_r, 0);
    }
 /*
 * CORE
*/
    // CORE ENCODER MOVEMENT
    void movF(int rev, double p) {
        driveModeSRE();
        driveTargetPos(rev, rev, rev, rev);
        driveModeRTP();
        drivePower(p, p, p, p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void movL(int rev, double p) {
        driveModeSRE();
        driveTargetPos(rev, rev, rev, rev); // Fix
        driveModeRTP();
        drivePower(p, p, p, p); // Fix
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void movR(int rev, double p) {
        driveModeSRE();
        driveTargetPos(rev, rev, rev, rev); // Fix
        driveModeRTP();
        drivePower(p, p, p, p); // Fix
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void movB(int rev, double p) {
        driveModeSRE();
        driveTargetPos(rev, rev, rev, rev); // Fix
        driveModeRTP();
        drivePower(p, p, p, p); // Fix
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void trnL() {
        // Fix
    }
    void trnR() {
        // Fix
    }
    // ENCODER MOVEMENT HELPER
    void driveTargetPos(double revlf, double revrf, double revlb, double revrb) {
        h.drive_lf.setTargetPosition((int)(revlf * 28));
        h.drive_rf.setTargetPosition((int)(revrf * 28));
        h.drive_lb.setTargetPosition((int)(revlb * 28));
        h.drive_rb.setTargetPosition((int)(revrb * 28));
    }
    void driveModeRTP() {
        h.drive_lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.drive_rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.drive_lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.drive_rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    void driveModeSRE() {
        h.drive_lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.drive_rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.drive_lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.drive_rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    private boolean drive_isBusy() {
        return h.drive_lf.isBusy() || h.drive_rf.isBusy() || h.drive_lb.isBusy() || h.drive_rb.isBusy();
    }

    // CORE TIME MOVEMENT
    void movF_(long t, double p) {
        drivePower(p, p, p, p);
        halt(t);
    }
    void movL_(long t, double p) {
        drivePower(-p, p, p, -p);
        halt(t);
    }
    void movR_(long t, double p) {
        drivePower(p, -p, -p, p);
        halt(t);
    }
    void movB_(long t, double p) {
        drivePower(-p, -p, -p, -p);
        halt(t);
    }
    void trnL_(long t, double p) {
        drivePower(-p, p, -p, p);
        halt(t);
    }
    void trnR_(long t, double p) {
        drivePower(p, -p, p, -p);
        halt(t);
    }
    // TIME MOVEMENT HELPER
    void drivePower(double plf, double prf, double plb, double prb) {
        h.drive_lf.setPower(plf);
        h.drive_rf.setPower(prf);
        h.drive_lb.setPower(plb);
        h.drive_rb.setPower(prb);
    }
    void halt(long t) {
        opmode.sleep(t);
        h.drive_lf.setPower(0);
        h.drive_rf.setPower(0);
        h.drive_lb.setPower(0);
        h.drive_rb.setPower(0);
    }

    // UTILITY
    public class AutoTelemetry implements Runnable {
        public boolean sDetector_xy = false;

        @Override public void run() {
            while (!opmode.isStopRequested()) {
                if (sDetector_xy) h.tWebcam();
                opmode.idle();
            }
        }
    }
    public class SepThreadMov implements Runnable {
        double p;
        int dir;
        @Override public void run() {
            if (dir == 0) {
                drivePower(-p, p, p, -p);
            } else {
                drivePower(p, -p, -p, p);
            }
        }
    }
}
