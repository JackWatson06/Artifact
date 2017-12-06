package com.murdermaninc.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ColorTester extends Canvas implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int width = 20;
	private final int height = 20;
	private final int scale = 4;
	
	private boolean running = false;
	
	private BufferedImage imageColorSelector = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
	private int[] pixelsColor = ((DataBufferInt) imageColorSelector.getRaster().getDataBuffer()).getData();
	
	
	private static BufferedImage image;
	public static int pixels[];
	
	private long lastTime;
	//private int add = 0;
	private boolean shift = false;
	
	public void start(){
		running = true;
		new Thread(this).start();
	}
	
	public void run() {
		try{
			image = ImageIO.read(ColorTester.class.getResourceAsStream("/colorTester.png"));
		}catch(IOException e){
			
		}
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		lastTime = System.currentTimeMillis();
		
		while(running){
			render();
		}	
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		
		if(System.currentTimeMillis() - lastTime >= 5000){
			lastTime = System.currentTimeMillis();
			//add -= 3939075;
			shift = true;
		}
		
		//System.out.println("Orange - Light: " + Integer.parseUnsignedInt("11111111111100000110110000001100", 2));
		//System.out.println("Orange - Dark: " + Integer.parseUnsignedInt("11111111101101000101000100001001", 2));
		///System.out.println("Green - Light: " + Integer.parseUnsignedInt("11111111011011001111000000001100", 2));
		//System.out.println("Green - Dark: " + Integer.parseUnsignedInt("11111111010100010111100000001001", 2));
		//System.out.println("Blue - Light: " + Integer.parseUnsignedInt("11111111000011000110110011110000", 2));
		//System.out.println("Blue - Dark: " + Integer.parseUnsignedInt("11111111000010010101000110110100", 2));
		
		
		//System.out.println("Color in binary: " + Integer.toBinaryString(pixels[1]));
		//System.out.println((pixels[1] & 0x00FEFEFF));
		//System.out.println("1 to binary: " + Integer.toBinaryString(0 & 0x00FFFFFF));
		//System.out.println("Shift and combinatrion: " + Integer.toBinaryString(((pixels[1] & 0xFFFFFF) + (0 & 0x00FFFFFF)) >> 1));
		
		for(int dy = 0; dy < width; dy++){
			for(int dx = 0; dx < height; dx++){
				int currentColor = pixels[dx + (dy * width)];
				System.out.println(currentColor);
				for(int y = 0; y < scale; y++){
					for(int x = 0; x < scale; x++){
						if(!shift){
							pixelsColor[(x + (dx * scale)) + ((y + (dy * scale)) * (width * scale))] = currentColor;
						}else{
							pixelsColor[(x + (dx * scale)) + ((y + (dy * scale)) * (width * scale))] = (currentColor & 0xfefeff) + (0x5f5f5f & 0xfefeff) >> 1;
						}
					}
				}
			}
			
		}
		
		System.out.println("Done");
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(imageColorSelector, 0, 0, width * scale, height * scale, null);
		g.dispose();
		bs.show();
		
	}
	
	public static void main(String[] args){
		
		ColorTester colorTester = new ColorTester();
		
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(20 * 4, 20 * 4);
		frame.setUndecorated(true);
		frame.setLayout(new BorderLayout());
		frame.add(colorTester, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		colorTester.start();
	}


	
}

//ORANGE COLOR

//Color - Lighter: 11111111, 11110000, 01101100, 00001100     Number - -1020916
/*
			Shifts: & 0xAAAAAA
  
  
  
 */				 //11111111, 10110000, 01000000, 00001000
//Color - Darker:  11111111, 10110100, 01010001, 00001001     Number - -4959991













//GREEN COLOR

//Color - Lighter: 11111111, 01101100, 11110000, 00001100     Number - -9637876
/*
		   Shifts: 




*/
//Color - Darker:  11111111, 01010001, 01111000, 00001001     Number - -11438071














//BLUE COLOR

//Color - Lighter: 11111111, 00001100, 01101100, 11110000     Number - -15962896
/*
		   Shifts: 




*/
//Color - Darker:  11111111, 00001001, 01010001, 10110100     Number - -16166476

//-1086453
//-5025528
