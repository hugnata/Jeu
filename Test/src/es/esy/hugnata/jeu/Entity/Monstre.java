package es.esy.hugnata.jeu.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import javax.swing.event.EventListenerList;

import es.esy.hugnata.jeu.Jeu;
import es.esy.hugnata.jeu.animation.Animation;
import es.esy.hugnata.jeu.exceptions.SpriteSheetException;
import es.esy.hugnata.jeu.listeners.MonstreEventListener;
import es.esy.hugnata.jeu.Entity.Projectile;

public class Monstre extends Entity{
	/**
	 * La vie du monstre
	 */
	int health;
	/**
	 * Sa vie de départ, qui ne peut pas être modifiée
	 */
	final int healthdepart;

	
	EventListenerList listeners = new EventListenerList();
	/**Le constructeur du monstre , identique à celui de l'{@link Entity} + la santée du monstre
	 * 
	 * @param nom
	 * @param spritesheet
	 * @param animwidth
	 * @param animheight
	 * @param height
	 * @param width
	 * @param health
	 * @throws SpriteSheetException
	 */
	public Monstre(String nom, File spritesheet, int animwidth, int animheight,
			int width, int height, int health) throws SpriteSheetException {
		super(nom, spritesheet, animwidth, animheight, width, height);
		this.health = health;
		this.healthdepart = health;
	}
	public void BaisserVie(int vie)
	{
		health -=vie;
		if(health<=0)
		{ 
			this.Visible = false;
			
			for(MonstreEventListener listener: listeners.getListeners(MonstreEventListener.class))
			{
				listener.OnMort(this);
			}
		}
	}
	public void DrawEntity(Graphics g,int x,int y)
	{
		
		g.setColor(Color.black);
		g.drawRect(getCoordx()+x, getCoordy()+y-30, 100, 20);
		g.setColor(Color.red);
		g.fillRect(getCoordx()+x-1, getCoordy()-29+y, (int) Math.floor((100*health)/healthdepart),18);
	g.drawImage(animator.play(), getCoordx()+x, getCoordy()+y,width,height, null);
		
	
		
	}
	
	public void AddEventListener(MonstreEventListener l)
	{
		listeners.add(MonstreEventListener.class, l);
	}
	public void RemoveEventListener(MonstreEventListener l)
	{
		listeners.remove(MonstreEventListener.class, l);
	}
	@Override
	public void OnFinAnimation(Animation anim) {
		if(anim.getName().equalsIgnoreCase("tir"))
		{
			Tirer();
			this.getAnimator().changerAnim("tir");
		}
		
		
		
	}
	private void Tirer() {
		try {
			Jeu.map.addProjectile(new Projectile(this,new File("ressources/divers/balleE.png"),32,32,64,64,1,100,10,1));
			Jeu.map.addProjectile(new Projectile(this,new File("ressources/divers/balleE.png"),32,32,64,64,1,100,10,2));
			Jeu.map.addProjectile(new Projectile(this,new File("ressources/divers/balleE.png"),32,32,64,64,1,100,10,3));
			Jeu.map.addProjectile(new Projectile(this,new File("ressources/divers/balleE.png"),32,32,64,64,1,100,10,4));
		} catch (SpriteSheetException e) {
			e.printStackTrace();
		}
		
	}
}
