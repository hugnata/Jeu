package es.esy.hugnata.jeuaki.assets.tuiles;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;

import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.Entity.Entity;
import es.esy.hugnata.jeuaki.Entity.Projectile;
import es.esy.hugnata.jeuaki.editeur.TuileConstructor;
import es.esy.hugnata.jeuaki.listeners.EntityColisionListener;

/**Représente une Tuile sur la carte
 * 
 * @author Hugnata
 *
 */
public class Tuile implements EntityColisionListener {
	/**La hitbox de l'image
	 * 
	 */
	private  BufferedImage bimg; 
	/**L'image de la tuile
	 * 
	 */
	private  Image img;
	/**Les entitées présentes dans la tuile
	 * 
	 */
	private List<Entity> entities;
	/**Les projectiles présentes dans la tuile
	 * 
	 */
	private List<Projectile> projectiles;
	/**Le listener de collision d'entitée
	 * 
	 */
	 private final EventListenerList listeners = new EventListenerList();
	
	/**Constructeur de la classe, on construit la tuile sur la base d'un {@link TuileConstructor}
	 * 
	 * @param tuileconstructor  Le constructeur de la tuile
	 */
	public Tuile(TuileConstructor tuileconstructor)
	{
			try {
			img =ImageIO.read(tuileconstructor.visible);
			bimg =ImageIO.read(tuileconstructor.hitbox);
			entities = new ArrayList<Entity>();
			projectiles = new ArrayList<Projectile>();
			} catch (IOException e) {
				
				e.printStackTrace();
			}	
	}
	private void addEntityListener(EntityColisionListener listener)
	{
		listeners.add(EntityColisionListener.class,listener);
	}
	private void removeEntityListener(EntityColisionListener listener)
	{
		listeners.remove(EntityColisionListener.class,listener);
	}
	
	/***Permet de dessiner la tuile en fonction des coordoonnées x,y de la carte
	 * 
	 * 
	 * Permet de dessiner la tuile en fonction des coordoonnées x,y de la carte, fonction automatiquement appelée lors de {@link es.esy.hugnata.jeuaki.assets.Carte#DrawCarte(Graphics)}
	 * Dessine aussi les {@link es.esy.hugnata.jeuaki.Entity.MovingEntity} présente dans le  {@link #entities}
	 * 
	 * @param  g 
	 * @param  x Coordonnée x de la tuile
	 * @param  y Coordonnée y de la tuile
	 */
	public void drawTuile(Graphics g,int x,int y)
	{
		g.drawImage(img,-x,-y, 640, 640, null);
		for(int i=0;i<entities.size();i++)
		{
			checkEntityColision();
			Entity en = entities.get(i);
			en.DrawEntity(g,-x,-y);
		}
		
	}
	private void checkEntityColision() {
		
		ListIterator<Entity> li = entities.listIterator();
		ListIterator<Entity> ll = entities.listIterator();
		while(li.hasNext())
		{
			Entity entity =	li.next();
			while(ll.hasNext())
				{
				
				Entity entity2 = ll.next();
				if(entity != entity2)
				{
						if (entity.getCoordx() < entity2.getCoordx() + entity2.getWidth() &&
						entity.getCoordx() + entity.getWidth() > entity2.getCoordx() &&
					   entity.getCoordy() < entity2.getCoordy() + entity2.getHeight() &&
					   entity.getCoordy() + entity.getCoordy() > entity2.getCoordy()) {
							for(EntityColisionListener Colisionl : listeners.getListeners(EntityColisionListener.class))
							{
								Colisionl.OnCollision(entity,entity2);
							}
						}
					}
				
				}
		}
	}
	/***Permet d'ajouter une {@link Entity} à la tuile
	 * 
	 * @param entity Entitée à ajouter
	 */
	public void addEntity(Entity entity)
	{	
		addEntityListener(this);
		this.entities.add(entity);
		
	}
	/**Permet de retirer une {@link Entity} de la Tuile
	 * 
	 * @param entity Entitée à virer
	 */
	public void RemoveEntity(Entity entity)
	{
		removeEntityListener(this);
		this.entities.remove(entity);
	}
	/***Permet d'ajouter une {@link Projectile} à la tuile
	 * 
	 * @param Projectile Entitée à ajouter
	 */
	public void addProjectile(Projectile Projectile)
	{	
		addEntityListener(this);
		this.entities.add(Projectile);
		
	}
	/**Permet de retirer une {@link Projectile} de la Tuile
	 * 
	 * @param Projectile Entitée à virer
	 */
	public void RemoveProjectile(Projectile Projectile)
	{
		removeEntityListener(this);
		this.entities.remove(Projectile);
	}
	public boolean collision(int x,int y)
	{
			if(x<0)x=0;
			if(y<0)y=0;
			if(x>=640)x=639;
			if(y>=640)y=639;
			
			if((((bimg.getRGB(x,y))>>24)&255)==0)
			{
			return true;
			}
		return false;
	}
	@Override
	public void OnCollision(Entity entity, Entity entity2) {
		//System.out.println("bonjour");
		
	}

	
	
	
}
