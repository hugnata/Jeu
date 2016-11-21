package es.esy.hugnata.jeuaki.assets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.Entity.Entity;
import es.esy.hugnata.jeuaki.Entity.Player;
import es.esy.hugnata.jeuaki.assets.tuiles.Tuile;
import es.esy.hugnata.jeuaki.editeur.TuileConstructor;

/**
 * @author Hugnata
 *
 */
public class Carte {
	/**Contient le nom de la map
	 * 
	 */
	protected String nom;
	/**Contient le répertoire de la map
	 * 
	 */
	public static File fichier;
	/**Tableau qui comprend toute les tuiles de la map, sous la forme de coordonées x et y
	 * 
	 * La tuile initiale est placée en 1,1 pour permettre de charger les tuiles autour (tuile 0,0;0,1;0,2; etc)
	 * sans dépasser les bornes du tableau (tuile 0,-1 est impossible, le tableau ne prend que des coordonées postives)
	 * Lorsque l'on pose une tuile à droite d'une autre tuile(1,1), on ajoute 1 à x et on obtient tuiledroite(2,1)
	 */
	protected Tuile[][] tuiles = null; 
	/** Contient  la tuile sur laquelle le joueur est presente
	 */
	protected Tuile activetuile;

	//private Entity[] entities;

	/**Crée une nouvelle carte en fonction des éléments présents dans le dossier
	 * 
	 * @param nom le nom du dossier ou se situe la carte
	 * @throws IOException
	 * 
	 * 
	 */
	
	 
	public Carte(String nom) throws IOException
	{
		tuiles = new Tuile[60][60];
		Properties prop = new Properties();
		this.fichier = new File("ressources/cartes/"+nom);
		InputStream input = new FileInputStream((fichier + "/"+nom+".carte"));
		if(input==null){
			System.err.println("Sorry, unable to find " + nom+".carte");
			return;
		}else
		{
			System.out.println("Un fichier à été trouvé: "+nom+".carte, chargement en cours" );
			prop.load(input);
			if(!LoadConfig(prop))
			{
				System.err.println("Erreur lors de l'initialisation de la carte");
			}
		}



	}
	/**Va simplement récuperer les informations du fichier de config de la carte
	 * 
	 * @param  prop Le fichier de configuration de la carte
	 * @return resultat de l'operation
	 */
	private boolean LoadConfig(Properties prop)
	{
		this.nom = prop.getProperty("nom");
		try {
			if(LoadTuiles(fichier)==false)
			{
				System.err.println("Erreur lors du chargement des tuiles !");
				return false;
			};
			return true;
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return false;
	}
	/**Permet de charger les tuiles dans le tableau {@link #tuiles}, et de determiner si l'on peut lancer la partie
	 *
	 * Cela requiert 3 conditions:
	 * <ul><li>Qu'il existe parmis toutes les tuiles une entrée et un donjon</li>
	 * <li>Qu'il existe au moins un couloir</li>
	 * </ul>
	 * 
	 * @param fichiercarte le dossier dans lequel se trouvent les tuiles
	 * @return boolean resultat de l'operation
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	private boolean LoadTuiles(File fichiercarte) throws ClassNotFoundException, IOException
	{

		
		/**On récupère toutes les tuiles */
		TuileConstructor[] totalutile = new TuileConstructor[20]; //Le nombre de tuiles
		int nbcouloir = 0; //Le nombre de couloirs chargés
		TuileConstructor entree = null; 
		TuileConstructor fin = null;
		for(File fichier: fichiercarte.listFiles())//Pour chaque fichier/dossier du répertoire
		{
			if(fichier.isDirectory()) //Si le fichier est un répertoire
			{
				for(File file: fichier.listFiles()) //On liste les fichiers qui le composent
				{
					int i = file.getName().lastIndexOf('.');//On récupère le dernier point de facon à situer la position de l'extension
					if (i > 0) {
						//String f = file.getName().substring(i+1);
						if(file.getName().substring(i+1).equalsIgnoreCase("tuile"))//Si l'extension est tuile
						{
						System.out.println("Chargement de la tuile " + file.getName()); 
								FileInputStream f2 = new FileInputStream(file.getPath());//On recupère le contenu du fichier
							//	int r = 2+2;
						ObjectInputStream ois = new ObjectInputStream(f2); //On en fait un object InputStream(on le désérialize)
						
						TuileConstructor tuile  = (TuileConstructor) ois.readObject();//Et on récupère l'objet
						tuile.visible =new File(Carte.fichier.getPath() + "/"+ tuile.visible.getPath()); //L'image affichée de la tuile est enregistrées
						tuile.hitbox =new File(Carte.fichier.getPath() + "/"+ tuile.hitbox.getPath());//La hitbox de la tuile est enregistrées
						ois.close();//On ferme le stream

						if(tuile.type.equalsIgnoreCase("couloir"))
						{
							totalutile[nbcouloir] = tuile;
							nbcouloir++;
						}
						if(tuile.type.equalsIgnoreCase("entree"))
						{
							tuiles[JeuAki.TUILEDEBUTX][JeuAki.TUILEDEBUTY]=new Tuile(tuile); 
							activetuile =new Tuile(tuile);
							entree = tuile;
						}
						if(tuile.type.equalsIgnoreCase("SalleBoss"))
						{
							fin = tuile;
						}
					}
				
					}
				}
			}
		}
		int nbcouloirs = 0;
		//On compte le nombre de "vrais" couloirs
		for(int i=0;i<totalutile.length;i++)
		{
			if(totalutile[i]!=null)
			{
				nbcouloirs++;
			}
		}
		if(tuiles[JeuAki.TUILEDEBUTX][JeuAki.TUILEDEBUTY]==null)//Si il n'y a pas d'entrée
		{
			System.err.println("Aucune entrée trouvées, impossible de charger le donjon");
			return false; 
		}
		if(fin==null)//Si il n'y a 
		{
			System.err.println("Aucune salle de boss trouvée, impossible de charger le donjon");
			return false;
		}
		if(nbcouloirs==0)
		{
			System.err.println("Aucun Couloir trouvé, impossible de charger le donjon !");
			return false;
		}

		System.out.println(nbcouloirs + " couloirs chargés");
		
		TrouverParcours(totalutile,entree,fin,4,nbcouloirs);
		
		
		return true;
	}
	/**Vérifie si 2 tuiles peuvent s'emboiter, à la manière d'un puzzle 
	 * 
	 * Vérifie si 2 tuiles peuvent s'emboiter, à la manière d'un puzzle  et que si l'on place la nouvelle pièce, elle ne sort pas des limites du tableau {@link #tuiles}(En effet, ce tableau est limité à des coordonnées positives. Si l'on crée un cheminqui monte sans cesse, on arrivera alord à tuiles[x][y] avec y<0
	 *   
	 * 
	 * @param activetuile La tuile déja "posée"
	 * @param juge La tuile "jugée"
	 * @param x La coordonée x de la tuile posée, pour ne pas sortir des limites du tableau {@link #tuiles}
	 * @param y La coordonée y de la tuile posée, pour ne pas sortir des limites du tableau {@link #tuiles}
	 * @return les deux tuiles peuvent s'emboiter
	 */
	
	public boolean eligible(TuileConstructor activetuile,TuileConstructor juge,int x,int y)
	{
		if(activetuile.isB()&&juge.isH())
		{
			if(!(y<2&&juge.isG()))
			{
				juge.setLink('h');
				return true;
			}
		}
		if(activetuile.isH()&&juge.isB())
		{
			if(!(x<2&&juge.isH()))
			{
				juge.setB(false);
				juge.setLink('b');
				return true;

			}
		}
		if(activetuile.isD()&&juge.isG())
		{
			if(!(x<2&&juge.isH()))
			{
				juge.setLink('g');
				return true;
			}
		}
		if(activetuile.isG()&&juge.isD())
		{
			if(!(x<2&&juge.isG()))
			{
				if(!(y<2&&juge.isH()))
				{
					juge.setLink('d');
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * @param couloirs
	 * @param activetuile
	 * @param fin
	 * @param taille
	 * @param nbcouloirs
	 */
	public void TrouverParcours(TuileConstructor[] couloirs,TuileConstructor activetuile,TuileConstructor fin,int taille,int nbcouloirs)
	{
		boolean fini = false;
		int i=0;
		int x=JeuAki.TUILEDEBUTX,y=JeuAki.TUILEDEBUTY;
		
			if(i>taille)
			{
				if(eligible(activetuile,fin,x,y))
				{
					if(fin.getLink() =='b')
					{
						y++;
						tuiles[x][y] = new Tuile(fin);
						return;
					}
					if(fin.getLink() =='h')
					{
						y--;
						tuiles[x][y] = new Tuile(fin);
						return;
					}
					if(fin.getLink() =='d')
					{
						x++;
						tuiles[x][y] = new Tuile(fin);
						return;
					}
					if(fin.getLink() =='g')
					{
						x--;
						tuiles[x][y] = new Tuile(fin);
						return;
					}
				
			}

			/****Chercheur de correspondances */
			TuileConstructor[] eligibles = new TuileConstructor[nbcouloirs];
			int nbeligible =0;

			for(TuileConstructor tuile:couloirs)
			{


				boolean choisi = false;
				if(tuile!=null)
				{
					TuileConstructor juge = tuile.Clone();
					if(eligible(activetuile,juge,x,y))
					{
						eligibles[nbeligible] = juge;
						nbeligible++;
					}

				}

			}
			Random r = new Random();
			int elu = r.nextInt(nbeligible);
			if(eligibles[elu].getLink() =='b')
			{
				y++;
				eligibles[elu].setB(false);;
				tuiles[x][y] = new Tuile(eligibles[elu]);
				activetuile = eligibles[elu];
			}
			if(eligibles[elu].getLink() =='h')
			{
				y--;
				eligibles[elu].setH(false);;
				tuiles[x][y] = new Tuile(eligibles[elu]);
				activetuile = eligibles[elu];
			}
			if(eligibles[elu].getLink() =='d')
			{
				x++;
				eligibles[elu].setD(false);;
				tuiles[x][y] = new Tuile(eligibles[elu]);
				activetuile = eligibles[elu];
			}
			if(eligibles[elu].getLink() =='g')
			{
				x--;
				eligibles[elu].setG(false);;
				tuiles[x][y] = new Tuile(eligibles[elu]);
				activetuile = eligibles[elu];
			}
			i++;



			/****FIN Chercheur de correspondances */
		}
	}

	/**
	 * @return La tuile active: {@link #activetuile}
	 */
	public Tuile getActiveTuile()
	{
		return this.activetuile;
	}
	/**
	 * @param direction
	 */
	public void changerTuile(int direction)
	{

	}

	/**
	 * 
	 */
	public void Load ()
	{

	}
	/**
	 * @param g
	 */
	public void DrawCarte(Graphics g)
	{	
	

			if(tuiles[JeuAki.perso.chunkx-1][JeuAki.perso.chunky-1]!= null)
		{
			tuiles[JeuAki.perso.chunkx-1][JeuAki.perso.chunky-1].drawTuile(g,JeuAki.perso.getCoordx()+640-320, JeuAki.perso.getCoordy()+640-320);
		}
		if(tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky-1]!= null)
		{
			tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky-1].drawTuile(g,JeuAki.perso.getCoordx()-320,JeuAki.perso.getCoordy()+640-320);
		}
		if(tuiles[JeuAki.perso.chunkx+1][JeuAki.perso.chunky-1]!= null)
		{
			tuiles[JeuAki.perso.chunkx+1][JeuAki.perso.chunky-1].drawTuile(g, JeuAki.perso.getCoordx()-640-320, JeuAki.perso.getCoordy()+640-320);
		}
		 

		if(tuiles[JeuAki.perso.chunkx-1][JeuAki.perso.chunky+1]!= null)
		{
			tuiles[JeuAki.perso.chunkx-1][JeuAki.perso.chunky+1].drawTuile(g,JeuAki.perso.getCoordx()+640-320,  JeuAki.perso.getCoordy()-640-320);
		}
		if(tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky+1]!= null)
		{
			tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky+1].drawTuile(g,JeuAki.perso.getCoordx()-320, JeuAki.perso.getCoordy()-640-320);
		}
		if(tuiles[JeuAki.perso.chunkx+1][JeuAki.perso.chunky+1]!= null)
		{
			tuiles[JeuAki.perso.chunkx+1][JeuAki.perso.chunky+1].drawTuile(g, JeuAki.perso.getCoordx()-640-320,  JeuAki.perso.getCoordy()-640-320);
		}
		if(tuiles[JeuAki.perso.chunkx-1][JeuAki.perso.chunky]!= null)
		{
			tuiles[JeuAki.perso.chunkx-1][JeuAki.perso.chunky].drawTuile(g,JeuAki.perso.getCoordx()-320+640,JeuAki.perso.getCoordy()-320);
		}

		if(tuiles[JeuAki.perso.chunkx+1][JeuAki.perso.chunky]!= null)
		{
			tuiles[JeuAki.perso.chunkx+1][JeuAki.perso.chunky].drawTuile(g, JeuAki.perso.getCoordx()-640-320,JeuAki.perso.getCoordy()-320);
		}
		if(tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky]!= null)
		{
			tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky].drawTuile(g,JeuAki.perso.getCoordx()-320 ,JeuAki.perso.getCoordy()-320);
		}



	}
	/**
	 * 
	 */
	public void ChangementTuile()
	{
		System.out.println("Changement de tuile x: "+ JeuAki.perso.chunkx + "   y:"+ JeuAki.perso.chunky);
	}
	/**Ajoute une entitée à la tuile désignée par les coords X et Y du chunk de l'entitée
	 * 
	 * @param entity
	 * 
	 * @see MovingEntity 
	 * 	 */
	public void AddEntity(Entity entity,int chunkx,int chunky)
	{
		tuiles[chunkx][chunky].addEntity(entity);
	}
	/**
	 * @param direction
	 * @param entity
	 * @return si il y a collision
	 */
	public boolean Collision(int direction,Player entity)
	{
		switch(direction)
		{
		case 1:
		{
			if(entity.getCoordy()+entity.getHeight()<=0)
			{
				entity.chunky--;
				activetuile.RemoveEntity(entity);
				activetuile = tuiles[entity.chunkx][entity.chunky];
				activetuile.addEntity(entity);
				ChangementTuile();
				entity.setCoordy(639-entity.getHeight());
				return false;
			}else
			{
				return activetuile.collision(entity.getCoordx(), entity.getCoordy() + entity.getHeight());
			}
		}
		case 2:
		{
			if(entity.getCoordy()+entity.getHeight()+1>=640)
			{
				entity.chunky++;
				activetuile.RemoveEntity(entity);
				activetuile = tuiles[entity.chunkx][entity.chunky];
				ChangementTuile();
				activetuile.addEntity(entity);
				entity.setCoordy(0-entity.getHeight());
				return false;
			}else
			{
				return activetuile.collision(entity.getCoordx(), entity.getCoordy()+1+entity.getHeight());
			}
		}
		case 3:
		{
			if(entity.getCoordx()<=0)
			{
				entity.chunkx--;
				activetuile.RemoveEntity(entity);
				activetuile = tuiles[entity.chunkx][entity.chunky];
				activetuile.addEntity(entity);
				ChangementTuile();
				entity.setCoordx(639);
				return false;
			}else
			{
				return activetuile.collision(entity.getCoordx(), entity.getCoordy()+ entity.getHeight());
			}
		}
		//se déplace vers le haut
		case 4:
		{
			if(entity.getCoordx()+1>=640)
			{
				entity.chunkx++;
				activetuile.RemoveEntity(entity);
				activetuile = tuiles[entity.chunkx][1];
				activetuile.addEntity(entity);
				ChangementTuile();
				entity.setCoordx(0);
			}else
			{
				return activetuile.collision(entity.getCoordx()+1, entity.getCoordy()+ entity.getHeight());
			}
		}



		}
		return false;
	}
}
