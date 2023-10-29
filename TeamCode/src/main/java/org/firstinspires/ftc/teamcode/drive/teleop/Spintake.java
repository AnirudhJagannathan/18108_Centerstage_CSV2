package org.firstinspires.ftc.teamcode.drive.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class Spintake {

    private DcMotorEx spintake;
    private LinearOpMode opmode;
    private boolean on;
    private final double POWER = 0.3;

    public Spintake(HardwareMap hardwareMap, LinearOpMode opmode) {
        spintake = hardwareMap.get(DcMotorEx.class, "spintake");
        this.opmode = opmode;
        on = false;
    }


    public void spin() {
        if (opmode.gamepad2.a) {
            spintake.setPower(POWER);
            // on = true;
        }
        else if (opmode.gamepad2.x) {
            spintake.setPower(0);
            // on = false;
        }
    }

    public void outtake() {
        if (opmode.gamepad2.right_bumper)
            spintake.setPower(-POWER);
    }
}
