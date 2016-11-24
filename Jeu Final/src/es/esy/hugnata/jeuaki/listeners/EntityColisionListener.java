package es.esy.hugnata.jeuaki.listeners;


import es.esy.hugnata.jeuaki.Entity.Entity;

import java.util.EventListener;


public interface EntityColisionListener extends EventListener {
    void OnCollision(Entity entity, Entity entity2);


}
