package com.murdermaninc.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Benchmark {
	
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<ArrayList<Float>> average = new ArrayList<ArrayList<Float>>();
	
	private static ArrayList<String> namesLong = new ArrayList<String>();
	private static ArrayList<ArrayList<Long>> averageLong = new ArrayList<ArrayList<Long>>();

	public static void addBenchmarkFloat(String name, float data) {
		if(!names.contains(name)) {
			names.add(name);
			average.add(new ArrayList<Float>());
		}
		
		for(int i = 0; i < names.size(); i++) {
			if(names.get(i).equals(name)) {
				average.get(i).add(data);
			}
		}
	}
	
	public static void addBenchmarkLong(String name, long data) {
		if(!namesLong.contains(name)) {
			namesLong.add(name);
			averageLong.add(new ArrayList<Long>());
		}
		
		for(int i = 0; i < namesLong.size(); i++) {
			if(namesLong.get(i).equals(name)) {
				averageLong.get(i).add(data);
			}
		}
	}
	
	public static void calculateAndStoreAverage() {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Benchmark")));
			
			for(int i = 0; i < names.size(); i++) {
				
				float averageCalc = 0.0F;
				for(int j = 0; j < average.get(i).size(); j++) {
					averageCalc += average.get(i).get(j);
				}
				
				averageCalc /= average.get(i).size();
				
				bw.write(names.get(i) + " " + Float.toString(averageCalc) + " ms");
				bw.write("\r\n");
				
			}
			
			for(int i = 0; i < namesLong.size(); i++) {
				
				long averageCalc = 0;
				for(int j = 0; j < averageLong.get(i).size(); j++) {
					averageCalc += averageLong.get(i).get(j);
				}
				
				averageCalc /= averageLong.get(i).size();
				
				bw.write(namesLong.get(i) + " " + Float.toString(averageCalc) + " loops");
				bw.write("\r\n");
				
			}

			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
}
