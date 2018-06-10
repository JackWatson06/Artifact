package com.murdermaninc.decorations.mainShip;

import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class Sign extends DecorationShip{

	private String displayText = "";
	
	private boolean displaySign = false;
	private int displayCounter = 0;
	
	private Font font = new Font();
	
	private boolean smallDoor = false;
	private boolean signPosition = false;
	
	public Sign(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean smallDoor, boolean signPosition, String displayText) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.displayText = displayText;
		this.smallDoor = smallDoor;
		this.signPosition = signPosition;
	}

	@Override
	public void tick(){
		if(manager.player !=null){
			if(manager.input.e && manager.player.x + 8 <= x + 63 && manager.player.x + 63 - 8 >= x && manager.player.y + 4 <= y + 96 && manager.player.y + 63 - 4 >= y + 16){
				displaySign = true;
				manager.input.e = false;
			}
		}
		
		if(displaySign){
			if(displayCounter >= 180){
				displaySign = false;
				displayCounter = 0;
			}else{
				displayCounter++;
			}
		}
	}

	@Override
	public void render(Screen screen, float interpolation){
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		
		if(displaySign){
			if(!smallDoor && !signPosition){
				font.drawText(screen, displayText, x - 122 - (font.getTextLength(displayText, 3) / 2), y - 256, 3);
			}else if(smallDoor && !signPosition){
				font.drawText(screen, displayText, x - 86 - (font.getTextLength(displayText, 3) / 2), y - 160, 3);
			}else if(smallDoor && signPosition){
				font.drawText(screen, displayText, x + 64 + 86 - (font.getTextLength(displayText, 3) / 2), y - 160, 3);
			}else{
				font.drawText(screen, displayText, x + 64 + 122 - (font.getTextLength(displayText, 3) / 2), y - 256, 3);
			}
		}
		screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
	}


}
