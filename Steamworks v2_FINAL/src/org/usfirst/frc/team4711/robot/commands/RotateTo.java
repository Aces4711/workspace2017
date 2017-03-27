package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RotateTo extends Command {
	private double angle;
	
	private DriveSubsystem driveSubsystem;
	
	public RotateTo(double angle) {
		super("RotateTo");
		
		this.angle = angle;
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
		
		setTimeout(10 * Math.abs(angle / 360));
	}
	
	@Override
	protected void initialize() {
		driveSubsystem.setRotateBy(angle);
		//used to start the PID Closed Loop
		//driveSubsystem.enable();
	}
	
	@Override
	protected void execute() {
		//no need if using the PID Closed Loop
		driveSubsystem.turnOnAxis(angle > 0 ? 1.0 : -1.0);

	}
	
	@Override
	protected boolean isFinished() {
		//Same thing
		//return driveSubsystem.onTarget() || isTimedOut();
		return Math.abs(driveSubsystem.getSetpoint() - driveSubsystem.getPosition()) < .1 || isTimedOut();
	}

	@Override
    protected void end() {
		//used to end the PID Closed Loop
        //driveSubsystem.disable();
        driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
