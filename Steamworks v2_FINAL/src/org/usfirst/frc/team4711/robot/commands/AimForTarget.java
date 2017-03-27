package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem.Target;

import edu.wpi.first.wpilibj.command.Command;

public class AimForTarget extends Command {
	
	private boolean cw;
	private Target target;
	
	private RobotEyeSubsystem robotEyeSubsystem;
	private DriveSubsystem driveSubsystem;
	
	public AimForTarget(Target target, boolean cw){
		super("aimForGearTarget");
		
		this.cw = cw;
		this.target = target;
		
		robotEyeSubsystem = RobotEyeSubsystem.getInstance();
		requires(robotEyeSubsystem);
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
		
		setTimeout(5);
	}
	
	@Override
	protected void initialize() {
		switch(target){
		case GEAR:
			robotEyeSubsystem.startVisionBack();
			break;
		case BASKET:
			robotEyeSubsystem.startVisionFront();
			break;
		}
	}
	
	@Override
	protected void execute() {
		driveSubsystem.turnOnAxis((cw)? 1.0 : -1.0);
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(robotEyeSubsystem.getTargetCenterX()) <= .02 || isTimedOut();
	}
	
	@Override
    protected void end() {
		switch(target){
		case GEAR:
			robotEyeSubsystem.endVisionBack();
			break;
		case BASKET:
			robotEyeSubsystem.endVisionFront();
			break;
		}
		driveSubsystem.stop();
    }
	
	@Override
    protected void interrupted() {
        end();
    }

}
