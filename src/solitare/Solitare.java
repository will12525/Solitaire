package solitare;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Solitare extends Canvas{

	//card dimensions 73, 106
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private boolean running = false;
	private int fpsS=0;
	private JFrame frame;
	private BufferedImage cardBack=null;

	private List<Card> deck = new ArrayList<Card>();
	private List<Card> deckFlipped = new ArrayList<Card>();

	private List<Card> spades = new ArrayList<Card>();
	private List<Card> clubs = new ArrayList<Card>();
	private List<Card> hearts = new ArrayList<Card>();
	private List<Card> diamonds = new ArrayList<Card>();

	private List<Card> pile1 = new ArrayList<Card>();
	private List<Card> pile2 = new ArrayList<Card>();
	private List<Card> pile3 = new ArrayList<Card>();
	private List<Card> pile4 = new ArrayList<Card>();
	private List<Card> pile5 = new ArrayList<Card>();
	private List<Card> pile6 = new ArrayList<Card>();
	private List<Card> pile7 = new ArrayList<Card>();


	public Solitare()
	{
		frame = new JFrame("Solitare");
		frame.setSize(900,600);
		frame.setLayout(new BorderLayout());
		frame.add(this,BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(new Mouse());
		setIgnoreRepaint(true);

		frame.setVisible(true);

		running = true;
		runGame();
	}


	public void init()
	{
		loadCards();
		populatePlaces();
		System.out.println(deck.size());
	}
	public void tick()
	{

	}
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs==null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
	
		int xOffset = frame.getWidth()/10;
		int yOffset = frame.getHeight()/5;
		
		drawCards(pile1,g,xOffset*3,yOffset*1);
		drawCards(pile2,g,xOffset*4,yOffset*1);
		drawCards(pile3,g,xOffset*5,yOffset*1);
		drawCards(pile4,g,xOffset*6,yOffset*1);
		drawCards(pile5,g,xOffset*7,yOffset*1);
		drawCards(pile6,g,xOffset*8,yOffset*1);
		drawCards(pile7,g,xOffset*9,yOffset*1);
		
		if(deckFlipped.size()>0)
		{
			g.drawImage(deckFlipped.get(deckFlipped.size()-1).getCard(),xOffset*2, 20, null);
		}
		if(deck.size()>0)
		{
			g.drawImage(cardBack, xOffset, 20, null);
		}
		
		drawPiles(spades,g,xOffset*6,0);
		drawPiles(clubs,g,xOffset*7,0);
		drawPiles(hearts,g,xOffset*8,0);
		drawPiles(diamonds,g,xOffset*9,0);
		

		g.setColor(Color.WHITE);
		g.drawString(fpsS+"", 20, 20);
		
		g.dispose();
		bs.show();
	}
	public void drawPiles(List<Card> theCards, Graphics g,int xCord, int yCord)
	{
		
		for(int x=0;x<theCards.size();x++)
		{
			g.drawImage(theCards.get(x).getCard(), xCord, yCord, null);
		}
	}
	public void drawCards(List<Card> theCards,Graphics g, int xCord,int yCord)
	{
		int rePos=30;
		if(theCards.size()>10)
		{
			rePos=10;
		}
		for(int x=0;x<theCards.size();x++)
		{
			Card card = theCards.get(x);
			if(card.getFlipped())
			{
				g.drawImage(card.getCard(), xCord, yCord+(rePos*x), null);
			}
			else
			{
				g.drawImage(cardBack, xCord, yCord+(rePos*x), null);
			}
		}
	}

	public void runGame()
	{
		long lastLoopTime = System.nanoTime();
		double nanoTick=1000000000/60;

		int ticks=0;
		int frames = 0;

		long lastTimer=System.currentTimeMillis();
		double delta=0;

		init();

		while(running)
		{
			long now=System.nanoTime();
			delta+=(now-lastLoopTime)/nanoTick;
			lastLoopTime=now;
			boolean shouldRender=true;
			while(delta>=1)
			{
				ticks++;
				tick();
				delta-=1;
				shouldRender=true;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(shouldRender&&frames<60)
			{
				frames++;
				render();
			}
			if(System.currentTimeMillis()-lastTimer>=1000)
			{
				lastTimer+=1000;
				System.out.println("Ticks: "+ticks+", frames: "+frames);
				fpsS=frames;
				frames=0;
				ticks=0;
			}
		}
	}

	public void populatePlaces()
	{
		for(int x=0;x<1000;x++)
		{
			int spot1=(int) (Math.random()*52);
			int spot2=(int) (Math.random()*52);
			Card holder = deck.get(spot1);
			Card holder2 = deck.get(spot2);
			deck.set(spot1, holder2);
			deck.set(spot2, holder);
		}


		populate(1);
		populate(2);
		populate(3);
		populate(4);
		populate(5);
		populate(6);
		populate(7);

		pile1.get(0).flip();
		pile2.get(1).flip();
		pile3.get(2).flip();
		pile4.get(3).flip();
		pile5.get(4).flip();
		pile6.get(5).flip();
		pile7.get(6).flip();
				
	}
	public void populate(int amount)
	{
		for(int x=0;x<amount;x++)
		{
			int cardToMove = (int) (Math.random()*deck.size());
			switch(amount)
			{
			case 1:
				pile1.add(deck.get(cardToMove));
				break;
			case 2:
				pile2.add(deck.get(cardToMove));
				break;
			case 3:
				pile3.add(deck.get(cardToMove));
				break;
			case 4:
				pile4.add(deck.get(cardToMove));
				break;
			case 5:
				pile5.add(deck.get(cardToMove));
				break;
			case 6:
				pile6.add(deck.get(cardToMove));
				break;
			case 7:
				pile7.add(deck.get(cardToMove));
				break;
			}
			deck.remove(cardToMove);
		}

	}

	public void loadCards()
	{
		String mainPath = Solitare.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String path = mainPath +"cards/";
		File[] images = new File(path).listFiles();
		for(int x=0;x<images.length;x++)
		{
			String theCard = images[x].toString().substring(path.length()-1).toLowerCase();
			if(theCard.contains("spade"))
			{
				createCard("spade",images[x],theCard);
			}
			if(theCard.contains("club"))
			{
				createCard("club",images[x],theCard);
			}
			if(theCard.contains("heart"))
			{
				createCard("heart",images[x],theCard);
			}
			if(theCard.contains("diamond"))
			{
				createCard("diamond",images[x],theCard);
			}
		}
		try {
			cardBack = resize(ImageIO.read(new File(mainPath+"solitare/blankCard.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void createCard(String suite,File file,String theCard)
	{
		BufferedImage img=null;
		String type = theCard.substring(0, theCard.length()-(suite.length()+".jpg".length()));
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(img==null)
		{
			System.out.println("there was an error loading images, good bye");
			System.exit(0);
		}

		Card card = new Card(suite,type,resize(img));
		deck.add(card);
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

	public static void main(String[] args) {

		new Solitare();
	}

}
