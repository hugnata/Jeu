package es.esy.hugnata.jeuaki.listeners;

import java.util.EventListener;

import es.esy.hugnata.jeuaki.animation.Animation;

public interface AnimationListener extends EventListener{
	/**Appelé lors de la fin d'une animation
	 * 
	 */
	public void OnFinAnimation(Animation anim);

	
	
}
