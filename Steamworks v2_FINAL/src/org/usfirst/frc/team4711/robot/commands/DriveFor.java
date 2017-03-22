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
		
		setTimeout(3);
	}
	
	@Override
	protected void initialize() {
		//driveSubsystem.arcadeDrive(1.0, 0);
		driveSubsystem.setMoveBy(distanceInches);
		//driveSubsystem.enable();
	}
	
	@Override
	protected void execute() {
		driveSubsystem.driveStraight(distanceInches > 0 ? .7 : -.7);
		System.out.println("Distance - (SetPoint, Position): (" + driveSubsystem.getSetpoint() + ", " 
							+ driveSubsystem.getPosition() +")");
	}

	@Override
	protected boolean isFinished() {
		//return isTimedOut();
		return Math.abs(driveSubsystem.getSetpoint() - driveSubsystem.getPosition()) < .1 || isTimedOut();
	}

	@Override
    protected void end() {
        //driveSubsystem.disable();
        driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
