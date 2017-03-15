package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.LauncherSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RunLauncher extends Command {

	private LauncherSubsystem launchSubsystem;
	private double rpm;
	
	public RunLauncher(double rpm) {
		super("runLaunch");
		
		launchSubsystem = LauncherSubsystem.getInstance();
		requires(launchSubsystem);
		
		this.rpm = rpm;
		
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		launchSubsystem.setLauncherRPM(rpm);
	}
	
	@Override
	protected void execute() {
		launchSubsystem.setAuggerSpeed(launchSubsystem.isLauncherReady() ? 1.0 : 0.0);
	}
	
	@Override
	protected boolean isFinished() {
		return isTimedOut();
	}
	
	@Override
    protected void end() {
		launchSubsystem.setAuggerSpeed(0.0);
		launchSubsystem.setLauncherSpeed(0.0);
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
