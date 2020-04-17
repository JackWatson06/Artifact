package com.murdermaninc.text;

import java.util.ArrayList;

import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;



public class TextBox {

	private int pixelWidth;
	private int pixelHeight; //pixel height does not include the height of the carrot
	
	private int textBoxTileWidth;
	private int textBoxTileHeight;
	
	private ArrayList<TextBoxPieces> textBoxPieces;
	
	private int maxCharsPerLine;
	
	private int minTextOffsetTopBot = 12;
	private int minTextOffsetLeftRight = 20;
	
	private int offsetLines = 10;
	
	private Font font = new Font();
	private int fontScale = 2;
	private boolean fontDraw = false;
	private int fontX = 0;
	private int fontY = 0;
	
	private ArrayList<String> textLines;
	private String text;
	
	
	public TextBox(String text, int x, int y, int position, int maxCharacters) {
		
		this.maxCharsPerLine = maxCharacters;
		
		this.text = text;
		
		int totalCharacters = text.length();
		
		
		if(totalCharacters > maxCharsPerLine) {
			
			fontDraw = true;
			
			ArrayList<String> lines = new ArrayList<String>();
			
			String lineOne = text.substring(0, maxCharsPerLine);
			
			
			int beginningIndex = 0;
			
			
			//System.out.println(lineOne.length());
			
			//System.out.println(lineOne);
			
			if(lineOne.charAt(maxCharsPerLine - 1) != ' ') {
				for(int i = lineOne.length() - 1; i >= 0; i--) {
					if(lineOne.charAt(i) == ' ') {
						lineOne = lineOne.substring(0, lineOne.length() - 1);
						beginningIndex = i + 1;
						break;
					}else {
						lineOne = lineOne.substring(0, lineOne.length() - 1);
					}
				}
			}else {
				beginningIndex = maxCharsPerLine;
				lineOne = lineOne.substring(0, lineOne.length() - 1);
			}
			
			lines.add(lineOne);
			
			

			int lastLineLength = 0;
			
			
			//System.out.println(lineOne);
			

			for(int i = beginningIndex; i < text.length(); i+=(lastLineLength)) { //we add the last line length and compare it to the entire string, we start at the beginning index because that repreents the line length of the first line
				
				String entireString = text.substring(beginningIndex, text.length());
			
				//System.out.println(entireString);
			
				if(entireString.length() > maxCharsPerLine) {
					String nextLine = text.substring(beginningIndex, beginningIndex + maxCharsPerLine);
					if(nextLine.charAt(maxCharsPerLine - 1) != ' ') {
						for(int j = nextLine.length() - 1; j >= 0; j--) {
							if(nextLine.charAt(j) == ' ') {
								nextLine = nextLine.substring(0, nextLine.length() - 1);
								
								//lines.add(nextLine);
								
								beginningIndex = beginningIndex + j + 1;
								break;
							}else {
								nextLine = nextLine.substring(0, nextLine.length() - 1);
							}
						}
					}else {
						nextLine = nextLine.substring(0, nextLine.length() - 1);
						beginningIndex = beginningIndex + maxCharsPerLine;
					}
					

					lines.add(nextLine);
					lastLineLength = nextLine.length() + 1;
				}else {
					
					String nextLine = text.substring(beginningIndex, text.length());
					lines.add(nextLine);
					lastLineLength = nextLine.length();
				}
				
				
			}		

			//for(int i = 0; i < lines.size(); i++) {
			//System.out.println(lines.get(i));
			//}
			
			textLines = lines;
			textBoxPieces = new ArrayList<TextBoxPieces>();
			
			int longestLineI = 0;
			
			for(int i = 0; i < lines.size(); i++) {
				if(font.getTextLength(lines.get(longestLineI), 2) < font.getTextLength(lines.get(i), 2)) {
					longestLineI = i;
				}
			}
			
			//System.out.println(lines.get(longestLineI));
			

			textBoxTileWidth = (int) Math.ceil((font.getTextLength(lines.get(longestLineI), fontScale) + (minTextOffsetLeftRight * 2)) / 64F);
			textBoxTileHeight = (int) Math.ceil((((12 * fontScale) * lines.size()) + (offsetLines * (lines.size() - 1)) + (minTextOffsetTopBot * 2)) / 64F);

			pixelWidth = font.getTextLength(lines.get(longestLineI), fontScale) + (minTextOffsetLeftRight * 2);
			pixelHeight = ((12 * fontScale) * lines.size()) + (offsetLines * (lines.size() - 1)) + (minTextOffsetTopBot * 2);
			
			//System.out.println("Tile Width: " + textBoxTileWidth);
			//System.out.println("Tile Height: " + textBoxTileHeight);
			
			//System.out.println("Pixel Width: " + pixelWidth);
			//System.out.println("Pixel Height: " + pixelHeight);
			
			if(position == 1) {
				
				y -= 24;
				
				int lastOffset = 0;
				
				//System.out.println((pixelWidth / 64F) - (textBoxTileWidth - 1));
				
				if((pixelWidth / 64F) - (textBoxTileWidth - 1) <= 0.1875F) {
					lastOffset = 12;
				}
				
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2, y - pixelHeight, 2, 3));
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2, y - 64, 2, 4));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64, y - pixelHeight, 3, 3));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64, y - 64, 3, 4));
				
				for(int i = 0; i < textBoxTileWidth - 2; i++) {
					if(i < textBoxTileWidth - 3) {
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64  + (i * 64), y - pixelHeight, 0, 4));
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64  + (i * 64), y - 64, 1, 4));
					}else {
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64  + (i * 64) - lastOffset, y - pixelHeight, 0, 4));
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64  + (i * 64) - lastOffset, y - 64, 1, 4));
					}
				}
				
				
				/*for(int i = 0; i < Math.ceil((textBoxTileWidth - 2) / 2F); i++) {
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64 + (i * 64), y - pixelHeight, 0, 4));
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64 + (i * 64), y - 64, 1, 4));
				}
				
				for(int i = 1; i < Math.floor((textBoxTileWidth - 2) / 2F) + 1; i++) {
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64 - (i * 64), y - pixelHeight, 0, 4));
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64 - (i * 64), y - 64, 1, 4));
				}*/
				
				for(int i = 0; i < textBoxTileHeight - 2; i++) {
					
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2, y - pixelHeight + 64 + (i * 64), 1, 3));
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64, y - pixelHeight +  64 + (i * 64), 0, 3));
					
					for(int j = 0; j < textBoxTileWidth - 2; j++) textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64 + (j * 64), y - pixelHeight +  64 + (i * 64), 3, 0));
				}
				
				textBoxPieces.add(new TextBoxPieces(x - (20 / 2), y - 4, 0, 0));
				
				fontX = x - (pixelWidth / 2) + minTextOffsetLeftRight;
				fontY = y - (pixelHeight) + minTextOffsetTopBot - (4 * 2);
				
			}else if(position == 2) {
				
				y -= 24; //subtract the hieght of the carrot for easier caluclations later
				
				//four corners
				
				int lastOffset = 0;
				
				//System.out.println((pixelWidth / 64F) - (textBoxTileWidth - 1));
				
				if((pixelWidth / 64F) - (textBoxTileWidth - 1) <= 0.1875F) {
					lastOffset = 12;
				}
				
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 64 - 20, y - pixelHeight, 2, 3));
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 64 - 20, y - 64, 2, 4));
				textBoxPieces.add(new TextBoxPieces(x  - 20, y - pixelHeight, 3, 3));
				textBoxPieces.add(new TextBoxPieces(x - 20, y - 64, 3, 4));
				
				
				for(int i = 0; i < textBoxTileWidth - 2; i++) {
					if(i < textBoxTileWidth - 3) {
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 44 + 64 + (i * 64), y - pixelHeight, 0, 4));
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 44 + 64 + (i * 64), y - 64, 1, 4));
					}else {
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 44 + 64 + (i * 64) - lastOffset, y - pixelHeight, 0, 4));
						textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 44 + 64 + (i * 64) - lastOffset, y - 64, 1, 4));
					}
					
				}
				
				for(int i = 0; i < textBoxTileHeight - 2; i++) {
					
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 44, y - pixelHeight + 64 + (i * 64), 1, 3));
					textBoxPieces.add(new TextBoxPieces(x - 20, y - pixelHeight +  64 + (i * 64), 0, 3));
					
					for(int j = 0; j < textBoxTileWidth - 2; j++) textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 44 + 64 + (j * 64), y - pixelHeight +  64 + (i * 64), 3, 0));
				}
				
				textBoxPieces.add(new TextBoxPieces(x - 20, y - 4, 1, 0));
				
				fontX = x - pixelWidth + 64 - 20 + minTextOffsetLeftRight;
				fontY = y - (pixelHeight) + minTextOffsetTopBot - (4 * 2);
				
			}else {
				
				y -= 24; //subtract the hieght of the carrot for easier caluclations later
				
				//four corners
				
				int lastOffset = 0;
				
				//System.out.println((pixelWidth / 64F) - (textBoxTileWidth - 1));
				
				if((pixelWidth / 64F) - (textBoxTileWidth - 1) <= 0.1875F) {
					lastOffset = 12;
				}
				
				textBoxPieces.add(new TextBoxPieces(x - 64 + 20, y - pixelHeight, 2, 3));
				textBoxPieces.add(new TextBoxPieces(x - 64 + 20, y - 64, 2, 4));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64, y - pixelHeight, 3, 3));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64, y - 64, 3, 4));
				
				
				for(int i = 0; i < textBoxTileWidth - 2; i++) {
					if(i < textBoxTileWidth - 3) {
						textBoxPieces.add(new TextBoxPieces(x - 44 + 64  + (i * 64), y - pixelHeight, 0, 4));
						textBoxPieces.add(new TextBoxPieces(x - 44 + 64  + (i * 64), y - 64, 1, 4));
					}else {
						textBoxPieces.add(new TextBoxPieces(x - 44 + 64  + (i * 64) - lastOffset, y - pixelHeight, 0, 4));
						textBoxPieces.add(new TextBoxPieces(x - 44 + 64  + (i * 64) - lastOffset, y - 64, 1, 4));
					}
				}
				
				for(int i = 0; i < textBoxTileHeight - 2; i++) {
					
					textBoxPieces.add(new TextBoxPieces(x - 64 + 20, y - pixelHeight + 64 + (i * 64), 1, 3));
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64, y - pixelHeight +  64 + (i * 64), 0, 3));
					
					for(int j = 0; j < textBoxTileWidth - 2; j++) textBoxPieces.add(new TextBoxPieces(x - 64 + 20 + 64 + (j * 64), y - pixelHeight +  64 + (i * 64), 3, 0));
				}
				
				textBoxPieces.add(new TextBoxPieces(x - 4, y - 4, 2, 0));
				
				fontX = x - 64 + 20 + minTextOffsetLeftRight;
				fontY = y - (pixelHeight) + minTextOffsetTopBot - (4 * 2);
				
			}
			
			
		}else {
			
			fontDraw = false;
			
			textBoxPieces = new ArrayList<TextBoxPieces>();
			textBoxTileWidth = (int) Math.ceil((font.getTextLength(text, fontScale) + (minTextOffsetLeftRight * 2)) / 64F);
			textBoxTileHeight = (int) Math.ceil(((12 * fontScale) + (minTextOffsetTopBot * 2)) / 64F); //12 represents the height of a uppercase change this to detect the maximum character height in the string
			
			pixelWidth = font.getTextLength(text, fontScale) + (minTextOffsetLeftRight * 2);
			pixelHeight = (12 * fontScale) + (minTextOffsetTopBot * 2);

			//System.out.println("Tile Width: " + textBoxTileWidth);
			//System.out.println("Tile Height: " + textBoxTileHeight);
			
			//System.out.println("Pixel Width: " + pixelWidth);
			//System.out.println("Pixel Height: " + pixelHeight);
			
			
			
			
			if(position == 1) {

				y -= 24; //subtract the hieght of the carrot for easier caluclations later
				
				//four corners
				
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2, y - pixelHeight, 2, 1));
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2, y - 36, 2, 2));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64, y - pixelHeight, 3, 1));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64, y - 36, 3, 2));
				
				
				for(int i = 0; i < Math.ceil((textBoxTileWidth - 2) / 2F); i++) {
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64 + (i * 64), y - pixelHeight, 0, 2));
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth / 2 + 64 + (i * 64), y - (pixelHeight / 2) - (36 - (pixelHeight / 2)), 1, 2));
				}
				
				for(int i = 1; i < Math.floor((textBoxTileWidth - 2) / 2F) + 1; i++) {
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64 - (i * 64), y - pixelHeight, 0, 2));
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth / 2 - 64 - (i * 64), y - (pixelHeight / 2) - (36 - (pixelHeight / 2)), 1, 2));
				}
				
	
				
				textBoxPieces.add(new TextBoxPieces(x - (20 / 2), y - 4, 0, 0));
				
				fontX = x - (pixelWidth / 2) + minTextOffsetLeftRight;
				fontY = y - (pixelHeight) + minTextOffsetTopBot - (4 * 2);
				
			}else if(position == 2) {
				
				y -= 24; //subtract the hieght of the carrot for easier caluclations later
				
				//four corners
				
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 64 - 20, y - pixelHeight, 2, 1));
				textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 64 - 20, y - 36, 2, 2));
				textBoxPieces.add(new TextBoxPieces(x  - 20, y - pixelHeight, 3, 1));
				textBoxPieces.add(new TextBoxPieces(x - 20, y - 36, 3, 2));
				
				
				for(int i = 0; i < textBoxTileWidth - 2; i++) {
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 64 - 20 + 64 + (i * 64), y - pixelHeight, 0, 2));
					textBoxPieces.add(new TextBoxPieces(x - pixelWidth + 64 - 20 + 64 + (i * 64), y - (pixelHeight / 2) - (36 - (pixelHeight / 2)), 1, 2));
				}
				
				
				textBoxPieces.add(new TextBoxPieces(x - 20, y - 4, 1, 0));
				
				fontX = x - pixelWidth + 64 - 20 + minTextOffsetLeftRight;
				fontY = y - (pixelHeight) + minTextOffsetTopBot - (4 * 2);
				
			}else {
				
				y -= 24; //subtract the hieght of the carrot for easier caluclations later
				
				//four corners
				
				textBoxPieces.add(new TextBoxPieces(x - 64 + 20, y - pixelHeight, 2, 1));
				textBoxPieces.add(new TextBoxPieces(x - 64 + 20, y - 36, 2, 2));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64, y - pixelHeight, 3, 1));
				textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64, y - 36, 3, 2));
				
				
				for(int i = 0; i < textBoxTileWidth - 2; i++) {
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64 - 64 - (i * 64), y - pixelHeight, 0, 2));
					textBoxPieces.add(new TextBoxPieces(x + pixelWidth - 64 + 20 - 64 - 64 - (i * 64), y - (pixelHeight / 2) - (36 - (pixelHeight / 2)), 1, 2));
				}
				
				
				textBoxPieces.add(new TextBoxPieces(x - 4, y - 4, 2, 0));
				
				fontX = x - 64 + 20 + minTextOffsetLeftRight;
				fontY = y - (pixelHeight) + minTextOffsetTopBot - (4 * 2);
				
			}
			
		}
		
	}
	
	
	
	public void render(Screen screen) {
		
		
		if(textBoxPieces != null) {
			for(int i = 0; i < textBoxPieces.size(); i++) {
				textBoxPieces.get(i).render(screen);
			}
		}
		
		if(fontDraw) {
			for(int i = 0; i < textLines.size(); i++) {
				font.drawText(screen, textLines.get(i), fontX, fontY + ((i * offsetLines) + (i * 12 * fontScale)), fontScale);
			}
		}else {
			font.drawText(screen, text, fontX, fontY, fontScale);
		}
		
		
		
		

		
	}
}
