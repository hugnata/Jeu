package es.esy.hugnata.jeu.Entity;

import java.awt.Graphics;

import es.esy.hugnata.jeu.Jeu;

public class TextEntity extends Entity {
	private String message;
	private int taille;
	private int delay;
	
	public TextEntity(String message,int x,int y,int taille,int delay){
		super();
		this.coordx = x;
		this.coordy = y;
		this.width =1;
		this.height = 1;
		this.message = message;
		this.taille = taille;
		this.delay = delay;
	}
	public String getMessage() {
		return message;
	}
	public int getTaille() {
		return taille;
	}
	public void DrawEntity(Graphics g,int x,int y)
	{
		if(delay>1)
		{
				g.drawString(message, coordx, coordy);
				delay-- ;
		}else
		{
			Jeu.RemoveText(this);
		}
	
	}
}
