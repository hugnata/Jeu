package es.esy.hugnata.jeu.animation;




import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import es.esy.hugnata.jeu.exceptions.SpriteSheetException;



public class SpriteSheet {
	/**L'image que l'on va utiliser comme spritesheet
	 * 
	 */
	protected BufferedImage img;
	/**La hauteur des sprite
	 * 
	 */
	private int height;
	/**La Largeur des sprite
	 * 
	 */
	private int width;
	
	/**Crée un nouveau spriteheet
	 * 
	 * @param img Le chemin vers l'image
	 * @param witdh La Largeur des sprite
	 * @param height La hauteur des sprite
	 * @throws SpriteSheetException Appelle une exception si la SpriteSheet est vide
	 * @throws IOException Si l'image n'existe pas
	 * @throws IndexOutOfBoundsException Si la dimension  des sprite est superieure à la dimension de l'image
	 */
	public SpriteSheet(File img, int width,int height) throws  IOException,IndexOutOfBoundsException
	{
		System.out.println("Création d'un nouveau SpriteSheet");
	
		this.img = ImageIO.read(img);
		if(!(this.img.getHeight()<height)&&!(this.img.getWidth()<width))
		{
				this.height = height ;
				this.width = width;
		}else
		{
		throw new IndexOutOfBoundsException();
		}
		
	}
	/**Permet de récuperer une animation
	 * 
	 * @param x La coordonée x du premier sprite à récupérer pour l'animation
	 * @param y La coordonée y du premier sprite à récupérer pour l'animation
	 * @param nb Le nombre de Sprite dont est composé l'animation
	 * @param delay Le delai entre chaque sprite
	 * @return une nouvelle animation
	 */
	public Animation getAnimation(String name,int x,int y,int nb,int delay)
	{
		Image[] sprite = new Image[nb];
		for(int i=0;i<nb;i++)
		{
			Image imgd = img.getSubimage(x+i*this.width, y, this.width, this.height);
			sprite[i] = imgd;
		}
		 return new Animation(name,sprite,delay);
	}
	

}
