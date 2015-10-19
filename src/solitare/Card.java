package solitare;

import java.awt.image.BufferedImage;

public class Card {
	private String suite="";
	private String type="";
	BufferedImage card =null;
	private boolean flipped=false;
	int x,y;

	public Card(String suite, String type, BufferedImage card)
	{
		this.suite=suite;
		this.type=type;
		this.card=card;
	}
	public String getSuite() {
		return suite;
	}
	public String getType() {
		return type;
	}
	public BufferedImage getCard() {
		return card;
	}
	
	public boolean getFlipped() {
		return flipped;
	}
	public void flip() {
		flipped=true;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	

}
