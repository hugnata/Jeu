package es.esy.hugnata.jeuaki.listeners;

import es.esy.hugnata.jeuaki.animation.Animation;

import java.util.EventListener;

public interface AnimationListener extends EventListener {
    /**
     * Appelé lors de la fin d'une animation
     */
    public void OnFinAnimation(Animation anim);


}
