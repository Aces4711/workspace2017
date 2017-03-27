package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class RightPositionCommands extends CommandGroup {

	public RightPositionCommands(){
		addSequential(new DriveFor(-75.0));
		addSequential(new AimForTarget(RobotEyeSubsystem.Target.GEAR, false));
		addSequential(new SquareUpToGearTarget());
		addSequential(new BackUpTo(0.5));
		addSequential(new WaitForRemovalOfGear());
		addSequential(new WaitCommand(2));
		addSequential(new DriveFor(36.0));
		addSequential(new AimForTarget(RobotEyeSubsystem.Target.BASKET, false));
		addSequential(new RunLauncherBaseOnDistance(24.0));
	}
}
