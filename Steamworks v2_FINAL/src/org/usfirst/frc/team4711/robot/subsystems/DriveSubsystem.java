package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveSubsystem extends PIDSubsystem {
	public static enum State {
		AUTO_STRAIGHT, AUTO_TURN, TELEOP;
	}
	private State state;
	
	private CANTalon frontLeftWithEncoder;
	private CANTalon frontRightWithEncoder;
	
	private CANTalon backLeft;
	private CANTalon backRight;
	
	private RobotDrive wheels;

	private ADXRS450_Gyro gyro;
	private double startPosition; //only if no gyro
	
	private AnalogInput frontDistanceSensor;
	private AnalogInput backDistanceSensor;
	
	private static DriveSubsystem instance;
	
	private DriveSubsystem() {
		super("driveSubsystem", .0, .0, .0);
		
		frontLeftWithEncoder = new CANTalon(IOMap.FRONT_LEFT_MOTOR_CHANNEL);
		frontLeftWithEncoder.setEncPosition(frontLeftWithEncoder.getPulseWidthPosition() & 0xFFF);
		frontLeftWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontLeftWithEncoder.configEncoderCodesPerRev(1024);
		
		frontRightWithEncoder = new CANTalon(IOMap.FRONT_RIGHT_MOTOR_CHANNEL);
		frontRightWithEncoder.setEncPosition(frontRightWithEncoder.getPulseWidthPosition() & 0xFFF);
		frontRightWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontRightWithEncoder.configEncoderCodesPerRev(1024);
		
		/*
		 * SRX Mag Encoder can be either Pulse Width or QuadEncoder Signal Type
		 * PulseWidth only if < 6600rpms
		 * QuadEncoder if >= 6600rpms
		 * 
		 * Default pulsesPerRotation = 1024
		 */
		backLeft = new CANTalon(IOMap.REAR_LEFT_MOTOR_CHANNEL);
		backRight = new CANTalon(IOMap.REAR_RIGHT_MOTOR_CHANNEL);
		
		wheels = new RobotDrive(frontLeftWithEncoder, backLeft, frontRightWithEncoder, backRight);
		wheels.setSafetyEnabled(false);
		
		try {
			/*
			 * ADXRS450 Gyro is an Analog Device
			 * +-300mV/sec
			 */
			gyro = new ADXRS450_Gyro();
			//gyro.setSensitivity(.3);
		} catch (Exception e) {
			System.out.println("No gyroscrope found, rotation will be based on position");
			startPosition = 0.0;
		}

        setAbsoluteTolerance(10);
		setState(State.TELEOP);
		
		frontDistanceSensor = new AnalogInput(IOMap.FRONT_SENSOR);
		backDistanceSensor = new AnalogInput(IOMap.BACK_SENSOR);
	}
	
	public static DriveSubsystem getInstance(){
		if(instance == null)
			instance = new DriveSubsystem();
		
		return instance;
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick());
	}
	
	@Override
	protected double returnPIDInput() {
		switch(state){
		case AUTO_STRAIGHT:
			return frontRightWithEncoder.getPosition();
		case AUTO_TURN:
			double pidInput = 0.0;
			if(gyro != null)
				pidInput = gyro.getAngle();
			else {
				pidInput = (startPosition - backLeft.getPosition()) / (IOMap.ROBOT_WIDTH * .5);
				pidInput *= 180 / Math.PI;
			}
			return pidInput ;
		default:
			break;
		}
		
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		switch(state){
		case AUTO_STRAIGHT:
			driveStraight(output);
			break;
		case AUTO_TURN:
				arcadeDrive(0, -output);
			break;
		default:
			break;
		}
	}
	
	public double getFrontSensorVoltage() {
		return frontDistanceSensor.getVoltage();
	}
	
	public double getBackSensorVoltage() {
		return backDistanceSensor.getVoltage();
	}
	
	public void arcadeDrive(double moveValue, double rotateValue){
		wheels.arcadeDrive(moveValue * MotorSpeeds.DRIVE_SPEED_ACCEL, rotateValue * MotorSpeeds.DRIVE_SPEED_TURN);
	}
	
	public void driveStraight(double moveValue){
		/*
		if(gyro != null)
			//arcadeDrive(moveValue, -gyro.getAngle() * .03);
			arcadeDrive(moveValue, .3);
		else
			arcadeDrive(moveValue, 0);
		*/
		
		if(Math.abs(frontLeftWithEncoder.getSpeed()) > Math.abs(frontRightWithEncoder.getSpeed()))
			wheels.tankDrive(moveValue * Math.abs(frontRightWithEncoder.getSpeed() / frontLeftWithEncoder.getSpeed()), moveValue);
		else if(Math.abs(frontLeftWithEncoder.getSpeed()) < Math.abs(frontRightWithEncoder.getSpeed()))
			wheels.tankDrive(moveValue, moveValue * Math.abs(frontLeftWithEncoder.getSpeed() / frontRightWithEncoder.getSpeed()));
		else
			arcadeDrive(moveValue, 0);
		
	}
	
	public void setMoveBy(double distanceInches){
		setState(DriveSubsystem.State.AUTO_STRAIGHT);
		double wheelCircumference = Math.PI * IOMap.DRIVE_WHEEL_DIAMETER;
		setSetpointRelative(distanceInches / wheelCircumference);
		
	}
	
	public void setRotateBy(double angle){
		setState(DriveSubsystem.State.AUTO_TURN);
		if(gyro == null)
			startPosition = backLeft.getPosition();
		setSetpointRelative(angle);
	}
	
	public void stop(){
		wheels.stopMotor();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		switch(state){
		case AUTO_STRAIGHT:
			getPIDController().setPID(IOMap.DRIVE_DISTANCE_PID[0], IOMap.DRIVE_DISTANCE_PID[1], IOMap.DRIVE_DISTANCE_PID[2]);
			break;
		case AUTO_TURN:
			getPIDController().setPID(IOMap.DRIVE_ANGLE_PID[0], IOMap.DRIVE_ANGLE_PID[1], IOMap.DRIVE_ANGLE_PID[2]);
			break;
		default:
			break;
		}
		
		this.state = state;
	}
}
