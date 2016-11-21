package es.esy.hugnata.jeu.animation;

import java.awt.Image;


import javax.swing.event.EventListenerList;

import es.esy.hugnata.jeu.listeners.AnimationListener;

public class Animation {
	EventListenerList listeners = new EventListenerList();
	Image[] sprites;
	String name;
	int nbsprite;
	int i;
	int delay;
	int i2;
	
	
	public Animation(String name, Image[] sprites,int delay)
	{
		this.name = name;
		this.sprites = sprites;
		nbsprite = sprites.length;
		if(delay != 0)
		{
			this.delay = delay;
		}else
		{
			delay=1;
		}
		i2 = 0;
	}
	public Image play()
	{
		
		if(i2>=nbsprite-1)
		{
			for(AnimationListener listener: listeners.getListeners(AnimationListener.class) )
			{
				listener.OnFinAnimation(this);
			}
			i2=0;
			return sprites[i2];
			
		}else
		{
			if(i<delay)
			{		i++;
					return sprites[i2];
					
			}else
			{
				i=0;
				i2++;
				return sprites[i2];
			}
			
		
			
		}
			
	}
	public void addAnimationListener(AnimationListener listener)
	{
		listeners.add(AnimationListener.class, listener);
	}
	public void removeAnimationListener(AnimationListener listener)
	{
		listeners.remove(AnimationListener.class, listener);
	}
	public String getName() {
		return name;
	}

}
