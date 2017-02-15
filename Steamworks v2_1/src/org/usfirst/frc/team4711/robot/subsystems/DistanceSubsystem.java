package org.usfirst.frc.team4711.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;

public class DistanceSubsystem implements ISubsystem {

	AnalogInput rangeFinderA;
	
	public DistanceSubsystem() {
		this.rangeFinderA = new AnalogInput(0);
		this.rangeFinderA.setOversampleBits(4);
		this.rangeFinderA.setAverageBits(2);
	}
	
	@Override
	public void autonomousPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void teleopPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean test() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean diagnostic() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getRange() {
		return rangeFinderA.getValue();
	}
	

}
