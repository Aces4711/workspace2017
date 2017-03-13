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
	}
	
	@Override
	protected void initialize() {
		driveSubsystem.arcadeDrive((isForward) ? 1.0 : -1.0, 0.0);

		sensorVoltages = new ArrayList<Double>();
	}
	
	@Override
	protected void execute() {
		if(sensorVoltages.size() < 100)
			sensorVoltages.add((isForward) ? driveSubsystem.getFrontSensorVoltage() : driveSubsystem.getBackSensorVoltage());
		else {
			double sum = 0.0;
			for(double voltage : sensorVoltages)
				sum += voltage;
			
			double distance = AnalogDistanceSensorLUT.calucateDistance(sum / sensorVoltages.size());
			
			sensorVoltages.clear();
		}

	}

	@Override
	protected boolean isFinished() {
		return driveSubsystem.onTarget();
	}

	@Override
    protected void end() {
        driveSubsystem.disable();
        driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
}
