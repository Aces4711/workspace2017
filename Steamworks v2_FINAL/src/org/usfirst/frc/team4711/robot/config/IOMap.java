package org.usfirst.frc.team4711.robot.config;

public class IOMap {
	// PIDs - needs tuning!!!
	public static final double[] DRIVE_DISTANCE_PID = {.05, .001, .0};
	public static final double[] DRIVE_ANGLE_PID = {.05, .001, .0};
	// tun'd
	public static final double[] LAUNCHER_SPEED_PID = {.425, .001, .001, .065};//p i d f
	
	// drive
	
	public static final int FRONT_LEFT_MOTOR_CHANNEL = 1;
	public static final int REAR_LEFT_MOTOR_CHANNEL = 2;
	public static final int FRONT_RIGHT_MOTOR_CHANNEL = 3;
	public static final int REAR_RIGHT_MOTOR_CHANNEL = 4;
	

	public static final int GYRO_CHANNEL = 3;
	public static final double ROBOT_WIDTH = 31; // used instead of the gryo

	
	//in inches
	public static final int DRIVE_WHEEL_DIAMETER = 4;
	
	
	
	// climber
	public static final int LEFT_CLIMB_CHANNEL = 6;
	
	// ball handling
	public static final int BALL_INTAKE_CHANNEL = 7;
	public static final int BALL_AUGGER = 8;
	public static final int BALL_LAUNCH_CHANNEL = 5;
	
	// wacky stuff
	public static final int JOYSTICK_PORT = 0;
	
	//Distance sensors
	public static final int LEFT_DISTANCE_SENSOR = 0;
	public static final int RIGHT_DISTANCE_SENSOR = 1;
	public static final int SPACE_BETWEEN_DISTANCE_SENSORS = 24;
	public static final int SPACE_FROM_BUMPER_TO_DISTANCE_SENSORS = 8;
	
	//gear
	public static final int GEAR_DIO_SWITCH = 2;
	
	//start position switches
	public static final int LEFT_DIO_SWITCH = 0;
	public static final int RIGHT_DIO_SWITCH = 1;
	
	// controller junk
	public static final int A = 1;
	public static final int B = 2;
	public static final int X = 3;
	public static final int Y = 4;
	
	public static final int AXIS_TRIGGER_LT = 2;
	public static final int AXIS_TRIGGER_RT = 3;
	
	public static final int TRIGGER_LB = 5;
	public static final int TRIGGER_RB = 6;
	
	public static final int BACK = 7;
	public static final int START = 8;
	public static final int STICK_LEFT = 9;
	public static final int STICK_RIGHT = 10;
	
	public static final int AXIS_LEFT_Y = 1;
	public static final int AXIS_LEFT_X = 0;
	public static final int AXIS_RIGHT_Y = 5;
	public static final int AXIS_RIGHT_X = 4;
	

	public static final int CAMERA_FRONT = 1;
	public static final int CAMERA_BACK = 0;
	
	//only can use 160x120, 320x240, 640x480
	public static final int CAMERA_IMG_WIDTH = 320;
	public static final int CAMERA_IMG_HEIGHT = 240;
	
	//aim box of camera
	public static final int AIM_BOX_X = 50;
	public static final int AIM_BOX_Y = 50;
	public static final int AIM_BOX_WIDTH = 50;
	public static final int AIM_BOX_LENGHT = 50;
}

