package com.murdermaninc.collectionBackground;

import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;

public class Time {
	
	private int[] Data;
	private Font font = new Font();
	
	public boolean timeBonus = false;
	private float totalTime;
	
	//This does not take into account the pause time
	
	
	public Time(Screen screen, LevelSequencing levelS, float endingTime, float thresholdTime){
		if(endingTime < thresholdTime){			
			timeBonus = true;
		}else{
			timeBonus = false;
		}
		
		totalTime = endingTime;
		levelS.saveTime((float) (Math.round(totalTime * 10.0) / 10.0));
		
		if(Data == null){
			if(timeBonus == false){
				Data = screen.loadData(0, 2, 6, 2, 4, "TimeSprite");
			}else{
				Data = screen.loadData(0, 0, 6, 2, 4, "TimeSprite");
			}
		}
		
	}
	
	public void render(Screen screen){

		screen.renderData(Data, 1140 + screen.screenX, 210 + screen.screenY, 6, 2, 4);
		
		font.drawText(screen, "Time:" + (Math.round(totalTime * 10.0) / 10.0) + "s", 1140 + (10 * 4) + screen.screenX, 210 + ((30 / 2) * 4) - (6 * 2) - (4 * 2) + screen.screenY, 2);
		 
		
	}

}
