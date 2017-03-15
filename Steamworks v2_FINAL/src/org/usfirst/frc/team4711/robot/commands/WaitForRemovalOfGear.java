package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.GearSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForRemovalOfGear extends Command{
	private GearSubsystem gearSubsystem;
	
	public WaitForRemovalOfGear() {
		super("waitForRemovalOfGear");
		
		gearSubsystem = GearSubsystem.getInstance();
		
		setTimeout(10);
	}

	@Override
	protected boolean isFinished() {
		return !gearSubsystem.hasGear() || isTimedOut();
	}

}
