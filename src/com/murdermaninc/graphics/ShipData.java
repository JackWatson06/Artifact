package com.murdermaninc.graphics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ShipData {

	public int playerRoom = 0;
	public int kingsChamber = 0;
	
	private String[] resoureData = new String[] {"playerRoom: ", "kingRoom: "};
	
	public ShipData() {
		File shipData = new File("ShipData");
		if(shipData.exists()) {
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(shipData));
				
	
				String currentString = br.readLine();		
				playerRoom = Integer.parseInt(currentString.substring(currentString.length() - 1));
				
				currentString = br.readLine();
				kingsChamber = Integer.parseInt(currentString.substring(currentString.length() - 1));
	
				
				br.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else {
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(shipData));
				
					
				bw.write(resoureData[0] + "0");
				bw.write("\n");
				bw.write(resoureData[1] + "0");
					
				
				bw.close();
				
			} catch (IOException e) {

				e.printStackTrace();
			}
			
		}
				
	}
	
	public void save(String room, int data){
		
		
		if(room.equals(resoureData[0])) playerRoom = data;
		if(room.equals(resoureData[1])) kingsChamber = data;
		
		
		File shipData = new File("ShipData");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(shipData));
			
			
				
			bw.write(resoureData[0] + Integer.toString(playerRoom));
			bw.write("\n");
			bw.write(resoureData[1] + Integer.toString(kingsChamber));
			
			bw.close();
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
}
