package org.usfirst.frc.team4711.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class DropNShoot extends CommandGroup {
	public DropNShoot() {
		addSequential(new DriveFor(120.0));
		addSequential(new RotateTo(90.0));
		addSequential(new AimForTarget());//find gear target
		addSequential(new DriveTo(24.0, true));
		addSequential(new AimForTarget());//double checking
		addSequential(new RotateTo(180.0));
		addSequential(new DriveTo(7.0, false));//back in
		addSequential(new WaitForRemovalOfGear());
		addSequential(new WaitCommand(2));//wait for sure that gear has enough time to be removed
		addSequential(new AimForTarget());//find shooting target
		addSequential(new DriveTo(48.0, true));//get in shooting range
		addSequential(new AimForTarget());//double checking
		addSequential(new RunLauncherBaseOnDistance());
	}
}
