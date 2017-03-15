package org.usfirst.frc.team4711.robot.commands;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team4711.robot.config.AnalogDistanceSensorLUT;
import org.usfirst.frc.team4711.robot.config.LauncherSpeedLUT;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.LauncherSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RunLauncherBaseOnDistance extends Command {
	private LauncherSubsystem launchSubsystem;
	private DriveSubsystem driveSubsystem;
	
	private List<Double> sensorVoltages;
	
	public RunLauncherBaseOnDistance() {
		super("runLauncherBaseOnDistance");
		
		launchSubsystem = LauncherSubsystem.getInstance();
		requires(launchSubsystem);
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
		
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {

		sensorVoltages = new ArrayList<Double>();
		sensorVoltages.add(driveSubsystem.getFrontSensorVoltage());
	}
	
	@Override
	protected void execute() {
		if(sensorVoltages.size() < 3)
			sensorVoltages.add(driveSubsystem.getFrontSensorVoltage());
		else {
			double sum = 0.0;
			for(double voltage : sensorVoltages)
				sum += voltage;
			
			launchSubsystem.setLauncherRPM(LauncherSpeedLUT.calculateRPM(AnalogDistanceSensorLUT.calucateDistance(sum / sensorVoltages.size())));
			sensorVoltages.clear();
		}
			
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
