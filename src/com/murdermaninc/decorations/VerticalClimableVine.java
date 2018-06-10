package com.murdermaninc.decorations;

import com.murdermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;

public class VerticalClimableVine extends Decoration{

	private boolean playerOn = false;

	//The 114 is the pixel length on the sprite sheet of the vine.
	//These nodes will not be rendered but the connection between the nodes will be interpolated then rendered.
	private Node[] nodes = new Node[114];
	private Constraint[] constraints = new Constraint[113];

	//113

	private Player player;

	public VerticalClimableVine(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

		//This is to offset the x value to the center of the tile and due to the fact that it gets render throught the use of particles
		//there is no downside to rendering it in this way.
		this.x = x + 28;
		
	}

	private boolean createNodes = true;
	private float mass = 10F;
	//private float dampaning = 0.05F;
	private float maxLength = 4.0F;
	//private int ropeMass = 1;

	
	private int playerIValue = 0;

	@Override
	public void tick(Player player) {
		if(this.player == null) this.player = player;

		
		if(player.input.e) {
			
			if(player.x + 8 <= x + nodes.length * 4 && player.x + 63 - 8 >= x - nodes.length * 4 && player.y + 4 <= y + nodes.length * 4 && player.y + 64 >= y ) { //The reason the last condition is only y is because the vine will only be attached to the top of the screen.
				
				float[] distances = new float[nodes.length];
				
				for(int i = 0; i < nodes.length; i++){
					
					distances[i] = distance((player.x + 32) - (nodes[i].x), (player.y + 32) - (y + Math.abs(nodes[i].y))); //The reason the y value is absolute is because we store the y value in the nodes inversly relative to they typical y coordinate system of a screen.
						
				}
				
				
				float lowestDistance = distances[0];
				int iValue = 0;
				
				for(int i = 0; i < distances.length; i++) {
					
					if(distances[i] < lowestDistance) {
						lowestDistance = distances[i];
						iValue = i;
					}
					
				}
				
				
				
				if(distances[iValue] <= 30.0F) {

					player.jump = false;
					player.gravity = false;
					playerOn = true;
					player.isClimbingVertical = true;
					
					if(iValue < nodes.length - 10) {
						playerIValue = iValue;
					}else {
						playerIValue = nodes.length - 10;
					}
					
					player.resetAnimation();

					player.input.e = false;
				}
				
				
			}
			
		}
		
		if(playerOn && !player.isClimbingVertical) {
			playerOn = false;
		}

		if(createNodes) {

			int xp = x;
			int yp = 0;

			for(int i = 0; i < nodes.length; i++) {


				if(i != 0) {
					nodes[i] = new Node(mass, xp, yp - (i * 4));
				}else if(i == 0){		
					nodes[i] = new Node(0, xp, yp);	
				}
				
				if(i != 0) {		
					constraints[i -1] = new Constraint(nodes[i - 1], nodes[i], maxLength);
				}

			}
			

			createNodes = false;
		}
		
		if(playerOn && player.input.w) {
			
			if(playerIValue > 0) {
				playerIValue--;
			}
			
		}
		
		if(playerOn && player.input.s) {
			
			if(playerIValue < nodes.length - 10) {
				playerIValue++;
			}
		}

		
		applyForces();
		integrate();
		
		for(int i = 0; i < 42; i++) {
			applyConstraints();
		}
		
		if(playerOn) {
			if(!player.facing) {
				player.x = (int) nodes[playerIValue].x - 28;
				player.y = y + Math.abs((int) nodes[playerIValue].y) - 12;
			}else {
				player.x = (int) nodes[playerIValue].x - 32;
				player.y = y + Math.abs((int) nodes[playerIValue].y) - 12;
			}
		}
		

	}
	
	public float distance(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}


	
	public void applyForces() {
		
		for(int i = 0; i < nodes.length; i++) {
			nodes[i].applyForce(0, -0.16F);
		}
		
		if(playerOn && player.movedA) {
			
			nodes[playerIValue].applyForce(-2F, 0);
			
		}
		
		if(playerOn && player.input.d) {
			
			nodes[playerIValue].applyForce(2F, 0);
		}
		
	}
	
	public void integrate() {
		
		for(int i = 0; i < nodes.length; i++) {
			nodes[i].integrate();
		}
		
	}
	
	public void applyConstraints() {
		
		for(int i = 0; i < constraints.length; i++) {
			constraints[i].solveConstraint();
		}
		
	}
	
	int scale = 4;

	@Override
	public void render(Screen screen, float interpolation) {
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");

		//screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		int windowLX = screen.screenX;
		int windowRX = screen.screenX + screen.width;
		int windowTY = screen.screenY;
		int windowBY = screen.screenY + screen.height;


		for(int i = 0; i < nodes.length; i++) {
			int nodeY = (int) (y + (-nodes[i].y)); 
			int nodeX =  (int) nodes[i].x;

			nodeY -= screen.screenY;
			nodeX -= screen.screenX;
			
			for(int dy = 0; dy < scale; dy++) {
				for(int dx = 0; dx < scale; dx++) {
					
					int xValue = nodeX + dx;
					int yValue = nodeY + dy;
					
					if(xValue >= windowLX - screen.screenX && xValue < windowRX - screen.screenX && yValue >= windowTY - screen.screenY && yValue < windowBY - screen.screenY){
					
						screen.pixels[xValue + (yValue * screen.width)] = -14336748;
						//-14930161
						
						screen.pixels[(xValue + 4) + (yValue * screen.width)] = -13808360;
						//-14666223
						
					}

				}
			}

		}


	}


	public class Node{

		float mass;

		/*Vector2D netForce = new Vector2D();
		Vector2D position;
		Vector2D lastPosition;
		Vector2D velocity = new Vector2D();*/
		
		
		float x, y;
		float lastX, lastY;
		float accX, accY;

		public Node(float mass, int x, int y) {
			this.mass = mass;
			
			this.x = x;
			this.y = y;
			this.lastX = x;
			this.lastY = y;
			this.accX = 0;
			this.accY = 0;
			
		}
		
		public void applyForce(float accX, float accY) {
			
			this.accX += accX;
			this.accY += accY;
			
		}
		
		float velocityDampening = 0.999F;

		public void integrate() {
			if(mass != 0) {
				
				
				float vx = x - lastX;
				float vy = y - lastY;
				
				lastX = x;
				lastY = y;
				
				x += vx * velocityDampening;
				y += vy * velocityDampening;
				
				x += accX;
				y += accY;

				
				
			}
				
			accX = 0;
			accY = 0;
			
		}

	}
	
	public class Constraint{
		
		
		Node node1;
		Node node2;
		float maxDistance;
		
		public Constraint(Node node1, Node node2, float maxDistance) {
			
			this.node1 = node1;
			this.node2 = node2;
			this.maxDistance = maxDistance;
			
		}
		
		
		public void solveConstraint() {
			
			float xChange = node2.x - node1.x;
			float yChange = node2.y - node1.y;
			
			float distance = distance(xChange, yChange);
			
			float difference = maxDistance - distance;
			
			float percentage = difference / distance * 0.98F;
			
			float offsetX = xChange * percentage;
			float offsetY = yChange * percentage;
			
			node2.x += offsetX;
			node2.y += offsetY;
			
			if(node1.mass != 0) {
				node1.x -= offsetX;
				node1.y -= offsetY;
			}

		}
		
		public float distance(float x1, float y1) {
			return (float) Math.sqrt(x1 * x1 + y1 * y1);
		}
		
		
	}


}
























/*
 * 
 * package com.murdermaninc.decorations;

import com.murdermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;

public class VerticalClimableVine extends Decoration{

	private boolean playerOn = false;

	//The 114 is the pixel length on the sprite sheet of the vine.
	//These nodes will not be rendered but the connection between the nodes will be interpolated then rendered.
	private Node[] nodes = new Node[114];

	//113

	private Player player;

	public VerticalClimableVine(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}

	boolean createNodes = true;
	float mass = 10F;
	Vector2D gravitationAceleration = new Vector2D(0, -9.81F);
	float deltaT = 1F / 60F; //seconds
	float springConstant = 35100.0F;//10780
	float dampaning = 0.1F;
	float springLength = 4.0F;


	@Override
	public void tick(Player player) {
		if(this.player == null) this.player = player;
		if(player.x + 63 - 8 >= x && player.x + 8 <= x + 63 && player.y + 63 >= y && player.y + 4 <= y + (spriteHeight * 64 - 1) && player.input.e && !player.isClimbingVertical){
			player.input.e = false;
			playerOn = true;
			player.isClimbingVertical = true;
			player.jump = false;
			player.gravity = false;

		}

		if(player.input.e && player.isClimbingVertical){
			player.input.e = false;
			player.isClimbingVertical = false;
			playerOn = false;
			player.jumpCount = 1;
		}

		if(createNodes) {

			int xp = x + 28;
			int yp = 0;

			for(int i = 0; i < nodes.length; i++) {


				if(i != 0) {

					nodes[i] = new Node(mass, xp, yp - (i * 4));

				}else {
					nodes[i] = new Node(0, xp, yp);
				}

			}


			createNodes = false;
		}

		//applySpring();
		//calculateMovement();
		//simulate();
		


		applyForces();
		inertia();
		for(int i = 0; i < 100; i++) {
			applyConstraints();
		}


	}

	//Gravitational Acceleration was calculated from tests with player physics.
	//float gravitationalAcceleration = -18F; //px per second
	
	/*private void resetForces() {

		for(int i = 0; i < nodes.length; i++) {
			nodes[i].init();
		}

	}*/

/*	int end = 0;
	boolean applyOnce = true;
	
	private void applyConstraints() {
		for(int i = 0; i < nodes.length - 1; i++) {
			
			float xDelta = nodes[i + 1].x - nodes[i].x;

			float xDeltaLength = (float) Math.sqrt(xDelta * xDelta);
			
			float xDiff = (xDeltaLength - 4.0F) / xDeltaLength;
			
			nodes[i].x -= xDelta * 0.5 * xDiff;
			nodes[i + 1].x +=  xDelta * 0.5 * xDiff;
			
			float yDelta = nodes[i + 1].y - nodes[i].y;

			float yDeltaLength = (float) Math.sqrt(yDelta * yDelta);
			
			float yDiff = (yDeltaLength - 4.0F) / yDeltaLength;
			
			nodes[i].y -= yDelta * 0.5 * yDiff;
			nodes[i + 1].y +=  yDelta * 0.5 * yDiff;
			
		}
	}

	private void applyForces() {

		gravitationAceleration = new Vector2D(0, -1F);
		
		gravitationAceleration.scaleUVector(mass);
		
		

		for(int i = 0; i < nodes.length; i++) {
			//nodes[i].applyForces(gravitationAceleration);

		}

		//System.out.println("Gravity force: " + gravitationAceleration.length());
		
		if(applyOnce) {
			//Vector2D dforce = new Vector2D(-10000, 0);

			
			//nodes[56].applyForces(dforce);
			
			applyOnce = false;
		}
		
		//if(applyOnce) {


			
			//applyOnce = false;
		//}

		//System.out.println("");
		//System.out.println("!!!!!!!Started Spring Forces");
		/*for(int i = 0; i < nodes.length - 1; i++) {


			//F = -k * x
			Vector2D springVector = nodes[i].position.copyVector();
			
			springVector.subtractVector(nodes[i + 1].position);
			
			//System.out.println(springVector.y);
			
			float r = springVector.length();
			
			//System.out.println("R: " + r);
			
			Vector2D force = new Vector2D();
			
			//F = -k(x - d) - bv
			
			if(r != 0) {

				//-k(x - d)
				springVector.scaleDVector(r);
				springVector.scaleUVector(r - springLength);
				springVector.scaleUVector(-springConstant);

				// - bv
				//Vector2D dampeningVector = nodes[i].velocity.copyVector();
				
			//	dampeningVector.subtractVector(nodes[i + 1].velocity);
				//dampeningVector.scaleUVector(dampaning);
				
				//springVector.subtractVector(dampeningVector);
				
				//force.addVector(springVector);
			}
			
			
			//System.out.println(springVector.y);
			
			//Vector2D frictionVector = nodes[i].velocity.copyVector();
			
			//frictionVector.subtractVector(nodes[i + 1].velocity);
			//frictionVector.reverseVector();
			//frictionVector.scaleUVector(frictionConstant);
			
			//System.out.println(frictionVector.y);
			
			//force.addVector(frictionVector);
			
			nodes[i].applyForces(force);
			force.reverseVector();
			nodes[i + 1].applyForces(force);
			
		//	System.out.println(force.y);



		}*/
		//System.out.println("!!!!!!Stop Spring Forces");
		//System.out.println("");



	/*private void simulate() {

		for(int i = 0; i < nodes.length; i++) {
			//nodes[i].simulate(deltaT);
		}

	}*/
	
/*	private void inertia() {
		
		for(int i = 0; i < nodes.length; i++) {
			nodes[i].inertia(deltaT);
		}
		
	}




	int scale = 4;

	@Override
	public void render(Screen screen, float interpolation) {
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");

		//screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);


		for(int i = 0; i < nodes.length; i++) {
			int nodeY = (int) (y + (-nodes[i].y)); 
			int nodeX =  (int) nodes[i].x;

			/*if(i != 0) {
				nodeY = (int) (y + (-nodes[i].position.y)); 
				nodeX =  (int) nodes[i].position.x;
			}else {
				nodeY = y;
				nodeX = x + 28;
			}*/


			//System.out.println(nodeY);
			//System.out.println(nodeY);
	/*		for(int dy = 0; dy < scale; dy++) {
				for(int dx = 0; dx < scale; dx++) {
					try {
						screen.pixels[(nodeX + dx) + ((nodeY + dy) * screen.width)] = -324342;
					}catch(ArrayIndexOutOfBoundsException e) {

					}

				}
			}

		}


	}


	public class Node{

		float mass;

		float x, y;
		float lastX, lastY;
		Vector2D netAcceleration = new Vector2D();

		public Node(float mass, int x, int y) {
			this.mass = mass;
			
			this.x = x;
			this.y = y;
			this.lastX = x;
			this.lastY = y;

			//System.out.println(position.x);
			//eSystem.out.println(position.y);
			
		}

		public void applyForces(Vector2D v) {

			Vector2D force = v.copyVector();
			force.scaleDVector(mass);
			
			netAcceleration.addVector(v);

		}

		/*public void init() {
			netAcceleration.x = 0;
			netAcceleration.y = 0;
		}*/

		/*public void simulate(float dt) {

			if(mass != 0) {

				//velocity += acceleration * dt;
				
				
				Vector2D acceleration = netAcceleration.copyVector();
				
				acceleration.scaleUVector(dt);
				
				velocity.addVector(acceleration);
				
				System.out.println("Velocity: " + velocity.length());
				
				//position += velocity * dt;
				
				Vector2D velocityCalc = velocity.copyVector();
				
				velocityCalc.scaleUVector(dt);
				
				position.addVector(velocityCalc);
			}



		}*/

		
/*		public void inertia(float dt) {
			
			if(mass != 0) {

				float accX = netAcceleration.x;
				float accY = netAcceleration.y;
					
				x += x-lastX + accX * dt * dt;
				y += y-lastY * accY * dt * dt;
				
				lastX = x;
				lastY = y;


				netAcceleration.x = 0;
				netAcceleration.y = 0;


			}
			
			
			
		}

	}

	public class Vector2D{

		float x,y;

		public Vector2D(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public Vector2D() {
			this.x = 0;
			this.y = 0;
		}

		public void addVector(Vector2D v) {

			this.x += v.x;
			this.y += v.y;

		}

		public void subtractVector(Vector2D v) {

			this.x -= v.x;
			this.y -= v.y;

		}

		public void scaleUVector(float scale) {

			this.x *= scale;
			this.y *= scale;

		}

		public void scaleDVector(float scale) {

			this.x /= scale;
			this.y /= scale;

		}

		public float length() {
			return (float) Math.sqrt((x * x) + (y * y));
		}

		public void reverseVector() {
			this.x = -this.x;
			this.y = -this.y;
		}

		public Vector2D copyVector() {
			return new Vector2D(this.x, this.y);
		}


	}

}
 * 
 * 
 * 
 */


/*
 * package com.murdermaninc.decorations;

import com.murdermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;

public class VerticalClimableVine extends Decoration{

	private boolean playerOn = false;

	//The 114 is the pixel length on the sprite sheet of the vine.
	//These nodes will not be rendered but the connection between the nodes will be interpolated then rendered.
	private Node[] nodes = new Node[114];
	private Constraint[] constraints = new Constraint[113];

	//113

	private Player player;

	public VerticalClimableVine(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}

	boolean createNodes = true;
	float mass = 10F;
	float deltaT = 1F / 60F; //seconds
	float maxLength = 4.0F;


	@Override
	public void tick(Player player) {
		if(this.player == null) this.player = player;
		if(player.x + 63 - 8 >= x && player.x + 8 <= x + 63 && player.y + 63 >= y && player.y + 4 <= y + (spriteHeight * 64 - 1) && player.input.e && !player.isClimbingVertical){
			player.input.e = false;
			playerOn = true;
			player.isClimbingVertical = true;
			player.jump = false;
			player.gravity = false;

		}

		if(player.input.e && player.isClimbingVertical){
			player.input.e = false;
			player.isClimbingVertical = false;
			playerOn = false;
			player.jumpCount = 1;
		}

		if(createNodes) {

			int xp = x + 28;
			int yp = 0;

			for(int i = 0; i < nodes.length; i++) {


				if(i != 0) {
					nodes[i] = new Node(mass, xp - (i * 4), yp);
				}else {		
					nodes[i] = new Node(0, xp, yp);	
				}
				
				if(i != 0) {		
					constraints[i -1] = new Constraint(nodes[i - 1], nodes[i], maxLength);
				}

			}

			createNodes = false;
		}
		
		applyForces();
		integrate();
		applyConstraints();

	}

	
	public void applyForces() {
		
		for(int i = 0; i < nodes.length; i++) {
			nodes[i].applyForce(0, (float) (nodes[i].mass * -50));
		}
		
		if(player.input.m) {
			nodes[65].applyForce(-600, 900);
			player.input.m = false;
		}
		
	}
	
	public void integrate() {
		
		for(int i = 0; i < nodes.length; i++) {
			nodes[i].integrate(deltaT);
		}
		
	}
	
	public void applyConstraints() {
		
		for(int i = 0; i < constraints.length; i++) {
			constraints[i].solveConstraint();
		}
		
	}
	
	int scale = 4;

	@Override
	public void render(Screen screen, float interpolation) {
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");

		//screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);


		for(int i = 0; i < nodes.length; i++) {
			int nodeY = (int) (y + (-nodes[i].y)); 
			int nodeX =  (int) nodes[i].x;

			for(int dy = 0; dy < scale; dy++) {
				for(int dx = 0; dx < scale; dx++) {
					try {
						screen.pixels[(nodeX + dx) + ((nodeY + dy) * screen.width)] = -324342;
					}catch(ArrayIndexOutOfBoundsException e) {

					}

				}
			}

		}


	}


	public class Node{

		float mass;

		float x, y;
		float lastX, lastY;
		float accX, accY;
		float netForceX, netForceY;

		public Node(float mass, int x, int y) {
			this.mass = mass;
			
			this.x = x;
			this.y = y;
			this.lastX = x;
			this.lastY = y;
			this.accX = 0;
			this.accY = 0;
			
		}
		
		public void applyForce(float forceX, float forceY) {
			
			netForceX += forceX;
			netForceY += forceY;
			
		}

		public void integrate(float dt) {
			if(mass != 0) {
				accX = netForceX / mass;
				accY = netForceY / mass;

				// xi+1 = xi + (xi - xi-1) + a * dt * dt

				x = x + (x - lastX) + accX * dt * dt;
				y = y + (y - lastY) + accY * dt * dt;


				lastX = x;
				lastY = y;

				accX = 0;
				accY = 0;
			}
			
		}

	}
	
	public class Constraint{
		
		
		Node node1;
		Node node2;
		float maxDistance;
		
		public Constraint(Node node1, Node node2, float maxDistance) {
			
			this.node1 = node1;
			this.node2 = node2;
			this.maxDistance = maxDistance;
			
		}
		
		
		public void solveConstraint() {
			
			float diffX = node2.x - node1.x;
			float diffY = node2.y - node1.y;

			
			float r = (float) Math.sqrt(diffX * diffX + diffY * diffY);
			
			
			
			if(r > maxDistance) {
				
				float directionX = diffX / r;
				float directionY = diffY / r;
				
				directionX *= (r - maxDistance);
				directionY *= (r - maxDistance);
				
				node2.x -= directionX;
				node2.y -= directionY;
				
				
			}

			
		}
		
		
	}
	
 * 
 * 
 * 
 */




