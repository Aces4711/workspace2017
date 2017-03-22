package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.KeyMap;
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
		robotEyeSubsystem.startVisionFront();
		//robotEyeSubsystem.startVisionBack();
		
    }

    protected void execute() {
    	
    	//driveSubsystem.arcadeDrive(controllerSubsystem.getController().getRawAxis(IOMap.AXIS_LEFT_Y), controllerSubsystem.getController().getRawAxis(IOMap.AXIS_RIGHT_X));
    	driveSubsystem.arcadeDrive((controllerSubsystem.getController().getRawAxis(KeyMap.ACCEL_FORWARD) > 0)? 
    										controllerSubsystem.getController().getRawAxis(KeyMap.ACCEL_FORWARD) :
    											((controllerSubsystem.getController().getRawAxis(KeyMap.ACCEL_BACK) > 0)? 
    													-controllerSubsystem.getController().getRawAxis(KeyMap.ACCEL_BACK):
    														0.0), -controllerSubsystem.getController().getRawAxis(IOMap.AXIS_LEFT_X));
        
    }
    
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
    protected void end() {
		driveSubsystem.stop();
		robotEyeSubsystem.endVisionFront();
		//robotEyeSubsystem.endVisionBack();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
	
}
