package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.MotorSpeeds;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class LauncherSubsystem extends Subsystem {
	
	private CANTalon augger;
	private CANTalon launcherWithEncoder;
	
	private static LauncherSubsystem instance;
	
	private boolean readyOrNotHereICome = false;
	
	private LauncherSubsystem() {
		super("launcherSubsystem");
		
		augger = new CANTalon(IOMap.BALL_AUGGER);
				
		launcherWithEncoder = new CANTalon(IOMap.BALL_LAUNCH_CHANNEL);
		launcherWithEncoder.setEncPosition(launcherWithEncoder.getPulseWidthPosition() & 0xFFF);
		launcherWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		launcherWithEncoder.configEncoderCodesPerRev(1024);
		launcherWithEncoder.configNominalOutputVoltage(0.0, 0.0);
		launcherWithEncoder.configPeakOutputVoltage(0.0f, -12.0f);
		launcherWithEncoder.setP(IOMap.LAUNCHER_SPEED_PID[0]);
		launcherWithEncoder.setI(IOMap.LAUNCHER_SPEED_PID[1]);
		launcherWithEncoder.setD(IOMap.LAUNCHER_SPEED_PID[2]);
		launcherWithEncoder.setF(IOMap.LAUNCHER_SPEED_PID[3]);

		launcherWithEncoder.reverseSensor(true);
		launcherWithEncoder.enableControl();
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
		launcherWithEncoder.set(-rpm);
		//readyOrNotHereICome = true;
	}
	
	public boolean isLauncherReady(){
		return (launcherWithEncoder.getControlMode() == TalonControlMode.Speed) ? 
				Math.abs(launcherWithEncoder.getSetpoint() - launcherWithEncoder.getSpeed()) < 20 : true;
	}
	
	public void setLauncherSpeed(double moveValue){
		launcherWithEncoder.changeControlMode(TalonControlMode.PercentVbus);
		launcherWithEncoder.set(moveValue * MotorSpeeds.LAUNCHER_SPEED);
	}
	
	public void setAuggerSpeed(double moveValue){
		augger.set(moveValue * MotorSpeeds.AUGGER_SPEED);
	}
	
	public boolean isAuggerOn() {
		return augger.getSpeed() != 0;
	}

}
