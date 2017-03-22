package org.usfirst.frc.team4711.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
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

	private Thread visionThreadFront;
	private Thread visionThreadBack;
	
	private CvSink cvSinkFront;
	private CvSource cvSourceFront;
	
	private CvSink cvSinkBack;
	private CvSource cvSourceBack;
	
	private GripPipeline gripPipeline;
	
	private Mat imageBack;
	
	private double positionX;
	
	private static RobotEyeSubsystem instance;
	
	private RobotEyeSubsystem(){
		super("robotEyeSubsystem");

		UsbCamera cameraFront = CameraServer.getInstance().startAutomaticCapture(IOMap.CAMERA_FRONT);
		cameraFront.setResolution(IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		cameraFront.setBrightness(25);
		
		UsbCamera cameraBack = CameraServer.getInstance().startAutomaticCapture(IOMap.CAMERA_BACK);
		cameraBack.setResolution(IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		cameraBack.setBrightness(25);
		
		cvSinkFront = CameraServer.getInstance().getVideo(cameraFront);
		cvSourceFront = CameraServer.getInstance().putVideo("RobotEyeFront", IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		
		cvSinkBack = CameraServer.getInstance().getVideo(cameraBack);
		cvSourceBack = CameraServer.getInstance().putVideo("RobotEyeBack", IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
		
		imageBack = new Mat();
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
	
	public void startVisionFront(){
		visionThreadFront = new Thread(() -> {
			List<Double> sensorVoltages = new ArrayList<Double>();
			double distance = -1.0;
			
			final Point recStart = new Point(IOMap.AIM_BOX_X, IOMap.AIM_BOX_Y);
			final Point recSize = new Point(IOMap.AIM_BOX_X+IOMap.AIM_BOX_WIDTH, IOMap.AIM_BOX_Y+IOMap.AIM_BOX_LENGHT);
			
			final Point textStart = new Point(IOMap.AIM_BOX_X, IOMap.AIM_BOX_Y + IOMap.AIM_BOX_LENGHT + 30);
			final Point textStart2 = new Point(5, 5);
			
			final Scalar white = new Scalar(255,255,255);
			final Scalar green = new Scalar(0,255,0);
			
			while(!Thread.interrupted()){
				if(cvSinkFront.grabFrame(imageBack)==0){
					cvSourceFront.notifyError(cvSinkFront.getError());
					continue;
				}
				
				Imgproc.putText(
						imageBack, 
						"Front Camera:", 
						new Point(IOMap.AIM_BOX_X, 15), 
						Core.FONT_HERSHEY_COMPLEX, 
						.5, 
						white,
						1);
				
				Imgproc.rectangle(
						imageBack, 
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
						imageBack, 
						"Distance : " + Math.round(distance) + " inches", 
						textStart, 
						Core.FONT_HERSHEY_COMPLEX, 
						.5, 
						(distance > 24 && distance < 60)? green : white,
						1);
				//if (LauncherSubsystem.getInstance().isLauncherReady()) {
				//	Imgproc.putText(source, "Running augger, firing", textStart2, Core.FONT_HERSHEY_COMPLEX, .5, white);
				//}
				
				cvSourceFront.putFrame(imageBack);
			}
		});
		
		visionThreadFront.start();
	}
	
	public void endVisionFront() {
		if(visionThreadFront.getState() == Thread.State.RUNNABLE)
			visionThreadFront.interrupt();
	}
	
	public void startVisionBack(){
		visionThreadBack = new Thread(() -> {
			Mat source = new Mat();
			
			List<Double> sensorVoltages = new ArrayList<Double>();
			double distance = -1.0;
			
			final Point textStart = new Point(IOMap.AIM_BOX_X, IOMap.AIM_BOX_Y + IOMap.AIM_BOX_LENGHT + 30);
			final Point textStart2 = new Point(10, 10);
			
			while(!Thread.interrupted()){
				if(cvSinkBack.grabFrame(source)==0){
					cvSourceBack.notifyError(cvSinkBack.getError());
					continue;
				}
				
				gripPipeline.process(source);
				
				if (!gripPipeline.filterContoursOutput().isEmpty()){
					Rect r = new Rect(IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT, 0, 0);
					for(MatOfPoint matOfPoint : gripPipeline.filterContoursOutput()) {
						Rect temp = Imgproc.boundingRect(matOfPoint);
						r.x = Math.min(r.x, temp.x);
						r.y = Math.min(r.y, temp.y);
						r.width = Math.max(r.x + r.width, temp.x + temp.width) - r.x;
						r.height = Math.max(r.height + r.height, temp.x + temp.height) - r.y;
					}
					
					Imgproc.rectangle(
							source, 
							new Point(r.x,r.y), 
							new Point(r.x + r.width, r.y + r.height), 
							new Scalar(255, 255, 255), 
							2);
					
		            //may have to re-think this whole thing lol
		            double rectCenterX = r.x + (r.width / 2);
		            double camImgCenterX = IOMap.CAMERA_IMG_WIDTH / 2;
		            double distanceAway = (rectCenterX - camImgCenterX) / camImgCenterX;
		            double returnValue = 1 - Math.abs(distanceAway);
		            
		            positionX = (distanceAway > 0)? returnValue : -returnValue;
				}
				
				Imgproc.putText(
						source, 
						"Back Camera:", 
						new Point(IOMap.AIM_BOX_X, 15), 
						Core.FONT_HERSHEY_COMPLEX, 
						.5, 
						new Scalar(255,255,255),
						1);
				
				if(sensorVoltages.size() < 2)
					sensorVoltages.add(DriveSubsystem.getInstance().getBackSensorVoltage());
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
						new Scalar(255,255,255),
						1);
				
				Imgproc.putText(
						source,
						(LauncherSubsystem.getInstance().isAuggerOn() ? "Augger is ON" : "Augger is OFF"),
						textStart2,
						Core.FONT_HERSHEY_COMPLEX,
						.5,
						new Scalar(255,255,255),
						1);
				
				Imgproc.putText(
						source,
						"PositionX : " + positionX,
						new Point(textStart.x, textStart.y + 30),
						Core.FONT_HERSHEY_COMPLEX,
						.5,
						new Scalar(255,255,255),
						1);
				
				cvSourceBack.putFrame(source);
			}
		});
		
		visionThreadBack.start();
	}
	
	public void endVisionBack() {
		if(visionThreadBack.getState() == Thread.State.RUNNABLE)
			visionThreadBack.interrupt();
	}
	
	//-1 <= value => 1, 0 been centered
	public double getTargetCenterX(){
		return positionX;
		
	}

}
