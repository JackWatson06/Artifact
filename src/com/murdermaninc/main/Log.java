package com.murdermaninc.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log{

	public static void write(String writeToFile) {
		
		String path = new String("Log");
		
		File file = new File(path);
		
		
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			
			bw.write(writeToFile + "\r\n");
			
			bw.flush();
			bw.close();
			
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		
		
	}
	
	
	
}
