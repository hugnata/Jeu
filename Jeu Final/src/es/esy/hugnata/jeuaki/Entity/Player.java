package es.esy.hugnata.jeuaki.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;




import es.esy.hugnata.jeuaki.Constant;
import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.animation.Animation;
import es.esy.hugnata.jeuaki.animation.Animator;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.listeners.AnimationListener;
import es.esy.hugnata.jeuaki.Entity.Projectile;

public class Player extends Monstre implements AnimationListener{
	public int chunkx = Constant.DEPARTX.valeur ;
	public int chunky = Constant.DEPARTX.valeur;
		
public int direction;
		
	/**Le constructeur du player
	 * 
	 * @param animator
	 * @param height
	 * @param width
	 * 
	 * @see Entity#Entity(Animator, int, int)
	 */
	public Player(File spritesheet,int animheight,int animwidth,int height,int width) throws SpriteSheetException
	{
		super("joueur",spritesheet,animwidth,animheight, height,width,200,2);
		
	}
	/***Le constructeur du player à partir des valeurs par defaut
	 * 
	 * @throws SpriteSheetException 
	 */
		@SuppressWarnings("serial")
		public Player()  throws SpriteSheetException 
		{
			
			super("joueur",new File("ressources/Spritesheets/PlayerSpritesheet.png"),32,50,100,64,200,2);
			
			animator.addAnim("bas",0, 0, 4, 20);
			animator.addAnim("haut",0, 50, 4, 20);
			animator.addAnim("droite",0, 100, 8, 10);
			animator.addAnim("gauche",0, 150, 8, 10);
			animator.addAnim("tir",0, 2, 4, 10);
		
				this.height = 100;
				this.width = 64;
				this.coordx = Constant.WIDTH.valeur/2;
				this.coordy = Constant.HEIGHT.valeur/2 ;
						}

		public void DrawEntity(Graphics g,int x,int y)
		{
			g.setColor(Color.black);
			g.drawRect(320, 280, 100, 20);
			g.setColor(Color.red);
			g.fillRect(319, 281, (int) Math.floor((100*health)/healthdepart),18);
			g.drawImage(animator.play(), 320, 320,width,height, null);
			//TODO REMOVE HITBOX
			g.setColor(Color.RED);
			g.drawRect(getCoordx()+x, getCoordy()+y, width, height);
			//TODO REMOVE COORDS
			g.setColor(Color.GREEN);
			g.drawString(Integer.toString(getCoordx()), getCoordx()+x+4, getCoordy()+y+2);
			g.drawString(Integer.toString(getCoordy()), getCoordx()+x+35, getCoordy()+y+2);
		}
		public void Tirer()
		{
			try {
				Projectile pro = new Projectile(this,new File("ressources/divers/Balle.png"),16,16,64,64,1,200,20,this.direction);
				JeuAki.map.addProjectile(pro,chunkx,chunky);
			} catch (SpriteSheetException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void OnFinAnimation(Animation anim) {
			this.getAnimator().changerAnim("defaut");
			if(anim.getName()=="tir")
			{
				Tirer();
			}
			
		}
		public int getDirection() {
			return direction;
		}
		public void setDirection(int direction) {
			this.direction = direction;
		}
	
		
}
