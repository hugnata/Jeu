package es.esy.hugnata.jeuaki.Entity;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import es.esy.hugnata.jeuaki.animation.Animation;
import es.esy.hugnata.jeuaki.animation.Animator;
import es.esy.hugnata.jeuaki.animation.SpriteSheet;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.listeners.AnimationListener;
	/**Classe repr�sentant une entit�e
	 * 
	 * @author Hugnata
	 *
	 */

public class Entity implements AnimationListener{
	/**Le nom de l'entit�e
	 * 
	 */
	protected String name;
	/**La coordonn�e x de l'entit�e, � l'interieur de sa tuile
	 * 
	 */
	protected  int coordx =320;
	/**La coordonn�e y de l'entit�e, � l'interieur de sa tuile
	 * 
	 */
	protected  int coordy =320;
	/**La hauteur de l'entit�e
	 * 
	 */
	protected  int height = 0 ;
	/**La Largeur de l'entit�e
	 * 
	 */
	protected  int width = 0;

	/**L'animator de l'entit�e 
	 * 
	 */
	protected Animator animator;
	/**
	 * Boolean qui dit si oui ou non l'entit�e est charg� par la map
	 */
	protected boolean Visible = true ;
	public boolean isVisible() {
		return Visible;
	}
	public void setVisible(boolean visible) {
		Visible = visible;
	}
	/**Cr�e l'entit�e � partir d'une image, de la hauteur et de la largeur
	 * 
	 * Une entit�e est un "objet" qui va �tre plac� sur une tuile.
	 * 
	 * @see MovingEntity
	 * @see Player
	 * @see es.esy.hugnata.jeuaki.assets.objet.Interagible
	 * 
	 * @param height La Largeur de l'entit�e
	 * @param width La hauteur de l'entit�e
	 * @param animator Le fichier contenant le spritesheet de l'entit�e
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
					throw new SpriteSheetException(this,"Le spritesheet n'a pas pu �tre charg�");
		}
				catch(IndexOutOfBoundsException e1)
				{
					throw new SpriteSheetException(this,"la dimension  des sprite est superieure � la dimension de l'image !");
					
				}
			
				animator.SetAnimationListener(this);
		
		
	}
	/**Constructeur vide permet seulement de r�cuperer la methode {@link #DrawEntity} et la position du joueur*/
	public Entity() {
		// TODO Auto-generated constructor stub
	}
	/**Dessine l'entit�e sur la tuile
	 * 
	 * Methode appel�e lors du rendu de la tuile, permet de dessiner l'entit�e sur la map, en fonction des coordonn�es {@link #coordx x} et  {@link #coordy y}de l'entit�e.
	 * Dessine l'image donn�e par l'{@link #animator Animator} de l'entit�e
	 * 
	 * 
	 * @param g Graphics
	 */
	public void DrawEntity(Graphics g,int x,int y)
	{
		g.drawImage(animator.play(), getCoordx()+x, getCoordy()+y,width,height, null);
	
	}
	/**
	 * Methode appel�e avant de dessiner l'entit�e
	 */
	public void preDraw()
	{
		
	}
	/**Renvoie la coordon�e y de l'entit�e
	 * 
	 * @return coordy
	 */
	public int getCoordy() {
		return coordy;
	}
	/**Met � jour la coordon�e y de l'entit�e
	 * 
	 * @param coordy
	 */
	public void setCoordy(int coordy) {
		this.coordy = coordy;
	}
	/**Renvoie la coordon�e x de l'entit�e
	 * 
	 * @return coordx
	 */
	public int getCoordx() {
		return coordx;
	}
	/**Met � jour la coordon�e x de l'entit�e
	 * 
	 * @param coordx
	 */
	public void setCoordx(int coordx) {
		this.coordx = coordx;
	}
	/**Renvoie le nom de l'entit�e
	 * 
	 * @return  le nom de l'entit�e
	 */
	public String getName() {
		return this.name;
	}
	/**Renvoie l'animator de l'entit�e
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
