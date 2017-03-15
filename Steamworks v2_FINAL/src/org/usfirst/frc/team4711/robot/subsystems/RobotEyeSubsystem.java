package org.usfirst.frc.team4711.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4711.robot.config.AnalogDistanceSensorLUT;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.config.LauncherSpeedLUT;
import org.usfirst.frc.team4711.robot.vision.GripPipeline;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;

public class RobotEyeSubsystem extends Subsystem {

	private Thread visionThread;
	
	private CvSink cvSink;
	private CvSource cvSource;
	
	private GripPipeline gripPipeline;
	
	private Mat image;
	
	private static RobotEyeSubsystem instance;
	
	private RobotEyeSubsystem(){
		super("robotEyeSubsystem");

		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		camera.setBrightness(25);
		
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
	
	public void startVision(){
		visionThread = new Thread(() -> {
			Mat source = new Mat();
			
			List<Double> sensorVoltages = new ArrayList<Double>();
			double distance = -1.0;
			
			final Point recStart = new Point(IOMap.AIM_BOX_X, IOMap.AIM_BOX_Y);
			final Point recSize = new Point(IOMap.AIM_BOX_X+IOMap.AIM_BOX_WIDTH, IOMap.AIM_BOX_Y+IOMap.AIM_BOX_LENGHT);
			
			final Point textStart = new Point(IOMap.AIM_BOX_X, IOMap.AIM_BOX_Y + IOMap.AIM_BOX_LENGHT + 30);
			
			final Scalar white = new Scalar(255,255,255);
			final Scalar green = new Scalar(0,255,0);
			
			while(!Thread.interrupted()){
				if(cvSink.grabFrame(source)==0){
					cvSource.notifyError(cvSink.getError());
					continue;
				}
				
				Imgproc.rectangle(
						source, 
						recStart, 
						recSize, 
						white, 
						2);
				
				if(sensorVoltages.size() < 2)
					sensorVoltages.add(DriveSubsystem.getInstance().getFrontSensorVoltage());
				else {
					double sum = 0.0;
					for(double voltage : sensorVoltages)
						sum += voltage;
					distance = AnalogDistanceSensorLUT.calucateDistance(sum / sensorVoltages.size());
					sensorVoltages.clear();
				}
				
				Imgproc.putText(
						source, 
						"Distance : " + Math.round(distance) + " inches", 
						textStart, 
						Core.FONT_HERSHEY_COMPLEX, 
						.5, 
						(distance > 24 && distance < 60)? green : white,
						1);
				
				cvSource.putFrame(source);
			}
		});
		
		visionThread.start();
	}
	
	public void endVision() {
		if(visionThread.getState() == Thread.State.RUNNABLE)
			visionThread.interrupt();
	}
	
	public void turnOn(){
		//turn on lights
	}
	
	public void turnOff(){
		//turn off lights
	}
	
	//-1 <= value => 1, 0 been centered
	public double getTargetCenterX(){
		image = new Mat();
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
