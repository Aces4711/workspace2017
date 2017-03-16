package org.usfirst.frc.team4711.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4711.robot.config.AnalogDistanceSensorLUT;
import org.usfirst.frc.team4711.robot.config.IOMap;
import org.usfirst.frc.team4711.robot.vision.GripPipeline;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TestSubsystem extends Subsystem{
	
	private CANTalon talonWithEncoder;
	private AnalogInput distanceSensor;
	
	private double wheelCircumference = Math.PI * 7; // 21.98 inches;
	
	private Thread vision;
	private Thread visionPipeline;
	
	private static TestSubsystem instance;
	
	private TestSubsystem(){

		talonWithEncoder = new CANTalon(0);
		
		talonWithEncoder.setEncPosition(talonWithEncoder.getPulseWidthPosition() & 0xFFF);
		talonWithEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonWithEncoder.configEncoderCodesPerRev(1024);
		talonWithEncoder.enableControl();
		talonWithEncoder.reverseSensor(true);
		
		//good between .656168 - 4.92126 feet or 7.874016 - 59.05512 inches 
		//2.5 - .55 volts
		distanceSensor = new AnalogInput(0);
		
		vision = new Thread(()->{
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			//only can use 160x120, 320x240, 640x480
			camera.setResolution(320, 240);
			camera.setBrightness(25);
		
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource cvSource = CameraServer.getInstance().putVideo("Output", IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
			
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
					sensorVoltages.add(distanceSensor.getVoltage());
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
		
		visionPipeline = new Thread(()->{
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			//only can use 160x120, 320x240, 640x480
			camera.setResolution(320, 240);
			camera.setBrightness(25);
		
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource cvSource = CameraServer.getInstance().putVideo("Output", IOMap.CAMERA_IMG_WIDTH, IOMap.CAMERA_IMG_HEIGHT);
			
			GripPipeline gripPipeline = new GripPipeline();
			Mat source = new Mat();
			
			while(!Thread.interrupted()){
				if(cvSink.grabFrame(source)==0){
					cvSource.notifyError(cvSink.getError());
					continue;
				}

				gripPipeline.process(source);
				if (!gripPipeline.filterContoursOutput().isEmpty()){
					Rect r = Imgproc.boundingRect(gripPipeline.filterContoursOutput().get(0));
					Imgproc.rectangle(
							source, 
							new Point(r.x, r.y), 
							new Point(r.x + r.width, r.y + r.height), 
							new Scalar(255, 255, 255), 
							2);
				}
				cvSource.putFrame(source);
			}
		});
		
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub	
	}

	public static TestSubsystem getInstance(){
		if(instance == null)
			instance = new TestSubsystem();
		
		return instance;
	}
	
	public void startCamera(){
		/*
		if(!vision.isAlive())
			vision.start();
		*/
		if(!visionPipeline.isAlive())
			visionPipeline.start();
	}
	
	public void log(){
		String log = "out: " + talonWithEncoder.getOutputVoltage() / talonWithEncoder.getBusVoltage() +  "\n";
		log += "Speed: " + talonWithEncoder.getSpeed() +  ", Position: " + talonWithEncoder.getPosition() + "\n";
		log += "Error: " + talonWithEncoder.getError() +  "\n";
		log += "hasMoved(): " + hasMoved() + ", hasSpeed(): " + hasSpeed() + ", RPMs(10.0): " + caluclateRPMs(10.0) + "\n";
		log += "distanceVoltage(): " + distanceSensor.getVoltage() + ", distance: " + AnalogDistanceSensorLUT.calucateDistance(distanceSensor.getVoltage());
		System.out.println(log);
	}
	
	public void setMotorSpeed(double moveValue) {
		talonWithEncoder.changeControlMode(TalonControlMode.PercentVbus);
		talonWithEncoder.set(moveValue);
	}
	
	
	public void setMoveBy(double distanceInches){
		talonWithEncoder.changeControlMode(TalonControlMode.Position);
		
		talonWithEncoder.setVoltageRampRate(12.0);
		talonWithEncoder.configNominalOutputVoltage(0.0, 0.0);
		talonWithEncoder.configPeakOutputVoltage(2.0f, -2.0f);
		talonWithEncoder.setF(0.0);
		talonWithEncoder.setP(0.01);
		talonWithEncoder.setI(0.0);
		talonWithEncoder.setD(0.0);

		talonWithEncoder.setPosition(0.0);
		talonWithEncoder.setSetpoint(distanceInches / wheelCircumference);
	}
	
	public void setLauncherRPM(double rpm){
		
		talonWithEncoder.changeControlMode(TalonControlMode.Speed);

		talonWithEncoder.configNominalOutputVoltage(0.0, 0.0);
		talonWithEncoder.configPeakOutputVoltage(0.0f, -12.0f);
		talonWithEncoder.setF(.065);
		talonWithEncoder.setP(.425);
		talonWithEncoder.setI(.001);
		talonWithEncoder.setD(.001);
		
		talonWithEncoder.set(-rpm);
	}
	
	public boolean hasMoved(){
		return Math.abs(talonWithEncoder.getSetpoint() - talonWithEncoder.getPosition()) < .1;
	}
	
	public boolean hasSpeed(){
		return Math.abs(talonWithEncoder.getSetpoint() - talonWithEncoder.getSpeed()) < 20;
	}
	
	//not reliable going to use a lut
	private double caluclateRPMs(double changeInDistanceFeet){
		double angleDegrees = 80;
		double angleRadians = angleDegrees * (Math.PI / 180);
		
		double changeInHeightFeet = 9 - 1;
		double changeInHeightMeter = changeInHeightFeet / 3.2808;
		
		double changeInDistanceMeter = changeInDistanceFeet / 3.2808;
		
		double gravity = 9.8;
		
		double drag = .85;
		
		double velocityMPS = (1 / Math.cos(angleRadians)) *Math.sqrt((.5 * gravity * changeInDistanceMeter * changeInDistanceMeter) / ((changeInDistanceMeter * Math.tan(angleRadians)) + changeInHeightMeter));
		velocityMPS *= changeInDistanceMeter * drag;
		
		double velocityFPS = velocityMPS * 3.2808;
		
		double radiusOfWheelInches = 3.5;
		double radiusOfWheelFeet = radiusOfWheelInches / 12;
		
		return ((velocityFPS / radiusOfWheelFeet) / (2 * Math.PI)) * 60;
	}

}
