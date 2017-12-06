package com.murdermaninc.levelCreator;

public class Font {

	//DONE - switch this to seperate sprite sheet and think about adding lowercase font
	
	//Maybe make spacing dependent on the character size - (just make a spereate matching int array in which i record the pixel width of each charcter and use that as space)
	//TODO - fix spacing when scaled up
	
	private static String characters = new String("ABCDEFGHIJKLMNOP"
										       +  "QRSTUVWXYZabcdef"
										       +  "ghijklmnopqrstuv"
										       +  "wxyz0123456789!."
										       +  "?() -:%[]");
	
	private int[] fontSize = { 9, 9, 9,  9, 9,  9,  9, 9, 10, 9, 8, 9, 9, 9, 9, 9,
							  10, 9, 9, 10, 9,  9,  9, 9,  8, 9, 7, 6, 6, 6, 6, 6,
							   6, 6, 6,  5, 5,  2,  8, 5,  6, 6, 7, 5, 6, 6, 6, 6,
							   8, 7, 6,  7, 9, 10,  9, 9,  9, 9, 9, 9, 9, 9, 3, 3,
							   8, 6, 6, 16, 7,  3, 11, 5,  5};
	
	private static float spacing = 2F;
	
	public void drawText(Screen screen, String text, int x, int y, int scale){
		int lastX = x;
		
		//first char just recoreds when the first character is done rendering becuase we want to add the spacing but not mess up the x value of the first character.
		for(int i = 0; i < text.length(); i++){
			char currentCharacter = text.charAt(i);
			boolean upperCase = Character.isUpperCase(currentCharacter);
			boolean isChar = Character.isLetter(currentCharacter);

			for(int j = 0; j < characters.length(); j++){
				
				//TEST FOR SPECIAL LOWERCASE SUCH AS g y q p j and render these are a shorter y value to give as the tail will be put normally into the sprite sheet.
				//It might be a good idea to align the sprites to the left hand side of the sprite for more acuracy on the x value.
				
				if(currentCharacter == characters.charAt(j) && (upperCase || !isChar)){

					if(j < 16){
						screen.render(lastX, y, j, 0, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}else if(j >= 16 && j < 32){
						screen.render(lastX, y, j - 16, 1, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}else if(j >= 32 && j < 48){
						screen.render(lastX, y, j - 32, 2, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}else if(j >= 48 && j < 64){
						screen.render(lastX, y, j - 48, 3, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}else{
						screen.render(lastX, y, j - 64, 4, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}
				}else if(currentCharacter == characters.charAt(j) && (!upperCase || isChar)){
					
					int setLower = 0;
					
					if(currentCharacter == 'g' || currentCharacter == 'y' || currentCharacter == 'q' || currentCharacter == 'p' ||currentCharacter == 'j' ){
						setLower = 3 * scale;
					}
					
					if(j >= 16 && j < 32){
						screen.render(lastX, y + setLower, j - 16, 1, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}else if(j >= 32 && j < 48){
						screen.render(lastX, y + setLower, j - 32, 2, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}else{
						screen.render(lastX, y + setLower, j - 48, 3, 1, 1, scale, "font");
						lastX += fontSize[j] * scale + (spacing * scale);
					}
				}
			}
		}
	}
	
	public int getTextLength(String text, int scale){
		int textSize = 0;
		for(int i = 0; i < text.length(); i++){
			for(int j = 0; j < characters.length(); j++){
				if(text.charAt(i) == characters.charAt(j)){
					if(i != text.length() - 1){
						textSize += fontSize[j] * scale + (spacing * scale);
					}else{
						textSize += fontSize[j] * scale;
					}
				}
			}
		}

		return textSize;
	}
}





