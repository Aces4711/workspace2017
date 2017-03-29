package org.usfirst.frc.team4711.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class DropNShoot extends CommandGroup {
	public DropNShoot() {
		addSequential(new RunCenterOrSidePositions( new CenterPositionCommands(), 
													new RunLeftOrRightPositions(new RedLeftPositionCommands(), 
																				new BlueRightPositionCommands())));
	}
}
