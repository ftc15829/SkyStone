package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class AutoBase {
/*Initializations*/
    AutoBase(Hardware hardware, LinearOpMode linearOpMode) { h = hardware; opmode = linearOpMode; }
    Hardware h;
    LinearOpMode opmode;

/*Actions*/
    void findSkystone(AutoBase.SepThreadMov sepThreadMov, int dir) {
        double p = 1;
        h.startStream();
        opmode.sleep(800); // Time for stream to start up
        Thread t = new Thread(sepThreadMov);
        sepThreadMov.dir = dir; // 1 -> right, 3 -> left
        sepThreadMov.p = p;
        sepThreadMov.isRunning = true;
        t.run();
        while(h.sDetect.foundRectangle().area() < 7000 || ( h.sDetect.getScreenPosition().x < 79 || h.sDetect.getScreenPosition().x > 82 )) {
            h.tCaminfo();
            opmode.idle();
        }
        sepThreadMov.isRunning = false;
        movF(2.0, 1);
    }

    void pickUp() {
        platform(10, 0.7);
        h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), 1);
        h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), -1);
        platform(-10, 0.7);
    }
    void drop() {
        platform(10, 0.7);
        h.grab_l.getController().setServoPosition(h.grab_l.getPortNumber(), -1);
        h.grab_r.getController().setServoPosition(h.grab_r.getPortNumber(), 1);
        platform(-10, 0.7);
    }

/*Helper*/
    // Drive Specific
    void driveTargetPos(double revlf, double revrf, double revlb, double revrb) {
        h.drive_lf.setTargetPosition((int)(revlf * 28 * 20));
        h.drive_rf.setTargetPosition((int)(revrf * 28 * 20));
        h.drive_lb.setTargetPosition((int)(revlb * 28 * 20));
        h.drive_rb.setTargetPosition((int)(revrb * 28 * 20));
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
    void drivePower(double plf, double prf, double plb, double prb) {
        h.drive_lf.setPower(plf); h.drive_rf.setPower(prf);
        h.drive_lb.setPower(plb); h.drive_rb.setPower(prb);
    }
    void drivePower(double p) {
        h.drive_lf.setPower(p); h.drive_rf.setPower(p);
        h.drive_lb.setPower(p); h.drive_rb.setPower(p);
    }
    private boolean drive_isBusy() {
        return h.drive_lf.isBusy() || h.drive_rf.isBusy() || h.drive_lb.isBusy() || h.drive_rb.isBusy();
    }
    // General
    void modeSRE(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    void modeRTP(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    void targetPos(DcMotor motor, double rev) {
        motor.setTargetPosition((int)(rev * 28));
    }
    void power(DcMotor motor, double p) {
        motor.setPower(p);
    }

/*Auxilary Movement*/
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

/*Encoder Based Movement*/
    void movF(double rev, double p) {
        driveModeSRE();
        driveTargetPos(rev, rev, rev, rev);
        driveModeRTP();
        drivePower(p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void movL(double rev, double p) {
        driveModeSRE();
        driveTargetPos(-rev, rev, rev, -rev);
        driveModeRTP();
        drivePower(p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void movR(double rev, double p) {
        driveModeSRE();
        driveTargetPos(rev, -rev, -rev, rev);
        driveModeRTP();
        drivePower(p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void movB(double rev, double p) {
        driveModeSRE();
        driveTargetPos(-rev, -rev, -rev, -rev);
        driveModeRTP();
        drivePower(p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void trnL(double rev, double p) {
        double mod = 5.0;
        driveModeSRE();
        driveTargetPos(-rev*mod, rev*mod, -rev*mod, rev*mod);
        driveModeRTP();
        drivePower(p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }
    void trnR(int rev, double p) {
        double mod = 5.0;
        driveModeSRE();
        driveTargetPos(rev*mod, -rev*mod, rev*mod, -rev*mod);
        driveModeRTP();
        drivePower(p);
        while (drive_isBusy()) {
            h.tDrivePos();
            opmode.idle();
        }
        halt(0);
    }

/*Time Based Movement*/
    void movF(long t, double p) {
        drivePower(p, p, p, p); halt(t); }
    void movL(long t, double p) {
        drivePower(-p, p, p, -p); halt(t); }
    void movR(long t, double p) {
        drivePower(p, -p, -p, p); halt(t); }
    void movB(long t, double p) {
        drivePower(-p, -p, -p, -p); halt(t); }
    void trnL(long t, double p) {
        drivePower(-p, p, -p, p); halt(t); }
    void trnR(long t, double p) {
        drivePower(p, -p, p, -p); halt(t); }
    void halt(long t) {
        opmode.sleep(t);
        h.drive_lf.setPower(0); h.drive_rf.setPower(0);
        h.drive_lb.setPower(0); h.drive_rb.setPower(0);
    }

/*Utility Classes*/
    public class AutoTelemetry implements Runnable { // Depreciated
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
        boolean isRunning = false;
        @Override public void run() {
            while(isRunning) {
                switch(dir) {
                    case 0: // Forward
                        drivePower(p);
                        break;
                    case 1: // Right
                        drivePower(p, -p, -p, p);
                        break;
                    case 2: // Backward
                        drivePower(-p);
                        break;
                    case 3: // Left
                        drivePower(-p, p, p, -p);
                        break;
                    default:
                        break;
                }
                opmode.idle();
            }
        }
    }
}
