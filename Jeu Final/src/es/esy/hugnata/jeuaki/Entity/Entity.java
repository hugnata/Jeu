package es.esy.hugnata.jeuaki.Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import es.esy.hugnata.jeuaki.animation.Animator;
import es.esy.hugnata.jeuaki.animation.SpriteSheet;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.listeners.EntityColisionListener;
	/**Classe représentant une entitée
	 * 
	 * @author Hugnata
	 *
	 */

public class Entity {
	/**Le nom de l'entitée
	 * 
	 */
	protected String name;
	/**La coordonnée x de l'entitée, à l'interieur de sa tuile
	 * 
	 */
	protected  int coordx =320;
	/**La coordonnée y de l'entitée, à l'interieur de sa tuile
	 * 
	 */
	protected  int coordy =320;
	/**La hauteur de l'entitée
	 * 
	 */
	protected  int height = 0 ;
	/**La Largeur de l'entitée
	 * 
	 */
	protected  int width = 0;

	/**L'animator de l'entitée 
	 * 
	 */
	protected Animator animator;
	
	/**Crée l'entitée à partir d'une image, de la hauteur et de la largeur
	 * 
	 * Une entitée est un "objet" qui va être placé sur une tuile.
	 * 
	 * @see MovingEntity
	 * @see Player
	 * @see es.esy.hugnata.jeuaki.assets.objet.Interagible
	 * 
	 * @param height La Largeur de l'entitée
	 * @param width La hauteur de l'entitée
	 * @param animator Le fichier contenant le spritesheet de l'entitée
	 * @param animheight La hauteur des animations
	 * @throws SpriteSheetException 
	 * @parma animwidth La largeur des animations
	 * 
	 */
	public Entity(String nom,File spritesheet,int animwidth,int animheight,int height,int width) throws SpriteSheetException
	{
		
		this.name = nom;
		this.height = height;
		this.width = width;
		
			
				try {
					this.animator = new Animator(new SpriteSheet(spritesheet,animwidth,animheight));
				} catch (IOException e) {
					throw new SpriteSheetException(this,"Le spritesheet n'a pas pu être chargé");
		}
				catch(IndexOutOfBoundsException e1)
				{
					throw new SpriteSheetException(this,"la dimension  des sprite est superieure à la dimension de l'image !");
					
				}
			
		
		
		
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
		g.drawImage(animator.play(), getCoordx()+x, getCoordy()+y,width,height, null);
	
	}
	/**Renvoie la coordonée y de l'entitée
	 * 
	 * @return coordy
	 */
	public int getCoordy() {
		return coordy;
	}
	/**Met à jour la coordonée y de l'entitée
	 * 
	 * @param coordy
	 */
	public void setCoordy(int coordy) {
		this.coordy = coordy;
	}
	/**Renvoie la coordonée x de l'entitée
	 * 
	 * @return coordx
	 */
	public int getCoordx() {
		return coordx;
	}
	/**Met à jour la coordonée x de l'entitée
	 * 
	 * @param coordx
	 */
	public void setCoordx(int coordx) {
		this.coordx = coordx;
	}
	/**Renvoie le nom de l'entitée
	 * 
	 * @return  le nom de l'entitée
	 */
	public String getName() {
		return this.name;
	}
	/**Renvoie l'animator de l'entitée
	 * 
	 * @return Animator
	 */
	public Animator getAnimator()
	{
		return this.animator;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void OnCollision(Entity entity2) {
		
		
	}
	
}
