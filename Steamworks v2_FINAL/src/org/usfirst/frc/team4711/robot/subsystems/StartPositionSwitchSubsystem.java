package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class StartPositionSwitchSubsystem extends Subsystem {
	public static enum StartPosition {
		CENTER, LEFT, RIGHT;
	}
	
	private StartPosition startPosition;
	
	private DigitalInput leftSwitch;
	private DigitalInput rightSwitch;
	
	private static StartPositionSwitchSubsystem instance;
	
	private StartPositionSwitchSubsystem(){
		super("startPositionSwitchSubsystem");

		leftSwitch = new DigitalInput(IOMap.LEFT_DIO_SWITCH);
		rightSwitch = new DigitalInput(IOMap.RIGHT_DIO_SWITCH);
		
		startPosition = (leftSwitch.get() && rightSwitch.get()) ?  
										StartPosition.CENTER:
										((leftSwitch.get()) ? 
												StartPosition.LEFT:
												StartPosition.RIGHT);	
	}

	@Override
	protected void initDefaultCommand() {
	}
	
	public static StartPositionSwitchSubsystem getInstance() {
		if(instance == null)
			instance = new StartPositionSwitchSubsystem();
		
		return instance;
	}
	
	public StartPosition getStartPosition(){
		return startPosition;
	}

}
