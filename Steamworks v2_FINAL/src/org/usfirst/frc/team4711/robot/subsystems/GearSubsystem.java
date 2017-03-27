package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GearSubsystem extends Subsystem {

	private DigitalInput limitSwitch;
	
	private static GearSubsystem instance;
	
	private GearSubsystem() {
		super("gearSubsystem");

		limitSwitch = new DigitalInput(IOMap.GEAR_DIO_SWITCH);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static GearSubsystem getInstance(){
		if(instance == null)
			instance = new GearSubsystem();
		
		return instance;
	}
	
	public boolean hasGear(){
		return !limitSwitch.get();
	}
}
