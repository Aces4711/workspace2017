package org.usfirst.frc.team4711.robot.config;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalogDistanceSensorLUT {
	
	public static Map<Double, Integer> lut = createMap();

	private static Map<Double, Integer> createMap(){
		Map<Double, Integer> map = new LinkedHashMap<Double, Integer>();
		map.put(2.5, 20);
		map.put(1.95, 30);
		map.put(1.55, 40);
		map.put(1.25, 50);
		map.put(1.1, 60);
		map.put(0.9, 70);
		map.put(0.85, 80);
		map.put(0.7, 90);
		map.put(0.65, 100);
		map.put(0.6, 110);
		map.put(0.55, 120);
		map.put(0.5, 130);
		map.put(0.45, 140);
		map.put(0.4, 150);
		return Collections.unmodifiableMap(map);
	}
	
	// returns inches
	public static double calucateDistance(double voltage){
		if(voltage < .4 || voltage > 2.5)
			return 0.0;
		
		Iterator<Double> keys = lut.keySet().iterator();
		double lowKey = keys.next();
		double highKey = lowKey;
		while(lowKey > voltage && keys.hasNext()){
			highKey = lowKey;
			lowKey = keys.next();
		}
		
		double differenceOfKeys = highKey - lowKey;
		double percent = (differenceOfKeys > 0 ) ? (voltage - lowKey) / differenceOfKeys : 0.0;
		
		double lowValue = lut.get(lowKey).doubleValue();
		double highValue = lut.get(highKey).doubleValue();
		
		double distanceCM = lowValue + percent*(highValue - lowValue);
		return distanceCM * .3937;
	}
}
