package solitare;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {

	private static int mouseX=0,mouseY=0;
	
	public void mouseClicked(MouseEvent e) {
		//System.out.println(e.getButton());
		mouseX=e.getX();
		mouseY=e.getY();
		Solitare.setClicked();
		//System.out.println(mouseX+", "+mouseY);
		
	}

	public void mouseEntered(MouseEvent e) {


	}

	public void mouseExited(MouseEvent e) {


	}

	public void mousePressed(MouseEvent e) 
	{
		
		

	}

	public void mouseReleased(MouseEvent e) {

	}
	public static int getX()
	{
		return mouseX;
	}
	public static int getY()
	{
		return mouseY;
	}
}