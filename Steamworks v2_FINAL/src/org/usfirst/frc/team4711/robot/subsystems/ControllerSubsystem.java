package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ControllerSubsystem extends Subsystem {
	private Joystick joystick;
	private JoystickButton buttonX;
	private JoystickButton buttonY;
	private JoystickButton buttonB;
	private JoystickButton buttonA;

	private static ControllerSubsystem instance;
	
	private ControllerSubsystem(){
		joystick = new Joystick(IOMap.JOYSTICK_PORT);
		buttonX = new JoystickButton(joystick, IOMap.X);
	}
	
	public static ControllerSubsystem getInstance(){
		if(instance == null)
			instance = new ControllerSubsystem();
		
		return instance;
	}
	
	public Joystick getController(){
		return joystick;
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
