package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.commands.RunLauncher;
import org.usfirst.frc.team4711.robot.commands.RunLauncherBaseOnDistance;
import org.usfirst.frc.team4711.robot.commands.RunClimber;
import org.usfirst.frc.team4711.robot.commands.RunIntake;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.KeyMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ControllerSubsystem extends Subsystem {
	private Joystick joystick;
	private JoystickButton intakeButton;
	private JoystickButton intakeUnjamButton;
	private JoystickButton winchUpButton;
	private JoystickButton winchDownButton;
	private JoystickButton continuousLaunchButton;
	private JoystickButton distanceLaunchButton;

	private static ControllerSubsystem instance;
	
	private ControllerSubsystem(){
		joystick = new Joystick(IOMap.JOYSTICK_PORT);
		
		intakeButton = new JoystickButton(joystick, KeyMap.INTAKE);
		intakeButton.toggleWhenPressed(new RunIntake(1.0));
		
		intakeUnjamButton = new JoystickButton(joystick, KeyMap.INTAKE_UNJAM);
		intakeUnjamButton.whenPressed(new RunIntake(-1.0));
		
		winchUpButton = new JoystickButton(joystick, KeyMap.WINCH_UP);
		winchUpButton.toggleWhenPressed(new RunClimber(1.0));

		winchDownButton = new JoystickButton(joystick, KeyMap.WINCH_DOWN);
		winchDownButton.toggleWhenPressed(new RunClimber(-1.0));
		
		continuousLaunchButton = new JoystickButton(joystick, KeyMap.CONTINUOUS_LAUNCH);
		continuousLaunchButton.toggleWhenPressed(new RunLauncher(3000));
		
		distanceLaunchButton = new JoystickButton(joystick, KeyMap.DISTANCE_LAUNCH);
		distanceLaunchButton.toggleWhenPressed(new RunLauncherBaseOnDistance());
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static ControllerSubsystem getInstance(){
		if(instance == null)
			instance = new ControllerSubsystem();
		
		return instance;
	}
	
	public Joystick getController(){
		return joystick;
	}

}
