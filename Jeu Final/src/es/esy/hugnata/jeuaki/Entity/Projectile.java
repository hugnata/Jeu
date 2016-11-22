package es.esy.hugnata.jeuaki.Entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;

import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.assets.Carte;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.utils.KeyInput;

public class Projectile extends Entity {
	protected int degat;
	protected int direction;

	public Projectile(String nom, File spritesheet, int animheight, int animwidth, int height, int width,int nbanim,int delay,int degat,int direction) throws SpriteSheetException {
		super(nom, spritesheet, animheight, animwidth, height, width);
		this.animator.addAnim("Tir", 0, 0, nbanim, delay);
		this.degat = degat;
		this.direction = direction;
	}
	public void DrawEntity(Graphics g,int x,int y)
	{
		if(direction==1)
		{ 
			if(!JeuAki.map.)this.setCoordy(perso.getCoordy() - 1);
				;
			
		}
		if(KeyInput.isDown(KeyEvent.VK_S))
		{ 
			if(!map.Collision(2,perso))perso.setCoordy(perso.getCoordy() + 1);
				;
		}
		if(KeyInput.isDown(KeyEvent.VK_Q))
		{ 
			if(!map.Collision(3,perso))perso.setCoordx(perso.getCoordx() - 1);
				
		}
		if(KeyInput.isDown(KeyEvent.VK_D))
		{ 
			if(!map.Collision(4,perso))perso.setCoordx(perso.getCoordx() + 1);
		}
		g.drawImage(animator.play(), getCoordx()+x, getCoordy()+y,width,height, null);
	
	}
}
