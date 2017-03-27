package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class BackUpTo extends Command {
	
	private double distanceInches;
	private boolean backUp;
	
	private DriveSubsystem driveSubsystem;

	public BackUpTo(double distanceInches) {
		super("DriveTo");
		
		this.distanceInches = distanceInches;
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);

		setTimeout(10);
	}
	
	@Override
	protected void initialize() {
		backUp = (	(driveSubsystem.getDistanceFromLeftSensor() < 0.0 && driveSubsystem.getDistanceFromRightSensor() < 0.0) ||
					(driveSubsystem.getDistanceFromLeftSensor() > distanceInches && driveSubsystem.getDistanceFromRightSensor() < 0.0) ||
					(driveSubsystem.getDistanceFromLeftSensor() < 0.0 && driveSubsystem.getDistanceFromRightSensor() > distanceInches) ||
					(driveSubsystem.getDistanceFromLeftSensor() > distanceInches && driveSubsystem.getDistanceFromRightSensor() > distanceInches));
	}
	
	@Override
	protected void execute() {
		driveSubsystem.driveStraight((!backUp) ? 1.0 : -1.0);
	}

	@Override
	protected boolean isFinished() {
		boolean isFinished = (	(driveSubsystem.getDistanceFromLeftSensor() < 0.0 && driveSubsystem.getDistanceFromRightSensor() < 0.0) ||
								(driveSubsystem.getDistanceFromLeftSensor() >= distanceInches && driveSubsystem.getDistanceFromRightSensor() < 0.0) ||
								(driveSubsystem.getDistanceFromLeftSensor() < 0.0 && driveSubsystem.getDistanceFromRightSensor() >= distanceInches) ||
								(driveSubsystem.getDistanceFromLeftSensor() >= distanceInches && driveSubsystem.getDistanceFromRightSensor() >= distanceInches));

		return ((!backUp) ? isFinished : !isFinished) || isTimedOut();
	}

	@Override
    protected void end() {
        driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
