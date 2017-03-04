package org.usfirst.frc.team4711.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.vision.GripPipeline;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;

public class RobotEyeSubsystem extends Subsystem {

	private CvSink cvSink;
	private CvSource cvSource;
	
	private GripPipeline gripPipeline;
	
	private static RobotEyeSubsystem instance;
	
	private RobotEyeSubsystem(){
		super("robotEyeSubsystem");

		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		
		cvSink = CameraServer.getInstance().getVideo();
		cvSource = CameraServer.getInstance().putVideo("RobotEye", IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		
		gripPipeline = new GripPipeline();
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public static RobotEyeSubsystem getInstance() {
		if(instance == null)
			instance = new RobotEyeSubsystem();
		
		return instance;
	}
	
	public void turnOn(){
		//turn on lights
	}
	
	public void turnOff(){
		//turn off lights
	}
	
	//-1 <= value => 1, 0 been centered
	public double getTargetCenterX(){
		Mat image = new Mat();
		cvSink.grabFrame(image);
		gripPipeline.process(image);
		
		if (!gripPipeline.filterContoursOutput().isEmpty()){
            Rect r = Imgproc.boundingRect(gripPipeline.filterContoursOutput().get(0));
            //may have to re-think this whole thing lol
            double rectCenterX = r.x + (r.width / 2);
            double camImgCenterX = IOMap.CAMERA_IMG_WIDTH / 2;
            double distanceAway = (rectCenterX - camImgCenterX) / camImgCenterX;
            double returnValue = 1 - Math.abs(distanceAway);
            
            return (distanceAway > 0)? returnValue : -returnValue;
		}
		
		return 0.0;
		
	}

}
