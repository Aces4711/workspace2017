package org.usfirst.frc.team4711.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * ControllersHelper eliminates the need for classes to create copies of the xboxes.
 * It also automatically creates deadzones for the axes and provides xbox value mapping.
 * @author Bryan
 */
public class XboxHelper {
	private XboxHelper() {}
	
	private static Joystick driver;
//	private static Joystick shooter;
	
	public static final int
	A = 1,
	B = 2,
	X = 3,
	Y = 4,
	TRIGGER_LT = 5,
	TRIGGER_RT = 6,
	BACK = 7,
	START = 8,
	STICK_LEFT = 9,
	STICK_RIGHT = 10,
	
	AXIS_LEFT_Y = 1,
	AXIS_LEFT_X = 2,
	AXIS_RIGHT_Y = 3,
	AXIS_RIGHT_X = 4,
	AXIS_TRIGGER_LEFT = 5,
	AXIS_TRIGGER_RIGHT = 6;
	
	private static boolean hasInit = false;
	
	public static void init() {
		driver = Robot.dope_joystick.getXbox();
//		shooter = Robooi.xboxShooter;
	}
	/*
	public static double getShooterAxis(int axis) {
		if (!hasInit) {	init();	}
		return Math.abs(shooter.getRawAxis(axis)) > Math.abs(Constants.XBOX_AXIS_TOLERANCE) ? shooter.getRawAxis(axis) : 0;
	}
	
	public static boolean getShooterButton(int button) {
		if (!hasInit) {	init();	}
		return shooter.getRawButton(button);
	}
	
	public static double getShooterPOV() {
		if (!hasInit) {	init();	}
		return shooter.getPOV();
	} */
	
	public static double getDriverAxis(int axis) {
		if (!hasInit) {	init();	}
		return Math.abs(driver.getRawAxis(axis)) > Math.abs(0.05) ? driver.getRawAxis(axis) : 0;
	}
	
	public static boolean getDriverButton(int button) {
		if (!hasInit) {	init();	}
		return driver.getRawButton(button);
	}
	
	public static double getDriverPOV() {
		if (!hasInit) {	init();	}
		return driver.getPOV();
	}
	
}

