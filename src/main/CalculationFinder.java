package com.murdermaninc.main;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculationFinder {

	
	private static String[] numbers = {"90", "64", "2", "1.2"};
	//private static String[] subInNumbers = {"2175", "(-1.5)", "256", "1080", "3264"};
	private static String[] equationSigns = {"+", "-", "/", "*"};
	private static int numberOfEquationNumbers = 5;
	
	private static ArrayList<String> workingEquations = new ArrayList<String>();
	
	//private static int resultInt = 3008;
	private static int resultInt = 2270;
	private static float resultFloat = (float) resultInt;
	private static double resultDouble = (double) resultInt;
	private static long resultLong = (long) resultInt;
	
	private static int secondResultInt = 3002;
	private static float secondResultFloat = (float) secondResultInt;
	private static double secondResultDouble = (double) secondResultInt;
	private static long secondResultLong = (long) secondResultInt;
	
	
	public static void main(String[] args){
		
			findEquation();
			System.out.println("=========DONE FINDING EQUATIONS========");
			//supInNumbers();
		}	
	
	public static void supInNumbers(){
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		
		for(int i = 0; i < workingEquations.size(); i++){
			String currentEquation = workingEquations.get(i);
			String subbedEquation = new String("");
			
			String currentNumber = new String("");
			for(int j = 0; j < currentEquation.length() + 1; j++){
				if(j != currentEquation.length()){
					char currentChar = currentEquation.charAt(j);
					char behindChar = '1';
					if(j >= 1){
						behindChar = currentEquation.charAt(j - 1);
					}
					if(currentChar != '+' && (currentChar != '-' && behindChar != '(') && currentChar != '/'  && currentChar != '*' ){
						currentNumber += currentChar;
					}else if(behindChar != '('){
						boolean doesNotContainParen = true;
						for(int x = 0; x < currentNumber.length(); x++){
							char currentNumberChar = currentNumber.charAt(x);
							if(currentNumberChar == '('){
								doesNotContainParen = false;
							}
						}
						
						float currentNumberFloat = 0.0F;
						
						if(doesNotContainParen){
							currentNumberFloat = Float.parseFloat(currentNumber);
						}
					
						if(currentNumberFloat == 2184.0){
							subbedEquation += Integer.toString(2175);
							subbedEquation +=currentChar;
							currentNumber = new String("");
						}else{
							subbedEquation +=currentNumber;
							subbedEquation +=currentChar;
							currentNumber = new String("");
						}
					
					
					}else{
						currentNumber+="-";
					}
				}else{
					boolean doesNotContainParen = true;
					for(int x = 0; x < currentNumber.length(); x++){
						char currentNumberChar = currentNumber.charAt(x);
						if(currentNumberChar == '('){
							doesNotContainParen = false;
						}
					}
					
					float currentNumberFloat = 0.0F;
					
					if(doesNotContainParen){
						currentNumberFloat = Float.parseFloat(currentNumber);
					}
					
					if(currentNumberFloat == 2184.0){
						subbedEquation += Integer.toString(2175);
						currentNumber = new String("");
					}else{
						subbedEquation +=currentNumber;
						currentNumber = new String("");
					}
				}
			}

			try {
				Object subbedValue = engine.eval(subbedEquation);

				if(subbedValue.equals(new Integer(secondResultInt))){
					System.out.println(subbedEquation + " = " + Integer.toString(secondResultInt));
				}else if(subbedValue.equals(new Double(secondResultDouble))){
					System.out.println(subbedEquation + " = " + Integer.toString(secondResultInt));
				}else if(subbedValue.equals(new Float(secondResultFloat))){
					System.out.println(subbedEquation + " = " + Integer.toString(secondResultInt));
				}else if(subbedValue.equals(new Long(secondResultLong))){
					System.out.println(subbedEquation + " = " + Integer.toString(secondResultInt));
				}
			} catch (ScriptException e) {
				e.printStackTrace();
			}

		}
	}
	
	
	
	public static void findEquation(){
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		for(int x = 1; x < numberOfEquationNumbers; x++){
				if(x == 1){
					for(int baseNumber = 0; baseNumber < numbers.length; baseNumber++){
						for(int j = 0; j < equationSigns.length; j++){
							for(int secondaryNumber = 0; secondaryNumber < numbers.length; secondaryNumber++){
								String equation = numbers[baseNumber] + equationSigns[j] + numbers[secondaryNumber];
								try {
									Object value = engine.eval(equation);
									if(value.equals(new Integer(resultInt))){
										System.out.println(equation + " = " + Integer.toString(resultInt));
										workingEquations.add(equation);
									}else if(value.equals(new Double(resultDouble))){
										System.out.println(equation + " = " + Integer.toString(resultInt));
										workingEquations.add(equation);
									}else if(value.equals(new Float(resultFloat))){
										System.out.println(equation + " = " + Integer.toString(resultInt));
										workingEquations.add(equation);
									}else if(value.equals(new Long(resultLong))){
										System.out.println(equation + " = " + Integer.toString(resultInt));
										workingEquations.add(equation);
									}
								} catch (ScriptException e) {
									e.printStackTrace();
								}
							}
						}
						
					}
				}else if(x == 2){
						for(int baseNumber = 0; baseNumber < numbers.length; baseNumber++){
							for(int firstSign = 0; firstSign < equationSigns.length; firstSign++){
								for(int secondaryNumber = 0; secondaryNumber < numbers.length; secondaryNumber++){
									for(int secondSign = 0; secondSign < equationSigns.length; secondSign++){
										for(int tertiaryNumber = 0; tertiaryNumber < numbers.length; tertiaryNumber++){
											String equation = numbers[baseNumber] + equationSigns[firstSign] + numbers[secondaryNumber] + equationSigns[secondSign] + numbers[tertiaryNumber];
											try {
												Object value = engine.eval(equation);
												if(value.equals(new Integer(resultInt))){
													System.out.println(equation + " = " + Integer.toString(resultInt));
													workingEquations.add(equation);
												}else if(value.equals(new Double(resultDouble))){
													System.out.println(equation + " = " + Integer.toString(resultInt));
													workingEquations.add(equation);
												}else if(value.equals(new Float(resultFloat))){
													System.out.println(equation + " = " + Integer.toString(resultInt));
													workingEquations.add(equation);
												}else if(value.equals(new Long(resultLong))){
													System.out.println(equation + " = " + Integer.toString(resultInt));
													workingEquations.add(equation);
												}
											} catch (ScriptException e) {
												e.printStackTrace();
											}
										}
									}
								}
							}
							
					}
				}else if(x == 3){
					for(int baseNumber = 0; baseNumber < numbers.length; baseNumber++){
						for(int firstSign = 0; firstSign < equationSigns.length; firstSign++){
							for(int secondaryNumber = 0; secondaryNumber < numbers.length; secondaryNumber++){
								for(int secondSign = 0; secondSign < equationSigns.length; secondSign++){
									for(int tertiaryNumber = 0; tertiaryNumber < numbers.length; tertiaryNumber++){
										for(int thridSign = 0; thridSign < equationSigns.length; thridSign++){
											for(int quartinaryNumber = 0; quartinaryNumber < numbers.length; quartinaryNumber++){
												String equation = numbers[baseNumber] + equationSigns[firstSign] + numbers[secondaryNumber] + equationSigns[secondSign] + numbers[tertiaryNumber] + equationSigns[thridSign] + numbers[quartinaryNumber];
												try {
													Object value = engine.eval(equation);
													if(value.equals(new Integer(resultInt))){
														System.out.println(equation + " = " + Integer.toString(resultInt));
														workingEquations.add(equation);
													}else if(value.equals(new Double(resultDouble))){
														System.out.println(equation + " = " + Integer.toString(resultInt));
														workingEquations.add(equation);
													}else if(value.equals(new Float(resultFloat))){
														System.out.println(equation + " = " + Integer.toString(resultInt));
														workingEquations.add(equation);
													}else if(value.equals(new Long(resultLong))){
														System.out.println(equation + " = " + Integer.toString(resultInt));
														workingEquations.add(equation);
													}
												} catch (ScriptException e) {
													e.printStackTrace();
												}
											}
										}
									}
								}
							}
						}
						
					}
				}else if(x == 4){
					for(int baseNumber = 0; baseNumber < numbers.length; baseNumber++){
						for(int firstSign = 0; firstSign < equationSigns.length; firstSign++){
							for(int secondaryNumber = 0; secondaryNumber < numbers.length; secondaryNumber++){
								for(int secondSign = 0; secondSign < equationSigns.length; secondSign++){
									for(int tertiaryNumber = 0; tertiaryNumber < numbers.length; tertiaryNumber++){
										for(int thridSign = 0; thridSign < equationSigns.length; thridSign++){
											for(int quartinaryNumber = 0; quartinaryNumber < numbers.length; quartinaryNumber++){
												for(int fourthSign = 0; fourthSign < equationSigns.length; fourthSign++){
													for(int quinaryNumber = 0; quinaryNumber < numbers.length; quinaryNumber++){
														String equation = numbers[baseNumber] + equationSigns[firstSign] + numbers[secondaryNumber] + equationSigns[secondSign] + numbers[tertiaryNumber] + equationSigns[thridSign] + numbers[quartinaryNumber] + equationSigns[fourthSign] + numbers[quinaryNumber];
														try {
															Object value = engine.eval(equation);
															if(value.equals(new Integer(resultInt))){
																System.out.println(equation + " = " + Integer.toString(resultInt));
																workingEquations.add(equation);
															}else if(value.equals(new Double(resultDouble))){
																System.out.println(equation + " = " + Integer.toString(resultInt));
																workingEquations.add(equation);
															}else if(value.equals(new Float(resultFloat))){
																System.out.println(equation + " = " + Integer.toString(resultInt));
																workingEquations.add(equation);
															}else if(value.equals(new Long(resultLong))){
																System.out.println(equation + " = " + Integer.toString(resultInt));
																workingEquations.add(equation);
															}
														} catch (ScriptException e) {
															e.printStackTrace();
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
					}
				}else{
					for(int baseNumber = 0; baseNumber < numbers.length; baseNumber++){
						for(int firstSign = 0; firstSign < equationSigns.length; firstSign++){
							for(int secondaryNumber = 0; secondaryNumber < numbers.length; secondaryNumber++){
								for(int secondSign = 0; secondSign < equationSigns.length; secondSign++){
									for(int tertiaryNumber = 0; tertiaryNumber < numbers.length; tertiaryNumber++){
										for(int thridSign = 0; thridSign < equationSigns.length; thridSign++){
											for(int quartinaryNumber = 0; quartinaryNumber < numbers.length; quartinaryNumber++){
												for(int fourthSign = 0; fourthSign < equationSigns.length; fourthSign++){
													for(int quinaryNumber = 0; quinaryNumber < numbers.length; quinaryNumber++){
														for(int fithSign = 0; fithSign < equationSigns.length; fithSign++){
															for(int senaryNumber = 0; senaryNumber < numbers.length; senaryNumber++){
																String equation = numbers[baseNumber] + equationSigns[firstSign] + numbers[secondaryNumber] + equationSigns[secondSign] + numbers[tertiaryNumber] + equationSigns[thridSign] + numbers[quartinaryNumber] + equationSigns[fourthSign] + numbers[quinaryNumber] + equationSigns[fithSign] + numbers[senaryNumber];
																try {
																	Object value = engine.eval(equation);
																	if(value.equals(new Integer(resultInt))){
																		System.out.println(equation + " = " + Integer.toString(resultInt));
																		workingEquations.add(equation);
																	}else if(value.equals(new Double(resultDouble))){
																		System.out.println(equation + " = " + Integer.toString(resultInt));
																		workingEquations.add(equation);
																	}else if(value.equals(new Float(resultFloat))){
																		System.out.println(equation + " = " + Integer.toString(resultInt));
																		workingEquations.add(equation);
																	}else if(value.equals(new Long(resultLong))){
																		System.out.println(equation + " = " + Integer.toString(resultInt));
																		workingEquations.add(equation);
																	}
																} catch (ScriptException e) {
																	e.printStackTrace();
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
					}
				}
		}
	}
}
