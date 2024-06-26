package org.firstinspires.ftc.teamcode.drive.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class CenterstageBot {

    private LinearOpMode opmode;
    private DcMotorEx leftFront, rightFront, leftBack, rightBack;

    public CenterstageBot(HardwareMap hardwareMap, LinearOpMode opmode) {
        this.opmode = opmode;
        leftFront = hardwareMap.get(DcMotorEx.class, "frontLeft");
        rightFront = hardwareMap.get(DcMotorEx.class, "frontRight");
        leftBack = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightBack = hardwareMap.get(DcMotorEx.class, "backRight");

        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void mecanumDriving(double maxSpeed) {
        double drive = opmode.gamepad1.left_stick_y;
        double strafe = opmode.gamepad1.right_stick_x;
        double turn = opmode.gamepad1.left_stick_x;
        double v1, v2, v3, v4;

        if (opmode.gamepad1.right_bumper) {
            v1 = Range.clip(-drive + strafe + turn, -0.35, 0.35);
            v2 = Range.clip(-drive - strafe - turn, -0.35, 0.35);
            v3 = Range.clip(-drive + strafe - turn, -0.35, 0.35);
            v4 = Range.clip(-drive - strafe + turn, -0.35, 0.35);
        }

        else {
            v1 = Range.clip(-drive + strafe + turn, -maxSpeed, maxSpeed);
            v2 = Range.clip(-drive - strafe - turn, -maxSpeed, maxSpeed);
            v3 = Range.clip(-drive + strafe - turn, -maxSpeed, maxSpeed);
            v4 = Range.clip(-drive - strafe + turn, -maxSpeed, maxSpeed);
        }

        leftFront.setPower(v1);
        rightFront.setPower(v2);
        leftBack.setPower(v3);
        rightBack.setPower(-v4);
    }
}
