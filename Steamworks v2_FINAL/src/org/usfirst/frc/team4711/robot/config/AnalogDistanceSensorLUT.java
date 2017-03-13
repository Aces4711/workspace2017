package org.usfirst.frc.team4711.robot.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalogDistanceSensorLUT {
	
	public static Map<Double, Integer> lut = createMap();

	private static Map<Double, Integer> createMap(){
		Map<Double, Integer> map = new HashMap<Double, Integer>();
		map.put(0.0, 0);
		map.put(2.3, 10);
		map.put(2.8, 15);
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
}
