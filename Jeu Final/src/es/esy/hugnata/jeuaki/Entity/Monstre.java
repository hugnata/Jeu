package es.esy.hugnata.jeuaki.Entity;

import es.esy.hugnata.jeuaki.JeuAki;
import es.esy.hugnata.jeuaki.animation.Animation;
import es.esy.hugnata.jeuaki.exceptions.SpriteSheetException;
import es.esy.hugnata.jeuaki.listeners.MonstreEventListener;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.io.File;
//import es.esy.hugnata.jeuaki.listeners.MonstreEventListener;


public class Monstre extends Entity {
    /**
     * Sa vitesse
     */
    public final int speed = 25;
    /**
     * Sa vie de départ, qui ne peut pas être modifiée
     */
    final int healthdepart;
    /**
     * La vie du monstre
     */
    int health;
    EventListenerList listeners = new EventListenerList();

    /**
     * Le constructeur du monstre , identique à celui de l'{@link Entity} + la santée du monstre
     *
     * @param nom
     * @param spritesheet
     * @param animwidth
     * @param animheight
     * @param height
     * @param width
     * @param health
     * @throws SpriteSheetException
     */
    public Monstre(String nom, File spritesheet, int animwidth, int animheight,
                   int width, int height, int health, int speed) throws SpriteSheetException {

        super(nom, spritesheet, animwidth, animheight, width, height);
        System.out.println("Cr�ation d'un nouveau monstre: " + nom);
        this.health = health;
        this.healthdepart = health;
    }

    public void BaisserVie(int vie) {
        health -= vie;
        if (health <= 0) {
            this.Visible = false;
            animator.changerAnim("rien");
            for (MonstreEventListener listener : listeners.getListeners(MonstreEventListener.class)) {
                listener.OnMort(this);

            }
        }
    }

    public void DrawEntity(Graphics g, int x, int y) {

        g.setColor(Color.black);
        g.drawRect(getCoordx() + x, getCoordy() + y - 30, 100, 20);
        g.setColor(Color.red);
        g.fillRect(getCoordx() + x - 1, getCoordy() - 29 + y, (int) Math.floor((100 * health) / healthdepart), 18);
        g.drawImage(animator.play(), getCoordx() + x, getCoordy() + y, width, height, null);
        //TODO REMOVE HITBOX
        g.setColor(Color.RED);
        g.drawRect(getCoordx() + x, getCoordy() + y, width, height);


    }

    public void AddEventListener(MonstreEventListener l) {
        listeners.add(MonstreEventListener.class, l);
    }

    public void RemoveEventListener(MonstreEventListener l) {
        listeners.remove(MonstreEventListener.class, l);
    }

    @Override
    public void OnFinAnimation(Animation anim) {
        if (anim.getName().equalsIgnoreCase("tir")) {
            Tirer();
            this.getAnimator().changerAnim("tir");
        }


    }

    private void Tirer() {
        try {
            JeuAki.map.addProjectile(new Projectile(this, new File("ressources/divers/balleE.png"), 32, 32, 64, 64, 1, 100, 10, 1));
            JeuAki.map.addProjectile(new Projectile(this, new File("ressources/divers/balleE.png"), 32, 32, 64, 64, 1, 100, 10, 2));
            JeuAki.map.addProjectile(new Projectile(this, new File("ressources/divers/balleE.png"), 32, 32, 64, 64, 1, 100, 10, 3));
            JeuAki.map.addProjectile(new Projectile(this, new File("ressources/divers/balleE.png"), 32, 32, 64, 64, 1, 100, 10, 4));
        } catch (SpriteSheetException e) {
            e.printStackTrace();
        }

    }
}
