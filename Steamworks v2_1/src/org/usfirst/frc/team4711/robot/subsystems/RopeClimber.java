package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.Robot;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.KeyMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;

public class RopeClimber implements ISubsystem {
	private CANTalon climberLeft;
	private CANTalon climberRight;
	
	public RopeClimber() {
		climberLeft = new CANTalon(IOMap.LEFT_CLIMB_CHANNEL);
		climberRight = new CANTalon(IOMap.RIGHT_CLIMB_CHANNEL);
	}
	
	@Override
	public void autonomousPeriodic() {
		
	}
	
	@Override
	public void teleopPeriodic() {
		boolean winch_up_pressed = Robot.joystick.getRawButton(KeyMap.WINCH_UP);
		boolean winch_down_pressed = Robot.joystick.getRawButton(KeyMap.WINCH_DOWN);
		
		if (winch_up_pressed && !winch_down_pressed) {
			this.climberLeft.set(MotorSpeeds.CLIMB_SPEED);
			this.climberRight.set(MotorSpeeds.CLIMB_SPEED);
		} else if (!winch_up_pressed & winch_down_pressed) {
			this.climberLeft.set(-1 * MotorSpeeds.CLIMB_SPEED);
			this.climberRight.set(-1 * MotorSpeeds.CLIMB_SPEED);
		} else {
			this.climberLeft.set(0);
			this.climberRight.set(0);
		}
		
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
