package es.esy.hugnata.jeu.listeners;

import es.esy.hugnata.jeu.Entity.Monstre;

public interface MonstreEventListener extends EntityEventListener{
	public void OnMort(Monstre entity);
		
	
}
