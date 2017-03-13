package org.usfirst.frc.team4711.robot.commands;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team4711.robot.config.AnalogDistanceSensorLUT;
import org.usfirst.frc.team4711.robot.config.LauncherSpeedLUT;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTo extends Command {
	
	private double distanceInches;
	private boolean isForward;
	
	private List<Double> sensorVoltages;
	
	private DriveSubsystem driveSubsystem;

	public DriveTo(double distanceInches, boolean isForward) {
		super("DriveTo");
		
		this.distanceInches = distanceInches;
		this.isForward = isForward;
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);

		setTimeout(30);	
	}
	
	@Override
	protected void initialize() {
		driveSubsystem.arcadeDrive((isForward) ? 1.0 : -1.0, 0.0);

		sensorVoltages = new ArrayList<Double>();
	}
	
	@Override
	protected void execute() {
		sensorVoltages.add((isForward) ? driveSubsystem.getFrontSensorVoltage() : driveSubsystem.getBackSensorVoltage());
	}

	@Override
	protected boolean isFinished() {
		if(isTimedOut())
			return true;
		
		if(sensorVoltages.size() >= 100) {
			double sum = 0.0;
			for(double voltage : sensorVoltages)
				sum += voltage;
			
			double distance = AnalogDistanceSensorLUT.calucateDistance(sum / sensorVoltages.size());
			if(distance > 0 && distance <= distanceInches)
				return true;
			
			sensorVoltages.clear();
		}
		
		return false;
	}

	@Override
    protected void end() {
        driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
