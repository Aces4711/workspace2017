package org.usfirst.frc.team4711.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import com.ctre.CANTalon;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private RobotDrive _robotDrive;
	
	private Gyro _gyro;
	private long _autonomousStartTime;
	
	// Use Encoder class only if encoder is attached to roboRIO DIO channels
	private Encoder _encoder;
	
	// This needed because encoder is attached and connected to the Talon
	private CANTalon _rearLeftTalon;
	private CANTalon _rearRightTalon;
	
	private Joystick _joystick;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	System.out.println("on robotInit");
    	
    	_robotDrive = new RobotDrive( new CANTalon(RobotMapping.FRONTLEFTMOTOR_CHANNEL), new CANTalon(RobotMapping.REARLEFTMOTOR_CHANNEL),
    								  new CANTalon(RobotMapping.FRONTRIGHTMOTOR_CHANNEL), new CANTalon(RobotMapping.REARRIGHTMOTOR_CHANNEL));
    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	if (_gyro == null) {
    		_gyro = new AnalogGyro(RobotMapping.GYRO_CHANNEL);
    	}
    	
    	_autonomousStartTime = System.currentTimeMillis();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if (System.currentTimeMillis() >= _autonomousStartTime + RobotMapping.AUTONOMOUS_DURATION * 1000) {
    		return;
    	}
    	
    	final double kp = 0.03;
    	_robotDrive.arcadeDrive(-1.0, -_gyro.getAngle() * kp);
    	
    	Timer.delay(0.01);
    }
    
    /**
     * This function is called once each time the robot enters teleoperated mode
     */
    public void teleopInit(){
    	System.out.println("on teleopinit");
        if (_joystick == null) {
        	_joystick = new Joystick(RobotMapping.JOYSTICK_PORT);
        }
        
        /* 
    	_encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	_encoder.setDistancePerPulse(5); 
    	if(_encoder != null)
    		_encoder.reset();
    	*/
        
    	_rearLeftTalon = new CANTalon(RobotMapping.REARLEFTMOTOR_CHANNEL);
    	_rearRightTalon = new CANTalon(RobotMapping.REARRIGHTMOTOR_CHANNEL);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//System.out.println("x axis : " + _joystick.getX() + ", y axis : " + _joystick.getY());
    	_robotDrive.arcadeDrive(_joystick);
    	
    	//test encoder
    	System.out.println("Left.EncPosition : " + _rearLeftTalon.getEncPosition() + ", Left.EncVelocity : " + _rearLeftTalon.getEncVelocity());
    	System.out.println("Right.EncPosition : " + _rearRightTalon.getEncPosition() + ", Right.EncVelocity : " + _rearRightTalon.getEncVelocity());
    	
    	Timer.delay(0.01);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	System.out.println("on testPeriodic");
    }
    
}
