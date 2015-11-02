package solitare;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Deck {
	private List<Card> deck = new ArrayList<Card>();
	private int amount=0;
	private List<Card> holders = new ArrayList<Card>();

	public Deck(int amount)
	{
		
		this.amount=amount;
		
			loadCards();
		
		
			shuffle();
	}

	public int getSize()
	{
		return deck.size();
	}

	public Card removeCard(int position)
	{
		Card toReturn = deck.get(position);
		deck.remove(position);
		return toReturn;
	}
	public Card getCard(int position)
	{
		return deck.get(position);
	}
	public void add(Card card)
	{
		deck.add(card);
	}
	
	public Card getHolder(String type)
	{
		if(type.toLowerCase().equals("back"))
		{
			return holders.get(0);
		}
		if(type.toLowerCase().equals("spade"))
		{
			return holders.get(1);
		}
		else if(type.toLowerCase().equals("club"))
		{
			return holders.get(2);
		}
		else if(type.toLowerCase().equals("heart"))
		{
			return holders.get(3);
		}
		else if(type.toLowerCase().equals("diamond"))
		{
			return holders.get(4);
		}
		else
		{
			System.out.println("query didnt match available suites");
			return holders.get(0);
		}
	}
	public void shuffle()
	{
		for(int x=0;x<deck.size()*5;x++)
		{
			int spot1=(int) (Math.random()*deck.size());
			int spot2=(int) (Math.random()*deck.size());
			Card holder = deck.get(spot1);
			Card holder2 = deck.get(spot2);
			deck.set(spot1, holder2);
			deck.set(spot2, holder);
		}
	}
	public void loadCards()
	{
		//URL url = this.getClass().getResource("/resources");

		//String mainPath =url.getPath();
		String path = "cards/";
		for(int j=0;j<amount;j++)
		{
			for(int k=1;k<14;k++)
			{
				String cardPath = path+k+"Spade.jpg";

				deck.add(createCard("spade",k,cardPath));
				cardPath = path+k+"Club.jpg";
				deck.add(createCard("club",k,cardPath));
				cardPath = path+k+"Heart.jpg";
				deck.add(createCard("heart",k,cardPath));
				cardPath = path+k+"diamond.jpg";
				deck.add(createCard("diamond",k,cardPath));
			}
		}
		path = "cardHolders/";
		
		holders.add(createCard("",0,path+"blankCard.png"));
		holders.add(createCard("spade",0,path+"Spade.jpg"));
		holders.add(createCard("club",0,path+"Club.jpg"));
		holders.add(createCard("heart",0,path+"Heart.jpg"));
		holders.add(createCard("diamond",0,path+"Diamond.jpg"));
	}
	public Card createCard(String suite,int value,String cardPath)
	{
		BufferedImage img=null;
		try {
			URL file = this.getClass().getResource("/" + cardPath);
			img = ImageIO.read(file);
		} catch (Exception e) {
			Solitaire.toClip(e.getMessage());
		}
		if(img==null)
		{
			//input == null!
			System.out.println("there was an error loading images, good bye");
			Solitaire.toClip("image == null");
			System.exit(0);
		}
		//Solitaire.toClip(this.getClass().getResourceAsStream("/resources"+cardPath).toString());
		return new Card(suite,value,resize(img));
	}
	public BufferedImage resize(BufferedImage img)
	{
		int scale = 3;

		Image tmp = img.getScaledInstance(img.getWidth()/scale, img.getHeight()/scale, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(img.getWidth()/scale, img.getHeight()/scale, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
}