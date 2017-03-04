package org.usfirst.frc.team4711.robot.subsystems;

import org.opencv.core.Point;

import edu.wpi.first.wpilibj.command.Subsystem;

public class RobotEyeSubsystem extends Subsystem{

	
	private static RobotEyeSubsystem instance;
	
	private RobotEyeSubsystem(){
		super("robotEyeSubsystem");
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static RobotEyeSubsystem getInstance() {
		if(instance == null)
			instance = new RobotEyeSubsystem();
		
		return instance;
	}
	
	public void turnOn(){
		
	}
	
	public void turnOff(){
		
	}
	
	public double getTargetCenterX(){
		return 0.0;
		
	}

}
