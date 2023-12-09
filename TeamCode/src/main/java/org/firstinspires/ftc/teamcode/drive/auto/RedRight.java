package org.firstinspires.ftc.teamcode.drive.auto;

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
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.Sensors.SensorDistance;
import org.firstinspires.ftc.teamcode.drive.teleop.FourBar;
import org.firstinspires.ftc.teamcode.drive.teleop.Slides;
import org.firstinspires.ftc.teamcode.drive.teleop.Spintake;
import org.firstinspires.ftc.teamcode.drive.teleop.TeleOp_Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous
public class RedRight extends LinearOpMode {
    static final double FEET_PER_METER = 3.28084;

    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag IDs from the 36h11 family
    int LEFT = 0;
    int MIDDLE = 1;
    int RIGHT = 2;

    AprilTagDetection tagOfInterest = null;

    public void runOpMode() throws InterruptedException{
        TeleOp_Drive teleOp_drive = new TeleOp_Drive();
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Spintake spintake = new Spintake(hardwareMap, this);
        SensorDistance sensorDistance = new SensorDistance(hardwareMap, this);
        Slides slides = new Slides(hardwareMap, this);
        FourBar fourBar = new FourBar(hardwareMap, this);
        Webcam webcam1 = new Webcam(hardwareMap, "Webcam 1");
        AprilTagDetectionPipeline aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        //Webcam webcam2 = new Webcam(hardwareMap, "Webcam 2");

        webcam1.setPipeline(new EasyOpenCVVision());
        //webcam2.setPipeline(new EasyOpenCVVision());

        // FtcDashboard dashboard = FtcDashboard.getInstance();
        // telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        // FtcDashboard.getInstance().startCameraStream(webcam1.getWebcam(), 30);
        // FtcDashboard.getInstance().startCameraStream(webcam2.getWebcam(), 30);

        while (opModeInInit()) {
            {
                telemetry.addData("avg1R:", dataFromOpenCV.AVG1R);
                telemetry.addData("avg2R:", dataFromOpenCV.AVG2R);
                telemetry.addData("avg3R:", dataFromOpenCV.AVG3R);
                telemetry.update();

                /* ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

                if(currentDetections.size() != 0)
                {
                    boolean tagFound = false;

                    for(AprilTagDetection tag : currentDetections)
                    {
                        if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                        {
                            tagOfInterest = tag;
                            tagFound = true;
                            break;
                        }
                    }

                    if (tagFound)
                    {
                        telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                        tagToTelemetry(tagOfInterest);
                    }
                    else
                    {
                        telemetry.addLine("Don't see tag of interest :(");

                        if(tagOfInterest == null)
                        {
                            telemetry.addLine("(The tag has never been seen)");
                        }
                        else
                        {
                            telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                            tagToTelemetry(tagOfInterest);
                        }
                    }

                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }

                }

                telemetry.update();
                sleep(20);
                 */

                slides.resetSlides();
                fourBar.closeClaw();
            }
        }

        /* if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }
         */

        int pos = 3;

        if (dataFromOpenCV.AVG1R > dataFromOpenCV.AVG2R && dataFromOpenCV.AVG1R > dataFromOpenCV.AVG3R)
            pos = 1;
        if (dataFromOpenCV.AVG2R > dataFromOpenCV.AVG1R && dataFromOpenCV.AVG2R > dataFromOpenCV.AVG3R)
            pos = 2;
        if (dataFromOpenCV.AVG3R > dataFromOpenCV.AVG1R && dataFromOpenCV.AVG3R > dataFromOpenCV.AVG2R)
            pos = 3;

        // webcam1.getWebcam().setPipeline(aprilTagDetectionPipeline);
        //Creating Autonomous trajectory

        Pose2d startPose = new Pose2d(0, 0, 0);

        drive.setPoseEstimate(startPose);

        Trajectory traj1 = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(30, 0, Math.toRadians(0)))
                .build();

        Trajectory traj1B = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(26, 0, Math.toRadians(0)))
                .build();

        Trajectory traj2A = drive.trajectoryBuilder(traj1.end())
                .lineToLinearHeading(new Pose2d(40, -8, Math.toRadians(90)))
                .build();

        Trajectory traj2B = drive.trajectoryBuilder(traj1.end())
                .lineToLinearHeading(new Pose2d(40, 8, Math.toRadians(-90)))
                .build();

        Trajectory backwards = drive.trajectoryBuilder(traj2B.end())
                .lineToLinearHeading(new Pose2d(35, 5, Math.toRadians(-90)))
                .build();

        Trajectory backwardsB = drive.trajectoryBuilder(traj1B.end())
                .lineToLinearHeading(new Pose2d(20, 0, Math.toRadians(0)))
                .build();

        Pose2d traj2End = traj1.end();
        if (pos == 1)
            traj2End = traj2A.end();
        if (pos == 3)
            traj2End = backwards.end();

        Trajectory traj3 = drive.trajectoryBuilder(traj2End)
                .lineToLinearHeading(new Pose2d(32, -5, Math.toRadians(91)))
                .build();

        TrajectorySequence traj3C = drive.trajectorySequenceBuilder(backwards.end())
                .lineToLinearHeading(new Pose2d(24, 5, Math.toRadians(91)))
                //.turn(Math.toRadians(180))
                .build();

        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                .lineToLinearHeading(new Pose2d(35, -40, Math.toRadians(91)))
                .build();

        Trajectory traj4C = drive.trajectoryBuilder(traj3C.end())
                .lineToLinearHeading(new Pose2d(20, -35, Math.toRadians(91)))
                .build();

        Trajectory traj5A = drive.trajectoryBuilder(traj4.end())
                .lineToLinearHeading(new Pose2d(38, -42, Math.toRadians(91)),
                        SampleMecanumDrive.getVelocityConstraint(0.5 * DriveConstants.MAX_VEL, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory traj5B = drive.trajectoryBuilder(traj4.end())
                .lineToLinearHeading(new Pose2d(36, -42, Math.toRadians(91)),
                        SampleMecanumDrive.getVelocityConstraint(0.25 * DriveConstants.MAX_VEL, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory traj5C = drive.trajectoryBuilder(traj4.end())
                .lineToLinearHeading(new Pose2d(31, -42, Math.toRadians(91)),
                        SampleMecanumDrive.getVelocityConstraint(0.8 * DriveConstants.MAX_VEL, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        waitForStart();

        if (pos == 1 || pos == 3)
            drive.followTrajectory(traj1);
        else
            drive.followTrajectory(traj1B);

        if (pos == 1)
            drive.followTrajectory(traj2A);
        if (pos == 3)
            drive.followTrajectory(traj2B);

        spintake.outtake(500);
        spintake.stop();
        sleep(200);
        if (pos == 2)
            drive.followTrajectory(backwardsB);
        /*if (pos == 3)
            drive.followTrajectory(backwards);*/
        sleep(400);

        if (pos == 1 || pos == 2) {
            drive.followTrajectory(traj3);
            drive.followTrajectory(traj4);
        }
        else {
            drive.followTrajectorySequence(traj3C);
            drive.followTrajectory(traj4C);
        }
        sleep(1000);

        fourBar.closeClaw();
        sleep(750);
        fourBar.raiseFourBar();
        sleep(500);
        slides.moveSlidesToHeightABS(1250, 0.7);
        sleep(300);
        fourBar.lowerFourBar();
        sleep(1000);
        // slides.moveSlidesToHeightABS(180, 0.4);

        if (pos == 1)
            drive.followTrajectory(traj5A);
        if (pos == 2)
            drive.followTrajectory(traj5B);
        if (pos == 3)
            drive.followTrajectory(traj5C);

        fourBar.openClaw();

        webcam1.getWebcam().stopStreaming();
    }

    private Object dataFromOpenCV() {
        return dataFromOpenCV();
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        /* telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.ftcPose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.ftcPose.roll)));
         */
    }
}
