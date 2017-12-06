package com.murdermaninc.level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.murdermaninc.main.Main;

public class LevelSequencing{

	public final int totalLevels = 18;
	public final int totalWorlds = 5;
	//These should all be equal in length
	public boolean[] levelAvailability = new boolean[totalLevels];
	private String[] levelNames = new String[totalLevels];
	public int[] artifactsCollected = new int[totalLevels]; //This is for each per level no matter if the level was played or not, and is the total amount of artifacts currently collected
	public boolean[] openWorlds = new boolean[totalWorlds];
	public float[] levelTime = new float[totalLevels];
	public int[] levelPercentage = new int[totalLevels];
	
	private Main main;
	private Level level;
	
	//private ArrayList<String[]> artifactsCurCol = new ArrayList<String[]>();
	private String[][] artifactsCurCol = new String[totalLevels][];

	
	public LevelSequencing(Main main){
		this.main = main;
	}
	
	public LevelSequencing(){
		
	}
	
	
	public void levelSequence(Level level){
		String currentLevel = level.name;
		
		int worldNumber = 0;
		int levelNumber = 0;
		
		int middleIndex = 0;
		for(int i = 0; i < currentLevel.length(); i++){
			if(currentLevel.charAt(i) == '-'){
				worldNumber = Integer.parseInt(currentLevel.substring(5, i));
				middleIndex = i;
			}
			if(i == currentLevel.length() - 1){
				levelNumber = Integer.parseInt(currentLevel.substring(middleIndex + 1));
			}
		}
		
		//This is where the level is switched and a new level is loaded.
		
		main.removeSpriteSheets();

		
		if(worldNumber == 1){
			if(levelNumber == 1) main.loadLevel("Level1-2", 2, 1);
			if(levelNumber == 2) main.loadLevel("Level1-3", 3, 1);
			if(levelNumber == 3) main.loadLevel("Level1-4", 4, 1);
			if(levelNumber == 4) main.loadLevel("Level1-5", 5, 1);
			if(levelNumber == 5) main.loadLevel("Level1-6", 6, 1);
			if(levelNumber == 6) main.loadLevel("Level1-7", 7, 1);
			if(levelNumber == 7) main.loadLevel("Level1-8", 8, 1);
			if(levelNumber == 8) main.loadLevel("Level1-9", 9, 1);
			if(levelNumber == 9) main.loadLevel("Level1-10", 10, 1);
			if(levelNumber == 10) main.loadLevel("Level1-11", 11, 1);
			if(levelNumber == 11) main.loadLevel("Level1-12", 12, 1);
			if(levelNumber == 12) main.loadLevel("Level1-13", 13, 1);
			if(levelNumber == 13) main.loadLevel("Level1-14", 14, 1);
			if(levelNumber == 14) main.loadLevel("Level1-15", 15, 1);
			if(levelNumber == 15) main.loadLevel("Level1-16", 16, 1);
			if(levelNumber == 16) main.mainShipWorld = true;
			
		}else if(worldNumber == 2){
			
		}
	}
	
	public void artifactFound(String artifactName, int xTile, int yTile, int widthPixels, int heightPixels, int artifactBarPosition){
		for(int i = 0; i < levelNames.length; i++){
			if(level.name.equals(levelNames[i])){
				artifactsCollected[i] = artifactsCollected[i] + 1;
				String[] artifactArray = new String[artifactsCollected[i]];
				if(artifactArray.length > 1){
					String[] currentArtifactArray = artifactsCurCol[i];
					for(int j = 0; j < currentArtifactArray.length; j++){
						artifactArray[j] = currentArtifactArray[j];
					}
				}
				artifactArray[artifactArray.length - 1] = artifactName;
				artifactsCurCol[i] = artifactArray;
			}
		}
		level.updateBar(xTile, yTile, artifactBarPosition, widthPixels, heightPixels);
		//saveLevelSequence();
	}
	
	public void mainArtifactFound(){
		for(int i = 0; i < levelNames.length; i++){
			if(level.name.equals(levelNames[i])){
				if(i < 16){
					levelAvailability[i + 1] = true;
				}
				
				if(i == 15){
					openWorlds[1] = true;
				}
				
			}
		}
		main.artifactMenu = true;
		saveLevelSequence();
	}
	
	public void saveTime(float time){
		for(int i = 0; i < levelNames.length; i++){
			if(level.name.equals(levelNames[i])){
				if(levelTime[i] > time || levelTime[i] == 0.0){
					levelTime[i] = time;
				}
			}
		}
		saveLevelSequence();
	}
	
	public void savePercentage(int percentage){
		for(int i = 0; i < levelNames.length; i++){
			if(level.name.equals(levelNames[i])){
				if(levelPercentage[i] < percentage){
					levelPercentage[i] = percentage;
				}
			}
		}
		saveLevelSequence();
	}
	
	public float getTime(int levelNumber, int worldNumber){
		if(worldNumber == 1){
			if(levelNumber == 1) return 13.0F;
			if(levelNumber == 2) return 14.0F;
			if(levelNumber == 3) return 25.0F;
			if(levelNumber == 4) return 45.0F;
			if(levelNumber == 5) return 35.0F;
			if(levelNumber == 6) return 35.0F;
			if(levelNumber == 7) return 50.0F;
			if(levelNumber == 8) return 35.0F;
			if(levelNumber == 9) return 55.0F;
			if(levelNumber == 10) return 55.0F;
			if(levelNumber == 11) return 80.0F;
			if(levelNumber == 12) return 50.0F;
			if(levelNumber == 13) return 50.0F;
			if(levelNumber == 14) return 65.0F;
			if(levelNumber == 15) return 100.0F;
			if(levelNumber == 16) return 80.0F;
		}else if(worldNumber == 2){
			
		}
		return 0.0F;
	}
	
	public int getLevelArtifacts(int levelNumber, int worldNumber){
		if(worldNumber == 1){
			if(levelNumber == 1) return 2;
			if(levelNumber == 2) return 4;
			if(levelNumber == 3) return 2;
			if(levelNumber == 4) return 3;
			if(levelNumber == 5) return 4;
			if(levelNumber == 6) return 2;
			if(levelNumber == 7) return 3;
			if(levelNumber == 8) return 3;
			if(levelNumber == 9) return 3;
			if(levelNumber == 10) return 2;
			if(levelNumber == 11) return 4;
			if(levelNumber == 12) return 3;
			if(levelNumber == 13) return 3;
			if(levelNumber == 14) return 3;
			if(levelNumber == 15) return 2;
			if(levelNumber == 16) return 5;
		}else if(worldNumber == 2){
			
		}
		return 0;
	}
	
	public Level getCurrentLevel(){
		return level;
	}
	
	public void setLevel(Level level){
		this.level = level;
	}
	
	public void loadLevelSequence(){
		File levelSeq = new File("LevelSequencing");
		
		// Makes a new file if the levelSequencing file does not exist
		if(!levelSeq.exists()){
			try {
				openWorlds[0] = true;
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(levelSeq));
				
				bw.write("{");
				bw.write("\n");
				
				for(int i = 0; i < totalLevels; i++){
					bw.write("Level1-" + (i + 1));
					bw.write("\n");
					if(i == 0){
						bw.write("True");
					}else{
						bw.write("False");
					}
					bw.write("\n");
					bw.write("0.0");
					bw.write("\n");
					bw.write("0");
					bw.write("\n");
					bw.write("0");
					bw.write("\n");
				}
				
				bw.write("}");
				bw.write("\n");
				bw.write("{");
				bw.write("\n");
				
				for(int i = 0; i < openWorlds.length; i++){
					bw.write(String.valueOf(openWorlds[i]));
					bw.write("\n");
					
				}
				
				bw.write("}");
				
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//This loads the data from the levelSequencing file
		try{
			BufferedReader br = new BufferedReader(new FileReader(levelSeq));
			try{
				//artifactsCurCol.clear();
				br.readLine();
				for(int i = 0; i < totalLevels; i++){
					levelNames[i] = br.readLine();
					levelAvailability[i] = Boolean.parseBoolean(br.readLine());
					levelTime[i] = Float.parseFloat(br.readLine());
					levelPercentage[i] = Integer.parseInt(br.readLine());
					artifactsCollected[i] = Integer.parseInt(br.readLine());
					if(artifactsCollected[i] > 0){
						String[] levelArt = new String[artifactsCollected[i]];
						for(int j = 0; j < levelArt.length; j++){
							levelArt[j] = br.readLine();
						}
						artifactsCurCol[i] = levelArt;
					}
				}
				br.readLine();
				br.readLine();
				
				for(int i = 0; i < openWorlds.length; i++){
					openWorlds[i] = Boolean.parseBoolean(br.readLine());
				}
				
				br.close();
			}catch(IOException e){
				e.printStackTrace();
			}

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		/*for(int i = 0; i < levelAvailability.length; i++){
			System.out.println(levelAvailability[i]);
		}
		
		for(int i = 0; i < levelNames.length; i++){
			System.out.println(levelNames[i]);
		}
		
		for(int i = 0; i < artifactsCollected.length; i++){
			System.out.println(artifactsCollected[i]);
		}
		
		for(int i = 0; i < artifactsCurCol.length; i++){
			if(artifactsCurCol[i] != null){
				for(int j = 0; j < artifactsCurCol[i].length; j++){
					System.out.println(artifactsCurCol[i][j]);
				}
			}
		}*/
	}
	
	public void saveLevelSequence(){
		
		//This block saves the current progress through the level when the level finishes and also when an artifact is pickup
		
		
		File file = new File("LevelSequencing");
		
		try{
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.write("{");
			bw.write("\n");
			
			for(int i = 0; i < totalLevels; i++){
				bw.write(levelNames[i]);
				bw.write("\n");
				bw.write(String.valueOf(levelAvailability[i]));
				bw.write("\n");
				bw.write(String.valueOf(levelTime[i]));
				bw.write("\n");
				bw.write(String.valueOf(levelPercentage[i]));
				bw.write("\n");
				bw.write(String.valueOf(artifactsCollected[i]));
				bw.write("\n");
				if(artifactsCollected[i] > 0){
					for(int j = 0; j < artifactsCollected[i]; j++){
						bw.write(artifactsCurCol[i][j]);
						bw.write("\n");
					}
				}
				
			}	
			
			bw.write("}");
			bw.write("\n");
			bw.write("{");
			bw.write("\n");
			
			for(int i = 0; i < openWorlds.length; i++){
				bw.write(String.valueOf(openWorlds[i]));
				bw.write("\n");
				
			}
			
			bw.write("}");
			
			bw.flush();
			bw.close();
			
		}catch(IOException e){
			
			
		}
		
	}
}
