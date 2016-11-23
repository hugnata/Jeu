package es.esy.hugnata.jeuaki.animation;

import java.awt.Image;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import es.esy.hugnata.jeuaki.listeners.AnimationListener;

/**
 * La classe de l'animator, qui va se charger du changement des animations
 */
public class Animator implements AnimationListener{
	Map<String,Animation> animations;
	String choisi;
	SpriteSheet sheet;
	AnimationListener entityListener;
	/**
	 * @param sheet
	 */
	public Animator(SpriteSheet sheet)
	{
		ChangeSpriteSheet(sheet);
	}
	public void ChangeSpriteSheet(SpriteSheet sheet)
	{
		this.sheet = sheet;
		animations = new HashMap<String, Animation>();
		animations.put("defaut",sheet.getAnimation("defaut",0, 0, 1, 1));
	}
	public void addAnim(String animation,int x,int y,int nb,int delay)
	{
		Animation anim = sheet.getAnimation(animation,x, y, nb, delay);
		anim.addAnimationListener(this);
		anim.addAnimationListener(entityListener);
		animations.put(animation,anim);
	}
	public void SetAnimationListener(AnimationListener e)
	{
		this.entityListener = e;
	}
	public Image play()
	{
		if(animations.containsKey(choisi))
		{
			return animations.get(choisi).play();
		}else
		{
			return animations.get("defaut").play();
		}

	}
	public void changerAnim(String anim)
	{
		this.choisi = anim;
	}
	@Override
	public void OnFinAnimation(Animation anim) {
		
		
	}
	
	
}
