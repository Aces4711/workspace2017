package org.usfirst.frc.team4711.robot.config;

public class IOMap {
	// PIDs - needs tuning!!!
	public static final double[] DRIVE_DISTANCE_PID = {.01, .001, .0};
	public static final double[] DRIVE_ANGLE_PID = {.5, .001, .0};
	// tun'd
	public static final double[] LAUNCHER_SPEED_PID = {.425, .001, .001, .065};//p i d f
	
	// drive
	/*
	public static final int FRONT_LEFT_MOTOR_CHANNEL = 0;
	public static final int REAR_LEFT_MOTOR_CHANNEL = 1;
	public static final int FRONT_RIGHT_MOTOR_CHANNEL = 2;
	public static final int REAR_RIGHT_MOTOR_CHANNEL = 3;
	*/
	
	// old channels
	public static final int FRONT_LEFT_MOTOR_CHANNEL = 4;
	public static final int REAR_LEFT_MOTOR_CHANNEL = 5;
	public static final int FRONT_RIGHT_MOTOR_CHANNEL = 7;
	public static final int REAR_RIGHT_MOTOR_CHANNEL = 6;
	

	public static final int GYRO_CHANNEL = 0;

	/*
	//in inches
	public static final int DRIVE_WHEEL_DIAMETER = 4;
	*/
	// old size
	public static final int DRIVE_WHEEL_DIAMETER = 3;
	
	
	// climber
	public static final int LEFT_CLIMB_CHANNEL = 4;
	public static final int RIGHT_CLIMB_CHANNEL = 5;
	
	// ball handling
	public static final int BALL_INTAKE_CHANNEL = 6;
	public static final int BALL_AUGGER = 7;
	//public static final int BALL_LAUNCH_CHANNEL = 8;
	public static final int BALL_LAUNCH_CHANNEL = 3;
	
	// wacky stuff
	public static final int JOYSTICK_PORT = 0;
	
	public static final int FRONT_SENSOR = 0;
	
	public static final int BACK_SENSOR = 1;
	
	public static final int AUTONOMOUS_DURATION = 15; // in seconds
	
	// controller junk
	public static final int A = 1;
	public static final int B = 2;
	public static final int X = 3;
	public static final int Y = 4;
	public static final int TRIGGER_LT = 5;
	public static final int TRIGGER_RT = 6;
	public static final int BACK = 7;
	public static final int START = 8;
	public static final int STICK_LEFT = 9;
	public static final int STICK_RIGHT = 10;
	
	public static final int AXIS_LEFT_Y = 1;
	public static final int AXIS_LEFT_X = 2;
	public static final int AXIS_RIGHT_Y = 3;
	public static final int AXIS_RIGHT_X = 4;
	public static final int AXIS_TRIGGER_LEFT = 5;
	public static final int AXIS_TRIGGER_RIGHT = 6;
	
	//robot eye
	public static final int CAMERA_IMG_WIDTH = 650;
	public static final int CAMERA_IMG_HEIGHT = 480;
}

