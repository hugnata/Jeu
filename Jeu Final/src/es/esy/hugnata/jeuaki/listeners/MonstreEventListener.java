package es.esy.hugnata.jeuaki.listeners;

import es.esy.hugnata.jeuaki.Entity.Monstre;

public interface MonstreEventListener extends EntityEventListener {
    void OnMort(Monstre entity);


}
