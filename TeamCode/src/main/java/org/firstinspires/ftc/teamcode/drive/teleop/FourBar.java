package org.firstinspires.ftc.teamcode.drive.teleop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FourBar {
    private CRServo fourbarLeft;
    private CRServo fourBarRight;
    private LinearOpMode opmode;
    private final double START_POS = 0.95;
    private final double END_POS = 0;
    public FourBar(HardwareMap hardwareMap, LinearOpMode opmode) {
        fourbarLeft = hardwareMap.get(CRServo.class, "fourBarLeft");
        fourBarRight = hardwareMap.get(CRServo.class, "fourBarRight");
        this.opmode = opmode;
    }
    public void resetPos(){
        fourbarLeft.setPower(-0.25);
        fourBarRight.setPower(-0.25);
    }
    public void rotate() {
        fourbarLeft.setPower(1.0);
        fourBarRight.setPower(1.0);
    }

}
