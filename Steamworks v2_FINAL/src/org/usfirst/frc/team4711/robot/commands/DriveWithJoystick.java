package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.ControllerSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;

public class DriveWithJoystick extends Command {
	
	private ControllerSubsystem controllerSubsystem;
	private RobotEyeSubsystem robotEyeSubsystem;
	private DriveSubsystem driveSubsystem;
	
	public DriveWithJoystick() {
		super("Drive With Joystick");
		
		controllerSubsystem = ControllerSubsystem.getInstance();
		requires(controllerSubsystem);
		
		robotEyeSubsystem = RobotEyeSubsystem.getInstance();
		requires(robotEyeSubsystem);
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
	}
	
	protected void initialize() {
		robotEyeSubsystem.startVision();
    }

    protected void execute() {
    	driveSubsystem.arcadeDrive(controllerSubsystem.getController().getAxis(AxisType.kY), controllerSubsystem.getController().getAxis(AxisType.kX));
    }
    
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
    protected void end() {
		driveSubsystem.stop();
		robotEyeSubsystem.endVision();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
	
}
