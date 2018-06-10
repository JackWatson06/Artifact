package com.murdermaninc.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelData {
	
	private int levelData[];
	private int levelWidth;
	private int levelHeight;
	private int decorationData[];
	private int subArtifactData[] = new int[0];
	private int collectedArtifacts[] = new int[0];
	
	private int currentAmountOfSub = 0;
	private String subArtifactsCollected[];
	
	private int totalAmountOfSub = 0;
	
	// There is a need for a subArtifactName because when I read the file it is nice to know which subArtifact is refering to which and how many there are
	// and is also important in the level sequencing file so I know which artifact has been picked up
	
	public LevelData(String levelName){
		File levelSequencing = new File("LevelSequencing");
		if(levelSequencing.exists()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(levelSequencing));
				BufferedReader lineNumber = new BufferedReader(new FileReader(levelSequencing));
					try {
						int totalLines = 0;
						while(lineNumber.readLine() != null){
							totalLines++;
						}
						//System.out.println(levelName);
						
						for(int i = 0; i < totalLines; i++){
							if(br.readLine().equals(levelName)){
								br.readLine();
								br.readLine();
								br.readLine();
								break;
							}
						}

						currentAmountOfSub = Integer.parseInt(br.readLine());
						
						if(currentAmountOfSub > 0){
							subArtifactsCollected = new String[currentAmountOfSub];
							for(int i = 0; i < currentAmountOfSub; i++){
								subArtifactsCollected[i] = br.readLine();
							}
						}
						br.close();
						lineNumber.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
			}
		}
		File loadFile = new File("levels/" + levelName);
		if(loadFile.exists()){
			try {
				FileReader reader = new FileReader(loadFile);
				BufferedReader br = new BufferedReader(reader);
				try {
					levelWidth = Integer.parseInt(br.readLine());
					levelHeight = Integer.parseInt(br.readLine());
					levelData = new int[levelWidth * levelHeight];
					for(int i = 0; i < levelHeight; i++){
						ArrayList<String> currentLineIDs = new ArrayList<String>();
						String currentLine = br.readLine();
						String currentID = "";
						for(int j = 0; j < currentLine.length(); j++){
							if(currentLine.charAt(j) == ','){
								currentLineIDs.add(currentID);
								currentID = "";
							}else{
								currentID += currentLine.charAt(j);
							}
							//levelData[j + i * screenWidth] = Character.getNumericValue(currentLine.charAt(j));
						}
						for(int j = 0; j < levelWidth; j++){
							levelData[j + i * levelWidth] = Integer.parseInt(currentLineIDs.get(j));
						}
					}
					int subArtifactAmount = Integer.parseInt(br.readLine());
					totalAmountOfSub = subArtifactAmount;
					if(subArtifactAmount > 0){
						String[] subArtifactTotalNames = new String[subArtifactAmount];
						int[] subArtifactPreData = new int[subArtifactAmount * 3];
						for(int i = 0; i < subArtifactAmount; i++){
							subArtifactTotalNames[i] = br.readLine();
							for(int j = 0; j < 3; j++){
								subArtifactPreData[j + (3 * i)] = Integer.parseInt(br.readLine());
							}
						}	

						subArtifactData = new int[(subArtifactAmount - currentAmountOfSub) * 3];
						collectedArtifacts = new int[currentAmountOfSub];
						
						
						if(currentAmountOfSub > 0){
							int[] marks = new int[subArtifactAmount];
							
							for(int i = 0; i < subArtifactTotalNames.length; i++){
								for(int j = 0; j < subArtifactsCollected.length; j++){
									if(subArtifactTotalNames[i].equals(subArtifactsCollected[j])){
										marks[i] = 1;
									}
								}
							}
							
							int iValues = 0;
							int collection = 0;
							for(int i = 0; i < marks.length; i++){
								if(marks[i] == 0){
									subArtifactData[iValues * 3] = subArtifactPreData[i * 3];
									subArtifactData[iValues * 3 + 1] = subArtifactPreData[i * 3 + 1];
									subArtifactData[iValues * 3 + 2] = subArtifactPreData[i * 3 + 2];
									iValues++;
								}else{
									collectedArtifacts[collection] = subArtifactPreData[i * 3];
									collection++;
								}
							}
							

						}else{
							subArtifactData = subArtifactPreData;
						}
						
					}
					int decorationAmount = Integer.parseInt(br.readLine());
					if(decorationAmount > 0){
						decorationData = new int[decorationAmount * 3];
						for(int i = 0; i < decorationData.length; i+=3){
							decorationData[i] = Integer.parseInt(br.readLine());
							decorationData[i + 1] = Integer.parseInt(br.readLine());
							decorationData[i + 2] = Integer.parseInt(br.readLine());
						}
					}else{
						decorationData = new int[0]; 
					}
					
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
	
	public int[] getLevelData(){
		return levelData;
	}
	
	public int getLevelWidth(){
		return levelWidth;
	}
	
	public int getLevelHeight(){
		return levelHeight;
	}
	
	public int[] getDecorationData(){
		return decorationData;
	}
	
	public int[] getSubArtifact(){
		return subArtifactData;
	}

	public int getTotalAmountOfSub() {
		return totalAmountOfSub;
	}
	
	public int[] getCollectionArtifacts() {
		return collectedArtifacts;
	}

}
