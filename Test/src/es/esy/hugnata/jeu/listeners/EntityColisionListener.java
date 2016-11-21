package es.esy.hugnata.jeuaki.listeners;


import java.util.EventListener;

import es.esy.hugnata.jeuaki.Entity.Entity;


public interface EntityColisionListener extends EventListener{
	void OnCollision(Entity entity,Entity entity2);
	
	

}
