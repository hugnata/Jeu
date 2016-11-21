package es.esy.hugnata.jeuaki;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardEndHandler;



import es.esy.hugnata.jeuaki.Entity.Entity;
import es.esy.hugnata.jeuaki.Entity.Player;
import es.esy.hugnata.jeuaki.assets.Carte;
import es.esy.hugnata.jeuaki.assets.tuiles.Tuile;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.utils.KeyInput;


/**Le launcher du jeu, contenant le main
 * 
 * @author Hugnata
 *
 */
public class JeuAki extends Canvas implements Runnable{
	
	public static final String TITLE = "JeuAki";
	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;
	public static final int TUILEDEBUTX = 10;
	public static final int TUILEDEBUTY = 10;
	public static Player perso;
	public static Entity monstre;
	private boolean running = false;
	public static final String version = "0.9 Version Tuiles auto - non Testé";
	
	public static Carte map;
	public static Tuile tuile;
	
	public JeuAki()
	{
	/*** Constructeur du jeu, crée la map et le player et initialise le KeyListener*/
		try {
			map = new Carte("jp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			perso = new Player();
		}
		catch (SpriteSheetException e) {
			
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(0);
		} 
		try {
			monstre = new Entity("test",new File("ressources/test/4.png"),512,512,50,50);
		} catch (SpriteSheetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.AddEntity(perso,TUILEDEBUTX,TUILEDEBUTY);
		map.AddEntity(monstre,TUILEDEBUTX,TUILEDEBUTY);
		
		addKeyListener(new KeyInput());
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
	private void render()
	{
		/***Dessine la map */
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		} 
		
		Graphics g = bs.getDrawGraphics();
	
		if(KeyInput.isDown(KeyEvent.VK_Z))
		{ 
			if(!map.Collision(1,perso))perso.setCoordy(perso.getCoordy() - 1);
				;
			
		}
		if(KeyInput.isDown(KeyEvent.VK_S))
		{ 
			if(!map.Collision(2,perso))perso.setCoordy(perso.getCoordy() + 1);
				;
		}
		if(KeyInput.isDown(KeyEvent.VK_Q))
		{ 
			if(!map.Collision(3,perso))perso.setCoordx(perso.getCoordx() - 1);
				
		}
		if(KeyInput.isDown(KeyEvent.VK_D))
		{ 
			if(!map.Collision(4,perso))perso.setCoordx(perso.getCoordx() + 1);
		}
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		map.DrawCarte(g);
	
		g.dispose();
		bs.show();
	}
	private void stop()
	{
		if(running==false) return;
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
		JeuAki jeu = new JeuAki();
		
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

}
