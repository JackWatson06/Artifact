package com.mudermaninc.entity;

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
	
	private int lastX = 0;
	private int lastY = 0;
	
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
	public ArrayList<int[]> runAnimationRight;
	public ArrayList<int[]> runAnimationLeft;
	public ArrayList<int[]> jumpFlame;
	public ArrayList<int[]> deflatKillRight;
	public ArrayList<int[]> deflatKillLeft;
	public ArrayList<int[]> climbingRight;
	public ArrayList<int[]> climbingBackHandRight;
	public ArrayList<int[]> climbingLeft;
	public ArrayList<int[]> climbingBackHandLeft;
	public ArrayList<int[]> poisonKillRight;
	public ArrayList<int[]> poisonKillLeft;
	
	private int deathX, deathY;
	
	private int darkOffset = 0;
	
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
	

	
	
	
	
	public Player(String name, InputManager input, Level currentLevel, int x, int y) {
		this.name = name;
		this.input = input;
		this.x = x;
		this.y = y;
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
			noControlCounter++;
			if(noControlCounter >= 5){
				noControl = false;
				noControlCounter = 0;
			}
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
		
		if(!onElevator){
			gravity(currentTick, interpolation);
		}else{
			y++;
		}
		
		
		
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
			if(input.w) {
				y -= climbingSpeed;
				movedW = true;
			}
			if(input.s) {
				y += (int) (climbingSpeed * 1.5F);
				movedS = true;
			}
			
			if(input.a) {
				movedA = true;
			}
			
			if(input.d) {
				movedD = true;
			}
		}
		
		//Detects when the player let goes of a vine
		if(isClimbingHorizontal){
			if(input.e){
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
			if(inputTick <= jGTick){
				double a = -jGSpeed / Math.pow(jGTick, 2);
				double equation = ((double)a * Math.pow(inputTick, 2)) + (((double) (-jGTick * 2) * a) * inputTick);
				y += (int) (equation * interpolation);
				currentGravitySpeed = (int) (equation * interpolation);
			}else{
				y += (int) (jGSpeed * interpolation);
				currentGravitySpeed = (int) (jGSpeed * interpolation);
			}
		}else{
			currentGravitySpeed = 0;
			resetGravity(currentTick);
		}
		
	}
	
	public void checkJumpStart(){
		if(currentLevel != null){
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

			if(!gL.collisions && !gR.collisions){
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
	
	@Override
	public void render(Screen screen, float interpolation, float testInterpolation){
		if(runAnimationRight == null) runAnimationRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 1 + darkOffset, 1, 1);
		if(runAnimationLeft == null) runAnimationLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 2 + darkOffset, 1, 1);
		if(jumpFlame == null) jumpFlame = animation.loadAnimationData(screen, "player", 4, 4, 0, 3, 1, 1);
		
		if(currentLevel != null) {
			if(currentLevel.worldNumber == 1) {
				if(deflatKillRight == null) deflatKillRight = animation.loadAnimationData(screen, "player", 4, 8, 0, 4 + darkOffset, 1, 1);
				if(deflatKillLeft == null) deflatKillLeft = animation.loadAnimationData(screen, "player", 4, 8, 0, 5 + darkOffset, 1, 1);
			}
		
			if(currentLevel.worldNumber == 2) {
				if(climbingRight == null) climbingRight = animation.loadAnimationData(screen, "player", 4, 8, 16, 0, 1, 1);
				if(climbingLeft == null) climbingLeft = animation.loadAnimationData(screen, "player", 4, 8, 16, 2, 1, 1);
				if(climbingBackHandRight == null) climbingBackHandRight = animation.loadAnimationData(screen, "player", 4, 8, 16, 1, 1, 1);
				if(climbingBackHandLeft == null) climbingBackHandLeft = animation.loadAnimationData(screen, "player", 4, 8, 16, 3, 1, 1);
				if(poisonKillRight == null) poisonKillRight = animation.loadAnimationData(screen, "player", 4, 11, 13, 4, 1, 1);
				if(poisonKillLeft == null) poisonKillLeft = animation.loadAnimationData(screen, "player", 4, 11, 13, 5, 1, 1);
			}
		}

		int testingX = (int) ((x - lastX) * interpolation + lastX);
		int testingY = (int) ((y - lastY) * interpolation + lastY);
		
		//System.out.println("Testing X: " + testingX + " Actual X: " + x);
		//System.out.println("Testing Y: " + testingY + " Actual Y: " + y);
		
		if(jumpingAir){
			flameAnimation.animateOnce(screen, jumpFlame, false, 10.0F, 1, 1, testingX + 16, testingY + 60, 4, 4, interpolation);
		}else{
			flameAnimation.resetCurrentAnimation();
		}
		
		int fps = 10;
		
		if(isRunning && currentSpeed == runningSpeed){
			fps = 15;
		}
		
		if(isRunning && !dying && !isClimbingHorizontal && !isClimbingVertical){
			
			if(currentSpeed != lastSpeed){
				if(currentSpeed == runningSpeed){
					animation.updateContinuous(10, 15);
				}else{
					
					animation.updateContinuous(15, 10);
				}
				
			}
			lastSpeed = currentSpeed;
			
			if(movedD){
				animation.animateContinuous(screen, runAnimationRight, false, fps, 1, 1, testingX, testingY, 8, 4, interpolation);
			}else if(movedA){
				animation.animateContinuous(screen, runAnimationLeft, false, fps, 1, 1, testingX, testingY, 8, 4, interpolation);
			}

		}else if(!dying && !isClimbingHorizontal){
			if(!facing){
				screen.render(x, y, 0, 1 + darkOffset, 1, 1, 4, "player");
				//killAnimation.animateContinuous(screen, poisonKillRight, false, 8, 1, 1, x, y, 10, 4);
			}else{
				screen.render(x, y, 0, 2 + darkOffset, 1, 1, 4, "player");
			}
		}else if(isClimbingHorizontal && isClimbingVertical && !dying){
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
		}
		
		if(dying){
			System.out.println(deathTag);
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
			}
		}
		
		


		
	}
	
	public void renderBeforeAllDecorations(Screen screen){
		if(isClimbingHorizontal) {
			if(isRunning){
				if(!facing){
					screen.renderData(climbingBackHandRight.get(animation.getCurrentSprite()), x, y, 1, 1, 4);
				}else{
					screen.renderData(climbingBackHandLeft.get(animation.getCurrentSprite()), x, y, 1, 1, 4);
				}
			}
		}
	}
	
	public void setDarkOffset(int darkOffset){
		this.darkOffset = darkOffset;
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
		if((!gL.collisions && !gR.collisions) && !jump && !gravity){
			gravity = true;
			resetGravity(currentTick);
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

		if(setDown){
			
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
			
			if(!notFull){
				y = (bR.y * 64) - 64;
			}else{

			}
			
			jumpAvailability = true;
			jumpCount = 0;
			gravity = false;
		}
		if(setLeft){
			
			if(!notFull){
				x = (uR.x * 64) - 56;
			}else{

			}
			
		}
		if(setRight){

			if(!notFull){
				x = (uL.x * 64) + 56;
			}else{
				
			}
			
		}
	}
	
	public void setElevator(Elevator elevator){
		this.elevator = elevator;
	}
	
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
