package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.ControllerSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoystick extends Command {
	
	private ControllerSubsystem controllerSubsystem;
	private DriveSubsystem driveSubsystem;
	
	public DriveWithJoystick() {
		super("Drive With Joystick");
		
		controllerSubsystem = ControllerSubsystem.getInstance();
		requires(controllerSubsystem);
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
	}
	
	protected void initialize() {
    }

    protected void execute() {
    	driveSubsystem.arcadeDrive(controllerSubsystem.getController().getAxis(AxisType.kY), controllerSubsystem.getController().getAxis(AxisType.kX));
    }
    
	@Override
	protected boolean isFinished() {
		return false;
	}
	
}
