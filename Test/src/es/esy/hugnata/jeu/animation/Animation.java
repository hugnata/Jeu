package es.esy.hugnata.jeuaki.animation;

import java.awt.Image;

public class Animation {
	
	Image[] sprites;
	int nbsprite;
	int i;
	int delay;
	int i2;
	
	
	public Animation(Image[] sprites,int delay)
	{
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

}
