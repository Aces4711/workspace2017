package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LauncherSubsystem extends Subsystem {
	
	private CANTalon augger;
	private CANTalon launcherWithEncoder;
	
	private static LauncherSubsystem instance;
	
	public LauncherSubsystem() {
		super("launcherSubsystem");
		
		augger = new CANTalon(IOMap.BALL_AUGGER);
		augger.configNominalOutputVoltage(0.0, 0.0);
		augger.configPeakOutputVoltage(12.0 * MotorSpeeds.AUGGER_SPEED, -12.0 * MotorSpeeds.AUGGER_SPEED);
		augger.setVoltageRampRate(6);
				
		launcherWithEncoder = new CANTalon(IOMap.BALL_LAUNCH_CHANNEL);
		launcherWithEncoder.setEncPosition(launcherWithEncoder.getPulseWidthPosition() & 0xFFF);
		launcherWithEncoder.setFeedbackDevice(FeedbackDevice.PulseWidth);
		launcherWithEncoder.setPID(IOMap.LAUNCHER_SPEED_PID[0], IOMap.LAUNCHER_SPEED_PID[1], IOMap.LAUNCHER_SPEED_PID[2]);
		launcherWithEncoder.setVoltageRampRate(12.0);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static LauncherSubsystem getInstance(){
		if(instance == null)
			instance = new LauncherSubsystem();
		
		return instance;
	}
	
	public void setLauncherRPM(double rpm){
		launcherWithEncoder.changeControlMode(TalonControlMode.Speed);
		launcherWithEncoder.set(rpm);
		launcherWithEncoder.enableControl();
	}
	
	public boolean isLauncherReady(){
		return (launcherWithEncoder.getControlMode() == TalonControlMode.Speed) ? 
				Math.abs(launcherWithEncoder.getSetpoint() - launcherWithEncoder.getPosition()) < .1 : true;
	}
	
	public void setLauncherSpeed(double moveValue){
		launcherWithEncoder.disableControl();
		launcherWithEncoder.changeControlMode(TalonControlMode.PercentVbus);
		launcherWithEncoder.set(moveValue);
	}
	
	public void setAuggerSpeed(double moveValue){
		augger.set(moveValue);
	}

}
