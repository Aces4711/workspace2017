package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.PickupSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RunIntake extends Command {

	private PickupSubsystem pickupSubsystem;
	
	private double motorSpeed;
	
	public RunIntake(double motorSpeed) {
		super("aquireBalls");
		this.motorSpeed = motorSpeed;
		
		pickupSubsystem = PickupSubsystem.getInstance();
		requires(pickupSubsystem);
		
		if(motorSpeed <= 0)
			setTimeout(1);
	}
	
	@Override
	protected void initialize() {
		pickupSubsystem.setMotorSpeed(motorSpeed);
	}
	
	@Override
	protected boolean isFinished() {
		return (motorSpeed > 0) ? false : isTimedOut();
	}
	
	@Override
    protected void end() {
		pickupSubsystem.setMotorSpeed(0.0);
    }
	
	@Override
    protected void interrupted() {
        end();
    }

}
