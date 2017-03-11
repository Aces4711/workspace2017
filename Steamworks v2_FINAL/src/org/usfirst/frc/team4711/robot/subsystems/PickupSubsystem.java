package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class PickupSubsystem extends Subsystem {

	private CANTalon pickup;
	
	private static PickupSubsystem instance;
	
	private PickupSubsystem() {
		super("pickupSubsystem");
		
		pickup = new CANTalon(IOMap.BALL_INTAKE_CHANNEL);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static PickupSubsystem getInstance(){
		if(instance == null)
			instance = new PickupSubsystem();
		
		return instance;
	}
	
	public void setMotorSpeed(double moveValue){
		pickup.set(moveValue * MotorSpeeds.INTAKE_SPEED);
	}
}
