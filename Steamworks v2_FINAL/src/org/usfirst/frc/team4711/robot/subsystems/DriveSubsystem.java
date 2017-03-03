package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.wrappers.ADXRS453Gyro;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class DriveSubsystem extends PIDSubsystem {
	public static enum State {
		AUTO_STRAIGHT, AUTO_TURN, TELEOP;
	}
	private State state;
	
	private CANTalon frontLeft;
	private CANTalon frontRight;
	
	private CANTalon backLeft;
	private CANTalon backRight;
	
	private RobotDrive wheels;

	private AnalogGyro gyroscope;
	
	private AnalogInput frontDistanceSensor;
	private AnalogInput backDistanceSensor;
	
	private static DriveSubsystem instance;
	
	private DriveSubsystem() {
		super("driveSubsystem", .5, .001, .0);
		
		frontLeft = new CANTalon(IOMap.FRONT_LEFT_MOTOR_CHANNEL);
		frontRight = new CANTalon(IOMap.FRONT_RIGHT_MOTOR_CHANNEL);
		
		/*
		 * SRX Mag Encoder can be either Pulse Width or QuadEncoder Signal Type
		 * PulseWidth only if < 6600rpms
		 * QuadEncoder if >= 6600rpms
		 * 
		 * Default pulsesPerRotation = 1024
		 */
		backLeft = new CANTalon(IOMap.REAR_LEFT_MOTOR_CHANNEL);
		//When measuring position, best to sample Pulse Width at rest and set the position of the quadrature signal to match it
		backLeft.setEncPosition(backLeft.getPulseWidthPosition() & 0xFFF);
		
		backLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//backLeft.configEncoderCodesPerRev(4);
		//set the peak and Nominal outputs, 12V mean full
		backLeft.configNominalOutputVoltage(+0f, -0f);
		backLeft.configPeakOutputVoltage(+12f, -12f);
		//0 to 6 in 1 sec
		backLeft.setVoltageRampRate(6);
		
		backRight = new CANTalon(IOMap.REAR_RIGHT_MOTOR_CHANNEL);
		backRight.setEncPosition(backRight.getPulseWidthPosition() & 0xFFF);
		backRight.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		backRight.configNominalOutputVoltage(+0f, -0f);
		backRight.configPeakOutputVoltage(+12f, -12f);
		backRight.setVoltageRampRate(6);
		
		wheels = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
		
		try {
			/*
			 * ADXRS450 Gyro is an Analog Device
			 * +-300mV/sec
			 */
			gyroscope = new AnalogGyro(IOMap.GYRO_CHANNEL);
			gyroscope.setSensitivity(.3);
		} catch (Exception e) {
			System.out.println("No gyroscrope found rotation will be based on position");
		}
		
		frontDistanceSensor = new AnalogInput(0);
		backDistanceSensor = new AnalogInput(1);

        setAbsoluteTolerance(10);
		setState(State.TELEOP);
	}
	
	public static DriveSubsystem getInstance(){
		if(instance == null)
			instance = new DriveSubsystem();
		
		return instance;
	}
	
	@Override
	protected void initDefaultCommand() {
		this.setDefaultCommand(new DriveWithJoystick());
	}
	
	@Override
	protected double returnPIDInput() {
		switch(state){
		case AUTO_STRAIGHT:
			return Math.min(-backLeft.getEncPosition(), backRight.getEncPosition());
		case AUTO_TURN:
			return gyroscope.getAngle();
		default:
			break;
		}
		
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		switch(state){
		case AUTO_STRAIGHT:
			arcadeDrive(-output, 0);
			//arcadeDrive(output, -gyroscope.getAngle() * .03);
			break;
		case AUTO_TURN:
			wheels.tankDrive(output, -output);
			break;
		default:
			break;
		}
	}
	
	public int getFrontDistance() {
		return frontDistanceSensor.getValue();
	}
	
	public int getBackDistance() {
		return backDistanceSensor.getValue();
	}
	
	public void arcadeDrive(double moveValue, double rotateValue){
		wheels.arcadeDrive(moveValue, rotateValue);
	}
	
	public void setMoveBy(double distanceInches){
		setState(DriveSubsystem.State.AUTO_STRAIGHT);
		double wheelCircumference = Math.PI * IOMap.DRIVE_WHEEL_DIAMETER;
		setSetpointRelative((distanceInches / wheelCircumference) * 1024);
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
			getPIDController().setPID(.5, .001, .0);
			break;
		case AUTO_TURN:
			getPIDController().setPID(.5, .001, .0);
			break;
		default:
			break;
		}
		
		this.state = state;
	}
}
