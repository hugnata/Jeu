package es.esy.hugnata.jeu.listeners;

import java.util.EventListener;

import es.esy.hugnata.jeu.animation.Animation;

public interface AnimationListener extends EventListener{
	/**Appel� lors de la fin d'une animation
	 * 
	 */
	public void OnFinAnimation(Animation anim);
	
}
