package org.usfirst.frc.team4711.robot.commands;

import org.usfirst.frc.team4711.robot.config.MotorSpeeds;
import org.usfirst.frc.team4711.robot.subsystems.TestSubsystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
/*
 * Notes:
 * @ 3000 rpm = 110 - 115 inches away 
 * @ position = Missing by about a rotation and ahalf 
 */
public class RunTest extends Command {

	public static enum State { FREE, AUTO_MOVE_TO, AUTO_SPEED_TO }
	private State state;
	private double moveValue;

	private Joystick joystick;
	
	private TestSubsystem testSubsystem;
	
	private int count;
	
	public RunTest(State state, double moveValue){
		super("runTest");
		
		this.state = state;
		this.moveValue = moveValue;
		
		joystick = new Joystick(0);
		
		testSubsystem = TestSubsystem.getInstance();
		requires(testSubsystem);
		
		//setTimeout(30);
	}
	
	@Override
	protected void initialize() {
		switch(state){
		case FREE:
			break;
		case AUTO_MOVE_TO:
			testSubsystem.setMoveBy(moveValue);
			break;
		case AUTO_SPEED_TO:
			testSubsystem.setLauncherRPM(moveValue);
			break;
		}
		
		testSubsystem.startCamera();
	}
	
	@Override
	protected void execute() {
		if(state == State.FREE)
			//testSubsystem.setMotorSpeed(joystick.getAxis(AxisType.kY));
			
		if(++count > 100){
			testSubsystem.log();
			count = 0;
		}
		
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
    protected void end() {
		testSubsystem.setMotorSpeed(0.0);
    }
	
	@Override
    protected void interrupted() {
        end();
    }

}
