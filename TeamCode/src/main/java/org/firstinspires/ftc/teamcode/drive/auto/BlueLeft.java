package org.firstinspires.ftc.teamcode.drive.auto;

import android.os.FileUriExposedException;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.teamcode.Vision.EasyOpenCVVision;
import org.firstinspires.ftc.teamcode.Vision.dataFromOpenCV;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.Sensors.SensorDistance;
import org.firstinspires.ftc.teamcode.drive.auto.Webcam;
import org.firstinspires.ftc.teamcode.drive.teleop.FourBar;
import org.firstinspires.ftc.teamcode.drive.teleop.Slides;
import org.firstinspires.ftc.teamcode.drive.teleop.Spintake;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class BlueLeft extends LinearOpMode {

    public void runOpMode() throws InterruptedException{
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Slides slides = new Slides(hardwareMap, this);
        FourBar fourBar = new FourBar(hardwareMap, this);
        Webcam webcam1 = new Webcam(hardwareMap, "Webcam 1");
        //Webcam webcam2 = new Webcam(hardwareMap, "Webcam 2");

        webcam1.setPipeline(new EasyOpenCVVision());
        //webcam2.setPipeline(new EasyOpenCVVision());

        // FtcDashboard dashboard = FtcDashboard.getInstance();
        // telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        // FtcDashboard.getInstance().startCameraStream(webcam1.getWebcam(), 30);
        // FtcDashboard.getInstance().startCameraStream(webcam2.getWebcam(), 30);

        while (opModeInInit()) {
            telemetry.addData("avg1B:", dataFromOpenCV.AVG1B);
            telemetry.addData("avg2B:", dataFromOpenCV.AVG2B);
            telemetry.addData("avg3B:", dataFromOpenCV.AVG3B);
            telemetry.update();

            drive.setMotorPowers(0, 0, 0, 0);
        }

        int pos = 0;

        if (dataFromOpenCV.AVG1B > dataFromOpenCV.AVG2B && dataFromOpenCV.AVG1B > dataFromOpenCV.AVG3B) {
            pos = 1;
        }
        if (dataFromOpenCV.AVG2B > dataFromOpenCV.AVG1B && dataFromOpenCV.AVG2B > dataFromOpenCV.AVG3B) {
            pos = 2;
        }
        if (dataFromOpenCV.AVG3B > dataFromOpenCV.AVG1B && dataFromOpenCV.AVG3B > dataFromOpenCV.AVG2B) {
            pos = 3;
        }

        //Creating Autonomous trajectory

        Pose2d startPose = new Pose2d(0, 0, 0);

        drive.setPoseEstimate(startPose);

        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .lineToLinearHeading(new Pose2d(27, 0, Math.toRadians(0)))
                .build();

        Trajectory traj2A = drive.trajectoryBuilder(traj1.end())
                .lineToLinearHeading(new Pose2d(37, -10, Math.toRadians(90)))
                .build();

        Trajectory traj2B = drive.trajectoryBuilder(traj1.end())
                .lineToLinearHeading(new Pose2d(33, 7, Math.toRadians(-90)))
                .build();

        Pose2d traj2End = traj1.end();
        if (pos == 1)
            traj2End = traj2A.end();
        if (pos == 3)
            traj2End = traj2B.end();

        Trajectory traj2Yellow = drive.trajectoryBuilder(traj2End)
                .back(3)
                //.back(68)
                .build();

        Trajectory trajDepositYellow = drive.trajectoryBuilder(traj2Yellow.end().minus(new Pose2d(0, 0, Math.toRadians(90))))
                .lineToConstantHeading(new Vector2d(35, 40))
                .build();

        Trajectory traj3Yellow = drive.trajectoryBuilder(traj2End)
                .back(3)
                //.splineTo(new Vector2d(33, 4), Math.toRadians(180))
                //.back(68)
                //.splineTo(new Vector2d(33, 68), Math.toRadians(90))
                //.strafeTo(new Vector2d(23, 68))
                .build();

        Trajectory trajLeftDetected = drive.trajectoryBuilder(traj2End)
                .strafeRight(30)
                .build();

        Trajectory trajRightDetected = drive.trajectoryBuilder(traj2End)
                .strafeLeft(30)
                .build();

        Trajectory traj5 = drive.trajectoryBuilder(traj2End)
                .back(68)
                .build();

        Trajectory traj3 = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(5, 0, Math.toRadians(0)))
                .build();

        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                        .lineToLinearHeading(new Pose2d(38, 80, Math.toRadians(0)))
                                .build();

        Trajectory trajPark1 = drive.trajectoryBuilder(trajLeftDetected.end())
                .forward(84)
                .build();

        Trajectory traj2detected = drive.trajectoryBuilder(trajLeftDetected.end())
                .forward(30)
                .build();

        Trajectory traj2Park = drive.trajectoryBuilder(traj2detected.end())
                .lineToLinearHeading(new Pose2d(58, 87, Math.toRadians(95)))
                .build();

        Trajectory traj3Park = drive.trajectoryBuilder(trajRightDetected.end())
                .back(90)
                .build();

        Trajectory trajPark = drive.trajectoryBuilder(traj1.end())
                .lineToLinearHeading(new Pose2d(48, 0, Math.toRadians(90)))
                .build();
        Trajectory trajPark2 = drive.trajectoryBuilder(trajPark.end())
                .forward(92)
                .build();

        Pose2d trajIntakepos = traj1.end();
        if (pos == 1)
            trajIntakepos = traj5.end();
        if (pos == 3)
            trajIntakepos = traj3Yellow.end();

        Trajectory trajIntake = drive.trajectoryBuilder(traj1.end())
                .splineTo(new Vector2d(3, 24), Math.toRadians(-90))
                //.forward(24)
                //.splineTo(new Vector2d(2, 0), Math.toRadians(90))
                //.strafeTo(new Vector2d(35, -20))
                .build();

        Trajectory trajDeposit = drive.trajectoryBuilder(trajIntake.end())
                .back(3)
                //.splineTo(new Vector2d(50,20), Math.toRadians(90))
                //.strafeTo(new Vector2d(24, 68))
                .build();

        waitForStart();


        drive.followTrajectory(traj1);
/*
        if (pos == 1) {
            drive.followTrajectory(traj2A);
            drive.followTrajectory(traj5);
        }

        if (pos == 3) {
            drive.followTrajectory(traj2B);
            drive.followTrajectory(traj3Yellow);
        }

*/
        if (pos == 2){
            drive.followTrajectory(traj2Yellow);
            drive.turn(Math.toRadians(-93));
            drive.followTrajectory(trajDepositYellow);
            sleep(200);
        }

        //drive.followTrajectory(trajIntake);
        //drive.followTrajectory(trajDeposit);

        webcam1.getWebcam().stopStreaming();
    }

    private Object dataFromOpenCV() {
        return dataFromOpenCV();
    }
}
