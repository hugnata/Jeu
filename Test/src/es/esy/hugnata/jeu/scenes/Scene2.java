package es.esy.hugnata.jeu.scenes;

import java.io.File;

import es.esy.hugnata.jeu.Jeu;
import es.esy.hugnata.jeu.Entity.Monstre;
import es.esy.hugnata.jeu.assets.Tuile;
import es.esy.hugnata.jeu.exceptions.SpriteSheetException;

public class Scene2 {
	public Scene2()
	{
		Jeu.map = new Tuile(new File("ressources/maps/2/map2.png"),new File("ressources/maps/2/map2h.png"));
		Monstre monstre = null;
		try {
			monstre = new Monstre("Mouton",new File("ressources/monstres/Mouton.png"),64,64,100,100,250);
		} catch (SpriteSheetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monstre.setCoordx(700);
		monstre.setCoordy(600);
		Jeu.map.addEntity(monstre);
	}
}
