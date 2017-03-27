package org.usfirst.frc.team4711.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CenterPositionCommands extends CommandGroup {
	public CenterPositionCommands(){
		addSequential(new DriveFor(-77.5));
		addSequential(new WaitForRemovalOfGear());
		addSequential(new WaitCommand(5));
		addSequential(new DriveFor(36.0));
		addSequential(new RotateTo(90.0));
	}
}
