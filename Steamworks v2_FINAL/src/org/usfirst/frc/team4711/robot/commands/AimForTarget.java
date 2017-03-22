package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class AimForTarget extends Command {
	
	private RobotEyeSubsystem robotEyeSubsystem;
	private DriveSubsystem driveSubsystem;
	
	public AimForTarget(){
		super("aimforTarget");
		
		robotEyeSubsystem = RobotEyeSubsystem.getInstance();
		requires(robotEyeSubsystem);
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
		
		//setTimeout(10);
	}
	
	@Override
	protected void initialize() {
		robotEyeSubsystem.startVisionBack();
	}
	
	@Override
	protected void execute() {
		driveSubsystem.arcadeDrive(0, robotEyeSubsystem.getTargetCenterX());
	}

	@Override
	protected boolean isFinished() {
		return (-0.01 < robotEyeSubsystem.getTargetCenterX() && robotEyeSubsystem.getTargetCenterX() < 0.01 ) || isTimedOut();
	}
	
	@Override
    protected void end() {
		robotEyeSubsystem.endVisionBack();
		driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }

}
