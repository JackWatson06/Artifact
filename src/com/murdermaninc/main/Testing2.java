package com.murdermaninc.main;

public class Testing2{

	public static boolean[] xArray = new boolean[2_000_000_000];

	public static void main(String[] args){

	    long timeStart = System.currentTimeMillis();

	    int numberOfLoops = 0;
	    int uboveMax = 0;

	    long timeTaken = System.currentTimeMillis();

	    System.out.println("Number of Loops: " + numberOfLoops);
	    System.out.println("Ubove Max: " + uboveMax);
	    System.out.println("Time Taken(MS): " + (timeTaken - timeStart));
		
	}
	
}

//Average Without break ms: 
//Average Without break ns: 
//Average With break ms: 349.6
//Average With break ns: 349901376

// In your opinion would an outcome like this be caused by my Eclipse 