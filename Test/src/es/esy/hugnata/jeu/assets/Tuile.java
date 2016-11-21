package es.esy.hugnata.jeu.assets;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;

import es.esy.hugnata.jeu.Entity.Entity;
import es.esy.hugnata.jeu.Entity.Projectile;




/**Représente une Tuile sur la carte
 * 
 * @author Hugnata
 *
 */
public class Tuile{
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
	private List<Projectile> projectiles;
	
	private EventListenerList listeners = new EventListenerList();
	/**Constructeur de la classe, on construit la tuile sur la base d'un {@link TuileConstructor}
	 * 
	 * @param tuileconstructor  Le constructeur de la tuile
	 */
	public Tuile(File visible,File hitbox)
	{
			try {
			img =ImageIO.read(visible);
			bimg =ImageIO.read(hitbox);
			entities = new ArrayList<Entity>();
		projectiles = new ArrayList<Projectile>();
			} catch (IOException e) {
				
				e.printStackTrace();
			}	
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
		g.drawImage(img,-x,-y, img.getWidth(null), img.getHeight(null), null);
		
		Entity[] entity = new Entity[entities.size()];
		for(int i=0;i<entities.size();i++)
		{
			
			Entity en = entities.get(i);
			en.preDraw();
			en.DrawEntity(g,-x,-y);
			if(!en.isVisible())
			{
				entity[i] = en;
			}
		}
		Entity[] projectilemort = new Entity[projectiles.size()];
		for(int i=0;i<projectiles.size();i++)
		{
			
			Entity en = projectiles.get(i);
			en.preDraw();
			en.DrawEntity(g,-x,-y);
			if(!en.isVisible())
			{
 				projectilemort[i] = en;
			}
		} 
		checkEntityColision();
		removeEntities(entity);
		removeProjectile(projectilemort);
	}
	private void removeEntities(Entity[] entity) {
		for(int i=0;i<entity.length;i++)
		{
			if(entity[i]!=null)
			{
				this.entities.remove(entity[i]);
			}
			
		}
		
	}
	private void removeProjectile(Entity[] entity) {
		for(int i=0;i<entity.length;i++)
		{
			if(entity[i]!=null)
			{
				this.projectiles.remove(entity[i]);
			}
			
		}
		
	}
	private void checkEntityColision(){
		
		ListIterator<Entity> li = entities.listIterator();
		
		while(li.hasNext())
		{
			Entity entity =	li.next();
			ListIterator<Projectile> ll = projectiles.listIterator();
			while(ll.hasNext())
				{
				
				Entity entity2 = ll.next();
				if(entity != entity2)
				{
						if (entity.getCoordx() < entity2.getCoordx() + entity2.getWidth() &&
						entity.getCoordx() + entity.getWidth() > entity2.getCoordx() &&
					   entity.getCoordy() < entity2.getCoordy() + entity2.getHeight() &&
					   entity.getCoordy() + entity.getHeight() > entity2.getCoordy()) {
							
							entity.OnCollision(entity2);
							entity2.OnCollision(entity);
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
		this.entities.add(entity);	
	
	}
	/**Permet d'ajouter un projectile à la carte
	 * 
	 * @param projectile
	 */
	public void addProjectile(Projectile projectile)
	{
		this.projectiles.add(projectile);
	}
	private void removeProjectiles(Projectile[] projectile) {
		for(int i=0;i<projectile.length;i++)
		{
			if(projectile[i]!=null)
			{
				this.entities.remove(projectile[i]);
			}
			
		}
	}	
	public boolean collision(int x, int y)
	{
		try{
			if(((((bimg.getRGB(x,y)))>>24)&255)==0)
			{
			return true;
			}
		return false;
		}catch(ArrayIndexOutOfBoundsException e)
		{ 
			e.getMessage();
			return true;
		}
		
	}
	public boolean Collision(int direction,Entity entity)
	{
		switch(direction)
		{
		case 1:
		{
		return (this.collision(entity.getCoordx(), entity.getCoordy())||this.collision(entity.getCoordx()+entity.getWidth(), entity.getCoordy()));
		}
		case 2:
		{
		return (this.collision(entity.getCoordx(), entity.getCoordy()+1+entity.getHeight())||this.collision(entity.getCoordx()+entity.getWidth(), entity.getCoordy()+1+entity.getHeight()));
		}
		case 3:
		{
		
		return this.collision(entity.getCoordx(), entity.getCoordy()+ entity.getHeight());
		}
		case 4:
		{
		return this.collision(entity.getCoordx()+entity.getWidth(), entity.getCoordy()+ entity.getHeight());
		}



		}
		return false;
	}
	
	
}
