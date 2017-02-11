package org.usfirst.frc.team4711.robot.subsystems;

public interface ISubsystem {
	public void autonomousPeriodic();
	public void teleopPeriodic();
	
	public void start();
	public void stop();
	public boolean test();
	public boolean diagnostic();
}
