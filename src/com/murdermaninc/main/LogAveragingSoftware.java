package com.murdermaninc.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LogAveragingSoftware {
	
	private ArrayList<Float> renderTime = new ArrayList<Float>();
	private ArrayList<Float> showTime = new ArrayList<Float>();
	private ArrayList<Float> renderMethod = new ArrayList<Float>();
	private ArrayList<Float> levelScaled = new ArrayList<Float>();
	private ArrayList<Float> levelNotScaled = new ArrayList<Float>();
	private ArrayList<Float> shipScaled = new ArrayList<Float>();
	private ArrayList<Float> shipNotScaled = new ArrayList<Float>();
	
	private ArrayList<Float> background = new ArrayList<Float>();
	private ArrayList<Float> backgroundRender = new ArrayList<Float>();
	private ArrayList<Float> everythingElseRender = new ArrayList<Float>();
	
	public LogAveragingSoftware() {
		
		loadAveragingData();
		
		saveInFile();
		
	}
	
	public void loadAveragingData() {
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(new File("Log")));
			
			String currentString = "";
			
			while((currentString = br.readLine()) != null) {
				
					if(currentString.length() >= 26 && currentString.substring(0, 26).equals("|||Drawing to Screen (ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(27, currentString.length()));
					
						
						showTime.add(currentNumber);
						
					}else if(currentString.length() >= 20 && currentString.substring(0, 20).equals("|||Render Time (ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(21, currentString.length()));
						
						renderTime.add(currentNumber);
						
					}else if(currentString.length() >= 26 && currentString.substring(0, 26).equals("|||Draw Render Method(ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(27, currentString.length()));
						
						renderMethod.add(currentNumber);
						
					}else if(currentString.length() >= 31 && currentString.substring(0, 31).equals("|||Draw Image Level Scaled(ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(32, currentString.length()));
						
						levelScaled.add(currentNumber);
						
					}else if(currentString.length() >= 24 && currentString.substring(0, 24).equals("|||Draw Image Level(ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(25, currentString.length()));
						
						levelNotScaled.add(currentNumber);
						
					}else if(currentString.length() >= 30 && currentString.substring(0, 30).equals("|||Draw Image Ship Scaled(ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(31, currentString.length()));
						
						shipScaled.add(currentNumber);
						
					}else if(currentString.length() >= 23 && currentString.substring(0, 23).equals("|||Draw Image Ship(ms):")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(24, currentString.length()));
						
						shipNotScaled.add(currentNumber);
						
					}else if(currentString.length() >= 14 && currentString.substring(0, 14).equals("|||Background:")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(15, currentString.length()));
						
						background.add(currentNumber);
						
					}else if(currentString.length() >= 21 && currentString.substring(0, 21).equals("|||Background Render:")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(22, currentString.length()));
						
						backgroundRender.add(currentNumber);
						
					}else if(currentString.length() >= 14 && currentString.substring(0, 14).equals("|||Everything:")) {
						
						
						float currentNumber = Float.parseFloat(currentString.substring(15, currentString.length()));
						
						everythingElseRender.add(currentNumber);
						
					}
				
			}
			
			br.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		
	}
	
	
	public void saveInFile() {
		
		float renderAverage = average(renderTime);
		
		float showAverage = average(showTime);
		
		float methodAverage =  average(renderMethod);
		
		float levelAverage = average(levelNotScaled);
		
		float levelScaledAverage = average(levelScaled);
		
		float shipAverage = average(shipNotScaled);
		
		float shipScaledAverage = average(shipScaled);
		
		
		
		float backgroundAverage = average(background);
		float backgroundRenderAverage = average(backgroundRender);
		float everythingRender = average(everythingElseRender);

		
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Log Average")));
			
			bw.write("Render Method Average: " + Float.toString(methodAverage) + "\r\n");
			
			
			//This reason some of these are included even though they could be zero is that
			//they can tell A. if anything wierd is happening and B. what regions of the game the player was in
			
			bw.write("Level Not Scaled Average: " + Float.toString(levelAverage) + "\r\n");
			
			bw.write("Level Scaled Average: " + Float.toString(levelScaledAverage) + "\r\n");
			
			bw.write("Ship Not Scaled Average: " + Float.toString(shipAverage) + "\r\n");
			
			bw.write("Ship Scaled Average: " + Float.toString(shipScaledAverage) + "\r\n");
			
			bw.write("Background Shiut: " + Float.toString(backgroundAverage) + "\r\n");
			
			bw.write("Background Render: " + Float.toString(backgroundRenderAverage) + "\r\n");
			
			bw.write("Everything Else Render: " + Float.toString(everythingRender) + "\r\n");
			
			bw.write("Render Time Average: " + Float.toString(renderAverage) + "\r\n");
			
			bw.write("Show Time Average: " + Float.toString(showAverage) + "\r\n");
			
			bw.write("Total Time: " + Float.toString(renderAverage + showAverage));
			
			bw.flush();
			bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public float average(ArrayList<Float> array) {
		
		float average = 0.0F;
		
		for(int i = 0; i < array.size(); i++) { average += array.get(i); }
		
		if(array.size() != 0) {
			average = average / array.size();
		}
		
		return average;
		
	}
	
	
	public static void main(String[] args) {
		
		
		new LogAveragingSoftware();
	}

}
