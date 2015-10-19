package solitare;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class CardSplitter{
	//179 by 260
	//2323 by 1043
	public CardSplitter(){
		File f = new File("C:/Users/William/Desktop/myCards.jpg");
		FileInputStream fs=null;
		try {
			fs = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage cards=null;
		try {
			cards = ImageIO.read(fs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int chunks =4 *13;

		int chunkHeight = cards.getHeight()/4;
		int chunkWidth = cards.getWidth()/13;

		int count = 0;

		BufferedImage cardImages[] = new BufferedImage[chunks];

		for(int x =0; x<4;x++)
		{
			for(int y =0;y<13;y++)
			{

				cardImages[count] = new BufferedImage(chunkWidth,chunkHeight,cards.getType());

				Graphics2D g =cardImages[count++].createGraphics();
				g.drawImage(cards, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);

				g.dispose();
				System.out.println("X: "+x +" Y: "+y);

			}
		}
		System.out.println("split done");
		int k=0;
		for(int i = 0; i<cardImages.length;i++)
		{
			k++;
			
			String nameId="";
			String numId="";
			if(i<13)
			{
				nameId="Spade";
			}
			if(i>12&&i<26)
			{
				nameId="Club";
			}
			if(i>25&&i<39)
			{
				nameId="Heart";	
			}
			if(i>38)
			{
				nameId="Diamond";
			}

			if(i==0||i==13||i==26||i==39)
			{
				numId="Ace";
			}
			if(i==10||i==23||i==36||i==49)
			{
				numId="Jack";
			}
			if(i==11||i==24||i==37||i==50)
			{
				numId="Queen";
			}
			if(i==12||i==25||i==38||i==52)
			{
				numId="King";
			}

			try {
				ImageIO.write(cardImages[i],"jpg",new File("C:/Users/William/Desktop/miniPics/"+(k)+numId+nameId+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("mini images created");




	}

}
