package com.murdermaninc.entity;

import java.util.ArrayList;

import com.murdermaninc.blocks.Block;
import com.murdermaninc.decorations.mainShip.Elevator;
import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;
import com.murdermaninc.main.InputManager;
import com.murdermaninc.main.Main;

public class Player extends Entity {

	
	public InputManager input;
	
	public boolean movedW = false;
	public boolean movedA = false;
	public boolean movedS = false;
	public boolean movedD = false;
	public boolean facing = false;
	public boolean lastFacing = false; //used to compare facing values
	
	
	public boolean isRunning = false;
	public boolean isClimbingHorizontal = false;
	public boolean isClimbingVertical = false;
	public int walkingSpeed = 4;
	public int runningSpeed = 8;
	public int climbingSpeed = 2;
	public int currentSpeed;
	
	private Level currentLevel;
	
	//Animation Data
	private Animation animation = new Animation();
	private Animation flameAnimation = new Animation();
	private Animation killAnimation = new Animation();
	//private Animation climbingAnimation = new Animation();
	private ArrayList<int[]> runAnimationRight;
	private ArrayList<int[]> runAnimationLeft;
	private ArrayList<int[]> jumpFlame;
	private ArrayList<int[]> deflatKillRight;
	private ArrayList<int[]> deflatKillLeft;
	private ArrayList<int[]> drownRight;
	private ArrayList<int[]> drownLeft;
	private ArrayList<int[]> climbingRight;
	private ArrayList<int[]> climbingBackHandRight;
	private ArrayList<int[]> climbingLeft;
	private ArrayList<int[]> climbingBackHandLeft;
	private ArrayList<int[]> poisonKillRight;
	private ArrayList<int[]> poisonKillLeft;
	private ArrayList<int[]> disolveRight;
	private ArrayList<int[]> disolveLeft;
	private ArrayList<int[]> verticalClimbingRight;
	private ArrayList<int[]> verticalClimbingLeft;
	
	private int[] frontHandRight;
	private int[] frontHandLeft;
	private int[] verticalFacingRight;
	private int[] verticalFacingLeft;
	private int[] verticalSwingingRight;
	private int[] verticalSwingingLeft;
	
	private int deathX, deathY;
	
	//Jump Variables
	public int jumpTick;
	public boolean jumpAvailability = true;
	public int jumpCount = 0;
	public int jumpBuffer = 0;
	public boolean jump = false;
	private boolean jumpCounter = true; // Checks where the jump occurs
	private boolean jumpingAir = false; // Checks if the jump began in the air and if it does produce purple fire
	public double currentJumpSpeed;
	
	//Make the jGTick value interpolated
	//Jump and Gravity Tick Stuff
	private int jGTick = 42;
	private int jGSpeed = 16;
	private int jumpOffset = 10;
	
	//Gravity
	public boolean gravity = false;
	public int beginningGravityTick;
	public double currentGravitySpeed;
	
	//This is only used when I need to completely disable gravity such as when the player has certain death animations.
	//If this is set to true it MUST be disabled in order to turn back on gravity.
	private boolean disableGravity = false;
	
	//Dying which causes no control for a short period
	private String deathTag = new String("");
	private boolean dying = false;
	public boolean noControl = false;
	private int noControlCounter = 0;
	
	//Detects the currentShipRoom(for collisions) and checks elevator stuff
	public String currentShipRoom = new String("");
	public Elevator elevator;
	private boolean onElevator = false;
	
	//Climbing Variables
	public int leftClimbBound = 0;
	public int rightClimbBound = 0;
	
	//This variable is to determine that amount of ticks sense the last time on the ground
	//and it is used to determine the one pixel offset for the jump.
	private int lastTimeOnFloor = 0;
	
	
	
	public Player(int id, int xTile, int yTile, int spriteWidth, int spriteHeight, String name, InputManager input, Level currentLevel, int x, int y) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.name = name;
		this.input = input;
		this.lastX = x;
		this.lastY = y;
		this.currentLevel = currentLevel;
		deathX = x;
		deathY = y;
		
	}
	
	
	public void tick(float interpolation, int currentTick){
		
		lastX = x;
		lastY = y;
		
		if(noControl){
			if(noControlCounter == 0) {
				resetGravity(currentTick);
			}
			noControlCounter++;
			if(noControlCounter >= 17){
				noControl = false;
				noControlCounter = 0;
				resetJump();
			}
		}
		
		if(gravity) {
			lastTimeOnFloor++;
		}
		
		
		//PLAYER NOW JUMPS HIGHER ex. level1-3 i think it may be due to the switch of the jump start from the inputManager to this player class
		if(input.spaceBar && jumpAvailability && !jump && !dying && !noControl && !isClimbingHorizontal && !isClimbingVertical){
				if(jumpCount == 0){
					jump = true;
					jump();
				}else if(jumpCount == 1 && jumpBuffer >= 15){
					jumpBuffer = 0;
					jumpAvailability = false;
					jump = true;
					jump();
			}
		}
		
		if(!dying && !noControl && !isClimbingHorizontal && !isClimbingVertical){
			jump(currentTick, interpolation);
		}
		
		if(!onElevator && !disableGravity){
			gravity(currentTick, interpolation);
			
		}else if(!disableGravity){
			y++;
		}

		
		//System.out.println("Last Time On Floor: " + lastTimeOnFloor);
		
		resetButtons();
		//Jump
		//(16 / 60 squared) x squared - ((60 * 2) * (16 / 60 squared)) * x) + 16
		//Gravity
		//((-16 / 60 squared) x squared) + ((-60 * 2) * (-16 / 60 squared)) * x)
		
		//ORIGINAL RUN SPEED: 6
		//ORIGINAL WALK SPEED: 4
		if(!dying && !noControl && !isClimbingVertical){
			if (input.a && !input.shift){
				x-= (int) (walkingSpeed * interpolation);
				currentSpeed = (int) (walkingSpeed * interpolation);
				facing = true;
				movedA = true;
			}
			if (input.d && !input.shift){
				x+= (int) (walkingSpeed * interpolation);
				currentSpeed = (int) (walkingSpeed * interpolation);
				facing = false;
				movedD = true;
			}
			if (input.a && input.shift && !isClimbingHorizontal){
				x-= (int) (runningSpeed * interpolation);
				currentSpeed = (int) (runningSpeed * interpolation);
				facing = true;
				movedA = true;
			}else if(input.a && input.shift && isClimbingHorizontal){
				x-= (int) (walkingSpeed * interpolation);
				currentSpeed = (int) (walkingSpeed * interpolation);
				facing = true;
				movedA = true;
			}
			if (input.d && input.shift && !isClimbingHorizontal){
				x+= (int) (runningSpeed * interpolation);
				currentSpeed = (int) (runningSpeed * interpolation);
				facing = false;
				movedD = true;
			}else if(input.d && input.shift && isClimbingHorizontal){
				x+= (int) (walkingSpeed * interpolation);
				currentSpeed = (int) (walkingSpeed * interpolation);
				facing = false;
				movedD = true;
			}
			if(!input.a && !input.d){
				currentSpeed = 0;
			}
			if(input.a || input.d){
				isRunning = true;
			}else{
				isRunning = false;
			}
		}
		
		if(isClimbingVertical) {
			if(input.e) {
				resetGravity(currentTick);
				resetJump();
				jumpCount = 0;
				jumpAvailability = true;
				input.e = false;
				isClimbingVertical = false;
				animation.resetCurrentAnimation();
			}
			
			if(input.a) {	
				movedA = true;	
				facing = true;
			}
			
			if(input.d) {
				movedD = true;
				facing = false;
			}
			
			if(input.w) {
				movedW = true;
			}
			
			if(input.s) {
				movedS = true;
			}
			
			if(input.a || input.d){
				isRunning = true;
			}else{
				isRunning = false;
			}
			
			if(facing != lastFacing) {
				lastFacing = facing;
				resetAnimation();
			}
			
		}
		
		
		if(isClimbingHorizontal){
			
			//Detects when the player let goes of a vine
			
			if(input.e){
				resetGravity(currentTick);
				resetJump();
				jumpCount = 0;
				jumpAvailability = true;
				input.e = false;
				isClimbingHorizontal = false;
				animation.resetCurrentAnimation();
			}
			

			//Check climbing collisions
			if(x + 4 <= leftClimbBound){
				x +=  currentSpeed;
			}
			
			if(x + 63 - 4 >= rightClimbBound){
				x -=  currentSpeed;
			}
		}
		

		if(currentLevel != null){
			if(y > (currentLevel.levelHeight + 4) * 64){
				kill("fall", 0, 0);
			}
		}
		
		if(currentLevel != null){
			checkCollisions(currentTick);
		}else{
			checkCollisionShip(currentTick);
		}
		
	
		

	}
	
	public void jump(int currentTick, float interpolation){
		if(jump){
			if(jumpCounter){
				jumpCounter = false;
				checkJumpStart();
				if(jumpingAir){
					jumpCount = 2;
				}else{
					jumpCount++;
				}
			}
			gravity = false;
			int inputTick = currentTick - jumpTick;
			double a = jGSpeed / Math.pow(jGTick, 2);
			double equation = ((double)a * Math.pow(inputTick, 2)) - (((double) (jGTick * 2) * a) * inputTick) + ((double)jGSpeed);
			y -=  (int) (equation * interpolation);
			currentJumpSpeed = (int) (equation * interpolation);
			if(inputTick >= jGTick - jumpOffset){
				jump = false;
				jumpBuffer = 0;
				resetJump();
			}
		}else{
			currentJumpSpeed = 0;
			resetJump();
		}
		
	}
	
	public void jump(){
		jumpTick = Main.getTicks();
	}
	
	public void gravity(int currentTick, float interpolation){
		if(!jump && !isClimbingHorizontal && !isClimbingVertical && gravity){

			int inputTick = currentTick - beginningGravityTick;
			jumpBuffer++;
			
			//System.out.println(inputTick);
			//System.out.println(jGTick);
			if(inputTick <= jGTick){
				double a = -jGSpeed / Math.pow(jGTick, 2);
				double equation = ((double)a * Math.pow(inputTick, 2)) + (((double) (-jGTick * 2) * a) * inputTick);
				y += (int) (equation * interpolation);
				//System.out.println("Gravity: " + (int) (equation * interpolation));
				currentGravitySpeed = (int) (equation * interpolation);
			}else{
				y += (int) (jGSpeed * interpolation);
				currentGravitySpeed = (int) (jGSpeed * interpolation);
				//System.out.println("Y Distance Change: " + (y - startCalculatingY));
				//System.exit(0);
				
			}
		}else{
			currentGravitySpeed = 0;
			resetGravity(currentTick);
		}
		
	}
	
	public void checkJumpStart(){
		if(currentLevel != null){
			//System.out.println("Check Jump Start: " + lastTimeOnFloor);
			
			int px = x + 8;
			int py = y + 4;
			int pxr = x + 63 - 8;
			int pyb = y + 63;
			Block uL = currentLevel.getBlock((int)Math.floor(px / 64), (int) Math.floor(py / 64));
			Block uR = currentLevel.getBlock((int)Math.floor(pxr / 64), (int) Math.floor(py / 64));
			
			Block bL = currentLevel.getBlock((int)Math.floor(px / 64), (int) Math.floor(pyb / 64));
			Block bR = currentLevel.getBlock((int)Math.floor(pxr / 64), (int) Math.floor(pyb / 64));
			

			Block gL = currentLevel.getBlock((int)Math.floor(px  / 64), (int) Math.floor((pyb + 1) / 64));
			Block gR = currentLevel.getBlock((int)Math.floor(pxr / 64), (int) Math.floor((pyb + 1) / 64));
			if(uR.collisions || bR.collisions){
				 gL = currentLevel.getBlock((int)Math.floor((px - currentSpeed)  / 64), (int) Math.floor((pyb + 1) / 64));
				 gR = currentLevel.getBlock((int)Math.floor((pxr - currentSpeed) / 64), (int) Math.floor((pyb + 1) / 64));
			}else if(uL.collisions || bL.collisions){
				 gL = currentLevel.getBlock((int)Math.floor((px + currentSpeed)  / 64), (int) Math.floor((pyb + 1) / 64));
				 gR = currentLevel.getBlock((int)Math.floor((pxr + currentSpeed) / 64), (int) Math.floor((pyb + 1) / 64));
			}

			if(!gL.collisions && !gR.collisions && (lastTimeOnFloor != 1 && lastTimeOnFloor != 2)){
				jumpingAir = true;
			}
		}else{
			if(currentShipRoom.equals("room")){
				if(y + 63 < 779 && y + 4 > 303){
					jumpingAir = true;
				}
			}else if(currentShipRoom.equals("dorm")){

				//gravity floor (bottom) for sections 9 and 10 due to overlap with elevator the noElevator variable is required only do this section when NOT ON ELEVATOR
				if(y + 63 < 1015 && y + 4 >= 555 && (x + 4 < 764 || x + 63 - 4 > 1152) && !onElevator){
					jumpingAir = true;
				}
				
				//gravity for sections 1
				if(y + 63 < 1015 && y + 4 >= 555 && x + 63 - 4 < 764){
					jumpingAir = true;
				}
				
				//gravity for sections 2
				if(y + 63 < 1015 && y + 4 >= 555 && x + 4 > 1152){
					jumpingAir = true;
				}
				
				//gravity floor(bottom) for sections 5 and 6
				if(y + 63 < 523 && y + 4 >= 63 && (x + 4 < 764 || x + 63 - 4 > 1152)){
					jumpingAir = true;
				}
				
				//gravity floor(bottom) for section 4
				if(y + 63 < 1039 && y + 4 >= elevator.colB - 1 && x + 4 >= 764 && x + 63 - 4 <= 1152){
					jumpingAir = true;
				}
				
				//this tests the gravity ubove the elevator and has overlap with 9 and 10
				if(y + 63 < elevator.colT  - 2 && y + 4 >= 63 && x + 4 >= 764 && x + 63 - 4 <= 1152){
					jumpingAir = true;
				}
				
			}else if(currentShipRoom.equals("corridor")){
				if(y + 63 < 1015 && y + 4 > 63){
					jumpingAir = true;
				}
			}else if(currentShipRoom.equals("kingsChambers")){
				if(y + 63 < 1015 && y + 4 > 63){
					jumpingAir = true;
				}
			}else if(currentShipRoom.equals("podRoom")){
				if(y + 63 < 1015 && y + 4 > 63){
					jumpingAir = true;
				}
			}else if(currentShipRoom.equals("launchRoom")){
				if(y + 63 < 1015 && y + 4 > 63){
					jumpingAir = true;
				}
			}
		}
	}
	
	public void resetGravity(int currentTick){
		beginningGravityTick = currentTick;
		currentGravitySpeed = 0;
	}
	
	public void resetJump(){
		jumpingAir = false;
		flameAnimation.once = true;
		jumpCounter = true;
		jump = false;
	}
	
	public void resetButtons(){
		movedW = false;
		movedA = false;
		movedS = false;
		movedD = false;
	}
	
	public void kill(String specificDeath, int decorationX, int decoraitonY){
		//x = deathX;
		//y = deathY;
		currentSpeed = 0;
		if(specificDeath.equals("deflat") && !dying){
			dying = true;
			if(x + 32 < decorationX + 32){
				deathTag = new String("deflatRight");
			}else{
				deathTag = new String("deflatLeft");
			}
			killAnimation.once = true;
			gravity = true;
			jump = false;
		}
		
		if(specificDeath.equals("poison") && !dying) {
			dying = true;
			if(x + 32 < decorationX + 32){
				deathTag = new String("poisonRight");
			}else{
				deathTag = new String("poisonLeft");
			}
			killAnimation.once = true;
			gravity = true;
			jump = false;
		}
		
		if(specificDeath.equals("drown") && !dying) {
			dying = true;
			if(!facing) {
				deathTag = new String("drownRight");
			}else {
				deathTag = new String("drownLeft");
			}
			killAnimation.once = true;
			gravity = false;
			disableGravity = true;
			jump = false;
		}
		
		if(specificDeath.equals("disolve") && !dying) {
			dying = true;
			if(!facing) {
				deathTag = new String("disolveRight");
			}else {
				deathTag = new String("disolveLeft");
			}
			killAnimation.once = true;
			gravity = true;
			jump = false;
			
		}
		
		if(specificDeath.equals("fall") && !dying){
			deathTag = new String("fall");
			dying = true;
		}
	}
	public void killXY(){
		x = deathX;
		y = deathY;
		noControl = true;
		isRunning = false;
	}


	
	
	//This keeps track of the animation reset for the switch in player movement from moving left to right
	private int lastSpeed = 4;
	
	//This is used so methods are not randomly checked and called.
	private boolean animationFound = false;
	
	@Override
	public void render(Screen screen, float interpolation){
		
		
		//This section loads all of the animation data for the player
		//Some animations are specific to a level and are loaded respectively
		
		if(!animationFound) {
			
			if(currentLevel != null && currentLevel.isDarkened()) {
				
				if(runAnimationRight == null) runAnimationRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 7, 1, 1);
				if(runAnimationLeft == null) runAnimationLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 8, 1, 1);
				
			}else{
				
				if(runAnimationRight == null) runAnimationRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 1, 1, 1);
				if(runAnimationLeft == null) runAnimationLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 2, 1, 1);
				
			}

			if(jumpFlame == null) jumpFlame = animation.loadAnimationData(screen, "player", 4, 4, 0, 3, 1, 1);

			if(currentLevel != null && !animationFound) {

				//Deflat animation for light and dark levels
				if(currentLevel.containsDeathTag("deflat") && currentLevel.isDarkened()) {

					if(deflatKillRight == null) deflatKillRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 10, 1, 1);
					if(deflatKillLeft == null) deflatKillLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 11, 1, 1);

				}else if(currentLevel.containsDeathTag("deflat")) {

					if(deflatKillRight == null) deflatKillRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 4, 1, 1);
					if(deflatKillLeft == null) deflatKillLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 5, 1, 1);

				}

				//Drown animation data for light and dark levels
				if(currentLevel.containsDeathTag("drown") && currentLevel.isDarkened()) {

					if(drownRight == null) drownRight = animation.loadAnimationData(screen, "player", 4, 11, 0, 13, 1, 1);
					if(drownLeft == null) drownLeft = animation.loadAnimationData(screen, "player", 4, 11, 0, 14, 1, 1);

				}else if(currentLevel.containsDeathTag("drown")) {

					if(drownRight == null) drownRight = animation.loadAnimationData(screen, "player", 4, 11, 13, 6, 1, 1);
					if(drownLeft == null) drownLeft = animation.loadAnimationData(screen, "player", 4, 11, 13, 7, 1, 1);

				}
				
				
				//Disolve animation data for light and dark levels
				
				if(currentLevel.containsDeathTag("disolve") && currentLevel.isDarkened()) {
					
					if(disolveRight == null) disolveRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 15, 1, 1);
					if(disolveLeft == null) disolveLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 16, 1, 1);
					
				}else if(currentLevel.containsDeathTag("disolve")) {
					
					if(disolveRight == null) disolveRight = animation.loadAnimationData(screen, "player", 4, 8, 16, 8, 1, 1);
					if(disolveLeft == null) disolveLeft = animation.loadAnimationData(screen, "player", 4, 8, 16, 9, 1, 1);
					
				}
				
				//Climbing Horizontal Animations
				
				if(currentLevel.containsDecorations(new int[] {2052, 2053, 2054})) {
					if(climbingRight == null) climbingRight = animation.loadAnimationData(screen, "player", 4, 8, 16, 0, 1, 1);
					if(climbingLeft == null) climbingLeft = animation.loadAnimationData(screen, "player", 4, 8, 16, 2, 1, 1);
					if(climbingBackHandRight == null) climbingBackHandRight = animation.loadAnimationData(screen, "player", 4, 8, 16, 1, 1, 1);
					if(climbingBackHandLeft == null) climbingBackHandLeft = animation.loadAnimationData(screen, "player", 4, 8, 16, 3, 1, 1);
				}
				
				//Poison Animation
				
				if(currentLevel.containsDeathTag("poison")) {
					
					if(poisonKillRight == null) poisonKillRight = animation.loadAnimationData(screen, "player", 4, 11, 13, 4, 1, 1);
					if(poisonKillLeft == null) poisonKillLeft = animation.loadAnimationData(screen, "player", 4, 11, 13, 5, 1, 1);
					
				}
				
				//Climbing Vertical Animations
				if(currentLevel.containsDecorations(new int[] {2058})) {
					if(frontHandRight == null) frontHandRight = screen.loadData(23, 11, 1, 1, 4, "player");
					if(frontHandLeft == null) frontHandLeft = screen.loadData(23, 13, 1, 1, 4, "player");
					if(verticalClimbingRight == null) verticalClimbingRight = animation.loadAnimationData(screen, "player", 4, 5, 19, 10, 1, 1);
					if(verticalClimbingLeft == null) verticalClimbingLeft = animation.loadAnimationData(screen, "player", 4, 5, 19, 12, 1, 1);
					if(verticalFacingRight == null) verticalFacingRight = screen.loadData(19, 10, 1, 1, 4, "player");
					if(verticalFacingLeft == null) verticalFacingLeft = screen.loadData(19, 12, 1, 1, 4, "player");
					if(verticalSwingingRight == null) verticalSwingingRight = screen.loadData(23, 10, 1, 1, 4, "player");
					if(verticalSwingingLeft == null) verticalSwingingLeft = screen.loadData(23, 12, 1, 1, 4, "player");
				}

			}
			
			animationFound = true;
			
		}

		
		if(jumpingAir){
			flameAnimation.animateOnce(screen, jumpFlame, false, 10.0F, 1, 1, x + 16, y + 60, 4, 4, interpolation);
		}else{
			flameAnimation.resetCurrentAnimation();
		}
		
		int fps = 10;
		
		if(isRunning && currentSpeed == runningSpeed){
			fps = 15;
		}
		
		if(isRunning && !dying && !isClimbingHorizontal && !isClimbingVertical){
			
			//Running animations
			
			
			if(currentSpeed != lastSpeed){
				if(currentSpeed == runningSpeed){
					animation.updateContinuous(10, 15);
				}else{
					
					animation.updateContinuous(15, 10);
				}
				
			}
			lastSpeed = currentSpeed;
			
			if(movedD){
				animation.animateContinuous(screen, runAnimationRight, false, fps, 1, 1, x, y, 8, 4, interpolation);
			}else if(movedA){
				animation.animateContinuous(screen, runAnimationLeft, false, fps, 1, 1, x, y, 8, 4, interpolation);
			}

		}else if(!dying && !isClimbingHorizontal && !isClimbingVertical){
			
			//Render basic standing position
			
			if(!facing){
				if(currentLevel != null && currentLevel.isDarkened()) {
					screen.render(x, y, 0, 7, 1, 1, 4, "player");
				}else {
					screen.render(x, y, 0, 1, 1, 1, 4, "player");
				}
				//killAnimation.animateContinuous(screen, poisonKillRight, false, 8, 1, 1, x, y, 10, 4);
			}else{
				if(currentLevel != null && currentLevel.isDarkened()) {
					screen.render(x, y, 0, 8, 1, 1, 4, "player");
				}else {
					screen.render(x, y, 0, 2, 1, 1, 4, "player");
				}
			}
		}else if(isClimbingHorizontal && !dying && !isClimbingVertical){
			
			//Render for climbing horizontally
			
			
			if(isRunning){
				if(!facing){
					animation.animateContinuous(screen, climbingRight, false, 7F, 1, 1, x, y, 8, 4, interpolation);
				}else{
					animation.animateContinuous(screen, climbingLeft, false, 7F, 1, 1, x, y, 8, 4, interpolation);
				}
			}else{
				if(!facing){
					screen.render(x, y, 14, 0, 1, 1, 4, "player");
				}else{
					screen.render(x, y, 14, 1, 1, 1, 4, "player");
				}
			}
		}else if(isClimbingVertical && !dying) {
			
			if(!facing){
				screen.renderData(frontHandRight, x, y, 1, 1, 4);
			}else{
				screen.renderData(frontHandLeft, x, y, 1, 1, 4);
			}
				
		}
		
		if(dying){
			//System.out.println(deathTag);
			if(deathTag.equals("deflatLeft")){
				killAnimation.animateOnce(screen, deflatKillLeft, false, 8, 1, 1, x, y, 8, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
				}
			}else if(deathTag.equals("deflatRight")){
				killAnimation.animateOnce(screen, deflatKillRight, false, 8, 1, 1, x, y, 8, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
				}
			}else if(deathTag.equals("fall")){
				killXY();
				dying = false;
			}else if(deathTag.equals("poisonRight")) {
				killAnimation.animateOnce(screen, poisonKillRight, false, 8, 1, 1, x, y, 11, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
				}
			}else if(deathTag.equals("poisonLeft")) {
				killAnimation.animateOnce(screen, poisonKillLeft, false, 8, 1, 1, x, y, 11, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
				}
			}else if(deathTag.equals("drownRight")) {
				
				//Needs to be updated
				killAnimation.animateOnce(screen, drownRight, false, 10, 1, 1, x, y, 11, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
					disableGravity = false;
				}
			}else if(deathTag.equals("drownLeft")) {
				
				//Needs to be updated
				killAnimation.animateOnce(screen, drownLeft, false, 10, 1, 1, x, y, 11, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
					disableGravity = false;
				}
			}else if(deathTag.equals("disolveRight")) {
				
				//Needs to be updated
				killAnimation.animateOnce(screen, disolveRight, false, 10, 1, 1, x, y, 8, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
					disableGravity = false;
				}
			}else if(deathTag.equals("disolveLeft")) {
				
				//Needs to be updated
				killAnimation.animateOnce(screen, disolveLeft, false, 10, 1, 1, x, y, 8, 4, interpolation);
				if(!killAnimation.once){
					killXY();
					dying = false;
					disableGravity = false;
				}
			}
		}
		
		


		
	}
	
	public void renderBeforeAllDecorations(Screen screen, float interpolation){
		if(isClimbingHorizontal && !isClimbingVertical) {
			if(isRunning){
				if(!facing){
					//System.out.println("Testing");
					screen.renderData(climbingBackHandRight.get(animation.getCurrentSprite()), x, y, 1, 1, 4);
				}else{
					screen.renderData(climbingBackHandLeft.get(animation.getCurrentSprite()), x, y, 1, 1, 4);
				}
			}
			
		}else if(isClimbingVertical) {
			if(isRunning){
				if(!facing){
					
					//System.out.println(animation.once);
					
					if(animation.once) {
						animation.animateOnce(screen, verticalClimbingRight, false, 10F, 1, 1, x, y, 5, 4, interpolation);
					}else {
						screen.renderData(verticalSwingingRight, x, y, 1, 1, 4);
					}
				}else{
					
					if(animation.once) {
						animation.animateOnce(screen, verticalClimbingLeft, false, 10F, 1, 1, x, y, 5, 4, interpolation);
					}else {
						screen.renderData(verticalSwingingLeft, x, y, 1, 1, 4);
					}
				}
			}else {
				
				if(!facing){
						screen.renderData(verticalFacingRight, x, y, 1, 1, 4);

				}else{	
						screen.renderData(verticalFacingLeft, x, y, 1, 1, 4);
				}
				
			}
		}
	}
	
	
	public void resetAnimation(){
		animation.resetCurrentAnimation();
	}
	
	
	private void checkCollisions(int currentTick){
		

		int px = x + 8;
		int pxr = x + 63 - 8;
		int py = y + 4;
		int pyb = y + 63;
		
		
		boolean setUp = false;
		boolean setDown = false;
		boolean setLeft = false;
		boolean setRight = false;
		
		boolean notFull = false;
		int offset = 0;
		
		Block uL = currentLevel.getBlock((int)Math.floor(px / 64), (int) Math.floor(py / 64));
		Block uR = currentLevel.getBlock((int)Math.floor(pxr / 64), (int) Math.floor(py / 64));
		
		Block bL = currentLevel.getBlock((int)Math.floor(px / 64), (int) Math.floor(pyb / 64));
		Block bR = currentLevel.getBlock((int)Math.floor(pxr / 64), (int) Math.floor(pyb / 64));
		

		Block gL = currentLevel.getBlock((int)Math.floor(px  / 64), (int) Math.floor((pyb + 1) / 64));
		Block gR = currentLevel.getBlock((int)Math.floor(pxr / 64), (int) Math.floor((pyb + 1) / 64));
		

		
		//System.out.println("UL: " + uL.collisions);
		//System.out.println("UR: " + uR.collisions);
		//System.out.println("BR: " + bR.collisions);
		//System.out.println("BL: " + bL.collisions);
		
		
		if(uR.collisions || bR.collisions){
			 gL = currentLevel.getBlock((int)Math.floor((px - currentSpeed)  / 64), (int) Math.floor((pyb + 1) / 64));
			 gR = currentLevel.getBlock((int)Math.floor((pxr - currentSpeed) / 64), (int) Math.floor((pyb + 1) / 64));
		}else if(uL.collisions || bL.collisions){
			 gL = currentLevel.getBlock((int)Math.floor((px + currentSpeed)  / 64), (int) Math.floor((pyb + 1) / 64));
			 gR = currentLevel.getBlock((int)Math.floor((pxr + currentSpeed) / 64), (int) Math.floor((pyb + 1) / 64));
		}
		
		if(uR.collisions && uL.collisions && bL.collisions) {
			 gL = currentLevel.getBlock((int)Math.floor((px + currentSpeed)  / 64), (int) Math.floor((pyb + 1) / 64));
			 gR = currentLevel.getBlock((int)Math.floor((pxr + currentSpeed) / 64), (int) Math.floor((pyb + 1) / 64));
		}
		
		if(uR.collisions && uL.collisions && bR.collisions) {
			 gL = currentLevel.getBlock((int)Math.floor((px - currentSpeed)  / 64), (int) Math.floor((pyb + 1) / 64));
			 gR = currentLevel.getBlock((int)Math.floor((pxr - currentSpeed) / 64), (int) Math.floor((pyb + 1) / 64));
		}
		
		
		if((!gL.collisions && !gR.collisions) && !jump && !gravity){
			gravity = true;
			resetGravity(currentTick);
		}
		
		//System.out.println("PlayerX: " + x);
		//System.out.println("PlayerY: " + y);
		//System.out.println("PlayerYBottom: " + (y + 64));
		//System.out.println("Jump: " + jump);
		//System.out.println("Jump Count: " + jumpCount);
		//System.out.println("Jump Availability: " + jumpAvailability);
		//System.out.println("Gravity: " + gravity);
		//System.out.println("GL: " + gL.collisions);
		//System.out.println("GRY: " + (gR.y * 64));
		
		
		
		if((gL.collisions || gR.collisions) && !jump) {
			//This is necessary to prevent the player from landing perfectly on a block then the jumpCount would not reset, which would cause the player to not be able to jump
			//System.out.println("Testing!");
			jumpCount = 0;
			jumpAvailability = true;
		}
		
		
		if(uR.collisions && !uL.collisions && !bL.collisions && !bR.collisions){
			if(pxr - currentSpeed <= uR.x * 64){
				setLeft = true;
				setDown = false;
				setUp = false;
			}else{
				setDown = true;
				setUp = false;
				setLeft = false;
			}
		}
		if(uL.collisions && !uR.collisions && !bL.collisions && !bR.collisions){
			if(px + currentSpeed >= (uL.x * 64) + 63){
				setRight = true;
				setDown = false;
				setUp = false;
			}else{
				setDown = true;
				setUp = false;
				setLeft = false;
			}
		}
		if(bL.collisions && !uR.collisions && !uL.collisions && !bR.collisions){
			if(px + currentSpeed >= (bL.x * 64) + 63){
				setRight = true;
				setDown = false;
				setUp = false;
			}else{
				setUp = true;
				setDown = false;
				setLeft = false;
			}
		}
		if(bR.collisions && !uL.collisions && !bL.collisions && !uR.collisions){
			if(pxr - currentSpeed <= bR.x * 64){
				setLeft = true;
				setDown = false;
				setUp = false;
			}else{
				setUp = true;
				setDown = false;
				setLeft = false;
			}
		}
		
		if(bL.collisions && bR.collisions){
			setUp = true;
			setLeft = false;
			setRight = false;
		}
		if(uL.collisions && uR.collisions){
			setDown = true;
			setLeft = false;
			setRight = false;
		}
		if(uR.collisions && bR.collisions){
			setLeft = true;
			setUp = false;
			setDown = false;
		}
		if(uL.collisions && bL.collisions){
			setRight = true;
			setUp = false;
			setDown = false;
		}
		if(uR.collisions && bL.collisions && gravity){
			setUp = true;
			setLeft = true;
		}
		if(uR.collisions && bL.collisions && jump){
			setDown = true;
			setRight = true;
		}
		if(uL.collisions && bR.collisions && gravity){
			setUp = true;
			setRight = true;
		}
		if(uL.collisions && bR.collisions && jump){
			setDown = true;
			setLeft = true;
		}
		
		if(uL.notFull){
			if(py > uL.y * 64 + uL.collisionPixelsHeight){
				setDown = false;
				setRight = false;
			}else if(!(px + currentSpeed >= (uL.x * 64) + 63)){
				notFull = true;
				offset = uL.collisionPixelsHeight;
			}
		}
		
		if(uR.notFull){
			if(py > uR.y * 64 + uR.collisionPixelsHeight){
				setDown = false;
				setLeft = false;
			}else if(!(pxr - currentSpeed <= uR.x * 64)){
				notFull = true;
				offset = uR.collisionPixelsHeight;
			}
		}
		
		if(uL.notFull && uR.notFull){
			if(!(py > uR.y * 64 + uR.collisionPixelsHeight)){
				setDown = true;
				setLeft = false;
				setRight = false;
			}
		}
		
		if(bL.notFull){
			
		}
		
		if(bR.notFull){
			
		}
		
		
		boolean data = false;
		if(data){
			System.out.println("UboveL: " + uL);
			System.out.println("UboveR: " + uR);
			System.out.println();
			System.out.println("BelowL: " + bL);
			System.out.println("BelowR: " + bR);
			System.out.println();
			System.out.println("GravityL: " + gL);
			System.out.println("GravityR: " + gR);
			System.out.println("Player X: " + x);
			System.out.println("Player Y: " + y);
			System.out.println(currentTick);
		}
		
		//System.out.println("Gravity: " + gravity);
		//System.out.println("Jumping: " + jump);
		//System.out.println("Player X: " + x);
		//System.out.println("Player Y: " + y);

		if(setDown){
			
			//System.out.println("Set Down");
			
			if(!notFull){
				y = (uR.y * 64) + 60;
			}else{
				y = (uR.y * 64) + (offset);
			}
			
			jump = false;
			jumpBuffer = 0;
			resetJump();	
			
		}
		if(setUp){
			
			//System.out.println("Set Up");
			
			if(!notFull){
				y = (bR.y * 64) - 64;
			}else{

			}
			
			jumpAvailability = true;
			jumpCount = 0;
			gravity = false;
			
			lastTimeOnFloor = 0;
		}
		if(setLeft){
			
			//System.out.println("Set Left: " + bR);
			
			if(!notFull){
				x = (uR.x * 64) - 56;
			}else{

			}
			
		}
		if(setRight){

			//System.out.println("Set Right");
			
			if(!notFull){
				x = (uL.x * 64) + 56;
			}else{
				
			}
			
		}
	}
	
	//This is to set the elevator mainly just to test for collisions.
	
	public void setElevator(Elevator elevator){
		this.elevator = elevator;
	}
	
	
	//THESE ARE THE COLLISIONS FOR THE SHIP
	
	public void checkCollisionShip(int currentTick){
		if(currentShipRoom.equals(new String("room"))){
			
			
			if(x + 4 <= 452){
				x = 448;
			}
			
			if(x + 63 >= 1472){
				x = 1472 - 64;
			}
			
			if((y + 63 < 779 && y + 4 > 303) && !jump && !gravity){
				gravity = true;
				resetGravity(currentTick);
			}
			
			if(y + 63 >= 780){
				y = 780 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
			
			if(y + 4 <= 304){
				y = 300;
				jump = false;
				jumpBuffer = 0;
				resetJump();	
			}
			

		}else if(currentShipRoom.equals(new String("dorm"))){
			

			
			if(y + 64 != elevator.colT){
				onElevator = false;
			}
			
			//gravity floor (bottom) for sections 9 and 10 due to overlap with elevator the noElevator variable is required only do this section when NOT ON ELEVATOR
			if(y + 63 < 1015 && y + 4 >= 555 && (x + 4 < 764 || x + 63 - 4 > 1152) && !jump && !gravity && !onElevator){
				gravity = true;
				onElevator = false;
				resetGravity(currentTick);
			}
			
			//gravity for sections 1
			if(y + 63 < 1015 && y + 4 >= 555 && x + 63 - 4 < 764 && !jump && !gravity){
				gravity = true;
				onElevator = false;
				resetGravity(currentTick);
			}
			
			//gravity for sections 2
			if(y + 63 < 1015 && y + 4 >= 555 && x + 4 > 1152 && !jump && !gravity){
				gravity = true;
				onElevator = false;
				resetGravity(currentTick);
			}
			
			//gravity floor(bottom) for sections 5 and 6
			if(y + 63 < 523 && y + 4 >= 63 && (x + 4 < 764 || x + 63 - 4 > 1152) && !jump && !gravity){
				gravity = true;
				onElevator = false;
				resetGravity(currentTick);
			}
			
			//gravity floor(bottom) for section 4
			if(y + 63 < 1039 && y + 4 >= elevator.colB - 1 && x + 4 >= 764 && x + 63 - 4 <= 1152  && !jump && !gravity){
				gravity = true;
				onElevator = false;
				resetGravity(currentTick);
			}
			
			//this tests the gravity ubove the elevator and has overlap with 9 and 10
			if(y + 63 < elevator.colT - 1 && y + 4 >= 63  && x + 4 + currentSpeed >= 764 && x + 63 - 4 - currentSpeed<= 1152  && !jump && !gravity){
				
				gravity = true;
				onElevator = false;
				resetGravity(currentTick);
			}
			
			
			// side (left) for sections 5 and 1
			if(x + 4 <= 4){
				x = 0;
			}
			
			//side (right) for sections 6 and 2
			if(x + 63 >= 1920){
				x = 1920 - 64;
			}
			
			//side (left) for section 4
			if(y + 63 >= 1016 && x + 63 - 4 >= 764 && x + 4 <= 764 && x + 4 + currentSpeed >= 764){
				x = 760;
			}
			
			//side (right) for section 4
			if(y + 63 >= 1016 && x + 4 <= 1152 && x + 63 - 4 >= 1152 && x + 63 - 4 - currentSpeed <= 1152){
				x = 1152 - 60;
			}
			
			//side (left) for section 3
			if(y + 63 > 524 && y + 4 < 556 && x + 63 - 4 >= 764 && x + 4 <= 764 && x + 4 + currentSpeed >= 764){
				x = 760;
			}
			
			//side (right) for section 3
			if(y + 63 > 524 && y + 4 < 556 && x + 4 <= 1152 && x + 63 - 4 >= 1152 && x + 63 - 4 - currentSpeed <= 1152){
				x = 1152 - 60;
			}
			
			//side(left) for section 3
			if(y + 63 > elevator.colT && y + 4 < elevator.colB && x + 4 <= elevator.colL && x + 63 - 4 >= elevator.colL && x + 63 - 4 - currentSpeed <= elevator.colL){
				x = elevator.colL - 60;
			}
			
			if(y + 63 > elevator.colT && y + 4 < elevator.colB && x + 63 - 4 >= elevator.colR && x + 4 <= elevator.colR && x + 4 + currentSpeed >= elevator.colR){
				x = elevator.colR - 4;
			}
			
			//ground floor (bottom) for sections 1 and 2
			if(y + 63 >= 1016 && y + 4 <= 1016 && (x + 4 < 764 || x + 63 - 4 > 1152)){
				y = 1016 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
			
			//ground floor(bottom) for sections 5 and 6
			if(y + 63 >= 524 && y + 4 <= 524 && (x + 4 < 764 || x + 63 - 4 > 1152)){
				y = 524 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
			
			
			//ground floor(bottom) for sections 8
			if(y + 63 >= elevator.colT && y + 4 <= elevator.colT && x + 4 < elevator.colR && x + 63 - 4 > elevator.colL){
				y = elevator.colT - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
				onElevator = true;
			}
			
			//ground floor(bottom) for section 4
			if(y + 63 >= 1040 && y + 4 <= 1040 && x + 4 >= 764 && x + 63 - 4 <= 1152){
				y = 1040 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
			
			//entire celing so top for sections 5, 7, and 6
			if(y + 4 <= 64 && y + 4 >= 0){
				y = 60;
				jump = false;
				jumpBuffer = 0;
				resetJump();	
			}
			
			//entire ceiling so top for sections 1 and 2
			if(y + 4 <= 556 && y + 4 >= 524 && (x + 4 < 764 || x + 63 - 4 > 1152)){
				y = 552;
				jump = false;
				jumpBuffer = 0;
				resetJump();
			}
			
			
			//entire ceiling so top for sections 8
			if(y + 4 <= elevator.colB && y + 4 >= elevator.colT && x + 4 < elevator.colR && x + 63 - 4 > elevator.colL){
				y = elevator.colB - 4;
				jump = false;
				jumpBuffer = 0;
				resetJump();
			}
			
		}else if(currentShipRoom.equals(new String("corridor"))){
			if(x + 4 <= 4){
				x = 0;
			}
			
			if(x + 63 >= 1920 * 2){
				x = 1920 * 2 - 64;
			}
			
			if((y + 63 < 1015 && y + 4 > 63) && !jump && !gravity){
				gravity = true;
				resetGravity(currentTick);
			}
			
			if(y + 4 <= 64){
				y = 60;
				jump = false;
				jumpBuffer = 0;
				resetJump();	
			}
			
			if(y + 63 >= 1016){
				y = 1016 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
		}else if(currentShipRoom.equals(new String("kingsChambers"))){
			
			if(x + 63 >= 1920){
				x = 1920 - 64;
			}
			
			if((y + 63 < 1015 && y + 4 > 63) && !jump && !gravity){
				gravity = true;
				resetGravity(currentTick);
			}
			
			if(y + 4 <= 64){
				y = 60;
				jump = false;
				jumpBuffer = 0;
				resetJump();	
			}
			
			if(y + 63 >= 1016){
				y = 1016 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
		}else if(currentShipRoom.equals(new String("podRoom"))){
			
			if(x + 63 >= 1920){
				x = 1920 - 64;
			}
			
			if(x + 4 <= 4){
				x = 0;
			}
			
			if((y + 63 < 1015 && y + 4 > 63) && !jump && !gravity){
				gravity = true;
				resetGravity(currentTick);
			}
			
			if(y + 4 <= 64){
				y = 60;
				jump = false;
				jumpBuffer = 0;
				resetJump();	
			}
			
			if(y + 63 >= 1016){
				y = 1016 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
		}else if(currentShipRoom.equals(new String("launchRoom"))){
			
			if(x + 63 >= 1920){
				x = 1920 - 64;
			}
			
			if(x + 4 <= 4){
				x = 0;
			}
			
			if((y + 63 < 1015 && y + 4 > 63) && !jump && !gravity){
				gravity = true;
				resetGravity(currentTick);
			}
			
			if(y + 4 <= 64){
				y = 60;
				jump = false;
				jumpBuffer = 0;
				resetJump();	
			}
			
			if(y + 63 >= 1016){
				y = 1016 - 64;
				jumpAvailability = true;
				jumpCount = 0;
				gravity = false;
			}
		}
	
	}

}


/*
 * Jump: false
JumpCounter: 0
Jump Buffer: 48
No Control Finished
Jump: false
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: true
JumpCounter: 0
Jump Buffer: 49
Jump: false
JumpCounter: 0
Jump Buffer: 0
Check Jump Start: 1
Jump: true
JumpCounter: 1
Jump Buffer: 0
Jump: true
JumpCounter: 1
Jump Buffer: 0
 * 
 */



/*
 * 		if(AnimationData == null){
			loadData(screen, 5, 9, 1, 1, 1);
		}
		
		int framerate = currentSpeed;
		int maxSprites = 8;
		
		if(animationCounter >= framerate * maxSprites){
			animationCounter = 0;
		}
		
		if(isRunning){
			for(int i = 0; i < maxSprites; i++){
				if(animationCounter >= i * framerate && animationCounter <= (i + 1) * framerate){
					if(movedD){
						screen.render(x, y, i, 8, 1, 1, 4, "Icons");
					}else if(movedA){
						screen.render(x, y, i, 9, 1, 1, 4, "Icons");
					}
				}
			}
		}else{
			if(!facing){
				//screen.render(x, y, 0, 8, 1, 1, 4, "Icons");
				animation.animateContinuous(screen, AnimationData, true, 8, 1, 1, x, y, 8, 4);
			}else{
				screen.render(x, y, 7, 9, 1, 1, 4, "Icons");
			}
		}
		
		animationCounter++;
 * 
 */
