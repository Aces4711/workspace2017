package org.usfirst.frc.team4711.robot.config;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LauncherSpeedLUT {
	//key in inches, values in rpms
	public static Map<Integer, Integer> lut = createMap();

	private static Map<Integer, Integer> createMap(){
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		map.put(60, 3000);
		map.put(54, 2500);
		map.put(48, 1800);
		map.put(42, 1500);
		map.put(36, 1300);
		map.put(30, 1000);
		map.put(24, 800);
		return Collections.unmodifiableMap(map);
	}

	public static double calculateRPM(double distanceInches){
		if(distanceInches < 24 || distanceInches > 60)
			return 0.0;
		
		Iterator<Integer> keys = lut.keySet().iterator();
		int lowKey = keys.next();
		int highKey = lowKey;
		while(lowKey > distanceInches && keys.hasNext()){
			highKey = lowKey;
			lowKey = keys.next();
		}
		
		float differenceOfKeys = highKey - lowKey;
		double percent = (differenceOfKeys > 0 ) ? (distanceInches - lowKey) / differenceOfKeys : 0.0;
		
		double lowValue = lut.get(lowKey).doubleValue();
		double highValue = lut.get(highKey).doubleValue();
		
		return lowValue + percent*(highValue - lowValue);
	}

}
