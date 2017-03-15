package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.Robot;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.KeyMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;

public class BallLauncher implements ISubsystem {

	private CANTalon augger;
	private CANTalon launcher;
	
	private boolean active;
	
	private long startTime;
	
	public BallLauncher() {
		this.augger = new CANTalon(IOMap.BALL_AUGGER);
		this.launcher = new CANTalon(IOMap.BALL_LAUNCH_CHANNEL);
		
		this.active = false;
	}
	
	@Override
	public void autonomousPeriodic() {
		
	}

	@Override
	public void teleopPeriodic() {
		boolean continuous_launch_pressed = Robot.joystick.getRawButton(KeyMap.CONTINUOUS_LAUNCH);
		boolean single_launch_pressed = Robot.joystick.getRawButton(KeyMap.SINGLE_LAUNCH);
		
		// start spinning up the launcher
		/**if (single_launch_pressed) {
			this.augger.set(MotorSpeeds.AUGGER_SPEED);
		}**/
		
		if (continuous_launch_pressed) {
			this.augger.set(MotorSpeeds.AUGGER_SPEED);
			this.launcher.set(MotorSpeeds.LAUNCHER_SPEED);
		} else {
			this.augger.set(0);
			this.launcher.set(0);
		}
		
		/** this section handles the ball movey thingy
		if (single_launch_pressed && !active) {
			this.startTime = System.currentTimeMillis();
			this.active = true;
		} else if (this.startTime - System.currentTimeMillis() >= KeyMap.SINGLE_LAUNCH_TIME && active) {
			this.active = false;
		}
		
		if (active || continuous_launch_pressed) {
			this.augger.set(MotorSpeeds.LAUNCHER_SPEED);
		} else {
			this.augger.set(0);
		}**/
		// end
		
		
	}

	@Override
	public void start() {
		this.active = true;
	
		this.augger.set(MotorSpeeds.AUGGER_SPEED);
		this.launcher.set(MotorSpeeds.LAUNCHER_SPEED);
	}
	
	@Override
	public void stop() {
		this.active = false;
		
		this.augger.set(0);
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
