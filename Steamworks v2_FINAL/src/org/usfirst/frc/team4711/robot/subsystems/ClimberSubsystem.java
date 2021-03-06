package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSubsystem extends Subsystem {

	private CANTalon climber;
	
	private static ClimberSubsystem instance;
	
	private ClimberSubsystem() {
		super("climberSubsystem");
		
		climber = new CANTalon(IOMap.LEFT_CLIMB_CHANNEL);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static ClimberSubsystem getInstance(){
		if(instance == null)
			instance = new ClimberSubsystem();
		
		return instance;
	}
	
	public void setMotorSpeed(double moveValue){
		climber.set(moveValue * MotorSpeeds.CLIMB_SPEED);
	}
}
