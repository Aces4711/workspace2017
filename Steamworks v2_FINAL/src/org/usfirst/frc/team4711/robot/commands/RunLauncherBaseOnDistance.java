package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.LauncherSpeedLUT;

public class RunLauncherBaseOnDistance extends RunLauncher {

	private double distance;
	
	public RunLauncherBaseOnDistance(double distance) {
		super(LauncherSpeedLUT.calculateRPM(distance));
		
		this.distance = distance;
	}
	
	public double getDistance(){
		return distance;
	}
	
}
