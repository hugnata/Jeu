package es.esy.hugnata.jeu.exceptions;

import es.esy.hugnata.jeu.Entity.Entity;




public class SpriteSheetException extends Exception{
	public SpriteSheetException()
	{
		super("Un probl�me est apparu lors de la cr�ation de l'animator");
	}
	public SpriteSheetException(String message)
	{
		super("Un probl�me est apparu lors de la cr�ation de l'animator\n    " + message);
	}
	public SpriteSheetException(Entity entity)
	{
		super("Un probl�me est apparu lors de la cr�ation de l'animator de l'entit�e "+entity.getName() );
	}
	public SpriteSheetException(Entity entity,String message)
	{
		super("Un probl�me est apparu lors de la cr�ation de l'animator de l'entit�e "+entity.getName()+"\n    " + message);

	}
		
	
}
