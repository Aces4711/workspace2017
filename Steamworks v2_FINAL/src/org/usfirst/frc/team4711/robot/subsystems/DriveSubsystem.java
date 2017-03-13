package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;
import org.usfirst.frc.team4711.robot.wrappers.CustomDrive;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveSubsystem extends PIDSubsystem {
	public static enum State {
		AUTO_STRAIGHT, AUTO_TURN, TELEOP;
	}
	private State state;
	
	private CANTalon frontLeft;
	private CANTalon frontRight;
	
	private CANTalon backLeftWithEncoder;
	private CANTalon backRightWithEncoder;
	
	private CustomDrive wheels;

	private AnalogGyro gyro;
	private double startPosition; //only if no gyro
	
	private AnalogInput frontDistanceSensor;
	private AnalogInput backDistanceSensor;
	
	private static DriveSubsystem instance;
	
	private DriveSubsystem() {
		super("driveSubsystem", .0, .0, .0);
		
		frontLeft = new CANTalon(IOMap.FRONT_LEFT_MOTOR_CHANNEL);
		frontRight = new CANTalon(IOMap.FRONT_RIGHT_MOTOR_CHANNEL);
		
		/*
		 * SRX Mag Encoder can be either Pulse Width or QuadEncoder Signal Type
		 * PulseWidth only if < 6600rpms
		 * QuadEncoder if >= 6600rpms
		 * 
		 * Default pulsesPerRotation = 1024
		 */
		backLeftWithEncoder = new CANTalon(IOMap.REAR_LEFT_MOTOR_CHANNEL);
	    backLeftWithEncoder.setEncPosition(backLeftWithEncoder.getPulseWidthPosition() & 0xFFF);
		backLeftWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		backLeftWithEncoder.configEncoderCodesPerRev(1024);
		
		backRightWithEncoder = new CANTalon(IOMap.REAR_RIGHT_MOTOR_CHANNEL);
		backRightWithEncoder.setEncPosition(backRightWithEncoder.getPulseWidthPosition() & 0xFFF);
		backRightWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		backRightWithEncoder.configEncoderCodesPerRev(1024);
		
		wheels = new CustomDrive(frontLeft, backLeftWithEncoder, frontRight, backRightWithEncoder);
		
		try {
			/*
			 * ADXRS450 Gyro is an Analog Device
			 * +-300mV/sec
			 */
			gyro = new AnalogGyro(IOMap.GYRO_CHANNEL);
			gyro.setSensitivity(.3);
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
			return Math.min(-backLeftWithEncoder.getPosition(), backRightWithEncoder.getPosition());
		case AUTO_TURN:
			double pidInput = 0.0;
			if(gyro != null)
				pidInput = gyro.getAngle();
			else
				pidInput = startPosition - backLeftWithEncoder.getPosition() / IOMap.r
		default:
			break;
		}
		
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		switch(state){
		case AUTO_STRAIGHT:
			if(gyro != null)
				arcadeDrive(output, -gyro.getAngle() * .03);
			else
				arcadeDrive(output, 0);
			break;
		case AUTO_TURN:
			wheels.tankDrive(-output, output);
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
		wheels.arcadeDrive(moveValue * MotorSpeeds.DRIVE_SPEED, rotateValue);
	}
	
	public void setMoveBy(double distanceInches){
		setState(DriveSubsystem.State.AUTO_STRAIGHT);
		double wheelCircumference = Math.PI * IOMap.DRIVE_WHEEL_DIAMETER;
		setSetpointRelative(distanceInches / wheelCircumference);
	}
	
	public void setRotateBy(double angle){
		setState(DriveSubsystem.State.AUTO_TURN);
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
