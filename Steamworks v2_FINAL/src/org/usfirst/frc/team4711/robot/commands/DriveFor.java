package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveFor extends Command {
	
	private double distanceInches;
	
	private DriveSubsystem driveSubsystem;

	public DriveFor(double distanceInches) {
		super("DriveFor");
		
		this.distanceInches = distanceInches;
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
		
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		driveSubsystem.setMoveBy(distanceInches);
		driveSubsystem.enable();
	}
	
	@Override
	protected void execute() {
		System.out.println("Distance - (SetPoint, Position): (" + driveSubsystem.getSetpoint() + ", " 
							+ driveSubsystem.getPosition() +")");
	}

	@Override
	protected boolean isFinished() {
		return driveSubsystem.onTarget() || isTimedOut();
	}

	@Override
    protected void end() {
        driveSubsystem.disable();
        driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
