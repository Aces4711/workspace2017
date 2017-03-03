package org.usfirst.frc.team4711.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DropNShoot extends CommandGroup {
	public DropNShoot() {
		addSequential(new DriveTo(120.0));
		addSequential(new DriveTo(-120.0));
	}
}
