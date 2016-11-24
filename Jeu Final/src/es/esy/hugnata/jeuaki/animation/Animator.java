package es.esy.hugnata.jeuaki.animation;

import es.esy.hugnata.jeuaki.listeners.AnimationListener;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe de l'animator, qui va se charger du changement des animations
 */
public class Animator implements AnimationListener {
    Map<String, Animation> animations;
    String choisi;
    SpriteSheet sheet;
    AnimationListener entityListener;

    /**
     * @param sheet
     */
    public Animator(SpriteSheet sheet) {
        ChangeSpriteSheet(sheet);
    }

    public void ChangeSpriteSheet(SpriteSheet sheet) {
        this.sheet = sheet;
        animations = new HashMap<String, Animation>();
        animations.put("defaut", sheet.getAnimation("defaut", 0, 0, 1, 1));
    }

    public void addAnim(String animation, int x, int y, int nb, int delay) {
        Animation anim = sheet.getAnimation(animation, x, y, nb, delay);
        anim.addAnimationListener(this);
        anim.addAnimationListener(entityListener);
        animations.put(animation, anim);
    }

    public void SetAnimationListener(AnimationListener e) {
        this.entityListener = e;
    }

    public Image play() {
        if (choisi != "rien") {
            if (animations.containsKey(choisi)) {
                return animations.get(choisi).play();
            } else {
                return animations.get("defaut").play();
            }
        } else return null;


    }

    public void changerAnim(String anim) {
        this.choisi = anim;
    }

    @Override
    public void OnFinAnimation(Animation anim) {


    }


}
