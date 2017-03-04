package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.MotorSpeeds;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class RotateTo extends Command {
	private double angle;
	
	private DriveSubsystem driveSubsystem;
	
	public RotateTo(double angle) {
		super("RotateTo");
		
		this.angle = angle;
		
		driveSubsystem = DriveSubsystem.getInstance();
		requires(driveSubsystem);
	}
	
	@Override
	protected void initialize() {
		driveSubsystem.setRotateBy(angle);
		driveSubsystem.enable();
	}
	
	@Override
	protected void execute() {
		System.out.println("Rotation- (SetPoint, Position): (" + driveSubsystem.getSetpoint() + ", " 
							+ driveSubsystem.getPosition() +")");
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
