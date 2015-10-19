package solitare;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		//System.out.println(e.getButton());
		int mouseX=e.getX();
		int mouseY=e.getY();
		System.out.println(mouseX+", "+mouseY);
		
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
}