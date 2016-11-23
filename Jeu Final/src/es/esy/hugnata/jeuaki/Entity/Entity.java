package es.esy.hugnata.jeuaki.Entity;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import es.esy.hugnata.jeuaki.animation.Animation;
import es.esy.hugnata.jeuaki.animation.Animator;
import es.esy.hugnata.jeuaki.animation.SpriteSheet;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.listeners.AnimationListener;
	/**Classe représentant une entitée
	 * 
	 * @author Hugnata
	 *
	 */

public class Entity implements AnimationListener{
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
	/**
	 * Boolean qui dit si oui ou non l'entitée est chargé par la map
	 */
	protected boolean Visible = true ;
	public boolean isVisible() {
		return Visible;
	}
	public void setVisible(boolean visible) {
		Visible = visible;
	}
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
	public Entity(String nom,File spritesheet,int animwidth,int animheight,int width,int height) throws SpriteSheetException
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
			
				animator.SetAnimationListener(this);
		
		
	}
	/**Constructeur vide permet seulement de récuperer la methode {@link #DrawEntity} et la position du joueur*/
	public Entity() {
		// TODO Auto-generated constructor stub
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
	/**
	 * Methode appelée avant de dessiner l'entitée
	 */
	public void preDraw()
	{
		
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
		System.out.println("Colision entre "+ this.getName() + " et " + entity2.getName());
	}
	@Override
	public void OnFinAnimation(Animation anim) {
		System.out.println("FinAnimationde " + this.getName());
		
	}
	
	
}
