package com.murdermaninc.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StartData {

	public String startString;
	
	public StartData() {
		File file = new File("StartData");
		
		if(file.exists()) {
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				startString = br.readLine();
				
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else {
			
			startString = "room";
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				
				bw.write(startString);
				
				bw.flush();
				bw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void save(String saveString) {
		File file = new File("StartData");
		
		startString = saveString;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(startString);
			
			bw.flush();
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
