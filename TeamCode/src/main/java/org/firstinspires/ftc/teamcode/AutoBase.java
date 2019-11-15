package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import static java.lang.Thread.sleep;

public class AutoBase {
    // INITIALIZATIONS
    public AutoBase(Hardware hardware, LinearOpMode linearOpMode) { h = hardware; opmode = linearOpMode; }
    Hardware h;
    LinearOpMode opmode;

    // ACTIONS
    void pickUp() {
        // Fix
    }
    void drop() {
        // Fix
    }
    // General Helper
    void modeSRE(DcMotor motor) { motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); }
    void modeRTP(DcMotor motor) { motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); }
    void targetPos(DcMotor motor, double rev) { motor.setTargetPosition((int)(rev * 28)); }
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
        driveModeSRE();
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
        driveModeSRE();
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
        driveModeSRE();
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
        driveModeSRE();
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
}
