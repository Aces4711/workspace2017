package org.usfirst.frc.team4711.robot.commands;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team4711.robot.config.AnalogDistanceSensorLUT;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTo extends Command {
	
	private double distanceInches;
	private boolean isForward;
	
	private List<Double> sensorDistances;
	
	private DriveSubsystem driveSubsystem;

	public DriveTo(double distanceInches, boolean isForward) {
		super("DriveTo");
		
		this.distanceInches = distanceInches;
		this.isForward = isForward;
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);

		setTimeout(3);	
	}
	
	@Override
	protected void initialize() {
		sensorDistances = new ArrayList<Double>();
	}
	
	@Override
	protected void execute() {
		driveSubsystem.driveStraight((isForward) ? 0.7 : -.7);
		double distance = AnalogDistanceSensorLUT.calucateDistance((isForward) ? driveSubsystem.getFrontSensorVoltage() : driveSubsystem.getBackSensorVoltage());
		if(distance > 0)
			sensorDistances.add(distance);
	}

	@Override
	protected boolean isFinished() {
		if(isTimedOut())
			return true;
		
		if(sensorDistances.size() >= 3) {
			double sum = 0.0;
			for(double distance : sensorDistances)
				sum += distance;
			
			double mean = sum / sensorDistances.size();
			
			if(mean > 0 && mean <= distanceInches)
				return true;
			
			sensorDistances.clear();
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
