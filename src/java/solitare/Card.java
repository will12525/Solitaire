package solitare;

import java.awt.image.BufferedImage;

public class Card {
	private String suite="";
	private int value = 0;
	BufferedImage card =null;
	private boolean flipped=false;
	private int x,y;

	public Card(String suite, int value, BufferedImage card)
	{
		this.suite=suite;
		this.value=value;
		this.card=card;
	}
	public String getSuite() {
		return suite;
	}
	public int getValue() {
		return value;
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
	public void flipBack()
	{
		flipped=false;
	}
	public int getColor()
	{
		if(suite.equals("heart")||suite.equals("diamond"))
		{
			return 1;
		}
		return 0;
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