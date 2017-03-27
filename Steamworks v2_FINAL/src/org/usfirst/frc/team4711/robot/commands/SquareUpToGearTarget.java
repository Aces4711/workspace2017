package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4711.robot.subsystems.RobotEyeSubsystem.Target;

import edu.wpi.first.wpilibj.command.Command;

public class SquareUpToGearTarget extends Command {
	private static enum State {
		BACK_UP, ROTATE, DRIVE_FOR, AIM_FOR_TARGET, SQUARED_UP;
	}
	private State state;
	private Command command;
	
	private double targetAngle;
	
	private static final double BACKED_UP_DISTANCE = 24.0;
	
	public SquareUpToGearTarget(){
		super("squareUpToGearTarget");
		
		setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		state = State.AIM_FOR_TARGET;
	}
	
	@Override
	protected void execute() {
		if(command == null || !command.isRunning()){
			switch(state){
			case BACK_UP:
				targetAngle = getTargetAngle();
				if(Math.toDegrees(Math.abs(targetAngle)) < 5)
					state = State.SQUARED_UP;
				else {
					state = State.ROTATE;
					command = new RotateTo((targetAngle > 0)? 90.0 : -90.0);
					command.start();
				}
				break;
			case ROTATE:
				state = State.DRIVE_FOR;
				command = new DriveFor(-(BACKED_UP_DISTANCE * Math.tan(Math.abs(targetAngle))));
				command.start();
				break;
			case DRIVE_FOR:
				state = State.AIM_FOR_TARGET;
				command = new AimForTarget(Target.GEAR, targetAngle > 0);
				command.start();
			case AIM_FOR_TARGET:
				state = State.BACK_UP;
				command = new BackUpTo(BACKED_UP_DISTANCE);
				command.start();
			}
		}
	}
	
	@Override
	protected boolean isFinished() {
		return state == State.SQUARED_UP || isTimedOut();
	}
	
	@Override
    protected void end() {
		if(command.isRunning())
			command.cancel();
    }
	
	@Override
    protected void interrupted() {
        end();
    }
	
	private double getTargetAngle(){
		double leftSensor = DriveSubsystem.getInstance().getDistanceFromLeftSensor();
		double rightSensor = DriveSubsystem.getInstance().getDistanceFromRightSensor();
		
		leftSensor = (leftSensor < 0) ?  BACKED_UP_DISTANCE : leftSensor;
		rightSensor = (rightSensor < 0) ?  BACKED_UP_DISTANCE : rightSensor;
		
		double oppSide = leftSensor - rightSensor;
		double adjSide = IOMap.SPACE_BETWEEN_DISTANCE_SENSORS;
		
		return Math.atan2(oppSide, adjSide);
	}

}
