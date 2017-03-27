package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team4711.robot.config.AnalogDistanceSensorLUT;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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
	
	private AnalogInput leftDistanceSensor;
	private AnalogInput rightDistanceSensor;
	
	private static DriveSubsystem instance;
	
	private DriveSubsystem() {
		super("driveSubsystem", .0, .0, .0);
		
		/*
		 * SRX Mag Encoder can be either Pulse Width or QuadEncoder Signal Type
		 * PulseWidth only if < 6600rpms
		 * QuadEncoder if >= 6600rpms
		 * 
		 * Default pulsesPerRotation = 1024
		 */
		frontLeftWithEncoder = new CANTalon(IOMap.FRONT_LEFT_MOTOR_CHANNEL);
		frontLeftWithEncoder.setEncPosition(frontLeftWithEncoder.getPulseWidthPosition() & 0xFFF);
		frontLeftWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontLeftWithEncoder.configEncoderCodesPerRev(1024);
		
		frontRightWithEncoder = new CANTalon(IOMap.FRONT_RIGHT_MOTOR_CHANNEL);
		frontRightWithEncoder.setEncPosition(frontRightWithEncoder.getPulseWidthPosition() & 0xFFF);
		frontRightWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		frontRightWithEncoder.configEncoderCodesPerRev(1024);
		
		backLeft = new CANTalon(IOMap.REAR_LEFT_MOTOR_CHANNEL);
		backRight = new CANTalon(IOMap.REAR_RIGHT_MOTOR_CHANNEL);
		
		wheels = new RobotDrive(frontLeftWithEncoder, backLeft, frontRightWithEncoder, backRight);
		wheels.setSafetyEnabled(false);
		
		//not using gyro anyways
		try {
			/*
			 * ADXRS450 Gyro is an Analog Device
			 * +-300mV/sec
			 */
			gyro = new ADXRS450_Gyro();
			//gyro.setSensitivity(.3);
		} catch (Exception e) {
			System.out.println("No gyroscrope found, rotation will be based on position");
		}

        setAbsoluteTolerance(10);
		setState(State.TELEOP);
		
		leftDistanceSensor = new AnalogInput(IOMap.LEFT_DISTANCE_SENSOR);
		rightDistanceSensor = new AnalogInput(IOMap.RIGHT_DISTANCE_SENSOR);
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
		return frontRightWithEncoder.getPosition();
	}

	@Override
	protected void usePIDOutput(double output) {
		switch(state){
		case AUTO_STRAIGHT:
			driveStraight(output);
			break;
		case AUTO_TURN:
			turnOnAxis(output);
			break;
		default:
			break;
		}
	}
	
	public double getDistanceFromLeftSensor() {
		return AnalogDistanceSensorLUT.calucateDistance(leftDistanceSensor.getVoltage()) - IOMap.SPACE_FROM_BUMPER_TO_DISTANCE_SENSORS;
	}
	
	public double getDistanceFromRightSensor() {
		return AnalogDistanceSensorLUT.calucateDistance(rightDistanceSensor.getVoltage()) - IOMap.SPACE_FROM_BUMPER_TO_DISTANCE_SENSORS;
	}
	
	public void arcadeDrive(double moveValue, double rotateValue){
		wheels.arcadeDrive(moveValue * MotorSpeeds.DRIVE_SPEED_ACCEL, rotateValue * MotorSpeeds.DRIVE_SPEED_TURN);
	}
	
	public void driveStraight(double moveValue){
		double leftMoveValue = moveValue * MotorSpeeds.DRIVE_SPEED_ACCEL;
		double rightMoveValue = moveValue * MotorSpeeds.DRIVE_SPEED_ACCEL;
		
		if(Math.abs(frontLeftWithEncoder.getSpeed()) > Math.abs(frontRightWithEncoder.getSpeed()))
			leftMoveValue *= Math.abs(frontRightWithEncoder.getSpeed() / frontLeftWithEncoder.getSpeed());
		else if(Math.abs(frontLeftWithEncoder.getSpeed()) < Math.abs(frontRightWithEncoder.getSpeed()))
			rightMoveValue *= Math.abs(frontLeftWithEncoder.getSpeed() / frontRightWithEncoder.getSpeed());
		
		wheels.tankDrive(leftMoveValue, rightMoveValue);
	}
	
	public void turnOnAxis(double moveValue) {
		double leftMoveValue = moveValue * MotorSpeeds.DRIVE_SPEED_TURN;
		double rightMoveValue = moveValue * MotorSpeeds.DRIVE_SPEED_TURN;
		
		if(Math.abs(frontLeftWithEncoder.getSpeed()) > Math.abs(frontRightWithEncoder.getSpeed()))
			leftMoveValue *= Math.abs(frontRightWithEncoder.getSpeed() / frontLeftWithEncoder.getSpeed());
		else if(Math.abs(frontLeftWithEncoder.getSpeed()) < Math.abs(frontRightWithEncoder.getSpeed()))
			rightMoveValue *= Math.abs(frontLeftWithEncoder.getSpeed() / frontRightWithEncoder.getSpeed());
		
		wheels.tankDrive(leftMoveValue, -rightMoveValue);
	}
	
	public void setMoveBy(double distanceInches){
		setState(DriveSubsystem.State.AUTO_STRAIGHT);
		double wheelCircumference = Math.PI * IOMap.DRIVE_WHEEL_DIAMETER;
		setSetpointRelative(distanceInches / wheelCircumference);
	}
	
	public void setRotateBy(double angle){
		setState(DriveSubsystem.State.AUTO_TURN);
		double length = IOMap.ROBOT_WIDTH * Math.PI * (angle / 360);
		double wheelCircumference = Math.PI * IOMap.DRIVE_WHEEL_DIAMETER;
		setSetpointRelative(length / wheelCircumference);
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
