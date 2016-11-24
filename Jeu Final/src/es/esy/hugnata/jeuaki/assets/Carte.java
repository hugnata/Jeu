package es.esy.hugnata.jeuaki.assets;

import es.esy.hugnata.jeuaki.Constant;
import es.esy.hugnata.jeuaki.Entity.Entity;
import es.esy.hugnata.jeuaki.Entity.Player;
import es.esy.hugnata.jeuaki.Entity.Projectile;
import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.assets.tuiles.Tuile;
import es.esy.hugnata.jeuaki.editeur.TuileConstructor;

import java.awt.*;
import java.io.*;
import java.util.Properties;
import java.util.Random;

/**
 * @author Hugnata
 */
public class Carte {
    /**
     * Contient le r�pertoire de la map
     */
    public static File fichier;
    /**
     * Contient le nom de la map
     */
    protected String nom;
    /**
     * Tableau qui comprend toute les tuiles de la map, sous la forme de coordonées x et y
     * <p>
     * La tuile initiale est placée en 1,1 pour permettre de charger les tuiles autour (tuile 0,0;0,1;0,2; etc)
     * sans dépasser les bornes du tableau (tuile 0,-1 est impossible, le tableau ne prend que des coordonées postives)
     * Lorsque l'on pose une tuile à droite d'une autre tuile(1,1), on ajoute 1 à x et on obtient tuiledroite(2,1)
     */
    protected Tuile[][] tuiles = null;
    /**
     * Contient  la tuile sur laquelle le joueur est presente
     */
    protected Tuile activetuile;

    //private Entity[] entities;

    /**
     * /**Cr�e une nouvelle carte en fonction des �l�ments pr�sents dans le dossier
     *
     * @param nom le nom du dossier ou se situe la carte
     * @throws IOException
     */
    public Carte(String nom) throws IOException {
        System.out.print("Chargement de la carte en cours...");
        System.out.print("...");
        tuiles = new Tuile[60][60];
        Properties prop = new Properties();
        System.out.println("...");
        this.fichier = new File("ressources/cartes/" + nom);

        InputStream input = new FileInputStream((fichier + "/" + nom + ".carte"));
        if (input == null) {
            System.err.println("Sorry, unable to find " + nom + ".carte");
            return;
        } else {
            System.out.println("Un fichier � �t� trouv�: " + nom + ".carte, chargement en cours");
            prop.load(input);
            if (!LoadConfig(prop)) {
                System.err.println("Erreur lors de l'initialisation de la carte");
            }
        }


    }

    /**
     * Va simplement r�cuperer les informations du fichier de config de la carte
     *
     * @param prop Le fichier de configuration de la carte
     * @return resultat de l'operation
     */
    private boolean LoadConfig(Properties prop) {
        this.nom = prop.getProperty("nom");
        try {
            if (LoadTuiles(fichier) == false) {
                System.err.println("Erreur lors du chargement des tuiles !");
                return false;
            }
            ;
            return true;
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }

    /**
     * Permet de charger les tuiles dans le tableau {@link #tuiles}, et de determiner si l'on peut lancer la partie
     * <p>
     * Cela requiert 3 conditions:
     * <ul><li>Qu'il existe parmis toutes les tuiles une entr�e et un donjon</li>
     * <li>Qu'il existe au moins un couloir</li>
     * </ul>
     *
     * @param fichiercarte le dossier dans lequel se trouvent les tuiles
     * @return boolean resultat de l'operation
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private boolean LoadTuiles(File fichiercarte) throws ClassNotFoundException, IOException {


        /**On r�cup�re toutes les tuiles */
        TuileConstructor[] totalutile = new TuileConstructor[20];
        int nbcouloir = 0;
        TuileConstructor entree = null;
        TuileConstructor fin = null;
        for (File fichier : fichiercarte.listFiles()) {
            if (fichier.isDirectory()) {
                for (File file : fichier.listFiles()) {
                    int i = file.getName().lastIndexOf('.');
                    if (i > 0) {
                        String f = file.getName().substring(i + 1);
                        if (file.getName().substring(i + 1).equalsIgnoreCase("tuile")) {
                            System.out.println("Chargement de la tuile " + file.getName());
                            FileInputStream f2 = new FileInputStream(file.getPath());
                            int r = 2 + 2;
                            ObjectInputStream ois = new ObjectInputStream(f2);

                            TuileConstructor tuile = (TuileConstructor) ois.readObject();
                            tuile.visible
                                    = new File(Carte.fichier.getPath() + "/" + tuile.visible.getPath());
                            tuile.hitbox = new File(Carte.fichier.getPath() + "/" + tuile.hitbox.getPath());
                            ois.close();

                            if (tuile.type.equalsIgnoreCase("couloir")) {
                                totalutile[nbcouloir] = tuile;
                                nbcouloir++;
                            }
                            if (tuile.type.equalsIgnoreCase("entree")) {
                                tuiles[Constant.DEPARTX.valeur][Constant.DEPARTY.valeur] = new Tuile(tuile);
                                activetuile = new Tuile(tuile);
                                entree = tuile;
                            }
                            if (tuile.type.equalsIgnoreCase("SalleBoss")) {
                                fin = tuile;
                            }
                        }

                    }
                }
            }
        }
        int nbcouloirs = 0;
        for (int i = 0; i < totalutile.length; i++) {
            if (totalutile[i] != null) {
                nbcouloirs++;
            }
        }
        if (tuiles[Constant.DEPARTX.valeur][Constant.DEPARTX.valeur] == null) {
            System.err.println("Aucune entr�e trouv�es, impossible de charger le donjon");
            return false;
        }
        if (fin == null) {
            System.err.println("Aucune salle de boss trouv�e, impossible de charger le donjon");
            return false;
        }
        if (nbcouloirs == 0) {
            System.err.println("Aucun Couloir trouv�, impossible de charger le donjon !");
            return false;
        }

        System.out.println(nbcouloirs + " couloirs charg�s");
        TrouverParcours(totalutile, entree, fin, 10, nbcouloirs);
        return true;
    }

    /**
     * V�rifie si 2 tuiles peuvent s'emboiter, � la mani�re d'un puzzle
     * <p>
     * V�rifie si 2 tuiles peuvent s'emboiter, � la mani�re d'un puzzle  et que si l'on place la nouvelle pi�ce, elle ne sort pas des limites du tableau {@link #tuiles}(En effet, ce tableau est limit� � des coordonn�es positives. Si l'on cr�e un cheminqui monte sans cesse, on arrivera alord � tuiles[x][y] avec y<0
     *
     * @param activetuile La tuile d�ja "pos�e"
     * @param juge        La tuile "jug�e"
     * @param x           La coordon�e x de la tuile pos�e, pour ne pas sortir des limites du tableau {@link #tuiles}
     * @param y           La coordon�e y de la tuile pos�e, pour ne pas sortir des limites du tableau {@link #tuiles}
     * @return les deux tuiles peuvent s'emboiter
     */

    public TuileConstructor[] eligible(TuileConstructor activetuile, TuileConstructor juge, int x, int y) {
        TuileConstructor[] eligible = new TuileConstructor[20];
        int nbeligible = 0;

        if (activetuile.isB() && juge.isH()) {
            if (!(y < 2 && juge.isG())) {
                eligible[nbeligible] = juge.Clone();
                eligible[nbeligible].setLink('h');
                eligible[nbeligible].setH(true);
                nbeligible++;
            }
        }
        if (activetuile.isH() && juge.isB()) {
            if (!(x < 2 && juge.isH())) {
                eligible[nbeligible] = juge.Clone();
                eligible[nbeligible].setLink('b');
                eligible[nbeligible].setB(true);
                nbeligible++;

            }
        }
        if (activetuile.isD() && juge.isG()) {
            if (!(x < 2 && juge.isH())) {
                eligible[nbeligible] = juge.Clone();
                eligible[nbeligible].setLink('g');
                eligible[nbeligible].setG(true);
                nbeligible++;

            }
        }
        if (activetuile.isG() && juge.isD()) {
            if (!(x < 2 && juge.isG())) {
                if (!(y < 2 && juge.isH())) {
                    eligible[nbeligible] = juge.Clone();
                    eligible[nbeligible].setLink('d');
                    eligible[nbeligible].setD(true);
                    nbeligible++;

                }
            }
        }


        return eligible;
    }

    /**
     * Permet de trouver un parcours de la tuileactive � la tuile fin en passant par au minimun taille couloirs et stoke le labyrinthe dans le tableau {@link #tuiles}
     *
     * @param couloirs    Les differents couloirs
     * @param activetuile La tuile de d�part
     * @param fin         La tuile de fin
     * @param taille      La taille minimale du donjon
     * @param nbcouloirs  le nombre de couloirs dans le tableau couloirs
     */
    private void TrouverParcours(TuileConstructor[] couloirs, TuileConstructor activetuile, TuileConstructor fin, int taille, int nbcouloirs) {
        Random r = new Random();
        boolean fini = false;
        int i = 0;
        int x = Constant.DEPARTX.valeur, y = Constant.DEPARTY.valeur;
        while (!fini) {
            System.out.println("n�" + i + " Nom=" + activetuile.nom + " Link=" + activetuile.link


            );

            /****Chercheur de correspondances */
            TuileConstructor[] eligibles = new TuileConstructor[nbcouloirs * 4];
            int nbeligible = 0;

            //Pour chaque tuile
            for (TuileConstructor tuile : couloirs) {


                boolean choisi = false;
                if (tuile != null) {

                    //On stocke les configuration de la tuiles qui pourraient coller avec la pi�ce jug�e
                    for (TuileConstructor jugee : eligible(activetuile, tuile, x, y)) {
                        if (jugee != null) {
                            eligibles[nbeligible] = jugee;
                            nbeligible++;
                        }

                    }


                }

            }

            int elu = r.nextInt(nbeligible);

            if (eligibles[elu].getLink() == 'b') {
                y--;
                eligibles[elu].setB(false);
                ;
                tuiles[x][y] = new Tuile(eligibles[elu]);
                activetuile = eligibles[elu];
            }
            if (eligibles[elu].getLink() == 'h') {
                y++;
                eligibles[elu].setH(false);
                ;
                tuiles[x][y] = new Tuile(eligibles[elu]);
                activetuile = eligibles[elu];
            }
            if (eligibles[elu].getLink() == 'd') {
                x--;
                eligibles[elu].setD(false);
                ;
                tuiles[x][y] = new Tuile(eligibles[elu]);
                activetuile = eligibles[elu];
            }
            if (eligibles[elu].getLink() == 'g') {
                x++;
                eligibles[elu].setG(false);
                ;
                tuiles[x][y] = new Tuile(eligibles[elu]);
                activetuile = eligibles[elu];
            }

            System.out.println("n�" + i + " Nom=" + activetuile.nom + " Link=" + activetuile.link


            );
            i++;

            if (i > taille) {
                TuileConstructor[] tuiledefin = eligible(activetuile, fin, x, y);
                int tailletableau = 0;
                for (TuileConstructor t : tuiledefin) {
                    if (t != null) tailletableau++;
                }
                if (tailletableau > 0) {


                    TuileConstructor choisie = tuiledefin[r.nextInt(tailletableau)];
                    if (choisie.getLink() == 'b') {
                        y--;
                        tuiles[x][y] = new Tuile(fin);

                    }
                    if (choisie.getLink() == 'h') {
                        y++;
                        tuiles[x][y] = new Tuile(fin);

                    }
                    if (choisie.getLink() == 'd') {
                        x--;
                        tuiles[x][y] = new Tuile(fin);

                    }
                    if (choisie.getLink() == 'g') {
                        x++;
                        tuiles[x][y] = new Tuile(fin);

                    }
                    fini = true;
                    System.out.println("n�" + i + " Nom=" + choisie.nom + " Link=" + choisie.link);
                }


            }
        }

        /****FIN Chercheur de correspondances */
        System.out.println("Donjon g�n�r�: G�n�ration des monstres");
    }


    /**
     * @return La tuile active: {@link #activetuile}
     */
    public Tuile getActiveTuile() {
        return this.activetuile;
    }

    /**
     * @param direction
     */
    public void changerTuile(int direction) {

    }

    /**
     *
     */
    public void Load() {

    }

    /**
     * @param g
     */
    public void DrawCarte(Graphics g) {


        if (tuiles[JeuAki.perso.chunkx - 1][JeuAki.perso.chunky - 1] != null) {
            tuiles[JeuAki.perso.chunkx - 1][JeuAki.perso.chunky - 1].drawTuile(g, JeuAki.perso.getCoordx() + 640 - 320, JeuAki.perso.getCoordy() + 640 - 320);
        }
        if (tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky - 1] != null) {
            tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky - 1].drawTuile(g, JeuAki.perso.getCoordx() - 320, JeuAki.perso.getCoordy() + 640 - 320);
        }
        if (tuiles[JeuAki.perso.chunkx + 1][JeuAki.perso.chunky - 1] != null) {
            tuiles[JeuAki.perso.chunkx + 1][JeuAki.perso.chunky - 1].drawTuile(g, JeuAki.perso.getCoordx() - 640 - 320, JeuAki.perso.getCoordy() + 640 - 320);
        }


        if (tuiles[JeuAki.perso.chunkx - 1][JeuAki.perso.chunky + 1] != null) {
            tuiles[JeuAki.perso.chunkx - 1][JeuAki.perso.chunky + 1].drawTuile(g, JeuAki.perso.getCoordx() + 640 - 320, JeuAki.perso.getCoordy() - 640 - 320);
        }
        if (tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky + 1] != null) {
            tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky + 1].drawTuile(g, JeuAki.perso.getCoordx() - 320, JeuAki.perso.getCoordy() - 640 - 320);
        }
        if (tuiles[JeuAki.perso.chunkx + 1][JeuAki.perso.chunky + 1] != null) {
            tuiles[JeuAki.perso.chunkx + 1][JeuAki.perso.chunky + 1].drawTuile(g, JeuAki.perso.getCoordx() - 640 - 320, JeuAki.perso.getCoordy() - 640 - 320);
        }
        if (tuiles[JeuAki.perso.chunkx - 1][JeuAki.perso.chunky] != null) {
            tuiles[JeuAki.perso.chunkx - 1][JeuAki.perso.chunky].drawTuile(g, JeuAki.perso.getCoordx() - 320 + 640, JeuAki.perso.getCoordy() - 320);
        }

        if (tuiles[JeuAki.perso.chunkx + 1][JeuAki.perso.chunky] != null) {
            tuiles[JeuAki.perso.chunkx + 1][JeuAki.perso.chunky].drawTuile(g, JeuAki.perso.getCoordx() - 640 - 320, JeuAki.perso.getCoordy() - 320);
        }
        if (tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky] != null) {
            tuiles[JeuAki.perso.chunkx][JeuAki.perso.chunky].drawTuile(g, JeuAki.perso.getCoordx() - 320, JeuAki.perso.getCoordy() - 320);
        }


    }

    /**
     *
     */
    public void ChangementTuile() {
        System.out.println("Changement de tuile x: " + JeuAki.perso.chunkx + "   y:" + JeuAki.perso.chunky);
    }

    /**
     * Ajoute une entit�e � la tuile d�sign�e par les coords X et Y du chunk de l'entit�e
     *
     * @param entity
     * @param chunkx
     * @param chunky
     */
    public void addEntity(Entity entity, int chunkx, int chunky) {
        tuiles[chunkx][chunky].addEntity(entity);
    }

    /**
     * Ajoute un projectile � la tuile d�sign�e par les coords X et Y du chunk de l'entit�e
     *
     * @param Projectile
     * @param chunkx
     * @param chunky
     */
    public void addProjectile(Projectile Projectile, int chunkx, int chunky) {
        tuiles[chunkx][chunky].addProjectile(Projectile);
    }

    /**
     * Ajoute un projectile � la tuile active
     *
     * @param projectile
     */
    public void addProjectile(Projectile projectile) {

        activetuile.addProjectile(projectile);
    }

    /**
     * @param direction
     * @param entity
     * @return si il y a collision
     */
    public boolean Collision(int direction, Player entity) {
        switch (direction) {
            //Si on monte
            case 1: {
                if (entity.getCoordy() + entity.getHeight() - entity.speed <= 0) {
                    entity.chunky--;
                    activetuile.RemoveEntity(entity);
                    activetuile = tuiles[entity.chunkx][entity.chunky];
                    activetuile.addEntity(entity);
                    ChangementTuile();
                    entity.setCoordy(639 - entity.getHeight());
                    return false;
                } else {

                    return ((activetuile.collision(entity.getCoordx(), entity.getCoordy() + entity.getHeight() - entity.speed)) || (activetuile.collision(entity.getCoordx() + entity.getWidth(), entity.getCoordy() + entity.getHeight() - entity.speed)));

                }

            }
            //Si on descend
            case 2: {
                if (entity.getCoordy() + entity.getHeight() + 1 + entity.speed >= 640) {
                    entity.chunky++;
                    activetuile.RemoveEntity(entity);
                    activetuile = tuiles[entity.chunkx][entity.chunky];
                    ChangementTuile();
                    activetuile.addEntity(entity);
                    entity.setCoordy(0 - entity.getHeight());
                    return false;
                } else {
                    return (activetuile.collision(entity.getCoordx(), entity.getCoordy() + entity.getHeight() + 1 + entity.speed) || activetuile.collision(entity.getCoordx() + entity.getWidth(), entity.getCoordy() + 1 + entity.getHeight() + entity.speed));
                }
            }
            //Si on va � gauche
            case 3: {
                if (entity.getCoordx() - entity.speed <= 0) {
                    entity.chunkx--;
                    activetuile.RemoveEntity(entity);
                    activetuile = tuiles[entity.chunkx][entity.chunky];
                    activetuile.addEntity(entity);
                    ChangementTuile();
                    entity.setCoordx(639);
                    return false;
                } else {
                    return activetuile.collision(entity.getCoordx() - entity.speed, entity.getCoordy() + entity.getHeight());
                }
            }
            //Si on va vers la droite
            case 4: {
                if (entity.getCoordx() + entity.speed >= 640) {
                    entity.chunkx++;
                    activetuile.RemoveEntity(entity);
                    activetuile = tuiles[entity.chunkx][entity.chunky];
                    activetuile.addEntity(entity);
                    ChangementTuile();
                    entity.setCoordx(entity.speed);
                } else {
                    return activetuile.collision(entity.getCoordx() + entity.getWidth() + entity.speed, entity.getCoordy() + entity.getHeight());
                }
            }


        }
        return false;
    }


}
