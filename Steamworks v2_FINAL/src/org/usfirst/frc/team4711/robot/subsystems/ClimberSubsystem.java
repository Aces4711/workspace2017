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
		//set the peak and Nominal outputs, 12V mean full
		climber.configNominalOutputVoltage(0.0, 0.0);
		climber.configPeakOutputVoltage(12.0 * MotorSpeeds.CLIMB_SPEED, -12.0 * MotorSpeeds.CLIMB_SPEED);
		//0 to 6 in 1 sec
		climber.setVoltageRampRate(6);
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
		climber.set(moveValue);
	}
}
