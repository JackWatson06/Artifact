package com.murdermaninc.levelCreator;

import java.awt.MouseInfo;

public class Menu {
	
	public int scrollAmount = 0;
	public int scrollAmountDecorations = 0;
	public int menuDepiction = 0; //0 - blocks, 1 - decorations, 2 - artifacts, 3 - subAtifacts
	private int maxSprites = 15;
	private Main main;
	
	private int menuX = 100;
	private int menuY = 0;
	private Block[] currentBlocks = new Block[0];
	private DecorationArtifact[] artifacts = new DecorationArtifact[0];
	private Decoration[] decorations = new Decoration[1];
	private DecorationSubArtifact[] subArtifacts = new DecorationSubArtifact[0];
	
	public boolean showing = false;
	
	private Screen screen;
	
	public Menu(Main main, Screen screen){
		this.main = main;
		this.screen = screen;
		screen.loadSpriteSheet("/editorMenu.png", "editorMenu");
	}
	
	public void tick(){
		
		menuDepiction = main.input.menuDepiction;
		
		if(scrollAmount < 5 && menuDepiction != 1){
			scrollAmount = 0;
		}else if(scrollAmountDecorations < 0 && menuDepiction == 1){
			scrollAmountDecorations = 0;
		}
		
		if(scrollAmount > main.maxBlockId && menuDepiction == 0){
			
			scrollAmount = main.maxBlockId - (main.maxBlockId % 5);
		}
		
		if(scrollAmountDecorations > main.maxDecorationId - 2000 && menuDepiction == 1){
			scrollAmountDecorations = main.maxDecorationId - 2000;
		}
		
		if(scrollAmount > main.maxArtifactId - 2749 && menuDepiction == 2){
			
			if((main.maxArtifactId - 2749) % 5 == 0){
				scrollAmount = (main.maxArtifactId - 2749) - 5;
			}else{
				scrollAmount = main.maxArtifactId - 2749 - ((main.maxArtifactId - 2749) % 5);
			}
		}
		
		if(scrollAmount >= main.maxSubArtifactId - 2999 && menuDepiction == 3){
			
			if((main.maxSubArtifactId - 2999) % 5 == 0){
				scrollAmount = (main.maxSubArtifactId - 2999) - 5;
			}else{
				scrollAmount = main.maxSubArtifactId - 2999 - ((main.maxSubArtifactId - 2999) % 5);
			}
		}
		
		menuX = screen.screenX + 100;
		menuY = screen.screenY;
		
		if(showing){
			if(menuDepiction == 0){
				if(main.maxBlockId - scrollAmount >= maxSprites){
					currentBlocks = new Block[maxSprites];
				}else{
					currentBlocks = new Block[main.maxBlockId - scrollAmount + 1];
				}
				
				int rows = 1;
				
				for(int i = 0; i < currentBlocks.length; i++){
					if(i % 5 == 0 && i != 0){
						rows++;
					}

					if(rows == 1){
						currentBlocks[i] = main.editorBlock(scrollAmount + i, menuX + 36 + (i * 96), menuY + 16);
					}else if(rows == 2){
						currentBlocks[i] = main.editorBlock(scrollAmount + i, menuX + 36 + ((i - 5) * 96), menuY + 16 + 80);
					}else{
						currentBlocks[i] = main.editorBlock(scrollAmount + i, menuX + 36 + ((i - 10) * 96),menuY + 16 + 160);
					}

				}
				
				
			}else if(menuDepiction == 1){
				
				
				decorations[0] = main.editorDecoration(2000 + scrollAmountDecorations, menuX + 40, menuY + 40);
				
			}else if(menuDepiction == 2){
				if((main.maxArtifactId - 2749) - scrollAmount >= maxSprites){
					artifacts = new DecorationArtifact[maxSprites];
				}else{
					artifacts = new DecorationArtifact[main.maxArtifactId - 2749 - scrollAmount];
				}
				
				int rows = 1;
				
				for(int i = 0; i < artifacts.length; i++){
					if(i % 5 == 0 && i != 0){
						rows++;
					}
					if(rows == 1){
						artifacts[i] = main.editorDecorationArtifact(2750 + scrollAmount + i, menuX + 36 + (i * 96), menuY + 16);
					}else if(rows == 2){
						artifacts[i] = main.editorDecorationArtifact(2750 + scrollAmount + i, menuX + 36 + ((i - 5) * 96), menuY + 16 + 80);
					}else{
						artifacts[i] = main.editorDecorationArtifact(2750 + scrollAmount + i, menuX + 36 + ((i - 10) * 96),menuY + 16 + 160);
					}

				}
			}else{
				if((main.maxSubArtifactId - 2999) - scrollAmount >= maxSprites){
					subArtifacts = new DecorationSubArtifact[maxSprites];
				}else{
					subArtifacts = new DecorationSubArtifact[main.maxSubArtifactId - 2999 - scrollAmount];
				}
				
				int rows = 1;
				//System.out.println("testing");
				//System.out.println(main.maxSubArtifactId - 2999 - scrollAmount);
				
				for(int i = 0; i < subArtifacts.length; i++){
					if(i % 5 == 0 && i != 0){
						rows++;
					}
					if(rows == 1){
						subArtifacts[i] = main.editorDecorationSubArtifact(3000 + scrollAmount + i, menuX + 36 + (i * 96), menuY + 16);
					}else if(rows == 2){
						subArtifacts[i] = main.editorDecorationSubArtifact(3000 + scrollAmount + i, menuX + 36 + ((i - 5) * 96), menuY + 16 + 80);
					}else{
						subArtifacts[i] = main.editorDecorationSubArtifact(3000 + scrollAmount + i, menuX + 36 + ((i - 10) * 96),menuY + 16 + 160);
					}

				}
			}
		}
	}
	
	public void render(){
		if(showing){
			screen.render(menuX, menuY, 0, 0, 9, 5, 4, "editorMenu");
			
			if(menuDepiction == 0){
				for(int i = 0; i < currentBlocks.length; i++){
					currentBlocks[i].renderNotScaled(screen);
				}
				
			}else if(menuDepiction == 1){
				decorations[0].render(screen);
			}else if(menuDepiction == 2){
				for(int i = 0; i < artifacts.length; i++){
					artifacts[i].render(screen);
				}
			}else{
				for(int i = 0; i < subArtifacts.length; i++){
					subArtifacts[i].render(screen);
				}
			}
		}
	}
	
	//130 * 4 = 520
	//70 * 4 = 280
	
	public boolean checkMouseCollision(){
		int mouseX = MouseInfo.getPointerInfo().getLocation().x + screen.screenX;
		int mouseY = MouseInfo.getPointerInfo().getLocation().y + screen.screenY;
		
		if(mouseX > menuX && mouseX < menuX + 520 && mouseY < menuY + 280 && mouseY > menuY){
			return true;
		}
		
		return false;
	}
	
	public void mouseClickCollision(){
		int mouseX = MouseInfo.getPointerInfo().getLocation().x + screen.screenX;
		int mouseY = MouseInfo.getPointerInfo().getLocation().y + screen.screenY;
		
		if(menuDepiction == 0){
			for(int i = 0; i < currentBlocks.length; i++){
				int blockX = currentBlocks[i].x;
				int blockY = currentBlocks[i].y;
				if(mouseX > blockX && mouseX < blockX + 64 && mouseY < blockY + 64 && blockY > menuY){
					main.editorId = currentBlocks[i].id;
					break;
				}
			}
		}else if(menuDepiction == 1){
			int decorationX = decorations[0].x;
			int decorationY = decorations[0].y;
			if(mouseX > decorationX && mouseX < decorationX + (decorations[0].spriteWidth * 64) && mouseY < decorationY + (decorations[0].spriteHeight * 64) && decorationY > menuY){
				main.editorId = decorations[0].id;
			}
		}else if(menuDepiction == 2){
			for(int i = 0; i < artifacts.length; i++){
				int artifactX = artifacts[i].x;
				int artifactY = artifacts[i].y;
				if(mouseX > artifactX && mouseX < artifactX + 64 && mouseY < artifactY + 64 && artifactY > menuY){
					main.editorId = artifacts[i].id;
					break;
				}
			}
		}else{
			for(int i = 0; i < subArtifacts.length; i++){
				int subArtifactX = subArtifacts[i].x;
				int subArtifactY = subArtifacts[i].y;
				if(mouseX > subArtifactX && mouseX < subArtifactX + 64 && mouseY < subArtifactY + 64 && subArtifactY > menuY){
					main.editorId = subArtifacts[i].id;
					break;
				}
			}
		}
	}
	
	public void currentMenu(int menuChange){
		menuDepiction = menuChange;
	}

}
