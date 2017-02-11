package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.Robot;
import org.usfirst.frc.team4711.robot.config.IOMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;

public class DriveSubsystem extends RobotDrive implements ISubsystem {
	public DriveSubsystem() {

		super(new CANTalon(IOMap.FRONT_LEFT_MOTOR_CHANNEL),
				new CANTalon(IOMap.REAR_LEFT_MOTOR_CHANNEL),
    			new CANTalon(IOMap.FRONT_RIGHT_MOTOR_CHANNEL),
    			new CANTalon(IOMap.REAR_RIGHT_MOTOR_CHANNEL));
		
	}

	@Override
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
		// insert the thingies for the proximity sensors before calling arcadeDrive
		
		super.arcadeDrive(moveValue, rotateValue, squaredInputs);
	}
	
	@Override
	public void autonomousPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void teleopPeriodic() {
		this.arcadeDrive(Robot.joystick.getY(), Robot.joystick.getX(), true);
		
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean test() {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public boolean diagnostic() {
		// TODO Auto-generated method stub
		
		return true;
	}

}
