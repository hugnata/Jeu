package es.esy.hugnata.jeu.Entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import es.esy.hugnata.jeu.Jeu;
import es.esy.hugnata.jeu.Entity.Entity;
import es.esy.hugnata.jeu.Entity.Monstre;
import es.esy.hugnata.jeu.Entity.Player;
import es.esy.hugnata.jeu.animation.Animation;
import es.esy.hugnata.jeu.animation.Animator;
import es.esy.hugnata.jeu.animation.SpriteSheet;
import es.esy.hugnata.jeu.exceptions.SpriteSheetException;
import es.esy.hugnata.jeu.utils.KeyInput;

public class Projectile extends Entity {
	protected int degat;
	protected int direction;
	private boolean touche = false;
	protected boolean gentil;
	
	public Projectile(Entity lanceur, File spritesheet, int animheight, int animwidth, int height, int width,int nbanim,int delay,int degat,int direction) throws SpriteSheetException {
		super(lanceur.getName()+"_Projectile", spritesheet, animheight, animwidth, height, width);
	try{
		Player p =(Player)lanceur;
		gentil=true;
	}catch(ClassCastException e)
	{
		gentil = false;
	}
		this.animator.addAnim("Tir", 0, 0, nbanim, delay);
		this.degat = degat;
		this.direction = direction;
		this.coordx = lanceur.coordx;
		this.coordy = lanceur.coordy;
	}
	/**Dessine l'entitée sur la tuile
	 * 
	 * Methode appelée lors du rendu de la tuile, permet de dessiner l'entitée sur la map, en fonction des coordonnées {@link #coordx x} et  {@link #coordy y}de l'entitée.
	 * Dessine l'image donnée par l'{@link #animator Animator} de l'entitée
	 * 
	 * 
	 * @param g Graphics
	 */
	public void DrawEntity(Graphics g,int x,int y)
	{
	
		switch(direction)
		{
		case 1:
			coordy--;
			break;
		case 2:
			coordy++;
			break;
		case 3:
			coordx--;
			break;
		case 4:
			coordx++;
			break;
		}
		g.drawImage(animator.play(), getCoordx()+x, getCoordy()+y,width,height, null);
	
	}	
	public void OnCollision(Entity entity2) {
		
			if(touche ==false)
			{
				if(gentil)
				{
					try{
					Monstre m = (Monstre)entity2;
					if(!m.getName().equalsIgnoreCase("joueur"))
					{
						m.BaisserVie(20);
						JouerExplosion();
					}
					
					}catch(ClassCastException e)
					{
					}
				}else
				{
					try{
						
						Player m = (Player)entity2;
						m.BaisserVie(20);
						JouerExplosion();
						}catch(ClassCastException e)
						{
						}
				}
			}
		
			
		
	}
	public void JouerExplosion()
	{
			try {
				animator.ChangeSpriteSheet(new SpriteSheet(new File("ressources/effets/explosion.png"),128,128));
			} catch (IndexOutOfBoundsException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			animator.addAnim("dest", 0,0, 10,4);
			animator.changerAnim("dest");
			touche = true;
			
			
	}
	@Override
	public void OnFinAnimation(Animation anim) {
		if(anim.getName().equalsIgnoreCase("dest"))
		{
			System.out.println("Adieu !");
			this.Visible = false;
		}
		
	}
	
}
