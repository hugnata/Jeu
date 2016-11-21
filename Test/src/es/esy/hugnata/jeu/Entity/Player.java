package es.esy.hugnata.jeuaki.Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.animation.Animator;
import es.esy.hugnata.jeuaki.animation.SpriteSheet;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;

public class Player extends Entity {
		
public int chunkx;
public int chunky;
		
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
		super("joueur",spritesheet,animwidth,animheight, height,width);
		
	}
	/***Le constructeur du player à partir des valeurs par defaut
	 * 
	 * @throws SpriteSheetException 
	 */
		@SuppressWarnings("serial")
		public Player() throws SpriteSheetException 
		{
			
			super("joueur",new File("ressources/test/Voitures5.png"),24,32,500,500);
			animator.addAnim("feu",0, 0, 3, 25);
			animator.changerAnim("feu");
				this.height = 150;
				this.width = 150;
				this.chunkx = JeuAki.TUILEDEBUTX;
				this.chunky = JeuAki.TUILEDEBUTY;
				this.coordx = 320;
				this.coordy = 320 ;
						}

		public void DrawEntity(Graphics g,int x,int y)
		{
			g.drawImage(animator.play(), 320, 320,width,height, null);
		
		}
		
		
}
