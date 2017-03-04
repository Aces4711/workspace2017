package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.MotorSpeeds;
import org.usfirst.frc.team4711.robot.subsystems.LauncherSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class Launch extends Command {

	private LauncherSubsystem launchSubsystem;
	
	public Launch() {
		super("launch");
		
		launchSubsystem = LauncherSubsystem.getInstance();
		requires(launchSubsystem);
		
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		launchSubsystem.setLauncherRPM(MotorSpeeds.LAUNCHER_SPEED);
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
