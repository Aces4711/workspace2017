package org.usfirst.frc.team4711.robot;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.subsystems.BallIntake;
import org.usfirst.frc.team4711.robot.subsystems.BallLauncher;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.ISubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RopeClimber;

import edu.wpi.first.wpilibj.Joystick;

public class Robot {
	
	public static Joystick joystick;
	
	// subsystems
	private ISubsystem ropeClimber;
	private ISubsystem ballIntake;
	private ISubsystem ballLauncher;
	private ISubsystem robotDrive;
	
    public void robotInit() {
    	System.out.println("on robotInit");
    	
    	Robot.joystick = new Joystick(IOMap.JOYSTICK_PORT);
    	
    	// subsystems
    	this.ropeClimber = new RopeClimber();
    	this.ballIntake = new BallIntake();
    	this.ballLauncher = new BallLauncher();
    	this.robotDrive = new DriveSubsystem();
    	
    }
    
    public void teleopPeriodic() {
    	this.robotDrive.teleopPeriodic();
    	
    	this.ropeClimber.teleopPeriodic();
    	this.ballIntake.teleopPeriodic();
    	this.ballLauncher.teleopPeriodic();
    }
}
