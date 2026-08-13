/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.text.DecimalFormat;
import java.util.List;

@TeleOp(name="PB_dev2_2_debug", group="Non-Pedro")
//@Disabled
public class PB_dev2_2_debug extends LinearOpMode { // This is the main teleop, but WITH telemetry. Other teleop doesn't have telemetry, in order to (in theory) improve code loop speed.

    // Flicks launcher servo up and down
    private void flickLauncher() { // Be sure to reset the launchTime variable when this is run
        if (flickTimer.milliseconds() > 300){
            launcherPos = 0.75;
            flicking = false;
        } else launcherPos = 0.55;
    }
    // Launch sequence
    private void launchScript() { // Be sure to reset the launchTime variable when this is run
        if (launchTime.seconds() > 4.20) {
            curTargetVelocity = 0;
            intakePower = 0;
            launching = false;
        } else if (launchTime.seconds() > 4) {
            flicking = true;
            flickTimer.reset();
        } else if (launchTime.seconds() > 2.25) {
            intakePower = 0.67;
        } else if (launchTime.seconds() > 2.15) {
            intakePower = 0.67;
            flicking = true;
            flickTimer.reset();
        } else if (launchTime.seconds() > 0.80) {
            intakePower = 0;
        } else if (launchTime.seconds() > 0.75) {
            flicking = true;
            flickTimer.reset();
        } else if (launchTime.seconds() > 0) {
            curTargetVelocity = lowVelocity;
        }
    }

    // Main AprilTag Code
   private void aprilTagInfo() {
       List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
       // Creates a list of detected tag IDs and assigns a integer variable to the detected tag ID
       idsFound = new StringBuilder();
       for (AprilTagDetection detection : currentDetections) {
           idsFound.append(detection.id);
           idsFound.append(" ");
           currentID = detection.id;
       }

       if (!(" ".contentEquals(idsFound) || idsFound.length() == 0)) {
           tagDetected = true;

           // Assigns AprilTag readings to double variables, bearing is the only value that is currently used
           for (AprilTagDetection detection : currentDetections) {
//               tagValueX = Double.parseDouble(df2.format(detection.ftcPose.x));
//               tagValueY = Double.parseDouble(df2.format(detection.ftcPose.y));
//               tagValueZ = Double.parseDouble(df2.format(detection.ftcPose.z));
//               tagValuePitch = Double.parseDouble(df2.format(detection.ftcPose.pitch));
//               tagValueRoll = Double.parseDouble(df2.format(detection.ftcPose.roll));
//               tagValueYaw = Double.parseDouble(df2.format(detection.ftcPose.yaw));
//               tagValueRange = Double.parseDouble(df2.format(detection.ftcPose.range));
               tagValueBearing = Double.parseDouble(df2.format(detection.ftcPose.bearing));
//               tagValueElevation = Double.parseDouble(df2.format(detection.ftcPose.elevation));
           }
       } else {
           tagDetected = false;
       }

   }


 // When run, will angle robot toward the detected AprilTag (in theory)
    private void aprilTagBearingLock() {

       if (tagDetected) {
           LED2.off();
           LED3.on();

           if (currentID == 24) {
               if (tagValueBearing < -14) {
                   yaw = -0.4;
                   strlockstatus = "Correcting Left";
               } else if (tagValueBearing > 10) {
                   yaw = 0.4;
                   strlockstatus = "Correcting Right";
               } else if (tagValueBearing < -3){
                   yaw = -0.2;
                   strlockstatus = "Correcting Left";
               }
               else if (tagValueBearing > -1) {
                   yaw = 0.2;
                   strlockstatus = "Correcting Right";
               } else {
                   yaw = 0;
                   strlockstatus = "In Lock Red Side";
               }
           } else if (currentID  == 20) {
               if (tagValueBearing < -10) {
                   yaw = -0.4;
                   strlockstatus = "Correcting Left";
               } else if (tagValueBearing > 14) {
                   yaw = 0.4;
                   strlockstatus = "Correcting Right";
               } else if (tagValueBearing < 1){
                   yaw = -0.2;
                   strlockstatus = "Correcting Left";
               }
               else if (tagValueBearing > 3) {
                   yaw = 0.2;
                   strlockstatus = "Correcting Right";
               } else {
                   yaw = 0;
                   strlockstatus = "In Lock Blue Side";
               }
           } else {
               if (tagValueBearing < -12) {
                   yaw = -0.4;
                   strlockstatus = "Correcting Left";
               } else if (tagValueBearing > 12) {
                   yaw = 0.4;
                   strlockstatus = "Correcting Right";
               } else if (tagValueBearing < -1){
                   yaw = -0.2;
                   strlockstatus = "Correcting Left";
               }
               else if (tagValueBearing > 1) {
                   yaw = 0.2;
                   strlockstatus = "Correcting Right";
               } else {
                   yaw = 0;
                   strlockstatus = "In Lock";
                   //lockToTag = false;
               }
           }

       } else {
           strlockstatus = "No Tag";
           LED2.on();
           LED3.off();
       }



    }


    // Declare Elapsed Time variables
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime launchTime = new ElapsedTime();
    private ElapsedTime flickTimer = new ElapsedTime();
    private ElapsedTime loadTimer = new ElapsedTime();
    // Declare OpMode members for each of the 4 motors.
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    // Declare drive input variables
    double axial = 0;
    double lateral = 0;
    double yaw = 0;
    // RBT + Ramp declaration
    private DcMotor RBT = null;
    private CRServo ramp11 = null;
    private CRServo ramp12 = null;
    // Flywheel motor declaration
    private DcMotorEx flywheel = null;
    // Flywheel PIDF variables
    double curTargetVelocity = 0;
    final double P = 170;
    final double F = 12.99;
    final double lowVelocity = 1700;
    final double highVelocity = 2000;
    // Launcher servo declaration
    private Servo launcher = null;

    // Color sensor
    private ColorSensor color = null;
    private ColorSensor intake_color = null;

    // Range sensor declaration, currently unused
    //private Rev2mDistanceSensor rangesensor = null;

    // Declarations for AprilTag code, currently unused
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    // AprilTag info strings + integers
    private StringBuilder idsFound;
    int currentID;
    private StringBuilder tagInfoXYZ;
    private String[] tagArrayXYZ = {"0"};
    double tagValueX = 0;
    double tagValueY = 0;
    double tagValueZ = 0;
    private StringBuilder tagInfoPRY;
    private String[] tagArrayPRY = {"0"};
    double tagValuePitch = 0;
    double tagValueRoll = 0;
    double tagValueYaw = 0;
    private StringBuilder tagInfoRBE;
    private String[] tagArrayRBE = {"0"};
    double tagValueRange = 0;
    double tagValueBearing = 0;
    double tagValueElevation = 0;
    private DecimalFormat df2 = new DecimalFormat("0.00");
    boolean tagDetected = false;
    boolean lockToTag = false;

    // Status string initiations
    private String strintakestatus = "Off";
    private String strlockstatus = "Off";
    // LED declarations
    private LED LED0 = null;
    private LED LED1 = null;
    private LED LED2 = null;
    private LED LED3 = null;
    private LED LED4 = null;
    private LED LED5 = null;
    private LED LED6 = null;
    private LED LED7 = null;

    // Misc
    boolean intakeWasAutoOff = false;
    boolean wasArtifactInIntake = false;
    boolean launching = false;
    boolean flicking = false;
    double intakePower = 0;
    double launcherPos = 0.75;




    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        // Drive motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "leftfront");
        backLeftDrive = hardwareMap.get(DcMotor.class, "leftrear");
        frontRightDrive = hardwareMap.get(DcMotor.class, "rightfront");
        backRightDrive = hardwareMap.get(DcMotor.class, "rightrear");
        // Intake motor and servos
        RBT = hardwareMap.get(DcMotor.class, "RBT");
        ramp11 = hardwareMap.get(CRServo.class, "ramp11");
        ramp12 = hardwareMap.get(CRServo.class, "ramp12");
        // Flywheel motor
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        // Launcher servo
        launcher = hardwareMap.get(Servo.class, "swoop_servo");
        // Color sensor
        color = hardwareMap.get(ColorSensor.class, "color_sensor");
        intake_color = hardwareMap.get(ColorSensor.class, "intake_color");
        // Distance sensor, currently unused
        //rangesensor = hardwareMap.get(Rev2mDistanceSensor.class, "distance0");
        // Webcam and april tag processor
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(webcamName, aprilTagProcessor);
        LED0 = hardwareMap.get(LED.class, "Led0");
        LED1 = hardwareMap.get(LED.class, "Led1");
        LED2 = hardwareMap.get(LED.class, "Led2");
        LED3 = hardwareMap.get(LED.class, "Led3");
        LED4 = hardwareMap.get(LED.class, "RearLed1_red");
        LED5 = hardwareMap.get(LED.class, "RearLed1_green");
        LED6 = hardwareMap.get(LED.class, "RearLed2_red");
        LED7 = hardwareMap.get(LED.class, "RearLed2_green");

        // Set motor directions and behaviors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        flywheel.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RBT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // Initiate LEDs
        LED0.off();
        LED1.off();
        LED2.off();
        LED3.off();

        // Controls drive speed
        double drivemultiplier = 0.6;

        // Wait for the game to start (driver presses START)
        telemetry.addLine("Initialization complete");
        telemetry.update();

        waitForStart();

        // Give hardware initial positions and powers
        // Intake
        RBT.setPower(intakePower);
        ramp11.setPower(intakePower);
        ramp12.setPower(-intakePower);
        // Drive motors
        // The drive power variables are local, so the motors' powers are simply set to zero
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        // Launcher servo
        launcher.setPosition(launcherPos);

        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;
            //boolean x = gamepad1.xWasPressed();

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            axial = -gamepad1.left_stick_y;
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;

            // Run code to lock heading to detected AprilTag
            if (lockToTag) aprilTagBearingLock();

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double frontLeftPower  = drivemultiplier * (axial + lateral + yaw);
            double frontRightPower = drivemultiplier * (axial - lateral - yaw);
            double backLeftPower   = drivemultiplier * (axial - lateral + yaw);
            double backRightPower  = drivemultiplier * (axial + lateral - yaw);

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }


            // Flicks launcher servo manually
            if (gamepad2.dpadUpWasPressed()) {
                flicking = true;
                flickTimer.reset();
            }

            // Launch sequence
            if (gamepad2.dpadDownWasPressed()) {
                launching = true;
                launchTime.reset();
            }
            // Abort launch
            if (gamepad2.rightStickButtonWasPressed()) {
                launching = false;
            }
            // Manual flywheel control
            if (gamepad2.aWasPressed()) {
                curTargetVelocity = lowVelocity;
            }
            if (gamepad2.bWasPressed()) {
                curTargetVelocity = 0;
            }
            if (gamepad2.xWasPressed()) {
                curTargetVelocity = highVelocity;
            }

            // Intake control code
            if (gamepad2.rightBumperWasPressed()) {
                intakePower = 0;
                strintakestatus = "Off";
            }
            if (gamepad2.leftBumperWasPressed()) {
                intakePower = 0.67;
                strintakestatus = "In";
            }
            if (gamepad2.right_trigger > 0 && gamepad2.left_trigger > 0) {
                intakePower = -0.5;
                strintakestatus = "Out";
            }


            // Turbo Mode™
            if (gamepad1.left_bumper) {
                drivemultiplier = 1;
            } else if (gamepad1.right_bumper) {
                drivemultiplier = 0.15;
            } else {
                drivemultiplier = 0.6;
            }

            // Heading lock
            lockToTag = gamepad1.right_trigger > 0;

            // Pulls info from AprilTag
            aprilTagInfo();

            // Run timed programs
            if (launching) launchScript();
            if (flicking) flickLauncher();

            // Assign calculated values to hardware
            // Intake
            RBT.setPower(intakePower);
            ramp11.setPower(intakePower);
            ramp12.setPower(-intakePower);
            // Drive motors
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);
            // Flywheel
            flywheel.setVelocity(curTargetVelocity);
            // Launcher servo
            launcher.setPosition(launcherPos);

            double curVelocity = flywheel.getVelocity();
            double error = curTargetVelocity - curVelocity;

            // LED indicators
            // Flywheel error LED indicator
            if (Math.abs(error) > 15) {
                LED0.on();
                LED1.off();
            } else {
                LED0.off();
                LED1.on();
            }
            // AprilTag LED indicator
            if (!lockToTag) {
                strlockstatus = "Off";
                LED2.off();
                LED3.off();
            }
            // Rear color sensor LED indicator
            if (color.alpha() > 70) {
                LED4.on();
                LED5.off();
            } else {
                LED4.off();
                LED5.on();
            }
            // Intake color sensor LED indicator & load detector
            if (intake_color.alpha() > 70) {
                LED6.on();
                LED7.off();
                if (!wasArtifactInIntake) {
                    loadTimer.reset();
                }
                wasArtifactInIntake = true;
            } else {
                LED6.off();
                LED7.on();
                wasArtifactInIntake = false;
                intakeWasAutoOff = false;
            }
            if (loadTimer.seconds() > 2 && wasArtifactInIntake) {
                if (!intakeWasAutoOff) {
                    intakePower = 0;
                    intakeWasAutoOff = true;
                }
            }

            // Telemetry
            // Elapsed time since program started
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            // Final wheel power values
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            // Flywheel data
            telemetry.addData("Flywheel Target Velocity", curTargetVelocity);
            telemetry.addData("Current Velocity", "%.2f", curVelocity);
            telemetry.addData("Error", "%.2f", error);
            // Rear color sensor
            telemetry.addData("Rear color alpha", color.alpha());
            // Intake color sensor
            telemetry.addData("Intake Color alpha", intake_color.alpha());

            // Statuses and misc info
            telemetry.addData("Bearing", tagValueBearing);
            telemetry.addData("Intake Status", strintakestatus);
            telemetry.addData("Heading Lock Status", strlockstatus);
            telemetry.addData("Detected AprilTag ID(s)", idsFound);
            telemetry.addData("Detected AprilTag ID(s) double", currentID);
            // Call telemetry update
            telemetry.update();
            //sleep(20); This (might) improve performance if uncommented
        }
    }

}
