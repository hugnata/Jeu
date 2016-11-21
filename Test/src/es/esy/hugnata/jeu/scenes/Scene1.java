package es.esy.hugnata.jeu.scenes;

import java.io.File;

import es.esy.hugnata.jeu.Jeu;
import es.esy.hugnata.jeu.Entity.Entity;
import es.esy.hugnata.jeu.Entity.Monstre;
import es.esy.hugnata.jeu.Entity.TextEntity;
import es.esy.hugnata.jeu.assets.Tuile;
import es.esy.hugnata.jeu.exceptions.SpriteSheetException;
import es.esy.hugnata.jeu.listeners.MonstreEventListener;

public class Scene1 implements MonstreEventListener{

	public  Scene1() throws SpriteSheetException {
		Jeu.map = new Tuile(new File("ressources/maps/1/MapEntiereG.png"), new File("ressources/maps/1/hitboxG.png"));
		Jeu.map.addEntity(Jeu.perso);
		Monstre monstre = new Monstre("Mamout",new File("ressources/monstres/vomammoth.png"),63,48,126,116,20);
		monstre.setCoordx(30);
		monstre.setCoordy(10);
		monstre.AddEventListener(this);
		Monstre tourelle = new Monstre("Tourelle",new File("ressources/monstres/Tourelle.png"),32,64,64,128,100);
		tourelle.getAnimator().addAnim("tir", 0, 0, 4, 40);
		tourelle.getAnimator().changerAnim("tir");
		tourelle.setCoordx(30);
		tourelle.setCoordy(200);
		Jeu.map.addEntity(tourelle);
		Jeu.map.addEntity(monstre);
		}

	@Override
	public void OnMort(Monstre entity) {
		System.out.println("gfdsdfghnhgfdfgh");
		Jeu.addText(new TextEntity("Tu as fini ta mission ! ",200,200,100,200));
	//	new Scene2();
	}
	
}
