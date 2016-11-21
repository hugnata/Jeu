package es.esy.hugnata.jeu.exceptions;

import es.esy.hugnata.jeu.Entity.Entity;




public class SpriteSheetException extends Exception{
	public SpriteSheetException()
	{
		super("Un problème est apparu lors de la création de l'animator");
	}
	public SpriteSheetException(String message)
	{
		super("Un problème est apparu lors de la création de l'animator\n    " + message);
	}
	public SpriteSheetException(Entity entity)
	{
		super("Un problème est apparu lors de la création de l'animator de l'entitée "+entity.getName() );
	}
	public SpriteSheetException(Entity entity,String message)
	{
		super("Un problème est apparu lors de la création de l'animator de l'entitée "+entity.getName()+"\n    " + message);

	}
		
	
}
