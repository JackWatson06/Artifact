package com.murdermaninc.main;


public class Testing{
	
	public static int[] testing = new int[10];
	
	public static void main(String[] args){
		
		int totalIValues = 0;
		
		for(int i = 0; i < testing.length; i++){
			totalIValues++;
		}
		
		System.out.println(totalIValues);
		totalIValues = 0;
		
		for(int i = 0; i < testing.length; i++){
			if(i == 4){
				testing = new int[2];
			}
			totalIValues++;
		}
		
		System.out.println(totalIValues);
		
	}
}
