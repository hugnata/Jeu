package es.esy.hugnata.jeu;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JFrame;

import es.esy.hugnata.jeu.Entity.Player;
import es.esy.hugnata.jeu.Entity.TextEntity;
import es.esy.hugnata.jeu.assets.Tuile;
import es.esy.hugnata.jeu.exceptions.SpriteSheetException;
import es.esy.hugnata.jeu.scenes.Scene1;
import es.esy.hugnata.jeu.utils.KeyInput;


/**Le launcher du jeu, contenant le main
 * 
 * @author Hugnata
 *
 */
public class Jeu extends Canvas implements Runnable{

	public static final String TITLE = "test";
	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;
	private boolean running = false;
	

	public static final String version = "0.1 Rien ne marche";
	public static Tuile map;
	public static Player perso;
	private static List<TextEntity> texte = new LinkedList<TextEntity>();
	
	public Jeu()
	{
		try {
			perso = new Player();
		} catch (SpriteSheetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		addKeyListener(new KeyInput());
		try {
			new Scene1();
		} catch (SpriteSheetException e) {
			
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void render()
	{
		
	        BufferStrategy bs = getBufferStrategy();
	        if (bs == null) {
	            createBufferStrategy(3);
	            return;
	        }

	        Graphics g = bs.getDrawGraphics();
	        Graphics2D g2d = (Graphics2D) g;
	        
	        
	        ////////////////////////////////////////////////

	        g2d.setColor(new Color(0x5A2000));
	        g2d.fillRect(0, 0, WIDTH, HEIGHT);
	        
	        if(KeyInput.isDown(KeyEvent.VK_Z))
			{ 
				if(!map.Collision(1,perso))
					{
					perso.setDirection(1);
					perso.setCoordy(perso.getCoordy() - 1);
					perso.getAnimator().changerAnim("haut");;
					}
				
				
				
			}
			if(KeyInput.isDown(KeyEvent.VK_S))
			{ 
				if(!map.Collision(2,perso))
					{
					perso.setDirection(2);
					perso.setCoordy(perso.getCoordy() + 1);	
					perso.getAnimator().changerAnim("bas");;
					}
			}
			if(KeyInput.isDown(KeyEvent.VK_Q))
			{ 
				if(!map.Collision(3,perso))
					{
					perso.setDirection(3);
					perso.setCoordx(perso.getCoordx() - 1);
					perso.getAnimator().changerAnim("gauche");
					}
			
					
			}
			if(KeyInput.isDown(KeyEvent.VK_D))
			{ 
				if(!map.Collision(4,perso))
				{
					perso.setDirection(4);
					perso.setCoordx(perso.getCoordx() + 1);
					perso.getAnimator().changerAnim("droite");
				}
				
			}
			if(KeyInput.isDown(KeyEvent.VK_SPACE))
			{
				perso.getAnimator().changerAnim("tir");
			}
			map.drawTuile(g2d, perso.getCoordx()-WIDTH/2,  perso.getCoordy()-HEIGHT/2);
			perso.DrawEntity(g,WIDTH/2, HEIGHT/2);
			Iterator<TextEntity> list = texte.iterator();
			while(list.hasNext())
			{
				TextEntity t = list.next();
				t.DrawEntity(g2d,0,0);
			}
			
		
	        g.dispose();
	        bs.show();
	     
	}
	private void start()
	{
		/**Lance le jeu, permet de créer une nouvelle instance de JeuAki*/
		//lancement du jeu 
		System.out.println("Le jeu se lance !");
		if(running == true) //On vérifie qu'il ne soit pas lancé
		{
			//Si oui, on envoie un msg d'erreur
			System.err.println("Vous essayez de lancer le jeu alors qu'il est déja lancé !");
			return;
		}
		//Sinon, on lance le jeu !
		running = true;
		
		 new Thread(this,"JeuAki-Thread").start();
	}
	private void stop()
	{
		running = false;
	}
	@Override
	public void run() {
		double target = 60.0; //FPS recherché
		double nsPerTick = 1000000000.0/target; //Nano seconde par Tick. 1 tick = 1 image donc  s par tick  = 1/60
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double unprocessed = 0.0;//Temps entre 2 ticks
		int fps=0; 
		int tps = 0; //tick/sec
		boolean canRender = false;
		while(running)
		{
			long now= System.nanoTime();
			unprocessed += (now-lastTime)/nsPerTick;
			lastTime = now;
			if(unprocessed >= 1.0)
			{
				tick();
				unprocessed--;
				tps++;
				canRender = true;
			}else canRender =false;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			if(canRender)
			{
				render();
				fps++;
			}
			if(System.currentTimeMillis()-1000> timer)
			{
				timer += 1000;
				//System.out.printf("FPS: %d | TPS: %d\n",fps,tps);
				fps = 0;
				tps=0;
			}
		}
		System.exit(0);
	}
	private void tick() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args)
	{
		System.out.println("Le jeu se lance. Version: "+ version);
		Jeu jeu = new Jeu();
		
		JFrame frame = new JFrame();
		frame.add(jeu);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setTitle(TITLE);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e){
				System.err.println("Exiting Game");
				jeu.stop();
				System.exit(0);
			}
				
			
		});
		frame.setVisible(true);
		jeu.start();
	}

	public static void addText(TextEntity textEntity) {
		
		texte.add(textEntity);
	}
	public static void RemoveText(TextEntity textEntity) {
		
		texte.remove(textEntity);
	}

}
