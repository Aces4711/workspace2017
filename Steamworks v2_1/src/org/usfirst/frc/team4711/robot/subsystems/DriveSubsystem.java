package org.usfirst.frc.team4711.robot.subsystems;

import org.usfirst.frc.team4711.robot.Robot;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.KeyMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;

public class DriveSubsystem extends RobotDrive implements ISubsystem {
	public DriveSubsystem() {

		super(new CANTalon(IOMap.FRONT_LEFT_MOTOR_CHANNEL),
				new CANTalon(IOMap.REAR_LEFT_MOTOR_CHANNEL),
    			new CANTalon(IOMap.FRONT_RIGHT_MOTOR_CHANNEL),
    			new CANTalon(IOMap.REAR_RIGHT_MOTOR_CHANNEL));
		
	}

	@Override
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
	    // local variables to hold the computed PWM values for the motors
	    if (!kArcadeStandard_Reported) {
	      HAL.report(tResourceType.kResourceType_RobotDrive, getNumMotors(),
	          tInstances.kRobotDrive_ArcadeStandard);
	      kArcadeStandard_Reported = true;
	    }

	    double leftMotorSpeed;
	    double rightMotorSpeed;

	    moveValue = limit(moveValue);
	    rotateValue = limit(rotateValue);

	    if (squaredInputs) {
	      // square the inputs (while preserving the sign) to increase fine control
	      // while permitting full power
	      if (moveValue >= 0.0) {
	        moveValue = moveValue * moveValue;
	      } else {
	        moveValue = -(moveValue * moveValue);
	      }
	      if (rotateValue >= 0.0) {
	        rotateValue = rotateValue * rotateValue;
	      } else {
	        rotateValue = -(rotateValue * rotateValue);
	      }
	    }

	    if (moveValue > 0.0) {
	      if (rotateValue > 0.0) {
	        leftMotorSpeed = moveValue - rotateValue;
	        rightMotorSpeed = Math.max(moveValue, rotateValue);
	      } else {
	        leftMotorSpeed = Math.max(moveValue, -rotateValue);
	        rightMotorSpeed = moveValue + rotateValue;
	      }
	    } else {
	      if (rotateValue > 0.0) {
	        leftMotorSpeed = -Math.max(-moveValue, rotateValue);
	        rightMotorSpeed = moveValue + rotateValue;
	      } else {
	        leftMotorSpeed = moveValue - rotateValue;
	        rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
	      }
	    }

	    if (!(Robot.joystick.getRawButton(KeyMap.AIM) && moveValue > 0)) {
	    	// EDITED
	    	setLeftRightMotorOutputs(-leftMotorSpeed, -rightMotorSpeed);
	    }
	}
	
	@Override
	public void autonomousPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void teleopPeriodic() {
		this.arcadeDrive(Robot.joystick.getY(), Robot.joystick.getX(), true);
		
		
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
		
		return true;
	}

	@Override
	public boolean diagnostic() {
		// TODO Auto-generated method stub
		
		return true;
	}

}
