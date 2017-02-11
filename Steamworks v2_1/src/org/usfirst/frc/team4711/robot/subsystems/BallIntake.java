package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.Robot;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.KeyMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;

public class BallIntake implements ISubsystem {

	private CANTalon intake;
	
	private boolean active;
	
	// this just means active or whatever
	// tad different from our intake_pressed
	private boolean button_pressed;
	
	public BallIntake() {	
		this.intake = new CANTalon(IOMap.BALL_INTAKE_CHANNEL);
		
		this.active = false;
	}
	
	@Override
	public void autonomousPeriodic() {
		
	}

	@Override
	public void teleopPeriodic() {
		boolean intake_pressed = Robot.joystick.getRawButton(KeyMap.INTAKE);
		boolean unjam_pressed = Robot.joystick.getRawButton(KeyMap.INTAKE_UNJAM);
		
		if (intake_pressed && !button_pressed) {
			this.active = !active;
			this.button_pressed = true;
			
			this.intake.set(active ? MotorSpeeds.INTAKE_SPEED : 0); 
			
		} else if (!intake_pressed && button_pressed) {
			this.button_pressed = false;
		}
		
		// end section
		
		// we don't need a toggle for reversing it, since we wont be doing it much
		// (in case it gets jammed)
		if (unjam_pressed) {
			if (!this.active) {
				this.intake.set(-1 * MotorSpeeds.INTAKE_SPEED);
			} else {
				this.intake.set(0);
			}
		}
		
	}

	@Override
	public void start() {
		this.active = true;
		this.intake.set(MotorSpeeds.INTAKE_SPEED);
	}

	@Override
	public void stop() {
		this.active = false;
		this.intake.set(0);
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
