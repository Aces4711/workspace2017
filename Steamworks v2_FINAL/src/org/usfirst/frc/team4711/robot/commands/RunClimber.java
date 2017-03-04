package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.ClimberSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RunClimber extends Command {
	
	private ClimberSubsystem climberSubsystem;
	
	private double motorSpeed;
	
	public RunClimber(double motorSpeed){
		super("runClimber");
		
		this.motorSpeed = motorSpeed;
		
		climberSubsystem = ClimberSubsystem.getInstance();
		requires(climberSubsystem);
		
		//safe guard to run for the longest 30 sec
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		climberSubsystem.setMotorSpeed(motorSpeed);
	}
	
	@Override
	protected boolean isFinished() {
		return isTimedOut();
	}
	
	@Override
    protected void end() {
		climberSubsystem.setMotorSpeed(0.0);
    }
	
	@Override
    protected void interrupted() {
        end();
    }

}
