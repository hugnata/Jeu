package es.esy.hugnata.jeuaki.animation;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;

public class Animator {
	Map<String,Animation> animations;
	String choisi;
	SpriteSheet sheet;
	/**
	 * @param sheet
	 */
	public Animator(SpriteSheet sheet)
	{
		this.sheet = sheet;
		animations = new HashMap<String, Animation>();
		animations.put("defaut",sheet.getAnimation(0, 0, 1, 1));
		//TODO Créer une animation default au cas ou
	}
	public void addAnim(String animation,int x,int y,int nb,int delay)
	{
		animations.put(animation,sheet.getAnimation(x, y, nb, delay));
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
}
