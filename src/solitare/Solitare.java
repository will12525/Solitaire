package solitare;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
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
	private static boolean clicked = false;
	private boolean cardInHand = false;
	private int fpsS=0;
	private JFrame frame;
	private BufferedImage cardBack=null;

	private List<Card> pileInHand = new ArrayList<Card>();

	private List<Card> deck = new ArrayList<Card>();
	private List<Card> deckFlipped = new ArrayList<Card>();

	private List<List<Card>> finalSuite = new ArrayList<List<Card>>();

	private List<Card> spades = new ArrayList<Card>();
	private List<Card> clubs = new ArrayList<Card>();
	private List<Card> hearts = new ArrayList<Card>();
	private List<Card> diamonds = new ArrayList<Card>();

	private List<List<Card>> piles = new ArrayList<List<Card>>();

	private List<Card> pile1 = new ArrayList<Card>();
	private List<Card> pile2 = new ArrayList<Card>();
	private List<Card> pile3 = new ArrayList<Card>();
	private List<Card> pile4 = new ArrayList<Card>();
	private List<Card> pile5 = new ArrayList<Card>();
	private List<Card> pile6 = new ArrayList<Card>();
	private List<Card> pile7 = new ArrayList<Card>();

	private int lastPile=0;
	private int toPile = 0;
	private int selectedRow = 0;
	private int selectedColumn = 0;

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
		int mouseX=0;
		int mouseY=0;
		toPile=0;
		Card placeOn = null;
		if(clicked)
		{
			if(pileInHand.size()>0)
			{
				cardInHand=true;
			}
			else
			{
				cardInHand=false;
			}
			mouseX = (int) (Mouse.getX()-frame.getLocation().getX());
			mouseY = (int) (Mouse.getY()-frame.getLocation().getY());


			int xOffset = frame.getWidth()/10;
			int yOffset = frame.getHeight()/5;

			//place cards
			if(cardInHand)
			{
				if(mouseY>=yOffset)
				{
					for(int x=0;x<piles.size();x++)
					{
						if(mouseX>xOffset*(x+3)&&mouseX<xOffset*(x+4))
						{
							List<Card> tempPile = piles.get(x);
							if(tempPile.size()>0)
							{
								placeOn = tempPile.get(tempPile.size()-1);
								toPile = x;
							}
							else{
								placeOn = null;
								toPile=x;
							}
						}
					}
					if(placeCard(pileInHand.get(0),placeOn))
					{
						//places card on the desired pile
						for(int k=0;k<pileInHand.size();k++)
						{
							piles.get(toPile).add(pileInHand.get(k));
						}
						pileInHand.clear();
						//piles.get(toPile).add(inHand);
						if(lastPile!=7)
						{
							if(piles.get(lastPile).size()>0)
							{
								piles.get(lastPile).get(piles.get(lastPile).size()-1).flip();
							}
						}

					}
					else if(lastPile==7)
					{
						//the flipped deck
						deckFlipped.add(pileInHand.get(0));
						pileInHand.clear();
					}
					else
					{
						for(int k=0;k<pileInHand.size();k++)
						{
							piles.get(lastPile).add(pileInHand.get(k));
						}
						pileInHand.clear();
					}
				}
			}
			//pickup cards
			if(!cardInHand&&pileInHand.size()<=0)
			{
				//the piles
				if(mouseY>=yOffset)
				{
					for(int x=0;x<piles.size();x++)
					{
						if(mouseX>xOffset*(x+3)&&mouseX<xOffset*(x+4))
						{
							List<Card> tempPile = piles.get(x);
							if(tempPile.size()>0)
							{
								for(int k=0;k<tempPile.size();k++)
								{
									if(mouseY>tempPile.get(k).getY())
									{
										if(k==tempPile.size()-1)
										{
											pileInHand.add(tempPile.get(k));
											piles.get(x).remove(k);
											lastPile=x;
										}
										else if(mouseY<tempPile.get(k+1).getY())
										{
											System.out.println("hi");
											for(int j=k;j<tempPile.size();j++)
											{
												if(tempPile.get(j).getFlipped())
												{
													pileInHand.add(tempPile.get(j));
												}
												lastPile=x;
											}
											piles.get(x).removeAll(pileInHand);
										}
									}
								}
							}
						}
					}
				}
				//the deck
				if(mouseY<yOffset&&mouseX<xOffset*2&&mouseX>xOffset)
				{
					if(deck.size()>0)
					{
						deckFlipped.add(deck.get(deck.size()-1));
						deckFlipped.get(deckFlipped.size()-1).flip();
						deck.remove(deck.size()-1);
					}
					else if(deckFlipped.size()>0)
					{
						for(int x=deckFlipped.size()-1;x>=0;x--)
						{
							Card temp = deckFlipped.get(x);
							temp.flip();
							deck.add(temp);
						}
						deckFlipped.clear();
					}
				}
				//the flipped deck
				if(mouseY<yOffset&&mouseX<xOffset*3&&mouseX>xOffset*2)
				{
					if(deckFlipped.size()>0)
					{
						pileInHand.add(deckFlipped.get(deckFlipped.size()-1));
						deckFlipped.remove(pileInHand.get(0));
						lastPile=7;
					}
				}

			}

		}

		clicked =false;
	}
	public void render()
	{
		Point p = MouseInfo.getPointerInfo().getLocation();

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

		for(int x=0;x<piles.size();x++)
		{
			int rePos = drawPiles(piles.get(x),g,xOffset*(x+3),yOffset);
			{
				for(int y=0;y<piles.get(x).size();y++)
				{
					piles.get(x).get(y).setY(yOffset+(y*rePos));
				}
			}
		}

		if(deckFlipped.size()>0)
		{
			g.drawImage(deckFlipped.get(deckFlipped.size()-1).getCard(),xOffset*2, 0, null);
		}
		if(deck.size()>0)
		{
			g.drawImage(cardBack, xOffset, 0, null);
		}

		for(int x=0;x<finalSuite.size();x++)
		{
			drawFinal(finalSuite.get(x),g,xOffset*(x+6),0);
		}

		if(pileInHand.size()>0)
		{
			for(int x=0;x<pileInHand.size();x++)
			{
				g.drawImage(pileInHand.get(x).getCard(),(int)p.getX(), (int)p.getY()+(x*20), null);
			}
		}

		g.setColor(Color.WHITE);
		g.drawString(fpsS+"", 20, 20);

		g.dispose();
		bs.show();
	}
	public void drawFinal(List<Card> theCards, Graphics g,int xCord, int yCord)
	{
		//g.drawImage(theCards.get(theCards.size()-1).getCard(), xCord,yCord,null);
		for(int x=0;x<theCards.size();x++)
		{
			g.drawImage(theCards.get(x).getCard(), xCord, yCord, null);
		}
	}
	public int drawPiles(List<Card> theCards,Graphics g, int xCord,int yCord)
	{
		int rePos=30;
		if(theCards.size()>12)
		{
			rePos=20;
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
		return rePos;
	}

	public boolean placeCard(Card cardToPlace, Card placeOn)
	{
		if(placeOn==null)
		{
			if(cardToPlace.getNum()==13)
			{
				return true;
			}
			return false;
		}
		if(((cardToPlace.getNum()+1)==placeOn.getNum())&&(cardToPlace.getColor()!=placeOn.getColor()))
		{
			return true;	
		}

		return false;
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
			if(shouldRender)
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
		piles.add(pile1);
		piles.add(pile2);
		piles.add(pile3);
		piles.add(pile4);
		piles.add(pile5);
		piles.add(pile6);
		piles.add(pile7);

		for(int x=0;x<piles.size();x++)
		{
			populate(piles.get(x),x+1);
			piles.get(x).get(piles.get(x).size()-1).flip();;
		}

		finalSuite.add(spades);
		finalSuite.add(clubs);
		finalSuite.add(hearts);
		finalSuite.add(diamonds);
	}
	public void populate(List<Card> aPile,int amount)
	{
		for(int x=0;x<amount;x++)
		{
			int cardToMove = (int) (Math.random()*deck.size());
			aPile.add(deck.get(cardToMove));
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
			cardBack = resize(ImageIO.read(new File(mainPath+"cardHolders/blankCard.png")));
			spades.add(new Card("spade","start",resize(ImageIO.read(new File(mainPath+"cardHolders/Spade.jpg")))));
			clubs.add(new Card("club","start",resize(ImageIO.read(new File(mainPath+"cardHolders/Club.jpg")))));
			hearts.add(new Card("heart","start",resize(ImageIO.read(new File(mainPath+"cardHolders/Heart.jpg")))));
			diamonds.add(new Card("diamond","start",resize(ImageIO.read(new File(mainPath+"cardHolders/Diamond.jpg")))));
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
	public static void setClicked()
	{
		clicked =true;
	}

	public static void main(String[] args) {

		new Solitare();
	}

}
