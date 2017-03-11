package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.MotorSpeeds;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.LauncherSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RunLauncher extends Command {

	private LauncherSubsystem launchSubsystem;
	private DriveSubsystem driveSubsystem;
	
	public RunLauncher() {
		super("runLaunch");
		
		launchSubsystem = LauncherSubsystem.getInstance();
		requires(launchSubsystem);
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
		
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		launchSubsystem.setLauncherRPM(RunLauncher.caluclateRPMs(driveSubsystem.getFrontDistance()));
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

	private static double caluclateRPMs(double changeInDistanceFeet){
		double angleDegrees = 81;
		double angleRadians = angleDegrees * (Math.PI / 180);
		
		double changeInHeightFeet = 9 - 1;
		double changeInHeightMeter = changeInHeightFeet / 3.2808;
		
		double changeInDistanceMeter = changeInDistanceFeet / 3.2808;
		
		double gravity = 9.8;
		
		double drag = .01;
		
		double velocityMPS = (1 / Math.cos(angleRadians)) *Math.sqrt((.5 * gravity * changeInDistanceMeter * changeInDistanceMeter) / ((changeInDistanceMeter * Math.tan(angleRadians)) + changeInHeightMeter));
		velocityMPS += changeInDistanceMeter * drag;
		
		double velocityFPS = velocityMPS * 3.2808;
		
		double radiusOfWheelInches = 3.5;
		double radiusOfWheelFeet = radiusOfWheelInches / 12;
		
		return ((velocityFPS / radiusOfWheelFeet) / (2 * Math.PI)) * 60;
	}
}
